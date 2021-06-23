package com.wanmi.sbc.marketing.coupon.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * APP和H5 我的优惠券返回结果总数统计
 *
 * @author chenli
 * @date 2018/9/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeCountResponse {

    /**
     * 我的优惠券未使用总数
     */
    private long unUseCount = 0;

    /**
     * 我的优惠券已使用总数
     */
    private long usedCount = 0;

    /**
     * 我的优惠券已过期总数
     */
    private long overDueCount = 0;
}
