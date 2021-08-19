package com.wanmi.sbc.order.returnorder.service;

import com.aliyuncs.linkedmall.model.v20180116.ApplyRefundRequest;
import com.aliyuncs.linkedmall.model.v20180116.InitApplyRefundResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryOrderListResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.linkedmall.api.provider.goods.LinkedMallGoodsQueryProvider;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderProvider;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailByIdRequest;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderListQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcApplyRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcInitApplyRefundRequest;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderApplyRequest;
import com.wanmi.sbc.order.api.response.linkedmall.LinkedMallReturnReasonResponse;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.vo.LinkedMallReasonVO;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderRepository;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.ThirdPlatformTrade;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.repository.ProviderTradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * linkedMall订单服业务处理
 *
 * @author: daiyitian
 * @createDate: 2020/08/26 18:17
 * @version: 1.0
 */
@Slf4j
@Service
public class LinkedMallReturnOrderService {

    @Autowired
    private LinkedMallTradeService linkedMallTradeService;

    @Autowired
    private LinkedMallOrderQueryProvider linkedMallOrderQueryProvider;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @Autowired
    private LinkedMallReturnOrderProvider linkedMallReturnOrderProvider;

    @Autowired
    private LinkedMallGoodsQueryProvider linkedMallGoodsQueryProvider;

    @Autowired
    private ReturnOrderRepository returnOrderRepository;

    @Autowired
    private ProviderTradeRepository providerTradeRepository;

    /**
     * 根据供应商退单处理拆分业务
     *
     * @param providerReturnOrder 供应商退单
     */
    public List<ReturnOrder> splitReturnOrder(ReturnOrder providerReturnOrder) {
        log.info("===========linkedMall退单拆分业务id：{}，开始========", providerReturnOrder.getId());
        //合并items
        List<ReturnItem> zipItems = this.zipLinkedMallItem(providerReturnOrder.getReturnItems(), providerReturnOrder.getReturnGifts());
        if(CollectionUtils.isEmpty(zipItems)){
            log.info("===========linkedMall退单不含linkedMall数据：{}========", providerReturnOrder.getId());
            return Collections.singletonList(providerReturnOrder);
        }
        List<ThirdPlatformTrade> tradeList = linkedMallTradeService.findListByParentId(providerReturnOrder.getPtid());
        if (CollectionUtils.isEmpty(tradeList)) {
            log.info("===========linkedMall退单拆分业务id：{}，没有数据========", providerReturnOrder.getId());
            return Collections.singletonList(providerReturnOrder);
        }
        String userId = null;
        if (Objects.nonNull(providerReturnOrder.getBuyer()) && StringUtils.isNotBlank(providerReturnOrder.getBuyer().getId())) {
            userId = providerReturnOrder.getBuyer().getId();
        }
        if (userId == null) {
            userId = tradeList.get(0).getBuyer().getId();
        }

        //形成Map<skuId,linkedTrade>
        List<String> thirdIds = tradeList.stream().flatMap(t -> t.getThirdPlatformOrderIds().stream()).distinct().collect(Collectors.toList());
        Map<Long, QueryOrderListResponse.LmOrderListItem> lmOrderMap = linkedMallOrderQueryProvider.queryOrderDetail(
                SbcOrderListQueryRequest.builder().lmOrderList(thirdIds)
                        .pageNum(1L)
                        .pageSize(20L)
                        .bizUid(userId)
                        .allFlag(Boolean.TRUE)
                        .enableStatus(-1)
                        .build()).getContext().getLmOrderListItems()
                .stream().collect(Collectors.toMap(k -> k.getLmOrderId(), i -> i));

        //形成Map<skuId,ThirdPlatformTrade>
        Map<String, ThirdPlatformTrade> skuTradeMap = new HashMap<>();
        Map<String, TradeItem> skuItemMap = new HashMap<>();
        tradeList.forEach(trade -> {
            if (CollectionUtils.isNotEmpty(trade.getTradeItems())) {
                trade.getTradeItems().forEach(i -> {
                    skuTradeMap.put(i.getSkuId(), trade);
                    skuItemMap.put(i.getSkuId(), i);
                });

            }
            if (CollectionUtils.isNotEmpty(trade.getGifts())) {
                trade.getGifts().forEach(i -> {
                    skuTradeMap.put(i.getSkuId(), trade);
                    skuItemMap.put(i.getSkuId(), i);
                });
            }
        });

        //形成<skuId,returnItem> 商品
        Map<String, ReturnItem> returnItemMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(providerReturnOrder.getReturnItems())){
            returnItemMap.putAll(providerReturnOrder.getReturnItems().stream().collect(Collectors.toMap(ReturnItem::getSkuId, c -> c)));
        }

