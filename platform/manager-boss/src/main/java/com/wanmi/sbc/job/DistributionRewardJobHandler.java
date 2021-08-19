package com.wanmi.sbc.job;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.request.funds.GrantAmountRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdForEsResponse;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.*;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewListRequest;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewSupplyAgainRequest;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerInviteInfoPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.EsDistributionCustomerByIdResponse;
import com.wanmi.sbc.customer.bean.enums.FailReasonFlag;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.bean.enums.RewardFlag;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerInviteInfoVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewListVO;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerProvider;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerAddRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsRequest;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.SendCouponGroupRequest;
import com.wanmi.sbc.marketing.api.response.coupon.GetCouponGroupResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RewardCashType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionRewardCouponVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务Handler
 * 根据分销设置的奖励规则补发邀请奖励
 */
@JobHandler(value = "distributionRewardJobHandler")
@Component
@Slf4j
public class DistributionRewardJobHandler extends IJobHandler {


    /**
     * 后台步长
     */
    private static final int STEP = 30;
    /**
     * 分销设置
     */
    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;


    /**
     * 邀新记录
     */
    @Autowired
    private DistributionInviteNewQueryProvider distributionInviteNewQueryProvider;

    /**
     * 邀新记录
     */
    @Autowired
    private DistributionInviteNewProvider distributionInviteNewProvider;

    /**
     * 会员资金
     */
    @Autowired
    private CustomerFundsProvider customerFundsProvider;

    /**
     * 优惠券活动
     */
    @Autowired
    private CouponActivityProvider couponActivityProvider;

    /**
     * 注入客户查询provider
     */
    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    /***
     *分销用户信息
     */
    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;


    /**
     * 分销员
     */
    @Autowired
    private DistributionCustomerSaveProvider distributionCustomerSaveProvider;


    /**
     * 分销用户邀新信息
     */
    @Autowired
    private DistributionCustomerInviteInfoQueryProvider distributionCustomerInviteInfoQueryProvider;

    /**
     * 分销用户邀新信息
     */
    @Autowired
    private DistributionCustomerInviteInfoSaveProvider distributionCustomerInviteInfoSaveProvider;

    @Autowired
    private CouponActivityQueryProvider couponActivityQueryProvider;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private EsCustomerFundsProvider esCustomerFundsProvider;

    @Autowired
    private EsDistributionCustomerProvider esDistributionCustomerProvider;


    @Override
    @Transactional
    public ReturnT<String> execute(String param) throws Exception {
        //获取分销设置
        DistributionSettingGetResponse distributionSetting = distributionSettingQueryProvider.getSetting().getContext();
        XxlJobLogger.log("此次分销设置为:" + JSONObject.toJSONString(distributionSetting));
        //处理邀新奖励现金
        dealRewardCash(distributionSetting);
        //处理邀新奖励优惠券
        dealRewardCoupon(distributionSetting);
        return SUCCESS;
    }

    /**
     * 处理邀新奖励现金
     *
     * @param distributionSettingGetResponse
     */
    private void dealRewardCash(DistributionSettingGetResponse distributionSettingGetResponse) {
        DistributionSettingVO distributionSetting = distributionSettingGetResponse.getDistributionSetting();
        //1.开启分销 2.开启邀新奖励 3.勾选奖励现金
        Boolean openFlag = DefaultFlag.YES.equals(distributionSetting.getOpenFlag()) && DefaultFlag.YES.equals(distributionSetting.getInviteFlag()) && DefaultFlag.YES.equals(distributionSetting.getRewardCashFlag());
        XxlJobLogger.log("此次分销设置邀新奖励现金openFlag为:" + openFlag);
        //处理邀新奖励现金
        if (openFlag) {
            while (true) {
                if (!getDistributionCustomer4RewardCash(distributionSetting)) {
                    break;
                }
            }
        }
    }

