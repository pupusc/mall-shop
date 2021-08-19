package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.SwitchQueryProvider;
import com.wanmi.sbc.setting.api.request.SwitchGetByIdRequest;
import com.wanmi.sbc.setting.api.response.SwitchGetByIdResponse;
import com.wanmi.sbc.setting.sysswitch.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SwitchQueryController implements SwitchQueryProvider {
    @Autowired
    private SwitchService switchService;

    @Override
    public BaseResponse<SwitchGetByIdResponse> getById(@RequestBody @Valid SwitchGetByIdRequest request) {
        return BaseResponse.success(switchService.findSwitchById(request.getId()));
    }
}
