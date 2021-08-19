package com.wanmi.sbc.marketing.common.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.marketing.MarketingPluginService;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.distribution.service.DistributionSettingService;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsCacheProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoCachesByIdsRequest;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoResponseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.request.market.InfoForPurchseRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketInfoForPurchaseResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingCacheVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.marketing.bean.vo.GoodsInfoMarketingVO;
import com.wanmi.sbc.marketing.cache.MarketingCacheService;
import com.wanmi.sbc.marketing.common.request.MarketingRequest;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.distribution.service.DistributionCacheService;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarketingCommonService {

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private MarketingCacheService marketingCacheService;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private GoodsCacheProvider goodsCacheProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private SystemPointsConfigQueryProvider systemPointsConfigQueryProvider;

    @Autowired
    private  DistributionSettingService distributionSettingService;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    @Autowired
    private MarketingPluginService marketingPluginService;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    public MarketInfoForPurchaseResponse queryInfoForPurchase(InfoForPurchseRequest request) {

        CustomerVO customer = request.getCustomer();
        if (Objects.isNull(request.getStoreLevelsMap())) request.setStoreLevelsMap(new HashMap<>());

        MarketInfoForPurchaseResponse response = new MarketInfoForPurchaseResponse();
        List<GoodsInfoMarketingVO> goodsInfos = request.getGoodsInfos();

        // 1.查询正在进行中的有效营销信息
        List<String> goodsInfoIds = goodsInfos.stream().map(GoodsInfoMarketingVO::getGoodsInfoId).collect(Collectors.toList());
        Map<String, List<MarketingViewVO>> marketingMap = this.getMarketingsByGoodsInfoIds(goodsInfoIds, Objects.nonNull(customer)
                && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState()) ? PriceType.ENTERPRISE_SKU : PriceType.SKU);

        // 客户等级过滤营销列表
        marketingMap.keySet().forEach(goodsInfoId -> {
            List<MarketingViewVO> marketingList = marketingMap.get(goodsInfoId);
            marketingList = marketingList.stream().filter(marketing -> {
                String joinLevel = marketing.getJoinLevel();
                CommonLevelVO customerLevel = request.getStoreLevelsMap().get(marketing.getStoreId());
                marketing.setMarketingScopeList(null);
                if (MarketingType.SUITS.equals(marketing.getMarketingType())
                        || MarketingType.FLASH_SALE.equals(marketing.getMarketingType())) {
                    return false;
                }

                if(String.valueOf(MarketingJoinLevel.PAID_CARD_CUSTOMER.toValue()).equals(marketing.getJoinLevel()) && Objects.nonNull(customer)) {
                    //付费会员
                    List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                            .list(PaidCardCustomerRelListRequest.builder()
                                    .delFlag(DeleteFlag.NO)
                                    .endTimeBegin(LocalDateTime.now())
                                    .customerId(customer.getCustomerId()).build())
                            .getContext().getPaidCardCustomerRelVOList();
                    if(CollectionUtils.isNotEmpty(relVOList)) {
                        return true;
                    }
                } if(String.valueOf(MarketingJoinLevel.ENTERPRISE_CUSTOMER.toValue()).equals(marketing.getJoinLevel())
                        && Objects.nonNull(customer) && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())) {
                    //企业会员
                    return true;
                }else if (joinLevel.equals("-1")) {
                    // 全平台用户
                    return true;
                } else if (joinLevel.equals("0")) {
                    // 店铺内全部等级
//                    return Objects.nonNull(customerLevel);
                    return true;
                }else{
                    // 店铺内指定等级
                    String[] joinLevels = joinLevel.split(",");
                    return Objects.nonNull(customerLevel) && Arrays.asList(joinLevels).contains(customerLevel.getLevelId().toString());
                }
            }).collect(Collectors.toList());
            goodsInfos.stream().filter(i -> i.getGoodsInfoId().equals(goodsInfoId)).findFirst().get()
                    .setMarketingViewList(marketingList);
        });

        // 2.设置分销信息
        DistributionSettingCacheVO disCache = marketingCacheService.queryDistributionSetting();
        // 设置分销员佣金比例
        if (Objects.nonNull(customer)) {
            DistributionCustomerVO distributionCustomer = marketingCacheService.getDistributorByCustomerId(customer.getCustomerId());
            if (Objects.nonNull(distributionCustomer)
                    && DefaultFlag.YES.equals(distributionCustomer.getDistributorFlag())
                    && DefaultFlag.NO.equals(distributionCustomer.getForbiddenFlag())) {
                DistributorLevelVO distributorLevel = disCache.getDistributorLevels().stream()
                        .filter(level -> level.getDistributorLevelId().equals(distributionCustomer.getDistributorLevelId())).findFirst().get();
                response.setCommissionRate(distributorLevel.getCommissionRate());
            }
        }
        // 设置商品分销状态
        Map<Long, List<GoodsInfoMarketingVO>> storeGoodsInfoMap = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfoMarketingVO::getStoreId));
        for (Long storeId: storeGoodsInfoMap.keySet()) {
            DefaultFlag storeDisFlag = distributionCacheService.queryStoreOpenFlag(storeId.toString());
            List<GoodsInfoMarketingVO> storeGoodsInfos = storeGoodsInfoMap.get(storeId);
            if (DeleteFlag.NO.equals(disCache.getDistributionSetting().getOpenFlag())
                    || DeleteFlag.NO.equals(storeDisFlag)) {
                storeGoodsInfos.forEach(item -> item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS));
            }
        }
        response.setGoodsInfos(goodsInfos);
        return response;
    }

    private Map<String, List<MarketingViewVO>> getMarketingsByGoodsInfoIds(List<String> goodsInfoIds, PriceType type) {

        Map<String, List<MarketingResponse>> marketingMap = marketingService.getMarketingMapByGoodsId(
                MarketingRequest.builder()
                        .goodsInfoIdList(goodsInfoIds)
                        .deleteFlag(DeleteFlag.NO)
                        .cascadeLevel(true)
                        .marketingStatus(MarketingStatus.STARTED).build()
        );

        // 查询满赠赠品信息
        List<String> giftSkuIds = new ArrayList<>();
        Map<Long, List<String>> marketingGiftSkuIdsMap = new HashMap<>();
        marketingMap.keySet().forEach(key -> {
            marketingMap.get(key).forEach(marketing -> {
                if (MarketingType.GIFT.equals(marketing.getMarketingType())) {
                    List<String> skuIds = marketing.getFullGiftLevelList().stream()
                            .flatMap(giftLevel -> giftLevel.getFullGiftDetailList().stream().map(MarketingFullGiftDetail::getProductId))
                            .distinct().collect(Collectors.toList());
                    giftSkuIds.addAll(skuIds);
                    marketingGiftSkuIdsMap.put(marketing.getMarketingId(), skuIds);
                }
            });
        });
        if (CollectionUtils.isNotEmpty(giftSkuIds)) {
            List<GoodsInfoVO> allGifts = goodsCacheProvider.listGoodsInfosByIds(
                    new GoodsInfoCachesByIdsRequest(giftSkuIds.stream().distinct().collect(Collectors.toList()), type)).getContext().getGoodsInfos();
            marketingMap.keySet().forEach(key -> {
                marketingMap.get(key).forEach(marketing -> {
                    if (MarketingType.GIFT.equals(marketing.getMarketingType())) {
                        List<String> skuIds = marketingGiftSkuIdsMap.get(marketing.getMarketingId());
                        List<GoodsInfoVO> gifts = allGifts.stream().filter(gift -> skuIds.contains(gift.getGoodsInfoId())).collect(Collectors.toList());
                        marketing.setGoodsList(GoodsInfoResponseVO.builder().goodsInfos(gifts).build());
                    }
                });
            });
        }

        Map<String, List<MarketingViewVO>> marketingViewMap = new HashMap<>();
        marketingMap.keySet().forEach(key -> {
            List<MarketingViewVO> marketings = KsBeanUtil.convertList(marketingMap.get(key), MarketingViewVO.class);
            marketingViewMap.put(key, marketings);
        });

        return marketingViewMap;
    }
}
