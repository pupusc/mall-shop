package com.soybean.mall.cart.vo;

import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * @author Liang Jun
 * @date 2022-06-16 19:18:00
 */
@Slf4j
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
     * 活动id
     */
    private String activityId;
    /**
     * 优惠券id
     */
    private String couponId;
    /**
     * 优惠券id
     */
    private String couponCodeId;
    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private Integer couponType;
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
    private int limitScope;
    /**
     * 是否可以领取
     */
    private Boolean canFetch;
    /**
     * 不可领取说明
     */
    private String canFetchDesc;
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
    private String storeNameText = "仅樊登读书自营旗舰店可用";
    /**
     * 优惠券营销范围（scopeContent）
     */
    private String limitScopeText;
    /**
     * 优惠券营销范围，具体描述（仅可购买...）
     */
    private String limitScopeDesc;
    /**
     * 起止时间类型 0：按起止时间，1：按N天有效
     */
    private Integer rangeDayType;
    /**
     * 有效天数
     */
    private Integer effectiveDays;
    /**
     * 状态
     */
    private Integer status;

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
        if (limitPrice != null) {
            this.limitPriceText = limitPrice.compareTo(BigDecimal.ZERO) <= 0 ? "无门槛" : "满" + this.limitPrice + "可用";
        }
    }
    public void setLimitScope(Integer limitScope) {
        this.limitScope = limitScope;
        if (ScopeType.ALL.toValue() == this.limitScope) {
            this.limitScopeText = "全部商品";
            return;
        }
        if (ScopeType.BRAND.toValue() == this.limitScope) {
            this.limitScopeText = "限品牌";
            this.limitScopeDesc = "仅可购买指定品牌商品";
            return;
        }
        if (ScopeType.BOSS_CATE.toValue() == this.limitScope) {
            this.limitScopeText = "限品类";
            this.limitScopeDesc = "仅可购买指定品类商品";
            return;
        }
        if (ScopeType.STORE_CATE.toValue() == this.limitScope) {
            this.limitScopeText = "限分类";
            this.limitScopeDesc = "仅可购买指定分类商品";
            return;
        }
        this.limitScopeText = "部分商品";
    }
    public void setCanFetch(Boolean canFetch) {
        this.canFetch = canFetch;
        this.canFetchDesc = "已抢光";
    }

    public void setNearOverdue(boolean nearOverdue) {
        this.nearOverdue = nearOverdue;
        if (this.nearOverdue) {
            try {
                long days = (System.currentTimeMillis() - DateUtils.parseDate(this.endTime, "yyyy-MM-dd").getTime()) / (1000*3600*24);
                this.nearOverdueText = "距过期仅剩"+ days +"天";
            } catch (ParseException e) {
                log.warn("日期格式解析错误", e);
                this.nearOverdueText = "已经快要过期了";
            }
        }
    }
}
