package com.wanmi.sbc.customer.provider.impl.email;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.email.CustomerEmailProvider;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailAddRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailDeleteRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailModifyRequest;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailAddResponse;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailModifyResponse;
import com.wanmi.sbc.customer.email.model.root.CustomerEmail;
import com.wanmi.sbc.customer.email.service.CustomerEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class CustomerEmailController implements CustomerEmailProvider {
    @Autowired
    private CustomerEmailService customerEmailService;

    /**
     * 根据客户邮箱ID删除财务邮箱
     *
     * @param request {@link CustomerEmailDeleteRequest}
     * @return 返回删除结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse deleteByCustomerId(@RequestBody @Valid CustomerEmailDeleteRequest request) {
        customerEmailService.deleteCustomerEmailByCustomerEmailId(request);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 新增客户财务邮箱
     *
     * @param request {@link CustomerEmailAddRequest}
     * @return 返回财务邮箱信息 {@link CustomerEmailModifyResponse}
     */
    @Override

    public BaseResponse<CustomerEmailAddResponse> add(@RequestBody @Valid CustomerEmailAddRequest request) {
        CustomerEmail customerEmail = customerEmailService.addCustomerEmail(request);

        CustomerEmailAddResponse response = new CustomerEmailAddResponse();

        KsBeanUtil.copyPropertiesThird(customerEmail, response);

        return BaseResponse.success(response);
    }

    /**
     * 修改客户财务邮箱
     *
     * @param request {@link CustomerEmailModifyRequest}
     * @return 返回财务邮箱信息 {@link CustomerEmailModifyResponse}
     */
    @Override

    public BaseResponse<CustomerEmailModifyResponse> modify(@RequestBody @Valid CustomerEmailModifyRequest request) {
        CustomerEmail customerEmail = customerEmailService.modifyCustomerEmail(request);

        CustomerEmailModifyResponse response = new CustomerEmailModifyResponse();

        KsBeanUtil.copyPropertiesThird(customerEmail, response);

        return BaseResponse.success(response);
    }
}
