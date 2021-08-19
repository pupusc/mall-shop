package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.loginregister.*;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerByAccountResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.request.BalancePayPasswordRequest;
import com.wanmi.sbc.customer.validGroups.NotCustomerAccount;
import com.wanmi.sbc.customer.validGroups.NotCustomerId;
import com.wanmi.sbc.customer.validGroups.NotPayPassword;
import com.wanmi.sbc.customer.validGroups.NotVerify;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by feitingting on 2019/3/1.
 */
@RestController
@Slf4j
@Api(tags = "CustomerBalanceBaseController", description = "余额提现相关")
public class CustomerBalanceBaseController {
    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private CustomerSiteProvider customerSiteProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CustomerSiteQueryProvider customerSiteQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * H5 - 设置支付密码或忘记支付密码时发送验证码
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "H5 - 设置支付密码或忘记支付密码时发送验证码")
    @RequestMapping(value = "/balancePayPassword", method = RequestMethod.POST)
    public BaseResponse balancePayPassword(@Validated({NotCustomerAccount.class}) @RequestBody BalancePayPasswordRequest
                                                   request) {
        return this.sendVerifyCode(request.getCustomerAccount(), request.getIsForgetPassword(),
                CacheKeyConstant.FIND_BALANCE_PAY_PASSWORD, CacheKeyConstant.BALANCE_PAY_PASSWORD);
    }

    /**
     * H5 - 设置支付密码--验证验证码
     *
     * @param balancePayPasswordRequest
     * @return
     */
    @ApiOperation(value = "H5 - 设置支付秘密时验证,进入下一步")
    @RequestMapping(value = "/validatePayPassword", method = RequestMethod.POST)
    public BaseResponse<String> validatePayCode(@Validated({NotCustomerAccount.class, NotVerify.class})
                                                @RequestBody BalancePayPasswordRequest balancePayPasswordRequest) {
        return this.validatePayPassword(balancePayPasswordRequest.getCustomerAccount(), balancePayPasswordRequest.getVerifyCode(),
                balancePayPasswordRequest.getIsForgetPassword(), CacheKeyConstant.FIND_BALANCE_PAY_PASSWORD, CacheKeyConstant.BALANCE_PAY_PASSWORD);
    }

    /**
     * H5 - 设置/忘记支付密码
     *
     * @param balancePayPasswordRequest
     * @return
     */
    @ApiOperation(value = "H5 - 设置/忘记支付密码")
    @RequestMapping(value = "/payPasswordByForgot", method = RequestMethod.POST)
    public BaseResponse passwordByForgot(@Validated({NotCustomerId.class, NotPayPassword.class, NotVerify.class})
                                         @RequestBody BalancePayPasswordRequest balancePayPasswordRequest) {

        return this.setPayPassword(balancePayPasswordRequest.getCustomerId(), balancePayPasswordRequest.getVerifyCode(), balancePayPasswordRequest.getCustomerPayPassword(),
                balancePayPasswordRequest.getIsForgetPassword(), CacheKeyConstant.FIND_BALANCE_PAY_PASSWORD, CacheKeyConstant.BALANCE_PAY_PASSWORD);
    }

    /**
     * APP - 设置支付密码或忘记支付密码时发送验证码
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "APP - 设置支付密码或忘记支付密码时发送验证码")
    @RequestMapping(value = "/app/verify/code", method = RequestMethod.POST)
    public BaseResponse sendPayVerifyCode(@Validated({NotCustomerAccount.class}) @RequestBody BalancePayPasswordRequest
                                                  request) {
        return this.sendVerifyCode(request.getCustomerAccount(), request.getIsForgetPassword(),
                CacheKeyConstant.YZM_APP_FIND_BALANCE_PAY_PASSWORD, CacheKeyConstant.YZM_APP_BALANCE_PAY_PASSWORD);
    }

    /**
     * APP - 设置支付秘密时验证,进入下一步
     *
     * @param balancePayPasswordRequest
     * @return
     */
    @ApiOperation(value = "APP - 设置支付秘密时验证,进入下一步")
    @RequestMapping(value = "/app/validate/code", method = RequestMethod.POST)
    public BaseResponse<String> validateCode(@Validated({NotCustomerAccount.class, NotVerify.class})
                                             @RequestBody BalancePayPasswordRequest balancePayPasswordRequest) {
        return this.validatePayPassword(balancePayPasswordRequest.getCustomerAccount(), balancePayPasswordRequest.getVerifyCode(),
                balancePayPasswordRequest.getIsForgetPassword(), CacheKeyConstant.YZM_APP_FIND_BALANCE_PAY_PASSWORD, CacheKeyConstant.YZM_APP_BALANCE_PAY_PASSWORD);
    }

    /**
     * APP - 设置/忘记支付密码
     *
     * @param balancePayPasswordRequest
     * @return
     */
    @ApiOperation(value = "APP - 设置/忘记支付密码")
    @RequestMapping(value = "/app/pay/pwd", method = RequestMethod.POST)
    public BaseResponse setPayPassword(@Validated({NotCustomerId.class, NotPayPassword.class, NotVerify.class})
                                       @RequestBody BalancePayPasswordRequest balancePayPasswordRequest) {

        return this.setPayPassword(balancePayPasswordRequest.getCustomerId(), balancePayPasswordRequest.getVerifyCode(), balancePayPasswordRequest.getCustomerPayPassword(),
                balancePayPasswordRequest.getIsForgetPassword(), CacheKeyConstant.YZM_APP_FIND_BALANCE_PAY_PASSWORD, CacheKeyConstant.YZM_APP_BALANCE_PAY_PASSWORD);
    }


    /**
     * 设置密码或忘记密码发送验证码
     *
     * @param customerAccount 发送验证码手机号
     * @param isForgetPwd     是否是忘记密码需求
     * @param findPassword    忘记密码的Redis key 前缀
     * @param setPassword     设置支付密码的Redis key 前缀
     * @return
     */
    private BaseResponse sendVerifyCode(String customerAccount, Boolean isForgetPwd, String findPassword, String setPassword) {
        CustomerValidateSendMobileCodeRequest customerValidateSendMobileCodeRequest = new CustomerValidateSendMobileCodeRequest();
        customerValidateSendMobileCodeRequest.setMobile(customerAccount);
        //是否可以发送
        if (!customerSiteProvider.validateSendMobileCode(customerValidateSendMobileCodeRequest).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        CustomerByAccountRequest customerByAccountRequest = new CustomerByAccountRequest();
        customerByAccountRequest.setCustomerAccount(customerAccount);
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(customerByAccountRequest);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (response == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        CustomerDetailVO customerDetail = this.findAnyCustomerDetailByCustomerId(response.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(customerAccount);
        customerSendMobileCodeRequest.setRedisKey(Objects.nonNull(isForgetPwd) && isForgetPwd ?
                findPassword : setPassword);
        customerSendMobileCodeRequest.setSmsTemplate(Objects.nonNull(isForgetPwd) && isForgetPwd ?
                SmsTemplate.FIND_BALANCE_PAY_PASSWORD : SmsTemplate.BALANCE_PAY_PASSWORD);
        //发送验证码
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 验证发送的验证码
     *
     * @param customerAccount 发送验证吗的手机号
     * @param verifyCode      前端传入的验证码
     * @param isForgetPwd     是否是忘记密码需求
     * @param findPassword    忘记支付密码Redis key前缀
     * @param setPassword     设置支付密码Redis key前缀
     * @return
     */
    private BaseResponse<String> validatePayPassword(String customerAccount, String verifyCode, Boolean isForgetPwd, String findPassword, String setPassword) {
        //验证验证码
        String t_verifyCode = redisService.getString(Objects.nonNull(isForgetPwd) && isForgetPwd ? findPassword.concat(customerAccount) : setPassword.concat(customerAccount));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(verifyCode))) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(customerAccount);
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (Objects.isNull(response)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        CustomerDetailVO customerDetail = findAnyCustomerDetailByCustomerId(response.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        return BaseResponse.success(response.getCustomerId());
    }

    /**
     * 验证完验证码后，设置或修改支付密码
     *
     * @param customerId   客户ID
     * @param verifyCode   前端传入的验证码
     * @param payPassword  支付密码
     * @param isForgetPwd  是否是忘记密码需求
     * @param findPassword 忘记支付密码Redis key前缀
     * @param setPassword  设置支付密码Redis key前缀
     * @return
     */
    private BaseResponse setPayPassword(String customerId, String verifyCode, String payPassword, Boolean isForgetPwd, String findPassword, String setPassword) {
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        if (customer == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        CustomerDetailVO customerDetail = this.findAnyCustomerDetailByCustomerId(customerId);
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //验证验证码
        String t_verifyCode = redisService.getString(Objects.nonNull(isForgetPwd) && isForgetPwd ?
                findPassword.concat(customer.getCustomerAccount()) : setPassword.concat(customer.getCustomerAccount()));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(verifyCode))) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }
        customer.setCustomerPayPassword(payPassword);
        CustomerModifyRequest customerModifyRequest = new CustomerModifyRequest();
        KsBeanUtil.copyPropertiesThird(customer, customerModifyRequest);
        customerSiteProvider.modifyCustomerPayPwd(customerModifyRequest);
        //删除验证码缓存
        redisService.delete(Objects.nonNull(isForgetPwd) && isForgetPwd ?
                findPassword.concat(customer.getCustomerAccount()) : setPassword.concat(customer.getCustomerAccount()));
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 根据会员获取
     *
     * @param customerId
     * @return
     */
    private CustomerDetailVO findAnyCustomerDetailByCustomerId(String customerId) {
        return customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest.builder().customerId(customerId).build()).getContext();
    }

    /**
     * 校验会员支付密码是否可用
     * 是否存在 账户是否被冻结
     *
     * @return
     */
    @ApiOperation(value = "校验会员支付密码是否可用")
    @RequestMapping(value = "/isPayPwdValid", method = RequestMethod.POST)
    public BaseResponse isPayPwdValid() {
        CustomerGetByIdResponse customerGetByIdResponse = customerQueryProvider.getCustomerById(new
                CustomerGetByIdRequest(commonUtil.getCustomer().getCustomerId())).getContext();
        if (StringUtils.isBlank(customerGetByIdResponse.getCustomerPayPassword())) {
            throw new SbcRuntimeException(CustomerErrorCode.NO_CUSTOMER_PAY_PASSWORD);
        }
        if (customerGetByIdResponse.getPayErrorTime() != null && customerGetByIdResponse.getPayErrorTime() == 3) {
            Duration duration = Duration.between(customerGetByIdResponse.getPayLockTime(), LocalDateTime.now());
            if (duration.toMinutes() < 30) {
                //支付密码输错三次，并且锁定时间还未超过30分钟，返回账户冻结错误信息
                throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_PAY_LOCK_TIME_ERROR, new Object[]{30 - duration.toMinutes()});
            }
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校验会员输入支付密码是否正确
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "校验会员输入支付密码是否正确")
    @RequestMapping(value = "/checkCustomerPayPwd", method = RequestMethod.POST)
    public BaseResponse checkCustomerPayPwd(@RequestBody @Valid CustomerCheckPayPasswordRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        return customerSiteProvider.checkCustomerPayPwd(request);
    }
}
