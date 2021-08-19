package com.wanmi.sbc.order.trade.service;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单超时未支付自动取消，定时任务每五分钟执行一次
 */
@Service
public class TradeTimeoutCancelAnalyseService {

    private static Logger logger = LoggerFactory.getLogger(TradeTimeoutCancelAnalyseService.class);

    @Autowired
    private TradeService tradeService;

    public void orderCancelTask() {
        // 订单查询条件（付款状态为未支付，支付顺序为先款后货）
        TradeQueryRequest request = new TradeQueryRequest();
        TradeState tradeState = new TradeState();
        tradeState.setPayState(PayState.NOT_PAID);
        request.setTradeState(tradeState);
        request.setPaymentOrder(PaymentOrder.PAY_FIRST);
        // 银联支付时间是15分钟，所以我们控制，20分钟之前进入支付状态的订单，才会转化成作废
        request.setStartPayTime(LocalDateTime.now().minusMinutes(20));
        // 查询订单
        List<Trade> tradeList = tradeService.getTradeList(request.getWhereCriteria());
        tradeList.forEach(trade -> {
            // 查询订单过期时间与当前时间判断
            LocalDateTime orderTimeout = trade.getOrderTimeOut();
            if (orderTimeout != null && LocalDateTime.now().isAfter(orderTimeout)) {
                try {
                    //订单取消操作人默认为平台系统用户
                    Operator operator = new Operator();
                    operator.setPlatform(Platform.PLATFORM);
                    operator.setName("system");
                    operator.setAccount("system");
                    operator.setIp("127.0.0.1");
                    //订单取消方法
                    tradeService.autoCancelOrder(trade.getId(), operator);
                } catch (RuntimeException e) {
                    logger.error("自动取消超时未支付订单异常，订单号{}，error={}", trade.getId(), e);
                }
            }
        });
    }
}
