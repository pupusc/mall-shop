package com.wanmi.sbc.order.returnorder.fsm.builder;

import com.wanmi.sbc.order.returnorder.fsm.Builder;
import com.wanmi.sbc.order.returnorder.fsm.action.CompleteReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
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
 * 已退款状态机
 * Created by jinwei on 22/4/2017.
 */
@Component
public class RefundReturnBuilder implements Builder {

    @Autowired
    private CompleteReturnAction completeReturnAction;

    @Override
    public ReturnFlowState supportState() {
        return ReturnFlowState.REFUNDED;
    }

    @Override
    public StateMachine<ReturnFlowState, ReturnEvent> build(ReturnOrder returnOrder, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<ReturnFlowState, ReturnEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
            .withConfiguration()
            .machineId("REFUNDED").beanFactory(beanFactory)
            .listener(listener());

        builder.configureStates()
            .withStates()
            .initial(ReturnFlowState.REFUNDED)
            .states(EnumSet.allOf(ReturnFlowState.class));

        builder.configureTransitions()
            // 已退款 -> 已完成
            .withExternal()
            .source(ReturnFlowState.REFUNDED).target(ReturnFlowState.COMPLETED)
            .event(ReturnEvent.COMPLETE)
            .action(completeReturnAction)
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
