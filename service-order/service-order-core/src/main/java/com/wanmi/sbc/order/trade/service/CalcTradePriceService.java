package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingTradePluginProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingMapGetByGoodsIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingTradeBatchWrapperRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingMapGetByGoodsIdResponse;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingWrapperDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.DiscountsPriceDetail;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

//    /**
//     * @see TradeService#validateAndWrapperTrade(com.wanmi.sbc.order.trade.model.root.Trade, com.wanmi.sbc.order.trade.request.TradeParams)
//     */
//    public Trade calc(Trade trade, TradeParams tradeParams) {
//        // 2.1.设置订单基本信息(购买人,商家,代客下单操作人,收货地址,发票信息,配送方式,支付方式,备注,附件,操作人ip,订单商品,订单总价...)
//        trade.setTradeItems(tradeParams.getTradeItems());
//        trade.setTradePrice(tradeParams.getTradePrice());
//
//        // 2.2.订单中商品信息填充(同时设置商品的客户级别价格/客户指定价salePrice)
//        // 计算价格前换购商品排除
//        trade.setTradeItems(trade.getTradeItems().stream().filter(tradeItem -> !Boolean.TRUE.equals(tradeItem.getIsMarkupGoods())).collect(Collectors.toList()));
//        TradeGoodsListVO skuList;
//        if (Objects.isNull(tradeParams.getCustomer())) {
//            GoodsInfoViewByIdsResponse idsResponse = tradeCacheService.getGoodsInfoViewByIds(IteratorUtils.collectKey(trade.getTradeItems(), TradeItem::getSkuId));
//            CustomerSimplifyOrderCommitVO customerVO = verifyService.simplifyById(trade.getBuyer().getId());
//            skuList = tradeGoodsService.getGoodsInfoResponse(trade,customerVO,idsResponse);
//        } else {
//            skuList = tradeGoodsService.getGoodsInfoResponse(trade,tradeParams.getCustomer(),tradeParams.getGoodsInfoViewByIdsResponse());
//        }
//
//
//
//        // 2.3.若是后端下单/修改,校验商家跟商品的关系(因为前端下单信息都是从库里读取的,无需验证)
//
//        // 2.4.校验sku 和 【商品价格计算第①步】: 商品的 客户级别价格 (完成客户级别价格/客户指定价/订货区间价计算) -> levelPrice
//        verifyService.verifyGoods(trade.getTradeItems(), tradeParams.getOldTradeItems(), skuList,
//                trade.getSupplier().getStoreId(), true, null);
//
//        // 2.5.处理分销
//
//        // 2.6.商品营销信息冗余,验证,计算,设置各营销优惠,实付金额
//        tradeParams.getMarketingList().forEach(i -> {
//            List<TradeItem> items = trade.getTradeItems().stream().filter(s -> i.getSkuIds().contains(s.getSkuId()))
//                    .collect(Collectors.toList());
//            items.forEach(s -> s.getMarketingIds().add(i.getMarketingId()));
//        });
//        // 拼团订单--处理
//        // 校验组合购活动信息
//
//
//        //构建订单满系营销对象 优惠券
//        this.wrapperMarketingForCommit(trade, tradeParams);
//
//        // 2.7.赠品信息校验与填充
//
//        // 2.8.计算满系营销、优惠券均摊价，并设置结算信息
//        calcMarketingPrice(trade);
//
//        // 2.9.计算并设置订单总价(已减去营销优惠总金额)
//        trade.setTradePrice(calc(trade));
//
//        // 2.10.计算运费
//        // 2.11.计算订单总价(追加运费)
//        return trade;
//    }

    /**
     * @see TradeService#validateAndWrapperTrade(com.wanmi.sbc.order.trade.model.root.Trade, com.wanmi.sbc.order.trade.request.TradeParams)
     * 暂时只支持微信小程序端的价格体系
     * 【算价流程：】
     * 1.验证用户
     * 2.验证营销有效性
     * 3.验证优惠券有效性
     * 4.查询商品信息
     * 5.验证商品信息
     */
    public TradePrice calc(List<TradeItem> tradeItems, String couponId, String customerId) {
        //商品校验
        if (tradeItems.stream().anyMatch(tradeItem -> Boolean.TRUE.equals(tradeItem.getIsMarkupGoods()))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "当前还不支持加价购活动算价");
        }
        if (tradeItems.stream().anyMatch(tradeItem -> Boolean.TRUE.equals(tradeItem.getIsBookingSaleGoods()))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "当前还不支持预售购活动算价");
        }
        if (tradeItems.stream().anyMatch(tradeItem -> Boolean.TRUE.equals(tradeItem.getIsFlashSaleGoods()))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "当前还不支持秒杀购活动算价");
        }
        //1.验证用户
        CustomerSimplifyOrderCommitVO customerVO = verifyService.simplifyById(customerId);

        //4.查询商品信息
        GoodsInfoViewByIdsResponse skuResp = tradeCacheService.getGoodsInfoViewByIds(IteratorUtils.collectKey(tradeItems, TradeItem::getSkuId));
        Trade tradeParam = new Trade();
        tradeParam.setTradeItems(tradeItems);
        TradeGoodsListVO goodsResp = tradeGoodsService.getGoodsInfoResponse(tradeParam, customerVO, skuResp);

        Map<String, GoodsInfoVO> skuMap = goodsResp.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, i -> i));

        tradeItems.forEach(item -> {
            GoodsInfoVO sku = skuMap.get(item.getSkuId());
            if (Objects.isNull(sku)) {
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS, "sku信息没有查询到, skuId={}", item.getSkuId());
            }
            item.setPrice(sku.getSalePrice());
            item.setLevelPrice(sku.getSalePrice());
            item.setOriginalPrice(Objects.isNull(sku.getMarketPrice()) ? BigDecimal.ZERO : sku.getMarketPrice());
            item.setSplitPrice(item.getPrice().multiply(new BigDecimal(item.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
        });
        //总价
        BigDecimal totalPrice = BigDecimal.ZERO;

        //按照营销活动分组
        Map<Long, List<TradeItem>> mktId2items = tradeItems.stream().filter(i->i.getMarketingId()!=null && i.getMarketingId()>0)
                .collect(Collectors.groupingBy(TradeItem::getMarketingId));
        List<String> skuIds = tradeItems.stream().map(item -> item.getSkuId()).distinct().collect(Collectors.toList());

        //根据skuId查询关联的营销活动
        MarketingMapGetByGoodsIdRequest mktParamm = MarketingMapGetByGoodsIdRequest.builder().goodsInfoIdList(skuIds)
                .marketingStatus(MarketingStatus.STARTED).deleteFlag(DeleteFlag.NO).cascadeLevel(true).build();
        BaseResponse<MarketingMapGetByGoodsIdResponse> mktResponse = marketingQueryProvider.getMarketingMapByGoodsId(mktParamm);

        Map<Long, MarketingForEndVO> mktId2mkt = new HashMap<>();
        if (mktResponse != null && mktResponse.getContext() != null && mktResponse.getContext().getListMap() != null) {
            mktResponse.getContext().getListMap().values().stream().flatMap(item->item.stream()).forEach(mkt->mktId2mkt.put(mkt.getMarketingId(), mkt));
        }

        List<TradeMarketingDTO> marketings = new ArrayList<>();
        for (Map.Entry<Long, List<TradeItem>> entry : mktId2items.entrySet()) {
            MarketingForEndVO mkt = mktId2mkt.get(entry.getKey());
            if (mkt == null) {
                log.warn("营销活动不存在或者当前不可用, mktId={}", entry.getKey());
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "指定的营销活动不存在");
            }
            if (mkt.getIsPause() == BoolFlag.YES || mkt.getDelFlag() == DeleteFlag.YES
                    || mkt.getBeginTime().isAfter(LocalDateTime.now()) || mkt.getEndTime().isBefore(LocalDateTime.now())) {
                log.warn("营销活动当前的状态信息不正确, mkt={}", JSON.toJSONString(mkt));
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "指定的营销活动状态错误");
            }

            TradeMarketingDTO dto = new TradeMarketingDTO();
            dto.setStoreId(mkt.getStoreId());
            dto.setMarketingId(mkt.getMarketingId());
            dto.setMarketingSubType(mkt.getSubType().toValue());
            dto.setSkuIds(entry.getValue().stream().map(TradeItem::getSkuId).distinct().collect(Collectors.toList()));
            //满折
            if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mkt.getSubType()) || MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mkt.getSubType())) {
                dto.setMarketingLevelId(CollectionUtils.isEmpty(mkt.getFullDiscountLevelList()) ? null : mkt.getFullDiscountLevelList().get(0).getDiscountLevelId());
            } else if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mkt.getSubType()) || MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mkt.getSubType())) {
                dto.setMarketingLevelId(CollectionUtils.isEmpty(mkt.getFullReductionLevelList()) ? null : mkt.getFullReductionLevelList().get(0).getReductionLevelId());
            } else {
                log.warn("其他营销类型暂不支持, mktId={}, subTpye={}", mkt.getMarketingId(), mkt.getSubType().toValue());
                throw new SbcRuntimeException("暂时只支持满减或满折类型的营销活动");
            }
            marketings.add(dto);
        }

        // 2.4.校验sku 和 【商品价格计算第①步】: 商品的 客户级别价格 (完成客户级别价格/客户指定价/订货区间价计算) -> levelPrice
