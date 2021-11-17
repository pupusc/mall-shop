package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
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
        return null;
    }
}
