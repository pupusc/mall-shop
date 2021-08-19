package com.wanmi.sbc.order.pointstrade.fsm;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.pointstrade.fsm.params.PointsTradeStateRequest;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

/**
 * Created by Administrator on 2017/4/19.
 */
public class PointsTradeStateContext {

    private StateContext<PointsFlowState, PointsTradeEvent> stateContext;

    public PointsTradeStateContext(StateContext<PointsFlowState, PointsTradeEvent> stateContext) {
        this.stateContext = stateContext;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public PointsTradeStateContext put(Object key, Object value) {
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
    public PointsTradeStateRequest getRequest() {
        return this.stateContext.getExtendedState().get(PointsTradeStateRequest.class, PointsTradeStateRequest.class);
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
    public StateMachine<PointsFlowState, PointsTradeEvent> getStateMachine() {
        return this.stateContext.getStateMachine();
    }

    /**
     * @return
     */
    public StateContext<PointsFlowState, PointsTradeEvent> getStateContext() {
        return stateContext;
    }
}
