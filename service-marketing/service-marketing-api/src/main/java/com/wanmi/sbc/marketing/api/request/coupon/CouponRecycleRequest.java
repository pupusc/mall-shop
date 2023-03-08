package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.api.request.Source;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class CouponRecycleRequest implements Serializable {

    @ApiModelProperty
    private String customerId;

    @ApiModelProperty(value = "来源")
    private Source source;

}
