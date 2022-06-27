package com.wanmi.sbc.order.trade.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingTradePluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingTradeBatchWrapperRequest;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingWrapperDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.DiscountsPriceDetail;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.TradeParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 计算价格
 */
@Slf4j
@Service
public class CalcTradePriceService {
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private TradeGoodsService tradeGoodsService;
    @Autowired
    private TradeItemService tradeItemService;
    @Autowired
    private TradeMarketingService tradeMarketingService;
    @Autowired
    private MarketingTradePluginProvider marketingTradePluginProvider;
    @Autowired
    private TradeCacheService tradeCacheService;

    /**
     * @see TradeService#validateAndWrapperTrade(com.wanmi.sbc.order.trade.model.root.Trade, com.wanmi.sbc.order.trade.request.TradeParams)
     */
    public Trade calc(Trade trade, TradeParams tradeParams) {
        // 2.1.设置订单基本信息(购买人,商家,代客下单操作人,收货地址,发票信息,配送方式,支付方式,备注,附件,操作人ip,订单商品,订单总价...)
        trade.setTradeItems(tradeParams.getTradeItems());
        trade.setTradePrice(tradeParams.getTradePrice());

        // 2.2.订单中商品信息填充(同时设置商品的客户级别价格/客户指定价salePrice)
        // 计算价格前换购商品排除
        trade.setTradeItems(trade.getTradeItems().stream().filter(tradeItem -> !Boolean.TRUE.equals(tradeItem.getIsMarkupGoods())).collect(Collectors.toList()));
        TradeGoodsListVO skuList;
        if (Objects.isNull(tradeParams.getCustomer())) {
            GoodsInfoViewByIdsResponse idsResponse = tradeCacheService.getGoodsInfoViewByIds(IteratorUtils.collectKey(trade.getTradeItems(), TradeItem::getSkuId));
            CustomerSimplifyOrderCommitVO customerVO = verifyService.simplifyById(trade.getBuyer().getId());
            skuList = tradeGoodsService.getGoodsInfoResponse(trade,customerVO,idsResponse);
        } else {
            skuList = tradeGoodsService.getGoodsInfoResponse(trade,tradeParams.getCustomer(),tradeParams.getGoodsInfoViewByIdsResponse());
        }



        // 2.3.若是后端下单/修改,校验商家跟商品的关系(因为前端下单信息都是从库里读取的,无需验证)

        // 2.4.校验sku 和 【商品价格计算第①步】: 商品的 客户级别价格 (完成客户级别价格/客户指定价/订货区间价计算) -> levelPrice
        verifyService.verifyGoods(trade.getTradeItems(), tradeParams.getOldTradeItems(), skuList,
                trade.getSupplier().getStoreId(), true, null);

        // 2.5.处理分销

        // 2.6.商品营销信息冗余,验证,计算,设置各营销优惠,实付金额
        tradeParams.getMarketingList().forEach(i -> {
            List<TradeItem> items = trade.getTradeItems().stream().filter(s -> i.getSkuIds().contains(s.getSkuId()))
                    .collect(Collectors.toList());
            items.forEach(s -> s.getMarketingIds().add(i.getMarketingId()));
        });
        // 拼团订单--处理
        // 校验组合购活动信息

        
        //构建订单满系营销对象 优惠券
        this.wrapperMarketingForCommit(trade, tradeParams);

        // 2.7.赠品信息校验与填充

        // 2.8.计算满系营销、优惠券均摊价，并设置结算信息
        calcMarketingPrice(trade);

        // 2.9.计算并设置订单总价(已减去营销优惠总金额)
        trade.setTradePrice(calc(trade));

        // 2.10.计算运费
        // 2.11.计算订单总价(追加运费)
        return trade;
    }

    /**
     * 包装营销信息(供提交订单使用)
     */
    public void wrapperMarketingForCommit(Trade trade, TradeParams tradeParams) {

        // 1.构建订单满系营销对象
        trade.setTradeMarketings(this.wrapperMarketingForConfirm(trade.getTradeItems(),
                tradeParams.getMarketingList()));

        // 2.构建订单优惠券对象
        if (StringUtils.isNotEmpty(tradeParams.getCouponCodeId())) {
            trade.setTradeCoupon(tradeMarketingService.buildTradeCouponInfo(
                    trade.getTradeItems(), tradeParams.getCouponCodeId(), tradeParams.isForceCommit(),
                    trade.getBuyer().getId()));
        }

    }

