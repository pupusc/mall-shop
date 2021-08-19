package com.wanmi.sbc.marketing.distribution.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelBatchSaveRequest;
import com.wanmi.sbc.customer.api.request.distribution.NormalDistributorLevelNameUpdateRequest;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewPageResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.marketing.api.request.distribution.*;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSimSettingResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingListByStoreIdsResponse;
import com.wanmi.sbc.marketing.bean.dto.DistributionRewardCouponDTO;
import com.wanmi.sbc.marketing.bean.enums.*;
import com.wanmi.sbc.marketing.bean.vo.*;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivity;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityConfigRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponActivityRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.distribution.model.DistributionRecruitGift;
import com.wanmi.sbc.marketing.distribution.model.DistributionRewardCoupon;
import com.wanmi.sbc.marketing.distribution.model.DistributionSetting;
import com.wanmi.sbc.marketing.distribution.model.DistributionStoreSetting;
import com.wanmi.sbc.marketing.distribution.repository.DistributionRecruitGiftRepository;
import com.wanmi.sbc.marketing.distribution.repository.DistributionRewardCouponRepository;
import com.wanmi.sbc.marketing.distribution.repository.DistributionSettingRepository;
import com.wanmi.sbc.marketing.distribution.repository.DistributionStoreSettingRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>分销设置业务逻辑</p>
 *
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@Service("DistributionSettingService")
public class DistributionSettingService {

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private DistributionSettingRepository settingRepository;

    @Autowired
    private DistributionRewardCouponRepository rewardCouponRepository;

    @Autowired
    private DistributionRecruitGiftRepository recruitGiftRepository;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private DistributionStoreSettingRepository storeSettingRepository;

    @Autowired
    private CouponActivityConfigRepository couponActivityConfigRepository;

    @Autowired
    private CouponActivityRepository couponActivityRepository;

    @Autowired
    private DistributorLevelProvider distributorLevelProvider;

    @Autowired
    private DistributorLevelQueryProvider distributorLevelQueryProvider;

    /**
     * 查询分销设置
     */
    @Transactional
    public DistributionSettingGetResponse querySetting() {

        DistributionSettingGetResponse response = new DistributionSettingGetResponse();

        // 1.查询分销设置
        List<DistributionSetting> settings = settingRepository.findAll();
        DistributionSetting setting;
        if (CollectionUtils.isEmpty(settings)) {
            // 如果设置为空，初始化设置
            setting = initSetting();
        } else {
            setting = settings.get(0);
        }

        response.setDistributionSetting(KsBeanUtil.convert(setting, DistributionSettingVO.class));

        // 2.查询分销奖励的优惠券
        List<DistributionRewardCoupon> rewardCoupons = rewardCouponRepository.findAll();
        if (CollectionUtils.isNotEmpty(rewardCoupons)) {
            List<CouponInfo> couponInfos = couponInfoRepository.queryByIds(
                    rewardCoupons.stream().map(DistributionRewardCoupon::getCouponId).collect(Collectors.toList()));
            response.setCouponInfos(KsBeanUtil.convert(couponInfos, CouponInfoVO.class));
        }
        response.setCouponInfoCounts(KsBeanUtil.convert(rewardCoupons, DistributionRewardCouponVO.class));

        // 3.查询分销大礼包商品
        List<DistributionRecruitGift> recruitGifts = recruitGiftRepository.findAll();
        List<String> goodsInfoIds = recruitGifts.stream()
                .map(DistributionRecruitGift::getGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            GoodsInfoViewPageRequest queryRequest = new GoodsInfoViewPageRequest();
            queryRequest.setPageSize(100);
            queryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
            queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
            queryRequest.setGoodsInfoIds(goodsInfoIds);
            GoodsInfoViewPageResponse goodsInfoResponse = goodsInfoQueryProvider.pageView(queryRequest).getContext();
            response.setGoodsInfos(goodsInfoResponse.getGoodsInfoPage().getContent());
            response.setBrands(goodsInfoResponse.getBrands());
            response.setCates(goodsInfoResponse.getCates());
        }

        // 4.查询分销员等级列表
        List<DistributorLevelVO> distributorLevels = distributorLevelQueryProvider.listAll().getContext().getDistributorLevelList();
        if (CollectionUtils.isEmpty(distributorLevels)) {
            distributorLevels = initDistributorLevel();
        }
        response.setDistributorLevels(distributorLevels);

        return response;

    }

    /**
     * 简单查询分销设置
     */
    public DistributionSimSettingResponse querySimSetting() {

        DistributionSimSettingResponse response = new DistributionSimSettingResponse();
        // 1.查询分销设置
        DistributionSetting setting = settingRepository.findAll().get(0);
        response.setDistributionSettingSimVO(KsBeanUtil.convert(setting,DistributionSettingSimVO.class));
        return response;

    }

