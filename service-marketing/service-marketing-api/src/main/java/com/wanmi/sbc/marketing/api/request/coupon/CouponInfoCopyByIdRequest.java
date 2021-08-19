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
 * 根据id复制优惠券的请求结构
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponInfoCopyByIdRequest implements Serializable {

    private static final long serialVersionUID = -4485444157498437822L;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券Id")
    @NotBlank
    private String couponId;

    /**
     * 记录操作人
     */
    @ApiModelProperty(value = "记录操作人")
    @NotBlank
    private String operatorId;
}
