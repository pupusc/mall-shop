package com.wanmi.sbc.crm.api.provider.customertag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customertag.CustomerAutoTagRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerPreferenceAutoTagRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.crm.name}",contextId = "CustomerAutoTagQueryProvider")
public interface CustomerAutoTagQueryProvider {
    /**
     * 获取会员标签
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/tag/customer-tag")
    BaseResponse getCustomerTag(@RequestBody @Valid CustomerAutoTagRequest request);

    /**
     * 获取会员偏好类标签
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/tag/customer-preference-tag")
    BaseResponse getCustomerPreferenceTag(@RequestBody @Valid CustomerPreferenceAutoTagRequest request);
}
