package com.wanmi.sbc.init.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.VASStatus;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingProvider;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingQueryProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: songhanlin
 * @Date: Created In 13:37 2020/3/2
 * @Description: 企业购服务层
 */
@Slf4j
@Service
public class IEPService {

    @Autowired
    private IepSettingQueryProvider iepSettingQueryProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 初始化企业购设置信息
     *
     * @param list
     */
    public void init(List<ConfigVO> list) {
        // 删除企业购配置信息
        redisService.delete(CacheKeyConstant.IEP_SETTING);
        Optional<ConfigVO> configOptional = list.stream().filter(s -> StringUtils.equals(s.getConfigType(),
                ConfigType.VAS_IEP_SETTING.toValue())).findFirst();
        if (configOptional.isPresent()) {
            ConfigVO configVO = configOptional.get();
            if(configVO.getStatus() == 1) {
                iepSettingQueryProvider.cacheIepSetting();
            }
        } else {
            // 如果需要, 可以隐藏此处日志
            log.info("无企业购增值服务");
        }
    }
}
