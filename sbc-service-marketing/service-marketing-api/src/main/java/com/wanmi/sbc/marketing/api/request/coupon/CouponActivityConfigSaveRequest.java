package com.wanmi.sbc.marketing.api.request.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class CouponActivityConfigSaveRequest {

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    @NotBlank
    private String couponId;


    /**
     * 优惠券总张数
     */
    @ApiModelProperty(value = "优惠券总张数")
    @NotNull
    private Long totalCount;
}
