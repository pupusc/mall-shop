package com.wanmi.sbc.customer.provider.impl.account;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.account.model.root.CustomerAccount;
import com.wanmi.sbc.customer.account.request.CustomerAccountSaveRequest;
import com.wanmi.sbc.customer.account.service.CustomerAccountService;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountProvider;
import com.wanmi.sbc.customer.api.request.account.*;
import com.wanmi.sbc.customer.api.response.account.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 9:35
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerAccountController implements CustomerAccountProvider {

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 新增会员银行账户
     * @param customerAccountAddRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountAddResponse> add(@RequestBody @Valid CustomerAccountAddRequest
                                                                    customerAccountAddRequest) {
        CustomerAccountSaveRequest customerAccountSaveRequest = new CustomerAccountSaveRequest();
        KsBeanUtil.copyPropertiesThird(customerAccountAddRequest,customerAccountSaveRequest);
        CustomerAccount customerAccount = customerAccountService.addCustomerAccount(customerAccountSaveRequest,customerAccountAddRequest.getEmployeeId());
        CustomerAccountAddResponse customerAccountAddResponse = CustomerAccountAddResponse.builder().build();
        if (Objects.isNull(customerAccount)){
            return BaseResponse.success(customerAccountAddResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerAccount,customerAccountAddResponse);
        return BaseResponse.success(customerAccountAddResponse);
    }

    /**
     * boss端修改会员银行账户
     * @param customerAccountModifyRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountModifyResponse> modify(@RequestBody @Valid CustomerAccountModifyRequest customerAccountModifyRequest) {
        CustomerAccountSaveRequest customerAccountSaveRequest = new CustomerAccountSaveRequest();
        KsBeanUtil.copyPropertiesThird(customerAccountModifyRequest,customerAccountSaveRequest);
        CustomerAccount customerAccount = customerAccountService.editCustomerAccount(customerAccountSaveRequest,customerAccountModifyRequest.getEmployeeId());
        CustomerAccountModifyResponse customerAccountModifyResponse = CustomerAccountModifyResponse.builder().build();
        if (Objects.isNull(customerAccount)){
            return BaseResponse.success(customerAccountModifyResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerAccount,customerAccountModifyResponse);
        return BaseResponse.success(customerAccountModifyResponse);
    }

    /**
     * 客户端修改会员银行账户
     * @param customerAccountModifyByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountModifyByCustomerIdResponse> modifyByCustomerId(@RequestBody @Valid CustomerAccountModifyByCustomerIdRequest customerAccountModifyByCustomerIdRequest) {
        CustomerAccountSaveRequest customerAccountSaveRequest = new CustomerAccountSaveRequest();
        KsBeanUtil.copyPropertiesThird(customerAccountModifyByCustomerIdRequest,customerAccountSaveRequest);
        Integer result = customerAccountService.updateCustomerAccount(customerAccountSaveRequest,customerAccountModifyByCustomerIdRequest.getCustomerId());
        return BaseResponse.success(new CustomerAccountModifyByCustomerIdResponse(result));
    }

    /**
     * 根据会员银行账号ID和EmployeeID删除会员银行账号信息（Boss端）
     * @param customerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdResponse> deleteByCustomerAccountIdAndEmployeeId(@RequestBody @Valid
                                                                                    CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest customerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest) {
       Integer result = customerAccountService.deleteCustomerAccount(customerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest.getCustomerAccountId(),customerAccountDeleteByCustomerAccountIdAndEmployeeIdRequest.getEmployeeId());
        return BaseResponse.success(new CustomerAccountDeleteByCustomerAccountIdAndEmployeeIdResponse(result));
    }

    /**
     * 根据会员银行账号ID和会员ID删除会员银行账号信息（客户端）
     * @param customerAccountDeleteByCustomerAccountIdAndCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountDeleteByCustomerAccountIdAndCustomerIdResponse> deleteCustomerAccountByCustomerAccountIdAndCustomerId(@RequestBody @Valid CustomerAccountDeleteByCustomerAccountIdAndCustomerIdRequest customerAccountDeleteByCustomerAccountIdAndCustomerIdRequest) {
        Integer result = customerAccountService.deleteCustomerAccountById(customerAccountDeleteByCustomerAccountIdAndCustomerIdRequest.getCustomerAccountId(),customerAccountDeleteByCustomerAccountIdAndCustomerIdRequest.getCustomerId());
        return BaseResponse.success(new CustomerAccountDeleteByCustomerAccountIdAndCustomerIdResponse(result));
    }
}
