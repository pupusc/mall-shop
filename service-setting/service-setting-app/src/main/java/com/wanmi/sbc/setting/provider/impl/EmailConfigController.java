package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.EmailConfigProvider;
import com.wanmi.sbc.setting.api.request.EmailConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.EmailConfigQueryResponse;
import com.wanmi.sbc.setting.email.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class EmailConfigController implements EmailConfigProvider {

    @Autowired
    private EmailConfigService emailConfigService;

    @Override
    public BaseResponse<EmailConfigQueryResponse> queryEmailConfig() {
        return BaseResponse.success(emailConfigService.getSystemEmailConfig());
    }

    @Override
    public BaseResponse<EmailConfigQueryResponse> modifyEmailConfig(@RequestBody @Valid EmailConfigModifyRequest request) {
        request.setUpdateTime(LocalDateTime.now());
        return BaseResponse.success(emailConfigService.modifyCustomerEmail(request));
    }
}
