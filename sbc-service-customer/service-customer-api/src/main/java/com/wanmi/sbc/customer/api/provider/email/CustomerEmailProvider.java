package com.wanmi.sbc.customer.api.provider.email;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailAddRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailDeleteRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailModifyRequest;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailAddResponse;
import com.wanmi.sbc.customer.api.response.email.CustomerEmailModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.customer.name}", contextId = "CustomerEmailProvider")
public interface CustomerEmailProvider {
    /**
     * 根据客户邮箱ID删除财务邮箱
     *
     * @param request {@link CustomerEmailDeleteRequest}
     * @return 返回删除结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/email/delete-by-customer-id")
    BaseResponse deleteByCustomerId(@RequestBody @Valid CustomerEmailDeleteRequest request);

    /**
     * 新增客户财务邮箱
     *
     * @param request {@link CustomerEmailAddRequest}
     * @return 返回财务邮箱信息 {@link CustomerEmailModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/email/add")
    BaseResponse<CustomerEmailAddResponse> add(@RequestBody @Valid CustomerEmailAddRequest request);

    /**
     * 修改客户财务邮箱
     *
     * @param request {@link CustomerEmailModifyRequest}
     * @return 返回财务邮箱信息 {@link CustomerEmailModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/email/modify")
    BaseResponse<CustomerEmailModifyResponse> modify(@RequestBody @Valid CustomerEmailModifyRequest request);
}
