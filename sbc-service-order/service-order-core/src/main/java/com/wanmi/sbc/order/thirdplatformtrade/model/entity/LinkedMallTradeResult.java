package com.wanmi.sbc.order.thirdplatformtrade.model.entity;

import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * linkedmall 自动处理结果类
 *
 * @author dyt
 * Date 2020-8-22 13:01:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkedMallTradeResult {

    /**
     * 操作状态 0：完成  1:自动退款 2：定时观察 3标记支付失败
     */
    private Integer status;

    /**
     * 成功子订单
     */
    private List<Trade> successTrades;

    /**
     * 需要自动退款的订单
     */
    private List<Trade> autoRefundTrades;
}
