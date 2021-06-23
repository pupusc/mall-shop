package com.wanmi.sbc.order.pointstrade.fsm.action;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeAction;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeStateContext;
import com.wanmi.sbc.order.pointstrade.fsm.params.PointsTradeStateRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by Administrator on 2017/4/21.
 */
@Component
public class PointsTradeConfirmAction extends PointsTradeAction {

    @Override
    protected void evaluateInternal(Trade trade, PointsTradeStateRequest request, PointsTradeStateContext tsc) {
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
