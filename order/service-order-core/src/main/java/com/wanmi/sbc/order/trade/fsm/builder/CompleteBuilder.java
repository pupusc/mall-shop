package com.wanmi.sbc.order.trade.fsm.builder;

import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.CancelAction;
import com.wanmi.sbc.order.trade.fsm.action.ObsoletePayAction;
import com.wanmi.sbc.order.trade.fsm.action.RefundAction;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * Created by mac on 2017/4/7.
 */
@Component
public class CompleteBuilder implements Builder {

    @Autowired
    private RefundAction refundAction;

    @Autowired
    private CancelAction cancelAction;

    @Autowired
    private ObsoletePayAction obsoletePayAction;

    @Override
    public FlowState supportState() {
        return FlowState.COMPLETED;
    }


    @Override
    public StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, TradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("COMPLETED").beanFactory(beanFactory);

        builder.configureStates()
                .withStates()
                .initial(FlowState.COMPLETED)
                .states(EnumSet.allOf(FlowState.class));

        builder.configureTransitions()

                // 完成 -> [申请退货] -> 退货
                .withExternal()
                .source(FlowState.COMPLETED).target(FlowState.COMPLETED)
                .event(TradeEvent.REFUND)
                .action(refundAction)

                // 完成 -> [申请退货] -> 退货
                .and()
                .withExternal()
                .source(FlowState.COMPLETED).target(FlowState.VOID)
                .event(TradeEvent.VOID)
                .action(cancelAction)

                .and()
                .withExternal()
                .source(FlowState.COMPLETED).target(FlowState.CONFIRMED)
                .event(TradeEvent.OBSOLETE_PAY)
                .action(obsoletePayAction);
        return builder.build();
    }
}
