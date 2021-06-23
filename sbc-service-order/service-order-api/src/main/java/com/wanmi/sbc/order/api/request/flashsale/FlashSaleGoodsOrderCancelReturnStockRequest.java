package com.wanmi.sbc.order.api.request.flashsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName FlashSaleGoodsOrderCancelReturnStockRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/5 15:04
 **/
@ApiModel
@Data
public class FlashSaleGoodsOrderCancelReturnStockRequest {

    /**
     * 抢购商品Id
     */
    @ApiModelProperty(value = "抢购商品Id")
    @NotNull
    private Long flashSaleGoodsId;

    /**
     * 抢购商品数量
     */
    @ApiModelProperty(value = "抢购商品数量")
    private Integer flashSaleGoodsNum;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;
}
