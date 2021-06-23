package com.wanmi.sbc.order.trade.fsm.builder;

import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.AuditAction;
import com.wanmi.sbc.order.trade.fsm.action.CancelAction;
import com.wanmi.sbc.order.trade.fsm.action.RefundAction;
import com.wanmi.sbc.order.trade.fsm.action.RemedyAction;
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
public class InitBuilder implements Builder {

    @Autowired
    private RemedyAction remedyAction;

    @Autowired
    private AuditAction auditAction;

    @Autowired
    private CancelAction cancelAction;

    @Autowired
    private RefundAction refundAction;

    @Override
    public FlowState supportState() {
        return FlowState.INIT;
    }

    @Override
    public StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, TradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("INIT").beanFactory(beanFactory)
                .listener(listener())
        ;

        builder.configureStates()
                .withStates()
                .initial(FlowState.INIT)
                .states(EnumSet.allOf(FlowState.class))
        ;

        builder.configureTransitions()
                // 新建 -> [修改] -> 新建
                .withExternal()
                .source(FlowState.INIT).target(FlowState.INIT)
                .event(TradeEvent.REMEDY)
                .action(remedyAction)
                .and()

                // 新建 -> [审核] -> 审核
                .withExternal()
                .source(FlowState.INIT).target(FlowState.AUDIT)
                .event(TradeEvent.AUDIT)
                .action(auditAction)
                .and()

                // 新建 -> [取消] -> 作废
                .withExternal()
                .source(FlowState.INIT).target(FlowState.VOID)
                .event(TradeEvent.VOID)
                .action(cancelAction)
                .and()

                // 新建 -> [退款] -> 退款
                .withExternal()
                .source(FlowState.INIT).target(FlowState.REFUND)
                .event(TradeEvent.REFUND)
                .action(refundAction)
                .and()
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
