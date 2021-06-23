package com.wanmi.sbc.marketing.coupon.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author: songhanlin
 * @Date: Created In 6:05 PM 2018/9/13
 * @Description: 优惠券分类排序request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCateSortRequest {

    /**
     * 优惠券分类Id
     */
    @NotBlank
    private String couponCateId;

    /**
     * 优惠券排序顺序
     */
    @NotNull
    private Integer cateSort;
}
