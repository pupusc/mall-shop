package com.wanmi.sbc.order.returnorder.fsm.builder;

import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.fsm.Builder;
import com.wanmi.sbc.order.returnorder.fsm.action.RejectReceive2DeliveredAction;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
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
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/28 2:41 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
public class RejectReceiveReturnBuilder implements Builder {


    @Autowired
    private RejectReceive2DeliveredAction rejectReceive2DeliveredAction;

    @Override
    public ReturnFlowState supportState() {
        return ReturnFlowState.REJECT_RECEIVE;
    }

    @Override
    public StateMachine<ReturnFlowState, ReturnEvent> build(ReturnOrder returnOrder, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<ReturnFlowState, ReturnEvent> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .machineId("REJECT_RECEIVE").beanFactory(beanFactory)
                .listener(listener());
        builder.configureStates()
                .withStates()
                .initial(ReturnFlowState.REJECT_RECEIVE)
                .states(EnumSet.allOf(ReturnFlowState.class));

        builder.configureTransitions()
                // 拒绝收货 -> 已发退货
                .withExternal()
                .source(ReturnFlowState.REJECT_RECEIVE).target(ReturnFlowState.DELIVERED)
                .event(ReturnEvent.REVERSE_RETURN)  //扭转退货退单状态
                .action(rejectReceive2DeliveredAction)
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
