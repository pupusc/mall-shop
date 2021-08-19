package com.wanmi.sbc.customer.provider.impl.company;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoProvider;
import com.wanmi.sbc.customer.api.request.company.*;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoAddResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoModifyResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInformationModifyResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyTypeModifyResponse;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.company.service.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>公司信息接口实现</p>
 * Created by of628-wenzhi on 2018-08-18-下午4:41.
 */
@RestController
@Validated
public class CompanyInfoController implements CompanyInfoProvider {

    @Autowired
    private CompanyInfoService companyInfoService;

    @Override

    public BaseResponse<CompanyInfoAddResponse> addCompanyInfo(@RequestBody CompanyInfoAddRequest request) {
        CompanyInfoSaveRequest saveRequest = new CompanyInfoSaveRequest();
        KsBeanUtil.copyPropertiesThird(request, saveRequest);
        CompanyInfo companyInfo = companyInfoService.saveCompanyInfo(saveRequest);
        CompanyInfoAddResponse response = new CompanyInfoAddResponse();
        KsBeanUtil.copyPropertiesThird(companyInfo, response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyInfoModifyResponse> modifyCompanyInfo(@RequestBody CompanyInfoModifyRequest request) {
        CompanyInfoSaveRequest saveRequest = new CompanyInfoSaveRequest();
        KsBeanUtil.copyPropertiesThird(request, saveRequest);
        CompanyInfo companyInfo = companyInfoService.updateCompanyInfo(saveRequest);
        CompanyInfoModifyResponse response = new CompanyInfoModifyResponse();
        KsBeanUtil.copyPropertiesThird(companyInfo, response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyInformationModifyResponse> modifyCompanyInformation(@RequestBody CompanyInformationSaveRequest request) {
        CompanyInfo companyInfo = companyInfoService.updateCompanyInformation(request);
        CompanyInformationModifyResponse response = new CompanyInformationModifyResponse();
        KsBeanUtil.copyPropertiesThird(companyInfo, response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyTypeModifyResponse> modifyCompanyType(@RequestBody CompanyTypeRequest request) {
        CompanyInfo companyInfo = companyInfoService.updateCompanyType(request);
        CompanyTypeModifyResponse response = new CompanyTypeModifyResponse();
        KsBeanUtil.copyPropertiesThird(companyInfo, response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse modifyAllCompanyInfo(@RequestBody CompanyInfoAllModifyRequest request) {
        companyInfoService.modifyAllCompanyInfo(request);
        return BaseResponse.SUCCESSFUL();
    }
}
