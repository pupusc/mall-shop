package com.wanmi.sbc.order.pointstrade.fsm;

import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;

/**
 * Created by mac on 2017/4/7.
 */
public interface PointsTradeBuilder {

    /**
     * @return
     */
    PointsFlowState supportState();

    /**
     * @param trade
     * @return
     */
    StateMachine<PointsFlowState, PointsTradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception;
}
