package com.wanmi.sbc.order.trade.reponse;

import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-07-13 21:49:00
 */
@Data
public class CalcPriceResult {
    private TradePrice tradePrice;
    private List<TradeMkt> tradeMkts = new ArrayList<>();

    @Data
    public static  class TradeMkt {
        private Long mktId;
        private Long mktLevelId;
    }
}
