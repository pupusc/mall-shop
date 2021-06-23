package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: dyt
 * @Date: Created In 上午10:49 2020/4/21
 * @Description:
 */
@ApiModel
@Data
public class TradeItemConfirmModifyGoodsNumRequest {

    /**
     * 商品skuId
     */
    @ApiModelProperty("商品skuId")
    @NotBlank
    private String goodsInfoId;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    @NotNull
    private Long buyCount;
}