    /**
     * 保存社交分销开关状态
     */
    @Transactional
    public void saveOpenFlag(DefaultFlag openFlag) {
        DistributionSetting setting = settingRepository.findAll().get(0);
        setting.setOpenFlag(openFlag);
        settingRepository.save(setting);
        distributionCacheService.saveSetting(this.querySetting());
    }

    /**
     * 保存基础设置
     */
    @Transactional
    @GlobalTransactional
    public void saveBasic(DistributionBasicSettingSaveRequest request) {
        DistributionSetting setting = settingRepository.findAll().get(0);
        BeanUtils.copyProperties(request, setting);
        setting.setRewardLimitType(setting.getBaseLimitType());
        setting.setLimitType(setting.getBaseLimitType());
        settingRepository.save(setting);
        distributorLevelProvider.updateNormalDistributorLevelName(
                new NormalDistributorLevelNameUpdateRequest(setting.getDistributorName())
        );
        distributionCacheService.saveSetting(this.querySetting());
    }

    /**
     * 保存分销员招募设置
     */
    @Transactional
    public void saveRecruit(DistributionRecruitSettingSaveRequest request) {

        // 1.保存招募信息
        DistributionSetting setting = settingRepository.findAll().get(0);
        BeanUtils.copyProperties(request, setting);
        settingRepository.save(setting);

        // 2.保存分销大礼包商品
        recruitGiftRepository.deleteAll();
        if (CollectionUtils.isNotEmpty(request.getGoodsInfoIds())) {
            List<DistributionRecruitGift> goodsInfos = request.getGoodsInfoIds().stream().map(goodsInfoId -> {
                DistributionRecruitGift goodsInfo = new DistributionRecruitGift();
                goodsInfo.setGoodsInfoId(goodsInfoId);
                return goodsInfo;
            }).collect(Collectors.toList());
            recruitGiftRepository.saveAll(goodsInfos);
        }

        // 3.刷新redis
        distributionCacheService.saveSetting(this.querySetting());
    }

    /**
     * 保存奖励模式设置
     */
    @Transactional
    public void saveReward(DistributionRewardSettingSaveRequest request) {

        // 1.保存奖励信息
        DistributionSetting setting = settingRepository.findAll().get(0);
        BeanUtils.copyProperties(request, setting);
        settingRepository.save(setting);

        // 2.保存分销奖励的优惠券
        rewardCouponRepository.deleteAll();
        if (CollectionUtils.isNotEmpty(request.getChooseCoupons())) {
            appendConfigsToActivity(request.getChooseCoupons());
            rewardCouponRepository.saveAll(
                    KsBeanUtil.convert(request.getChooseCoupons(), DistributionRewardCoupon.class));
        }

        // 3.刷新redis
        distributionCacheService.saveSetting(this.querySetting());
    }

    /**
     * 保存多级分销设置
     */
    @Transactional
    @GlobalTransactional
    public void saveMultistage(DistributionMultistageSettingSaveRequest request) {

        // 1.保存多级分销信息
        DistributionSetting setting = settingRepository.findAll().get(0);
        BeanUtils.copyProperties(request, setting);
        settingRepository.save(setting);

        // 2.保存分销员等级列表
        distributorLevelProvider.batchSave(
                new DistributorLevelBatchSaveRequest(request.getDistributorLevels())
        );
        // 3.刷新redis
        distributionCacheService.saveSetting(this.querySetting());
    }

    /**
     * 向"分销邀新赠券活动"中追加优惠券
     */
    private void appendConfigsToActivity(List<DistributionRewardCouponDTO> chooseCoupons) {
        // 1.找出分销邀新赠券活动
        CouponActivity activity = couponActivityRepository.findDistributeCouponActivity();

        if(Objects.isNull(activity)){
            // 初始化分销邀新赠券活动
            CouponActivity newActivity = new CouponActivity();
            newActivity.setActivityName("分销邀新赠券活动");
            newActivity.setCouponActivityType(CouponActivityType.DISTRIBUTE_COUPON);
            newActivity.setReceiveType(DefaultFlag.NO);
            newActivity.setJoinLevel("-1");
            newActivity.setStoreId(-1L);
            newActivity.setPlatformFlag(DefaultFlag.NO);
            newActivity.setPauseFlag(DefaultFlag.NO);
            newActivity.setDelFlag(DeleteFlag.NO);
            activity = couponActivityRepository.save(newActivity);
        }

        List<String> couponIds = new ArrayList<>();

        if(Objects.isNull(activity)) {
            couponIds = couponActivityConfigRepository.findByActivityId(activity.getActivityId())
                    .stream().map(item -> item.getCouponId()).collect(Collectors.toList());
        }

        List<CouponActivityConfig> couponActivityConfigs = new ArrayList<>();
        // 2.过滤出活动还未关联的优惠券
        if(CollectionUtils.isNotEmpty(couponIds)) {
            List<String> finalCouponIds = couponIds;
            CouponActivity finalActivity = activity;
            couponActivityConfigs = chooseCoupons.stream().map(item -> {
                CouponActivityConfig config = new CouponActivityConfig();
                config.setActivityId(finalActivity.getActivityId());
                config.setCouponId(item.getCouponId());
                config.setTotalCount(0L);
                config.setHasLeft(DefaultFlag.NO);
                return config;
            }).filter(item -> !finalCouponIds.contains(item.getCouponId())).collect(Collectors.toList());
        }

        // 3.追加优惠券
        if (CollectionUtils.isNotEmpty(couponActivityConfigs)) {
            couponActivityConfigRepository.saveAll(couponActivityConfigs);
        }
    }


