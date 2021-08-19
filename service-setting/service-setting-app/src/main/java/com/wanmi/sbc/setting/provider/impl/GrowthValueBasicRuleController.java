package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.GrowthValueBasicRuleProvider;
import com.wanmi.sbc.setting.api.request.GrowthValueBasicRuleModifyRequest;
import com.wanmi.sbc.setting.growthValue.service.GrowthValueBasicRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GrowthValueBasicRuleController implements GrowthValueBasicRuleProvider {
    @Autowired
    private GrowthValueBasicRuleService growthValueBasicRuleService;

    @Override
    public BaseResponse modifyGrowthValueBasicRule(@RequestBody @Valid GrowthValueBasicRuleModifyRequest request) {
        growthValueBasicRuleService.modifyGrowthValueBasicRule(request.getGrowthValueBasicRuleDTOList());
        return BaseResponse.SUCCESSFUL();
    }
}
