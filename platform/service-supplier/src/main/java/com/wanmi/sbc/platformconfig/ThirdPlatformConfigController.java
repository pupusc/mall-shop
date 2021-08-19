package com.wanmi.sbc.platformconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigProvider;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "第三方平台配置管理API", tags = "ThirdPlatformConfigController")
@RestController
@RequestMapping(value = "/third/platformconfig")
public class ThirdPlatformConfigController {

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private ThirdPlatformConfigProvider thirdPlatformConfigProvider;

    @ApiOperation(value = "第三方平台配置列表")
    @PostMapping("/list")
    public BaseResponse<ThirdPlatformConfigQueryResponse> list() {
        return thirdPlatformConfigQueryProvider.list();
    }

    @ApiOperation(value = "查询第三方平台配置信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "configType",
            value = "第三方平台配置", required = true)
    @RequestMapping(value = "/{configType}", method = RequestMethod.GET)
    public BaseResponse<ThirdPlatformConfigResponse> list(@PathVariable String configType) {
        return thirdPlatformConfigQueryProvider.get(ThirdPlatformConfigByTypeRequest.builder().configType(configType).build());
    }
}
