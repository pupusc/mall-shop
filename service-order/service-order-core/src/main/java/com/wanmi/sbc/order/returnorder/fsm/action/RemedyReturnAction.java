package com.wanmi.sbc.order.returnorder.fsm.action;

import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.common.base.Operator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 修改退货单
 * Created by jinwei on 21/4/2017.
 */
@Component
public class RemedyReturnAction extends ReturnAction {
    @Override
    protected void evaluateInternal(ReturnOrder oldReturnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        ReturnOrder newReturnOrder = rsc.findRequestData();
        oldReturnOrder.setReturnFlowState(ReturnFlowState.INIT);
        String join = StringUtils.join(oldReturnOrder.buildDiffStr(newReturnOrder), ";");
        if (StringUtils.isNotBlank(join) && !join.equals("null")) {
            oldReturnOrder.appendReturnEventLog(
                    ReturnEventLog.builder()
                            .operator(operator)
                            .eventTime(LocalDateTime.now())
                            .eventType(ReturnEvent.REMEDY.getDesc())
                            .eventDetail(join)
                            .build()
            );
        }
        //merge returnorder
        oldReturnOrder.merge(newReturnOrder);

        returnOrderService.updateReturnOrder(oldReturnOrder);
        if (StringUtils.isNotBlank(join) && !join.equals("null")) {
            super.operationLogMq.convertAndSend(operator, ReturnEvent.REMEDY.getDesc(), join);
        }
    }
}
