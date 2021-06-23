package com.wanmi.sbc.trade;

import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.TradePushRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetBookingTypeByIdResponse;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.FlowState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 异步线程推送0元订单到ERP系统
 */
@Service
@Slf4j
public class PayBaseAsycService {

    @Autowired
    private TradeProvider tradeProvider;

    @Async
    public void batchPushOrderToErp(List<String> tradeIds){
        tradeIds.forEach(tradeId->{
            TradePushRequest request =
                    TradePushRequest.builder().tid(tradeId).build();
            tradeProvider.pushOrderToERP(request);

//            // 查询订单信息,判断定金支付
//            TradeGetBookingTypeByIdResponse trade = tradeProvider.queryTradeInformation(tradeId).getContext();
//            if (BookingType.EARNEST_MONEY == trade.getBookingType()){
//                if (trade.getTradeState().getFlowState() == FlowState.AUDIT){
//                    log.info("0元订单======>订单号:{},订单类型:{},订单状态：{}",trade.getId(),trade.getBookingType(),trade.getTradeState().getFlowState());
//                    tradeProvider.pushOrderToERP(request);
//                }else {
//                    log.info("0元订单======>订单号:{},订单类型:{},订单状态：{}",trade.getId(),trade.getBookingType(),trade.getTradeState().getFlowState());
//                }
//            }else {
//                log.info("0元订单======>订单号:{},订单类型:{},订单状态：{}",trade.getId(),trade.getBookingType(),trade.getTradeState().getFlowState());
//                tradeProvider.pushOrderToERP(request);
//            }

        });
    }
}
