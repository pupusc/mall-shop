package com.wanmi.sbc.customer.api.provider.loginregister;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.loginregister.*;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerConsummateRegisterResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerRegisterResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerSendMobileCodeResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerValidateSendMobileCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员-注册/修改/发送验证码/绑定第三方账号API
 *
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:28
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerSiteProvider")
public interface CustomerSiteProvider {

    /**
     * 会员注册
     *
     * @param customerRegisterRequest {@link CustomerRegisterRequest}
     * @return 会员注册结果 {@link CustomerRegisterResponse}
     */
    @PostMapping("/customer/${application.customer.version}/register")
    BaseResponse<CustomerRegisterResponse> register(@RequestBody @Valid CustomerRegisterRequest customerRegisterRequest);

    /**
     * 企业会员注册
     *
     * @param customerRegisterRequest {@link CustomerRegisterRequest}
     * @return 企业会员注册结果 {@link CustomerRegisterResponse}
     */
    @PostMapping("/customer/${application.customer.version}/register-enterprise")
    BaseResponse<CustomerRegisterResponse> registerEnterprise(@RequestBody @Valid CustomerRegisterRequest customerRegisterRequest);

    /**
     * 企业会员驳回后重新注册
     *
     * @param customerRegisterRequest {@link CustomerRegisterRequest}
     * @return 企业会员注册结果 {@link CustomerRegisterResponse}
     */
    @PostMapping("/customer/${application.customer.version}/register-enterprise-again")
    BaseResponse<CustomerRegisterResponse> registerEnterpriseAgain(@RequestBody @Valid CustomerRegisterRequest customerRegisterRequest);

    /**
     * 完善会员注册信息
     *
     * @param customerConsummateRegisterRequest {@link CustomerConsummateRegisterRequest}
     * @return 会员信息 {@link CustomerConsummateRegisterResponse}
     */
    @PostMapping("/customer/${application.customer.version}/register-consummate")
    BaseResponse<CustomerConsummateRegisterResponse> registerConsummate(@RequestBody @Valid
                                                                                CustomerConsummateRegisterRequest
                                                                                customerConsummateRegisterRequest);


    /**
     * 修改密码
     *
     * @param customerModifyRequest {@link CustomerModifyRequest}
     * @return 会员修改密码结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/modify-customer-pwd")
    BaseResponse modifyCustomerPwd(@RequestBody @Valid CustomerModifyRequest customerModifyRequest);

    /**
     * 发送手机验证码
     *
     * @param customerSendMobileCodeRequest {@link CustomerSendMobileCodeRequest}
     * @return 发送手机验证码结果 {@link CustomerSendMobileCodeResponse}
     */
    @PostMapping("/customer/${application.customer.version}/send-mobile-code")
    BaseResponse<CustomerSendMobileCodeResponse> sendMobileCode(@RequestBody @Valid CustomerSendMobileCodeRequest customerSendMobileCodeRequest);

    /**
     * 是否可以发送验证码
     *
     * @param customerValidateSendMobileCodeRequest {@link CustomerValidateSendMobileCodeRequest}
     * @return 是否可以发送验证码 {@link CustomerValidateSendMobileCodeResponse}
     */
    @PostMapping("/customer/${application.customer.version}/validate-send-mobile-code")
    BaseResponse<CustomerValidateSendMobileCodeResponse> validateSendMobileCode(@RequestBody @Valid
                                                                                        CustomerValidateSendMobileCodeRequest
                                                                                        customerValidateSendMobileCodeRequest);


    /**
     * 绑定第三方账号信息
     *
     * @param customerBindThirdAccountRequest {@link CustomerBindThirdAccountRequest}
     * @return 绑定第三方账号信息 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/bind-third-account")
    BaseResponse bindThirdAccount(@RequestBody @Valid CustomerBindThirdAccountRequest customerBindThirdAccountRequest);


    @PostMapping("/customer/${application.customer.version}/modify-customer-pay-pwd")
    BaseResponse modifyCustomerPayPwd(@RequestBody @Valid CustomerModifyRequest customerModifyRequest);

    /**
     * 校验会员输入支付密码是否正确
     * @param customerCheckPayPasswordRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/check-customer-pay-pwd")
    BaseResponse checkCustomerPayPwd(@RequestBody @Valid CustomerCheckPayPasswordRequest customerCheckPayPasswordRequest);


    @PostMapping("/customer/${application.customer.version}/upate-info")
    BaseResponse updateInfo(@RequestBody @Valid CustomerBindThirdAccountRequest customerBindThirdAccountRequest);
}
