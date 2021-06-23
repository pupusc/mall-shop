package com.wanmi.sbc.order.trade.fsm.action;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.fsm.TradeAction;
import com.wanmi.sbc.order.trade.fsm.TradeStateContext;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/4/21.
 */
@Component
public class DeliverAction extends TradeAction {

    @Override
    protected void evaluateInternal(Trade trade, StateRequest request, TradeStateContext tsc) {
        deliver(trade, tsc.getOperator(), tsc.getRequestData());
    }

    /**
     * 是否全部发货
     *
     * @return
     */
    private boolean isAllShipped(Trade trade) {
        List<TradeItem> allCollect = new ArrayList<>();
        //主订单的发货状态不受作废子订单影响
        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(trade.getId());
        List<ProviderTrade> voidProviderTradeList
                = providerTrades.stream().filter(providerTrade -> providerTrade.getTradeState().getFlowState() == FlowState.VOID).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(voidProviderTradeList)){
            List<Long> providerAndStoreIds = new ArrayList<>();
            voidProviderTradeList.forEach(providerTrade -> {
                if (CollectionUtils.isNotEmpty(providerTrade.getTradeItems())){
                    if (providerTrade.getTradeItems().get(0).getProviderId() != null) {
                        providerAndStoreIds.add(providerTrade.getTradeItems().get(0).getProviderId());
                    } else {
                        providerAndStoreIds.add(providerTrade.getTradeItems().get(0).getStoreId());
                    }
                }else if (CollectionUtils.isNotEmpty(providerTrade.getGifts())){
                    if (providerTrade.getGifts().get(0).getProviderId() != null) {
                        providerAndStoreIds.add(providerTrade.getGifts().get(0).getProviderId());
                    } else {
                        providerAndStoreIds.add(providerTrade.getGifts().get(0).getStoreId());
                    }
                }
            });
            allCollect.addAll(trade.getTradeItems().stream().filter(item -> {
                if (item.getProviderId() != null){
                    return !providerAndStoreIds.contains(item.getProviderId());
                }else{
                    return !providerAndStoreIds.contains(item.getStoreId());
                }
            }).collect(Collectors.toList()));
            allCollect.addAll(trade.getGifts().stream().filter(item -> {
                if (item.getProviderId() != null){
                    return !providerAndStoreIds.contains(item.getProviderId());
                }else{
                    return !providerAndStoreIds.contains(item.getStoreId());
                }
            }).collect(Collectors.toList()));
        }else{
            //添加赠品
            allCollect.addAll(trade.getTradeItems());
            allCollect.addAll(trade.getGifts());
        }
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

        //周期购订单 重新设置商品数量 购买的数量*期数
        if(trade.getCycleBuyFlag()) {
            trade.getTradeItems().forEach(tradeItem -> {
                tradeItem.setNum(tradeItem.getCycleNum()*tradeItem.getNum());
            });
        }

        StringBuilder stringBuilder = new StringBuilder(200);

        //处理商品
        handleShippingItems(trade, tradeDeliver.getShippingItems(), stringBuilder, operator, false);

        //处理赠品
        handleShippingItems(trade, tradeDeliver.getGiftItemList(), stringBuilder, operator, true);

        stringBuilder.trimToSize();
        /*tradeDeliver.setStatus(DeliverStatus.SHIPPED);
        trade.addTradeDeliver(tradeDeliver);
        trade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(operator)
                .eventType(FlowState.DELIVERED.getDescription())
                .eventDetail(stringBuilder.toString())
                .eventTime(LocalDateTime.now())
                .build());*/

        // 更改发货状态
        boolean allShipped = isAllShipped(trade);
        if (allShipped) {
            tradeDeliver.setStatus(DeliverStatus.SHIPPED);
            trade.addTradeDeliver(tradeDeliver);
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
            // 定制逻辑  发货记录必须完成状态
            tradeDeliver.setStatus(DeliverStatus.SHIPPED);
            trade.addTradeDeliver(tradeDeliver);
            trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
            trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(FlowState.DELIVERED_PART.getDescription())
                    .eventDetail(String.format("订单[%s],已部分发货,发货人:%s", trade.getId(), operator.getName()))
                    .eventTime(LocalDateTime.now())
                    .build());
        }

        if (logger.isInfoEnabled()) {
            logger.info(String.format("订单[%s] => 发货状态[ %s ], 流程状态[ %s ]", trade.getId(), trade.getTradeState().getDeliverStatus(), trade.getTradeState().getFlowState()));
        }

        //周期购订单 设置相关发货信息
        if(trade.getCycleBuyFlag()) {
            //周期购订单 还原设置商品数量 购买的数量/期数
            trade.getTradeItems().forEach(tradeItem -> {
                tradeItem.setNum(tradeItem.getNum()/tradeItem.getCycleNum());
            });
            if(isShippingItems(tradeDeliver)){
                TradeCycleBuyInfo tradeCycleBuyInfo=trade.getTradeCycleBuyInfo();
                List<DeliverCalendar> deliverCalendars=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus()==CycleDeliverStatus.NOT_SHIPPED).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deliverCalendars)) {
                    //更新发货日历状态
                    deliverCalendars.get(0).setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                }
                //设置第几期
                int count=trade.getTradeDelivers().size();
                tradeDeliver.setCycleNum(count);
            }

        }
        //更新订单信息
        save(trade);

        if (allShipped) {
            super.operationLogMq.convertAndSend(operator, FlowState.DELIVERED.getDescription(), trade.getTradeEventLogs().get(0).getEventDetail());
            super.operationLogMq.convertAndSend(operator, FlowState.DELIVERED.getDescription(), trade.getTradeEventLogs().get(1).getEventDetail());
        } else {
            super.operationLogMq.convertAndSend(operator, FlowState.DELIVERED.getDescription(), trade.getTradeEventLogs().get(0).getEventDetail());
        }
    }


    private Boolean handleShippingItems(Trade trade, List<ShippingItem> shippingItems, StringBuilder stringBuilder, Operator operator, boolean isGift){
        AtomicBoolean flag = new AtomicBoolean(false);
        ConcurrentHashMap<String, TradeItem> skuItemMap;
        if(isGift){
            skuItemMap = trade.giftSkuItemMap();
        }else{
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
                //flag.set(true);
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
        return flag.get();
    }


    /**
     * 判断本次发货是赠品发货还是主品发货
     * @param tradeDeliver
     * @return
     */
    private Boolean isShippingItems( TradeDeliver tradeDeliver){
        if (CollectionUtils.isNotEmpty(tradeDeliver.getShippingItems()) ) {
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }

    }
}
