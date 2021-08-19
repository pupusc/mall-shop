package com.wanmi.sbc.order.api.response.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 各店铺运费
 */
@Data
@ApiModel
public class TradeGetFreightResponse implements Serializable {
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 配送费用，可以从TradePriceInfo获取
     */
    @ApiModelProperty(value = "配送费用")
    private BigDecimal deliveryPrice;

}
