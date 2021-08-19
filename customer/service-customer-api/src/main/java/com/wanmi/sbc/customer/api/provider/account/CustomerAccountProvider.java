package com.wanmi.sbc.customer.api.provider.account;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.account.*;
import com.wanmi.sbc.customer.api.response.account.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员银行账户-新增/修改/删除API
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:28
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerAccountProvider")
public interface CustomerAccountProvider {

    /**
     * 新增会员银行账户
     *
     * @param customerAccountAddRequest {@link CustomerAccountAddRequest}
     * @return 会员银行账户新增结果 {@link CustomerAccountAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/add")
    BaseResponse<CustomerAccountAddResponse> add(@RequestBody @Valid CustomerAccountAddRequest
                                                         customerAccountAddRequest);

    /**
     * boss端修改会员银行账户
     *
     * @param customerAccountModifyRequest  {@link CustomerAccountModifyRequest}
     * @return 会员银行账户修改结果 {@link CustomerAccountModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/modify")
    BaseResponse<CustomerAccountModifyResponse> modify(@RequestBody @Valid
                                                               CustomerAccountModifyRequest
                                                               customerAccountModifyRequest);

    /**
     * 客户端修改会员银行账户
     *
     * @param customerAccountModifyByCustomerIdRequest {@link CustomerAccountModifyByCustomerIdRequest}
     * @return 会员银行账户修改结果 {@link CustomerAccountModifyByCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/modify-by-customer-id")
    BaseResponse<CustomerAccountModifyByCustomerIdResponse> modifyByCustomerId(@RequestBody @Valid
                                                                                       CustomerAccountModifyByCustomerIdRequest customerAccountModifyByCustomerIdRequest);


    /**
     * 根据会员银行账号ID和EmployeeID删除会员银行账号信息
     *
     * @param customerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest  {@link CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest}
     * @return 会员银行账户删除结果 {@link CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/delete-by-customer-account-id-and-employee-id")
    BaseResponse<CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdResponse>
    deleteByCustomerAccountIdAndEmployeeId(@RequestBody @Valid
                                                                                                                               CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest customerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest);


    /**
     * 根据会员银行账号ID和会员ID删除会员银行账号信息
     *
     * @param customerAccountDeleteByCustomerAccountIdAndCustomerIdRequest {@link CustomerAccountDeleteByCustomerAccountIdAndCustomerIdRequest}
     * @return 会员银行账户删除结果 {@link CustomerAccountDeleteByCustomerAccountIdAndCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/delete-by-customer-account-id-and-customer-id")
    BaseResponse<CustomerAccountDeleteByCustomerAccountIdAndCustomerIdResponse>
    deleteCustomerAccountByCustomerAccountIdAndCustomerId(@RequestBody @Valid
                                                                                                                                              CustomerAccountDeleteByCustomerAccountIdAndCustomerIdRequest customerAccountDeleteByCustomerAccountIdAndCustomerIdRequest);
}
