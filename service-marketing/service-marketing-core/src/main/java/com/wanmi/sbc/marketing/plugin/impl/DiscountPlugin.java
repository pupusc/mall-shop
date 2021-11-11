package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.model.entity.TradeMarketing;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.repository.MarketingRepository;
import com.wanmi.sbc.marketing.common.request.TradeItemInfo;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.request.TradeMarketingRequest;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import com.wanmi.sbc.marketing.discount.repository.MarketingFullDiscountLevelRepository;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.plugin.ITradeCommitPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 营销满折插件
 * Created by dyt on 2016/12/8.
 */
@Repository("discountPlugin")
public class DiscountPlugin implements IGoodsListPlugin, IGoodsDetailPlugin, ITradeCommitPlugin {


    @Autowired
    private MarketingFullDiscountLevelRepository marketingFullDiscountLevelRepository;

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
        List<MarketingResponse> marketingList = request.getMarketingMap().values().stream().flatMap(Collection::stream)
                .filter(marketing -> MarketingType.DISCOUNT.equals(marketing.getMarketingType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingList)) {
            return;
        }

        List<Long> marketingIds = marketingList.stream().map(MarketingResponse::getMarketingId).collect(Collectors.toList());

        Map<Long, MarketingResponse> params = new HashMap<>();
        marketingList.forEach(marketingResponse -> params.put(marketingResponse.getMarketingId(), marketingResponse));
        //填充营销描述<营销编号,描述>
        Map<Long, String> labelMap = this.getLabelMap(marketingIds, params);

        goodsInfos.stream()
                .filter(goodsInfo -> request.getMarketingMap().containsKey(goodsInfo.getGoodsInfoId()))
                .forEach(goodsInfo -> {
                    request.getMarketingMap().get(goodsInfo.getGoodsInfoId()).stream()
                            .filter(marketing -> MarketingType.DISCOUNT.equals(marketing.getMarketingType()))
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
                || marketingList.stream().filter(marketing -> MarketingType.DISCOUNT.equals(marketing.getMarketingType())).count() < 1) {
            return;
        }

        MarketingResponse marketingObj = marketingList.stream().filter(marketing -> MarketingType.DISCOUNT.equals(marketing.getMarketingType())).findFirst().get();
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

    @Override
    public TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request) {
        TradeMarketingRequest tradeMarketingDTO = request.getTradeMarketingDTO();
        if (tradeMarketingDTO == null) {
            return null;
        }
        Marketing marketing = marketingRepository.findById(tradeMarketingDTO.getMarketingId()).get();
        if (marketing.getMarketingType() != MarketingType.DISCOUNT) {
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

        MarketingFullDiscountLevel level = marketingFullDiscountLevelRepository.findById(tradeMarketingDTO.getMarketingLevelId())
                .orElseThrow(() -> new SbcRuntimeException("K-050312"));
        BigDecimal price = tradeItems.stream().map(i -> i.getPrice().multiply(new BigDecimal(i.getNum()))).reduce(
                BigDecimal.ZERO, BigDecimal::add);
        if (marketing.getSubType() == MarketingSubType.DISCOUNT_FULL_AMOUNT) {
            //满金额赠
            if (price.compareTo(level.getFullAmount()) < 0) {
                throw new SbcRuntimeException("K-050312");
            }
        } else if (marketing.getSubType() == MarketingSubType.DISCOUNT_FULL_COUNT) {
            Long count = tradeItems.stream().map(TradeItemInfo::getNum).reduce(0L, (a, b) -> a + b);
            //满数量赠
            if (count < level.getFullCount()) {
                throw new SbcRuntimeException("K-050312");
            }
        }

        BigDecimal amount = price.multiply(level.getDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP);

        TradeMarketingResponse response = new TradeMarketingResponse();
        response.setTradeMarketing(TradeMarketing.builder()
                .fullDiscountLevel(level)
                .discountsAmount(price.subtract(amount))
                .realPayAmount(amount)
                .marketingId(marketing.getMarketingId())
                .marketingName(marketing.getMarketingName())
                .marketingType(marketing.getMarketingType())
                .skuIds(tradeMarketingDTO.getSkuIds())
                .subType(marketing.getSubType())
                .build());
        return response;
    }

    /**
     * 获取营销描述<营销编号,描述>
     *
     * @param marketingIds 营销编号
     * @return
     */
    private Map<Long, String> getLabelMap(List<Long> marketingIds, Map<Long, MarketingResponse> marketingMap) {
        Map<Long, List<MarketingFullDiscountLevel>> levelsMap = marketingFullDiscountLevelRepository.findAll((root, cquery, cbuild) -> root.get("marketingId").in(marketingIds), Sort.by(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "fullAmount"), new Sort.Order(Sort.Direction.ASC, "fullCount"))))
                .stream().collect(Collectors.groupingBy(MarketingFullDiscountLevel::getMarketingId));
        Map<Long, String> labelMap = new HashMap<>();
        DecimalFormat fmt = new DecimalFormat("#.##");
        levelsMap.forEach((marketingId, levels) -> {
            List<String> descs = levels.stream().map(level -> {
                if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(marketingMap.get(marketingId).getSubType())) {
                    return String.format("满%s享%s折", fmt.format(level.getFullAmount()), fmt.format(level.getDiscount().multiply(new BigDecimal(10))));
                }
                return String.format("满%s件享%s折", level.getFullCount(), fmt.format(level.getDiscount().multiply(new BigDecimal(10))));
            }).collect(Collectors.toList());
            labelMap.put(marketingId, StringUtils.join(descs, "，"));
        });
        return labelMap;
    }

}