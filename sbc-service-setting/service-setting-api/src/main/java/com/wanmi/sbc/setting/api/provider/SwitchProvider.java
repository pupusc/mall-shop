package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.SwitchModifyRequest;
import com.wanmi.sbc.setting.api.response.SwitchModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "SwitchProvider")
public interface SwitchProvider {
    /**
     * 开关开启关闭
     *
     * @param request {@link SwitchModifyRequest}
     * @return 修改的记录数 {@link SwitchModifyResponse}
     */
    @PostMapping("/setting/${application.setting.version}/sysswitch/modify")
    BaseResponse<SwitchModifyResponse> modify(@RequestBody SwitchModifyRequest request);
}
