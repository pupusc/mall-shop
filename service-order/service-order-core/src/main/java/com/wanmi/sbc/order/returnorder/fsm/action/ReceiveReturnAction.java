package com.wanmi.sbc.order.returnorder.fsm.action;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.common.base.Operator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 商家接受退货
 * Created by jinwei on 22/4/2017.
 */
@Component
public class ReceiveReturnAction extends ReturnAction {

    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        String detail = "";
        if (returnOrder.getReturnFlowState().equals(ReturnFlowState.DELIVERED)) {
            detail = "退单[%s]货物已签收,操作人:%s";

        } else if (returnOrder.getReturnFlowState().equals(ReturnFlowState.COMPLETED)) {
            detail = "退单[%s]执行作废退款操作,退单状态扭转为待退款，操作人:%s";
        } else {
            throw new SbcRuntimeException("K-050002");
        }

        Operator operator = rsc.findOperator();
        returnOrder.setReturnFlowState(ReturnFlowState.RECEIVED);
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.RECEIVE.getDesc())
                .eventTime(LocalDateTime.now())
                .eventDetail(String.format(detail, returnOrder.getId(), operator.getName()))
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.RECEIVE.getDesc(), eventLog.getEventDetail());
    }
}
