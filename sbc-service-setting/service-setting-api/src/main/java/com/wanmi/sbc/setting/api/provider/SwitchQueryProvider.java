package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.SwitchGetByIdRequest;
import com.wanmi.sbc.setting.api.response.SwitchGetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "SwitchQueryProvider")
public interface SwitchQueryProvider {
    /**
     * 根据id查询开关
     *
     * @param request {@link SwitchGetByIdRequest}
     * @return 系统开关 {@link SwitchGetByIdResponse}
     */
    @PostMapping("/setting/${application.setting.version}/sysswitch/get-by-id")
    BaseResponse<SwitchGetByIdResponse> getById(@RequestBody SwitchGetByIdRequest request);
}
