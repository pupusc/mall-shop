package com.soybean.mall.order.service;
import com.soybean.mall.order.request.DiscountPriceReq;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.order.bean.vo.SupplierVO;

import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.order.response.TradeConfirmResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.store.ListNoDeleteStoreByIdsResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingViewQueryByIdsRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsValidRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelBySkuRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsValidResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelBySkuResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.MarketingBuyoutPriceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingHalfPriceSecondPieceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingPointBuyLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.order.api.provider.trade.TradeItemQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.MergeGoodsInfoRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetGoodsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeQueryPurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsListDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmMarketingVO;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.bean.vo.TradeItemMarketingVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/29 1:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class DiscountPriceService {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Resource
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Resource
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private TradeItemQueryProvider tradeItemQueryProvider;

    @Resource
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private MarkupQueryProvider markupQueryProvider;


    @Autowired
    private MarketingSuitsQueryProvider marketingSuitsQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private MarketingPluginQueryProvider marketingPluginQueryProvider;



    //copy原来凑单页面
    public TradeConfirmResp computePayPrice(DiscountPriceReq discountPriceReq, CustomerGetByIdResponse customer) {

        TradeConfirmResp confirmResponse = new TradeConfirmResp();
        //验证用户
//        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        List<String> skuIdList = discountPriceReq.getItems().stream().map(DiscountPriceReq.DiscountPriceSkuReq::getSkuId).collect(Collectors.toList());
        GoodsInfoResponse goodsResponse = this.getGoodsResponse(skuIdList, customer);
        Long storeId = goodsResponse.getGoodsInfos().get(0).getStoreId();
        StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext().getStoreVO();
        DefaultFlag freightTemplateType = storeVO.getFreightTemplateType();
        SupplierVO supplier = SupplierVO.builder()
                .storeId(storeVO.getStoreId())
                .storeName(storeVO.getStoreName())
                .isSelf(storeVO.getCompanyType() == BoolFlag.NO)
                .supplierCode(storeVO.getCompanyInfo().getCompanyCode())
                .supplierId(storeVO.getCompanyInfo().getCompanyInfoId())
                .supplierName(storeVO.getCompanyInfo().getSupplierName())
                .freightTemplateType(freightTemplateType)
                .build();

        List<TradeItemGroupVO> tradeItemGroups = new ArrayList<>();
        TradeItemGroupVO tradeItemGroupVO = new TradeItemGroupVO();
        tradeItemGroupVO.setTradeItems(KsBeanUtil.convert(discountPriceReq.getItems(), TradeItemVO.class));
        tradeItemGroupVO.setSupplier(supplier);
        tradeItemGroupVO.setTradeMarketingList(KsBeanUtil.convert(discountPriceReq.getMarketings(), TradeItemMarketingVO.class));

        tradeItemGroups.add(tradeItemGroupVO);

        List<TradeConfirmItemVO> items = new ArrayList<>();
        List<String> skuIds =  tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream())
                .map(TradeItemVO::getSkuId).collect(Collectors.toList());



//        //获取订单商品详情,包含区间价，会员级别价salePrice
//        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);

        // 营销活动赠品
        List<String> giftIds = tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
                        .filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds()))
                        .flatMap(r -> r.getGiftSkuIds().stream()).distinct()
                        .collect(Collectors.toList());
        giftIds.addAll(tradeItemGroups.stream()
                        .flatMap(i -> i.getTradeMarketingList().stream())
                        .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                        .flatMap(r -> r.getMarkupSkuIds().stream())
                        .distinct().collect(Collectors.toList()));
        ListNoDeleteStoreByIdsRequest listNoDeleteStoreByIdsRequest = new ListNoDeleteStoreByIdsRequest();
        List<Long> storeIdList = new ArrayList<>();
        for (TradeItemGroupVO tradeItemGroup : tradeItemGroups) {
            storeIdList.add(tradeItemGroup.getSupplier().getStoreId());
        }
        listNoDeleteStoreByIdsRequest.setStoreIds(storeIdList);
        ListNoDeleteStoreByIdsResponse listNoDeleteStoreByIdsResponse = storeQueryProvider.listNoDeleteStoreByIds(listNoDeleteStoreByIdsRequest).getContext();
        Map<Long, StoreVO> storeMap = listNoDeleteStoreByIdsResponse.getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId,s -> s));
        TradeGetGoodsResponse giftTemp = tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(giftIds).build()).getContext();


        for (TradeItemGroupVO g : tradeItemGroups) {
            //周期购使用单品运费
            DefaultFlag freightTemplateTypeTmp = Objects.nonNull(g.getCycleBuyInfo()) ? DefaultFlag.YES : storeMap.get(g.getSupplier().getStoreId()).getFreightTemplateType();
            g.getSupplier().setFreightTemplateType(freightTemplateTypeTmp);
            List<TradeItemVO> tradeItems = g.getTradeItems();
            List<TradeItemDTO> tradeItemDTOList = KsBeanUtil.convert(tradeItems, TradeItemDTO.class);

            //商品验证并填充商品价格
            VerifyGoodsRequest verifyGoodsRequest =
                    new VerifyGoodsRequest(tradeItemDTOList, Collections.emptyList(), KsBeanUtil.convert(goodsResponse, TradeGoodsInfoPageDTO.class), g.getSupplier().getStoreId(), true);
            List<TradeItemVO> tradeItemVOList = verifyQueryProvider.verifyGoods(verifyGoodsRequest).getContext().getTradeItems();

            for (TradeItemVO tradeItemVO : tradeItemVOList) {
                for (TradeItemVO tradeItem : g.getTradeItems()) {
                    for (TradeItemMarketingVO tradeItemMarketingVO : g.getTradeMarketingList()) {
                        if (tradeItemMarketingVO.getSkuIds().contains(tradeItemVO.getSkuId())
                                && !tradeItemVO.getMarketingIds().contains(tradeItemMarketingVO.getMarketingId())) {
                            tradeItemVO.getMarketingIds().add(tradeItemMarketingVO.getMarketingId());
                            tradeItemVO.getMarketingLevelIds().add(tradeItemMarketingVO.getMarketingLevelId());
                        }
                    }

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
                }
            }
            g.setTradeItems(tradeItemVOList);
        }


        List<TradeItemMarketingVO> tradeMarketingList =
                tradeItemGroups.stream()
                .flatMap(tradeItemGroupVOParam -> tradeItemGroupVOParam.getTradeMarketingList().stream())
                .collect(Collectors.toList());

        // 加价购商品信息填充
        List<Long> marketingIds = tradeMarketingList
                        .parallelStream()
                        .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                        .map(TradeItemMarketingVO::getMarketingId)
                        .distinct()
                        .collect(Collectors.toList());

        MarkupListRequest markupListRequest = MarkupListRequest.builder().marketingId(marketingIds).build();
        List<MarkupLevelVO> levelList = markupQueryProvider.getMarkupList(markupListRequest).getContext().getLevelList();

        //赠品信息填充
        List<String> giftItemIds = tradeMarketingList
                        .parallelStream()
                        .filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds()))
                        .flatMap(r -> r.getGiftSkuIds().stream())
                        .distinct()
                        .collect(Collectors.toList());
        List<TradeItemDTO> gifts = giftItemIds
                        .stream()
                        .map(i -> TradeItemDTO.builder().price(BigDecimal.ZERO)
                        .skuId(i)
                        .build())
                        .collect(Collectors.toList());
        MergeGoodsInfoRequest mergeGoodsInfoRequest = new MergeGoodsInfoRequest(gifts, KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class));
        List<TradeItemVO> giftVoList = verifyQueryProvider.mergeGoodsInfo(mergeGoodsInfoRequest).getContext().getTradeItems();
        gifts = KsBeanUtil.convert(giftVoList, TradeItemDTO.class);

        List<TradeItemDTO> markupItem = new ArrayList<>();
        for (TradeItemGroupVO g : tradeItemGroups) {
            Map<String, List<MarkupLevelVO>> listMap = levelList.stream().collect(Collectors.groupingBy(l -> "" + l.getMarkupId() + l.getId()));
            // 通过 加价购id 阶梯id 加购商品sku定位 换购价格
            List<TradeItemDTO> finalMarkupItem = markupItem;
            List<TradeItemMarketingVO> tradeItemMarketingVOList = g.getTradeMarketingList()
                                            .parallelStream()
                                            .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                                            .collect(Collectors.toList());
            for (TradeItemMarketingVO m : tradeItemMarketingVOList) {
                List<MarkupLevelVO> markupLevelVOS = listMap.get("" + m.getMarketingId() + m.getMarketingLevelId());
                if (CollectionUtils.isNotEmpty(markupLevelVOS)) {
                    for (String sku : m.getMarkupSkuIds()) {
                        List<MarkupLevelDetailVO> markupLevelDetailVOList = markupLevelVOS
                                        .stream()
                                        .flatMap(l -> l.getMarkupLevelDetailList().stream())
                                        .filter(detailVO -> sku.equals(detailVO.getGoodsInfoId()))
                                        .collect(Collectors.toList());
                        for (MarkupLevelDetailVO detailVO : markupLevelDetailVOList) {
                            TradeItemDTO tradeItemDTO = new TradeItemDTO();
                            tradeItemDTO.setPrice(detailVO.getMarkupPrice());
                            tradeItemDTO.setSkuId(detailVO.getGoodsInfoId());
                            tradeItemDTO.setNum(NumberUtils.LONG_ONE);
                            tradeItemDTO.setIsMarkupGoods(Boolean.TRUE);
                            finalMarkupItem.add(tradeItemDTO);
                        }
                    }
                }
            }

            g.getTradeMarketingList()
                    .parallelStream()
                    .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
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

        if (CollectionUtils.isNotEmpty(items)) {
            for (TradeConfirmItemVO item : items) {
                List<TradeItemVO> tmpGifts = item.getGifts();
                if (CollectionUtils.isNotEmpty(tmpGifts)) {
                    Map<String, GoodsStatus> statusMap = new HashMap<>();
                    List<String> tmpSkuIds = tmpGifts.stream().map(TradeItemVO::getSkuId).collect(Collectors.toList());
                    List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(tmpSkuIds).build())
                            .getContext().getGoodsInfos();
                    if (CollectionUtils.isNotEmpty(goodsInfos)) {
                        statusMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g.getGoodsStatus(), (k1, k2) -> k1));
                        for (TradeItemVO gift : tmpGifts) {
                            gift.setGoodsStatus(statusMap.get(gift.getSkuId()));
                        }
                    }
                }
            }
        }

        confirmResponse.setTradeConfirmItems(items);

        // 校验组合购活动信息
        dealSuitOrder(confirmResponse, tradeItemGroups);

        // 填充立即购买的满系营销信息
        fillMarketingInfo(confirmResponse, tradeItemGroups);


        // 加价购填充
        markupForGoodsInfo(confirmResponse, customer);

        return confirmResponse;
    }

    /**
     * 获取订单商品详情,包含区间价，会员级别价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {

        GoodsInfoResponse skuResp = new GoodsInfoResponse();

        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIds);
        goodsInfoViewByIdsRequest.setIsHavSpecText(Constants.yes);
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoViewByIdsRequest).getContext();


        skuResp.setGoodsInfos(response.getGoodsInfos());
        //获取客户的等级
        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算区间价
            List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class);
            GoodsIntervalPriceByCustomerIdRequest goodsIntervalPriceByCustomerIdRequest = new GoodsIntervalPriceByCustomerIdRequest();
            goodsIntervalPriceByCustomerIdRequest.setCustomerId(customer.getCustomerId());
            goodsIntervalPriceByCustomerIdRequest.setGoodsInfoDTOList(goodsInfoDTOList);
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceProvider.putByCustomerId(goodsIntervalPriceByCustomerIdRequest).getContext();
            skuResp.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
//            List<TradeItemDTO> tradeItemDTOList = KsBeanUtil.convert(response.getGoodsInfos(), TradeItemDTO.class);
            skuResp.setGoodsInfos(priceResponse.getGoodsInfoVOList());

            //计算会员价
            MarketingLevelGoodsListFilterRequest marketingLevelGoodsListFilterRequest = new MarketingLevelGoodsListFilterRequest();
            marketingLevelGoodsListFilterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class));
            marketingLevelGoodsListFilterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(marketingLevelGoodsListFilterRequest).getContext().getGoodsInfoVOList();
            skuResp.setGoodsInfos(goodsInfoVOList);
        }

        skuResp.setGoodses(response.getGoodses());
        return skuResp;
    }

    /**
     * 填充 拼团信息
     * @param response
     * @param tradeItemGroups
     * @param customerId
     */
