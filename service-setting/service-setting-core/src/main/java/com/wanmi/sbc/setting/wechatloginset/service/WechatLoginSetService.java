package com.wanmi.sbc.setting.wechatloginset.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.response.wechatloginset.WechatLoginSetResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.wechatloginset.repository.WechatLoginSetRepository;
import com.wanmi.sbc.setting.wechatloginset.model.root.WechatLoginSet;
import com.wanmi.sbc.setting.bean.vo.WechatLoginSetVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>微信授权登录配置业务逻辑</p>
 *
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@Service("WechatLoginSetService")
public class WechatLoginSetService {
    @Autowired
    private WechatLoginSetRepository wechatLoginSetRepository;

    /**
     * 获取平台授信配置
     *
     * @return
     */
    public WechatLoginSet getLoginSet(WechatLoginSet wechatLoginSet) {
        WechatLoginSet set = wechatLoginSetRepository.findByStoreId(wechatLoginSet.getStoreId()).orElse(null);
        return set != null ? set : WechatLoginSet.getDefault();
    }

    /**
     * 门店查询授信配置
     *
     * @return
     */
    public WechatLoginSet getLoginSetByStoreId(Long storeId) {
        Optional<WechatLoginSet> optional = wechatLoginSetRepository.findByStoreId(storeId);
        return optional.orElse(WechatLoginSet.getDefault());
    }

    /**
     * 新增微信授权登录配置
     *
     * @author lq
     */
    @Transactional
    public void add(WechatLoginSet entity) {
        WechatLoginSet set = wechatLoginSetRepository.findByStoreId(entity.getStoreId()).orElse(null);
        if (set != null) {
            entity.setWechatSetId(set.getWechatSetId());
            entity.setUpdateTime(LocalDateTime.now());
        } else {
            entity.setCreateTime(LocalDateTime.now());
        }
        wechatLoginSetRepository.save(entity);
    }

    /**
     * 单个查询微信授权登录配置
     *
     * @author lq
     */
    public WechatLoginSet getById(String id) {
        return wechatLoginSetRepository.findById(id).orElse(null);
    }


    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public WechatLoginSetVO wrapperVo(WechatLoginSet wechatLoginSet) {
        if (wechatLoginSet != null) {
            WechatLoginSetVO wechatLoginSetVO = new WechatLoginSetVO();
            KsBeanUtil.copyPropertiesThird(wechatLoginSet, wechatLoginSetVO);
            return wechatLoginSetVO;
        }
        return null;
    }

    /**
     * 将实体包装成VO
     *
     * @author lq
     */
    public WechatLoginSetResponse wrapperInfoVo(WechatLoginSet wechatLoginSet) {
        if (wechatLoginSet != null) {
            WechatLoginSetResponse wechatLoginSetResponse = new WechatLoginSetResponse();
            KsBeanUtil.copyPropertiesThird(wechatLoginSet, wechatLoginSetResponse);
            return wechatLoginSetResponse;
        }
        return null;
    }
}
