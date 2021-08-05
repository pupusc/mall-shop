package com.wanmi.sbc.order.trade.fsm;

import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceModifyOrderStatusRequest;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.orderinvoice.service.OrderInvoiceService;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 状态机服务
 * Created by jinwei on 28/03/2017.
 */
@Service
@Slf4j
public class TradeFSMService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private BuilderFactory builderFactory;

    @Autowired
    private OrderInvoiceService orderInvoiceService;


    /**
     * 修改订单状态
     *
     * @param request
     * @return
     */
    public boolean changeState(StateRequest request) {
        //1、查找订单信息
        Trade trade = tradeRepository.findById(request.getTid()).orElse(null);
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{request.getTid()});
        }

        //2. 根据订单创建状态机
        StateMachine<FlowState, TradeEvent> stateMachine = builderFactory.create(trade);
        stateMachine.getExtendedState().getVariables().put(StateRequest.class, request);

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


        //排除不开票的订单
        if (!Objects.equals(trade.getInvoice().getType(),-1)) {
            FlowState flowState= stateMachine.getState().getId();
            //更新订单状态
            OrderInvoiceModifyOrderStatusRequest orderInvoiceModifyOrderStatusRequest=new OrderInvoiceModifyOrderStatusRequest();
            orderInvoiceModifyOrderStatusRequest.setOrderNo(trade.getId());
            orderInvoiceModifyOrderStatusRequest.setOrderStatus(flowState);
            if (PayState.NOT_PAID==trade.getTradeState().getPayState()) {
                orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.NOTPAY);
            } else  if (PayState.PAID==trade.getTradeState().getPayState()) {
                orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.PAYED);
            }else  if (PayState.UNCONFIRMED==trade.getTradeState().getPayState()) {
                orderInvoiceModifyOrderStatusRequest.setPayOrderStatus(PayOrderStatus.TOCONFIRM);
            }
            log.info("订单开票更新订单状态，订单编号 {},订单当前状态：{}，更新之后的状态：{}", trade.getId(),trade.getTradeState().getFlowState(),flowState);
            orderInvoiceService.updateOrderStatus(orderInvoiceModifyOrderStatusRequest);
        }
        return true;
    }

}
