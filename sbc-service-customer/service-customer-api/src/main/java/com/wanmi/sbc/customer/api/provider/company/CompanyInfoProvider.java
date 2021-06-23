package com.wanmi.sbc.customer.api.provider.company;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.company.*;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoAddResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoModifyResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInformationModifyResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyTypeModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 公司信息-公司信息添加/修改/删除API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CompanyInfoProvider")
public interface CompanyInfoProvider {


    /**
     * 新增公司信息
     *
     * @param request 新增公司信息request {@link CompanyInfoAddRequest}
     * @return 公司信息 {@link CompanyInfoAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/add-company-info")
    BaseResponse<CompanyInfoAddResponse> addCompanyInfo(@RequestBody CompanyInfoAddRequest request);

    /**
     * 修改公司信息
     *
     * @param request 修改公司信息request {@link CompanyInfoModifyRequest}
     * @return 公司信息 {@link CompanyInfoModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/modify-company-info")
    BaseResponse<CompanyInfoModifyResponse> modifyCompanyInfo(@RequestBody CompanyInfoModifyRequest request);

    /**
     * 修改公司工商信息
     *
     * @param request 修改公司信息request {@link CompanyInformationSaveRequest}
     * @return 公司信息 {@link CompanyInformationSaveRequest}
     */
    @PostMapping("/customer/${application.customer.version}/company/modify-company-information")
    BaseResponse<CompanyInformationModifyResponse> modifyCompanyInformation(@RequestBody
                                                                                    CompanyInformationSaveRequest request);

    /**
     * 修改公司信息类型
     *
     * @param request 修改公司信息类型request {@link CompanyTypeRequest}
     * @return 公司信息 {@link CompanyInformationSaveRequest}
     */
    @PostMapping("/customer/${application.customer.version}/company/modify-company-type")
    BaseResponse<CompanyTypeModifyResponse> modifyCompanyType(@RequestBody CompanyTypeRequest request);

    /**
     * 修改全量公司信息
     *
     * @param request 修改公司信息request{@link CompanyInfoAllModifyRequest}
     * @return 修改公司信息结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/modify-all-company-info")
    BaseResponse modifyAllCompanyInfo(@RequestBody CompanyInfoAllModifyRequest request);
}
