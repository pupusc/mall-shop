package com.wanmi.sbc.order.pointstrade.fsm.action;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeAction;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeStateContext;
import com.wanmi.sbc.order.pointstrade.fsm.params.PointsTradeStateRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/4/21.
 */
@Component
public class PointsTradeDeliverAction extends PointsTradeAction {

    @Override
    protected void evaluateInternal(Trade trade, PointsTradeStateRequest request, PointsTradeStateContext tsc) {
        deliver(trade, tsc.getOperator(), tsc.getRequestData());
    }

    /**
     * 是否全部发货
     *
     * @return
     */
    private boolean isAllShipped(Trade trade) {
        List<TradeItem> allCollect = new ArrayList<>();
        //添加赠品
        allCollect.addAll(trade.getTradeItems());
        allCollect.addAll(trade.getGifts());
        List<TradeItem> collect = allCollect
                .stream()
                .filter(tradeItem -> !Objects.equals(tradeItem.getDeliveredNum(), tradeItem.getNum()))
                .collect(Collectors.toList());
        return collect.isEmpty();
    }

    /**
     * 发货
     *
     * @param trade
     * @param tradeDeliver
     */
    private void deliver(Trade trade, Operator operator, TradeDeliver tradeDeliver) {

        // 如果没有收货信息, 就用之前的存的收货信息
        if (tradeDeliver.getConsignee() == null) {
            tradeDeliver.setConsignee(trade.getConsignee());
        }

        StringBuilder stringBuilder = new StringBuilder(200);

        //处理商品
        handleShippingItems(trade, tradeDeliver.getShippingItems(), stringBuilder, operator, false);

        //处理赠品
        handleShippingItems(trade, tradeDeliver.getGiftItemList(), stringBuilder, operator, true);

        stringBuilder.trimToSize();
        tradeDeliver.setStatus(DeliverStatus.SHIPPED);
        trade.addTradeDeliver(tradeDeliver);
        trade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(operator)
                .eventType(FlowState.DELIVERED.getDescription())
                .eventDetail(stringBuilder.toString())
                .eventTime(LocalDateTime.now())
                .build());

        // 更改发货状态
        boolean allShipped = isAllShipped(trade);
        if (allShipped) {
            trade.getTradeState().setDeliverTime(LocalDateTime.now());
            trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
            trade.getTradeState().setFlowState(FlowState.DELIVERED);
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(FlowState.DELIVERED.getDescription())
                    .eventDetail(String.format("订单[%s],已全部发货,发货人:%s", trade.getId(), operator.getName()))
                    .eventTime(LocalDateTime.now())
                    .build());
        } else {
            trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
            trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
        }

        if (logger.isInfoEnabled()) {
            logger.info(String.format("订单[%s] => 发货状态[ %s ], 流程状态[ %s ]", trade.getId(), trade.getTradeState().getDeliverStatus(), trade.getTradeState().getFlowState()));
        }

        save(trade);
        if (allShipped) {
            super.operationLogMq.convertAndSend(operator, FlowState.DELIVERED.getDescription(), trade.getTradeEventLogs().get(0).getEventDetail());
            super.operationLogMq.convertAndSend(operator, FlowState.DELIVERED.getDescription(), trade.getTradeEventLogs().get(1).getEventDetail());
        } else {
            super.operationLogMq.convertAndSend(operator, FlowState.DELIVERED.getDescription(), trade.getTradeEventLogs().get(0).getEventDetail());
        }
    }


    private void handleShippingItems(Trade trade, List<ShippingItem> shippingItems, StringBuilder stringBuilder, Operator operator, boolean isGift) {
        ConcurrentHashMap<String, TradeItem> skuItemMap;
        if (isGift) {
            skuItemMap = trade.giftSkuItemMap();
        } else {
            skuItemMap = trade.skuItemMap();
        }

        shippingItems.forEach(shippingItem -> {
            TradeItem tradeItem = skuItemMap.get(shippingItem.getSkuId());

            //1. 增加发货数量
            Long hasNum = tradeItem.getDeliveredNum();
            if (hasNum != null) {
                hasNum += shippingItem.getItemNum();
            } else {
                hasNum = shippingItem.getItemNum();
            }
            tradeItem.setDeliveredNum(hasNum);


            //2. 更新发货状态
            if (hasNum.equals(tradeItem.getNum())) {
                tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
            } else if (hasNum > tradeItem.getNum()) {  //什么鬼, 发多了？
                throw new SbcRuntimeException("K-050109", new Object[]{tradeItem.getSkuId(), tradeItem.getNum(), hasNum});
            } else {
                tradeItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }
            stringBuilder.append(String.format("订单%s,商品[%s], 发货数：%s, 目前状态:[%s],发货人:%s\r\n",
                    trade.getId(),
                    (isGift ? "【赠品】" : "") + tradeItem.getSkuName(),
                    shippingItem.getItemNum().toString(),
                    tradeItem.getDeliverStatus().getDescription(),
                    operator.getName()));

            //3. 合并数据
            shippingItem.setItemName(tradeItem.getSkuName());
            shippingItem.setSpuId(tradeItem.getSpuId());
            shippingItem.setPic(tradeItem.getPic());
            shippingItem.setSpecDetails(tradeItem.getSpecDetails());
            shippingItem.setUnit(tradeItem.getUnit());
        });
    }
}
