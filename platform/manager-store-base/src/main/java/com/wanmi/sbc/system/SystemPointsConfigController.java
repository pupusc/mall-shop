package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台端-积分设置
 */
@Api(tags = "SystemPointsConfigController", description = "平台端-积分设置API")
@RestController
@RequestMapping("/boss/pointsConfig")
public class SystemPointsConfigController {

    @Autowired
    private SystemPointsConfigQueryProvider systemPointsConfigQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 查询积分设置
     *
     * @return
     */
    @ApiOperation(value = "查询积分设置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<SystemPointsConfigQueryResponse> query() {
        BaseResponse<SystemPointsConfigQueryResponse> response = systemPointsConfigQueryProvider.querySystemPointsConfig();
        if(BoolFlag.YES.equals(commonUtil.getCompanyType())){
            response.getContext().setPointsUsageFlag(PointsUsageFlag.ORDER);
        }
        return response;
    }
}
