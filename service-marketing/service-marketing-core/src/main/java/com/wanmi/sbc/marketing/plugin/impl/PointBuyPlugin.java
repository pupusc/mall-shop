package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.model.entity.TradeMarketing;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.repository.MarketingRepository;
import com.wanmi.sbc.marketing.common.request.TradeItemInfo;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.request.TradeMarketingRequest;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingPointBuyLevel;
import com.wanmi.sbc.marketing.discount.repository.MarketingPointBuyLevelRepository;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.plugin.ITradeCommitPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 积分兑换营销
 */
@Component("pointBuyPlugin")
@Slf4j
public class PointBuyPlugin implements IGoodsListPlugin, IGoodsDetailPlugin, ITradeCommitPlugin {

    @Autowired
    private MarketingPointBuyLevelRepository marketingPointBuyLevelRepository;
    @Autowired
    private MarketingRepository marketingRepository;

    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        log.info("积分换购 goodsListFilter");
        if (request.getCommitFlag()) return;

        List<MarketingResponse> marketingList = request.getMarketingMap().values().stream().flatMap(Collection::stream)
                .filter(marketing -> MarketingType.POINT_BUY.equals(marketing.getMarketingType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingList)) {
            return;
        }
        List<Long> marketingIds = marketingList.stream().map(MarketingResponse::getMarketingId).collect(Collectors.toList());
        List<String> skuIds = goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());

        Map<String, List<MarketingResponse>> marketingMap = request.getMarketingMap();
        Map<Long, String> labelMap = null;
        List<MarketingPointBuyLevel> levelsMap = null;
        for (GoodsInfoVO goodsInfo : goodsInfos) {
            List<MarketingResponse> marketingResponses = marketingMap.get(goodsInfo.getGoodsInfoId());
            if(marketingResponses != null){
                for (MarketingResponse marketingRespons : marketingResponses) {
                    if(MarketingType.POINT_BUY.equals(marketingRespons.getMarketingType())){
                        if(labelMap == null || levelsMap == null){
                            levelsMap = marketingPointBuyLevelRepository.findAllByMarketingIdInAndSkuIdIn(marketingIds, skuIds);
                            labelMap = new HashMap<>();
                            for (MarketingPointBuyLevel marketingPointBuyLevel : levelsMap) {
                                labelMap.put(marketingPointBuyLevel.getMarketingId(), marketingPointBuyLevel.getPointNeed() + "积分换购");
                            }
                        }
                        for (MarketingPointBuyLevel marketingPointBuyLevel : levelsMap) {
                            if(marketingPointBuyLevel.getSkuId().equals(goodsInfo.getGoodsInfoId())){
                                MarketingLabelVO label = new MarketingLabelVO();
                                label.setMarketingId(marketingRespons.getMarketingId());
                                label.setMarketingType(marketingRespons.getMarketingType().toValue());
                                label.setMarketingDesc(labelMap.get(marketingRespons.getMarketingId()));
                                label.setPrice(marketingPointBuyLevel.getPrice());
                                label.setPointNeed(marketingPointBuyLevel.getPointNeed());
                                goodsInfo.getMarketingLabels().add(label);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) {
        log.info("积分换购 goodsDetailFilter");
    }

    @Override
    public TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request) {
        TradeMarketingRequest tradeMarketingDTO = request.getTradeMarketingDTO();
        if (tradeMarketingDTO == null) return null;
        Marketing marketing = marketingRepository.findById(tradeMarketingDTO.getMarketingId()).get();
        if (marketing.getMarketingType() != MarketingType.POINT_BUY) {
            return null;
        }
        List<TradeItemInfo> tradeItems = request.getTradeItems();
        // 校验营销关联商品中，是否存在分销商品
        List<String> distriSkuIds = tradeItems.stream()
                .filter(item -> item.getDistributionGoodsAudit() == DistributionGoodsAudit.CHECKED)
                .map(TradeItemInfo::getSkuId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(distriSkuIds)) {
            tradeMarketingDTO.getSkuIds().forEach(skuId -> {
                if (distriSkuIds.contains(skuId)) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
            });
        }
        List<MarketingPointBuyLevel> levels = marketingPointBuyLevelRepository.findAllByMarketingIdInAndSkuIdIn(Arrays.asList(tradeMarketingDTO.getMarketingId()),
                Arrays.asList(tradeItems.get(0).getSkuId()));
        if(CollectionUtils.isEmpty(levels) || levels.size() > 1){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "积分活动校验错误");
        }

//        MarketingPointBuyLevel level = levels.get(0);
        TradeMarketingResponse response = new TradeMarketingResponse();
//        response.setTradeMarketing(TradeMarketing.builder()
//                .fullDiscountLevel(level)
//                .discountsAmount(price.subtract(amount))
//                .realPayAmount(amount)
//                .marketingId(marketing.getMarketingId())
//                .marketingName(marketing.getMarketingName())
//                .marketingType(marketing.getMarketingType())
//                .skuIds(tradeMarketingDTO.getSkuIds())
//                .subType(marketing.getSubType())
//                .build());

        return null;
    }
}
