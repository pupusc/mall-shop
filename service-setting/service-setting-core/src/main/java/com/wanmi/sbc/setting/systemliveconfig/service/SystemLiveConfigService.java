package com.wanmi.sbc.setting.systemliveconfig.service;

import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.api.request.SystemLiveStatusModifyRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.systemliveconfig.repository.SystemLiveConfigRepository;
import com.wanmi.sbc.setting.systemliveconfig.model.root.SystemLiveConfig;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>小程序直播设置业务逻辑</p>
 *
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@Service("SystemLiveConfigService")
public class SystemLiveConfigService {
    @Autowired
    private SystemLiveConfigRepository systemLiveConfigRepository;


    /**
     * 修改直播开关
     *
     * @param request
     */
    @Transactional
    public void modifySystemLiveStatus(SystemLiveStatusModifyRequest request) {
        systemLiveConfigRepository.updateStatusById(request.getSystemLiveConfigId(), request.getStatus(), LocalDateTime.now());
    }

    /**
     * 查询直播开关
     * @return
     */
    public SystemLiveConfig querySystemLiveConfig() {

        List<SystemLiveConfig> configs = systemLiveConfigRepository.findByDelFlag(DeleteFlag.NO);
        SystemLiveConfig systemLiveConfig = new SystemLiveConfig();
        if (CollectionUtils.isEmpty(configs)) {
            // 如果数据库里无数据，初始化
            systemLiveConfig.setStatus(EnableStatus.DISABLE);
            systemLiveConfig.setDelFlag(DeleteFlag.NO);
            systemLiveConfig.setCreateTime(LocalDateTime.now());
            systemLiveConfig = systemLiveConfigRepository.saveAndFlush(systemLiveConfig);
        } else {
            systemLiveConfig = configs.get(0);
        }

        return systemLiveConfig;

    }
}

