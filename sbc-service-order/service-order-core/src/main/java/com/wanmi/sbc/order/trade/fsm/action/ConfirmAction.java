package com.wanmi.sbc.order.trade.fsm.action;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.fsm.TradeAction;
import com.wanmi.sbc.order.trade.fsm.TradeStateContext;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by Administrator on 2017/4/21.
 */
@Component
public class ConfirmAction extends TradeAction {

    @Override
    protected void evaluateInternal(Trade trade, StateRequest request, TradeStateContext tsc) {
        TradeState tradeState = trade.getTradeState();

        if (!tradeState.getDeliverStatus().equals(DeliverStatus.SHIPPED)) {
            throw new SbcRuntimeException("K-050107");
        }

        //
        tradeState.setFlowState(FlowState.CONFIRMED);

        //
        String detail = String.format("订单%s已确认收货", trade.getId());
        trade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(tsc.getOperator())
                .eventType(FlowState.CONFIRMED.getDescription())
                .eventDetail(detail)
                .eventTime(LocalDateTime.now())
                .build());
        save(trade);
        super.operationLogMq.convertAndSend(tsc.getOperator(), FlowState.COMPLETED.getDescription(), detail);
    }
}
