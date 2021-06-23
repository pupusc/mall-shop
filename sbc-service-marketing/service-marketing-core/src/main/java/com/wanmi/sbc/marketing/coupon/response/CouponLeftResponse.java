package com.wanmi.sbc.marketing.coupon.response;

import lombok.Builder;
import lombok.Data;

/**
 * 优惠券剩余
 */
@Data
@Builder
public class CouponLeftResponse {

    /**
     * 剩余数量
     */
    private Long leftCount;

    /**
     * 总数
     */
    private Long totalCount;
}
