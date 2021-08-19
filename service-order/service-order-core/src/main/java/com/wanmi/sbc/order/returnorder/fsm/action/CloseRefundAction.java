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
 * 取消退单
 */
@Component
public class CloseRefundAction extends ReturnAction {
    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        returnOrder.setReturnFlowState(ReturnFlowState.COMPLETED);
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.CLOSE_REFUND.getDesc())
                .eventTime(LocalDateTime.now())
                .eventDetail(String.format("手动关闭退单，操作人:%s", operator.getName()))
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.CLOSE_REFUND.getDesc(), eventLog.getEventDetail());
    }
}