    /**
     * 处理邀新奖励现金
     * 查询满足补发条件的用户
     *
     * @param distributionSetting
     */
    private boolean getDistributionCustomer4RewardCash(DistributionSettingVO distributionSetting) {
        DistributionCustomerInviteInfoPageRequest distributionCustomerInviteInfoPageReq = new DistributionCustomerInviteInfoPageRequest();
        distributionCustomerInviteInfoPageReq.setPageSize(STEP);
        //邀新奖励设置奖励上限限制x人
        if (RewardCashType.LIMITED.equals(distributionSetting.getRewardCashType())) {
            // 已发放邀新奖励现金人数<x
            distributionCustomerInviteInfoPageReq.setRewardCashCountEnd(distributionSetting.getRewardCashCount());
        }

        //邀新奖励设置-仅限有效邀新
        if (DistributionLimitType.EFFECTIVE.equals(distributionSetting.getRewardLimitType())) {
            //存在达到上限未发放奖励现金有效邀新人数
            distributionCustomerInviteInfoPageReq.setRewardCashAvailableLimitCountStart(0);
        } else {
            //存在达到上限未发放奖励现金人数
            distributionCustomerInviteInfoPageReq.setRewardCashLimitCountStart(0);
        }
        //查询满足补发条件的用户
        BaseResponse<DistributionCustomerInviteInfoPageResponse> distributionCustomerInviteInfoPage = distributionCustomerInviteInfoQueryProvider.page(distributionCustomerInviteInfoPageReq);
        if (Objects.isNull(distributionCustomerInviteInfoPage.getContext()) || CollectionUtils.isEmpty(distributionCustomerInviteInfoPage.getContext().getDistributionCustomerInviteInfoVOPage().getContent())) {
            XxlJobLogger.log("distributionRewardJobHandler补发奖励金:没有满足补发条件的用户");
            return false;
        }

        List<DistributionCustomerInviteInfoVO> distributionCustomerInviteInfoVOList = distributionCustomerInviteInfoPage.getContext().getDistributionCustomerInviteInfoVOPage().getContent();
        distributionCustomerInviteInfoVOList.forEach(vo -> {
            //查询需要补发的数据
            List<DistributionInviteNewListVO> inviteNewListVOList = getDistributionInviteNew4RewardCash(vo, distributionSetting);
            XxlJobLogger.log("distributionRewardJobHandler补发奖励金:1、用户".concat(JSONObject.toJSONString(vo)));
            XxlJobLogger.log("distributionRewardJobHandler补发奖励金:2、分销记录信息".concat(JSONObject.toJSONString(inviteNewListVOList)));
            if (CollectionUtils.isNotEmpty(inviteNewListVOList)) {
                //开始补发
                //1.修改分销员信息
                updateDistributor4Reward(vo, inviteNewListVOList, distributionSetting);

                inviteNewListVOList.forEach(inviteNewListVO -> {
                    //2:修改邀新记录
                    updateInviteNew4RewardCash(inviteNewListVO, distributionSetting);
                    //3.会员资金
                    dealAmount(vo, distributionSetting.getRewardCash());
                });
            } else {
                XxlJobLogger.log("distributionRewardJobHandler补发奖奖金:校正数据".concat(vo.getCustomerId()));
                DistributorCustomerInviteInfoReviseRequest request = new DistributorCustomerInviteInfoReviseRequest();
                request.setCustomerId(vo.getCustomerId());
                request.setDistributionId(vo.getDistributionId());
                // 校正
                distributionCustomerInviteInfoSaveProvider.revise(request);
            }
        });
        return true;
    }

    /**
     * 修改邀新记录为已发放奖励
     *
     * @return
     */
    void updateInviteNew4RewardCash(DistributionInviteNewListVO inviteNewListVO, DistributionSettingVO distributionSetting) {
        DistributionInviteNewSupplyAgainRequest request = new DistributionInviteNewSupplyAgainRequest();
        request.setRecordId(inviteNewListVO.getRecordId());
        request.setRewardCash(distributionSetting.getRewardCash());
        distributionInviteNewProvider.updateBySupplyAgain(request);
    }

    /**
     * 修改邀新记录为已发放奖励
     *
     * @return
     */
    void updateInviteNew4RewardCoupon(DistributionInviteNewListVO inviteNewListVO, DistributionSettingVO distributionSetting) {
        DistributionInviteNewSupplyAgainRequest request = new DistributionInviteNewSupplyAgainRequest();
        request.setRecordId(inviteNewListVO.getRecordId());
        // 优惠券名称
        request.setRewardCoupon(inviteNewListVO.getRewardCoupon());
        distributionInviteNewProvider.updateBySupplyAgain(request);
    }

