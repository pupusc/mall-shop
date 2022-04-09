package com.soybean.mall.order.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.soybean.mall.order.trade.model.OrderCommitResult;
import com.soybean.mall.order.trade.model.OrderReportDetailDTO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointCancelRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.goods.PackDetailByPackIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsPackDetailResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradePurchaseRequest;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.trade.model.entity.TradeCommitResult;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.MiniProgram;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.request.TradeWrapperListRequest;
import com.wanmi.sbc.order.trade.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {


    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private TradeCustomerService tradeCustomerService;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeGoodsService tradeGoodsService;


    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private ExternalProvider externalProvider;

//    @Autowired
//    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;



    /**
     * 提交订单，不用快照,只是正常商品下单，注释的是第一期不做的
     * @param tradeCommitRequest
     * @return
     */
    @Transactional
    @GlobalTransactional
    public List<OrderCommitResult> commitTrade(TradeCommitRequest tradeCommitRequest) {
        // 验证用户
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(tradeCommitRequest.getOperator().getUserId());
        customer.setOpenId(tradeCommitRequest.getOpenId());
        tradeCommitRequest.setCustomer(customer);
        Operator operator = tradeCommitRequest.getOperator();
        //商品明细传参
        if(CollectionUtils.isEmpty(tradeCommitRequest.getTradeItems())){
            throw new SbcRuntimeException("K-050214");
        }

        List<TradeItemGroup> tradeItemGroups = tradeService.getTradeItemList(TradePurchaseRequest.builder().customer(customer).tradeItems(tradeCommitRequest.getTradeItems()).build());
        List<TradeItem> tradeItems = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).collect(Collectors.toList());

        //商品信息
        List<String> skuIds = tradeItems.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoViewByIdsResponse.getGoodsInfos();

        Map<Long, TradeItemGroup> tradeItemGroupsMap = tradeItemGroups.stream().collect(
                Collectors.toMap(g -> g.getSupplier().getStoreId(), Function.identity()));
        List<StoreVO> storeVOList = tradeCacheService.queryStoreList(new ArrayList<>(tradeItemGroupsMap.keySet()));

        Map<Long, CommonLevelVO> storeLevelMap = tradeCustomerService.listCustomerLevelMapByCustomerIdAndIds(
                new ArrayList<>(tradeItemGroupsMap.keySet()), customer.getCustomerId());
        // 1.验证失效的营销信息(目前包括失效的赠品、满系活动、优惠券)
        //verifyService.verifyInvalidMarketings(tradeCommitRequest, tradeItemGroups, storeLevelMap);
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
        trades.forEach(trade -> {
            trade.setMiniProgram(new MiniProgram());
        });
        List<OrderCommitResult> results = new ArrayList<>();
        try {
            // 处理积分抵扣
            tradeService.dealPoints(trades, tradeCommitRequest);
            // 处理打包商品
            tradeService.dealGoodsPackDetail(trades, tradeCommitRequest, customer);
            // 处理知豆抵扣
            //tradeService.dealKnowledge(trades, tradeCommitRequest);
            // 预售补充尾款价格
            //tradeService.dealTailPrice(trades, tradeCommitRequest);


            List<TradeCommitResult> successResults = tradeService.createBatch(trades, null, operator);
            //返回订单信息
            results = KsBeanUtil.convertList(trades,OrderCommitResult.class);
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
        return results;
    }


    /**
     *
     * @param request
     * @return
     */
//    public List<TradeItemGroup> getTradeItemList(TradePurchaseRequest request) {
//        List<String> skuIds = request.getTradeItems().stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());
//        String customerId = request.getCustomer().getCustomerId();
//
//        //查询是否购买付费会员卡
//        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider
//                .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
//                        .customerId(customerId)
//                        .delFlag(DeleteFlag.NO)
//                        .endTimeFlag(LocalDateTime.now())
//                        .build())
//                .getContext();
//        PaidCardVO paidCardVO = new PaidCardVO();
//        if (CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)) {
//            paidCardVO = paidCardCustomerRelVOList.stream()
//                    .map(PaidCardCustomerRelVO::getPaidCardVO)
//                    .min(Comparator.comparing(PaidCardVO::getDiscountRate)).get();
//        }
//
//        GoodsInfoResponse response = tradeGoodsService.getGoodsResponse(skuIds, request.getCustomer());
//        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos();
//        Map<String, GoodsVO> goodsMap = response.getGoodses().stream().filter(goods -> goods.getCpsSpecial() != null)
//                .collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
//        Map<String, Integer> cpsSpecialMap = goodsInfoVOList.stream()
//                .collect(Collectors.toMap(goodsInfo -> goodsInfo.getGoodsInfoId(), goodsInfo2 -> goodsMap.get(goodsInfo2.getGoodsId()).getCpsSpecial()));
//        List<TradeItem> tradeItems = KsBeanUtil.convert(request.getTradeItems(), TradeItem.class);
//        //获取付费会员价
//        if (Objects.nonNull(paidCardVO.getDiscountRate())) {
//            for (GoodsInfoVO goodsInfoVO : response.getGoodsInfos()) {
//                goodsInfoVO.setSalePrice(goodsInfoVO.getMarketPrice().multiply(paidCardVO.getDiscountRate()));
//            }
//            if (CollectionUtils.isNotEmpty(tradeItems)) {
//                for (TradeItem tradeItem : tradeItems) {
//                    if (Objects.nonNull(tradeItem.getPrice())) {
//                        tradeItem.setPrice(tradeItem.getPrice().multiply(paidCardVO.getDiscountRate()));
//                    }
//                }
//            }
//        }
//        verifyService.verifyGoods(tradeItems, Collections.emptyList(), KsBeanUtil.convert(response, TradeGoodsListVO.class), null, false, null);
//        verifyService.verifyStore(response.getGoodsInfos().stream().map(GoodsInfoVO::getStoreId).collect(Collectors.toList()));
//        Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
//        tradeItems.stream().forEach(tradeItem -> {
//            tradeItem.setCpsSpecial(cpsSpecialMap.get(tradeItem.getSkuId()));
//            tradeItem.setGoodsType(GoodsType.fromValue(goodsInfoVOMap.get(tradeItem.getSkuId()).getGoodsType()));
//            tradeItem.setVirtualCouponId(goodsInfoVOMap.get(tradeItem.getSkuId()).getVirtualCouponId());
//            tradeItem.setBuyPoint(goodsInfoVOMap.get(tradeItem.getSkuId()).getBuyPoint());
//            tradeItem.setStoreId(goodsInfoVOMap.get(tradeItem.getSkuId()).getStoreId());
//        });
//
//        tradeItems = tradeGoodsService.fillActivityPrice(tradeItems, goodsInfoVOList, customerId);
//        for (TradeItem tradeItem : tradeItems) {
//            BaseResponse<String> priceByGoodsId = goodsIntervalPriceProvider.findPriceByGoodsId(tradeItem.getSkuId());
//            if (priceByGoodsId.getContext() != null) {
//                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
//            }
//        }
//
////        //获取商品的打包信息，
////        List<String> mainGoodsIdList = tradeItems.stream().map(TradeItem::getSpuId).collect(Collectors.toList());
////        BaseResponse<List<GoodsPackDetailResponse>> packResponse = goodsQueryProvider.listPackDetailByPackIds(new PackDetailByPackIdsRequest(mainGoodsIdList));
////        List<GoodsPackDetailResponse> goodsPackDetailList = packResponse.getContext();
////
////        Map<String, List<GoodsPackDetailResponse>> packId2GoodsPackDetailMap = new HashMap<>();
////        if (!CollectionUtils.isEmpty(goodsPackDetailList)) {
////            for (GoodsPackDetailResponse goodsPackDetailParam : goodsPackDetailList) {
////                List<GoodsPackDetailResponse> goodsPackDetailListTmp = packId2GoodsPackDetailMap.computeIfAbsent(goodsPackDetailParam.getPackId(), k -> new ArrayList<>());
////                goodsPackDetailListTmp.add(goodsPackDetailParam);
////            }
////        }
////
////        List<TradeItem> resultTradeItem = new ArrayList<>();
////        for (TradeItem tradeItemParam : tradeItems) {
////            List<GoodsPackDetailResponse> goodsPackDetailListTmp = packId2GoodsPackDetailMap.get(tradeItemParam.getSpuId());
////            if (goodsPackDetailListTmp != null) {
////                tradeItemParam.
////            } else {
////                resultTradeItem.add(tradeItemParam);
////            }
////        }
//
//
//
//        // 校验商品限售信息
//        TradeItemGroup tradeItemGroupVOS = new TradeItemGroup();
//        tradeItemGroupVOS.setTradeItems(tradeItems);
//        tradeGoodsService.validateRestrictedGoods(tradeItemGroupVOS, request.getCustomer());
//
//
//        //商品按店铺分组
//        Map<Long, List<TradeItem>> map = tradeItems.stream().collect(Collectors.groupingBy(TradeItem::getStoreId));
//        List<TradeItemGroup> itemGroups = new ArrayList<>();
//        map.forEach((key,value)->{
//            StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(key)
//                    .build())
//                    .getContext().getStoreVO();
//            DefaultFlag freightTemplateType = store.getFreightTemplateType();
//            Supplier supplier = Supplier.builder()
//                    .storeId(store.getStoreId())
//                    .storeName(store.getStoreName())
//                    .isSelf(store.getCompanyType() == BoolFlag.NO)
//                    .supplierCode(store.getCompanyInfo().getCompanyCode())
//                    .supplierId(store.getCompanyInfo().getCompanyInfoId())
//                    .supplierName(store.getCompanyInfo().getSupplierName())
//                    .freightTemplateType(freightTemplateType)
//                    .build();
//            TradeItemGroup tradeItemGroup = new TradeItemGroup();
//            tradeItemGroup.setTradeItems(value);
//            tradeItemGroup.setSupplier(supplier);
//            tradeItemGroup.setTradeMarketingList(new ArrayList<>());
//            itemGroups.add(tradeItemGroup);
//        });
//        return itemGroups;
//    }

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
