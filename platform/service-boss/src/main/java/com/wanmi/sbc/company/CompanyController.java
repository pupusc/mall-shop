package com.wanmi.sbc.company;

import com.wanmi.sbc.account.api.provider.company.CompanyAccountProvider;
import com.wanmi.sbc.account.api.provider.company.CompanyAccountQueryProvider;
import com.wanmi.sbc.account.api.request.company.CompanyAccountByCompanyInfoIdAndDefaultFlagRequest;
import com.wanmi.sbc.account.api.request.company.CompanyAccountFindByAccountIdRequest;
import com.wanmi.sbc.account.api.request.company.CompanyAccountRemitRequest;
import com.wanmi.sbc.account.api.response.company.CompanyAccountFindResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoProvider;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyAccountPageRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInformationSaveRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoByIdResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInformationModifyResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyAccountVO;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationQueryProvider;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyAccountQueryRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyPageRequest;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyAccountResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyInfoResponse;
import com.wanmi.sbc.elastic.bean.vo.companyAccount.EsCompanyAccountVO;
import com.wanmi.sbc.elastic.bean.vo.storeInformation.EsCompanyInfoVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 商家
 * Created by sunkun on 2017/11/6.
 */
@RestController
@RequestMapping("/company")
@Api(tags = "CompanyController", description = "S2B 平台端-商家管理API")
public class CompanyController {

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private CompanyInfoProvider companyInfoProvider;

    @Autowired
    private CompanyAccountQueryProvider companyAccountQueryProvider;

    @Autowired
    private CompanyAccountProvider companyAccountProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsStoreInformationQueryProvider esStoreInformationQueryProvider;

    /**
     * 商家列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "查询商家列表")
    @PostMapping(value = "/list")
    public BaseResponse<Page<EsCompanyInfoVO>> list(@RequestBody EsCompanyPageRequest request) {
        request.setDeleteFlag(DeleteFlag.NO);
        request.setIsMasterAccount(NumberUtils.INTEGER_ONE);
        BaseResponse<EsCompanyInfoResponse> response = esStoreInformationQueryProvider.companyInfoPage(request);

        MicroServicePage<EsCompanyInfoVO> page = null;
        if(Objects.nonNull(response.getContext())){
            page = response.getContext().getEsCompanyAccountPage();
        }
        return BaseResponse.success(page);
    }

    /**
     * 工商信息修改
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改商家工商信息")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse<CompanyInformationModifyResponse> update(@Valid @RequestBody CompanyInformationSaveRequest request) {
        operateLogMQUtil.convertAndSend("设置","店铺信息","编辑店铺信息");
        return companyInfoProvider.modifyCompanyInformation(request);
    }

    /**
     * 查询公司信息
     *
     * @return
     */
    @ApiOperation(value = "查询商家公司信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "商家公司id", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BaseResponse<CompanyInfoResponse> findOne(@PathVariable Long id) {
        if (Objects.isNull(id)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CompanyInfoResponse response = new CompanyInfoResponse();
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(id).build()
        ).getContext();
        KsBeanUtil.copyPropertiesThird(companyInfo, response);
        return BaseResponse.success(response);
    }


    /**
     * 商家列表(收款账户)
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询商家公司收款账户")
    @RequestMapping(value = "old/account", method = RequestMethod.POST)
    public BaseResponse<Page<CompanyAccountVO>> accountList(@RequestBody CompanyAccountPageRequest request) {
        request.setDeleteFlag(DeleteFlag.NO);
        return BaseResponse.success(companyInfoQueryProvider.pageCompanyAccount(request).getContext()
                .getCompanyAccountVOPage());
    }


    /**
     * 商家列表(收款账户) 从es搜索
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询商家公司收款账户")
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public BaseResponse accountListFromEs(@RequestBody EsCompanyAccountQueryRequest request) {

        request.setCompanyInfoDelFlag(DeleteFlag.NO);
        request.setStoreDelFlag(DeleteFlag.NO);
        request.setEmployeeDelFlag(DeleteFlag.NO);
        request.setIsMasterAccount(NumberUtils.INTEGER_ONE);
        BaseResponse<EsCompanyAccountResponse> response = esStoreInformationQueryProvider.companyAccountPage(request);

        MicroServicePage<EsCompanyAccountVO> page = null;
        if(Objects.nonNull(response.getContext())){
             page = response.getContext().getEsCompanyAccountPage();
        }
        return BaseResponse.success(page);
    }


    /**
     * 商家账号明细
     *
     * @param companyInfoId
     * @return
     */
    @ApiOperation(value = "根据商家公司id查询商家收款账户列表")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "companyInfoId", value = "商家公司id", required = true)
    @RequestMapping(value = "/account/detail/{companyInfoId}", method = RequestMethod.GET)
    public BaseResponse<List<com.wanmi.sbc.account.bean.vo.CompanyAccountVO>> accountDetail(@PathVariable Long companyInfoId) {
        return BaseResponse.success(companyAccountQueryProvider.listByCompanyInfoIdAndDefaultFlag(
                CompanyAccountByCompanyInfoIdAndDefaultFlagRequest.builder()
                        .companyInfoId(companyInfoId).defaultFlag(DefaultFlag.NO).build()
        ).getContext().getCompanyAccountVOList());
    }


    /**
     * 商家账号打款
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "商家账号打款")
    @RequestMapping(value = "/account/remit", method = RequestMethod.PUT)
    public BaseResponse accountRemit(@RequestBody CompanyAccountRemitRequest request) {
        //操作日志记录
        CompanyAccountFindResponse findResponse =
                companyAccountQueryProvider.getByAccountId(new CompanyAccountFindByAccountIdRequest(request.getAccountId())).getContext();
        if (Objects.nonNull(findResponse)){
            CompanyInfoByIdResponse response =
                    companyInfoQueryProvider.getCompanyInfoById(new CompanyInfoByIdRequest(findResponse.getCompanyInfoId())).getContext();
            operateLogMQUtil.convertAndSend("财务", "确认打款", "确认打款：商家编号" + (Objects.nonNull(response) ?
                    response.getCompanyCode() : ""));
        }

        return companyAccountProvider.remit(request);
    }

}
