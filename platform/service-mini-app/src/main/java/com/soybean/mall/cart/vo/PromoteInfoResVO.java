package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @desc 促销信息
 * @date 2022-06-16 19:07:00
 */
@Data
public class PromoteInfoResVO {
    /**
     * 活动列表
     */
    private List<PromoteInfoResVO$Marketing> marketings;
    /**
     * 优惠券列表
     */
    private List<PromoteInfoResVO$Coupon> coupons;
}