//    private void validGrouponOrder(TradeConfirmResponse response, List<TradeItemGroupVO> tradeItemGroups, String customerId) {
//        TradeItemGroupVO tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
//        TradeItemVO item = tradeItemGroup.getTradeItems().get(NumberUtils.INTEGER_ZERO);
//        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
//        TradeItemVO resItem = confirmItem.getTradeItems().get(NumberUtils.INTEGER_ZERO);
//        if (Objects.nonNull(tradeItemGroup.getGrouponForm())) {
//
//            TradeGrouponCommitFormVO grouponForm = tradeItemGroup.getGrouponForm();
//
//            if (!DistributionGoodsAudit.COMMON_GOODS.equals(item.getDistributionGoodsAudit())) {
//                log.error("拼团单，miniapp 不能下分销商品");
//                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//            }
//
//            // 1.校验拼团商品
//            GrouponGoodsInfoVO grouponGoodsInfo = grouponProvider.validGrouponOrderBeforeCommit(
//                    GrouponOrderValidRequest.builder()
//                            .buyCount(item.getNum().intValue()).customerId(customerId).goodsId(item.getSpuId())
//                            .goodsInfoId(item.getSkuId())
//                            .grouponNo(grouponForm.getGrouponNo())
//                            .openGroupon(grouponForm.getOpenGroupon())
//                            .build()).getContext().getGrouponGoodsInfo();
//
//            if (Objects.isNull(grouponGoodsInfo)) {
//                log.error("拼团单下的不是拼团商品");
//                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//            }
//
//            // 2.设置拼团活动信息
//            boolean freeDelivery = grouponActivityQueryProvider.getFreeDeliveryById(
//                    new GrouponActivityFreeDeliveryByIdRequest(grouponGoodsInfo.getGrouponActivityId())).getContext().isFreeDelivery();
//
//
//            // 3.设成拼团价
//            BigDecimal grouponPrice = grouponGoodsInfo.getGrouponPrice();
//            BigDecimal splitPrice = grouponPrice.multiply(new BigDecimal(item.getNum()));
//            resItem.setSplitPrice(splitPrice);
//            resItem.setPrice(grouponPrice);
//            resItem.setLevelPrice(grouponPrice);
//            resItem.setBuyPoint(NumberUtils.LONG_ZERO);
//            TradePriceVO tradePrice = confirmItem.getTradePrice();
//            tradePrice.setGoodsPrice(splitPrice);
//            tradePrice.setTotalPrice(splitPrice);
//            tradePrice.setBuyPoints(NumberUtils.LONG_ZERO);
//
//            response.setOpenGroupon(grouponForm.getOpenGroupon());
//            response.setGrouponFreeDelivery(freeDelivery);
//            response.setTotalBuyPoint(NumberUtils.LONG_ZERO);
//        }
//    }


    /*
     *  组合购活动信息校验\处理tradeItem
     * @param response
     * @param tradeItemGroups
     * @param customerId
     */
    private void dealSuitOrder(
            TradeConfirmResp response, List<TradeItemGroupVO> tradeItemGroups) {
        TradeItemGroupVO tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
        // 组合购标记
        if (Objects.equals(tradeItemGroup.getSuitMarketingFlag(), Boolean.TRUE) && CollectionUtils.isNotEmpty(tradeItemGroup.getTradeMarketingList())) {
            // 获取并校验组合购活动信息
            MarketingSuitsValidRequest marketingSuitsValidRequest = new MarketingSuitsValidRequest();
            marketingSuitsValidRequest.setMarketingId(tradeItemGroup.getTradeMarketingList().get(NumberUtils.INTEGER_ZERO).getMarketingId());
            marketingSuitsValidRequest.setBaseStoreId(Constants.BOSS_DEFAULT_STORE_ID);
            marketingSuitsValidRequest.setUserId(commonUtil.getOperatorId());
            BaseResponse<MarketingSuitsValidResponse> marketingSuits = marketingSuitsQueryProvider.validSuitOrderBeforeCommit(marketingSuitsValidRequest);
            List<MarketingSuitsSkuVO> marketingSuitsSkuVOList = marketingSuits.getContext().getMarketingSuitsSkuVOList();

            confirmItem.getTradeItems().stream().filter(i -> !Boolean.TRUE.equals(i.getIsMarkupGoods())).forEach(tradeItemVO -> {
                MarketingSuitsSkuVO suitsSku = marketingSuitsSkuVOList.stream().filter(sku -> Objects.equals(sku.getSkuId(), tradeItemVO.getSkuId())
                ).findFirst().orElse(new MarketingSuitsSkuVO());
                //设置组合购商品价格
                BigDecimal discountPrice = suitsSku.getDiscountPrice();
                BigDecimal splitPrice = discountPrice.multiply(new BigDecimal(suitsSku.getNum()));
                tradeItemVO.setSplitPrice(splitPrice);
                tradeItemVO.setPrice(discountPrice);
                tradeItemVO.setLevelPrice(discountPrice);
                tradeItemVO.setBuyPoint(NumberUtils.LONG_ZERO);
            });

            BigDecimal goodsPrice = confirmItem.getTradeItems().stream().map(TradeItemVO::getSplitPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            ;
            confirmItem.getTradePrice().setGoodsPrice(goodsPrice);
            confirmItem.getTradePrice().setTotalPrice(goodsPrice);
            response.setTotalBuyPoint(NumberUtils.LONG_ZERO);
        }
    }


    /*
     * 仅用于立即购买后的确认订单页面，满系活动信息封装
     * @param response
     */
    private void fillMarketingInfo(TradeConfirmResp response, List<TradeItemGroupVO> tradeItemGroups) {
        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
        TradeItemGroupVO tradeItemGroupVO = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        List<TradeItemVO> items = confirmItem.getTradeItems();
        List<Long> marketingIds = items.stream().filter(v -> CollectionUtils.isNotEmpty(v.getMarketingIds()))
                .flatMap(v -> v.getMarketingIds().stream()).collect(Collectors.toList());
        List<Long> marketingLevelIds = items.stream().filter(v -> CollectionUtils.isNotEmpty(v.getMarketingLevelIds()))
                .flatMap(v -> v.getMarketingLevelIds().stream()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingIds) || CollectionUtils.isEmpty(marketingLevelIds)) {
            return;
        }
        Map<Long, TradeItemMarketingVO> marketingVOMap = tradeItemGroupVO.getTradeMarketingList()
                .stream().collect(Collectors.toMap(v -> v.getMarketingId(), v -> v));
        List<TradeConfirmMarketingVO> tradeConfirmMarketingList = new ArrayList<>();

        //批量获取满系营销以及规则信息
        MarketingViewQueryByIdsRequest idsRequest = new MarketingViewQueryByIdsRequest();
        idsRequest.setMarketingIds(marketingIds);
        idsRequest.setLevelFlag(true);
        List<MarketingViewVO> marketingViewVOS = marketingQueryProvider.queryViewByIds(idsRequest).getContext().getMarketingViewList();
        if (CollectionUtils.isNotEmpty(marketingViewVOS)) {
            Map<Long, MarketingViewVO> viewMap = marketingViewVOS.stream().collect(Collectors.toMap(MarketingViewVO::getMarketingId, m -> m));
            DecimalFormat fmt = new DecimalFormat("#.##");
            items.stream().filter(i -> CollectionUtils.isNotEmpty(i.getMarketingIds())).forEach(i -> {

                for (Long marketingId : i.getMarketingIds()) {
                    MarketingViewVO marketingViewVO = viewMap.get(marketingId);
                    Long levelId = marketingVOMap.get(marketingId).getMarketingLevelId();
                    if (marketingViewVO != null) {
                        TradeConfirmMarketingVO confirmMarketingVO = new TradeConfirmMarketingVO();
                        confirmMarketingVO.setMarketingId(marketingViewVO.getMarketingId());
                        confirmMarketingVO.setSkuIds(Collections.singletonList(i.getSkuId()));
                        confirmMarketingVO.setMarketingLevelId(levelId);
                        confirmMarketingVO.setMarketingType(marketingViewVO.getMarketingType().toValue());
                        String desc = "该营销不存在";
                        if (MarketingType.REDUCTION.equals(marketingViewVO.getMarketingType())) {
                            MarketingFullReductionLevelVO levelVO = marketingViewVO.getFullReductionLevelList().stream()
                                    .filter(l -> l.getReductionLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(marketingViewVO.getSubType())) {
                                    desc = String.format("您已满足满%s减%s", fmt.format(levelVO.getFullAmount()), fmt.format(levelVO.getReduction()));
                                } else {
                                    desc = String.format("您已满足满%s件减%s", levelVO.getFullCount(), fmt.format(levelVO.getReduction()));
                                }
                            }
                        } else if (MarketingType.DISCOUNT.equals(marketingViewVO.getMarketingType())) {
                            MarketingFullDiscountLevelVO levelVO = marketingViewVO.getFullDiscountLevelList().stream()
                                    .filter(l -> l.getDiscountLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(marketingViewVO.getSubType())) {
                                    desc = String.format("您已满足满%s享%s折", fmt.format(levelVO.getFullAmount()), fmt.format(levelVO.getDiscount().multiply(new BigDecimal(10))));
                                } else {
                                    desc = String.format("您已满足满%s件享%s折", levelVO.getFullCount(), fmt.format(levelVO.getDiscount().multiply(new BigDecimal(10))));
                                }
                            }
                        } else if (MarketingType.GIFT.equals(marketingViewVO.getMarketingType())) {
                            MarketingFullGiftLevelVO levelVO = marketingViewVO.getFullGiftLevelList().stream()
                                    .filter(l -> l.getGiftLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (MarketingSubType.GIFT_FULL_AMOUNT.equals(marketingViewVO.getSubType())) {
                                    desc = String.format("您已满足满%s获赠品", fmt.format(levelVO.getFullAmount()));
                                } else {
                                    desc = String.format("您已满足满%s件获赠品", levelVO.getFullCount());
                                }
                                confirmMarketingVO.setGiftSkuIds(levelVO.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList()));
                            }
                        } else if (MarketingType.BUYOUT_PRICE.equals(marketingViewVO.getMarketingType())) {
                            MarketingBuyoutPriceLevelVO levelVO = marketingViewVO.getBuyoutPriceLevelList().stream()
                                    .filter(l -> l.getReductionLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                desc = String.format("您已满足%s件%s元", levelVO.getChoiceCount(), fmt.format(levelVO.getFullAmount()));
                            }
                        } else if (MarketingType.HALF_PRICE_SECOND_PIECE.equals(marketingViewVO.getMarketingType())) {
                            MarketingHalfPriceSecondPieceLevelVO levelVO = marketingViewVO.getHalfPriceSecondPieceLevel().stream()
                                    .filter(l -> l.getId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (BigDecimal.ZERO.compareTo(levelVO.getDiscount()) == 0) {
                                    desc = String.format("您已满足买%s送1", levelVO.getNumber() - 1);
                                } else {
                                    desc = String.format("您已满足第%s件%s折", levelVO.getNumber(), fmt.format(levelVO.getDiscount()));
                                }
                                confirmMarketingVO.setHalfPriceSecondPieceLevel(levelVO);
                            }
                        } else if (MarketingType.MARKUP.equals(marketingViewVO.getMarketingType())) {
                            MarkupLevelVO levelVO = marketingViewVO.getMarkupLevelList().stream()
                                    .filter(l -> l.getId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                desc = String.format("您已满足%s加价购", levelVO.getLevelAmount());
                                confirmMarketingVO.setMarkupLevelVO(levelVO);
                            }
                        } else if(MarketingType.POINT_BUY.equals(marketingViewVO.getMarketingType())) {
                            List<MarketingPointBuyLevelVO> pointBuyLevelList = marketingViewVO.getPointBuyLevelList();
                            List<TradeItemVO> tradeItems = tradeItemGroups.get(0).getTradeItems();
                            boolean got = false;
                            for (MarketingPointBuyLevelVO levelVO : pointBuyLevelList) {
                                if(got) break;
                                for (TradeItemVO tradeItem : tradeItems) {
                                    if(levelVO.getSkuId().equals(tradeItem.getSkuId())){
                                        desc = String.format("您已满足%s积分+%s元换购", levelVO.getPointNeed(), levelVO.getMoney());
                                        confirmMarketingVO.setPointNeed(levelVO.getPointNeed());
                                        confirmMarketingVO.setMoney(levelVO.getMoney());
                                        got = true;
                                        break;
                                    }
                                }
                            }
                            /*MarketingPointBuyLevelVO levelVO = marketingViewVO.getPointBuyLevelList().stream().filter(l -> l.getId().equals(levelId)).findFirst().orElse(null);
                            if(levelVO != null){
                                desc = String.format("您已满足%s积分+%s元换购", levelVO.getPointNeed(), levelVO.getMoney());
                                confirmMarketingVO.setPointNeed(levelVO.getPointNeed());
                                confirmMarketingVO.setMoney(levelVO.getMoney());
                            }*/
                        }
                        if (!MarketingType.SUITS.equals(marketingViewVO.getMarketingType())) {
                            confirmMarketingVO.setMarketingDesc(desc);
                        }
                        tradeConfirmMarketingList.add(confirmMarketingVO);
                    }
                }
            });
        }

        confirmItem.setTradeConfirmMarketingList(tradeConfirmMarketingList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TradeConfirmMarketingVO::getMarketingId))), ArrayList::new)));
    }


    /**
     * 添加加价购商品
     *
     * @param confirmResponse
     */
    private void markupForGoodsInfo(TradeConfirmResp confirmResponse, CustomerGetByIdResponse customer) {
        // 是否有加价购
        List<TradeConfirmMarketingVO> tradeConfirmMarketingVOS = confirmResponse.getTradeConfirmItems().stream()
                .filter(i -> CollectionUtils.isNotEmpty(i.getTradeConfirmMarketingList()))
                .flatMap(i -> i.getTradeConfirmMarketingList().stream())
                .filter(m -> m.getMarketingType() == MarketingType.MARKUP.toValue())
                .collect(Collectors.toList());
        // 页面不需要展示加价购营销信息,排除
        confirmResponse.getTradeConfirmItems().stream()
                .filter(i -> CollectionUtils.isNotEmpty(i.getTradeConfirmMarketingList()))
                .forEach(i -> {
                    List<TradeConfirmMarketingVO> markupList = i.getTradeConfirmMarketingList().stream()
                            .filter(m -> m.getMarketingType() == MarketingType.MARKUP.toValue())
                            .collect(Collectors.toList());
                    i.getTradeConfirmMarketingList().removeAll(markupList);
                });
        if (CollectionUtils.isNotEmpty(tradeConfirmMarketingVOS)) {
            return;
        }

        // 秒杀,拼团,预售商品 不显示加价购信息
        TradeItemVO item = confirmResponse.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO).getTradeItems().get(NumberUtils.INTEGER_ZERO);
        if (Objects.nonNull(confirmResponse.getOpenGroupon()) || item.getIsBookingSaleGoods()
                || item.getIsFlashSaleGoods()) {
            return;
        }

        HashMap<Long, CommonLevelVO> commonLevelVOMap = new HashMap<>();
        List<PaidCardCustomerRelVO> relVOList = new ArrayList<>();
        if (Objects.nonNull(customer)) {

            // 获取用户在店铺里的等级
            List<Long> storeIds =
                    confirmResponse.getTradeConfirmItems().stream().map(i -> i.getSupplier().getStoreId()).collect(Collectors.toList());
            commonLevelVOMap = marketingPluginQueryProvider.getCustomerLevelsByStoreIds(
                            MarketingPluginGetCustomerLevelsByStoreIdsRequest.builder().storeIds(storeIds)
                                    .customer(KsBeanUtil.convert(customer, CustomerDTO.class)).build()).getContext()
                    .getCommonLevelVOMap();

            relVOList = paidCardCustomerRelQueryProvider
                    .list(PaidCardCustomerRelListRequest.builder()
                            .delFlag(DeleteFlag.NO)
                            .endTimeBegin(LocalDateTime.now())
                            .customerId(customer.getCustomerId()).build())
                    .getContext().getPaidCardCustomerRelVOList();
        }
        // 查询加价购阶梯详情
        List<String> ids = confirmResponse.getTradeConfirmItems().stream().flatMap(i -> i.getTradeItems().stream())
                .map(t -> t.getSkuId()).collect(Collectors.toList());
        MarkupLevelBySkuRequest markupLevelBySkuRequest = MarkupLevelBySkuRequest.builder()
                .skuIds(ids).levelAmount(confirmResponse.getTotalPrice()).marketingJoinLevelList(new ArrayList<>()).build();


        // 付费会员
        if (CollectionUtils.isNotEmpty(relVOList)) {
            markupLevelBySkuRequest.getMarketingJoinLevelList().add(MarketingJoinLevel.PAID_CARD_CUSTOMER);
        }
