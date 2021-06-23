package com.wanmi.sbc.order.trade.fsm.builder;

import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.CancelAction;
import com.wanmi.sbc.order.trade.fsm.action.DeliverAction;
import com.wanmi.sbc.order.trade.fsm.action.ObsoleteDeliverAction;
import com.wanmi.sbc.order.trade.fsm.action.RefundAction;
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
public class DeliveredPartBuilder implements Builder {

    @Autowired
    private DeliverAction deliverAction;


    @Autowired
    private ObsoleteDeliverAction obsoleteDeliverAction;

    @Autowired
    private RefundAction refundAction;

    @Autowired
    private CancelAction cancelAction;

    @Override
    public FlowState supportState() {
        return FlowState.DELIVERED_PART;
    }

    @Override
    public StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, TradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("DeliveredPart").beanFactory(beanFactory)
                .listener(listener())
        ;

        builder.configureStates()
                .withStates()
                .initial(FlowState.DELIVERED_PART)
                .states(EnumSet.allOf(FlowState.class));

        builder.configureTransitions()

                // 部分发货 -> [发货] -> 发货
                .withExternal()
                .source(FlowState.DELIVERED_PART).target(FlowState.DELIVERED)
                .event(TradeEvent.DELIVER)
                .action(deliverAction)
                .and()

                // 部分发货 -> [作废] -> 审核
                .withExternal()
                .source(FlowState.DELIVERED_PART).target(FlowState.AUDIT)
                .event(TradeEvent.OBSOLETE_DELIVER)
                .action(obsoleteDeliverAction)
                .and()

                //周期购订单在申请退货退款时状态变更 部分发货 -> [申请退货] -> 部分发货
                .withExternal()
                .source(FlowState.DELIVERED_PART).target(FlowState.DELIVERED_PART)
                .event(TradeEvent.REFUND)
                .action(refundAction)
                .and()

                //周期购订单在商家收货时状态变更 部分发货 -> [申请退货] -> 作废
                .withExternal()
                .source(FlowState.DELIVERED_PART).target(FlowState.VOID)
                .event(TradeEvent.VOID)
                .action(cancelAction);

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
