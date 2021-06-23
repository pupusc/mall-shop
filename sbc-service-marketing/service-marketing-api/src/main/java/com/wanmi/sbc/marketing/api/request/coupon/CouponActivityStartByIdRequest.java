package com.wanmi.sbc.marketing.api.request.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponActivityStartByIdRequest implements Serializable {

    private static final long serialVersionUID = 6497031021854777792L;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    @NotBlank
    private String id;
}