//        // 企业会员
//        if (EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())) {
//            markupLevelBySkuRequest.getMarketingJoinLevelList().add(MarketingJoinLevel.ENTERPRISE_CUSTOMER);
//        }
        markupLevelBySkuRequest.setLevelMap(commonLevelVOMap);
        MarkupLevelBySkuResponse levelBySkuResponse = markupQueryProvider.getMarkupListBySku(markupLevelBySkuRequest).getContext();
        List<MarkupLevelVO> levelList = levelBySkuResponse.getLevelList();
        if (CollectionUtils.isEmpty(levelList)) {
            return;
        }
        // 返回对象填充值
        Map<String, GoodsInfoVO> goodsInfoVOMap = levelBySkuResponse.getGoodsInfoVOList().stream().collect(Collectors.toMap(g -> g.getGoodsInfoId(), g -> g));
        Map<Long, List<String>> skuMap = levelList.stream().collect(Collectors.toMap(l -> l.getId(), l -> l.getSkuIds()));
        List<MarkupLevelDetailVO> markupLevelDetailVOS = levelList.stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
                .map(d -> {
                    d.setGoodsInfo(goodsInfoVOMap.get(d.getGoodsInfoId()));
                    d.setSkuIds(skuMap.get(d.getMarkupLevelId()));
                    return d;
                }).collect(Collectors.toList());
        //
        List<String> skuIds = confirmResponse.getTradeConfirmItems().stream()
                .flatMap(i -> i.getTradeItems().stream())
                .map(i -> i.getSkuId()).collect(Collectors.toList());
        List<MarkupLevelDetailVO> markupLevelDetailVOList = markupLevelDetailVOS.stream().filter(d -> !skuIds.contains(d.getGoodsInfoId()))
                .collect(Collectors.toList());
        confirmResponse.setMarkupLevel(markupLevelDetailVOList);

    }

