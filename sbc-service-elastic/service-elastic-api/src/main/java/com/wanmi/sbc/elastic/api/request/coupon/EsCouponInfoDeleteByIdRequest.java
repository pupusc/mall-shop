package com.wanmi.sbc.elastic.api.request.coupon;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 新增优惠券
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsCouponInfoDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = -9162622109556746710L;

    /**
     * 优惠券ID
     */
    @ApiModelProperty(value = "优惠券ID")
    @NotBlank
    private String couponId;
}
