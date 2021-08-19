package com.wanmi.sbc.customer.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:38 2019/2/25
 * @Description:
 */
@Data
public class DistributionRewardCouponDTO implements Serializable {

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 每组张数
     */
    private Integer count;

}