// TODO 这里后续改版使用
//    public void list(List<String> skuIds, CustomerGetByIdResponse customer, SupplierDTO supplierDTO) {
//
//        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
//        goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIds);
//        goodsInfoViewByIdsRequest.setIsHavSpecText(Constants.yes);
//        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoViewByIdsRequest).getContext();
//
//        //计算区间价
//        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class);
//        GoodsIntervalPriceByCustomerIdRequest goodsIntervalPriceByCustomerIdRequest = new GoodsIntervalPriceByCustomerIdRequest();
//        goodsIntervalPriceByCustomerIdRequest.setCustomerId(customer.getCustomerId());
//        goodsIntervalPriceByCustomerIdRequest.setGoodsInfoDTOList(goodsInfoDTOList);
//        GoodsIntervalPriceByCustomerIdResponse priceResponse =
//                goodsIntervalPriceProvider.putByCustomerId(goodsIntervalPriceByCustomerIdRequest).getContext();
//        response.setGoodsInfos(priceResponse.getGoodsInfoVOList());
//
//        //获取客户的等级
//        if (StringUtils.isNotBlank(customer.getCustomerId())) {
//            //计算会员价
//            MarketingLevelGoodsListFilterRequest marketingLevelGoodsListFilterRequest = new MarketingLevelGoodsListFilterRequest();
//            marketingLevelGoodsListFilterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class));
//            marketingLevelGoodsListFilterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
//            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(marketingLevelGoodsListFilterRequest).getContext().getGoodsInfoVOList();
//            response.setGoodsInfos(goodsInfoVOList);
//        }
//        GoodsInfoResponse skuResp = new GoodsInfoResponse();
//        skuResp.setGoodses(response.getGoodses());
//        skuResp.setGoodsInfos(response.getGoodsInfos());
//        skuResp.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());List<TradeItemDTO> tradeItemDTOList = KsBeanUtil.convert(response.getGoodsInfos(), TradeItemDTO.class);
//        VerifyGoodsRequest verifyGoodsRequest = new VerifyGoodsRequest();
//        verifyGoodsRequest.setTradeItems(tradeItemDTOList);
//        verifyGoodsRequest.setOldTradeItems(Collections.emptyList());
//        verifyGoodsRequest.setGoodsInfoResponse(KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class));
//        verifyGoodsRequest.setStoreId(supplierDTO.getStoreId());
//        verifyGoodsRequest.setIsFull(true);
//        List<TradeItemVO> tradeItemVOList = verifyQueryProvider.verifyGoods(verifyGoodsRequest).getContext().getTradeItems();
//    }
}
