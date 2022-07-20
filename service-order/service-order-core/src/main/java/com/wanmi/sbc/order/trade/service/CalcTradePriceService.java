package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
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
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.DiscountsPriceDetail;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.reponse.CalcPriceResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;
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
    public CalcPriceResult calc(List<TradeItem> tradeItems, String couponId, String customerId) {
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

        //4.查询商品原始信息
        GoodsInfoViewByIdsResponse skuResp = tradeCacheService.getGoodsInfoViewByIds(IteratorUtils.collectKey(tradeItems, TradeItem::getSkuId));
        Trade tradeParam = new Trade();
        tradeParam.setTradeItems(tradeItems);
        //查询商品下单信息（计算会员价）
        TradeGoodsListVO goodsResp = tradeGoodsService.getGoodsInfoResponse(tradeParam, customerVO, skuResp);
        Map<String, GoodsInfoVO> skuMap = goodsResp.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, i -> i));
        for (TradeItem item : tradeItems) {
            if (Objects.isNull(skuMap.get(item.getSkuId()))) {
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS, "sku信息没有查询到, skuId={}", item.getSkuId());
            }
            GoodsInfoVO sku = skuMap.get(item.getSkuId());
            item.setPrice(sku.getSalePrice());
            item.setLevelPrice(sku.getSalePrice());
            item.setOriginalPrice(Objects.isNull(sku.getMarketPrice()) ? BigDecimal.ZERO : sku.getMarketPrice());
            item.setSplitPrice(item.getPrice().multiply(new BigDecimal(item.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
            item.setPropPrice(getSkuPropPrice(sku.getGoodsInfoId()));
        }

        //按照营销活动分组
        Map<Long, List<TradeItem>> mktId2items = tradeItems.stream()
                .filter(i->i.getMarketingId() != null && i.getMarketingId() > 0)
                .collect(Collectors.groupingBy(TradeItem::getMarketingId));

        //对营销活动进行总数量和总价格统计
        Map<Long, Long> mktId2count = new HashMap<>();
        Map<Long, BigDecimal> mktId2Price = new HashMap<>();

        for (Map.Entry<Long, List<TradeItem>> entry : mktId2items.entrySet()) {
            mktId2count.put(entry.getKey(), entry.getValue().stream().mapToLong(TradeItem::getNum).sum());
            mktId2Price.put(entry.getKey(), entry.getValue().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getNum()))).reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        //所有需要计算商品的id
        List<String> skuIds = tradeItems.stream().map(item -> item.getSkuId()).distinct().collect(Collectors.toList());

        //根据skuId查询所有关联的营销活动
        MarketingMapGetByGoodsIdRequest mktParamm = MarketingMapGetByGoodsIdRequest.builder().goodsInfoIdList(skuIds)
                .marketingStatus(MarketingStatus.STARTED).deleteFlag(DeleteFlag.NO).cascadeLevel(true).build();
        BaseResponse<MarketingMapGetByGoodsIdResponse> mktResponse = marketingQueryProvider.getMarketingMapByGoodsId(mktParamm);

        //商品查询的全部营销活动
        Map<Long, MarketingForEndVO> mkt2all = new HashMap<>();
        if (mktResponse != null && mktResponse.getContext() != null && mktResponse.getContext().getListMap() != null) {
            mktResponse.getContext().getListMap().values().stream().flatMap(item->item.stream()).forEach(mkt->mkt2all.put(mkt.getMarketingId(), mkt));
        }
        //不满足的营销活动
        Set<Long> mkt2bad = new HashSet<>();
        //满足条件的营销活动
        Map<Long, TradeMarketingDTO> mkt2yes = new HashMap<>();
        for (TradeItem item : tradeItems) {
            Long mktId = item.getMarketingId();
            if (mktId == null || mktId == 0) {
                continue;
            }
            MarketingForEndVO mkt = mkt2all.get(mktId);
            if (mkt == null) {
                log.warn("营销活动不存在或者当前不可用, mktId={}", mktId);
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "指定的营销活动不存在或不可用");
            }
            if (mkt.getIsPause() == BoolFlag.YES || mkt.getDelFlag() == DeleteFlag.YES
                    || mkt.getBeginTime().isAfter(LocalDateTime.now()) || mkt.getEndTime().isBefore(LocalDateTime.now())) {
                log.warn("营销活动当前的状态信息不正确, mkt={}", JSON.toJSONString(mkt));
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "指定的营销活动当前不可用状态");
            }
            if (mkt2bad.contains(mktId)) {
                continue; //已经排除了那就跳过
            }
            if (mkt2yes.containsKey(mktId)) {
                mkt2yes.get(mktId).getSkuIds().add(item.getSkuId());
                continue;
            }
            Long levelId = getMktLevelId(mkt, mktId2count.getOrDefault(mkt.getMarketingId(), 0L), mktId2Price.getOrDefault(mkt.getMarketingId(), BigDecimal.ZERO));
            if (levelId == null) {
                mkt2bad.add(mktId); //不满足活动就排除
                continue;
            }
            TradeMarketingDTO dto = new TradeMarketingDTO();
            dto.setStoreId(mkt.getStoreId());
            dto.setMarketingId(mkt.getMarketingId());
            dto.setMarketingSubType(mkt.getSubType().toValue());
            dto.setSkuIds(new ArrayList<>(Arrays.asList(item.getSkuId())));
            dto.setMarketingLevelId(levelId);
            mkt2yes.put(mktId, dto);
        }
        List<TradeMarketingDTO> mktDTOs = mkt2yes.values().stream().collect(Collectors.toList());

        //        // 2.4.校验sku 和 【商品价格计算第①步】: 商品的 客户级别价格 (完成客户级别价格/客户指定价/订货区间价计算) -> levelPrice
