package com.wanmi.sbc.coupon.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 用户领取优惠券请求结构
 * @author daiyitian
 */
@ApiModel
@Data
public class CouponFetchBaseRequest implements Serializable {
    /**
     *  优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    @NotBlank
    private String couponInfoId;

    /**
     *  优惠券活动Id
     */
    @ApiModelProperty(value = "优惠券活动Id")
    @NotBlank
    private String couponActivityId;

}
