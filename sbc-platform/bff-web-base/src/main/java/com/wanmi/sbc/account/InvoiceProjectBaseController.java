package com.wanmi.sbc.account;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectQueryProvider;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectListByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchListSupportByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchListSupportByCompanyInfoIdResponse;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectListVO;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchSupportVO;
import com.wanmi.sbc.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 开票项目管理
 * Created by chenli on 2017/7/24.
 */
@RestController
@RequestMapping("/account")
@Api(tags = "InvoiceProjectBaseController", description = "S2B web公用-开票项目管理API")
public class InvoiceProjectBaseController {

    @Autowired
    private InvoiceProjectQueryProvider invoiceProjectQueryProvider;

    @Autowired
    private InvoiceProjectSwitchQueryProvider invoiceProjectSwitchQueryProvider;

    /**
     * 根据公司ID查询商家所有开票项
     * @return List<InvoiceResponse>
     */
    @ApiOperation(value = "根据公司ID查询商家所有开票项")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "companyInfoId", value = "商家公司ID", required = true)
    @RequestMapping(value = "/invoiceProjects/{companyInfoId}", method = RequestMethod.GET)
    public BaseResponse<List<InvoiceProjectListVO>> findAllInvoiceProject(@PathVariable("companyInfoId") Long companyInfoId) {
        InvoiceProjectListByCompanyInfoIdRequest request = new InvoiceProjectListByCompanyInfoIdRequest();
        request.setCompanyInfoId(companyInfoId);
        BaseResponse<InvoiceProjectListByCompanyInfoIdResponse> baseResponse = invoiceProjectQueryProvider.listByCompanyInfoId(request);
        InvoiceProjectListByCompanyInfoIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)){
            return BaseResponse.error("暂无开票项目");
        }
        List<InvoiceProjectListVO> invoiceProjectListVOList = response.getInvoiceProjectListDTOList();
        if(CollectionUtils.isEmpty(invoiceProjectListVOList)){
            return BaseResponse.error("暂无开票项目");
        }
        return BaseResponse.success(invoiceProjectListVOList);
    }

    /**
     * 根据商家id集合，查询商家是否支持开票
     * @return Boolean
     */
    @ApiOperation(value = "根据商家id集合，查询商家是否支持开票")
    @RequestMapping(value = "/invoice/switch", method = RequestMethod.POST)
    public BaseResponse<List<InvoiceProjectSwitchSupportVO>> queryProjectSwitchByIds(@RequestBody InvoiceProjectSwitchListSupportByCompanyInfoIdRequest switchRequest) {
        BaseResponse<InvoiceProjectSwitchListSupportByCompanyInfoIdResponse> baseResponse = invoiceProjectSwitchQueryProvider.listSupportByCompanyInfoId(switchRequest);
        InvoiceProjectSwitchListSupportByCompanyInfoIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)){
            return BaseResponse.error("查询失败，稍后重试");
        }
        return BaseResponse.success(response.getInvoiceProjectSwitchSupportVOList());
    }
}
