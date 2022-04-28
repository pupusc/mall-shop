package com.wanmi.sbc.order.returnorder.fsm.builder;

import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.fsm.Builder;
import com.wanmi.sbc.order.returnorder.fsm.action.RefundReject2RefundRejectAction;
import com.wanmi.sbc.order.returnorder.fsm.action.RejectReceive2DeliveredAction;
import com.wanmi.sbc.order.returnorder.fsm.action.RemedyReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.trade.fsm.action.RemedyAction;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * Description: 拒绝退款
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/28 2:41 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
public class RejectRefundReturnBuilder implements Builder {


    @Autowired
    private RefundReject2RefundRejectAction refundReject2RefundRejectAction;

    @Autowired
    private RemedyReturnAction remedyReturnAction;

    @Override
    public ReturnFlowState supportState() {
        return ReturnFlowState.REJECT_REFUND;
    }

    @Override
    public StateMachine<ReturnFlowState, ReturnEvent> build(ReturnOrder returnOrder, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<ReturnFlowState, ReturnEvent> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .machineId("REJECT_REFUND").beanFactory(beanFactory)
                .listener(listener());
        builder.configureStates()
                .withStates()
                .initial(ReturnFlowState.REJECT_REFUND)
                .states(EnumSet.allOf(ReturnFlowState.class));

        builder.configureTransitions()
                // 拒绝退款 -> 已审核
                .withExternal()
                .source(ReturnFlowState.REJECT_REFUND).target(ReturnFlowState.DELIVERED)
                .event(ReturnEvent.REVERSE_RETURN)  //扭转退货退单状态
                .action(refundReject2RefundRejectAction)

                .and()
                .withExternal()
                .source(ReturnFlowState.REJECT_REFUND)
                .event(ReturnEvent.REMEDY)
                .action(remedyReturnAction)
        ;

        return builder.build();
    }


    public StateMachineListener<ReturnFlowState, ReturnEvent> listener() {
        return new StateMachineListenerAdapter<ReturnFlowState, ReturnEvent>() {
            @Override
            public void stateChanged(State<ReturnFlowState, ReturnEvent> from, State<ReturnFlowState, ReturnEvent> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
