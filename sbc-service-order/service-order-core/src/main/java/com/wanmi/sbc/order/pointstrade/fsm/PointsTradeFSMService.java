package com.wanmi.sbc.order.pointstrade.fsm;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.PointsFlowState;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import com.wanmi.sbc.order.pointstrade.fsm.params.PointsTradeStateRequest;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

/**
 * 状态机服务
 * Created by jinwei on 28/03/2017.
 */
@Service
@Slf4j
public class PointsTradeFSMService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private PointsTradeBuilderFactory builderFactory;

    /**
     * 修改订单状态
     *
     * @param request
     * @return
     */
    public boolean changeState(PointsTradeStateRequest request) {
        //1、查找订单信息
        Trade trade = tradeRepository.findById(request.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{request.getTid()});
        }

        //2. 根据订单创建状态机
        StateMachine<PointsFlowState, PointsTradeEvent> stateMachine = builderFactory.create(trade);
        stateMachine.getExtendedState().getVariables().put(PointsTradeStateRequest.class, request);

        //3. 发送当前请求的状态
        boolean isSend = stateMachine.sendEvent(request.getEvent());
        if (!isSend) {
            log.error("创建订单状态机失败,无法从状态 {} 转向 => {}", trade.getTradeState().getFlowState().getDescription(), request.getEvent().getDescription());
            throw new SbcRuntimeException("K-050102");
        }

        //4. 判断处理过程中是否出现了异常
        Exception exception = stateMachine.getExtendedState().get(Exception.class, Exception.class);
        if (exception != null) {
            if (exception.getClass().isAssignableFrom(SbcRuntimeException.class)) {
                throw (SbcRuntimeException) exception;
            } else {
                throw new SbcRuntimeException("K-050102");
            }
        }
        return true;
    }

}
