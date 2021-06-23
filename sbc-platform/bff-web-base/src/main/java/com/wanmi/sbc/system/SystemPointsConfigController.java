package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.system.response.SystemPointsConfigSimplifyQueryResponse;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * WEB端-积分设置
 */
@Api(tags = "SystemPointsConfigController", description = "WEB端-积分设置API")
@RestController
@RequestMapping("/pointsConfig")
public class SystemPointsConfigController {

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;



    /**
     * 查询积分设置
     *
     * @return
     */
    @ApiOperation(value = "查询积分设置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<SystemPointsConfigQueryResponse> query() {
        return BaseResponse.success(this.querySettingCache());
    }


    /**
     * 查询积分设置-精简版
     *
     * @return
     */
    @ApiOperation(value = "查询积分设置-精简版")
    @RequestMapping(value = "/simplify",method = RequestMethod.GET)
    public BaseResponse<SystemPointsConfigSimplifyQueryResponse> simplify() {
        SystemPointsConfigQueryResponse response = this.querySettingCache();
        SystemPointsConfigSimplifyQueryResponse simplifyQueryResponse = SystemPointsConfigSimplifyQueryResponse.builder()
                .maxDeductionRate(response.getMaxDeductionRate()).overPointsAvailable(response.getOverPointsAvailable())
                .pointsUsageFlag(response.getPointsUsageFlag()).pointsWorth(response.getPointsWorth())
                .status(response.getStatus())
                .build();
        return BaseResponse.success(simplifyQueryResponse);
    }

    /**
     * 查询积分设置
     * @return
     */
    private SystemPointsConfigQueryResponse querySettingCache(){
        return systemPointsConfigService.querySettingCache();
    }

}
