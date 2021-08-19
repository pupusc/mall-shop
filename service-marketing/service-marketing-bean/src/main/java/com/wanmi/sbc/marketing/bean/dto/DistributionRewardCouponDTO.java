package com.wanmi.sbc.marketing.bean.dto;

import lombok.Data;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:20 2019/2/25
 * @Description:
 */
@Data
public class DistributionRewardCouponDTO {

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 每组张数
     */
    private Integer count;

}
