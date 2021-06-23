package com.wanmi.sbc.customer.provider.impl.loginregister;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteQueryProvider;
import com.wanmi.sbc.customer.api.request.loginregister.*;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerByAccountResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerDetailByAccountResponse;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerLoginResponse;
import com.wanmi.sbc.customer.api.response.loginregister.ThirdLoginAndBindResponse;
import com.wanmi.sbc.customer.bean.dto.ThirdLoginRelationDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
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
public class CustomerSiteQueryController implements CustomerSiteQueryProvider{

    @Autowired
    private CustomerSiteService customerSiteService;

    /**
     * 会员登录
     *
     * @param customerLoginRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerLoginResponse> login(@RequestBody @Valid CustomerLoginRequest customerLoginRequest) {
        Customer customer = customerSiteService.login(customerLoginRequest.getCustomerAccount(),customerLoginRequest.getPassword());
        if (Objects.isNull(customer)){
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.success(KsBeanUtil.convert(customer,CustomerLoginResponse.class));
    }

    /**
     * 根据会员账号查询会员信息
     *
     * @param customerByAccountRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerByAccountResponse> getCustomerByCustomerAccount(@RequestBody @Valid CustomerByAccountRequest customerByAccountRequest) {
        Customer customer = customerSiteService.getCustomerByAccount(customerByAccountRequest.getCustomerAccount());
        CustomerByAccountResponse customerByAccountResponse = CustomerByAccountResponse.builder().build();
        if (Objects.isNull(customer)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customer,customerByAccountResponse);
        CustomerDetailVO customerDetailVO = new CustomerDetailVO();
        KsBeanUtil.copyPropertiesThird(customer.getCustomerDetail(), customerDetailVO);
        customerByAccountResponse.setCustomerDetail(customerDetailVO);
        return BaseResponse.success(customerByAccountResponse);
    }

    /**
     * 根据会员账号查询会员详情信息
     *
     * @param customerDetailByAccountRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDetailByAccountResponse> getCustomerDetailByCustomerAccount(@RequestBody @Valid CustomerDetailByAccountRequest customerDetailByAccountRequest) {
        CustomerDetail customerDetail = customerSiteService.getCustomerDetailByAccount(customerDetailByAccountRequest.getCustomerAccount());
        CustomerDetailByAccountResponse customerDetailByAccountResponse = CustomerDetailByAccountResponse.builder().build();
        if (Objects.isNull(customerDetail)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerDetail,customerDetailByAccountResponse);
        return BaseResponse.success(customerDetailByAccountResponse);
    }


    /**
     * 第三方快捷登录并绑定账号信息
     *
     * @param customerLoginAndBindThirdAccountRequest
     * @return
     */

    @Override
    public BaseResponse<ThirdLoginAndBindResponse> loginAndBindThirdAccount(@RequestBody @Valid CustomerLoginAndBindThirdAccountRequest customerLoginAndBindThirdAccountRequest) {
        ThirdLoginRelationDTO thirdLoginRelationDTO = customerLoginAndBindThirdAccountRequest.getThirdLoginRelationDTO();
        ThirdLoginRelation thirdLoginRelation = new ThirdLoginRelation();
        KsBeanUtil.copyPropertiesThird(thirdLoginRelationDTO,thirdLoginRelation);
        ThirdLoginAndBindResponse customer = customerSiteService.thirdLoginAndBind(
                customerLoginAndBindThirdAccountRequest.getPhone(),
                thirdLoginRelation,
                customerLoginAndBindThirdAccountRequest.getShareUserId());
        if (Objects.isNull(customer)){
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.success(customer);
    }


    @Override
    public BaseResponse checkByAccount(@RequestBody @Valid CustomerCheckByAccountRequest request){
        customerSiteService.beforeRegister(request.getCustomerAccount());
        return BaseResponse.SUCCESSFUL();
    }
}
