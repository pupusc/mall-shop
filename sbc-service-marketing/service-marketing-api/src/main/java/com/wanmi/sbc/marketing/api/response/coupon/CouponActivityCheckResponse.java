package com.wanmi.sbc.marketing.api.response.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponActivityCheckResponse implements Serializable {

    private static final long serialVersionUID = -9069345051105857387L;

    /**
     * 优惠券id集合
     */
    @ApiModelProperty(value = "优惠券id集合")
    private List<String> ids;
}
