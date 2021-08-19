package com.wanmi.sbc.goods.api.request.virtualcoupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 卡券关联商品
 */
@Data
public class VirtualCouponGoodsRequest {

    @NotBlank
    @ApiModelProperty("商品ID")
    private String skuId;

    @NotNull
    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @NotNull
    @ApiModelProperty("店铺ID")
    private Long storeId;

    @NotBlank
    @ApiModelProperty("更新人")
    private String updatePerson;
}
