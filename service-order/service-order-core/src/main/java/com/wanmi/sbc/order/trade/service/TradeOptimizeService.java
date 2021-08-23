package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointCancelRequest;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleMergeInProgressRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleMergeInProcessResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.order.api.request.trade.TradeBatchDeliverRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.LogisticsDTO;
import com.wanmi.sbc.order.bean.dto.TradeBatchDeliverDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.trade.model.entity.TradeCommitResult;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeGrouponCommitForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.root.*;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.request.TradeWrapperListRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单优化版本
 *
 * @author wanggang
 */
@Slf4j
@Service
public class TradeOptimizeService {

    /**
     * 秒杀活动抢购商品订单类型："FLASH_SALE"
     */
    public static final String FLASH_SALE_GOODS_ORDER_TYPE = "FLASH_SALE";

    @Autowired
    private TradeItemService tradeItemService;

    @Autowired
    private TradeItemSnapshotService tradeItemSnapshotService;

    @Autowired
    private TradeMarketingService tradeMarketingService;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private TradeCustomerService tradeCustomerService;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeGroupService tradeGroupService;

    @Autowired
    private TradeGoodsService tradeGoodsService;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private ExternalProvider externalProvider;
    /**
     * C端下单
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> commit(TradeCommitRequest tradeCommitRequest) {

        // 验证用户
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(tradeCommitRequest.getOperator().getUserId());
        tradeCommitRequest.setCustomer(customer);

        Operator operator = tradeCommitRequest.getOperator();
        TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(tradeCommitRequest.getTerminalToken());
        if (tradeItemSnapshot == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_050201);
        }
        List<TradeItemGroup> tradeItemGroups = tradeItemSnapshot.getItemGroups();

        // 组合购标记
        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE, s.getSuitMarketingFlag()));
        //开店礼包
        Boolean storeBagsFlag = tradeItemGroups.stream().anyMatch(s -> DefaultFlag.YES.equals(s.getStoreBagsFlag()));

        //拼团订单
        Boolean isGrouponOrder = tradeItemGroups.stream().anyMatch(s -> Objects.nonNull(s.getGrouponForm()) && Objects.nonNull(s.getGrouponForm().getOpenGroupon()));

        //积分价商品
        Boolean isBuyPoint = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream())
                .anyMatch(i -> Objects.nonNull(i.getBuyPoint()) && i.getBuyPoint() > 0);

        // 预售商品
        boolean isBookingSaleGoods = tradeItemGroups.stream().flatMap(s -> s.getTradeItems().stream()).anyMatch(s -> Objects.equals(Boolean.TRUE, s.getIsBookingSaleGoods()));
        // 预约
        boolean isAppointmentSaleGoods = tradeItemGroups.stream().flatMap(s -> s.getTradeItems().stream()).anyMatch(s -> Objects.equals(Boolean.TRUE, s.getIsAppointmentSaleGoods()));

        Boolean isCommonGoods = ChannelType.PC_MALL.equals(tradeCommitRequest.getDistributeChannel().getChannelType())
                || suitMarketingFlag || isBookingSaleGoods || isAppointmentSaleGoods;
        // 如果为PC商城下单，将分销商品变为普通商品
        if (isCommonGoods || isGrouponOrder) {
            tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).forEach(tradeItem -> {
                tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
            });
        }

        List<TradeItem> tradeItemVOS = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).collect(Collectors.toList());
        String snapshotType = tradeItemGroups.get(0).getSnapshotType();
        List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS = Lists.newArrayList();
        CustomerSimplifyVO customerSimplifyVO = new CustomerSimplifyVO();
        //非组合购、非开店礼包、非拼团订单、非抢购订单
        if (!suitMarketingFlag && !storeBagsFlag && !isGrouponOrder && !FLASH_SALE_GOODS_ORDER_TYPE.equals(snapshotType)) {
            TradeItemGroup tradeItemGroupVOS = new TradeItemGroup();
            tradeItemGroupVOS.setTradeItems(tradeItemVOS);
            tradeGoodsService.validateRestrictedGoods(tradeItemGroupVOS, customer);
        }
//        AppointmentSaleInProcessResponse response = null;
        List<String> allSkuIds = Lists.newArrayList();
        if (!suitMarketingFlag && !isGrouponOrder) {
            allSkuIds = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream())
                    .filter(i -> Objects.isNull(i.getBuyPoint()) || i.getBuyPoint() == 0)
                    .map(TradeItem::getSkuId).collect(Collectors.toList());

//            if (CollectionUtils.isNotEmpty(allSkuIds)) {
//                response =
//                        appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList(
//                                AppointmentSaleInProgressRequest.builder().goodsInfoIdList(allSkuIds)
//                                        .build()).getContext();
//            }

        }
        List<String> bookingSaleGoodsInfoIds = Lists.newArrayList();
        Map<String, Long> skuIdAndBookSaleIdMap = Maps.newHashMap();
        if (isBookingSaleGoods) {
            List<TradeItem> bookingSaleGoodsTradeItem = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream())
                    .filter(tradeItem -> tradeItem.getIsBookingSaleGoods()).collect(Collectors.toList());

            bookingSaleGoodsTradeItem.stream().forEach(tradeItem -> {
                if (StringUtils.isEmpty(tradeCommitRequest.getTailNoticeMobile()) && tradeItem.getBookingType() == BookingType.EARNEST_MONEY) {
                    throw new SbcRuntimeException("K-000009");
                }
            });
            bookingSaleGoodsInfoIds = bookingSaleGoodsTradeItem.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
            skuIdAndBookSaleIdMap = bookingSaleGoodsTradeItem.stream().collect(Collectors.toMap(TradeItem::getSkuId, TradeItem::getBookingSaleId));
        }
        //增加预售活动过期校验
        // tradeGoodsService.validateBookingQualification(tradeItemGroups, tradeCommitRequest);

        List<String> skuIds = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).map(TradeItem::getSkuId).collect(Collectors.toList());

        skuIds.addAll(tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeMarketingList().stream())
                .filter(tradeMarketingDTO -> CollectionUtils.isNotEmpty(tradeMarketingDTO.getMarkupSkuIds()))
                .flatMap(tradeMarketingDTO->tradeMarketingDTO.getMarkupSkuIds().stream())
                .collect(Collectors.toList()));
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoViewByIdsResponse.getGoodsInfos();

        //校验是商品是否参加了预约，预售活动
        if (!suitMarketingFlag && !isGrouponOrder && !isBuyPoint && !storeBagsFlag && !FLASH_SALE_GOODS_ORDER_TYPE.equals(snapshotType)) {
            List<String> needValidSkuIds = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream())
                    .filter(i -> Objects.nonNull(i.getIsAppointmentSaleGoods()) && !i.getIsAppointmentSaleGoods()
                            && Objects.nonNull(i.getIsBookingSaleGoods()) && !i.getIsBookingSaleGoods())
                    .map(TradeItem::getSkuId).collect(Collectors.toList());
            //积分价商品不需要校验
            needValidSkuIds.removeAll(goodsInfoVOList.stream()
                    .filter(goodsInfoVO -> Objects.nonNull(goodsInfoVO.getBuyPoint()) && goodsInfoVO.getBuyPoint() > 0)
                    .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
//            if (CollectionUtils.isNotEmpty(needValidSkuIds)) {
//                appointmentSaleQueryProvider.containAppointmentSaleAndBookingSale(AppointmentSaleInProgressRequest.builder().goodsInfoIdList(needValidSkuIds).build());
//            }
            AppointmentSaleMergeInProgressRequest mergeInProgressRequest = AppointmentSaleMergeInProgressRequest.builder()
                    .appointSaleGoodsInfoIds(allSkuIds).needValidSkuIds(needValidSkuIds).bookingSaleGoodsInfoIds(bookingSaleGoodsInfoIds)
                    .skuIdAndBookSaleIdMap(skuIdAndBookSaleIdMap).goodsRestrictedValidateVOS(goodsRestrictedValidateVOS)
                    .customerVO(customerSimplifyVO).build();
            AppointmentSaleMergeInProcessResponse mergeInProcessResponse = appointmentSaleQueryProvider.mergeVaildAppointmentSaleAndBookingSale(mergeInProgressRequest).getContext();

            if (Objects.nonNull(mergeInProcessResponse)) {
                List<String> appointmentSaleSkuIds = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream())
                        .filter(i -> Objects.nonNull(i.getIsAppointmentSaleGoods()) && i.getIsAppointmentSaleGoods())
                        .map(TradeItem::getSkuId).collect(Collectors.toList());
                //预约活动校验是否有资格
                tradeGoodsService.validateAppointmentQualification(mergeInProcessResponse, appointmentSaleSkuIds.size(), customer.getCustomerId());
            }

            if (isBookingSaleGoods) {
                tradeCommitRequest.setIsBookingSaleGoods(Boolean.TRUE);
            }

        }

        // 拼团订单--验证
        TradeGrouponCommitForm grouponForm = tradeItemGroups.get(NumberUtils.INTEGER_ZERO).getGrouponForm();
        if (Objects.nonNull(grouponForm)) {
            GrouponGoodsInfoVO grouponGoodsInfo = tradeMarketingService.validGroupon(tradeCommitRequest, tradeItemGroups);
            grouponForm.setGrouponActivityId(grouponGoodsInfo.getGrouponActivityId());
            grouponForm.setLimitSellingNum(grouponGoodsInfo.getLimitSellingNum());
            grouponForm.setGrouponPrice(grouponGoodsInfo.getGrouponPrice());
        }

        // 1.查询快照中的购物清单
        // list转map,方便获取
        Map<Long, TradeItemGroup> tradeItemGroupsMap = tradeItemGroups.stream().collect(
                Collectors.toMap(g -> g.getSupplier().getStoreId(), Function.identity()));

        List<StoreVO> storeVOList = tradeCacheService.queryStoreList(new ArrayList<>(tradeItemGroupsMap.keySet()));

        Map<Long, CommonLevelVO> storeLevelMap = tradeCustomerService.listCustomerLevelMapByCustomerIdAndIds(
                new ArrayList<>(tradeItemGroupsMap.keySet()), customer.getCustomerId());


        // 1.验证失效的营销信息(目前包括失效的赠品、满系活动、优惠券)
        verifyService.verifyInvalidMarketings(tradeCommitRequest, tradeItemGroups, storeLevelMap);
        //校验周期购
        verifyCycleBuy(tradeItemGroups, goodsInfoVOList,tradeCommitRequest.isForceCommit());

        // 处理加价购加购商品
        tradeService.wrapperMarkup(tradeItemGroups,goodsInfoVOList,tradeCommitRequest.isForceCommit());
        // 校验组合购活动信息
        // 2.按店铺包装多个订单信息、订单组信息
        TradeWrapperListRequest tradeWrapperListRequest = new TradeWrapperListRequest();
        tradeWrapperListRequest.setStoreLevelMap(storeLevelMap);
        tradeWrapperListRequest.setStoreVOList(storeVOList);
        tradeWrapperListRequest.setTradeCommitRequest(tradeCommitRequest);
        tradeWrapperListRequest.setTradeItemGroups(tradeItemGroups);
        List<Trade> trades = tradeService.wrapperTradeList(tradeWrapperListRequest);

        TradeGroup tradeGroup = tradeGroupService.wrapperTradeGroup(trades, tradeCommitRequest, grouponForm);
        if (suitMarketingFlag) {
            trades.parallelStream().flatMap(trade -> trade.getTradeItems().stream()).forEach(tradeItem -> {
                tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
                tradeItem.setBuyKnowledge(NumberUtils.LONG_ZERO);
            });
        }
        // 3.批量提交订单
        List<TradeCommitResult> successResults;
        try {
        // 处理积分抵扣
        tradeService.dealPoints(trades, tradeCommitRequest);
        // 处理知豆抵扣
        tradeService.dealKnowledge(trades, tradeCommitRequest);
        // 预售补充尾款价格
        tradeService.dealTailPrice(trades, tradeCommitRequest);

        if (tradeGroup != null) {
            successResults = tradeService.createBatchWithGroup(trades, tradeGroup, operator);
        } else {
            successResults = tradeService.createBatch(trades, null, operator);
        }
        }catch (Exception e){
            log.error("提交订单异常：{}",e);
            for (Trade trade : trades) {
                if (StringUtils.isNotBlank(trade.getDeductCode())) {
                    log.error("提交订单异常补偿取消订单积分对象：{}", trade);
                    if (tradeCommitRequest.getPoints() != null && tradeCommitRequest.getPoints() > 0) {
                        //释放积分接口
                        externalProvider.pointCancel(FanDengPointCancelRequest.builder().deductCode(trade.getDeductCode())
                                .desc("订单提交异常返还(退单号:" + trade.getId() + ")").build());
                    }
                    //释放知豆接口
                    if (tradeCommitRequest.getKnowledge() != null && tradeCommitRequest.getKnowledge() > 0) {
                        //释放积分接口
                        externalProvider.knowledgeCancel(FanDengPointCancelRequest.builder().deductCode(trade.getDeductCode())
                                .desc("订单提交异常返还(退单号:" + trade.getId() + ")").build());
                    }
                }
            }
            throw  new SbcRuntimeException("K-000001");
        }
        try {
            // 4.订单提交成功，且为购物车购买，删除关联的采购单商品
            if (Boolean.TRUE.equals(tradeItemSnapshot.getPurchaseBuy())) {
                trades.forEach(
                        trade -> {
                            List<String> tradeSkuIds =
                                    trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
                            tradeService.deletePurchaseOrder(customer.getCustomerId(), tradeSkuIds,
                                    tradeCommitRequest.getDistributeChannel());
                        }
                );
            }
            // 5.订单提交成功，删除订单商品快照
            tradeItemService.remove(tradeCommitRequest.getTerminalToken());
            // 6.订单提交成功，增加限售记录
            tradeService.insertRestrictedRecord(trades);
        } catch (Exception e) {
            log.error("Delete the trade sku list snapshot or the purchase order exception," +
                            "trades={}," +
                            "customer={}",
                    JSONObject.toJSONString(trades),
                    customer,
                    e
            );
        }
        return successResults;
    }



    /**
     * 批量发货
     */
    @Transactional
    public void batchDeliver(TradeBatchDeliverRequest batchDeliverRequest) {
        Operator operator = batchDeliverRequest.getOperator();
        List<TradeBatchDeliverDTO> batchDeliverDTOList = batchDeliverRequest.getBatchDeliverDTOList();
        batchDeliverDTOList.forEach(batchDeliverDTO -> {
            String tid = batchDeliverDTO.getTid();
            TradeDeliver tradeDeliver = new TradeDeliver();
            if (Platform.SUPPLIER == operator.getPlatform()){
                Long storeId = Long.valueOf(operator.getStoreId());
                Trade trade = tradeService.detail(tid);
                this.wrapTradeDeliverInfo(trade, tradeDeliver, batchDeliverDTO);

                //主单有着下属子单，则tradeDeliver部分信息进行重置，增加子单发货步骤
                List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(tid);
                Optional<ProviderTrade> providerTrade = providerTradeList.stream().filter(v -> v.getSupplier().getStoreId().equals(storeId)).findFirst();
                providerTrade.ifPresent(v -> {
                    String deliverId = providerTradeService.dealBatchDeliver(v, tradeDeliver, operator);
                    tradeDeliver.setSunDeliverId(deliverId);
                });
                tradeService.deliver(tid, tradeDeliver, operator, BoolFlag.YES);
            }else {
                //供应商子单发货
                ProviderTrade providerTrade = providerTradeService.providerDetail(tid);
                tradeDeliver.setDeliverId(generatorService.generate("TD"));
                tradeDeliver.setLogistics(KsBeanUtil.copyPropertiesThird(batchDeliverDTO.getLogistics(), Logistics.class));
                tradeDeliver.setDeliverTime(batchDeliverDTO.getDeliverTime());
                tradeDeliver.setStatus(DeliverStatus.SHIPPED);
                tradeDeliver.setShipperType(ShipperType.PROVIDER);
                tradeDeliver.setProviderName(providerTrade.getSupplier().getSupplierName());
                String deliverId = providerTradeService.dealBatchDeliver(providerTrade, tradeDeliver, operator);

                tradeDeliver.setSunDeliverId(deliverId);
                tradeDeliver.setTradeId(providerTrade.getParentId());
                tradeDeliver.setShipperType(ShipperType.SUPPLIER);
                try {
                    tradeService.deliver(providerTrade.getParentId(), tradeDeliver, operator,  BoolFlag.YES);
                }catch (SbcRuntimeException e){
                    log.error("批量发货失败>>>>>子单号：{}>>>>>错误信息：{}", tid, e);
                    if ("K-050114".equals(e.getErrorCode()))
                        throw new SbcRuntimeException("K-050114", new Object[]{tid});
                    else
                        throw e;
                }catch (Exception e){
                    log.error("批量发货失败>>>>>子单号：{}>>>>>错误信息：{}", tid, e);
                    throw e;
                }
            }
        });
    }

