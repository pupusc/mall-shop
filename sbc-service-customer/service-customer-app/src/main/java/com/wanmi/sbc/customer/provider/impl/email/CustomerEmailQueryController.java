package com.wanmi.sbc.customer.provider.impl.email;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.email.CustomerEmailQueryProvider;
import com.wanmi.sbc.customer.api.request.email.NoDeleteCustomerEmailListByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.email.NoDeleteCustomerEmailListByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerEmailVO;
import com.wanmi.sbc.customer.email.model.root.CustomerEmail;
import com.wanmi.sbc.customer.email.service.CustomerEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class CustomerEmailQueryController implements CustomerEmailQueryProvider {
    @Autowired
    private CustomerEmailService customerEmailService;

    /**
     * 根据客户ID查询客户的财务邮箱列表
     *
     * @param request {@link NoDeleteCustomerEmailListByCustomerIdRequest}
     * @return 财务邮箱列表 {@link NoDeleteCustomerEmailListByCustomerIdResponse}
     */
    @Override
    public BaseResponse<NoDeleteCustomerEmailListByCustomerIdResponse> list(@RequestBody @Valid NoDeleteCustomerEmailListByCustomerIdRequest request) {
        List<CustomerEmail> emails = customerEmailService.listCustomerEmailByCustomerId(request.getCustomerId());

        return BaseResponse.success(new NoDeleteCustomerEmailListByCustomerIdResponse(KsBeanUtil.convert
                (emails, CustomerEmailVO.class)));
    }
}
