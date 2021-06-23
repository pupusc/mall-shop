package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
public class GetCouponGroupRequest implements Serializable {

    @ApiModelProperty(value = "客户id")
    @NotNull
    private String customerId;

    @ApiModelProperty(value = "优惠券活动类型")
    @NotNull
    private CouponActivityType type;

    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
