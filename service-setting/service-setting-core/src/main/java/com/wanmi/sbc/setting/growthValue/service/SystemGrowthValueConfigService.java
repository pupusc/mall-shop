package com.wanmi.sbc.setting.growthValue.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import com.wanmi.sbc.setting.growthValue.model.root.SystemGrowthValueConfig;
import com.wanmi.sbc.setting.growthValue.repository.SystemGrowthValueConfigRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>系统成长值设置业务逻辑</p>
 *
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@Service("SystemGrowthValueConfigService")
public class SystemGrowthValueConfigService {

    @Autowired
    private SystemGrowthValueConfigRepository systemGrowthValueConfigRepository;

    /**
     * 查询成长值设置
     *
     * @return
     */
    public SystemGrowthValueConfig querySystemGrowthValueConfig() {
        List<SystemGrowthValueConfig> configs = systemGrowthValueConfigRepository.findByDelFlag(DeleteFlag.NO);
        SystemGrowthValueConfig systemGrowthValueConfig = new SystemGrowthValueConfig();
        if (CollectionUtils.isEmpty(configs)) {
            // 如果数据库里无数据，初始化
            systemGrowthValueConfig.setStatus(EnableStatus.DISABLE);
            systemGrowthValueConfig.setRule(GrowthValueRule.USER_DEFINED);
            systemGrowthValueConfig.setDelFlag(DeleteFlag.NO);
            systemGrowthValueConfig.setCreateTime(LocalDateTime.now());
            systemGrowthValueConfig = systemGrowthValueConfigRepository.saveAndFlush(systemGrowthValueConfig);
        } else {
            systemGrowthValueConfig = configs.get(0);
        }

        return systemGrowthValueConfig;
    }

    /**
     * 修改成长值设置
     *
     * @param request
     */
    @Transactional
    public void modifySystemGrowthValueConfig(SystemGrowthValueConfigModifyRequest request) {
        // 根据配置id查询成长值设置详情
        SystemGrowthValueConfig systemGrowthValueConfig = systemGrowthValueConfigRepository.findByGrowthValueConfigIdAndDelFlag(
                request.getGrowthValueConfigId(), DeleteFlag.NO);
        // 成长值设置不存在
        if (Objects.isNull(systemGrowthValueConfig)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(request, systemGrowthValueConfig);
        systemGrowthValueConfigRepository.save(systemGrowthValueConfig);
    }

    /**
     * 修改成长值开关
     *
     * @param request
     */
    @Transactional
    public void modifySystemGrowthValueStatus(SystemGrowthValueStatusModifyRequest request) {
        // 根据配置id查询成长值设置详情
        SystemGrowthValueConfig systemGrowthValueConfig = systemGrowthValueConfigRepository.findByGrowthValueConfigIdAndDelFlag(
                request.getGrowthValueConfigId(), DeleteFlag.NO);
        // 成长值设置不存在
        if (Objects.isNull(systemGrowthValueConfig)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        systemGrowthValueConfigRepository.updateStatusById(request.getGrowthValueConfigId(), request.getStatus(), LocalDateTime.now());
    }
}
