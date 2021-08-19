package com.wanmi.sbc.customer.api.provider.loginregister;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.loginregister.*;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerByAccountResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerDetailByAccountResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerLoginResponse;
import com.wanmi.sbc.customer.api.response.loginregister.ThirdLoginAndBindResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员-登录/查询/第三方快捷登录API
 *
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:28
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerSiteQueryProvider")
public interface CustomerSiteQueryProvider {

    /**
     * 会员登录
     *
     * @param customerLoginRequest {@link CustomerLoginRequest}
     * @return 会员信息 {@link CustomerLoginResponse}
     */
    @PostMapping("/customer/${application.customer.version}/login")
    BaseResponse<CustomerLoginResponse> login(@RequestBody @Valid CustomerLoginRequest customerLoginRequest);

    /**
     * 根据会员账号查询会员信息
     *
     * @param customerByAccountRequest {@link CustomerByAccountRequest}
     * @return 会员信息 {@link CustomerByAccountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/get-customer-by-customer-account")
    BaseResponse<CustomerByAccountResponse> getCustomerByCustomerAccount(@RequestBody @Valid CustomerByAccountRequest
                                                                                 customerByAccountRequest);

    /**
     * 根据会员账号查询会员详情信息
     *
     * @param customerDetailByAccountRequest {@link CustomerDetailByAccountRequest}
     * @return 会员详情信息 {@link CustomerDetailByAccountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/get-customer-detail-by-customer-account")
    BaseResponse<CustomerDetailByAccountResponse> getCustomerDetailByCustomerAccount(@RequestBody @Valid
                                                                                             CustomerDetailByAccountRequest customerDetailByAccountRequest);

    /**
     * 第三方快捷登录并绑定账号信息
     *
     * @param customerLoginAndBindThirdAccountRequest {@link CustomerLoginAndBindThirdAccountRequest}
     * @return 会员信息 {@link ThirdLoginAndBindResponse}
     */
    @PostMapping("/customer/${application.customer.version}/login-and-bind-third-account")
    BaseResponse<ThirdLoginAndBindResponse> loginAndBindThirdAccount(@RequestBody @Valid
                                                                             CustomerLoginAndBindThirdAccountRequest
                                                                             customerLoginAndBindThirdAccountRequest);

    /**
     * 注册客户前判断客户状态，是否存在
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/check-by-account")
    BaseResponse checkByAccount(@RequestBody @Valid CustomerCheckByAccountRequest request);
}
