package com.wanmi.sbc.order.pointstrade.fsm.builder;

import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeBuilder;
import com.wanmi.sbc.order.pointstrade.fsm.action.PointsTradeCompleteAction;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
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
public class PointsTradeConfirmBuilder implements PointsTradeBuilder {

    @Autowired
    private PointsTradeCompleteAction pointsTradeCompleteAction;

    @Override
    public PointsFlowState supportState() {
        return PointsFlowState.CONFIRMED;
    }


    @Override
    public StateMachine<PointsFlowState, PointsTradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<PointsFlowState, PointsTradeEvent> builder = StateMachineBuilder.builder();

        builder.configureConfiguration()
                .withConfiguration()
                .machineId("CONFIRMED").beanFactory(beanFactory);

        builder.configureStates()
                .withStates()
                .initial(PointsFlowState.CONFIRMED)
                .states(EnumSet.allOf(PointsFlowState.class));

        builder.configureTransitions()

                // 确认 -> 完成
                .withExternal()
                .source(PointsFlowState.CONFIRMED).target(PointsFlowState.COMPLETED)
                .event(PointsTradeEvent.COMPLETE)
                .action(pointsTradeCompleteAction);

        return builder.build();
    }
}
