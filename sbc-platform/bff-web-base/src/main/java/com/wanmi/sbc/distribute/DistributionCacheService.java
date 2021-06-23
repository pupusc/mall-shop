package com.wanmi.sbc.distribute;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.distribute.dto.InviteRegisterDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingGetByStoreIdRequest;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSetting4StoreBagsResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RecruitApplyType;
import com.wanmi.sbc.marketing.bean.enums.RegisterLimitType;
import com.wanmi.sbc.marketing.bean.enums.RewardCashType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionRewardCouponVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingSimVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingVO;
import com.wanmi.sbc.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
     * 查询是否开启社交分销
     */
    public DefaultFlag queryOpenFlag() {
        return this.querySettingCache().getDistributionSetting().getOpenFlag();
    }
    /**
     * 查询是否开启开店
     */
    public DefaultFlag queryShopOpenFlag() {
        return this.querySettingCache().getDistributionSetting().getShopOpenFlag();
    }

    /**
     * 查询是否开启邀新奖励
     * @return
     */
    public DefaultFlag queryInviteFlag() {
        return this.querySettingCache().getDistributionSetting().getInviteFlag();
    }

    /**
     * 查询是否开启邀新奖励限制
     * @return
     */
    public DistributionLimitType queryRewardLimitType() {
        return this.querySettingCache().getDistributionSetting().getRewardLimitType();
    }

    /**
     * 查询邀新奖励金额
     * @return
     */
    public BigDecimal queryRewardCash() {
        return this.querySettingCache().getDistributionSetting().getRewardCash();
    }

    /**
     * 查询是否开启是否开启分销佣金
     * @return
     */
    public DefaultFlag queryCommissionFlag() {
        return this.querySettingCache().getDistributionSetting().getCommissionFlag();
    }

    public RewardCashType queryRewardCashType() {
        return this.querySettingCache().getDistributionSetting().getRewardCashType();
    }

    /**
     * 查询邀新奖励人数上限
     * @return
     */
    public Integer queryRewardCashCount() {
        return this.querySettingCache().getDistributionSetting().getRewardCashCount();
    }

    /**
     * 查询奖励的优惠券列表
     * @return
     */
    public List<CouponInfoVO> queryCouponInfoList() {
        return this.querySettingCache().getCouponInfos();
    }

    /**
     * 查询分销配置
     * @return
     */
    public DistributionSettingVO queryDistributionSetting() {
        return this.querySettingCache().getDistributionSetting();
    }

    /**
     * 查询店铺是否开启社交分销
     */
    public DefaultFlag queryStoreOpenFlag(String storeId) {
        DistributionStoreSettingGetByStoreIdResponse storeSettingGetByStoreIdResponse=this.queryStoreSettingCache(storeId);
        return null==storeSettingGetByStoreIdResponse?DefaultFlag.NO:storeSettingGetByStoreIdResponse.getOpenFlag();
    }

    /**
     * 查询平台端-基础分销设置- 小店名称
     * @return
     */
    public String getShopName() {
        return this.querySettingCache().getDistributionSetting().getShopName();
    }

    /**
     * 查询平台端-基础分销设置- 升级方式 0：购买礼包  1：邀新注册
     * @return
     */
    public RecruitApplyType getApplyType() {
        return this.querySettingCache().getDistributionSetting().getApplyType();
    }

    /**
     * 查询平台端-基础分销设置-分销员的名称
     * @return
     */
    public String getDistributorName() {
        return this.querySettingCache().getDistributionSetting().getDistributorName();
    }

    /**
     *  查询平台端-基础分销设置-分销入口是否打开 0：关闭  1：打开
     * @return
     */
    public DefaultFlag getDistributionApplyFlag() {
        return this.querySettingCache().getDistributionSetting().getApplyFlag();
    }

    /**
     * 查询开店礼包
     * @return
     */
    public DistributionSetting4StoreBagsResponse storeBags(){
        DistributionSettingGetResponse distributionSettingGetResponse=this.querySettingCache();
        DistributionSetting4StoreBagsResponse storeBagsResponse = new DistributionSetting4StoreBagsResponse();
        KsBeanUtil.copyPropertiesThird(distributionSettingGetResponse.getDistributionSetting(),storeBagsResponse);
        if(DefaultFlag.YES==distributionSettingGetResponse.getDistributionSetting().getOpenFlag()
                && RecruitApplyType.BUY==distributionSettingGetResponse.getDistributionSetting().getApplyType()){
            KsBeanUtil.copyPropertiesThird(distributionSettingGetResponse,storeBagsResponse);

        }
       return storeBagsResponse;
    }

    /**
     * 查询邀请注册信息
     */
    public InviteRegisterDTO getInviteRegisterDTO() {
        DistributionSettingVO setting = this.querySettingCache().getDistributionSetting();
        InviteRegisterDTO inviteRegister = new InviteRegisterDTO();
        if (DefaultFlag.YES.equals(setting.getOpenFlag())
                && DefaultFlag.YES.equals(setting.getApplyFlag())
                && RecruitApplyType.REGISTER.equals(setting.getApplyType())) {
            // 开启了邀请注册
            inviteRegister.setEnableFlag(DefaultFlag.YES);
            inviteRegister.setLimitType(setting.getLimitType());
            inviteRegister.setInviteCount(setting.getInviteCount());
        } else {
            // 没有开启邀请注册
            inviteRegister.setEnableFlag(DefaultFlag.NO);
        }
        return inviteRegister;
    }

    /**
     * 获取简单的分销设置信息
     * @return
     */
    public DistributionSettingSimVO getSimDistributionSetting(){
        return KsBeanUtil.convert(queryDistributionSetting(),DistributionSettingSimVO.class);
    }

    /**
     * 查询开店礼包商品
     * @return
     */
    public List<GoodsInfoVO> queryStoreBags() {
        return this.querySettingCache().getGoodsInfos();
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
     * 查询奖励优惠券上限(组数)
     * @return
     */
    public Integer getRewardCouponCount() {
        return this.querySettingCache().getDistributionSetting().getRewardCouponCount();
    }

    /**
     * 查询优惠券信息
     * @return
     */
    public List<CouponInfoVO> getCouponInfos() {
        return this.querySettingCache().getCouponInfos();
    }


    /**
     * 查询优惠券组数信息
     * @return
     */
    public List<DistributionRewardCouponVO> getCouponInfoCounts() {
        return this.querySettingCache().getCouponInfoCounts();
    }

    /**
     * 获取注册限制
     * @return
     */
    public RegisterLimitType getRegisterLimitType() {
        return this.querySettingCache().getDistributionSetting().getRegisterLimitType();
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
}
