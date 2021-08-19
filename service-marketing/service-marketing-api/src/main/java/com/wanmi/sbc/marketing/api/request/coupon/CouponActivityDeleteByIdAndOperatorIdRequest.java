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
public class CouponActivityDeleteByIdAndOperatorIdRequest implements Serializable {

    private static final long serialVersionUID = 2477714610994831293L;

    @ApiModelProperty(value = "优惠券活动id")
    @NotBlank
    private String id;

    @ApiModelProperty(value = "操作人")
    @NotBlank
    private String operatorId;
}
