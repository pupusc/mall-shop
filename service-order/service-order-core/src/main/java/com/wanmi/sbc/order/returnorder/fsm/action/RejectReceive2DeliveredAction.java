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
 * Description: 拒绝收货 到 已发退货
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/28 2:51 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Component
public class RejectReceive2DeliveredAction extends ReturnAction {


    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        //拒绝原因
        returnOrder.setRejectReason(rsc.findRequestData());
        returnOrder.setReturnFlowState(ReturnFlowState.DELIVERED);
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.REVERSE_RETURN.getDesc())
                .eventDetail(String.format("退单[%s]扭转状态 拒绝退货 到 已发退货,操作人:%s", returnOrder.getId(), operator.getName()))
                .eventTime(LocalDateTime.now())
                .remark(rsc.findRequestData())
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.REVERSE_RETURN.getDesc(), eventLog.getEventDetail());
    }
}
