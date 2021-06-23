package com.wanmi.sbc.marketing.api.request.coupon;

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
public class CouponFetchRequest implements Serializable {

    private static final long serialVersionUID = -4786916371683078520L;

    /**
     *  领券用户Id
     */
    @ApiModelProperty(value = "领券用户Id")
    @NotBlank
    private String customerId;

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

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}