    /**
     * 修改分销员信息
     *
     * @return
     */
    void updateDistributor4Reward(DistributionCustomerInviteInfoVO distributionCustomerInviteInfoVO, List<DistributionInviteNewListVO> inviteNewListVOList, DistributionSettingVO distributionSetting) {
        AfterSupplyAgainUpdateDistributorRequest request = new AfterSupplyAgainUpdateDistributorRequest();
        //发放邀新奖金人数
        Long rewardCashCount = inviteNewListVOList.stream().filter(vo -> RewardFlag.CASH.equals(vo.getRewardFlag())).count();
        //发放邀新奖金人数--有效邀新有几个
        Long rewardCashAvailableLimitCount = inviteNewListVOList.stream().filter(vo -> RewardFlag.CASH.equals(vo.getRewardFlag()) && InvalidFlag.YES.equals(vo.getAvailableDistribution())).count();
        //未入账邀新奖金扣除预计奖励金额
        BigDecimal rewardCashNotRecorded = inviteNewListVOList.stream().filter(vo -> RewardFlag.CASH.equals(vo.getRewardFlag())).map(vo -> vo.getSettingAmount()).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
        //预计发放邀新奖励金额
        BigDecimal rewardCash = distributionSetting.getRewardCash().multiply(new BigDecimal(rewardCashCount));
        //发放邀新优惠券人数
        Long rewardCouponCount = inviteNewListVOList.stream().filter(vo -> RewardFlag.COUPON.equals(vo.getRewardFlag())).count();
        //发放邀新优惠券人数--有效邀新有几个
        Long rewardCouponAvailableLimitCount = inviteNewListVOList.stream().filter(vo -> RewardFlag.COUPON.equals(vo.getRewardFlag()) && InvalidFlag.YES.equals(vo.getAvailableDistribution())).count();

        if (rewardCash.compareTo(BigDecimal.ZERO) > 0) {
            String distributionId = distributionCustomerInviteInfoVO.getDistributionId();
            request.setDistributeId(distributionId);
            request.setRewardCash(rewardCash);
            request.setRewardCashNotRecorded(rewardCashNotRecorded);
            distributionCustomerSaveProvider.afterSupplyAgainUpdate(request);
            DistributionCustomerByIdRequest idRequest = new DistributionCustomerByIdRequest(distributionId);
            EsDistributionCustomerByIdResponse idResponse = distributionCustomerQueryProvider.getByIdShowPhone(idRequest).getContext();
            DistributionCustomerShowPhoneVO distributionCustomerVO = idResponse.getDistributionCustomerVO();
            if (Objects.nonNull(distributionCustomerVO)) {
                EsDistributionCustomerAddRequest addRequest = EsDistributionCustomerAddRequest.builder()
                        .list(Collections.singletonList(distributionCustomerVO))
                        .build();
                esDistributionCustomerProvider.add(addRequest);
            }
        }

        //修改分销员邀请信息
        DistributorCustomerInviteInfoUpdateRequest distributorCustomerInviteInfoUpdateRequest = new DistributorCustomerInviteInfoUpdateRequest();
        distributorCustomerInviteInfoUpdateRequest.setDistributeId(distributionCustomerInviteInfoVO.getDistributionId());
        distributorCustomerInviteInfoUpdateRequest.setRewardCashCount(rewardCashCount.intValue());
        distributorCustomerInviteInfoUpdateRequest.setRewardCashLimitCount(rewardCashCount.intValue());
        distributorCustomerInviteInfoUpdateRequest.setRewardCashAvailableLimitCount(rewardCashAvailableLimitCount.intValue());
        distributorCustomerInviteInfoUpdateRequest.setRewardCouponCount(rewardCouponCount.intValue());
        distributorCustomerInviteInfoUpdateRequest.setRewardCouponLimitCount(rewardCouponCount.intValue());
        distributorCustomerInviteInfoUpdateRequest.setRewardCouponAvailableLimitCount(rewardCouponAvailableLimitCount.intValue());
        distributionCustomerInviteInfoSaveProvider.afterSupplyAgainUpdate(distributorCustomerInviteInfoUpdateRequest);
    }


