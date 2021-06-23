package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
public class CouponActivityDisabledTimeRequest implements Serializable {

    private static final long serialVersionUID = -2564683243020990927L;
    /**
     * 优惠券活动类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券， 4权益赠券
     */
    @ApiModelProperty(value = "优惠券活动类型")
    @NotNull
    private CouponActivityType couponActivityType;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private String activityId;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
}
