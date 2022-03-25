package com.wanmi.sbc.marketing.coupon.model.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.marketing.bean.enums.*;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import com.wanmi.sbc.marketing.coupon.response.CouponLeftResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
public class CouponView {

    /**
     * 优惠券+活动，关系id
     */
    private String activityConfigId;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     * 优惠券活动Id
     */
    private String activityId;

    /**
     * 购满多少钱
     */
    private Double fullBuyPrice;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    private FullBuyType fullBuyType;

    /**
     * 优惠券面值
     */
    private Double denomination;

    /**
     * 商户id
     */
    private Long storeId;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    private DefaultFlag platformFlag;

    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    private ScopeType scopeType;

    /**
     * 优惠券说明
     */
    private String couponDesc;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private CouponType couponType;

    /**
     * 优惠券活动类型
     */
    private CouponActivityType couponActivityType;

    /**
     * 优惠券开始时间
     */
    private String couponStartTime;

    /**
     * 优惠券结束时间
     */
    private String couponEndTime;

    /**
     * 起止时间类型 0：按起止时间，1：按N天有效
     */
    private RangeDayType rangeDayType;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 领取状态
     */
    private boolean hasFetched;

    /**
     * 一天限领一次的券今日是否已领取
     */
    private Boolean hasFetchToday;

    /**
     * 优惠券是否开始
     */
    private Boolean couponStarted;

    /**
     * 剩余状态
     */
    private boolean leftFlag;

    /**
     * 已抢百分比
     */
    private BigDecimal fetchPercent;

    /**
     * 优惠券即将过期
     */
    private boolean couponWillEnd;

    /**
     * 范围ids
     */
    private List<String> scopeIds;
    /**
     * 活动即将结束
     */
    private boolean activityWillEnd;
    /**
     * 优化券活动倒计时
     */
    private Long activityCountDown;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 活动开始时间
     */
    private String activityStartTime;

    /**
     * 活动结束时间
     */
    private String activityEndTime;

    /**
     * 总数量
     */
    private Long totalCount;
    /**
     * @param list
     * @param leftMap
     * @param fetchMap
     * @return
     */
    public static List<CouponView> converter(List<CouponCache> list, Map<String, Map<String, CouponLeftResponse>> leftMap
            , Map<String, Map<String, Boolean>> fetchMap) {
        return list.stream().map(item -> {
            //券是否剩余
            boolean leftFlag = item.getHasLeft() != DefaultFlag.NO &&
                    leftMap.get(item.getCouponActivityId()).get(item.getCouponInfoId()).getLeftCount() > 0;
            BigDecimal fetchPercent = BigDecimal.ZERO;
            //如果剩余，计算已抢百分比
            if (leftFlag) {
                CouponLeftResponse left = leftMap.get(item.getCouponActivityId()).get(item.getCouponInfoId());
                fetchPercent = new BigDecimal(left.getTotalCount()-left.getLeftCount()).divide(new BigDecimal(left.getTotalCount()), 2, BigDecimal.ROUND_FLOOR);
            }

            boolean activityWillEnd = false;
            Long activityCountDown = 0L;
            //优惠券活动是否显示倒计时
            if (item.getCouponActivity().getEndTime().isAfter(LocalDateTime.now()) && item.getCouponActivity().getEndTime().isBefore(LocalDateTime.now().plusDays(1))) {
                activityWillEnd = true;
                activityCountDown = Duration.between(LocalDateTime.now(),item.getCouponActivity().getEndTime()).toMillis();
            }

            //券是否会过期，只统计RANGE_DAY，5天内过期为即将过期
            boolean couponWillEnd = false;
            if (item.getCouponInfo().getRangeDayType() == RangeDayType.RANGE_DAY) {
                LocalDateTime endTime = item.getCouponInfo().getEndTime();
                //如果结束时间加上5天，大于现在时间，即将过期
                if (LocalDateTime.now().plusDays(5).isAfter(endTime)) {
                    couponWillEnd = true;
                }
            }

            //券的领用状态
            boolean hasFetched = fetchMap.get(item.getCouponActivityId()).get(item.getCouponInfoId());

            //优惠券是否已经开始 - 建立在已经领券的基础上，并且券是未过期的
            Boolean couponStarted = null;
            if (hasFetched) {
                if (item.getCouponInfo().getRangeDayType() == RangeDayType.DAYS || LocalDateTime.now().isAfter(item.getCouponInfo().getStartTime())) {
                    couponStarted = true;
                } else {
                    couponStarted = false;
                }
            }

            CouponView build = CouponView.builder()
                    .activityConfigId(item.getActivityConfigId())
                    .couponId(item.getCouponInfoId())
                    .activityId(item.getCouponActivityId())
                    .fullBuyPrice(item.getCouponInfo().getFullBuyPrice())
                    .fullBuyType(item.getCouponInfo().getFullBuyType())
                    .denomination(item.getCouponInfo().getDenomination())
                    .platformFlag(item.getCouponInfo().getPlatformFlag())
                    .storeId(item.getCouponInfo().getStoreId())
                    .scopeType(item.getCouponInfo().getScopeType())
                    .couponDesc(item.getCouponInfo().getCouponDesc())
                    .couponType(item.getCouponInfo().getCouponType())
                    .couponActivityType(item.getCouponActivity().getCouponActivityType())
                    .couponStartTime(Objects.isNull(item.getCouponInfo().getStartTime()) ? null : DateUtil.format(item.getCouponInfo().getStartTime(), DateUtil.FMT_DATE_1))
                    .couponEndTime(Objects.isNull(item.getCouponInfo().getEndTime()) ? null : DateUtil.format(item.getCouponInfo().getEndTime(), DateUtil.FMT_DATE_1))
                    .rangeDayType(item.getCouponInfo().getRangeDayType())
                    .effectiveDays(item.getCouponInfo().getEffectiveDays())
                    .couponStarted(couponStarted)
                    .hasFetched(hasFetched)
                    .hasFetchToday(item.getHasFetchToday())
                    .leftFlag(leftFlag)
                    .fetchPercent(fetchPercent)
                    .couponWillEnd(couponWillEnd)
                    .activityName(item.getCouponActivity().getActivityName())
                    .couponName(item.getCouponInfo().getCouponName())
                    .activityStartTime(Objects.isNull(item.getCouponActivity().getStartTime()) ? null : DateUtil.format(item.getCouponActivity().getStartTime(), DateUtil.FMT_TIME_1))
                    .activityEndTime(Objects.isNull(item.getCouponActivity().getEndTime()) ? null : DateUtil.format(item.getCouponActivity().getEndTime(), DateUtil.FMT_TIME_1))
                    .totalCount(item.getTotalCount())
                    .scopeIds(item.getScopes() != null ? item.getScopes().stream().sorted(
                            (o1, o2) -> {
                                if (Objects.equals(ScopeType.SKU, o1.getScopeType())) {
                                    return -1;
                                } else {
                                    return Long.valueOf(o1.getScopeId()).compareTo(Long.valueOf(o2.getScopeId()));
                                }
                            }).map(CouponMarketingScope::getScopeId).collect(Collectors.toList()) : null)
                    .activityCountDown(activityCountDown)
                    .activityWillEnd(activityWillEnd)
                    .build();
            if(item.getHasFetchToday() != null){
                build.setHasFetched(item.getHasFetchToday());
            }
            return build;
        }).collect(Collectors.toList());
    }
}