//        verifyService.verifyGoods(tradeItems, Collections.EMPTY_LIST, skuList, null, true, null);

        // 1.构建订单满系营销对象
        List<TradeMarketingVO> mktVOs = this.wrapperMarketingForConfirm(tradeItems, mktDTOs);
        // 2.构建订单优惠券对象
        TradeCouponVO tradeCouponVO = StringUtils.isBlank(couponId) ? null : tradeMarketingService.buildTradeCouponInfo(tradeItems, couponId, false, customerId);
        //计算并赋值splitPrice
        calcMarketingPrice(tradeItems, mktVOs, tradeCouponVO);
        // 2.9.计算并设置订单总价(已减去营销优惠总金额)
        TradePrice tradePrice = calc(tradeItems, mktVOs, tradeCouponVO);

        CalcPriceResult result = new CalcPriceResult();
        result.setTradePrice(tradePrice);
        result.setTradeMkts(mktDTOs.stream().map(i -> {
            CalcPriceResult.TradeMkt tradeMkt = new CalcPriceResult.TradeMkt();
            tradeMkt.setMktId(i.getMarketingId());
            tradeMkt.setMktLevelId(i.getMarketingLevelId());
            return tradeMkt;
        }).collect(Collectors.toList()));
        return result;
    }

    private Long getMktLevelId(MarketingForEndVO mkt, long totalCount, BigDecimal totalPrice) {
        Long levelId = null;
        //满折
        if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mkt.getSubType()) || MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mkt.getSubType())) {
            List<MarketingFullDiscountLevelVO> levelList = mkt.getFullDiscountLevelList();
            levelList.sort(Comparator.comparing(MarketingFullDiscountLevelVO::getDiscount).reversed());

            for (MarketingFullDiscountLevelVO level : levelList) {
                if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mkt.getSubType()) ? totalCount >= level.getFullCount() : totalPrice.compareTo(level.getFullAmount()) >= 0) {
                    levelId = level.getDiscountLevelId();
                }
            }
            return levelId;
        }
        //满减
        if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mkt.getSubType()) || MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mkt.getSubType())) {
            List<MarketingFullReductionLevelVO> levelList = mkt.getFullReductionLevelList();
            levelList.sort(Comparator.comparing(MarketingFullReductionLevelVO::getReduction));

            for (MarketingFullReductionLevelVO level : levelList) {
                if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mkt.getSubType()) ? totalCount >= level.getFullCount() : totalPrice.compareTo(level.getFullAmount()) >= 0) {
                    levelId = level.getReductionLevelId();
                }
            }
            return levelId;
        }

        log.warn("其他营销类型暂不支持, mktId={}, subTpye={}", mkt.getMarketingId(), mkt.getSubType().toValue());
        throw new SbcRuntimeException("暂时只支持满减或满折类型的营销活动");
    }

    private Double getSkuPropPrice(String skuId) {
        if (StringUtils.isBlank(skuId)) {
            return 0D;
        }
        BaseResponse<String> priceResult = goodsIntervalPriceProvider.findPriceByGoodsId(skuId);
        if (priceResult == null || StringUtils.isBlank(priceResult.getContext())) {
            return 0D;
        }
        return Double.valueOf(priceResult.getContext());
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
    private List<TradeMarketingVO> wrapperMarketingForConfirm(List<TradeItem> items, List<TradeMarketingDTO> mkts) {
        if (CollectionUtils.isEmpty(mkts)) {
            return Collections.EMPTY_LIST;
        }

        Map<Long, TradeMarketingDTO> mktId2mkt = mkts.stream().collect(Collectors.toMap(TradeMarketingDTO::getMarketingId, item -> item, (a, b) -> a));
        Map<Long, List<TradeItem>> mktId2items = items.stream().filter(item -> mktId2mkt.containsKey(item.getMarketingId())).collect(Collectors.groupingBy(TradeItem::getMarketingId));

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
        tradePrice.setPropPrice(BigDecimal.ZERO);
        tradePrice.setSalePrice(BigDecimal.ZERO);
        for (TradeItem t : tradeItems) {
            BigDecimal num = BigDecimal.valueOf(t.getNum());
            BigDecimal buyItemPrice = t.getPrice().multiply(num);
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
            //商品定价
            if (Objects.nonNull(t.getPropPrice())) {
                tradePrice.setPropPrice(tradePrice.getPropPrice().add(BigDecimal.valueOf(t.getPropPrice()).multiply(num)));
            }
            //市场售价
            if (Objects.nonNull(t.getOriginalPrice())) {
                tradePrice.setSalePrice(tradePrice.getSalePrice().add(t.getOriginalPrice().multiply(num)));
            }
        }
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