    /**
     * 发放邀新奖励
     */
    private void dealAmount(DistributionCustomerInviteInfoVO distributionCustomerInviteInfoVO, BigDecimal amount) {
        if (new BigDecimal(0).equals(amount)) {
            return;
        }
        GrantAmountRequest request = new GrantAmountRequest();
        request.setAmount(amount);
        request.setCustomerId(distributionCustomerInviteInfoVO.getCustomerId());
        request.setDateTime(LocalDateTime.now());
        request.setType(FundsType.INVITE_NEW_AWARDS);
        customerFundsProvider.grantAmount(request);

        //新增更改会员资金es
        CustomerFundsByCustomerIdRequest byCustomerIdRequest = new CustomerFundsByCustomerIdRequest();
        byCustomerIdRequest.setCustomerId(distributionCustomerInviteInfoVO.getCustomerId());
        BaseResponse<CustomerFundsByCustomerIdForEsResponse> response = customerFundsQueryProvider.getByCustomerIdForEs(byCustomerIdRequest);
        if (Objects.nonNull(response.getContext())) {
            esCustomerFundsProvider.initCustomerFunds(KsBeanUtil.convert(response.getContext(), EsCustomerFundsRequest.class));
        }

    }

    /**
     * 查询需要补发现金的邀新数据
     *
     * @param distributionCustomerInviteInfoVO
     * @param distributionSetting
     */
    private List<DistributionInviteNewListVO> getDistributionInviteNew4RewardCash(DistributionCustomerInviteInfoVO distributionCustomerInviteInfoVO, DistributionSettingVO distributionSetting) {
        //查询需要补发的邀新数据
        DistributionInviteNewListRequest request = new DistributionInviteNewListRequest();
        request.setRequestCustomerId(distributionCustomerInviteInfoVO.getCustomerId());
        request.setIsRewardRecorded(InvalidFlag.NO);
        //追加条件--1.达到上限未发放 2.入账类型 现金
        request.setRewardFlag(RewardFlag.CASH);
        request.setFailReasonFlag(FailReasonFlag.LIMITED);
        //限制有效邀新
        if (DistributionLimitType.EFFECTIVE.equals(distributionSetting.getRewardLimitType())) {
            request.setAvailableDistribution(InvalidFlag.YES);
        }
        DistributionInviteNewListResponse distributionInviteNewListResponse = distributionInviteNewQueryProvider.listDistributionInviteNewRecord(request).getContext();
        if (CollectionUtils.isNotEmpty(distributionInviteNewListResponse.getInviteNewListVOList())) {
            List<DistributionInviteNewListVO> inviteNewListVOList = distributionInviteNewListResponse.getInviteNewListVOList();
            //设置奖励上限x
            if (RewardCashType.LIMITED.equals(distributionSetting.getRewardCashType())) {
                // 如果设置奖励上限，按照(奖励上限-已发放奖励)进行补发
                int limitCount = distributionSetting.getRewardCashCount() - distributionCustomerInviteInfoVO.getRewardCashCount();
                if (limitCount - inviteNewListVOList.size() < 0) {
                    //需要补发的数据
                    inviteNewListVOList = inviteNewListVOList.stream().limit(limitCount).collect(Collectors.toList());
                }
            }
            return inviteNewListVOList;
        }
        return Collections.emptyList();
    }


