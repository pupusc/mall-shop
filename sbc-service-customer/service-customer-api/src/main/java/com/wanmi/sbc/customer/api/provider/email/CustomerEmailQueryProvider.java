package com.wanmi.sbc.customer.api.provider.email;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.email.NoDeleteCustomerEmailListByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.email.NoDeleteCustomerEmailListByCustomerIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.customer.name}", contextId = "CustomerEmailQueryProvider")
public interface CustomerEmailQueryProvider {
    /**
     * 根据客户ID查询客户的财务邮箱列表
     *
     * @param request {@link NoDeleteCustomerEmailListByCustomerIdRequest}
     * @return 财务邮箱列表 {@link NoDeleteCustomerEmailListByCustomerIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/email/list")
    BaseResponse<NoDeleteCustomerEmailListByCustomerIdResponse> list(@RequestBody @Valid
                                                                             NoDeleteCustomerEmailListByCustomerIdRequest
                                                                             request);
}
