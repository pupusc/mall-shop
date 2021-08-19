package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.SwitchProvider;
import com.wanmi.sbc.setting.api.request.SwitchModifyRequest;
import com.wanmi.sbc.setting.api.response.SwitchModifyResponse;
import com.wanmi.sbc.setting.sysswitch.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwitchController implements SwitchProvider {
    @Autowired
    private SwitchService switchService;

    @Override
    public BaseResponse<SwitchModifyResponse> modify(@RequestBody SwitchModifyRequest request) {
        SwitchModifyResponse response = new SwitchModifyResponse();
        response.setCount(switchService.updateSwitch(request.getId(), request.getStatus()));

        return BaseResponse.success(response);
    }
}
