package com.wanmi.sbc.setting.onlineservice.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.onlineservice.repository.OnlineServiceRepository;
import com.wanmi.sbc.setting.onlineservice.model.root.OnlineService;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>onlineService业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@Service("OnlineServiceService")
public class OnlineServiceService {
    @Autowired
    private OnlineServiceRepository onlineServiceRepository;

    /**
     * 新增onlineService
     *
     * @author lq
     */
    @Transactional
    public OnlineService add(OnlineService entity) {
        onlineServiceRepository.save(entity);
        return entity;
    }

    /**
     * 通过店铺id 查询在线客服
     *
     * @author lq
     */
    public OnlineService getByStoreId(Long storeId) {
        OnlineService onlineServer = onlineServiceRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
        if (Objects.nonNull(onlineServer)) {
            return onlineServer;
        } else {
            return this.saveOnlineServer(storeId);
        }
    }

    /**
     * 单个查询onlineService
     *
     * @param onlineServiceId
     * @return
     */
    public OnlineService getById(Integer onlineServiceId) {
        return onlineServiceRepository.getOne(onlineServiceId);
    }

    /**
     * 初始化一条客服开关设置记录
     *
     * @param storeId
     * @return
     */
    @Transactional
    public OnlineService saveOnlineServer(Long storeId) {
        OnlineService onlineServer = new OnlineService();
        onlineServer.setStoreId(storeId);
        onlineServer.setServerStatus(DefaultFlag.NO);
        onlineServer.setEffectiveApp(DefaultFlag.NO);
        onlineServer.setEffectiveMobile(DefaultFlag.NO);
        onlineServer.setEffectivePc(DefaultFlag.NO);
        onlineServer.setDelFlag(DeleteFlag.NO);
        onlineServer.setCreateTime(LocalDateTime.now());

        return onlineServiceRepository.saveAndFlush(onlineServer);
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public OnlineServiceVO wrapperVo(OnlineService onlineService) {
        if (onlineService != null) {
            OnlineServiceVO onlineServiceVO = new OnlineServiceVO();
            KsBeanUtil.copyPropertiesThird(onlineService, onlineServiceVO);
            return onlineServiceVO;
        }
        return null;
    }
}
