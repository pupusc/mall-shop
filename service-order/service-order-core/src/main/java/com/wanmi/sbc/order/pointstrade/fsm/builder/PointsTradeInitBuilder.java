package com.wanmi.sbc.order.pointstrade.fsm.builder;

import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeBuilder;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class PointsTradeInitBuilder implements PointsTradeBuilder {

    @Override
    public PointsFlowState supportState() {
        return PointsFlowState.INIT;
    }

    @Override
    public StateMachine<PointsFlowState, PointsTradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<PointsFlowState, PointsTradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("INIT").beanFactory(beanFactory)
                .listener(listener())
        ;

        builder.configureStates()
                .withStates()
                .initial(PointsFlowState.INIT)
                .states(EnumSet.allOf(PointsFlowState.class))
        ;

        return builder.build();
    }

    public StateMachineListener<PointsFlowState, PointsTradeEvent> listener() {
        return new StateMachineListenerAdapter<PointsFlowState, PointsTradeEvent>() {
            @Override
            public void stateChanged(State<PointsFlowState, PointsTradeEvent> from, State<PointsFlowState, PointsTradeEvent> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
