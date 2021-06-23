package com.wanmi.sbc.order.returnorder.fsm.action;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnAddress;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.common.base.Operator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 审核退单
 * Created by jinwei on 21/4/2017.
 */
@Component
public class AuditReturnAction extends ReturnAction {
    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        if (returnOrder.getReturnFlowState().equals(ReturnFlowState.INIT) || returnOrder.getReturnFlowState().equals(ReturnFlowState.COMPLETED)) {
            Operator operator = rsc.findOperator();
            String detail = "退单[%s]审核通过";
            if (returnOrder.getReturnFlowState().equals(ReturnFlowState.COMPLETED)) {
                detail = "退单[%s]执行作废退款操作，状态扭转为待退款";
            }
            detail = String.format(detail + ",操作人:%s", returnOrder.getId(), operator.getName());
            returnOrder.setReturnFlowState(ReturnFlowState.AUDIT);
            returnOrder.setAuditTime(LocalDateTime.now());
            returnOrder.appendReturnEventLog(
                    ReturnEventLog.builder()
                            .operator(operator)
                            .eventTime(LocalDateTime.now())
                            .eventType(ReturnEvent.AUDIT.getDesc())
                            .eventDetail(detail)
                            .build()
            );
            ReturnAddress returnAddress = rsc.findRequestData();
            returnOrder.setReturnAddress(returnAddress);
            returnOrderService.updateReturnOrder(returnOrder);
            super.operationLogMq.convertAndSend(operator, ReturnEvent.AUDIT.getDesc(), detail);
        } else {
            throw new SbcRuntimeException("K-050002");
        }
    }

}
