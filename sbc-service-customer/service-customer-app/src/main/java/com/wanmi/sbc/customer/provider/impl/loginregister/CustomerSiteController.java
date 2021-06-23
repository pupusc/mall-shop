package com.wanmi.sbc.customer.provider.impl.loginregister;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteProvider;
import com.wanmi.sbc.customer.api.request.loginregister.*;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerConsummateRegisterResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerRegisterResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerSendMobileCodeResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerValidateSendMobileCodeResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.service.CustomerSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 14:40
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerSiteController implements CustomerSiteProvider {

    @Autowired
    private CustomerSiteService customerSiteService;


    /**
     * 会员注册
     *
     * @param customerRegisterRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerRegisterResponse> register(@RequestBody @Valid CustomerRegisterRequest customerRegisterRequest) {
        CustomerDTO customerDTO = customerRegisterRequest.getCustomerDTO();
        Customer customer = KsBeanUtil.convert(customerDTO, Customer.class);
        Customer customerRegister = customerSiteService.register(customer,customerRegisterRequest.getEmployeeId());
        if (Objects.isNull(customerRegister)){
            return BaseResponse.SUCCESSFUL();
        }
        CustomerRegisterResponse customerRegisterResponse =  KsBeanUtil.convert(customerRegister,CustomerRegisterResponse.class);
        return BaseResponse.success(customerRegisterResponse);
    }

    /**
     * 企业会员注册
     *
     * @param customerRegisterRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerRegisterResponse> registerEnterprise(@RequestBody @Valid CustomerRegisterRequest customerRegisterRequest) {
        CustomerDTO customerDTO = customerRegisterRequest.getCustomerDTO();
        Customer customer = KsBeanUtil.convert(customerDTO, Customer.class);
        Customer customerRegister = customerSiteService.registerEnterprise(customer,customerRegisterRequest);
        if (Objects.isNull(customerRegister)){
            return BaseResponse.SUCCESSFUL();
        }
        CustomerRegisterResponse customerRegisterResponse =  KsBeanUtil.convert(customerRegister,CustomerRegisterResponse.class);
        return BaseResponse.success(customerRegisterResponse);
    }

    /**
     * 企业会员审核被驳回后重新注册
     *
     * @param request
     * @return
     */

    @Override
    public BaseResponse<CustomerRegisterResponse> registerEnterpriseAgain(@RequestBody @Valid CustomerRegisterRequest request) {
        Customer customer = customerSiteService.registerEnterpriseAgain(request);
        CustomerRegisterResponse customerRegisterResponse =  KsBeanUtil.convert(customer,CustomerRegisterResponse.class);
        return BaseResponse.success(customerRegisterResponse);
    }

    /**
     * 完善会员注册信息
     *
     * @param customerConsummateRegisterRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerConsummateRegisterResponse> registerConsummate(@RequestBody @Valid CustomerConsummateRegisterRequest customerConsummateRegisterRequest) {
        CustomerDetail customerDetail = new CustomerDetail();
        KsBeanUtil.copyPropertiesThird(customerConsummateRegisterRequest,customerDetail);
        Customer perfectCustomer = customerSiteService.perfectCustomer(customerDetail);
        return BaseResponse.success(KsBeanUtil.convert(perfectCustomer,CustomerConsummateRegisterResponse.class));
    }

    /**
     * 修改密码
     *
     * @param customerModifyRequest
     * @return
     */

    @Override
    public BaseResponse modifyCustomerPwd(@RequestBody @Valid CustomerModifyRequest customerModifyRequest) {
        Customer customer = new Customer();
        KsBeanUtil.copyPropertiesThird(customerModifyRequest,customer);
        customer.setStoreCustomerRelaListByAll(null);
        customerSiteService.editPassword(customer);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发送手机验证码
     *
     * @param customerSendMobileCodeRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerSendMobileCodeResponse> sendMobileCode(@RequestBody @Valid CustomerSendMobileCodeRequest customerSendMobileCodeRequest) {
        Integer result = customerSiteService.doMobileSms(customerSendMobileCodeRequest.getRedisKey(),customerSendMobileCodeRequest.getMobile(),customerSendMobileCodeRequest.getSmsTemplate());
        return BaseResponse.success(new CustomerSendMobileCodeResponse(result));
    }

    /**
     * 是否可以发送验证码
     *
     * @param customerValidateSendMobileCodeRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerValidateSendMobileCodeResponse> validateSendMobileCode(@RequestBody @Valid CustomerValidateSendMobileCodeRequest customerValidateSendMobileCodeRequest) {
        Boolean result = customerSiteService.isSendSms(customerValidateSendMobileCodeRequest.getMobile());
        return BaseResponse.success(new CustomerValidateSendMobileCodeResponse(result));
    }

    /**
     * 绑定第三方账号信息
     *
     * @param customerBindThirdAccountRequest
     * @return
     */

    @Override
    public BaseResponse bindThirdAccount(@RequestBody @Valid CustomerBindThirdAccountRequest customerBindThirdAccountRequest) {
        ThirdLoginRelation thirdLoginRelation = new ThirdLoginRelation();
        KsBeanUtil.copyPropertiesThird(customerBindThirdAccountRequest,thirdLoginRelation);
        customerSiteService.bindThird(thirdLoginRelation);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改会员支付密码
     * @param customerModifyRequest
     * @return
     */

    public BaseResponse modifyCustomerPayPwd(@RequestBody @Valid CustomerModifyRequest customerModifyRequest) {
        Customer customer = new Customer();
        KsBeanUtil.copyPropertiesThird(customerModifyRequest,customer);
        customer.setStoreCustomerRelaListByAll(null);
        customerSiteService.editPayPassword(customer);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校验会员输入支付密码是否正确
     * @param customerCheckPayPasswordRequest
     * @return
     */
    @Override
    public BaseResponse checkCustomerPayPwd(@RequestBody @Valid CustomerCheckPayPasswordRequest customerCheckPayPasswordRequest){
        try{
            customerSiteService.checkCustomerPayPwd(customerCheckPayPasswordRequest);
        }catch (SbcRuntimeException e){
            if (e.getErrorCode() != null && !e.getErrorCode().equals(CustomerErrorCode.NO_CUSTOMER_PAY_PASSWORD)) {
                //输入密码错误处理逻辑
                customerSiteService.checkCustomerPayPwdErrorEvent(customerCheckPayPasswordRequest);
            }
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateInfo(@RequestBody @Valid CustomerBindThirdAccountRequest customerBindThirdAccountRequest){
        ThirdLoginRelation thirdLoginRelation = new ThirdLoginRelation();
        KsBeanUtil.copyPropertiesThird(customerBindThirdAccountRequest,thirdLoginRelation);
        customerSiteService.updateInfo(thirdLoginRelation);
        return BaseResponse.SUCCESSFUL();
    }
}
