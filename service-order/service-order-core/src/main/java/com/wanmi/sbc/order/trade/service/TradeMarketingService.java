package com.wanmi.sbc.order.trade.service;


import com.sun.xml.bind.v2.util.CollisionCheckStack;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingCouponPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingCouponWrapperRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingCouponWrapperResponse;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.groupon.service.GrouponOrderService;
import com.wanmi.sbc.order.trade.model.entity.GrouponTradeValid;
import com.wanmi.sbc.order.trade.model.entity.TradeGrouponCommitForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 存放订单与营销服务相关的接口方法
 * @author wanggang
 */
@Service
public class TradeMarketingService {

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private MarketingCouponPluginProvider marketingCouponPluginProvider;

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private ClassifyProvider classifyProvider;

    /**
     * 调用营销插件，构造订单优惠券对象
     * @return
     */
    public TradeCouponVO buildTradeCouponInfo(List<TradeItem> tradeItems, String couponCodeId,
                                               boolean forceCommit, String customerId) {

        // 1.查询tradeItems的storeCateIds
        List<String> goodsIds = tradeItems.stream()
                .map(TradeItem::getSpuId).distinct().collect(Collectors.toList());
        Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(goodsIds).getContext();

        // 2.构建营销插件请求对象
        List<TradeItemInfoDTO> tradeItemInfos = tradeItems.stream().map(t -> {
            TradeItemInfoDTO.TradeItemInfoDTOBuilder tradeItemInfoDTOBuilder = TradeItemInfoDTO.builder()
                    .num(t.getNum())
                    .price(t.getPrice())
                    .skuId(t.getSkuId())
                    .cateId(t.getCateId())
                    .storeId(t.getStoreId())
                    .brandId(t.getBrand())
                    .distributionGoodsAudit(t.getDistributionGoodsAudit());
            if(storeCateIdMap.get(t.getSpuId()) != null){
                tradeItemInfoDTOBuilder.storeCateIds(storeCateIdMap.get(t.getSpuId()).stream().map(Integer::longValue).collect(Collectors.toList()));
            }else{
                tradeItemInfoDTOBuilder.storeCateIds(new ArrayList<>());
            }
            return tradeItemInfoDTOBuilder.build();
        }).collect(Collectors.toList());
        MarketingCouponWrapperRequest request = new MarketingCouponWrapperRequest();
        request.setCustomerId(customerId);
        request.setCouponCodeId(couponCodeId);
        request.setForceCommit(forceCommit);
        request.setTradeItems(tradeItemInfos);

        // 3.调用营销插件，查询订单优惠券对象
        MarketingCouponWrapperResponse response = marketingCouponPluginProvider.wrapper(request).getContext();
        if (response != null) {
            return response.getTradeCoupon();
        }

        return null;
    }

    /**
     * 拼团订单--验证
     */
    public GrouponGoodsInfoVO validGroupon(TradeCommitRequest request, List<TradeItemGroup> tradeItemGroups) {

        if (tradeItemGroups.size() != NumberUtils.INTEGER_ONE) {
            // 拼团订单只能有一个订单
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        TradeItemGroup tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        if (tradeItemGroup.getTradeItems().size() != NumberUtils.INTEGER_ONE) {
            // 拼团订单只能有一个商品
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        TradeItem tradeItem = tradeItemGroup.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        if (CollectionUtils.isNotEmpty(tradeItemGroup.getTradeMarketingList())
                || DefaultFlag.YES.equals(tradeItemGroup.getStoreBagsFlag())
                || DistributionGoodsAudit.CHECKED.equals(tradeItem.getDistributionGoodsAudit())) {
            // 拼团单不应该具有营销活动、非开店礼包、不是分销商品
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 验证拼团主体信息
        TradeGrouponCommitForm grouponForm = tradeItemGroup.getGrouponForm();
        GrouponTradeValid validInfo = GrouponTradeValid.builder()
                .buyCount(tradeItem.getNum().intValue())
                .customerId(request.getCustomer().getCustomerId())
                .goodsId(tradeItem.getSpuId())
                .goodsInfoId(tradeItem.getSkuId())
                .grouponNo(grouponForm.getGrouponNo())
                .openGroupon(grouponForm.getOpenGroupon())
                .build();
       return grouponOrderService.validGrouponOrderBeforeCommit(validInfo);
    }

}
