package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 积分兑换营销
 */
@Component("pointBuyPlugin")
public class PointBuyPlugin implements IGoodsListPlugin, IGoodsDetailPlugin, ITradeCommitPlugin {

    @Autowired
    private MarketingPointBuyLevelRepository marketingPointBuyLevelRepository;

    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        if (request.getCommitFlag()) return;

        List<MarketingResponse> marketingList = request.getMarketingMap().values().stream().flatMap(Collection::stream)
                .filter(marketing -> MarketingType.DISCOUNT.equals(marketing.getMarketingType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingList)) {
            return;
        }
        List<Long> marketingIds = marketingList.stream().map(MarketingResponse::getMarketingId).collect(Collectors.toList());

        Map<String, List<MarketingResponse>> marketingMap = request.getMarketingMap();
        Set<Map.Entry<String, List<MarketingResponse>>> goodsMarketings = marketingMap.entrySet();

        List<MarketingPointBuyLevel> levelsMap = null;
        for (GoodsInfoVO goodsInfo : goodsInfos) {
            List<MarketingResponse> marketingResponses = marketingMap.get(goodsInfo.getGoodsId());
            if(marketingResponses != null){
                for (MarketingResponse marketingRespons : marketingResponses) {
                    if(MarketingType.POINT_BUY.equals(marketingRespons.getMarketingType())){
                        if(levelsMap == null){
                            levelsMap = marketingPointBuyLevelRepository.findAllById(marketingIds);
                        }

                    }
                }
            }
        }

        for (Map.Entry<String, List<MarketingResponse>> goodsMarketing : goodsMarketings) {


            List<MarketingResponse> marketings = goodsMarketing.getValue();
            for (MarketingResponse marketing : marketings) {
                if(MarketingType.POINT_BUY.equals(marketing.getMarketingType())){
                    if(levelsMap == null){
                        levelsMap = marketingPointBuyLevelRepository.findAll();
                    }

                }
            }

        }
//
//        List<MarketingResponse> marketingList = request.getMarketingMap().values().stream()
//                .flatMap(Collection::stream)
//                .filter(marketing -> MarketingType.POINT_BUY.equals(marketing.getMarketingType()))
//                .collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(marketingList)) {
//            return;
//        }
//        Map<Long, String> labelMap = new HashMap<>();
//        for (MarketingResponse marketingResponse : marketingList) {
//
//        }
    }

    @Override
    public void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) {

    }

    @Override
    public TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request) {
        return null;
    }
}
