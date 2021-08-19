package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.response.flashsalegoods.FlashSaleGoodsListResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: sbc-micro-service
 * @description: 秒杀活动营销插件
 * @create: 2019-07-02 18:31
 **/
@Repository("flashSalePlugin")
public class FlashSalePlugin implements IGoodsListPlugin {
    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;
    /**
     * 商品列表处理
     *
     * @param goodsInfos 商品数据
     * @param request    参数
     */
    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        if (Objects.nonNull(request.getIsFlashSaleMarketing()) && request.getIsFlashSaleMarketing()){
            return;
        }
        //含购买积分的商品不允许参与秒杀
        List<GoodsInfoVO> tempGoodsInfos = goodsInfos.stream().filter(g -> Objects.isNull(g.getBuyPoint()) || g.getBuyPoint() == 0L)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tempGoodsInfos)){
            return;
        }
        List<String> goodsinfoIds = tempGoodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        Integer queryDataType = 1;//默认只显示进行中的秒杀
        //魔方特殊要求规则
        if(Boolean.TRUE.equals(request.getMoFangFlag())){
            queryDataType = 3;
        }
        FlashSaleGoodsListResponse listResponse =
                flashSaleGoodsQueryProvider.list(FlashSaleGoodsListRequest.builder().goodsinfoIds(goodsinfoIds).delFlag(DeleteFlag.NO)
                        .queryDataType(queryDataType).build()).getContext();
        Map<String, FlashSaleGoodsVO> goodsVOMap =
        listResponse.getFlashSaleGoodsVOList().stream().collect(Collectors.toMap(FlashSaleGoodsVO::getGoodsInfoId,
                v->v));

        tempGoodsInfos.forEach(goodsInfoVO -> {
            if (Objects.nonNull(goodsVOMap) && Objects.nonNull(goodsVOMap.get(goodsInfoVO.getGoodsInfoId()))) {
                FlashSaleGoodsVO vo = goodsVOMap.get(goodsInfoVO.getGoodsInfoId());
                //最小起订量大于库存量时认为活动结束
                if (vo.getStock() >= vo.getMinNum()) {
                    MarketingLabelVO marketingLabelVO = new MarketingLabelVO();
                    marketingLabelVO.setMarketingType(MarketingType.FLASH_SALE.toValue());
                    marketingLabelVO.setMarketingDesc("秒杀");
                    marketingLabelVO.setStartTime(vo.getActivityFullTime());
                    marketingLabelVO.setEndTime(vo.getActivityFullTime().plusHours(Constants.FLASH_SALE_LAST_HOUR));
                    marketingLabelVO.setMarketingStatus(getActivityStatus(marketingLabelVO.getStartTime(), marketingLabelVO.getEndTime()).toValue());
                    if(vo.getStock() > 0){
                        marketingLabelVO.setProgressRatio(
                                BigDecimal.valueOf(vo.getSalesVolume() == null ? 0 : vo.getSalesVolume()).divide(BigDecimal.valueOf(vo.getStock()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                    }
                    goodsInfoVO.getMarketingLabels().add(marketingLabelVO);
                    goodsInfoVO.setSalePrice(vo.getPrice());
                }
            }
        });
    }


    private MarketingStatus getActivityStatus(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startTime)) {
            return MarketingStatus.NOT_START;
        }
        if (now.isEqual(startTime) || now.isEqual(endTime) || now.isBefore(endTime)) {
            return MarketingStatus.STARTED;
        }
        return MarketingStatus.ENDED;
    }
}