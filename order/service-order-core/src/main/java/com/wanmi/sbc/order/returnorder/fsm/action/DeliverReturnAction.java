package com.wanmi.sbc.order.returnorder.fsm.action;

import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.common.base.Operator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 发货action
 * Created by jinwei on 21/4/2017.
 */
@Component
public class DeliverReturnAction extends ReturnAction {

    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {

        Operator operator = rsc.findOperator();
        returnOrder.setReturnLogistics(rsc.findRequestData());
        returnOrder.setReturnFlowState(ReturnFlowState.DELIVERED);
        String format = "退单[%s]货物已发,操作人:%s";
        if (returnOrder.getReturnWay() == ReturnWay.OTHER) {
            format = "退单[%s]退回方式为非快递，审核后流程状态自动变更为已发退货，审核人:%s";
        }
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.DELIVER.getDesc())
                .eventDetail(String.format(format, returnOrder.getId(), operator.getName()))
                .eventTime(LocalDateTime.now())
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.DELIVER.getDesc(), eventLog.getEventDetail());
    }
}
