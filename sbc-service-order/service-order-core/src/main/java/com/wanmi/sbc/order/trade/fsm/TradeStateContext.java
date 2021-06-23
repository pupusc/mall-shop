package com.wanmi.sbc.order.trade.fsm;

import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.common.base.Operator;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

/**
 * Created by Administrator on 2017/4/19.
 */
public class TradeStateContext {

    private StateContext<FlowState, TradeEvent> stateContext;

    public TradeStateContext(StateContext<FlowState, TradeEvent> stateContext) {
        this.stateContext = stateContext;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public TradeStateContext put(Object key, Object value) {
        stateContext.getExtendedState().getVariables().put(key, value);
        return this;
    }

    /**
     * @return
     */
    public Trade getTrade() {
        return this.stateContext.getExtendedState().get(Trade.class, Trade.class);
    }

    /**
     * @return
     */
    public StateRequest getRequest() {
        return this.stateContext.getExtendedState().get(StateRequest.class, StateRequest.class);
    }

    /**
     * @return
     */
    public Operator getOperator() {
        return getRequest().getOperator();
    }

    /**
     * @param <T>
     * @return
     */
    public <T> T getRequestData() {
        return (T) getRequest().getData();
    }

    /**
     * @return
     */
    public StateMachine<FlowState, TradeEvent> getStateMachine() {
        return this.stateContext.getStateMachine();
    }

    /**
     * @return
     */
    public StateContext<FlowState, TradeEvent> getStateContext() {
        return stateContext;
    }
}
