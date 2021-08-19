package com.wanmi.sbc.account.provider.impl.invoice;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectByIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectListByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectPageByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectQueryRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectByIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectPageByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectQueryResponse;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectListVO;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import com.wanmi.sbc.account.invoice.InvoiceProject;
import com.wanmi.sbc.account.invoice.InvoiceProjectService;
import com.wanmi.sbc.account.invoice.InvoiceResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/10/22 9:30
 * @Version: 1.0
 */
@RestController
@Validated
public class InvoiceProjectQueryController implements InvoiceProjectQueryProvider {

    @Autowired
    private InvoiceProjectService invoiceProjectService;

    /**
     * 根据开票项目名称和公司ID查询该项目是否存在
     * @param invoiceProjectQueryRequest
     * @return
     */
    @Override
    public BaseResponse<InvoiceProjectQueryResponse> queryByProjectNameAndCompanyInfoId(@RequestBody @Valid InvoiceProjectQueryRequest invoiceProjectQueryRequest) {
        Boolean result = invoiceProjectService.invoiceProjectIsExist(invoiceProjectQueryRequest.getProjectName(),invoiceProjectQueryRequest.getCompanyInfoId());
        return BaseResponse.success(new InvoiceProjectQueryResponse(result));
    }


    /**
     * 根据id查询开票项目
     * @param invoiceProjectByIdRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectByIdResponse> getById(@RequestBody @Valid InvoiceProjectByIdRequest invoiceProjectByIdRequest) {
        InvoiceProject invoiceProject = invoiceProjectService.findInvoiceProjectById(invoiceProjectByIdRequest.getProjcetId()).orElse(null);
        if (Objects.isNull(invoiceProject)) {
            return BaseResponse.success(new InvoiceProjectByIdResponse());
        }
        InvoiceProjectByIdResponse invoiceProjectByIdResponse = new InvoiceProjectByIdResponse();
        KsBeanUtil.copyPropertiesThird(invoiceProject,invoiceProjectByIdResponse);
        return BaseResponse.success(invoiceProjectByIdResponse);
    }

    /**
     * 根据公司ID分页查询开票项目
     * @param invoiceProjectPageByCompanyInfoIdRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectPageByCompanyInfoIdResponse> pageByCompanyInfoId(@RequestBody @Valid InvoiceProjectPageByCompanyInfoIdRequest invoiceProjectPageByCompanyInfoIdRequest) {
        Page<InvoiceProject> invoiceProjectPage = invoiceProjectService.findByPage(invoiceProjectPageByCompanyInfoIdRequest.getBaseQueryRequest(),invoiceProjectPageByCompanyInfoIdRequest.getCompanyInfoId());
        if (Objects.isNull(invoiceProjectPage)) {
            return BaseResponse.success(new InvoiceProjectPageByCompanyInfoIdResponse());
        }
        MicroServicePage<InvoiceProjectVO> invoiceProjectVOMicroServicePage = KsBeanUtil.convertPage(invoiceProjectPage,InvoiceProjectVO.class);
        return BaseResponse.success(new InvoiceProjectPageByCompanyInfoIdResponse(invoiceProjectVOMicroServicePage));
    }

    /**
     * 根据公司ID查询所有开票项
     * @param invoiceProjectListByCompanyInfoIdRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectListByCompanyInfoIdResponse> listByCompanyInfoId(@RequestBody @Valid InvoiceProjectListByCompanyInfoIdRequest invoiceProjectListByCompanyInfoIdRequest) {
        List<InvoiceResponse> invoiceResponseList = invoiceProjectService.findAllInoviceProject(invoiceProjectListByCompanyInfoIdRequest.getCompanyInfoId());
        if (CollectionUtils.isEmpty(invoiceResponseList)) {
            return BaseResponse.success(new InvoiceProjectListByCompanyInfoIdResponse());
        }
        List<InvoiceProjectListVO> invoiceProjectListDTOList = KsBeanUtil.convert(invoiceResponseList,InvoiceProjectListVO.class);
        return BaseResponse.success(new InvoiceProjectListByCompanyInfoIdResponse(invoiceProjectListDTOList));
    }
}
