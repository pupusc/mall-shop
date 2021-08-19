package com.wanmi.sbc.order.trade.reponse;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 各店铺运费
 */
@Data
public class TradeFreightResponse {
    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 配送费用，可以从TradePriceInfo获取
     */
    private BigDecimal deliveryPrice;

}
