package com.wanmi.sbc.setting.api.provider.systemconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by feitingting on 2019/11/6.
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemConfigQueryProvider")
public interface SystemConfigQueryProvider {

    @PostMapping("/setting/${application.setting.version}/sysconfig/list")
    BaseResponse<SystemConfigResponse> findByConfigKeyAndDelFlag(@RequestBody @Valid ConfigQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/sysconfig/find-by-type")
    BaseResponse<SystemConfigTypeResponse> findByConfigTypeAndDelFlag(@RequestBody @Valid ConfigQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/sysconfig/find-kuaidi-config")
    BaseResponse<LogisticsRopResponse> findKuaiDiConfig (@RequestBody @Valid ConfigQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/sysconfig/find-list")
    BaseResponse<SystemConfigResponse> list (@RequestBody @Valid SystemConfigQueryRequest request);
}
