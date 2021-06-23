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
 * 根据优惠券范围id查询优惠券商品作用范列表
 * @Author: daiyitian
 * @Date: Created In 上午9:27 2018/11/24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponMarketingScopeByScopeIdRequest implements Serializable {

    private static final long serialVersionUID = -4150614606625262723L;

    /**
     * 优惠券范围id
     */
    @ApiModelProperty(value = "优惠券范围id")
    @NotBlank
    private String scopeId;

}
