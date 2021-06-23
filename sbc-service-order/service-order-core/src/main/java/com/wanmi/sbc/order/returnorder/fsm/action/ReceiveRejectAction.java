package com.wanmi.sbc.order.returnorder.fsm.action;

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
 * 退货拒绝签收
 * Created by jinwei on 22/4/2017.
 */
@Component
public class ReceiveRejectAction extends ReturnAction {
    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        //拒绝原因
        returnOrder.setRejectReason(rsc.findRequestData());
        returnOrder.setReturnFlowState(ReturnFlowState.REJECT_RECEIVE);
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.REJECT_RECEIVE.getDesc())
                .eventDetail(String.format("退单[%s]货物已拒绝签收,操作人:%s", returnOrder.getId(), operator.getName()))
                .eventTime(LocalDateTime.now())
                .remark(rsc.findRequestData())
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.REJECT_RECEIVE.getDesc(), eventLog.getEventDetail());
    }
}
