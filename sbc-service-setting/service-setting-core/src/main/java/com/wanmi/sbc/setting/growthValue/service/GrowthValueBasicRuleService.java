package com.wanmi.sbc.setting.growthValue.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.bean.dto.GrowthValueBasicRuleDTO;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成长值设置service
 * Created by YINXIANZHI on 2019/02/22.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class GrowthValueBasicRuleService {

    @Autowired
    private ConfigRepository configRepository;

    /**
     * 查询成长值获取基础规则列表
     *
     * @return
     */
    public List<Config> listGrowthValueBasicRule() {
        return configRepository.findByConfigKeyAndDelFlag(ConfigKey.GROWTH_VALUE_BASIC_RULE.toString(), DeleteFlag.NO);
    }

    /**
     * 修改成长值基础获取规则
     *
     * @param growthValueBasicRuleDTOList
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyGrowthValueBasicRule(List<GrowthValueBasicRuleDTO> growthValueBasicRuleDTOList) {
        growthValueBasicRuleDTOList.forEach(growthValueBasicRuleDTO -> configRepository
                .updateStatusByTypeAndConfigKey(growthValueBasicRuleDTO.getConfigType().toString(),
                        growthValueBasicRuleDTO.getConfigKey().toString(), growthValueBasicRuleDTO.getStatus(),
                        growthValueBasicRuleDTO.getContext()));
    }

    /**
     * 根据类型查询config配置
     *
     * @return
     */
    public Config findByConfigTypeAndDelFlag(ConfigType type) {
        return configRepository.findByConfigTypeAndDelFlag(type.toValue(), DeleteFlag.NO);
    }
}
