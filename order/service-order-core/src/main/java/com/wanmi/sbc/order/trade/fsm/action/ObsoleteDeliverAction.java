package com.wanmi.sbc.order.trade.fsm.action;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.trade.fsm.TradeAction;
import com.wanmi.sbc.order.trade.fsm.TradeStateContext;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 作废发货
 * Created by chenpeng on 2017/6/1.
 */
@Component
public class ObsoleteDeliverAction extends TradeAction {

    @Override
    protected void evaluateInternal(Trade trade, StateRequest request, TradeStateContext tsc) {
        obsoleteDeliveryRecords(trade, request.getOperator(), tsc.getRequestData());
    }

    /**
     * 是否全部未发货
     *
     * @param trade
     * @return
     */
    private boolean isAllNotShipped(Trade trade) {
        List<TradeItem> allItems = new ArrayList<>();
        allItems.addAll(trade.getTradeItems());
        allItems.addAll(trade.getGifts());
        List<TradeItem> collect = allItems.stream()
                .filter(tradeItem -> !tradeItem.getDeliveredNum().equals(0L))
                .collect(Collectors.toList());
        return collect.isEmpty();
    }
    /**
     * 是否全部未发货
     *
     * @param trade
     * @return
     */
    private boolean isAllNotShipped(ProviderTrade trade) {
        List<TradeItem> allItems = new ArrayList<>();
        allItems.addAll(trade.getTradeItems());
        allItems.addAll(trade.getGifts());
        List<TradeItem> collect = allItems.stream()
                .filter(tradeItem -> !tradeItem.getDeliveredNum().equals(0L))
                .collect(Collectors.toList());
        return collect.isEmpty();
    }

    /**
     * 作废发货记录
     *
     * @param trade
     * @param operator
     * @param deliverId
     */
    private void obsoleteDeliveryRecords(Trade trade, Operator operator, String deliverId) {

        List<TradeDeliver> tradeDelivers = trade.getTradeDelivers();
        //查询发货记录
        Optional<TradeDeliver> tradeDeliverOptional = tradeDelivers
                .stream()
                .filter(tradeDeliver -> StringUtils.equals(deliverId, tradeDeliver.getDeliverId()))
                .findFirst();

        if (tradeDeliverOptional.isPresent()) {
            StringBuilder stringBuilder = new StringBuilder(200);

            TradeDeliver tradeDeliver = tradeDeliverOptional.get();

            //处理商品
            handleShippingItems(trade, tradeDeliver.getShippingItems(), stringBuilder, false);

            //处理赠品
            handleShippingItems(trade, tradeDeliver.getGiftItemList(), stringBuilder, true);

            //订单状态更新
            TradeState tradeState = trade.getTradeState();
            if (isAllNotShipped(trade)) {
                tradeState.setFlowState(FlowState.AUDIT);
                tradeState.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
            } else {
                tradeState.setFlowState(FlowState.DELIVERED_PART);
                tradeState.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }

            //添加操作日志
            stringBuilder.trimToSize();
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(TradeEvent.OBSOLETE_DELIVER.getDescription())
                    .eventDetail(stringBuilder.toString())
                    .eventTime(LocalDateTime.now())
                    .build());

            //设置周期购订单发货日历的状态
            if(trade.getCycleBuyFlag()) {
                TradeCycleBuyInfo tradeCycleBuyInfo=trade.getTradeCycleBuyInfo();
                List<DeliverCalendar> deliverCalendars=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus()==CycleDeliverStatus.SHIPPED).collect(Collectors.toList());
                deliverCalendars.get(deliverCalendars.size()-1).setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);

                List<TradeDeliver> tradeDeliverList= tradeDelivers.stream().filter(tradeDeliver1 -> tradeDeliver1.getCycleNum()>tradeDeliver.getCycleNum()).collect(Collectors.toList());

