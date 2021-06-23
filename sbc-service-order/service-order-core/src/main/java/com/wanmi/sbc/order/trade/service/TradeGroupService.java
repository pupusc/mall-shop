package com.wanmi.sbc.order.trade.service;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.trade.model.entity.TradeGrouponCommitForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroup;
import com.wanmi.sbc.order.trade.repository.TradeGroupRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TradeGroupService {

    @Autowired
    private TradeGroupRepository tradeGroupRepository;

    @Autowired
    private TradeItemService tradeItemService;

    @Autowired
    private TradeMarketingService tradeMarketingService;

    @Autowired
    private TradeService tradeService;


    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param tradeGroup
     */
    public void addTradeGroup(TradeGroup tradeGroup) {
        tradeGroupRepository.save(tradeGroup);
    }


    /**
     * 构建订单组对象，同时修改订单列表中相应的价格信息
     *
     * @param trades             订单列表
     * @param tradeCommitRequest
     * @return
     */
    public TradeGroup wrapperTradeGroup(
            List<Trade> trades, TradeCommitRequest tradeCommitRequest, TradeGrouponCommitForm grouponForm) {
        if (tradeCommitRequest.getCommonCodeId() == null) {
            return null;
        }
        CustomerSimplifyOrderCommitVO customer = tradeCommitRequest.getCustomer();
        TradeGroup tradeGroup = new TradeGroup();

        // 1.请求营销插件，验证并包装优惠券信息
        List<TradeItem> items = trades.stream().flatMap(trade -> trade.getTradeItems().stream()).collect(Collectors
                .toList());
        TradeCouponVO tradeCoupon = tradeMarketingService.buildTradeCouponInfo(
                items,
                tradeCommitRequest.getCommonCodeId(),
                tradeCommitRequest.isForceCommit(),
                customer.getCustomerId());
        if (tradeCoupon == null) {
            return null;
        }

        // 2.找出需要均摊的商品，以及总价
        List<TradeItem> matchItems = items.stream()
                .filter(t -> tradeCoupon.getGoodsInfoIds().contains(t.getSkuId())).collect(Collectors.toList());
        BigDecimal total = tradeItemService.calcSkusTotalPrice(matchItems);

        // 3.判断是否达到优惠券使用门槛
        BigDecimal fullBuyPrice = tradeCoupon.getFullBuyPrice();
        if (fullBuyPrice != null && fullBuyPrice.compareTo(total) == 1) {
            throw new SbcRuntimeException(CouponErrorCode.CUSTOMER_ORDER_COUPON_INVALID);
        }

        // 4.如果商品总价小于优惠券优惠金额，设置优惠金额为商品总价
        if (total.compareTo(tradeCoupon.getDiscountsAmount()) == -1) {
            tradeCoupon.setDiscountsAmount(total);
        }

        // 5.设置关联商品的结算信息
        matchItems.forEach(item -> {
            if (Objects.isNull(item.getCouponSettlements())) {
                item.setCouponSettlements(new ArrayList<>());
            }
            item.getCouponSettlements().add(TradeItem.CouponSettlement.builder()
                    .couponType(tradeCoupon.getCouponType())
                    .couponCodeId(tradeCoupon.getCouponCodeId())
                    .couponCode(tradeCoupon.getCouponCode())
                    .splitPrice(item.getSplitPrice()).build());
        });

        // 6.设置关联商品的均摊价
        tradeItemService.calcSplitPrice(matchItems, total.subtract(tradeCoupon.getDiscountsAmount()), total);

        // 7.刷新关联商品的结算信息
        matchItems.forEach(item -> {
            TradeItem.CouponSettlement settlement =
                    item.getCouponSettlements().get(item.getCouponSettlements().size() - 1);
            settlement.setReducePrice(settlement.getSplitPrice().subtract(item.getSplitPrice()));
            settlement.setSplitPrice(item.getSplitPrice());
        });

        // 8.按店铺分组被均摊的商品，刷新相应订单的价格信息
        Map<Long, List<TradeItem>> itemsMap = items.stream().collect(Collectors.groupingBy(TradeItem::getStoreId));
        itemsMap.keySet().forEach(storeId -> {
            // 8.1.找到店铺对应订单的价格信息
            Trade trade = trades.stream()
                    .filter(t -> t.getSupplier().getStoreId().equals(storeId)).findFirst().orElse(null);
            TradePrice tradePrice = trade.getTradePrice();
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                    && Objects.nonNull(tradePrice.getTailPrice())) {
                // 8.2.计算平台优惠券优惠额(couponPrice)，并追加至订单优惠金额、优惠券优惠金额
                BigDecimal marketTotalPrice = tradePrice.getTailPrice().subtract(tradePrice.getDiscountsPrice());
                BigDecimal couponPrice = tradeCoupon.getDiscountsAmount();
                tradePrice.setDiscountsPrice(tradePrice.getDiscountsPrice().add(couponPrice));
                tradePrice.setCouponPrice(tradePrice.getCouponPrice().add(couponPrice));
                tradePrice.setOriginPrice(tradePrice.getGoodsPrice());
            } else {
                // 8.2.计算平台优惠券优惠额(couponPrice)，并追加至订单优惠金额、优惠券优惠金额
                BigDecimal marketTotalPrice = tradePrice.getGoodsPrice().subtract(tradePrice.getDiscountsPrice());
                BigDecimal couponTotalPrice = tradeItemService.calcSkusTotalPrice(itemsMap.get(storeId));
                BigDecimal couponPrice = marketTotalPrice.subtract(couponTotalPrice);
                tradePrice.setDiscountsPrice(tradePrice.getDiscountsPrice().add(couponPrice));
                tradePrice.setCouponPrice(tradePrice.getCouponPrice().add(couponPrice));
                // 8.3.重设订单总价、原始金额
                tradePrice.setTotalPrice(couponTotalPrice);
                tradePrice.setOriginPrice(tradePrice.getGoodsPrice());
            }


            trade.getTradeItems().forEach(tradeItem -> {
                TradeItem matchItem = matchItems.stream().filter(
                        item -> item.getSkuId().equals(tradeItem.getSkuId())).findFirst().orElse(null);
                if (matchItem != null) {
                    tradeItem.setSplitPrice(matchItem.getSplitPrice());
                }
            });

            boolean virtualCouponGoods = Objects.isNull(trade.getIsVirtualCouponGoods())
                    || Boolean.FALSE.equals(trade.getIsVirtualCouponGoods());
            // 8.4.计算运费
            BigDecimal deliveryPrice = virtualCouponGoods ? tradeService.calcTradeFreight(trade.getConsignee(), trade.getSupplier(),
                    trade.getDeliverWay(),
                    tradePrice.getTotalPrice(), trade.getTradeItems(), trade.getGifts()) : BigDecimal.ZERO;
            if (Objects.nonNull(grouponForm) && grouponForm.isFreeDelivery()) {
                // 如果拼团活动包邮
                deliveryPrice = BigDecimal.ZERO;
            }

            tradePrice.setDeliveryPrice(deliveryPrice);

            // 8.5.订单总价、原始金额追加运费
            tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(deliveryPrice));
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(deliveryPrice));//应付金额 = 应付+运费
        });

        // 9.设置订单组平台优惠券
        tradeGroup.setCommonCoupon(tradeCoupon);
        return tradeGroup;
    }


}
