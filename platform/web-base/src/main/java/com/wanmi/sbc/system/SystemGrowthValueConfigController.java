package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueOpenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 成长值Controller
 * Created by yang on 2019/03/07.
 */
@Api(tags = "SystemGrowthValueConfigController", description = "成长值开关查询 Api")
@RestController
@RequestMapping("/growthValue")
public class SystemGrowthValueConfigController {

    @Autowired
    private SystemGrowthValueConfigQueryProvider systemGrowthValueConfigQueryProvider;

    /**
     * 查询成长值是否开启
     *
     * @return
     */
    @ApiOperation(value = "查询成长值是否开启")
    @RequestMapping(value = "/isOpen", method = RequestMethod.GET)
    public BaseResponse<SystemGrowthValueOpenResponse> isGrowthValueOpen() {
        SystemGrowthValueOpenResponse growthValueOpenResponse = systemGrowthValueConfigQueryProvider.isGrowthValueOpen().getContext();
        return BaseResponse.success(growthValueOpenResponse);
    }
}
