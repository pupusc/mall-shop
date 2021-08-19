package com.wanmi.sbc.distribution;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingGetByStoreIdRequest;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RewardCashType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:32 2019/3/5
 * @Description: 分销设置缓存服务
 */
@Service
public class DistributionCacheService {

    private static final String SETTING_KEY = "DIS_SETTING";

    private static final String STORE_SETTING_KEY = "DIS_STORE_SETTING";

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 查询设置缓存
     */
    private DistributionSettingGetResponse querySettingCache() {
        boolean hasKey = redisService.hasKey(SETTING_KEY);
        if (hasKey) {
            return JSONObject.parseObject(redisService.getString(SETTING_KEY), DistributionSettingGetResponse.class);
        } else {
            DistributionSettingGetResponse setting = distributionSettingQueryProvider.getSetting().getContext();
            redisService.setString(SETTING_KEY, JSONObject.toJSONString(setting));
            return setting;
        }
    }

    /**
     * 查询店铺设置缓存
     */
    private DistributionStoreSettingGetByStoreIdResponse queryStoreSettingCache(String storeId) {
        String key = STORE_SETTING_KEY + storeId;
        boolean hasKey = redisService.hasKey(key);
        if (hasKey) {
            return JSONObject.parseObject(redisService.getString(key), DistributionStoreSettingGetByStoreIdResponse.class);
        } else {
            DistributionStoreSettingGetByStoreIdResponse setting = distributionSettingQueryProvider.getStoreSettingByStoreId(
                    new DistributionStoreSettingGetByStoreIdRequest(storeId)).getContext();
            redisService.setString(key, JSONObject.toJSONString(setting));
            return setting;
        }
    }

    /**
     * 查询店铺是否开启社交分销
     */
    public DefaultFlag queryStoreOpenFlag(String storeId) {
        DistributionStoreSettingGetByStoreIdResponse storeSettingGetByStoreIdResponse = this.queryStoreSettingCache(storeId);
        return null == storeSettingGetByStoreIdResponse ? DefaultFlag.NO : storeSettingGetByStoreIdResponse.getOpenFlag();
    }

    /**
     * 查询是否开启社交分销
     */
    public DefaultFlag queryOpenFlag() {
        return this.querySettingCache().getDistributionSetting().getOpenFlag();
    }

    /**
     * 查询是否开启是否开启分销佣金
     * @return
     */
    public DefaultFlag queryCommissionFlag() {
        return this.querySettingCache().getDistributionSetting().getCommissionFlag();
    }

    /**
     * 查询是否开启奖励现金开关
     * @return
     */
    public DefaultFlag getRewardCashFlag() {
        return this.querySettingCache().getDistributionSetting().getRewardCashFlag();
    }

    /**
     * 查询是否开启奖励优惠券
     * @return
     */
    public DefaultFlag getRewardCouponFlag() {
        return this.querySettingCache().getDistributionSetting().getRewardCouponFlag();
    }

    /**
     * 奖励上限类型设置
     * @return
     */
    public RewardCashType queryRewardCashType() {
        return this.querySettingCache().getDistributionSetting().getRewardCashType();
    }

    /**
     * 查询奖励优惠券上限(组数)
     * @return
     */
    public Integer getRewardCouponCount() {
        return this.querySettingCache().getDistributionSetting().getRewardCouponCount();
    }

    /**
     * 查询邀新奖励人数上限
     * @return
     */
    public Integer queryRewardCashCount() {
        return this.querySettingCache().getDistributionSetting().getRewardCashCount();
    }

    /**
     * 获取分销员等级设置信息
     * @return
     */
    public List<DistributorLevelVO> getDistributorLevels() {
        return this.querySettingCache().getDistributorLevels();
    }

    /**
     * 基础邀新奖励限制
     * @return
     */
    public DistributionLimitType getBaseLimitType(){
        return this.querySettingCache().getDistributionSetting().getBaseLimitType();
    }

    /**
     * 查询优惠券信息
     * @return
     */
    public List<CouponInfoVO> getCouponInfos() {
        return this.querySettingCache().getCouponInfos();
    }

}
