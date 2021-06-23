package com.wanmi.sbc.order.common;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.setting.bean.enums.SettingRedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 积分设置缓存处理服务
 * Created by dyt on 2017/8/17.
 */
@Slf4j
@Service
public class SystemPointsConfigService {


    @Autowired
    private SystemPointsConfigQueryProvider systemPointsConfigQueryProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 查询设置缓存
     */
    public SystemPointsConfigQueryResponse querySettingCache() {
        String key = SettingRedisKey.SYSTEM_POINTS_CONFIG.toValue();
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            return JSONObject.parseObject(redisService.getString(key), SystemPointsConfigQueryResponse.class);
        }
        SystemPointsConfigQueryResponse response =systemPointsConfigQueryProvider.querySystemPointsConfig().getContext();
        redisService.setString(key, JSONObject.toJSONString(response));
        return response;
    }

    /**
     * 是否商品积分抵扣
     * @return
     */
    public boolean isGoodsPoint(){
        SystemPointsConfigQueryResponse response = querySettingCache();
        return EnableStatus.ENABLE.equals(response.getStatus()) && PointsUsageFlag.GOODS.equals(response.getPointsUsageFlag());
    }
}