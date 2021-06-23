package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeQueryRequest;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.GiftType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.common.model.entity.TradeMarketing;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.repository.MarketingRepository;
import com.wanmi.sbc.marketing.common.request.TradeItemInfo;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.request.TradeMarketingRequest;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;
import com.wanmi.sbc.marketing.coupon.model.entity.TradeCoupon;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.gift.model.root.MarketingFullGiftLevel;
import com.wanmi.sbc.marketing.gift.repository.MarketingFullGiftLevelRepository;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import com.wanmi.sbc.marketing.markup.repository.MarkupLevelRepository;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.plugin.ITradeCommitPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 加价购插件
 * Created by dyt on 2016/12/8.
 */
@Repository("markupPlugin")
public class MarkupPlugin implements IGoodsListPlugin, IGoodsDetailPlugin , ITradeCommitPlugin{


    @Autowired
    private MarkupLevelRepository markupLevelRepository;

    @Autowired
    private MarketingRepository marketingRepository;

    /**
     * 商品列表处理
     *
     * @param goodsInfos 商品数据
     * @param request    参数
     */
    @Override
    public void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        if (request.getCommitFlag()){
            return;
        }
        List<MarketingResponse> marketingList = request.getMarketingMap().values().stream()
                .flatMap(Collection::stream)
                .filter(marketing -> MarketingType.MARKUP.equals(marketing.getMarketingType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingList)) {
            return;
        }

        Map<Long, MarketingResponse> params = new HashMap<>();
        marketingList.forEach(marketingResponse -> params.put(marketingResponse.getMarketingId(), marketingResponse));
        List<Long> marketingIds = marketingList.stream().map(MarketingResponse::getMarketingId).collect(Collectors.toList());
        //填充营销描述<营销编号,描述>
        Map<Long, String> labelMap = this.getLabelMap(marketingIds, params);

        goodsInfos.stream()
                .filter(goodsInfo -> request.getMarketingMap().containsKey(goodsInfo.getGoodsInfoId()))
                .forEach(goodsInfo -> {
                    request.getMarketingMap().get(goodsInfo.getGoodsInfoId()).stream()
                            .filter(marketing -> MarketingType.MARKUP.equals(marketing.getMarketingType()))
                            .forEach(marketing -> {
                                MarketingLabelVO label = new MarketingLabelVO();
                                label.setMarketingId(marketing.getMarketingId());
                                label.setMarketingType(marketing.getMarketingType().toValue());
                                label.setMarketingDesc(labelMap.get(marketing.getMarketingId()));
                                goodsInfo.getMarketingLabels().add(label);
                            });
                });
    }

    /**
     * 商品详情处理
     *
     * @param detailResponse 商品详情数据
     * @param request        参数
     */
    @Override
    public void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) {
        List<MarketingResponse> marketingList = request.getMarketingMap().get(detailResponse.getGoodsInfo().getGoodsInfoId());

        if (CollectionUtils.isEmpty(marketingList)
                || marketingList.stream().filter(marketing -> MarketingType.MARKUP.equals(marketing.getMarketingType())).count() < 1) {
            return;
        }

        MarketingResponse marketingObj = marketingList.stream().filter(marketing -> MarketingType.MARKUP.equals(marketing.getMarketingType())).findFirst().get();
        //填充营销描述<营销编号,描述>
        Map<Long, MarketingResponse> params = new HashMap<>();
        params.put(marketingObj.getMarketingId(), marketingObj);
        Map<Long, String> labelMap = this.getLabelMap(Collections.singletonList(marketingObj.getMarketingId()), params);
        MarketingLabelVO label = new MarketingLabelVO();
        label.setMarketingId(marketingObj.getMarketingId());
        label.setMarketingType(marketingObj.getMarketingType().toValue());
        label.setMarketingDesc(labelMap.get(marketingObj.getMarketingId()));
        detailResponse.getGoodsInfo().getMarketingLabels().add(label);
    }



    /**
     * 获取营销描述<营销编号,描述>
     *
     * @param marketingIds 营销编号
     * @return
     */
    private Map<Long, String> getLabelMap(List<Long> marketingIds, Map<Long, MarketingResponse> marketingMap) {
        Map<Long, List<MarkupLevel>> levelsMap = markupLevelRepository.findAll((root, cquery, cbuild) -> root.get("markupId").in(marketingIds), Sort.by(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "levelAmount"), new Sort.Order(Sort.Direction.ASC, "levelAmount"))))
                .stream().collect(Collectors.groupingBy(MarkupLevel::getMarkupId));
        Map<Long, String> labelMap = new HashMap<>();
        DecimalFormat fmt = new DecimalFormat("#.##");
        levelsMap.forEach((marketingId, levels) -> {

                List<String> amount = levels.stream().filter(level -> Objects.nonNull(level.getLevelAmount())).map(level -> fmt.format(level.getLevelAmount())).collect(Collectors.toList());
                labelMap.put(marketingId, String.format("满%s满足加价购", StringUtils.join(amount, "，")));

        });
        return labelMap;
    }
    /**
     * 订单营销处理
     */
    @Override
    public TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request) {
        TradeMarketingRequest tradeMarketingDTO = request.getTradeMarketingDTO();
        if (tradeMarketingDTO == null) {
            return null;
        }
        Marketing marketing = marketingRepository.findById(tradeMarketingDTO.getMarketingId()).get();
        if (marketing.getMarketingType() != MarketingType.MARKUP) {
            return null;
        }
        List<TradeItemInfo> tradeItems = request.getTradeItems();
        // 校验加价购是否有周期购
        List<String> distriSkuIds = tradeItems.stream()
                .filter(item -> GoodsType.CYCLE_BUY.equals(item.getGoodsType()))
                .map(TradeItemInfo::getSkuId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(distriSkuIds)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        MarkupLevel level = markupLevelRepository.findById(tradeMarketingDTO.getMarketingLevelId())
                .orElseThrow(() -> new SbcRuntimeException("K-050312"));
        if ( tradeMarketingDTO.getGiftSkuIds().size() > 1) {
            throw new SbcRuntimeException("K-050312");
        }
        BigDecimal price = tradeItems.stream().map(i -> i.getPrice().multiply(new BigDecimal(i.getNum()))).reduce(
                BigDecimal.ZERO, BigDecimal::add);
        if (marketing.getSubType() == MarketingSubType.MARKUP) {
            //阶梯金额
            if (price.compareTo(level.getLevelAmount()) < 0) {
                throw new SbcRuntimeException("K-050312");
            }
        }

        TradeMarketingResponse response = new TradeMarketingResponse();
        response.setTradeMarketing(TradeMarketing.builder()
                .markupLevel(level)
                .discountsAmount(BigDecimal.ZERO)
                .marketingId(marketing.getMarketingId())
                .marketingName(marketing.getMarketingName())
                .marketingType(marketing.getMarketingType())
                .skuIds(tradeMarketingDTO.getSkuIds())
                .subType(marketing.getSubType())
                .markupSkuIds(tradeMarketingDTO.getMarkupSkuIds())
                .build());
        return response;
    }
}