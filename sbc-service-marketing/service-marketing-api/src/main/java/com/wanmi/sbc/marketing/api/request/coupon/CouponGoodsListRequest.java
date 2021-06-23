package com.wanmi.sbc.marketing.api.request.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 16:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponGoodsListRequest {

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券Id")
    private String couponId;

    /**
     *活动id
     */
    @ApiModelProperty(value = "优惠券活动Id")
    private String activityId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户Id")
    private String customerId;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}
