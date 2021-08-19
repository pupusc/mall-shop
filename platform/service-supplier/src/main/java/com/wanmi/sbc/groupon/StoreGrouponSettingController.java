package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.grouponsetting.GrouponSettingQueryProvider;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingAuditFlagResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by feitingting on 2019/5/16.
 */

/**
 * 拼团设置控制器
 */
@RestController
@RequestMapping("/groupon/setting")
@Api(description = "S2B拼团设置", tags = "GrouponSettingController")
public class StoreGrouponSettingController {

    @Autowired
    private GrouponSettingQueryProvider grouponSettingQueryProvider;

    /**
     * 获取拼团商品审核状态
     */
    @ApiOperation(value = "获取拼团商品审核状态")
    @RequestMapping(value="/get-goods-audit-flag",method = RequestMethod.GET)
    public BaseResponse<GrouponSettingAuditFlagResponse> getGoodsAuditFlag(){
        return grouponSettingQueryProvider.getGoodsAuditFlag();
    }

}