    /**
     * 查询店铺分销设置
     */
    @Transactional
    public DistributionStoreSettingGetByStoreIdResponse queryStoreSetting(String storeId) {
        DistributionStoreSetting setting = storeSettingRepository.findByStoreId(storeId);
        if (Objects.isNull(setting)) {
            setting = new DistributionStoreSetting();
            setting.setStoreId(storeId);
            setting.setOpenFlag(DefaultFlag.NO);
            setting.setCommissionFlag(DefaultFlag.NO);
            setting.setCommission(BigDecimal.ZERO);
            storeSettingRepository.save(setting);
        }
        return KsBeanUtil.convert(setting, DistributionStoreSettingGetByStoreIdResponse.class);
    }

    /**
     * 根据店铺ID集合查询分销设置
     */
    public DistributionStoreSettingListByStoreIdsResponse findByStoreIdIn(List<String> storeIds) {
        List<DistributionStoreSetting> storeSettingList = storeSettingRepository.findByStoreIdIn(storeIds);
        return new DistributionStoreSettingListByStoreIdsResponse(KsBeanUtil.convert(storeSettingList, DistributionStoreSettingVO.class));
    }

    /**
     * 保存店铺分销设置
     */
    @Transactional
    public void saveStoreSetting(DistributionStoreSettingSaveRequest request) {
        DistributionStoreSetting setting = storeSettingRepository.findByStoreId(request.getStoreId());
        if (setting != null) {
            request.setSettingId(setting.getSettingId());
        }
        storeSettingRepository.save(KsBeanUtil.convert(request, DistributionStoreSetting.class));
        distributionCacheService.saveStoreSetting(this.queryStoreSetting(request.getStoreId()));
    }

    /**
     * 查询分销设置中商品审核开关
     */
    public Boolean queryDistributionGoodsSwitch() {
        DistributionSetting setting = settingRepository.findAll().get(0);
        // 分销开关打开，并且商品审核开关关闭
        if (setting.getOpenFlag() == DefaultFlag.YES && setting.getGoodsAuditFlag() == DefaultFlag.NO){
            // 不用审核
            return Boolean.TRUE;
        }
        // 需要审核
        return Boolean.FALSE;
    }

    /**
     * 初始化设置
     */
    @Transactional
    @GlobalTransactional
    public DistributionSetting initSetting() {
        // 初始化设置表
        DistributionSetting setting = new DistributionSetting();
        setting.setOpenFlag(DefaultFlag.NO);
        setting.setDistributorName("分销员");
        setting.setCommissionFlag(DefaultFlag.YES);
        setting.setShopOpenFlag(DefaultFlag.NO);
        setting.setApplyFlag(DefaultFlag.NO);
        setting.setGoodsAuditFlag(DefaultFlag.YES);
        setting.setInviteFlag(DefaultFlag.NO);
        setting.setRegisterLimitType(RegisterLimitType.UNLIMITED);
        setting.setCommissionPriorityType(CommissionPriorityType.STORE);
        setting.setCommissionUnhookType(CommissionUnhookType.UNLIMITED);
        setting.setBaseLimitType(DistributionLimitType.UNLIMITED);
        setting.setLimitType(DistributionLimitType.UNLIMITED);
        setting.setRewardLimitType(DistributionLimitType.UNLIMITED);
        settingRepository.save(setting);

        // 初始化分销邀新赠券活动
        CouponActivity newActivity = new CouponActivity();
        newActivity.setActivityName("分销邀新赠券活动");
        newActivity.setCouponActivityType(CouponActivityType.DISTRIBUTE_COUPON);
        newActivity.setReceiveType(DefaultFlag.NO);
        newActivity.setJoinLevel("-1");
        newActivity.setStoreId(-1L);
        newActivity.setPlatformFlag(DefaultFlag.NO);
        newActivity.setPauseFlag(DefaultFlag.NO);
        newActivity.setDelFlag(DeleteFlag.NO);
        couponActivityRepository.save(newActivity);

        // 初始化分销员等级
        this.initDistributorLevel();

        return setting;
    }

    /**
     * 初始化分销员等级
     */
    private List<DistributorLevelVO> initDistributorLevel() {
        return distributorLevelProvider.initDistributorLevel().getContext().getDistributorLevels();
    }

}
