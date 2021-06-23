package com.wanmi.sbc.init.service;

import com.wanmi.sbc.common.constant.VASStatus;
import com.wanmi.sbc.redis.RedisHsetBean;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: songhanlin
 * @Date: Created In 13:44 2020/3/2
 * @Description: 增值服务公用服务类
 */
@Slf4j
@Service
public class VASCommonService {
    @Autowired
    private RedisService redisService;

    @Resource
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private IEPService iepService;

    /**
     * 初始化缓存增值服务信息
     */
    public void init() {
        redisService.delete(ConfigKey.VALUE_ADDED_SERVICES.toString());
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigKey(ConfigKey.VALUE_ADDED_SERVICES.toString());
        List<ConfigVO> list = auditQueryProvider.getByConfigKey(request).getContext().getConfigVOList();

        List<RedisHsetBean> redisHsetBeans = list.stream().map(item -> {
            RedisHsetBean redisHsetBean = new RedisHsetBean();
            redisHsetBean.setField(item.getConfigType());
            redisHsetBean.setValue(item.getStatus() == 0 ? VASStatus.DISABLE.toValue() :
                    VASStatus.ENABLE.toValue());
            return redisHsetBean;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(redisHsetBeans)) {
            iepService.init(list);
            redisService.hsetPipeline(ConfigKey.VALUE_ADDED_SERVICES.toString(), redisHsetBeans);
        } else {
            log.info("无增值服务");
        }
    }

}