//        verifyService.verifyGoods(tradeItems, Collections.EMPTY_LIST, skuList, null, true, null);

        // 1.构建订单满系营销对象
        List<TradeMarketingVO> tradeMarketingVOs = this.wrapperMarketingForConfirm(tradeItems, marketings);
        // 2.构建订单优惠券对象
        TradeCouponVO tradeCouponVO = StringUtils.isBlank(couponId) ? null : tradeMarketingService.buildTradeCouponInfo(tradeItems, couponId, false, customerId);
        //计算并赋值splitPrice
        calcMarketingPrice(tradeItems, tradeMarketingVOs, tradeCouponVO);
        // 2.9.计算并设置订单总价(已减去营销优惠总金额)
        return calc(tradeItems, tradeMarketingVOs, tradeCouponVO);
    }

    /**
     * 营销价格计算-结算信息设置
     * 【商品价格计算第②步】: 商品的 满折/满减营销活动 均摊价 -> splitPrice
     */
    private void calcMarketingPrice(List<TradeItem> tradeItems, List<TradeMarketingVO> tradeMarketings, TradeCouponVO tradeCoupon) {
        // 1.设置满系营销商品优惠后的均摊价、结算信息
        tradeMarketings.stream()
                .filter(i -> i.getMarketingType() != MarketingType.GIFT && i.getMarketingType() != MarketingType.MARKUP)
                .forEach(i -> {
                    List<TradeItem> items = tradeItems.stream()
                            .filter(t -> !Boolean.TRUE.equals(t.getIsMarkupGoods()) && i.getSkuIds().contains(t.getSkuId())).collect(Collectors.toList());
                    tradeItemService.clacSplitPrice(items, i.getRealPayAmount());
                    items.forEach(t -> {
                        List<TradeItem.MarketingSettlement> settlements = new ArrayList<>();
                        settlements.add(TradeItem.MarketingSettlement.builder().marketingType(i.getMarketingType()).splitPrice(t.getSplitPrice()).build());
                        t.setMarketingSettlements(settlements);
                    });
                });

        // 2.设置店铺优惠券后的均摊价、结算信息
        if (tradeCoupon != null) {
            // 2.1.查找出优惠券关联的商品，及总价
            List<TradeItem> items = tradeItems.stream()
                    .filter(t -> tradeCoupon.getGoodsInfoIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            // 换购商品加入店铺优惠计算
            items.addAll(tradeItems.stream()
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
            tradeItemService.calcSplitPrice(items, total.subtract(tradeCoupon.getDiscountsAmount()), total);
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
    private List<TradeMarketingVO> wrapperMarketingForConfirm(List<TradeItem> skus, List<TradeMarketingDTO> marketings) {
        if (CollectionUtils.isEmpty(marketings)) {
            return Collections.EMPTY_LIST;
        }

        Map<Long, TradeMarketingDTO> mktId2mkt = marketings.stream().collect(Collectors.toMap(TradeMarketingDTO::getMarketingId, item -> item, (a, b) -> a));
        Map<Long, List<TradeItem>> mktId2items = skus.stream().filter(item -> mktId2mkt.containsKey(item.getMarketingId())).collect(Collectors.groupingBy(TradeItem::getMarketingId));

        // 1.构建营销插件请求对象
        List<TradeMarketingWrapperDTO> pluginParam = new ArrayList<>();
        for (Map.Entry<Long, List<TradeItem>> entry : mktId2items.entrySet()) {
            List<TradeItemInfoDTO> tradeItems = entry.getValue().stream().map(
                    t -> TradeItemInfoDTO.builder()
                            .num(t.getNum())
                            .price(t.getPrice())
                            .skuId(t.getSkuId())
                            .storeId(t.getStoreId())
                            .goodsType(t.getGoodsType())
                            .distributionGoodsAudit(t.getDistributionGoodsAudit())
                            .build()).collect(Collectors.toList());
            pluginParam.add(TradeMarketingWrapperDTO.builder().tradeMarketingDTO(mktId2mkt.get(entry.getKey())).tradeItems(tradeItems).build());
        }
        // 2.调用营销插件，并设置满系营销信息
        MarketingTradeBatchWrapperRequest wrapperParam = MarketingTradeBatchWrapperRequest.builder().wraperDTOList(pluginParam).build();
        List<TradeMarketingWrapperVO> wrapperList = marketingTradePluginProvider.batchWrapper(wrapperParam).getContext().getWraperVOList();

        List<TradeMarketingVO> tradeMarketings = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(wrapperList)) {
            wrapperList.forEach(tradeMarketingWrapperVO -> tradeMarketings.add(tradeMarketingWrapperVO.getTradeMarketing()));
        }
        return tradeMarketings;
    }

    /**
     * 计算订单价格
     * 订单价格 = 商品总价 - 营销优惠总金额
     */
    private TradePrice calc(List<TradeItem> tradeItems, List<TradeMarketingVO> tradeMarketings, TradeCouponVO tradeCoupon) {
        final TradePrice tradePrice = new TradePrice();

        // 1.计算商品总价
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
                tradePrice.setBuyPoints(Objects.isNull(tradePrice.getBuyPoints()) ? t.getBuyPoint() * t.getNum() : tradePrice.getBuyPoints() + t.getBuyPoint() * t.getNum());
            }
        });

        List<TradeMarketingVO> mktList = tradeMarketings.stream().filter(
                i -> !MarketingType.GIFT.equals(i.getMarketingType()) && !MarketingType.MARKUP.equals(i.getMarketingType())).collect(Collectors.toList());

        // 2.计算所有营销活动的总优惠金额(非满赠)
        BigDecimal discountPrice = mktList.stream().map(TradeMarketingVO::getDiscountsAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //营销活动优惠总额
        tradePrice.setMarketingDiscountPrice(discountPrice);

        // 3.计算各类营销活动的优惠金额(比如:满折优惠xxx,满减优惠yyy)
        List<DiscountsPriceDetail> discountsPriceDetails = new ArrayList<>();
        mktList.stream().collect(Collectors.groupingBy(TradeMarketingVO::getMarketingType)).forEach((key, value) -> {
            DiscountsPriceDetail detail = DiscountsPriceDetail.builder()
                    .marketingType(key)
                    .discounts(value.stream().map(TradeMarketingVO::getDiscountsAmount).reduce(BigDecimal.ZERO, BigDecimal::add)).build();
            discountsPriceDetails.add(detail);
        });
        tradePrice.setDiscountsPriceDetails(discountsPriceDetails);

        // 4.设置优惠券优惠金额
        if (tradeCoupon != null) {
            tradePrice.setCouponPrice(tradeCoupon.getDiscountsAmount());
            discountPrice = discountPrice.add(tradeCoupon.getDiscountsAmount());
        }

        // 5.设置优惠总金额、应付金额 = 商品总金额 - 总优惠金额
        tradePrice.setDiscountsPrice(discountPrice);
        tradePrice.setTotalPrice(tradePrice.getTotalPrice().subtract(discountPrice));
        return tradePrice;
    }
}