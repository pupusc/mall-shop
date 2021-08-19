package com.wanmi.sbc.order.trade.fsm.builder;

import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.AuditAction;
import com.wanmi.sbc.order.trade.fsm.action.CompleteAction;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * Created by of628-wenzhi on 2017-08-30-下午4:38.
 */
@Component
public class VoidBuilder implements Builder {

    @Autowired
    private AuditAction auditAction;

    @Autowired
    private CompleteAction completeAction;

    @Override
    public FlowState supportState() {
        return FlowState.VOID;
    }

    @Override
    public StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, TradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("VOID").beanFactory(beanFactory);

        builder.configureStates()
                .withStates()
                .initial(FlowState.VOID)
                .states(EnumSet.allOf(FlowState.class));

        builder.configureTransitions()

                // 作废 -> [作废退款单] -> 已审核
                .withExternal()
                .source(FlowState.VOID).target(FlowState.AUDIT)
                .event(TradeEvent.REVERSE_REFUND)
                .action(auditAction)

                //作废 -> [作废退款单] -> 已完成
                .and()
                .withExternal()
                .source(FlowState.VOID).target(FlowState.AUDIT)
                .event(TradeEvent.REVERSE_RETURN)
                .action(completeAction);
        return builder.build();
    }

}
