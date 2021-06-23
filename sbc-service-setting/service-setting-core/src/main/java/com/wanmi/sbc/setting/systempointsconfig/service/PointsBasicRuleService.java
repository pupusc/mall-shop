package com.wanmi.sbc.setting.systempointsconfig.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.bean.dto.PointsBasicRuleDTO;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigRepository;
import com.wanmi.sbc.setting.growthValue.model.root.SystemGrowthValueConfig;
import com.wanmi.sbc.setting.growthValue.repository.SystemGrowthValueConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 积分获取service
 * Created by YINXIANZHI on 2019/03/25.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class PointsBasicRuleService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private SystemGrowthValueConfigRepository systemGrowthValueConfigRepository;

    /**
     * 查询积分基础获取规则
     *
     * @return
     */
    public List<Config> listPointsBasicRule() {
        return configRepository.findByConfigKeyAndDelFlag(ConfigKey.POINTS_BASIC_RULE.toString(), DeleteFlag.NO);
    }

    /**
     * 修改积分基础获取规则
     *
     * @param pointsBasicRuleDTOS
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyPointsBasicRule(List<PointsBasicRuleDTO> pointsBasicRuleDTOS) {
        pointsBasicRuleDTOS.forEach(pointsBasicRuleDTO -> configRepository
                .updateStatusByTypeAndConfigKey(pointsBasicRuleDTO.getConfigType().toString(),
                        pointsBasicRuleDTO.getConfigKey().toString(), pointsBasicRuleDTO.getStatus(),
                        pointsBasicRuleDTO.getContext()));

        //判断是否是同步积分规则
        List<SystemGrowthValueConfig> configs = systemGrowthValueConfigRepository.findByDelFlag(DeleteFlag.NO);
        if(GrowthValueRule.SYNCHRONIZE_POINTS.equals(configs.get(0).getRule())){
            List<Config> growthValueBasicRules = configRepository.findByConfigKeyAndDelFlag(ConfigKey
                    .GROWTH_VALUE_BASIC_RULE.toString(), DeleteFlag.NO);

            //同步基础规则
            for(int i=0;i<pointsBasicRuleDTOS.size();i++){
                growthValueBasicRules.get(i).setContext(pointsBasicRuleDTOS.get(i).getContext());
                growthValueBasicRules.get(i).setStatus(pointsBasicRuleDTOS.get(i).getStatus());
            }

            growthValueBasicRules.forEach(growthValueBasicRule -> configRepository
                    .updateStatusByTypeAndConfigKey(growthValueBasicRule.getConfigType().toString(),
                            growthValueBasicRule.getConfigKey().toString(), growthValueBasicRule.getStatus(),
                            growthValueBasicRule.getContext()));
        }
    }

}
