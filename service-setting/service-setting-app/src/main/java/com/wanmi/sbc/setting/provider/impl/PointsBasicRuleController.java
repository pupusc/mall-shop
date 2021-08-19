package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.PointsBasicRuleProvider;
import com.wanmi.sbc.setting.api.request.PointsBasicRuleModifyRequest;
import com.wanmi.sbc.setting.systempointsconfig.service.PointsBasicRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PointsBasicRuleController implements PointsBasicRuleProvider {
    @Autowired
    private PointsBasicRuleService pointsBasicRuleService;


    @Override
    public BaseResponse modifyPointsBasicRule(@RequestBody @Valid PointsBasicRuleModifyRequest request) {
        pointsBasicRuleService.modifyPointsBasicRule(request.getPointsBasicRuleDTOList());
        return BaseResponse.SUCCESSFUL();
    }
}