        //形成<skuId,returnItem> 赠品
        Map<String, ReturnItem> giftItemMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(providerReturnOrder.getReturnGifts())){
            giftItemMap.putAll(providerReturnOrder.getReturnGifts().stream().collect(Collectors.toMap(ReturnItem::getSkuId, c -> c)));
        }

        List<ReturnOrder> returnOrders = new ArrayList<>();
        for(ReturnItem item: zipItems){
            ThirdPlatformTrade trade = skuTradeMap.get(item.getSkuId());
            TradeItem tradeItem = skuItemMap.get(item.getSkuId());
            if (trade == null || tradeItem == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            QueryOrderListResponse.LmOrderListItem lmOrder = lmOrderMap.get(NumberUtils.toLong(trade.getThirdPlatformOrderIds().get(0)));
            ReturnOrder thirdReturnOrder = KsBeanUtil.convert(providerReturnOrder, ReturnOrder.class);
            //填装普通sku
            List<ReturnItem> returnItems = new ArrayList<>();
            ReturnItem returnItem = returnItemMap.get(item.getSkuId());
            if(returnItem != null){
                returnItem.setThirdPlatformSpuId(tradeItem.getThirdPlatformSpuId());
                returnItem.setThirdPlatformSkuId(tradeItem.getThirdPlatformSkuId());
                returnItem.setGoodsSource(GoodsSource.LINKED_MALL.toValue());
                returnItem.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                //根据skuId和spuId 设置相应的subLmOrderid
                if (Objects.nonNull(lmOrder) && CollectionUtils.isNotEmpty(lmOrder.getSubOrderList())) {
                    lmOrder.getSubOrderList().stream().filter(s -> Objects.toString(s.getItemId()).equals(tradeItem.getThirdPlatformSpuId())
                            && Objects.toString(s.getSkuId()).equals(tradeItem.getThirdPlatformSkuId())).findFirst()
                            .ifPresent(sub -> returnItem.setThirdPlatformSubOrderId(Objects.toString(sub.getLmOrderId(), null)));
                }
                returnItems.add(returnItem);
            }
            thirdReturnOrder.setReturnItems(returnItems);

            //填装赠品
            List<ReturnItem> returnGifts = new ArrayList<>();
            ReturnItem returnGift = giftItemMap.get(item.getSkuId());
            if(returnGift != null){
                returnGift.setThirdPlatformSpuId(tradeItem.getThirdPlatformSpuId());
                returnGift.setThirdPlatformSkuId(tradeItem.getThirdPlatformSkuId());
                returnGift.setGoodsSource(GoodsSource.LINKED_MALL.toValue());
                returnGift.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                //根据skuId和spuId 设置相应的subLmOrderid
                if (Objects.nonNull(lmOrder) && CollectionUtils.isNotEmpty(lmOrder.getSubOrderList())) {
                    lmOrder.getSubOrderList().stream().filter(s -> Objects.toString(s.getItemId()).equals(tradeItem.getThirdPlatformSpuId())
                            && Objects.toString(s.getSkuId()).equals(tradeItem.getThirdPlatformSkuId())).findFirst()
                            .ifPresent(sub -> returnGift.setThirdPlatformSubOrderId(Objects.toString(sub.getLmOrderId(), null)));
                }
                returnGifts.add(returnGift);
            }
            thirdReturnOrder.setReturnGifts(returnGifts);
            thirdReturnOrder.setThirdPlatformTradeId(trade.getId());
            thirdReturnOrder.setThirdPlatformOrderId(trade.getThirdPlatformOrderIds().get(0));
            thirdReturnOrder.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
            thirdReturnOrder.setThirdSellerId(trade.getThirdSellerId());
            thirdReturnOrder.setThirdSellerName(trade.getThirdSellerName());
            thirdReturnOrder.setThirdPlatformPayErrorFlag(trade.getThirdPlatformPayErrorFlag());
            thirdReturnOrder.setOutOrderId(CollectionUtils.isNotEmpty(trade.getOutOrderIds())?trade.getOutOrderIds().get(0):null);
            returnOrders.add(thirdReturnOrder);
        }
        log.info("===========linkedMall退单拆分退单数量：{}，结束========", returnOrders.size());
        return returnOrders;
    }

    /**
     * 查询原因列表
     * @param rid
     * @return
     */
    public LinkedMallReturnReasonResponse reasons(String rid) {
        LinkedMallReturnReasonResponse response = new LinkedMallReturnReasonResponse();
        ReturnOrder returnOrder = returnOrderRepository.findById(rid).orElse(null);
        if (Objects.isNull(returnOrder) || (!ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()))) {
            return response;
        }
        ProviderTrade trade = providerTradeRepository.findFirstById(returnOrder.getPtid());
        if (Objects.isNull(trade)) {
            return response;
        }

        //如果普通商品id不存在，则取赠品id
        ReturnItem item = null;
        if(CollectionUtils.isNotEmpty(returnOrder.getReturnItems())){
            item = returnOrder.getReturnItems().get(0);
        }else if(CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())){
            item = returnOrder.getReturnGifts().get(0);
        }
        if(item == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SbcInitApplyRefundRequest refundRequest = new SbcInitApplyRefundRequest();
        refundRequest.setSubLmOrderId(item.getThirdPlatformSubOrderId());
        refundRequest.setBizUid(trade.getBuyer().getId());
        if (ReturnType.REFUND.equals(returnOrder.getReturnType())) {
            refundRequest.setBizClaimType(1);
        } else {
            refundRequest.setBizClaimType(3);
        }
        //设定商品收货状态
        refundRequest.setGoodsStatus(chgGoodsStatus(trade, refundRequest.getBizClaimType()));
        List<InitApplyRefundResponse.InitApplyRefundData.RefundReasonListItem> items = linkedMallReturnOrderQueryProvider.initApplyRefund(refundRequest).getContext().getRefundReasonList();
        if (CollectionUtils.isNotEmpty(items)) {
            response.setReasonList(items.stream().map(i -> {
                LinkedMallReasonVO reason = new LinkedMallReasonVO();
                reason.setReasonTextId(i.getReasonTextId());
                reason.setReasonTips(i.getReasonTips());
                reason.setProofRequired(i.getProofRequired());
                reason.setRefundDescRequired(i.getRefundDescRequired());
                return reason;
            }).collect(Collectors.toList()));
        }
        response.setDescription(returnOrder.getDescription());
        response.setImages(returnOrder.getImages());
        return response;
    }

    /**
     * linkedMall申请
     * @param request
     */
    @Transactional
    public void apply(LinkedMallReturnOrderApplyRequest request) {
        ReturnOrder returnOrder = returnOrderRepository.findById(request.getRid()).orElse(null);
        if (Objects.isNull(returnOrder) || (!ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ProviderTrade trade = providerTradeRepository.findFirstById(returnOrder.getPtid());
        if (Objects.isNull(trade)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ReturnItem item = this.zipLinkedMallItem(returnOrder.getReturnItems(), returnOrder.getReturnGifts()).get(0);
        SbcApplyRefundRequest refundRequest = new SbcApplyRefundRequest();
        refundRequest.setSubLmOrderId(item.getThirdPlatformSubOrderId());
        refundRequest.setBizUid(trade.getBuyer().getId());
        if (ReturnType.REFUND.equals(returnOrder.getReturnType())) {
            refundRequest.setBizClaimType(1);
        } else {
            refundRequest.setBizClaimType(3);
        }
        BigDecimal price = item.getProviderPrice();
        if(price == null) {
            if (Objects.nonNull(item.getSupplyPrice())) {
                price = item.getSupplyPrice().multiply(BigDecimal.valueOf(item.getNum()));
            } else if (StringUtils.isNotBlank(item.getThirdPlatformSpuId())) {
                //远程查第三方平台供货价
                GoodsDetailByIdRequest goodsRequest = new GoodsDetailByIdRequest();
                goodsRequest.setProviderGoodsId(NumberUtils.toLong(item.getThirdPlatformSpuId()));
                QueryItemDetailResponse.Item lmSpu = linkedMallGoodsQueryProvider.getGoodsDetailById(goodsRequest).getContext();
                if (lmSpu == null) {
                    throw new SbcRuntimeException(CommonErrorCode.THIRD_SERVICE_ERROR);
                }
                QueryItemDetailResponse.Item.Sku sku = lmSpu.getSkus().stream()
                        .filter(k -> k.getSkuId().equals(NumberUtils.toLong(item.getThirdPlatformSkuId()))).findFirst()
                        .orElse(null);
                if (sku == null) {
                    price = BigDecimal.ZERO;
                } else {
                    price = BigDecimal.valueOf(sku.getPriceCent()).multiply(BigDecimal.valueOf(item.getNum())).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
                }
            } else {
                price = BigDecimal.ZERO;
            }
        }
        refundRequest.setApplyRefundFee(price.multiply(BigDecimal.valueOf(100)).longValue());//单位：分
        refundRequest.setApplyReasonTextId(request.getReasonTextId());
        refundRequest.setLeaveMessage(request.getLeaveMessage());
        //凭证图片
        if(CollectionUtils.isNotEmpty(request.getImages())){
            refundRequest.setLeavePictureList(request.getImages().stream().map(i -> {
                ApplyRefundRequest.LeavePictureList pic = new ApplyRefundRequest.LeavePictureList();
                pic.setPicture(i);
                pic.setDesc(request.getLeaveMessage());
                return pic;
            }).collect(Collectors.toList()));
        }
        //设定商品收货状态
        refundRequest.setGoodsStatus(chgGoodsStatus(trade, refundRequest.getBizClaimType()));
        linkedMallReturnOrderProvider.applyRefund(refundRequest).getContext().getDisputeStatus();
        returnOrder.setThirdReasonId(request.getReasonTextId());
        returnOrder.setThirdReasonTips(request.getReasonTips());
        returnOrderRepository.save(returnOrder);
    }

    /**
     * 转换商品状态
     * @param trade 订单
     * @param bizClaimType 退款类型
     * @return 1.未收到货 2.已收到货 3.已寄回 4.未发货 5.卖家确认收货  6.已发货
     */
    private int chgGoodsStatus(ProviderTrade trade, int bizClaimType){
        int goodsStatus = 4;
        TradeState state = trade.getTradeState();
        if (FlowState.COMPLETED.equals(state.getFlowState())) {
            goodsStatus = 2; //买家确认收货
            if (CollectionUtils.isNotEmpty(trade.getTradeEventLogs())) {
                //当完成事件判断操作人id与买家id不一致时，就是卖家确认收货
                if(trade.getTradeEventLogs().stream().anyMatch(s -> FlowState.COMPLETED.getDescription().equals(s.getEventType())
                        && s.getOperator() != null
                        && (!trade.getBuyer().getId().equals(s.getOperator().getUserId())))){
                    //卖家确认收货
                    goodsStatus = 5;
                }
            }
        } else if (DeliverStatus.SHIPPED.equals(state.getDeliverStatus())) {
            goodsStatus = 6;
            if (1 == bizClaimType) {
                goodsStatus = 1;
            }
        }
        return goodsStatus;
    }


    /**
     * 合并退单明细数据
     * @param tradeItems 普通商品
     * @param gifts 赠品
     * @return 新合并数据
     */
    private List<ReturnItem> zipLinkedMallItem(List<ReturnItem> tradeItems, List<ReturnItem> gifts) {
        List<ReturnItem> items = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeItems)) {
            items.addAll(tradeItems.stream()
                    .filter(i -> Objects.equals(ThirdPlatformType.LINKED_MALL, i.getThirdPlatformType()))
                    .map(i -> {
                        ReturnItem item = new ReturnItem();
                        item.setSkuId(i.getSkuId());
                        item.setThirdPlatformType(i.getThirdPlatformType());
                        item.setThirdPlatformSpuId(i.getThirdPlatformSpuId());
                        item.setThirdPlatformSkuId(i.getThirdPlatformSkuId());
                        item.setThirdPlatformSubOrderId(i.getThirdPlatformSubOrderId());
                        item.setNum(i.getNum());
                        item.setSupplyPrice(i.getSupplyPrice());
                        return item;
                    }).collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(gifts)) {
            List<ReturnItem> giftItems = gifts.stream()
                    .filter(i -> Objects.equals(ThirdPlatformType.LINKED_MALL, i.getThirdPlatformType()))
                    .map(i -> {
                        ReturnItem item = new ReturnItem();
                        item.setSkuId(i.getSkuId());
                        item.setThirdPlatformType(i.getThirdPlatformType());
                        item.setThirdPlatformSpuId(i.getThirdPlatformSpuId());
                        item.setThirdPlatformSkuId(i.getThirdPlatformSkuId());
                        item.setThirdPlatformSubOrderId(i.getThirdPlatformSubOrderId());
                        item.setNum(i.getNum());
                        item.setSupplyPrice(i.getSupplyPrice());
                        return item;
                    }).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(items)) {
                items = IteratorUtils.zip(items, giftItems,
                        (a, b) -> a.getSkuId().equals(b.getSkuId()),
                        (a, b) -> {
                            a.setNum(a.getNum() + b.getNum());
                            if (StringUtils.isBlank(a.getThirdPlatformSubOrderId())) {
                                a.setThirdPlatformSubOrderId(b.getThirdPlatformSubOrderId());
                            }
                            if (a.getProviderPrice() != null && b.getProviderPrice() != null) {
                                a.setProviderPrice(a.getProviderPrice().add(b.getProviderPrice()));
                            }
                        });
            } else {
                items.addAll(giftItems);
            }
        }
        return items;
    }
}