    /**
     * 营销价格计算-结算信息设置
     * 【商品价格计算第②步】: 商品的 满折/满减营销活动 均摊价 -> splitPrice
     *
     * @param trade
     */
    private void calcMarketingPrice(Trade trade) {
        // 1.设置满系营销商品优惠后的均摊价、结算信息
        trade.getTradeMarketings().stream()
                .filter(i -> i.getMarketingType() != MarketingType.GIFT && i.getMarketingType() != MarketingType.MARKUP).forEach(i -> {
            List<TradeItem> items = trade.getTradeItems().stream().filter(t -> Objects.isNull(t.getIsMarkupGoods()) || !t.getIsMarkupGoods())
                    .filter(t -> i.getSkuIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            tradeItemService.clacSplitPrice(items, i.getRealPayAmount());
            items.forEach(t -> {
                List<TradeItem.MarketingSettlement> settlements = new ArrayList<>();
                settlements.add(TradeItem.MarketingSettlement.builder().marketingType(i.getMarketingType())
                        .splitPrice(t.getSplitPrice()).build());
                t.setMarketingSettlements(settlements);
            });
        });

        // 2.设置店铺优惠券后的均摊价、结算信息
        TradeCouponVO tradeCoupon = trade.getTradeCoupon();
        if (tradeCoupon != null) {
            // 2.1.查找出优惠券关联的商品，及总价
            List<TradeItem> items = trade.getTradeItems().stream()
                    .filter(t -> trade.getTradeCoupon().getGoodsInfoIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            // 换购商品加入店铺优惠计算
            items.addAll(trade.getTradeItems().stream()
                    .filter(t -> Objects.nonNull(t.getIsMarkupGoods()) && t.getIsMarkupGoods())
                    .collect(Collectors.toList()));
            BigDecimal total = tradeItemService.calcSkusTotalPrice(items);

            // 2.2.判断是否达到优惠券使用门槛
            BigDecimal fullBuyPrice = tradeCoupon.getFullBuyPrice();
            if (fullBuyPrice != null && fullBuyPrice.compareTo(total) == 1) {
                throw new SbcRuntimeException(CouponErrorCode.CUSTOMER_ORDER_COUPON_INVALID);
            }

            // 2.3.如果商品总价小于优惠券优惠金额，设置优惠金额为商品总价
            if (total.compareTo(tradeCoupon.getDiscountsAmount()) == -1) {
                tradeCoupon.setDiscountsAmount(total);
            }

            // 2.4.计算均摊价、结算信息
            items.forEach(item -> {
                if (Objects.isNull(item.getCouponSettlements())) {
                    item.setCouponSettlements(new ArrayList<>());
                }
                item.getCouponSettlements().add(TradeItem.CouponSettlement.builder()
                        .couponType(tradeCoupon.getCouponType())
                        .couponCodeId(tradeCoupon.getCouponCodeId())
                        .couponCode(tradeCoupon.getCouponCode())
                        .splitPrice(item.getSplitPrice()).build());
            });
            tradeItemService.calcSplitPrice(items, total.subtract(trade.getTradeCoupon().getDiscountsAmount()), total);
            items.forEach(item -> {
                TradeItem.CouponSettlement settlement =
                        item.getCouponSettlements().get(item.getCouponSettlements().size() - 1);
                settlement.setReducePrice(settlement.getSplitPrice().subtract(item.getSplitPrice()));
                settlement.setSplitPrice(item.getSplitPrice());
            });
        }
    }

    /**
     * 包装营销信息(供确认订单使用)
     */
    public List<TradeMarketingVO> wrapperMarketingForConfirm(List<TradeItem> skus, List<TradeMarketingDTO>
            tradeMarketingRequests) {

        //积分换购活动只允许购物车存在一件商品
        for (TradeMarketingDTO tradeMarketingRequest : tradeMarketingRequests) {
            Integer marketingSubType = tradeMarketingRequest.getMarketingSubType();
            if(marketingSubType != null && MarketingSubType.POINT_BUY.toValue() == marketingSubType){
                if(skus.size() > 1){
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "不满足积分换购活动条件");
                }
                for (TradeItem tradeItem : skus) {
                    if(tradeItem.getNum() > 1){
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "不满足积分换购活动条件");
                    }
                }
            }
        }
        // 1.构建营销插件请求对象
        List<TradeMarketingWrapperDTO> requests = new ArrayList<>();
        List<TradeMarketingVO> tradeMarketings = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeMarketingRequests)) {
            tradeMarketingRequests.forEach(tradeMarketing -> {
                List<TradeItemInfoDTO> tradeItems = skus.stream()
                        .filter(s -> tradeMarketing.getSkuIds().contains(s.getSkuId()))
                        .map(t -> TradeItemInfoDTO.builder()
                                .num(t.getNum())
                                .price(t.getPrice())
                                .skuId(t.getSkuId())
                                .storeId(t.getStoreId())
                                .goodsType(t.getGoodsType())
                                .distributionGoodsAudit(t.getDistributionGoodsAudit())
                                .build())
                        .collect(Collectors.toList());
                requests.add(TradeMarketingWrapperDTO.builder().tradeMarketingDTO(tradeMarketing)
                        .tradeItems(tradeItems).build());
            });


        }
        // requests.stream().filter(r->CollectionUtils.isNotEmpty(r.getTradeItems())).collect(Collectors.toList());
        // 2.调用营销插件，并设置满系营销信息
        if (CollectionUtils.isNotEmpty(requests)) {
            List<TradeMarketingWrapperVO> voList = marketingTradePluginProvider.batchWrapper(MarketingTradeBatchWrapperRequest.builder()
                    .wraperDTOList(requests).build()).getContext().getWraperVOList();
            if (CollectionUtils.isNotEmpty(voList)) {
                voList.forEach(tradeMarketingWrapperVO -> {
                    tradeMarketings.add(tradeMarketingWrapperVO.getTradeMarketing());
                });
            }
        }

        return tradeMarketings;
    }

    /**
     * 计算订单价格
     * 订单价格 = 商品总价 - 营销优惠总金额
     *
     * @param trade
     */
    private TradePrice calc(Trade trade) {
        TradePrice tradePriceTemp = trade.getTradePrice();
        if (tradePriceTemp == null) {
            tradePriceTemp = new TradePrice();
            trade.setTradePrice(tradePriceTemp);
        }
        final TradePrice tradePrice = tradePriceTemp;

        // 1.计算商品总价
        handlePrice(trade.getTradeItems(), tradePrice);
        List<TradeMarketingVO> list = trade.getTradeMarketings().stream().filter(i -> i.getMarketingType()
                != MarketingType.GIFT && i.getMarketingType()
                != MarketingType.MARKUP).collect(Collectors.toList());

        // 2.计算所有营销活动的总优惠金额(非满赠)
        BigDecimal discountPrice = list.stream().filter(i -> i.getMarketingType() != MarketingType.GIFT && i.getMarketingType()
                != MarketingType.MARKUP).map
                (TradeMarketingVO
                        ::getDiscountsAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //营销活动优惠总额
        tradePrice.setMarketingDiscountPrice(discountPrice);

        if (trade.getTradeCoupon() != null) {
            discountPrice = discountPrice.add(trade.getTradeCoupon().getDiscountsAmount());
        }

        // 3.计算各类营销活动的优惠金额(比如:满折优惠xxx,满减优惠yyy)
        List<DiscountsPriceDetail> discountsPriceDetails = new ArrayList<>();
        list.stream().collect(Collectors.groupingBy(TradeMarketingVO::getMarketingType)).forEach((key, value) -> {
            DiscountsPriceDetail detail = DiscountsPriceDetail.builder()
                    .marketingType(key)
                    .discounts(value.stream().map(TradeMarketingVO::getDiscountsAmount).reduce(BigDecimal.ZERO,
                            BigDecimal::add))
                    .build();
            discountsPriceDetails.add(detail);
        });
        tradePrice.setDiscountsPriceDetails(discountsPriceDetails);

        // 4.设置优惠券优惠金额
        if (trade.getTradeCoupon() != null) {
            BigDecimal couponPrice = trade.getTradeCoupon().getDiscountsAmount();
            tradePrice.setCouponPrice(couponPrice);
        }

        // 5.设置优惠总金额、应付金额 = 商品总金额 - 总优惠金额
        tradePrice.setDiscountsPrice(discountPrice);
        tradePrice.setTotalPrice(tradePrice.getTotalPrice().subtract(discountPrice));
        return tradePrice;
    }

    /**
     * 计算商品总价
     *
     * @param tradeItems 多个订单项(商品)
     * @param tradePrice 订单价格对象(其中包括商品商品总金额,原始金额,应付金额)
     */
    private void handlePrice(List<TradeItem> tradeItems, TradePrice tradePrice) {
        tradePrice.setGoodsPrice(BigDecimal.ZERO);
        tradePrice.setOriginPrice(BigDecimal.ZERO);
        tradePrice.setTotalPrice(BigDecimal.ZERO);
        tradePrice.setBuyPoints(null);
        tradeItems.forEach(t -> {
            BigDecimal buyItemPrice = t.getPrice().multiply(BigDecimal.valueOf(t.getNum()));
            // 订单商品总价
            tradePrice.setGoodsPrice(tradePrice.getGoodsPrice().add(buyItemPrice));
            // 订单应付总金额
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(buyItemPrice));
            // 订单原始总金额
            tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(buyItemPrice));
            // 订单积分价商品总积分
            if (Objects.nonNull(t.getBuyPoint())) {
                tradePrice.setBuyPoints(Objects.isNull(tradePrice.getBuyPoints()) ?
                        t.getBuyPoint() * t.getNum() : tradePrice.getBuyPoints() + t.getBuyPoint() * t.getNum());
            }
        });
    }
}