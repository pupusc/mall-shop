package com.wanmi.sbc.marketing.plugin.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.info.VideoChannelSetFilterControllerProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.vo.CouponLabelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeQueryRequest;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.common.request.TradeItemInfo;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;
import com.wanmi.sbc.marketing.coupon.model.entity.TradeCoupon;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.repository.CouponMarketingScopeRepository;
import com.wanmi.sbc.marketing.coupon.service.CouponCacheService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.plugin.ITradeCommitPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 营销满赠插件
 * Created by dyt on 2016/12/8.
 */
@Repository("couponPlugin")
@Slf4j
public class CouponPlugin implements IGoodsListPlugin, IGoodsDetailPlugin, ITradeCommitPlugin {


    @Autowired
    private CouponCacheService couponCacheService;

    @Autowired
    private CouponCodeService couponCodeService;

    @Autowired
    private CouponMarketingScopeRepository couponMarketingScopeRepository;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;

    @Autowired
    private VideoChannelSetFilterControllerProvider videoChannelSetFilterControllerProvider;

    /**
     * 获取优惠券黑名单
     * @return
     */
    private List<String> listUnUseCouponBlackList () {
        List<String> unUseCouponBlackList = new ArrayList<>();
        //获取黑名单
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(
                Collections.singletonList(GoodsBlackListCategoryEnum.UN_USE_GOODS_COUPON.getCode()));
        BaseResponse<GoodsBlackListPageProviderResponse> goodsBlackListPageProviderResponseBaseResponse = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest);
        GoodsBlackListPageProviderResponse context = goodsBlackListPageProviderResponseBaseResponse.getContext();
        if (context.getUnUseCouponBlackListModel() != null && !CollectionUtils.isEmpty(context.getUnUseCouponBlackListModel().getGoodsIdList())) {
            unUseCouponBlackList.addAll(context.getUnUseCouponBlackListModel().getGoodsIdList());
        }
        return unUseCouponBlackList;
    }


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
        List<String> goodsIds=new ArrayList<>();
        goodsInfos.forEach(goodsInfo -> {
            goodsIds.add(goodsInfo.getGoodsId());
        });

        //去重
        List<String> distinctGoodsIds = goodsIds.stream().distinct().collect(Collectors.toList());

        //优化 将goodsid对应的店铺分类放置内存中
        //组装店铺分类
        Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(distinctGoodsIds).getContext();

        couponCacheService.refreshCache();
        goodsInfos.forEach(item -> {
            List<Integer> classifyIds = storeCateIdMap.get(item.getGoodsId());
            List<Long> classifyIdLong = null;
            if(classifyIds != null){
                classifyIdLong = classifyIds.stream().map(Integer::longValue).collect(Collectors.toList());
            }
            List<CouponCache> couponCacheList = couponCacheService.listCouponForGoodsInfos(item, request.getLevelMap(),classifyIdLong,request.getCouponScene());
            List<CouponLabelVO> labelList = couponCacheList.stream().limit(6).map(cache ->
                    CouponLabelVO.builder()
                            .couponActivityId(cache.getCouponActivityId())
                            .couponInfoId(cache.getCouponInfoId())
                            .couponDesc(getLabelMap(cache))
                            .couponScene(cache.getCouponActivity().getActivityScene()!= null ? String.join(",",cache.getCouponActivity().getActivityScene()):null)
                            .build()
            ).collect(Collectors.toList());
            item.getCouponLabels().addAll(labelList);
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
        //把品牌从goods搬运到goodsInfo
        detailResponse.getGoodsInfo().setBrandId(detailResponse.getGoods().getBrandId());
        detailResponse.getGoodsInfo().setCateId(detailResponse.getGoods().getCateId());
        List<CouponCache> couponCacheList = couponCacheService.listCouponForGoodsInfo(detailResponse.getGoodsInfo(), request.getLevelMap(), request.getCouponScene());
        List<CouponLabelVO> labelList = couponCacheList.stream().limit(3).map(cache ->
                CouponLabelVO.builder()
                        .couponActivityId(cache.getCouponActivityId())
                        .couponInfoId(cache.getCouponInfoId())
                        .couponDesc(getLabelMap(cache))
                        .build()
        ).collect(Collectors.toList());
        detailResponse.getGoodsInfo().getCouponLabels().addAll(labelList);
    }


    /**
     * 订单营销处理
     */
    @Override
    public TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request) {
        log.info("CouponPlugin wraperMarketingFullInfo param: {}", JSON.toJSONString(request));
        String couponCodeId = request.getCouponCodeId();
//        List<TradeItemInfo> tradeItems = request.getTradeItems();
        if(StringUtils.isEmpty(couponCodeId)) {
            return null;
        }

        /**
         * 优惠券 视频号黑名单
         */
        List<String> skuIdList = request.getTradeItems().stream().map(TradeItemInfo::getSkuId).collect(Collectors.toList());
        Map<String, Boolean> goodsId2VideoChannelMap = videoChannelSetFilterControllerProvider.filterGoodsIdHasVideoChannelMap(skuIdList).getContext();

        List<TradeItemInfo> tradeItems = new ArrayList<>();
        List<String> unUseCouponBlackList = this.listUnUseCouponBlackList();
        for (TradeItemInfo tradeItem : request.getTradeItems()) {
            if (unUseCouponBlackList.contains(tradeItem.getSpuId())) {
                continue;
            }
            if (goodsId2VideoChannelMap.get(tradeItem.getSpuId()) != null && goodsId2VideoChannelMap.get(tradeItem.getSpuId())) {
                continue;
            }
            tradeItems.add(tradeItem);
        }

        log.info("CouponPlugin wraperMarketingFullInfo tradeItems:{}", JSON.toJSONString(tradeItems));

        // 1.查询我的未使用优惠券
        List<CouponCode> couponCodes = couponCodeService.listCouponCodeByCondition(CouponCodeQueryRequest.builder()
                .customerId(request.getCustomerId())
                .useStatus(DefaultFlag.NO)
                .delFlag(DeleteFlag.NO).build());

        // 2.判断所传优惠券，是否是我的未用优惠券
        CouponCode couponCode = couponCodes.stream().filter(item ->
            StringUtils.equals(item.getCouponCodeId(), couponCodeId)
        ).findFirst().orElseThrow(() -> new SbcRuntimeException(CouponErrorCode.CUSTOMER_ORDER_COUPON_INVALID));

        // 3.判断所传优惠券，是否在使用时间
        if(LocalDateTime.now().isAfter(couponCode.getEndTime())) {
            if(!request.isForceCommit()) {
                CouponInfo couponInfo = couponInfoRepository.findById(couponCode.getCouponId()).get();
                StringBuilder sb = new StringBuilder("很抱歉，");
                if(FullBuyType.NO_THRESHOLD.equals(couponInfo.getFullBuyType())) {
                    sb.append("无门槛减").append(couponInfo.getDenomination().setScale(0));
                } else {
                    sb.append("满").append(couponInfo.getFullBuyPrice().setScale(0))
                            .append("减").append(couponInfo.getDenomination().setScale(0));
                }
                sb.append("优惠券已失效");
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, sb.toString());
            } else {
                return null;
            }
        }

        // 4.查找出优惠券关联的商品列表
        List<CouponMarketingScope> scopeList = couponMarketingScopeRepository.findByCouponId(couponCode.getCouponId());

        CouponInfo couponInfo = couponInfoRepository.findById(couponCode.getCouponId()).get();
        CouponCodeVO couponCodeVo = new CouponCodeVO();
        KsBeanUtil.copyPropertiesThird(couponCode, couponCodeVo);
        couponCodeVo.setScopeType(couponInfo.getScopeType());
        couponCodeVo.setPlatformFlag(couponInfo.getPlatformFlag());
        couponCodeVo.setStoreId(couponInfo.getStoreId());
        couponCodeVo.setFullBuyPrice(couponInfo.getFullBuyPrice());
        couponCodeVo.setCouponType(couponInfo.getCouponType());
        List<String> goodsInfoIds = couponCodeService.listCouponSkuIds(tradeItems, couponCodeVo, scopeList)
                .stream().map((item) -> item.getSkuId()).collect(Collectors.toList());

        // 5.排除分销商品
        // 现在分销商品可以使用优惠券了
       /* DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        List<String> distriSkuIds = tradeItems.stream()
                .filter(item -> {
                    DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(item.getStoreId().toString());
                    return DefaultFlag.YES.equals(openFlag)
                            && DefaultFlag.YES.equals(storeOpenFlag)
                            && DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit());
                })
                .map(TradeItemInfo::getSkuId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(distriSkuIds)) {
            goodsInfoIds = goodsInfoIds.stream()
                    .filter(skuId -> !distriSkuIds.contains(skuId)).collect(Collectors.toList());
        }*/

        // 6.如果优惠券没有关联下单商品
        if(CollectionUtils.isEmpty(goodsInfoIds)) {
            throw new SbcRuntimeException(CouponErrorCode.CUSTOMER_ORDER_COUPON_INVALID);
        }

        TradeMarketingResponse response = new TradeMarketingResponse();
        response.setTradeCoupon(TradeCoupon.builder()
                .couponCodeId(couponCode.getCouponCodeId())
                .couponCode(couponCode.getCouponCode())
                .goodsInfoIds(goodsInfoIds)
                .discountsAmount(couponInfo.getDenomination())
                .couponType(couponInfo.getCouponType())
                .fullBuyPrice(couponInfo.getFullBuyPrice()).build());
        return response;
    }

    /**
     * 获取优惠券描述
     *
     * @return
     */
    private String getLabelMap(CouponCache coupon) {
        if (coupon.getCouponInfo().getFullBuyType() == FullBuyType.FULL_MONEY) {
            return String.format("满%s减%s", coupon.getCouponInfo().getFullBuyPrice().intValue(), coupon.getCouponInfo().getDenomination().intValue());
        } else {
            return String.format("直减%s", coupon.getCouponInfo().getDenomination().intValue());
        }
    }


}