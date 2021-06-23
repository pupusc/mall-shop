package com.wanmi.sbc.marketing.plugin.impl;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
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
import com.wanmi.sbc.marketing.halfpricesecondpiece.model.entry.MarketingHalfPriceSecondPieceLevel;
import com.wanmi.sbc.marketing.halfpricesecondpiece.repository.HalfPriceSecondPieceLevelRepository;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.plugin.ITradeCommitPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 营销打包一口价插件
 * Created by weiwenhao on 2020/05/25.
 */
@Repository("halfPriceSecondPiecePlugin")
public class HalfPriceSecondPiecePlugin implements IGoodsListPlugin, IGoodsDetailPlugin, ITradeCommitPlugin {

    @Autowired
    HalfPriceSecondPieceLevelRepository halfPriceSecondPieceLevelRepository;

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
                .filter(marketing -> MarketingType.HALF_PRICE_SECOND_PIECE.equals(marketing.getMarketingType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingList)) {
            return;
        }

        Map<Long, MarketingResponse> params = new HashMap<>();
        marketingList.forEach(marketingResponse -> params.put(marketingResponse.getMarketingId(), marketingResponse));
        //填充营销描述<营销编号,描述>
        Map<Long, String> labelMap = this.getLabelMap(marketingList);
        goodsInfos.stream()
                .filter(goodsInfo -> request.getMarketingMap().containsKey(goodsInfo.getGoodsInfoId()))
                .forEach(goodsInfo -> {
                    request.getMarketingMap().get(goodsInfo.getGoodsInfoId()).stream()
                            .filter(marketing -> MarketingType.HALF_PRICE_SECOND_PIECE.equals(marketing.getMarketingType()))
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
        List<MarketingResponse> marketingList =
                request.getMarketingMap().get(detailResponse.getGoodsInfo().getGoodsInfoId());
        if (CollectionUtils.isEmpty(marketingList)
                || marketingList.stream().filter(marketing -> MarketingType.HALF_PRICE_SECOND_PIECE.equals(marketing.getMarketingType())).count() < 1) {
            return;
        }
        MarketingResponse marketingObj =
                marketingList.stream().filter(marketing -> MarketingType.HALF_PRICE_SECOND_PIECE.equals(marketing.getMarketingType())).findFirst().get();
        //填充营销描述<营销编号,描述>
        Map<Long, String> labelMap = this.getLabelMap(marketingList);
        MarketingLabelVO label = new MarketingLabelVO();
        label.setMarketingId(marketingObj.getMarketingId());
        label.setMarketingType(marketingObj.getMarketingType().toValue());
        label.setMarketingDesc(labelMap.get(marketingObj.getMarketingId()));
        detailResponse.getGoodsInfo().getMarketingLabels().add(label);
    }

    /**
     * 确认订单计算第二件半价优惠活动
     *
     * @param request
     * @return
     */
    @Override
    public TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request) {
        TradeMarketingRequest tradeMarketingDTO = request.getTradeMarketingDTO();
        if (tradeMarketingDTO == null) {
            return null;
        }
        Marketing marketing = marketingRepository.findById(tradeMarketingDTO.getMarketingId()).get();
        if (marketing.getMarketingType() != MarketingType.HALF_PRICE_SECOND_PIECE) {
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

        MarketingHalfPriceSecondPieceLevel level =
                halfPriceSecondPieceLevelRepository.findById(tradeMarketingDTO.getMarketingLevelId())
                        .orElseThrow(() -> new SbcRuntimeException("K-050312"));
        //合计数量
        Long count = tradeItems.stream().map(TradeItemInfo::getNum).reduce(0L, (a, b) -> a + b);
        if (count < level.getDiscount().compareTo(BigDecimal.valueOf(Constants.MARKETING_FULLAMOUNT_MIN))) {
            throw new SbcRuntimeException("K-050312");
        }

        //按价格升序,以便优先以价格低的商品凑单计算优惠金额
        List<TradeItemInfo> itemInfoList =
                tradeItems.stream().sorted(Comparator.comparing(TradeItemInfo::getPrice)).collect(Collectors.toList());
        //计算总的金额
        BigDecimal price = tradeItems.stream().map(i -> i.getPrice().multiply(new BigDecimal(i.getNum()))).reduce(
                BigDecimal.ZERO, BigDecimal::add);
        BigDecimal halfPriceSecondPiece = BigDecimal.ZERO;
        //计算折扣次数
        Long grade = count / level.getNumber();
        if (grade > 0) { //有折扣
            if (itemInfoList.size() <= grade) {//商品数量小于折扣时
                if (itemInfoList.size() == 1) {//当只有一件商品时一件商品多次折扣
                    for (int i = 0; i < grade; i++) {
                        if (level.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                            //第N件0折，第N件是买N件送一件，以价格最低的送
                            halfPriceSecondPiece = halfPriceSecondPiece.add(itemInfoList.get(0).getPrice());
                        } else {
                            //第N件以最小的商品金额计算折扣
                            halfPriceSecondPiece =
                                    halfPriceSecondPiece.add(itemInfoList.get(0).getPrice().multiply((BigDecimal.valueOf(10).subtract(level.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                } else {
                    for (TradeItemInfo tradeItemInfo : itemInfoList) {
                        if (level.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                            //第N件0折，第N件是买N件送一件，以价格最低的送
                            halfPriceSecondPiece = halfPriceSecondPiece.add(tradeItemInfo.getPrice());
                        } else {
                            //第N件以最小的商品金额计算折扣
                            halfPriceSecondPiece =
                                    halfPriceSecondPiece.add(tradeItemInfo.getPrice().multiply((BigDecimal.valueOf(10).subtract(level.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                    for (int i = 0; i < grade - itemInfoList.size(); i++) {
                        if (level.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                            //第N件0折，第N件是买N件送一件，以价格最低的送
                            halfPriceSecondPiece = halfPriceSecondPiece.add(itemInfoList.get(itemInfoList.size()-1).getPrice());
                        } else {
                            //第N件以最小的商品金额计算折扣
                            halfPriceSecondPiece =
                                    halfPriceSecondPiece.add(itemInfoList.get(itemInfoList.size()-1).getPrice().multiply((BigDecimal.valueOf(10).subtract(level.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
            }else {//当折扣小于商品数量的时候从高到低依次获取商品价格
                List<TradeItemInfo> gradeItemInfoList = itemInfoList.subList(0, grade.intValue());
                for (TradeItemInfo gradeTradeItemInfo : gradeItemInfoList) {
                    if (level.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                        //第N件0折，第N件是买N件送一件，以价格最低的送
                        halfPriceSecondPiece = halfPriceSecondPiece.add(gradeTradeItemInfo.getPrice());
                    } else {
                        //第N件以最小的商品金额计算折扣
                        halfPriceSecondPiece =
                                halfPriceSecondPiece.add(gradeTradeItemInfo.getPrice().multiply((BigDecimal.valueOf(10).subtract(level.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }

        }
        TradeMarketingResponse response = new TradeMarketingResponse();
        response.setTradeMarketing(TradeMarketing.builder()
                .halfPriceSecondPieceLevel(level)
                //优惠金额
                .discountsAmount(halfPriceSecondPiece)
                //该活动关联商品除去优惠金额外的应付金额
                .realPayAmount(price.subtract(halfPriceSecondPiece))
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
     * @param marketingList 营销列表
     * @return
     */
    private Map<Long, String> getLabelMap(List<MarketingResponse> marketingList) {
        Map<Long, String> labelMap = new HashMap<>();
        DecimalFormat fmt = new DecimalFormat("#.##");
        marketingList.stream().forEach(marketing -> {
            List<String> descs = new ArrayList<>();
            if (null != marketing && CollectionUtils.isNotEmpty(marketing.getHalfPriceSecondPieceLevel())) {
                for (MarketingHalfPriceSecondPieceLevel halfPriceSecondPieceLevel :
                        marketing.getHalfPriceSecondPieceLevel()) {
                    if (halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                        descs.add(String.format("买%s送1", halfPriceSecondPieceLevel.getNumber() - 1));
                    } else {
                        descs.add(String.format("第%s件%s折", halfPriceSecondPieceLevel.getNumber(),
                                fmt.format(halfPriceSecondPieceLevel.getDiscount())));
                    }

                }
            }
            labelMap.put(marketing.getMarketingId(), StringUtils.join(descs, "，"));
        });
        return labelMap;
    }
}