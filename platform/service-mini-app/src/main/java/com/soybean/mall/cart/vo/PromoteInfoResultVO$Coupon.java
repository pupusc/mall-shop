package com.soybean.mall.cart.vo;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liang Jun
 * @date 2022-06-16 19:18:00
 */
@Data
public class PromoteInfoResultVO$Coupon {
    /**
     * 优惠券开始时间
     */
    private String startTime;
    /**
     * 优惠券结束时间
     */
    private String endTime;
    /**
     * 优惠券Id
     */
    private String couponId;
    /**
     * 优惠券码id
     */
    private String couponCodeId;
    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private CouponType couponType;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 优惠券说明
     */
    private String couponDesc;
    /**
     * 优惠券面值
     */
    private BigDecimal denomination;
    /**
     * 限制金额：0不限制
     */
    private BigDecimal limitPrice = BigDecimal.ZERO;
    /**
     * 限制范围：0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    private ScopeType limitScope;
    /**
     * 是否可以领取
     */
    private Boolean canFetch;
    /**
     * 是否已经领取
     */
    private boolean hasFetch;
    /**
     * 是否即将过期
     */
    private boolean nearOverdue;
    /**
     * 即将过期提示文案
     */
    private String nearOverdueText;
    /**
     * 满减金额显示内容（fullContent）
     */
    private String limitPriceText;
    /**
     * 平台内容（platformContent）
     */
    private String storeNameText;
    /**
     * 优惠券营销范围（scopeContent）
     */
    private String limitScopeText;
    /**
     * 优惠券营销范围，具体描述（仅可购买...）
     */
    private String limitScopeDesc;
}
