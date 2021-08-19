package com.wanmi.sbc.customer.provider.impl.account;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.account.model.root.CustomerAccount;
import com.wanmi.sbc.customer.account.service.CustomerAccountService;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountQueryProvider;
import com.wanmi.sbc.customer.api.request.account.*;
import com.wanmi.sbc.customer.api.response.account.*;
import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 9:35
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerAccountQueryController implements CustomerAccountQueryProvider {

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 根据会员ID查询银行账户信息
     * @param customerAccountListRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountListResponse> listByCustomerId(@RequestBody @Valid CustomerAccountListRequest
                                                                                  customerAccountListRequest) {
        List<CustomerAccount> customerAccountList = customerAccountService.findCustomerAccountList(customerAccountListRequest.getCustomerId());
        if (!CollectionUtils.isNotEmpty(customerAccountList)){
            return BaseResponse.success(CustomerAccountListResponse.builder().build());
        }
        List<CustomerAccountVO> customerAccountVOList = KsBeanUtil.convert(customerAccountList, CustomerAccountVO.class);
        CustomerAccountListResponse customerAccountListResponse = CustomerAccountListResponse.builder()
                .customerAccountVOList(customerAccountVOList).build();
        return BaseResponse.success(customerAccountListResponse);
    }


    /**
     * 根据会员银行账号ID查询银行账户信息（仅包含未删除）
     * @param customerAccountOptionalRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountOptionalResponse> getByCustomerAccountIdAndDelFlag(@RequestBody @Valid CustomerAccountOptionalRequest customerAccountOptionalRequest) {
        CustomerAccount customerAccount = customerAccountService.findById(customerAccountOptionalRequest.getCustomerAccountId()).orElse(null);
        CustomerAccountOptionalResponse customerAccountOptionalResponse = new CustomerAccountOptionalResponse();
        if (Objects.isNull(customerAccount)){
           return BaseResponse.success(customerAccountOptionalResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerAccount,customerAccountOptionalResponse);
        return BaseResponse.success(customerAccountOptionalResponse);
    }

    /**
     * 根据会员银行账号ID查询银行账户信息（包含已删除和未删除）
     * @param customerAccountRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountResponse> getByCustomerAccountId(@RequestBody @Valid CustomerAccountRequest customerAccountRequest) {
        CustomerAccount customerAccount = customerAccountService.findByAccountId(customerAccountRequest.getCustomerAccountId());
        CustomerAccountResponse customerAccountResponse = CustomerAccountResponse.builder().build();
        if (Objects.isNull(customerAccount)){
            return BaseResponse.success(customerAccountResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerAccount,customerAccountResponse);
        return BaseResponse.success(customerAccountResponse);
    }

    /**
     * 根据会员银行账号查询银行账户信息（仅包含未删除）
     * @param customerAccountQueryRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountByCustomerAccountResponse> getByCustomerAccountNoAndDelFlag
            (@RequestBody @Valid CustomerAccountByCustomerAccountNoRequest customerAccountQueryRequest) {
        CustomerAccount customerAccount = customerAccountService.findByCustomerAccountNoAndDelFlag(customerAccountQueryRequest.getCustomerAccountNo()).orElse(null);
        CustomerAccountByCustomerAccountResponse customerAccountByCustomerAccountResponse = CustomerAccountByCustomerAccountResponse.builder().build();
        if (Objects.isNull(customerAccount)){
            return BaseResponse.success(customerAccountByCustomerAccountResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerAccount, customerAccountByCustomerAccountResponse);
        return BaseResponse.success(customerAccountByCustomerAccountResponse);
    }

    /**
     * 根据会员ID查询银行账号总数
     * @param customerAccountByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerAccountByCustomerIdResponse> countByCustomerId(@RequestBody @Valid CustomerAccountByCustomerIdRequest customerAccountByCustomerIdRequest) {
        Integer count = customerAccountService.countCustomerAccount(customerAccountByCustomerIdRequest.getCustomerId());
        return BaseResponse.success(new CustomerAccountByCustomerIdResponse(count));
    }
}
