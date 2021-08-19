package com.wanmi.sbc.order.trade.fsm;

import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;

/**
 *
 * Created by mac on 2017/4/7.
 */
public interface Builder {

    /**
     *
     * @return
     */
    FlowState supportState();

    /**
     *
     * @param trade
     * @return
     */
    StateMachine<FlowState, TradeEvent> build(Trade trade, BeanFactory beanFactory) throws Exception;

}
