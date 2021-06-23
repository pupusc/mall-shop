package com.wanmi.sbc.order.returnorder.fsm.action;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 退款失败
 */
@Component
public class RefundFailedAction extends ReturnAction {
    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        returnOrder.setReturnFlowState(ReturnFlowState.REFUND_FAILED);
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.REFUND_FAILED.getDesc())
                .eventTime(LocalDateTime.now())
                .eventDetail(String.format("退单[%s]退款失败，操作人:%s", returnOrder.getId(), operator.getName()))
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.REFUND_FAILED.getDesc(), eventLog.getEventDetail());
    }
}
