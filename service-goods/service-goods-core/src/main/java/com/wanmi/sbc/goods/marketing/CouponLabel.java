package com.wanmi.sbc.goods.marketing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营销标签
 * Created by hht on 2018/9/18.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponLabel {

    /**
     * 优惠券Id
     */
    private String couponInfoId;

    /**
     * 优惠券活动Id
     */
    private String couponActivityId;

    /**
     * 促销描述
     */
    private String couponDesc;

}
