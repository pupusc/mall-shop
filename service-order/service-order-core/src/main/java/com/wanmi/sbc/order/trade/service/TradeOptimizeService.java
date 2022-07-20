package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointCancelRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleMergeInProgressRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleMergeInProcessResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.bean.dto.MarketingPointBuyLevelDto;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.order.api.request.trade.TradeBatchDeliverRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradePurchaseRequest;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.LogisticsDTO;
import com.wanmi.sbc.order.bean.dto.TradeBatchDeliverDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.constant.OrderErrorCode;
import com.wanmi.sbc.order.purchase.PurchaseCacheService;
import com.wanmi.sbc.order.trade.model.entity.TradeCommitResult;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeGrouponCommitForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroup;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.model.root.TradeItemSnapshot;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CycleBuyQueryProvider cycleBuyQueryProvider;

    @Autowired
    private PurchaseCacheService purchaseCacheService;
    /**
     * C端下单
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> commit(TradeCommitRequest tradeCommitRequest) {

        if (CollectionUtils.isEmpty(tradeCommitRequest.getGoodsChannelTypeSet())) {
            throw new SbcRuntimeException("K-050215");
        }

        // 验证用户
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(tradeCommitRequest.getOperator().getUserId());
        tradeCommitRequest.setCustomer(customer);

        Operator operator = tradeCommitRequest.getOperator();
        TradeItemSnapshot tradeItemSnapshot = tradeItemSnapshotService.getTradeItemSnapshot(tradeCommitRequest.getTerminalToken());
        if (tradeItemSnapshot == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_050201);
        }

        List<TradeItemGroup> tradeItemGroups = tradeItemSnapshot.getItemGroups();
        List<TradeMarketingDTO> tradeMarketingList = tradeItemGroups.get(0).getTradeMarketingList();
        TradeMarketingDTO pointBuy = null;
        Iterator<TradeMarketingDTO> it = tradeMarketingList.iterator();
        while (it.hasNext()) {
            TradeMarketingDTO tradeMarketingDTO = it.next();
            if(tradeMarketingDTO.getMarketingSubType() != null && tradeMarketingDTO.getMarketingSubType().equals(MarketingSubType.POINT_BUY.toValue())){
                if(tradeCommitRequest.getJoinPointMarketing() != null && tradeCommitRequest.getJoinPointMarketing() == 0){
                    it.remove();
                    continue;
                }
                pointBuy = tradeMarketingDTO;
                break;
            }
        }
        // 积分换购活动积分合法性校验
        if(pointBuy != null){
            BaseResponse<MarketingPointBuyLevelDto> pointBuyLevel = marketingQueryProvider.getPointBuyLevel(pointBuy.getMarketingLevelId());
            if(pointBuyLevel.getContext() == null){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "参数错误");
            }
            tradeCommitRequest.setPoints(pointBuyLevel.getContext().getPointNeed().longValue());
        }
        // 组合购标记
        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE, s.getSuitMarketingFlag()));


        //组合购不可使用优惠券
        if(suitMarketingFlag &&  tradeItemGroups.stream().anyMatch(s -> Objects.equals(2, s.getSuitScene())) && StringUtils.isNotEmpty(tradeCommitRequest.getCommonCodeId())){
            throw new SbcRuntimeException(OrderErrorCode.COUPON_SUIT_NOT_ALLOWED);
        }
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
            //打包商品
            tradeService.dealGoodsPackDetail(trades, tradeCommitRequest, customer);
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

                log.info("************* 提交订单异常 订单 {} 作废 begin *************:", trade.getId());

                //异常订单，主单和子单全部标记作废
                if (trade.getTradeState() != null) {
                    trade.getTradeState().setFlowState(FlowState.VOID); //此处只是做标记，不做其他任何处理
                    trade.appendTradeEventLog(new TradeEventLog(operator, "订单异常，标记订单为作废状态", "订单异常，标记订单为作废状态", LocalDateTime.now()));
                    tradeService.addTrade(trade);
                }
                log.info("提交订单异常 订单：{} Trade 作废处理 完成，当前只做不退换优惠券处理，临时方案", trade.getId());
                //获取子单列表
                List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(trade.getId());
                for (ProviderTrade providerTradeParam : providerTradeList) {
                    providerTradeParam.getTradeState().setFlowState(FlowState.VOID);
                    providerTradeParam.appendTradeEventLog(new TradeEventLog(operator, "订单异常，标记订单为作废状态", "订单异常，标记订单为作废状态", LocalDateTime.now()));
                    providerTradeService.addProviderTrade(providerTradeParam);
                    log.info("提交订单异常 订单：{} 子单：{} ProviderTrade 作废处理 完成，当前只做不退换优惠券处理，临时方案", trade.getId(), providerTradeParam.getId());
                }

                log.info("************* 提交订单异常 订单 {} 作废 end *************:", trade.getId());
            }
            throw e;
        }
        try {
            // 4.订单提交成功，且为购物车购买，删除关联的采购单商品
            if (Boolean.TRUE.equals(tradeItemSnapshot.getPurchaseBuy())) {
                trades.forEach(
                        trade -> {
                            List<String> tradeSkuIds = trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
                            tradeService.deletePurchaseOrder(customer.getCustomerId(), tradeSkuIds, tradeCommitRequest.getDistributeChannel());
                            //删除购物车勾选缓存
                            purchaseCacheService.unTicks(customer.getCustomerId(), tradeSkuIds);
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

    /**
     * TODO 作废啦没有调用的地方
     * @param tradeCommitRequest
     * @return
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> commitTrade(TradeCommitRequest tradeCommitRequest) {
        // 验证用户
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(tradeCommitRequest.getOperator().getUserId());
        tradeCommitRequest.setCustomer(customer);
        Operator operator = tradeCommitRequest.getOperator();
        //商品明细传参
        if(CollectionUtils.isEmpty(tradeCommitRequest.getTradeItems())){
            throw new SbcRuntimeException("K-050214");
        }
        if (CollectionUtils.isEmpty(tradeCommitRequest.getGoodsChannelTypeSet())) {
            throw new SbcRuntimeException("K-050215");
        }


        List<TradeItemGroup> tradeItemGroups = getTradeItemList(TradePurchaseRequest.builder().customer(customer).tradeItems(tradeCommitRequest.getTradeItems()).build());
        List<TradeItem> tradeItems = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).collect(Collectors.toList());

        //商品信息
        List<String> skuIds = tradeItems.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoViewByIdsResponse.getGoodsInfos();

        // 1.查询快照中的购物清单 list转map,方便获取
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
        //tradeService.wrapperMarkup(tradeItemGroups,goodsInfoVOList,tradeCommitRequest.isForceCommit());
        // 2.按店铺包装多个订单信息、订单组信息
        TradeWrapperListRequest tradeWrapperListRequest = new TradeWrapperListRequest();
        tradeWrapperListRequest.setStoreLevelMap(storeLevelMap);
        tradeWrapperListRequest.setStoreVOList(storeVOList);
        tradeWrapperListRequest.setTradeCommitRequest(tradeCommitRequest);
        tradeWrapperListRequest.setTradeItemGroups(tradeItemGroups);
        List<Trade> trades = tradeService.wrapperTradeList(tradeWrapperListRequest);

        // 3.批量提交订单
        List<TradeCommitResult> successResults;
        try {
            // 处理积分抵扣
            tradeService.dealPoints(trades, tradeCommitRequest);
            // 处理知豆抵扣
            //tradeService.dealKnowledge(trades, tradeCommitRequest);
            // 预售补充尾款价格
            //tradeService.dealTailPrice(trades, tradeCommitRequest);
            //处理打包信息
            tradeService.dealGoodsPackDetail(trades, tradeCommitRequest, customer);
            successResults = tradeService.createBatch(trades, null, operator);
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
                }

                log.info("************* 提交订单异常 订单 {} 作废 begin *************:", trade.getId());

                //异常订单，主单和子单全部标记作废
                if (trade.getTradeState() != null) {
                    trade.getTradeState().setFlowState(FlowState.VOID); //此处只是做标记，不做其他任何处理
                    trade.appendTradeEventLog(new TradeEventLog(operator, "订单异常，标记订单为作废状态", "订单异常，标记订单为作废状态", LocalDateTime.now()));
                    tradeService.addTrade(trade);
                }
                log.info("提交订单异常 订单：{} Trade 作废处理 完成，当前只做不退换优惠券处理，临时方案", trade.getId());
                //获取子单列表
                List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(trade.getId());
                for (ProviderTrade providerTradeParam : providerTradeList) {
                    providerTradeParam.getTradeState().setFlowState(FlowState.VOID);
                    providerTradeParam.appendTradeEventLog(new TradeEventLog(operator, "订单异常，标记订单为作废状态", "订单异常，标记订单为作废状态", LocalDateTime.now()));
                    providerTradeService.addProviderTrade(providerTradeParam);
                    log.info("提交订单异常 订单：{} 子单：{} ProviderTrade 作废处理 完成，当前只做不退换优惠券处理，临时方案", trade.getId(), providerTradeParam.getId());
                }

                log.info("************* 提交订单异常 订单 {} 作废 end *************:", trade.getId());
            }

            throw  new SbcRuntimeException("K-000001");
        }
        try {
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
     *
     * @param request
     * @return
     */
    public List<TradeItemGroup> getTradeItemList(TradePurchaseRequest request) {
        List<String> skuIds = request.getTradeItems().stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());
        String customerId = request.getCustomer().getCustomerId();

        //查询是否购买付费会员卡
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider
                .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
                        .customerId(customerId)
                        .delFlag(DeleteFlag.NO)
                        .endTimeFlag(LocalDateTime.now())
                        .build())
                .getContext();
        PaidCardVO paidCardVO = new PaidCardVO();
        if (CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)) {
            paidCardVO = paidCardCustomerRelVOList.stream()
                    .map(PaidCardCustomerRelVO::getPaidCardVO)
                    .min(Comparator.comparing(PaidCardVO::getDiscountRate)).get();
        }

        GoodsInfoResponse response = tradeGoodsService.getGoodsResponse(skuIds, request.getCustomer());
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos();
        Map<String, GoodsVO> goodsMap = response.getGoodses().stream().filter(goods -> goods.getCpsSpecial() != null)
                .collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        Map<String, Integer> cpsSpecialMap = goodsInfoVOList.stream()
                .collect(Collectors.toMap(goodsInfo -> goodsInfo.getGoodsInfoId(), goodsInfo2 -> goodsMap.get(goodsInfo2.getGoodsId()).getCpsSpecial()));
        List<TradeItem> tradeItems = KsBeanUtil.convert(request.getTradeItems(), TradeItem.class);
        //获取付费会员价
        if (Objects.nonNull(paidCardVO.getDiscountRate())) {
            for (GoodsInfoVO goodsInfoVO : response.getGoodsInfos()) {
                goodsInfoVO.setSalePrice(goodsInfoVO.getMarketPrice().multiply(paidCardVO.getDiscountRate()));
            }
            if (CollectionUtils.isNotEmpty(tradeItems)) {
                for (TradeItem tradeItem : tradeItems) {
                    if (Objects.nonNull(tradeItem.getPrice())) {
                        tradeItem.setPrice(tradeItem.getPrice().multiply(paidCardVO.getDiscountRate()));
                    }
                }
            }
        }
        verifyService.verifyGoods(tradeItems, Collections.emptyList(), KsBeanUtil.convert(response, TradeGoodsListVO.class), null, false, null);
        verifyService.verifyStore(response.getGoodsInfos().stream().map(GoodsInfoVO::getStoreId).collect(Collectors.toList()));
        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        tradeItems.stream().forEach(tradeItem -> {
            tradeItem.setCpsSpecial(cpsSpecialMap.get(tradeItem.getSkuId()));
            tradeItem.setGoodsType(GoodsType.fromValue(goodsInfoVOMap.get(tradeItem.getSkuId()).getGoodsType()));
            tradeItem.setVirtualCouponId(goodsInfoVOMap.get(tradeItem.getSkuId()).getVirtualCouponId());
            tradeItem.setBuyPoint(goodsInfoVOMap.get(tradeItem.getSkuId()).getBuyPoint());
            tradeItem.setStoreId(goodsInfoVOMap.get(tradeItem.getSkuId()).getStoreId());
        });

        // 校验商品限售信息
        TradeItemGroup tradeItemGroupVOS = new TradeItemGroup();
        tradeItemGroupVOS.setTradeItems(tradeItems);
        tradeGoodsService.validateRestrictedGoods(tradeItemGroupVOS, request.getCustomer());

        tradeItems = tradeGoodsService.fillActivityPrice(tradeItems, goodsInfoVOList, customerId);
        for (TradeItem tradeItem : tradeItems) {
            BaseResponse<String> priceByGoodsId = goodsIntervalPriceProvider.findPriceByGoodsId(tradeItem.getSkuId());
            if (priceByGoodsId.getContext() != null) {
                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
            }
        }

        //商品按店铺分组
        Map<Long, List<TradeItem>> map = tradeItems.stream().collect(Collectors.groupingBy(TradeItem::getStoreId));
        List<TradeItemGroup> itemGroups = new ArrayList<>();
        map.forEach((key,value)->{
            StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(key)
                    .build())
                    .getContext().getStoreVO();
            DefaultFlag freightTemplateType = store.getFreightTemplateType();
            Supplier supplier = Supplier.builder()
                    .storeId(store.getStoreId())
                    .storeName(store.getStoreName())
                    .isSelf(store.getCompanyType() == BoolFlag.NO)
                    .supplierCode(store.getCompanyInfo().getCompanyCode())
                    .supplierId(store.getCompanyInfo().getCompanyInfoId())
                    .supplierName(store.getCompanyInfo().getSupplierName())
                    .freightTemplateType(freightTemplateType)
                    .build();
            TradeItemGroup tradeItemGroup = new TradeItemGroup();
            tradeItemGroup.setTradeItems(value);
            tradeItemGroup.setSupplier(supplier);
            tradeItemGroup.setTradeMarketingList(new ArrayList<>());
            itemGroups.add(tradeItemGroup);
        });
        return itemGroups;
    }



}
