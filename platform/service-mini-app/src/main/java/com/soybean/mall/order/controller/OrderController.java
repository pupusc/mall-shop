package com.soybean.mall.order.controller;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsListDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.bean.vo.TradeItemMarketingVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.request.TradeItemConfirmRequest;
import com.wanmi.sbc.order.response.TradeConfirmResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CommonUtil commonUtil;

    Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    /**
     * 用于确认订单后，创建订单前的获取订单商品信息
     */
    @ApiOperation(value = "用于确认订单后，创建订单前的获取订单商品信息")
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse<TradeConfirmResponse> getPurchaseItems(@RequestBody TradeItemConfirmRequest request) {

        TradeConfirmResponse confirmResponse = new TradeConfirmResponse();
        String customerId = commonUtil.getOperatorId();
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        //根据商品信息，
        List<TradeItemGroupVO> tradeItemGroups = new ArrayList<>();

        List<TradeConfirmItemVO> items = new ArrayList<>();
        List<String> skuIds = tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream())
                .map(TradeItemVO::getSkuId).collect(Collectors.toList());
        //获取订单商品详情,包含区间价，会员级别价salePrice
        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);
        Map<String, Integer> cpsMap = skuResp.getGoodses().stream().filter(good -> good.getCpsSpecial() != null).collect(Collectors.toMap(GoodsVO::getGoodsId, GoodsVO::getCpsSpecial));
        // 营销活动赠品
        List<String> giftIds =
                tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
                        .filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds())).flatMap(r -> r.getGiftSkuIds()
                        .stream()).distinct().collect(Collectors.toList());
        giftIds.addAll(tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
                .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds())).flatMap(r -> r.getMarkupSkuIds()
                        .stream()).distinct().collect(Collectors.toList()));
        Map<Long, StoreVO> storeMap = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest
                (tradeItemGroups.stream().map(g -> g
                        .getSupplier().getStoreId())
                        .collect(Collectors.toList()))).getContext().getStoreVOList().stream().collect(Collectors
                .toMap(StoreVO::getStoreId,
                        s -> s));
        TradeGetGoodsResponse giftResp;
        giftResp = tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(giftIds).build()).getContext();
        final TradeGetGoodsResponse giftTemp = giftResp;
        // 组合购标记
        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE,
                s.getSuitMarketingFlag()));
        Integer suitScene = null;
        if(suitMarketingFlag && tradeItemGroups.stream().anyMatch(s -> s.getSuitScene()!=null)){
            suitScene = tradeItemGroups.stream().filter(s -> s.getSuitScene()!=null).findFirst().get().getSuitScene();
        }
        //拼团标记
        boolean grouponFlag = tradeItemGroups.stream().anyMatch(s -> s.getGrouponForm() != null && s.getGrouponForm().getOpenGroupon() != null);
        confirmResponse.setSuitMarketingFlag(suitMarketingFlag);
        confirmResponse.setSuitScene(suitScene);
        // 如果为PC商城下单or组合购商品，将分销商品变为普通商品
        if (ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType()) || suitMarketingFlag || grouponFlag) {
            tradeItemGroups.forEach(tradeItemGroup ->
                    tradeItemGroup.getTradeItems().forEach(tradeItem -> {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                        if (suitMarketingFlag) {
                            tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
                        }
                    })
            );
            skuResp.getGoodsInfos().forEach(item -> {
                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                if (suitMarketingFlag) {
                    item.setBuyPoint(NumberUtils.LONG_ZERO);
                }
            });
        }

        //企业会员判断
        boolean isIepCustomerFlag = isIepCustomer(customer);
        boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();

        Map<String, Long> buyCountMap = new HashMap<>();
        Map<String, BigDecimal> enterprisePriceMap = new HashMap<>();
        List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();
        if (isIepCustomerFlag) {
            tradeItemGroups.forEach(e -> {
                List<TradeItemVO> tradeItems = e.getTradeItems();
                tradeItems.forEach(item -> {
                    buyCountMap.put(item.getSkuId(), item.getNum());
                });
            });
            //企业价
            EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
            enterprisePriceGetRequest.setGoodsInfoIds(skuIds);
            enterprisePriceGetRequest.setCustomerId(customer.getCustomerId());
            enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
            enterprisePriceGetRequest.setListFlag(false);
            enterprisePriceGetRequest.setOrderFlag(true);

            EnterprisePriceResponse context = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();
            Map<String, BigDecimal> priceMap = context.getPriceMap();
            if (priceMap != null) {
                enterprisePriceMap.putAll(priceMap);
            }

            //会员等级价
            goodsLevelPrices.addAll(this.getGoodsLevelPrices(skuIds, customer));
        }

        //分销开关
        boolean distributionPcMall = DefaultFlag.YES.equals(distributionCacheService.queryOpenFlag())
                && !ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType());
        //商品验证并填充商品价格
        List<Long> flashSaleGoodsIds = new ArrayList<>();
        tradeItemGroups.forEach(
                g -> {
                    //周期购使用单品运费
                    DefaultFlag freightTemplateType = Objects.nonNull(g.getCycleBuyInfo())
                            ? DefaultFlag.YES
                            : storeMap.get(g.getSupplier().getStoreId()).getFreightTemplateType();
                    g.getSupplier().setFreightTemplateType(freightTemplateType);
                    List<TradeItemVO> tradeItems = g.getTradeItems();
                    List<TradeItemDTO> tradeItemDTOList = KsBeanUtil.convert(tradeItems, TradeItemDTO.class);
                    // 分销商品、开店礼包商品、拼团商品、企业购商品，不验证起限定量
                    skuResp.getGoodsInfos().forEach(item -> {
                        //企业购商品
                        boolean isIep = isIepCustomerFlag && isEnjoyIepGoodsInfo(item.getEnterPriseAuditState());
                        if (DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                                || DefaultFlag.YES.equals(g.getStoreBagsFlag())
                                || Objects.nonNull(g.getGrouponForm()) || isIep) {
                            item.setCount(null);
                            item.setMaxCount(null);
                        }
                    });
                    //商品验证并填充商品价格
                    List<TradeItemVO> tradeItemVOList =
                            verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItemDTOList, Collections
                                    .emptyList(),
                                    KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class),
                                    g.getSupplier().getStoreId(), true)).getContext().getTradeItems();
                    tradeItemVOList.stream().forEach(
                            tradeItemVO -> {
                                tradeItemVO.setCpsSpecial(cpsMap.get(tradeItemVO.getSpuId()));
                            }
                    );
                    //企业购商品价格回设
                    if (isIepCustomerFlag) {
                        tradeItemVOList.forEach(tradeItemVO -> {
                            if (isEnjoyIepGoodsInfo(tradeItemVO.getEnterPriseAuditState())
                                    && (!isGoodsPoint || (isGoodsPoint && (tradeItemVO.getBuyPoint() == null || tradeItemVO.getBuyPoint() == 0)))) {
                                tradeItemVO.setPrice(tradeItemVO.getEnterPrisePrice());

                                BigDecimal price = enterprisePriceMap.get(tradeItemVO.getSkuId());
                                if (price != null) {
                                    tradeItemVO.setPrice(price);
                                }
                                tradeItemVO.setSplitPrice(tradeItemVO.getEnterPrisePrice().multiply(new BigDecimal(tradeItemVO.getNum())));
                                tradeItemVO.setLevelPrice(tradeItemVO.getEnterPrisePrice());
                                //判断当前用户对应企业购商品等级企业价
                                if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(tradeItemVO.getSkuId()))
                                            .findFirst();
                                    tradeItemVO.setPrice(first.isPresent() ? first.get().getPrice() : tradeItemVO.getEnterPrisePrice());
                                }
                            }
                        });
                    }
                    //抢购商品价格回设
                    if (StringUtils.isNotBlank(g.getSnapshotType()) && (g.getSnapshotType().equals(Constants.FLASH_SALE_GOODS_ORDER_TYPE))) {
                        tradeItemVOList.forEach(tradeItemVO -> {
                            g.getTradeItems().forEach(tradeItem -> {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                }
                            });
                        });
                        if (CollectionUtils.isNotEmpty(tradeItemVOList)) {
                            flashSaleGoodsIds.add(tradeItemVOList.get(0).getFlashSaleGoodsId());
                        }
                    }
                    tradeItemVOList.forEach(tradeItemVO -> {

                        g.getTradeItems().forEach(tradeItem -> {
                            g.getTradeMarketingList().forEach(tradeItemMarketingVO -> {
                                if (tradeItemMarketingVO.getSkuIds().contains(tradeItemVO.getSkuId())
                                        && !tradeItemVO.getMarketingIds().contains(tradeItemMarketingVO.getMarketingId())) {
                                    tradeItemVO.getMarketingIds().add(tradeItemMarketingVO.getMarketingId());
                                    tradeItemVO.getMarketingLevelIds().add(tradeItemMarketingVO.getMarketingLevelId());
                                }
                            });
                            if ((Objects.nonNull(tradeItem.getIsAppointmentSaleGoods()) && tradeItem.getIsAppointmentSaleGoods())) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                    tradeItemVO.setIsAppointmentSaleGoods(tradeItem.getIsAppointmentSaleGoods());
                                    tradeItemVO.setAppointmentSaleId(tradeItem.getAppointmentSaleId());
                                }
                            }
                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.FULL_MONEY) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
                                }
                            }
                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.EARNEST_MONEY) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getOriginalPrice());
                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
                                    tradeItemVO.setHandSelStartTime(tradeItem.getHandSelStartTime());
                                    tradeItemVO.setHandSelEndTime(tradeItem.getHandSelEndTime());
                                    tradeItemVO.setTailStartTime(tradeItem.getTailStartTime());
                                    tradeItemVO.setTailEndTime(tradeItem.getTailEndTime());
                                    tradeItemVO.setEarnestPrice(tradeItem.getEarnestPrice());
                                    tradeItemVO.setSwellPrice(tradeItem.getSwellPrice());
                                }
                            }
                        });
                    });
                    g.setTradeItems(tradeItemVOList);
                    // 分销商品、开店礼包商品，重新设回市场价
                    if (distributionPcMall) {
                        g.getTradeItems().stream().filter(tradeItemVO -> DefaultFlag.YES.equals(g.getStoreBagsFlag())
                                || (Objects.isNull(tradeItemVO.getBuyPoint()) || tradeItemVO.getBuyPoint() == 0)).forEach(item -> {
                            DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(item.getStoreId()
                                    .toString());
                            if ((Objects.isNull(item.getIsFlashSaleGoods()) ||
                                    (Objects.nonNull(item.getIsFlashSaleGoods()) && !item.getIsFlashSaleGoods())) &&
                                    (Objects.isNull(item.getIsAppointmentSaleGoods()) || !item.getIsAppointmentSaleGoods()) &&
                                    !(Objects.nonNull(item.getIsBookingSaleGoods()) && item.getIsBookingSaleGoods() && item.getBookingType() == BookingType.FULL_MONEY) &&
                                    DefaultFlag.YES.equals(storeOpenFlag) && (
                                    DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                                            || DefaultFlag.YES.equals(g.getStoreBagsFlag()))) {
                                if (null == item.getOriginalPrice()) {
                                    item.setOriginalPrice(BigDecimal.ZERO);
                                }
                                item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
                                item.setPrice(item.getOriginalPrice());
                                item.setLevelPrice(item.getOriginalPrice());
                                if (DefaultFlag.YES.equals(g.getStoreBagsFlag())) {
                                    item.setBuyPoint(NumberUtils.LONG_ZERO);
                                }
                            } else {
                                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                            }
                        });
                    }
                    confirmResponse.setStoreBagsFlag(g.getStoreBagsFlag());
                }
        );

        //秒杀包邮
        if (CollectionUtils.isNotEmpty(flashSaleGoodsIds)) {
            FlashSaleGoodsListRequest flashSaleGoodsListRequest = new FlashSaleGoodsListRequest();
            flashSaleGoodsListRequest.setIdList(flashSaleGoodsIds);
            List<FlashSaleGoodsVO> flashSaleGoodsVOList = flashSaleGoodsQueryProvider.list(flashSaleGoodsListRequest).getContext().getFlashSaleGoodsVOList();
            if (CollectionUtils.isNotEmpty(flashSaleGoodsVOList)) {
                Boolean flashFreeDelivery = Boolean.FALSE;
                for (FlashSaleGoodsVO flashSaleGoodsVO : flashSaleGoodsVOList) {
                    flashFreeDelivery = flashSaleGoodsVO.getPostage().equals(NumberUtils.INTEGER_ONE);
                }
                confirmResponse.setFlashFreeDelivery(flashFreeDelivery);
            }
        }

        List<TradeItemMarketingVO> tradeMarketingList = tradeItemGroups.stream()
                .flatMap(tradeItemGroupVO -> tradeItemGroupVO.getTradeMarketingList().stream())
                .collect(Collectors.toList());
        // 加价购商品信息填充
        List<Long> marketingIds =
                tradeMarketingList.parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                        .map(TradeItemMarketingVO::getMarketingId).distinct().collect(Collectors.toList());

        MarkupListRequest markupListRequest = MarkupListRequest.builder().marketingId(marketingIds).build();
        List<MarkupLevelVO> levelList = markupQueryProvider.getMarkupList(markupListRequest).getContext().getLevelList();

        //赠品信息填充
        List<String> giftItemIds =
                tradeMarketingList.parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds()))
                        .flatMap(r -> r.getGiftSkuIds().stream()).distinct().collect(Collectors.toList());
        List<TradeItemDTO> gifts =
                giftItemIds.stream().map(i -> TradeItemDTO.builder().price(BigDecimal.ZERO)
                        .skuId(i)
                        .build()).collect(Collectors.toList());

        List<TradeItemVO> giftVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(gifts
                , KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class)))
                .getContext().getTradeItems();
        gifts = KsBeanUtil.convert(giftVoList, TradeItemDTO.class);
        List<TradeItemDTO> markupItem = new ArrayList<>();
        for (TradeItemGroupVO g : tradeItemGroups) {
            Map<String, List<MarkupLevelVO>> listMap = levelList.stream().collect(Collectors.groupingBy(l -> "" + l.getMarkupId() + l.getId()));
            // 通过 加价购id 阶梯id 加购商品sku定位 换购价格
            List<TradeItemDTO> finalMarkupItem = markupItem;
            g.getTradeMarketingList().parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                    .forEach(m -> {
                        List<MarkupLevelVO> markupLevelVOS = listMap.get("" + m.getMarketingId() + m.getMarketingLevelId());
                        if (CollectionUtils.isNotEmpty(markupLevelVOS)) {
                            m.getMarkupSkuIds().stream().forEach(sku -> {
                                markupLevelVOS.stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
                                        .filter(detailVO -> sku.equals(detailVO.getGoodsInfoId()))
                                        .forEach(detail -> {
                                            finalMarkupItem.add(TradeItemDTO.builder()
                                                    .price(detail.getMarkupPrice())
                                                    .skuId(detail.getGoodsInfoId())
                                                    .num(NumberUtils.LONG_ONE)
                                                    .isMarkupGoods(Boolean.TRUE)
                                                    .build());
                                        });
                            });

                        }
                    });

            List<TradeItemVO> markupVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(markupItem
                    , KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class)))
                    .getContext().getTradeItems();
            // 换购商品阶梯价不受其他影响
            markupVoList.forEach(m -> {
                m.setSplitPrice(m.getPrice());
                m.setLevelPrice(m.getPrice());
                m.setOriginalPrice(m.getPrice());
            });
            markupItem = KsBeanUtil.convert(markupVoList, TradeItemDTO.class);
            items.add(tradeQueryProvider.queryPurchaseInfo(TradeQueryPurchaseInfoRequest.builder()
                    .tradeItemGroupDTO(KsBeanUtil.convert(g, TradeItemGroupDTO.class))
                    .tradeItemList(gifts).markupItemList(markupItem).build()).getContext().getTradeConfirmItemVO());
        }
        this.setGoodsStatus(items);

        // 验证小店商品
        this.validShopGoods(tradeItemGroups, commonUtil.getDistributeChannel());
        //设置购买总积分
        confirmResponse.setTotalBuyPoint(items.stream().flatMap(i -> i.getTradeItems().stream())
                .filter(i -> Objects.isNull(i.getIsMarkupGoods()) || !i.getIsMarkupGoods()).mapToLong(v -> Objects.isNull(v.getBuyPoint()) ? 0 : v.getBuyPoint() * v.getNum()).sum());

        confirmResponse.setTradeConfirmItems(items);

        BigDecimal totalCommission = dealDistribution(tradeItemGroups, confirmResponse);

        // 设置小店名称、返利总价
        confirmResponse.setShopName(distributionCacheService.getShopName());

        confirmResponse.setTotalCommission(totalCommission);

        // 设置邀请人名字
        if (StringUtils.isNotEmpty(commonUtil.getDistributeChannel().getInviteeId())) {
            DistributionCustomerByCustomerIdRequest request = new DistributionCustomerByCustomerIdRequest();
            request.setCustomerId(commonUtil.getDistributeChannel().getInviteeId());
            DistributionCustomerVO distributionCustomer = distributionCustomerQueryProvider.getByCustomerId(request)
                    .getContext().getDistributionCustomerVO();
            if (distributionCustomer != null) {
                confirmResponse.setInviteeName(distributionCustomer.getCustomerName());
            }
        }

        // 校验拼团信息
        validGrouponOrder(confirmResponse, tradeItemGroups, customerId);
        // 校验组合购活动信息
        dealSuitOrder(confirmResponse, tradeItemGroups, customerId);

        // 填充立即购买的满系营销信息
        fillMarketingInfo(confirmResponse, tradeItemGroups);

        //填充周期购信息
        fillCycleBuyInfoToConfirmResponse(confirmResponse, tradeItemGroups);

        // 根据确认订单计算出的信息，查询出使用优惠券页需要的优惠券列表数据
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        List<TradeItemInfoDTO> tradeDtos = items.stream().flatMap(confirmItem ->
                confirmItem.getTradeItems().stream().map(tradeItem -> {
                    TradeItemInfoDTO dto = new TradeItemInfoDTO();
                    dto.setBrandId(tradeItem.getBrand());
                    dto.setCateId(tradeItem.getCateId());
                    dto.setSpuId(tradeItem.getSpuId());
                    dto.setSkuId(tradeItem.getSkuId());
                    dto.setStoreId(confirmItem.getSupplier().getStoreId());
                    dto.setPrice(tradeItem.getSplitPrice());
                    dto.setDistributionGoodsAudit(tradeItem.getDistributionGoodsAudit());
                    if (DefaultFlag.NO.equals(openFlag) || DefaultFlag.NO.equals(
                            distributionCacheService.queryStoreOpenFlag(String.valueOf(tradeItem.getStoreId())))) {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    }
                    return dto;
                })
        ).collect(Collectors.toList());

        // 加价购填充
        markupForGoodsInfo(confirmResponse);

        CouponCodeListForUseByCustomerIdRequest requ = CouponCodeListForUseByCustomerIdRequest.builder()
                .customerId(customerId)
                .tradeItems(tradeDtos).price(confirmResponse.getTotalPrice()).build();

        confirmResponse.setCouponCodes(couponCodeQueryProvider.listForUseByCustomerId(requ).getContext()
                .getCouponCodeList());

        return BaseResponse.success(confirmResponse);
    }

    /**
     * 获取订单商品详情,包含区间价，会员级别价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {
        //性能优化，原来从order服务绕道，现在直接从goods服务直行
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        //计算区间价
        GoodsIntervalPriceByCustomerIdResponse priceResponse = goodsIntervalPriceService.getGoodsIntervalPriceVOList
                (response.getGoodsInfos(), customer.getCustomerId());
        response.setGoodsInfos(priceResponse.getGoodsInfoVOList());
        //获取客户的等级
        if (StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                    .goodsInfos(goodsInfoMapper.goodsInfoVOsToGoodsInfoDTOs(response.getGoodsInfos()))
                    .customerDTO(custmerMapper.customerVOToCustomerDTO(customer)).build())
                    .getContext().getGoodsInfoVOList();
            response.setGoodsInfos(goodsInfoVOList);
        }
        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .goodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList())
                .build();
    }

}
