package com.wanmi.sbc.order.pointstrade.fsm.builder;

import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeBuilder;
import com.wanmi.sbc.order.pointstrade.fsm.action.PointsTradeDeliverAction;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.*;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
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
public class PointsTradeAuditBuilder implements PointsTradeBuilder {

    @Autowired
    private PointsTradeDeliverAction pointsTradeDeliverAction;

    @Override
    public PointsFlowState supportState() {
        return PointsFlowState.AUDIT;
    }

    @Override
    public StateMachine<PointsFlowState, PointsTradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<PointsFlowState, PointsTradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("AUDIT").beanFactory(beanFactory)
                .listener(listener())
        ;

        builder.configureStates()
                .withStates()
                .initial(PointsFlowState.AUDIT)
                .states(EnumSet.allOf(PointsFlowState.class));

        builder.configureTransitions()

                // 审核 -> [发货] -> 部分发货
                .withExternal()
                .source(PointsFlowState.AUDIT).target(PointsFlowState.DELIVERED_PART)
                .event(PointsTradeEvent.DELIVER)
                .action(pointsTradeDeliverAction)
                .and()
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
