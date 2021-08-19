package com.wanmi.sbc.order.trade.fsm;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Just for build each state builder
 * Created by mac on 2017/4/7.
 */
@Component
public class BuilderFactory implements InitializingBean {

    /**
     *
     */
    private Map<FlowState, Builder> stateBuilderMap = new ConcurrentHashMap<>();

    /**
     *
     */
    @Autowired
    private List<Builder> stateBuilders;

    /**
     *
     */
    @Autowired
    private BeanFactory beanFactory;

    /**
     * @param trade
     * @return
     */
    public StateMachine<FlowState, TradeEvent> create(Trade trade) {
        FlowState flowState = trade.getTradeState().getFlowState();
        Builder builder = stateBuilderMap.get(flowState);
        if (builder == null) {
            throw new SbcRuntimeException("K-050102");
        }

        //
        StateMachine<FlowState, TradeEvent> sm;
        try {
            sm = builder.build(trade, beanFactory);
            sm.start();
        } catch (Exception e) {
            throw new SbcRuntimeException("K-050102");
        }

        //
        sm.getExtendedState().getVariables().put(Trade.class, trade);
        return sm;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        stateBuilderMap = stateBuilders.stream().collect(Collectors.toMap(Builder::supportState, Function.identity()));
    }
}
