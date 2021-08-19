package com.wanmi.sbc.order.trade.fsm.builder;

import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.fsm.Builder;
import com.wanmi.sbc.order.trade.fsm.action.CancelAction;
import com.wanmi.sbc.order.trade.fsm.action.GrouponAction;
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
 * <p>拼团订单状态流转分支</p>
 * Created by of628-wenzhi on 2019-05-24-17:18.
 */

@Component
public class GrouponBuilder implements Builder {

    @Override
    public FlowState supportState() {
        return FlowState.GROUPON;
    }

    @Autowired
    private GrouponAction grouponAction;

    @Autowired
    private CancelAction cancelAction;


    @Override
    public StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, TradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .listener(listener())
                .machineId("GROUPON").beanFactory(beanFactory);

        builder.configureStates()
                .withStates()
                .initial(FlowState.GROUPON)
                .states(EnumSet.allOf(FlowState.class));

        builder.configureTransitions()

                /*
                 *   （已参团）待成团 -> 已审核（成团后，订单成待发货状态），其实待成团状态订单已自动审核完成，
                 *     此处置为已审核是为了与之前页面端展示的待发货状态保持一致
                 */
                .withExternal()
                .source(FlowState.GROUPON).target(FlowState.AUDIT)
                .event(TradeEvent.JOIN_GROUPON)
                .action(grouponAction)

                //待成团 -> 已作废 （客户主动作废拼团订单）
                .and()
                .withExternal()
                .source(FlowState.GROUPON).target(FlowState.VOID)
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
