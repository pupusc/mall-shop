package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.api.provider.SystemLiveConfigProvider;
import com.wanmi.sbc.setting.api.provider.SystemLiveConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.ConfigUpdateRequest;
import com.wanmi.sbc.setting.api.request.SystemLiveStatusModifyRequest;
import com.wanmi.sbc.setting.api.response.SystemLiveOpenResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(description = "小程序直播设置管理API", tags = "SystemLiveConfigController")
@RestController
@RequestMapping(value = "/sysconfig")
public class SystemLiveConfigController {


    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private SystemConfigSaveProvider systemConfigSaveProvider;

    /**
     * 查询直播是否开启
     *
     * @return
     */
    @ApiOperation(value = "查询直播是否开启")
    @RequestMapping(value = "/isOpen", method = RequestMethod.GET)
    public BaseResponse<SystemConfigResponse> isLiveOpen() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(0);
        request.setConfigKey("liveSwitch");
        request.setConfigType("liveSwitch");
        return systemConfigQueryProvider.findByConfigKeyAndDelFlag(request);
    }

    /**
     * 直播开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "直播开关")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResponse openLive(@Valid @RequestBody ConfigUpdateRequest request) {

        systemConfigSaveProvider.update(request);

        return BaseResponse.SUCCESSFUL();
    }


}