    /**
     * 组装物发货单信息
     */
    public void wrapTradeDeliverInfo(Trade trade, TradeDeliver tradeDeliver, TradeBatchDeliverDTO batchDeliverDTO) {
        List<TradeItem> tradeItems = trade.getTradeItems();
        List<ShippingItem> shippingItems = tradeItems.stream().map(item -> {
            //默认全部发货
            item.setDeliveredNum(item.getNum());
            item.setDeliverStatus(DeliverStatus.SHIPPED);
            ShippingItem shippingItem = KsBeanUtil.copyPropertiesThird(item, ShippingItem.class);
            shippingItem.setItemName(item.getSkuName());
            shippingItem.setItemNum(item.getNum());
            return shippingItem;
        }).collect(Collectors.toList());
        List<ShippingItem> giftItems = trade.getGifts().stream().map(item -> {
            item.setDeliveredNum(item.getNum());
            item.setDeliverStatus(DeliverStatus.SHIPPED);
            ShippingItem shippingItem = KsBeanUtil.copyPropertiesThird(item, ShippingItem.class);
            shippingItem.setItemName(item.getSkuName());
            shippingItem.setItemNum(item.getNum());
            return shippingItem;
        }).collect(Collectors.toList());
        //组装发货单
        tradeDeliver.setTradeId(trade.getId());
        tradeDeliver.setDeliverId(generatorService.generate("TD"));
        tradeDeliver.setLogistics(KsBeanUtil.copyPropertiesThird(batchDeliverDTO.getLogistics(), Logistics.class));
        tradeDeliver.setShippingItems(shippingItems);
        tradeDeliver.setShipperType(ShipperType.SUPPLIER);
        tradeDeliver.setDeliverTime(batchDeliverDTO.getDeliverTime());
        tradeDeliver.setConsignee(trade.getConsignee());
        tradeDeliver.setGiftItemList(giftItems);
        tradeDeliver.setStatus(DeliverStatus.SHIPPED);
        tradeDeliver.setProviderName(trade.getSupplier().getSupplierName());
    }

