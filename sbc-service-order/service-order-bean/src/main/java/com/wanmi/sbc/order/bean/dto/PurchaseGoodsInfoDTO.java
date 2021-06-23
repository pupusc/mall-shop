package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 前端采购单缓存中的商品DTO
 */
@Data
@ApiModel
public class PurchaseGoodsInfoDTO implements Serializable {
    private static final long serialVersionUID = -2916726765693762313L;

    /**
     * SkuId
     */
    @ApiModelProperty(value = "SkuId", required = true)
    @NotNull
    private String goodsInfoId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量", required = true)
    @NotNull
    private Long goodsNum;
}
