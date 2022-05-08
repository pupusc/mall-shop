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
 * 拒绝退款
 * Created by jinwei on 2/5/2017.
 */
@Component
public class RefundReject2RefundRejectAction extends ReturnAction {

    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        returnOrder.setReturnFlowState(ReturnFlowState.RECEIVED);
        returnOrder.setRejectReason(rsc.findRequestData());
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.REVERSE_RETURN.getDesc())
                .eventTime(LocalDateTime.now())
                .eventDetail(String.format("退单[%s] 扭转拒绝退款到已审核,操作人:%s", returnOrder.getId(), operator.getName()))
                .remark(rsc.findRequestData())
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.RECEIVE.getDesc(), eventLog.getEventDetail());
    }
}
