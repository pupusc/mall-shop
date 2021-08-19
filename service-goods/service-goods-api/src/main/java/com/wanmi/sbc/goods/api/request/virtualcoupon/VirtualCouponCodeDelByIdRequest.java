package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除券码请求参数</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeDelByIdRequest extends GoodsBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 券码ID
     */
    @ApiModelProperty(value = "券码ID")
    @NotNull
    private Long id;

    @NotNull
    @ApiModelProperty(value = "券ID")
    private Long couponId;

    @NotNull
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
}
