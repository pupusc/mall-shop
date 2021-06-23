package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.PointsBasicRuleQueryProvider;
import com.wanmi.sbc.setting.api.response.PointsBasicRuleListResponse;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.systempointsconfig.service.PointsBasicRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PointsBasicRuleQueryController implements PointsBasicRuleQueryProvider {
    @Autowired
    private PointsBasicRuleService pointsBasicRuleService;


    @Override
    public BaseResponse<PointsBasicRuleListResponse> listPointsBasicRule() {
        PointsBasicRuleListResponse response = new PointsBasicRuleListResponse();

        List<Config> configList = pointsBasicRuleService.listPointsBasicRule();

        KsBeanUtil.copyList(configList, response.getConfigVOList());

        return BaseResponse.success(response);
    }
}
