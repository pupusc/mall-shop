package com.wanmi.sbc.order.pointstrade.fsm.builder;

import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeBuilder;
import com.wanmi.sbc.order.pointstrade.fsm.action.PointsTradeCompleteAction;
import com.wanmi.sbc.order.pointstrade.fsm.action.PointsTradeConfirmAction;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.trade.model.root.Trade;
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
 * Created by mac on 2017/4/7.
 */
@Component
public class PointsTradeDeliveredBuilder implements PointsTradeBuilder {

    @Autowired
    private PointsTradeConfirmAction confirmAction;

    @Autowired
    private PointsTradeCompleteAction pointsTradeCompleteAction;

    @Override
    public PointsFlowState supportState() {
        return PointsFlowState.DELIVERED;
    }

    @Override
    public StateMachine<PointsFlowState, PointsTradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<PointsFlowState, PointsTradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("Delivered").beanFactory(beanFactory)
                .listener(listener())
        ;

        builder.configureStates()
                .withStates()
                .initial(PointsFlowState.DELIVERED)
                .states(EnumSet.allOf(PointsFlowState.class));

        builder.configureTransitions()

                // 发货 -> [确认收货] -> 收货
                .withExternal()
                .source(PointsFlowState.DELIVERED).target(PointsFlowState.CONFIRMED)
                .event(PointsTradeEvent.CONFIRM)
                .action(confirmAction)

                // 收货 -> 完成
                .and()
                .withExternal()
                .source(PointsFlowState.DELIVERED).target(PointsFlowState.COMPLETED)
                .event(PointsTradeEvent.COMPLETE)
                .action(pointsTradeCompleteAction);

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