    /**
     * 根据物流单号，物流公司判断是否重复
     */
    public List<String> verifyLogisticNo(List<LogisticsDTO> logisticsDTOList) {
        Map<String, String> logisticsMap = logisticsDTOList.stream().collect(Collectors.toMap(LogisticsDTO::getLogisticNo, LogisticsDTO::getLogisticStandardCode));

        List<String> logisticNoList = logisticsDTOList.stream().map(LogisticsDTO::getLogisticNo).collect(Collectors.toList());
        List<Trade> trades = tradeService.queryAll(TradeQueryRequest.builder().logisticNos(logisticNoList).build());

        List<TradeDeliver> tradeDelivers = new ArrayList<>();
        trades.forEach(v -> tradeDelivers.addAll(v.getTradeDelivers()));

        return tradeDelivers.stream().filter(v -> logisticsMap.containsKey(v.getLogistics().getLogisticNo())
                && Objects.equals(v.getLogistics().getLogisticStandardCode(), logisticsMap.get(v.getLogistics().getLogisticNo())))
                .map(v -> v.getLogistics().getLogisticNo())
                .collect(Collectors.toList());
    }

    /**
     * 周期购
     * @param tradeItemGroups
     * @param goodsInfoVOList
     */
    public void verifyCycleBuy(List<TradeItemGroup> tradeItemGroups, List<GoodsInfoVO> goodsInfoVOList,boolean forceCommit){

        TradeItemGroup tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        TradeItem tradeItem = tradeItemGroup.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        CycleBuyInfoDTO cycleBuyInfo = tradeItemGroup.getCycleBuyInfo();
        if(Objects.nonNull(cycleBuyInfo)) {
            Optional<GoodsInfoVO> optional = goodsInfoVOList.stream().filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(tradeItem.getSkuId())).findFirst();
            if(!optional.isPresent()) {
                throw new SbcRuntimeException("K-050117");
            }
            verifyService.verifyCycleBuy(optional.get().getGoodsId(), cycleBuyInfo.getCycleBuyGifts(), null, null, null,forceCommit);
        }
    }
}