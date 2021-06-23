package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.response.CustomerSafeResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 客户信息
 * Created by CHENLI on 2017/7/11.
 */
@Api(tags = "CustomerController", description = "客户信息Api")
@RestController
@RequestMapping("/customer")
@Validated
public class CustomerController {
    @Autowired
//    private CustomerService customerService;
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 会员账号安全信息
     * 1）会员密码安全级别
     * 2）会员绑定手机号
     *
     * @return
     */
    @ApiOperation(value = "会员账号安全信息")
    @RequestMapping(value = "/customerSafeInfo", method = RequestMethod.GET)
    public BaseResponse<CustomerSafeResponse> getCustomerSafeInfo() {
//        Customer customer = customerService.findById(commonUtil.getOperator().getUserId());
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(commonUtil.getOperator()
                .getUserId())).getContext();
        String customerAccount = commonUtil.getOperator().getAccount();
        if (Objects.nonNull(customerAccount)) {
            return BaseResponse.success(CustomerSafeResponse.builder()
                    .customerAccount(customerAccount)
                    .safeLevel(customer.getSafeLevel())
                    .build());
        }
        return BaseResponse.FAILED();
    }

}
