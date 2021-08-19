package com.wanmi.sbc.marketing.api.request.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 根据id撤销优惠券使用请求结构
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCodeReturnByIdRequest implements Serializable {

    private static final long serialVersionUID = -4786916371683078520L;

    /**
     *  优惠券码id
     */
    @ApiModelProperty(value = "优惠券码id")
    @NotBlank
    private String couponCodeId;

}
