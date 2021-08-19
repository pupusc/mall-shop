package com.wanmi.sbc.order.trade.fsm.action;

import com.wanmi.sbc.order.trade.fsm.TradeAction;
import com.wanmi.sbc.order.trade.fsm.TradeStateContext;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>支付作废Action</p>
 * Created by of628-wenzhi on 2017-06-06-下午4:38.
 */
@Component
public class ObsoletePayAction extends TradeAction {
    @Override
    protected void evaluateInternal(Trade trade, StateRequest request, TradeStateContext tsc) {
        obsoletePay(trade, request.getOperator());
    }

    private void obsoletePay(Trade trade, Operator operator) {
        trade.getTradeState().setPayState(PayState.NOT_PAID);
        trade.getTradeState().setPayTime(null);
        trade.getTradeState().setFlowState(FlowState.CONFIRMED);
        //添加操作日志
        String detail = String.format("订单[%s]支付记录已作废，当前支付状态[%s],操作人：%s", trade.getId(),
                trade.getTradeState().getPayState().getDescription(), operator.getName());
        trade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(operator)
                .eventType(TradeEvent.OBSOLETE_PAY.getDescription())
                .eventDetail(detail)
                .eventTime(LocalDateTime.now())
                .build());
        save(trade);
        super.operationLogMq.convertAndSend(operator, TradeEvent.OBSOLETE_PAY.getDescription(), detail);
    }
}
