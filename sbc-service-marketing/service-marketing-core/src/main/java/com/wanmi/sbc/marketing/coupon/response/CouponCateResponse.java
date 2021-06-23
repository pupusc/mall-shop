package com.wanmi.sbc.marketing.coupon.response;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: songhanlin
 * @Date: Created In 5:35 PM 2018/9/13
 * @Description: 优惠券分类Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCateResponse {

    /**
     * 优惠券分类Id
     */
    private String couponCateId;

    /**
     * 优惠券分类名称
     */
    private String couponCateName;

    /**
     * 是否平台专用 0：否，1：是
     */
    private DefaultFlag onlyPlatformFlag;

}
