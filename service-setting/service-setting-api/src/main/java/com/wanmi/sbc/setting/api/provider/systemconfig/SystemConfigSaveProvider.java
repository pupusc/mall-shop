package com.wanmi.sbc.setting.api.provider.systemconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.ConfigUpdateRequest;
import com.wanmi.sbc.setting.api.request.systemconfig.LogisticsSaveRopRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by feitingting on 2019/11/6.
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemConfigSaveProvider")
public interface SystemConfigSaveProvider {

    @PostMapping("/setting/${application.setting.version}/sysconfig/save-kuaidi")
    BaseResponse saveKuaidi(@RequestBody @Valid LogisticsSaveRopRequest request);

    @PostMapping("/setting/${application.setting.version}/sysconfig/modify")
    BaseResponse modify(@RequestBody @Valid ConfigContextModifyByTypeAndKeyRequest request);

    @PostMapping("/setting/${application.setting.version}/sysconfig/update")
    BaseResponse update(@RequestBody @Valid ConfigUpdateRequest request);
}
