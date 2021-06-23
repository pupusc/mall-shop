package com.wanmi.sbc.order.trade.fsm.builder;

import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.CompleteAction;
import com.wanmi.sbc.order.trade.fsm.action.ConfirmAction;
import com.wanmi.sbc.order.trade.fsm.action.ObsoleteDeliverAction;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.bean.enums.FlowState;
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
public class DeliveredBuilder implements Builder {

    @Autowired
    private ConfirmAction confirmAction;

    @Autowired
    private CompleteAction completeAction;


    @Autowired
    private ObsoleteDeliverAction obsoleteDeliverAction;

    @Override
    public FlowState supportState() {
        return FlowState.DELIVERED;
    }

    @Override
    public StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, TradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("Delivered").beanFactory(beanFactory)
                .listener(listener())
        ;

        builder.configureStates()
                .withStates()
                .initial(FlowState.DELIVERED)
                .states(EnumSet.allOf(FlowState.class));

        builder.configureTransitions()

                // 发货 -> [确认收货] -> 收货
                .withExternal()
                .source(FlowState.DELIVERED).target(FlowState.CONFIRMED)
                .event(TradeEvent.CONFIRM)
                .action(confirmAction)

                // 收货 -> 完成
                .and()
                .withExternal()
                .source(FlowState.DELIVERED).target(FlowState.COMPLETED)
                .event(TradeEvent.COMPLETE)
                .action(completeAction)

                // 发货 -> [作废] -> 部分发货
                .and()
                .withExternal()
                .source(FlowState.DELIVERED).target(FlowState.DELIVERED_PART)
                .event(TradeEvent.OBSOLETE_DELIVER)
                .action(obsoleteDeliverAction)

                // 发货 -> [作废] -> 审核
                .and()
                .withExternal()
                .source(FlowState.DELIVERED).target(FlowState.AUDIT)
                .event(TradeEvent.OBSOLETE_DELIVER)
                .action(obsoleteDeliverAction)

        ;

        return builder.build();
    }

    public StateMachineListener<FlowState, TradeEvent> listener() {
        return new StateMachineListenerAdapter<FlowState, TradeEvent>() {
            @Override
            public void stateChanged(State<FlowState, TradeEvent> from, State<FlowState, TradeEvent> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