                //作废期数
                tradeDeliverList.forEach(tradeDeliver1 -> {
                    tradeDeliver1.setCycleNum(tradeDeliver1.getCycleNum()-1);
                });
            }
            //删除发货单
            tradeDelivers.remove(tradeDeliver);

            //保存
            save(trade);

            //处理子单
            List<ProviderTrade> listByParentId = providerTradeService.findListByParentId(trade.getId());
            if(CollectionUtils.isNotEmpty(listByParentId)){
                for (ProviderTrade providerTrade : listByParentId) {
                    List<String> deliverIds = providerTrade.getTradeDelivers().stream().map(TradeDeliver::getDeliverId).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(deliverIds) && deliverIds.contains(tradeDeliver.getSunDeliverId())){
                        providerObsoleteDeliveryRecords(providerTrade, operator, tradeDeliver.getSunDeliverId());
                    }
                }
            }
            super.operationLogMq.convertAndSend(operator, TradeEvent.OBSOLETE_DELIVER.getDescription(), stringBuilder.toString());
        }


    }

    private void providerObsoleteDeliveryRecords(ProviderTrade providerTrade, Operator operator, String deliverId) {
        List<TradeDeliver> tradeDelivers = providerTrade.getTradeDelivers();
        //查询发货记录
        Optional<TradeDeliver> tradeDeliverOptional = tradeDelivers
                .stream()
                .filter(tradeDeliver -> StringUtils.equals(deliverId, tradeDeliver.getDeliverId()))
                .findFirst();
        if (tradeDeliverOptional.isPresent()) {
            StringBuilder stringBuilder = new StringBuilder(200);

            TradeDeliver tradeDeliver = tradeDeliverOptional.get();

            //处理商品
            providerHandleShippingItems(providerTrade, tradeDeliver.getShippingItems(), stringBuilder, false);

            //处理赠品
            providerHandleShippingItems(providerTrade, tradeDeliver.getGiftItemList(), stringBuilder, true);

            //订单状态更新
            TradeState tradeState = providerTrade.getTradeState();
            if (isAllNotShipped(providerTrade)) {
                tradeState.setFlowState(FlowState.AUDIT);
                tradeState.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
            } else {
                tradeState.setFlowState(FlowState.DELIVERED_PART);
                tradeState.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }

            //添加操作日志
            stringBuilder.trimToSize();
            providerTrade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(TradeEvent.OBSOLETE_DELIVER.getDescription())
                    .eventDetail(stringBuilder.toString())
                    .eventTime(LocalDateTime.now())
                    .build());

            //设置周期购订单发货日历的状态
            if(providerTrade.getCycleBuyFlag()) {
                TradeCycleBuyInfo tradeCycleBuyInfo=providerTrade.getTradeCycleBuyInfo();
                List<DeliverCalendar> deliverCalendars=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus()==CycleDeliverStatus.SHIPPED).collect(Collectors.toList());
                deliverCalendars.get(deliverCalendars.size()-1).setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);

                List<TradeDeliver> tradeDeliverList= tradeDelivers.stream().filter(tradeDeliver1 -> tradeDeliver1.getCycleNum()>tradeDeliver.getCycleNum()).collect(Collectors.toList());

                //作废期数
                tradeDeliverList.forEach(tradeDeliver1 -> {
                    tradeDeliver1.setCycleNum(tradeDeliver1.getCycleNum()-1);
                });

            }

            //删除发货单
            tradeDelivers.remove(tradeDeliver);

            //保存
            saveProviderTrade(providerTrade);

            super.operationLogMq.convertAndSend(operator, TradeEvent.OBSOLETE_DELIVER.getDescription(), stringBuilder.toString());
        }

    }

    private void handleShippingItems(Trade trade, List<ShippingItem> shippingItems, StringBuilder stringBuilder, boolean isGift) {
        ConcurrentHashMap<String, TradeItem> skuItemMap ;
        if(isGift){
            skuItemMap = trade.giftSkuItemMap();
        }else{
            skuItemMap = trade.skuItemMap();
        }

        //订单商品更新
        shippingItems.forEach(shippingItem -> {
            TradeItem tradeItem = skuItemMap.get(shippingItem.getSkuId());

            Long shippedNum = tradeItem.getDeliveredNum();
            shippedNum -= shippingItem.getItemNum();
            tradeItem.setDeliveredNum(shippedNum);

            if (shippedNum.equals(0L)) {
                tradeItem.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
            } else if (shippedNum < tradeItem.getNum()) {
                tradeItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }

            stringBuilder.append(String.format("订单[%s],商品[%s], 作废发货[%s], 目前状态:[%s]\r\n",
                    trade.getId(),
                    (isGift ? "【赠品】" : "" ) + tradeItem.getSkuName(),
                    shippingItem.getItemNum().toString(),
                    tradeItem.getDeliverStatus().getDescription())
            );

        });
    }

    private void providerHandleShippingItems(ProviderTrade providerTrade, List<ShippingItem> shippingItems, StringBuilder stringBuilder, boolean isGift) {
        ConcurrentHashMap<String, TradeItem> skuItemMap ;
        if(isGift){
            skuItemMap = providerTrade.giftSkuItemMap();
        }else{
            skuItemMap = providerTrade.skuItemMap();
        }

        //订单商品更新
        shippingItems.forEach(shippingItem -> {
            TradeItem tradeItem = skuItemMap.get(shippingItem.getSkuId());

            Long shippedNum = tradeItem.getDeliveredNum();
            shippedNum -= shippingItem.getItemNum();
            tradeItem.setDeliveredNum(shippedNum);

            if (shippedNum.equals(0L)) {
                tradeItem.setDeliverStatus(DeliverStatus.NOT_YET_SHIPPED);
            } else if (shippedNum < tradeItem.getNum()) {
                tradeItem.setDeliverStatus(DeliverStatus.PART_SHIPPED);
            }

            stringBuilder.append(String.format("订单[%s],商品[%s], 作废发货[%s], 目前状态:[%s]\r\n",
                    providerTrade.getId(),
                    (isGift ? "【赠品】" : "" ) + tradeItem.getSkuName(),
                    shippingItem.getItemNum().toString(),
                    tradeItem.getDeliverStatus().getDescription())
            );

        });
    }


}
