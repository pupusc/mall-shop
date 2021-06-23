package com.wanmi.sbc.customer.api.provider.account;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.account.*;
import com.wanmi.sbc.customer.api.response.account.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员银行账户-查询API
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:28
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerAccountQueryProvider")
public interface CustomerAccountQueryProvider {

    /**
     * 根据会员ID查询银行账户信息
     *
     * @param customerAccountListRequest {@link CustomerAccountListRequest}
     * @return 会员银行账户信息 {@link CustomerAccountListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/list-by-customer-id")
    BaseResponse<CustomerAccountListResponse> listByCustomerId(@RequestBody @Valid
                                                                       CustomerAccountListRequest
                                                                       customerAccountListRequest);

    /**
     * 根据会员银行账号ID查询银行账户信息（仅包含未删除）
     *
     * @param customerAccountOptionalRequest {@link CustomerAccountOptionalRequest}
     * @return 会员银行账户信息 {@link CustomerAccountOptionalResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/get-by-customer-account-id-and-del-flag")
    BaseResponse<CustomerAccountOptionalResponse> getByCustomerAccountIdAndDelFlag(@RequestBody @Valid
                                                                                           CustomerAccountOptionalRequest customerAccountOptionalRequest);

    /**
     * 根据会员银行账号ID查询银行账户信息（包含已删除和未删除）
     *
     * @param customerAccountRequest {@link CustomerAccountRequest}
     * @return 会员银行账户信息 {@link CustomerAccountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/get-by-customer-account-id")
    BaseResponse<CustomerAccountResponse> getByCustomerAccountId(@RequestBody @Valid CustomerAccountRequest
                                                                         customerAccountRequest);

    /**
     * 根据会员银行账号查询银行账户信息（仅包含未删除）
     *
     * @param customerAccountQueryRequest {@link CustomerAccountByCustomerAccountNoRequest}
     * @return 会员银行账户信息 {@link CustomerAccountByCustomerAccountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/get-by-customer-account-no-and-del-flag")
    BaseResponse<CustomerAccountByCustomerAccountResponse> getByCustomerAccountNoAndDelFlag(
            @RequestBody @Valid CustomerAccountByCustomerAccountNoRequest customerAccountQueryRequest);

    /**
     * 根据会员ID查询银行账号总数
     *
     * @param customerAccountByCustomerIdRequest {@link CustomerAccountByCustomerIdRequest}
     * @return 会员银行账户总数 {@link CustomerAccountByCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/account/count-by-customer-id")
    BaseResponse<CustomerAccountByCustomerIdResponse> countByCustomerId(@RequestBody @Valid
                                                                                CustomerAccountByCustomerIdRequest
                                                                                customerAccountByCustomerIdRequest);
}
