package com.soybean.mall.cart.vo;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-06-16 19:18:00
 */
@Data
public class PromoteInfoResVO$Coupon {
    /**
     * 优惠券Id
     */
    private String couponId;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 优惠券说明
     */
    private String couponDesc;
    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private CouponType couponType;
    /**
     * 优惠券活动Id
     */
    private String activityId;
    /**
     * 优惠券面值
     */
    private Double denomination;
    /**
     * 购满多少钱
     */
    private Double fullBuyPrice;
    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    private FullBuyType fullBuyType;
    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    private ScopeType scopeType;
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
     * 优惠券是否已领取
     */
    private boolean hasFetched;

    /**
     * 优惠券是否开始
     */
    private Boolean couponStarted;

    /**
     * 优惠券是否有剩余
     */
    private boolean leftFlag;
    /**
     * 优惠券是否即将过期
     */
    private boolean couponWillEnd;
    /**
     * 一天限领一次的券今日是否已领取
     */
    private Boolean canFetchMore;
}
