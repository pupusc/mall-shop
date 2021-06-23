package com.wanmi.sbc.marketing.api.request.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponActivityGetByIdRequest implements Serializable {

    private static final long serialVersionUID = 8199582083025701505L;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    @NotBlank
    private String id;
}
