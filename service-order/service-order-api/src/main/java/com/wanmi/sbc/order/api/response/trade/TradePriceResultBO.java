package com.wanmi.sbc.order.api.response.trade;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-28 13:40:00
 */
@Data
public class TradePriceResultBO {
    private BigDecimal totalPrice;
    private BigDecimal cutPrice;
    private BigDecimal payPrice;
    private List<PriceItem> totalPriceItems;
    private List<PriceItem> cutPriceItems;

    @Data
    public static class PriceItem {
        private Integer type;
        private String desc;
        private BigDecimal amount;
    }
}
