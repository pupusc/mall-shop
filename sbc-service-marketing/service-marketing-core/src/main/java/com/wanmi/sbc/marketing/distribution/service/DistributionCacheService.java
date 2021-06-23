package com.wanmi.sbc.marketing.distribution.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.marketing.api.response.distribution.MultistageSettingGetResponse;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingCacheVO;
import com.wanmi.sbc.marketing.cache.MarketingCacheService;
import com.wanmi.sbc.marketing.cache.WmCacheConfig;
import com.wanmi.sbc.marketing.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:35 2019/3/25
 * @Description: 分销缓存服务
 */
@Service
public class DistributionCacheService {

    private static final String SETTING_KEY = "DIS_SETTING";

    private static final String STORE_SETTING_KEY = "DIS_STORE_SETTING";

    @Autowired
    private RedisService redisService;

    @Autowired
    private DistributionSettingService distributionSettingService;

    @Autowired
    private MarketingCacheService marketingCacheService;

    /**
     * 保存分销设置
     */
    public void saveSetting(DistributionSettingGetResponse setting) {
        redisService.setString(SETTING_KEY, JSONObject.toJSONString(setting));
    }

    /**
     * 保存店铺分销设置
     */
    public void saveStoreSetting(DistributionStoreSettingGetByStoreIdResponse storeSetting) {
        redisService.setString(STORE_SETTING_KEY + storeSetting.getStoreId(),
                JSONObject.toJSONString(storeSetting));
    }

    /**
     * 查询是否开启社交分销
     */
    public DefaultFlag queryOpenFlag() {
        return marketingCacheService.queryDistributionSetting().getDistributionSetting().getOpenFlag();
    }

    /**
     * 查询店铺是否开启社交分销
     */
    public DefaultFlag queryStoreOpenFlag(String storeId) {
        DistributionStoreSettingGetByStoreIdResponse storeSettingGetByStoreIdResponse = marketingCacheService.queryDistributionStoreSetting(storeId);
        return null == storeSettingGetByStoreIdResponse ? DefaultFlag.NO : storeSettingGetByStoreIdResponse.getOpenFlag();
    }

    /**
     * 查询多级分销设置信息
     */
    public MultistageSettingGetResponse getMultistageSetting() {
        DistributionSettingCacheVO setting = marketingCacheService.queryDistributionSetting();
        MultistageSettingGetResponse multistageSetting = new MultistageSettingGetResponse();
        multistageSetting.setCommissionUnhookType(setting.getDistributionSetting().getCommissionUnhookType());
        multistageSetting.setCommissionPriorityType(setting.getDistributionSetting().getCommissionPriorityType());
        multistageSetting.setDistributorLevels(setting.getDistributorLevels());
        return multistageSetting;
    }

}