    /**
     * 处理邀新奖励优惠券
     *
     * @param distributionSettingGetResponse
     */
    private void dealRewardCoupon(DistributionSettingGetResponse distributionSettingGetResponse) {
        DistributionSettingVO distributionSetting = distributionSettingGetResponse.getDistributionSetting();
        //1.开启分销 2.开启邀新奖励 3.勾选奖励优惠券
        Boolean openFlag = DefaultFlag.YES.equals(distributionSetting.getOpenFlag()) && DefaultFlag.YES.equals(distributionSetting.getInviteFlag()) && DefaultFlag.YES.equals(distributionSetting.getRewardCouponFlag());
        XxlJobLogger.log("此次分销设置邀新奖励优惠券openFlag为:" + openFlag);
        //处理邀新奖励优惠券
        if (openFlag) {
            //优惠券活动信息
            BaseResponse active = couponActivityQueryProvider.getDistributeCouponActivity();
            String activityId = active.getContext().toString();
            List<CouponInfoVO> couponInfos = distributionSettingGetResponse.getCouponInfos();
            List<DistributionRewardCouponVO> couponInfoCounts = distributionSettingGetResponse.getCouponInfoCounts();
            List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfos, GetCouponGroupResponse.class);
            getCouponGroupResponse.forEach(c -> {
                Integer count = couponInfoCounts.stream().filter(i -> c.getCouponId().equals(i.getCouponId())).findFirst().get().getCount();
                c.setTotalCount(count.longValue());
            });
            if (CollectionUtils.isEmpty(couponInfos) || couponInfos.size() == 0) {
                XxlJobLogger.log("此次分销设置邀新奖励优惠券:没查到优惠券信息");
                return;
            }
            while (true) {
                if (!getDistributionCustomer4RewardCoupon(getCouponGroupResponse, distributionSetting, activityId)) {
                    break;
                }
            }
        }
    }


    /**
     * 处理邀新奖励优惠券
     *
     * @param distributionSetting
     */
    private boolean getDistributionCustomer4RewardCoupon(List<GetCouponGroupResponse> couponInfos, DistributionSettingVO distributionSetting, String activityId) {
        DistributionCustomerInviteInfoPageRequest distributionCustomerInviteInfoPageReq = new DistributionCustomerInviteInfoPageRequest();
        distributionCustomerInviteInfoPageReq.setPageSize(STEP);
        //邀新奖励设置奖励优惠券上限 已发放邀新奖励优惠券人数<x
        distributionCustomerInviteInfoPageReq.setRewardCouponCountEnd(distributionSetting.getRewardCouponCount());
        //邀新奖励设置-仅限有效邀新
        if (DistributionLimitType.EFFECTIVE.equals(distributionSetting.getRewardLimitType())) {
            //存在达到上限未发放奖励现金有效邀新人数
            distributionCustomerInviteInfoPageReq.setRewardCouponAvailableLimitCountStart(0);
        } else {
            //存在达到上限未发放奖励优惠券人数
            distributionCustomerInviteInfoPageReq.setRewardCouponLimitCountStart(0);
        }

        //查询满足补发条件的用户
        BaseResponse<DistributionCustomerInviteInfoPageResponse> distributionCustomerInviteInfoPage = distributionCustomerInviteInfoQueryProvider.page(distributionCustomerInviteInfoPageReq);
        if (Objects.isNull(distributionCustomerInviteInfoPage.getContext()) || CollectionUtils.isEmpty(distributionCustomerInviteInfoPage.getContext().getDistributionCustomerInviteInfoVOPage().getContent())) {
            XxlJobLogger.log("distributionRewardJobHandler补发奖励优惠券:没有满足补发条件的用户");
            return false;
        }
        String rewardCoupon = concatCouponName(couponInfos);
        List<DistributionCustomerInviteInfoVO> distributionCustomerInviteInfoVOList = distributionCustomerInviteInfoPage.getContext().getDistributionCustomerInviteInfoVOPage().getContent();
        distributionCustomerInviteInfoVOList.forEach(vo -> {
            //查询需要补发邀新奖励优惠券的数据
            List<DistributionInviteNewListVO> inviteNewListVOList = getDistributionInviteNew4RewardCoupon(vo, distributionSetting);
            XxlJobLogger.log("distributionRewardJobHandler补发奖励优惠券:1、用户".concat(vo.getCustomerId()));
            XxlJobLogger.log("distributionRewardJobHandler补发奖励优惠券:2、分销记录信息".concat(JSONObject.toJSONString(inviteNewListVOList)));
            if (CollectionUtils.isNotEmpty(inviteNewListVOList)) {
                //开始补发
                //1.修改分销员信息
                updateDistributor4Reward(vo, inviteNewListVOList, distributionSetting);
                inviteNewListVOList.forEach(inviteNewListVO -> {
                    inviteNewListVO.setRewardCoupon(rewardCoupon);
                    //2:修改邀新记录
                    updateInviteNew4RewardCoupon(inviteNewListVO, distributionSetting);
                    //3:发放优惠券
                    sendCoupon(vo, couponInfos, activityId);
                });
            } else {
                XxlJobLogger.log("distributionRewardJobHandler补发奖励优惠券:校正数据".concat(vo.getCustomerId()));
                DistributorCustomerInviteInfoReviseRequest request = new DistributorCustomerInviteInfoReviseRequest();
                request.setCustomerId(vo.getCustomerId());
                request.setDistributionId(vo.getDistributionId());
                // 校正
                distributionCustomerInviteInfoSaveProvider.revise(request);
            }
        });
        return true;
    }

    /**
     * 发放优惠券名称
     *
     * @param couponInfos
     * @return
     */
    private String concatCouponName(List<GetCouponGroupResponse> couponInfos) {
        couponInfos.forEach(c -> {
            StringBuilder sb = new StringBuilder();
            c.setCouponName(sb.append("满").append(null == c.getFullBuyPrice() ? 0 : c.getFullBuyPrice()).append("减").append(null == c.getDenomination() ? 0 : c.getDenomination()).toString());

        });
        return couponInfos.stream().map(GetCouponGroupResponse::getCouponName).collect(Collectors.joining("，"));
    }

    /**
     * 发放优惠券
     *
     * @param distributionCustomerInviteInfoVO
     * @param couponInfos
     */
    private void sendCoupon(DistributionCustomerInviteInfoVO distributionCustomerInviteInfoVO, List<GetCouponGroupResponse> couponInfos, String activityId) {
        SendCouponGroupRequest request = new SendCouponGroupRequest();
        request.setActivityId(activityId);
        request.setCouponInfos(couponInfos);
        request.setCustomerId(distributionCustomerInviteInfoVO.getCustomerId());
        couponActivityProvider.sendCouponGroup(request);

    }

    /**
     * 查询需要补发奖励优惠券的邀新数据
     *
     * @param distributionCustomerInviteInfoVO
     * @param distributionSetting
     */
    private List<DistributionInviteNewListVO> getDistributionInviteNew4RewardCoupon(DistributionCustomerInviteInfoVO distributionCustomerInviteInfoVO, DistributionSettingVO distributionSetting) {
        //查询需要补发奖励优惠券的邀新数据
        DistributionInviteNewListRequest request = new DistributionInviteNewListRequest();
        request.setRequestCustomerId(distributionCustomerInviteInfoVO.getCustomerId());
        request.setIsRewardRecorded(InvalidFlag.NO);
        //追加条件--1.达到上限未发放 2.入账类型 优惠券
        request.setRewardFlag(RewardFlag.COUPON);
        request.setFailReasonFlag(FailReasonFlag.LIMITED);
        //限制有效邀新
        if (DistributionLimitType.EFFECTIVE.equals(distributionSetting.getRewardLimitType())) {
            request.setAvailableDistribution(InvalidFlag.YES);
        }
        DistributionInviteNewListResponse distributionInviteNewListResponse = distributionInviteNewQueryProvider.listDistributionInviteNewRecord(request).getContext();
        if (CollectionUtils.isNotEmpty(distributionInviteNewListResponse.getInviteNewListVOList())) {
            List<DistributionInviteNewListVO> inviteNewListVOList = distributionInviteNewListResponse.getInviteNewListVOList();
            //设置奖励上限，按照(奖励上限-已发放奖励)进行补发
            int limitCount = distributionSetting.getRewardCouponCount() - distributionCustomerInviteInfoVO.getRewardCouponCount();
            if (limitCount - inviteNewListVOList.size() < 0) {
                //需要补发的数据
                inviteNewListVOList = inviteNewListVOList.stream().limit(limitCount).collect(Collectors.toList());
            }
            return inviteNewListVOList;
        }
        return Collections.emptyList();
    }
}
