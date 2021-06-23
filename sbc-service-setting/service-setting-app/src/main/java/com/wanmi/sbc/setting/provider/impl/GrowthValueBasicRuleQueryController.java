package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.GrowthValueBasicRuleQueryProvider;
import com.wanmi.sbc.setting.api.response.GrowthValueBasicRuleListResponse;
import com.wanmi.sbc.setting.api.response.GrowthValueBasicRuleResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.growthValue.service.GrowthValueBasicRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GrowthValueBasicRuleQueryController implements GrowthValueBasicRuleQueryProvider {
    @Autowired
    private GrowthValueBasicRuleService growthValueBasicRuleService;

    @Override
    public BaseResponse<GrowthValueBasicRuleListResponse> listGrowthValueBasicRule() {
        GrowthValueBasicRuleListResponse response = new GrowthValueBasicRuleListResponse();

        List<Config> configList = growthValueBasicRuleService.listGrowthValueBasicRule();

        KsBeanUtil.copyList(configList, response.getConfigVOList());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GrowthValueBasicRuleResponse> findGrowthValueByConfigType(@RequestBody ConfigType type) {
        GrowthValueBasicRuleResponse response = new GrowthValueBasicRuleResponse();
        Config config = growthValueBasicRuleService.findByConfigTypeAndDelFlag(type);
        KsBeanUtil.copyPropertiesThird(config, response);
        return BaseResponse.success(response);
    }
}
