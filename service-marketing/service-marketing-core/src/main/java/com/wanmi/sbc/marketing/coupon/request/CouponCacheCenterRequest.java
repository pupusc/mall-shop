package com.wanmi.sbc.marketing.coupon.request;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import lombok.Data;

@Data
public class CouponCacheCenterRequest {

    /**
     * 优惠券分类id
     */
    private String couponCateId;

    /**
     * 优惠券类型
     */
    private CouponType couponType;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 分页页码
     */
    private int pageNum;

    /**
     * 每页数量
     */
    private int pageSize;

    private Long storeId;

}