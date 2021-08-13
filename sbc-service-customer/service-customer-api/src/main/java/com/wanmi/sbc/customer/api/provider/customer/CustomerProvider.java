package com.wanmi.sbc.customer.api.provider.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.CustomerAccountModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerAddRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerCheckStateModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerEnterpriseCheckStateModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerFandengModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerSalesManModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerToDistributorModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomersDeleteRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerAccountModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerAddResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerCheckStateModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomersDeleteResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(value = "${application.customer.name}", contextId = "CustomerProvider")
public interface CustomerProvider {
    /**
     * 审核客户状态
     *
     * @param request {@link CustomerCheckStateModifyRequest}
     * @return {@link CustomerCheckStateModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-customer-check-state")
    BaseResponse<CustomerCheckStateModifyResponse> modifyCustomerCheckState(@RequestBody @Valid
                                                                                    CustomerCheckStateModifyRequest request);
    /**
     * 审核企业会员
     *
     * @param request {@link CustomerEnterpriseCheckStateModifyRequest}
     * @return {@link CustomerEnterpriseCheckStateModifyRequest}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-enterprise-check-state")
    BaseResponse<CustomerCheckStateModifyResponse> checkEnterpriseCustomer(@RequestBody @Valid
                                                                                     CustomerEnterpriseCheckStateModifyRequest request);

    /**
     * boss批量删除会员
     * 删除会员
     * 删除会员详情表
     *
     * @param request {@link CustomersDeleteRequest}
     * @return {@link CustomersDeleteResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/delete-customers")
    BaseResponse<CustomersDeleteResponse> deleteCustomers(@RequestBody @Valid CustomersDeleteRequest request);

    /**
     * 新增客户共通
     *
     * @param request {@link CustomerAddRequest}
     * @return {@link CustomerAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/save-customer")
    BaseResponse<CustomerAddResponse> saveCustomer(@RequestBody @Valid CustomerAddRequest request);

    /**
     * Boss端修改会员
     *
     * @param request {@link CustomerModifyRequest}
     * @return  {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-customer")
    BaseResponse<CustomerModifyResponse> modifyCustomer(@RequestBody @Valid CustomerModifyRequest request);

    /**
     * 修改绑定手机号
     *
     * @param request {@link CustomerAccountModifyRequest}
     * @return {@link CustomerAccountModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-customer-account")
    BaseResponse<CustomerAccountModifyResponse> modifyCustomerAccount(@RequestBody @Valid CustomerAccountModifyRequest request);

    /**
     * 修改绑定樊登id
     *
     * @param request {@link CustomerAccountModifyRequest}
     * @return {@link CustomerAccountModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-customer-fandeng-time")
    BaseResponse<CustomerAccountModifyResponse> modifyCustomerFanDengIdTime(@RequestBody @Valid CustomerFandengModifyRequest request);

    /**
     * 修改已有的业务员
     *
     * @param request {@link CustomerSalesManModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-customer-sales-man")
    BaseResponse modifyCustomerSalesMan(@RequestBody @Valid CustomerSalesManModifyRequest request);


    /**
     * 审核客户状态
     *
     * @param request {@link CustomerCheckStateModifyRequest}
     * @return {@link CustomerCheckStateModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customer/modify-to-distributor")
    BaseResponse modifyToDistributor(@RequestBody @Valid CustomerToDistributorModifyRequest request);
    /**
     * 查询是否是知识顾问
     *
     * @param userId
     * @return BaseResponse
     */
    @PostMapping("/customer/${application.customer.version}/customer/is-counselor")
    BaseResponse<CounselorDto> isCounselor(@RequestParam Integer userId);
}
