package com.wanmi.sbc.order.returnorder.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.linkedmall.model.v20180116.QueryRefundApplicationDetailResponse;
import com.google.common.collect.Lists;
import com.mongodb.client.result.UpdateResult;
import com.sbc.wanmi.erp.bean.enums.ERPTradePayChannel;
import com.sbc.wanmi.erp.bean.enums.ERPTradePushStatus;
import com.sbc.wanmi.erp.bean.enums.ReturnTradeType;
import com.sbc.wanmi.erp.bean.vo.ERPTradePaymentVO;
import com.sbc.wanmi.erp.bean.vo.ReturnTradeItemVO;
import com.soybean.mall.wx.mini.enums.AfterSalesTypeEnum;
import com.soybean.mall.wx.mini.order.bean.request.WxDealAftersaleRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
import com.soybean.mall.order.enums.WxAfterSaleOperateType;
import com.soybean.mall.order.miniapp.service.WxOrderService;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.order.bean.dto.WxProductDTO;
import com.soybean.mall.wx.mini.order.bean.enums.WxAfterSaleStatus;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateAfterSaleRequest;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordProvider;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordAddRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordDeleteByReturnOrderCodeAndTypeRequest;
import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.*;
import com.wanmi.sbc.common.enums.node.ReturnOrderProcessType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountProvider;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storereturnaddress.StoreReturnAddressQueryProvider;
import com.wanmi.sbc.customer.api.request.account.CustomerAccountAddRequest;
import com.wanmi.sbc.customer.api.request.account.CustomerAccountByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.account.CustomerAccountOptionalRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressListRequest;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountAddResponse;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountOptionalResponse;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressListResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerAccountAddOrModifyDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.StoreReturnAddressVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.ReturnTradeCreateRequst;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsProvider;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsSaveProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoSaveProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleGoodsCountRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsBatchStockAndSalesVolumeRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoReturnModifyRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoBatchPlusStockRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderProvider;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcCancelRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivitySaveProvider;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityModifyStatisticsNumByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordDecrBuyNumRequest;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.api.constant.RefundReasonConstants;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.request.distribution.ReturnOrderSendMQRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRefundRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRequest;
import com.wanmi.sbc.order.api.request.returnorder.RefundRejectRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderProviderTradeRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.enums.BackRestrictedType;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.RefundChannel;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.ProviderTradeSimpleVO;
import com.wanmi.sbc.order.bean.vo.TradeItemSimpleVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.common.GoodsStockService;
import com.wanmi.sbc.order.common.OperationLogMq;
import com.wanmi.sbc.order.customer.service.CustomerCommonService;
import com.wanmi.sbc.order.groupon.service.GrouponOrderService;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.payorder.response.PayOrderResponse;
import com.wanmi.sbc.order.payorder.service.PayOrderService;
import com.wanmi.sbc.order.refund.model.root.RefundBill;
import com.wanmi.sbc.order.refund.model.root.RefundOrder;
import com.wanmi.sbc.order.refund.repository.RefundOrderRepository;
import com.wanmi.sbc.order.refund.service.RefundBillService;
import com.wanmi.sbc.order.refund.service.RefundOrderService;
import com.wanmi.sbc.order.returnorder.fsm.ReturnFSMService;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnAddress;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrderTransfer;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.order.returnorder.model.value.ReturnKnowledge;
import com.wanmi.sbc.order.returnorder.model.value.ReturnLogistics;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPoints;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPrice;
import com.wanmi.sbc.order.returnorder.mq.ReturnOrderProducerService;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderRepository;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderTransferRepository;
import com.wanmi.sbc.order.returnorder.request.ReturnQueryRequest;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.ThirdPlatformTrade;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeReturn;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroupon;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeCacheService;
import com.wanmi.sbc.order.trade.service.TradeService;
import com.wanmi.sbc.order.trade.service.VerifyService;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.ChannelItemByIdRequest;
import com.wanmi.sbc.pay.api.request.RefundResultByOrdercodeRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import com.wanmi.sbc.pay.api.response.PayChannelItemResponse;
import com.wanmi.sbc.pay.api.response.PayTradeRecordResponse;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jinwei on 20/4/2017.
 */
@Slf4j
@Service
public class ReturnOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private ReturnOrderRepository returnOrderRepository;

    @Autowired
    private ReturnOrderTransferRepository returnOrderTransferRepository;

    @Autowired
    private ReturnOrderTransferService returnOrderTransferService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;


    @Autowired
    private TradeService tradeService;

    @Autowired
    private ReturnFSMService returnFSMService;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private RefundBillService refundBillService;

    @Autowired
    private CustomerAccountQueryProvider customerAccountQueryProvider;

    @Autowired
    private CustomerAccountProvider customerAccountProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Autowired
//    private ElasticsearchTemplate template;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private AccountRecordProvider accountRecordProvider;

    @Autowired
    private RefundOrderRepository refundOrderRepository;

    @Autowired
    private CustomerCommonService customerCommonService;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private OperationLogMq operationLogMq;

    /**
     * 注入退单状态变更生产者service
     */
    @Autowired
    private ReturnOrderProducerService returnOrderProducerService;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private GrouponGoodsInfoSaveProvider grouponGoodsInfoProvider;

    @Autowired
    private GrouponActivitySaveProvider activityProvider;

    @Autowired
    private GrouponRecordProvider recordProvider;

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private OrderProducerService orderProducerService;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private BookingSaleGoodsQueryProvider bookingSaleGoodsQueryProvider;

    @Autowired
    private BookingSaleGoodsProvider bookingSaleGoodsProvider;

    @Autowired
    private LinkedMallReturnOrderProvider linkedMallReturnOrderProvider;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @Autowired
    private LinkedMallTradeService linkedMallTradeService;

    @Autowired
    private LinkedMallReturnOrderService linkedMallReturnOrderService;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private FlashSaleGoodsSaveProvider flashSaleGoodsSaveProvider;

    @Autowired
    private StoreReturnAddressQueryProvider returnAddressQueryProvider;

    @Autowired
    private GuanyierpProvider guanyierpProvider;


    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;


    @Value("${default.providerId}")
    private Long defaultProviderId;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${wx.create.order.send.message.link.url}")
    private String orderDetailUrl;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Autowired
    private WxOrderService wxOrderService;


    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param returnOrder
     */
    @Transactional
    public ReturnOrder addReturnOrder(ReturnOrder returnOrder) {
        return returnOrderRepository.save(returnOrder);
    }


    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param returnOrders
     */
    @Transactional
    public List<ReturnOrder> addReturnOrders(List<ReturnOrder> returnOrders) {
        return returnOrderRepository.saveAll(returnOrders);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param returnOrder
     */
    public void updateReturnOrder(ReturnOrder returnOrder) {
        returnOrderRepository.save(returnOrder);
    }

    /**
     * 删除文档
     *
     * @param rid
     */
    public void deleteReturnOrder(String rid) {
        returnOrderRepository.deleteById(rid);
    }


//    /**
//     * 商家代客退单
//     *
//     * @param returnOrder
//     * @param operator
//     * @return
//     */
//    public String s2bCreate(ReturnOrder returnOrder, Operator operator) {
//        Trade trade = tradeService.detail(returnOrder.getTid());
////        Customer customer = verifyService.verifyCustomer(trade.getBuyer().getId());
//        CustomerGetByIdResponse customer = verifyService.verifyCustomer(trade.getBuyer().getId());
//        verifyService.verifyCustomerWithSupplier(customer.getCustomerId(), returnOrder.getCompany().getCompanyInfoId());
//        return create(returnOrder, operator);
//    }

    private void splitReturnTrade(ReturnOrder returnOrder, Trade trade, List<ReturnOrder> returnOrders, Map<String, Boolean> providerDeliveryMap) {
        //订单详情集合
        List<TradeItem> tradeItemList = trade.getTradeItems();
        //退单的商品列表
        List<ReturnItem> returnItemList = returnOrder.getReturnItems();

        // 查询订单商品,赠品所属供应商id集合
        List<Long> returnProviderIds = returnItemList.stream().filter(returnItem -> Objects.nonNull(returnItem.getProviderId())).map(ReturnItem::getProviderId).distinct().collect(Collectors.toList());

        //退款时全部赠品都退;退货时只需退本次退单需要退的赠品信息由getAndSetReturnGifts()获取
//        List<TradeItem> gifts = trade.getGifts();
//        List<ReturnItem> returnGifts = returnOrder.getReturnGifts();
//        if (ReturnType.REFUND.equals(returnOrder.getReturnType()) && CollectionUtils.isNotEmpty(gifts)) {
//            returnProviderIds.addAll(
//                    gifts.stream().filter(item -> Objects.nonNull(item.getProviderId()))
//                            .map(TradeItem::getProviderId)
//                            .collect(Collectors.toList()));
//            returnProviderIds = returnProviderIds.stream().distinct().collect(Collectors.toList());
//        } else if (ReturnType.RETURN.equals(returnOrder.getReturnType()) && CollectionUtils.isNotEmpty(returnGifts)) {
//            returnProviderIds.addAll(
//                    returnGifts.stream().filter(item -> Objects.nonNull(item.getProviderId()))
//                            .map(ReturnItem::getProviderId)
//                            .collect(Collectors.toList()));
//            returnProviderIds = returnProviderIds.stream().distinct().collect(Collectors.toList());
//        }

        //供应商商品
        if (CollectionUtils.isNotEmpty(returnProviderIds)) {
            Map<Long, StoreVO> storeVOMap = tradeCacheService.queryStoreList(returnProviderIds).stream().collect(Collectors.toMap(StoreVO::getStoreId, s -> s));
            //根据供应商id拆单
            for (Long providerId : returnProviderIds) {
                //对应的子单
                ProviderTrade providerTrade = providerTradeService.getProviderTradeByIdAndPid(trade.getId(), providerId);
                ReturnOrder providerReturnOrder = KsBeanUtil.convert(returnOrder, ReturnOrder.class);
                if (Objects.nonNull(providerTrade)) {
                    providerReturnOrder.setPtid(providerTrade.getId());
                }
//                List<TradeItem> providerGiftItems = trade.getGifts().stream().filter(item -> providerId.equals(item.getProviderId())).collect(Collectors.toList());
                //退款时赠品拆分
//                this.fullReturnGifts(providerReturnOrder, providerGiftItems, providerId);
                buildReturnOrder(providerReturnOrder, trade, StoreType.PROVIDER, providerId, null, providerTrade, providerDeliveryMap);
                StoreVO storeVO = storeVOMap.get(providerId);
                //判断是否linkedmall,拆分LM店铺子订单
                if (storeVO != null && CompanySourceType.LINKED_MALL.equals(storeVO.getCompanySourceType())) {
                    returnOrders.addAll(linkedMallReturnOrderService.splitReturnOrder(providerReturnOrder));
                    returnOrders.forEach(o -> {
                        calcReturnPrice(o, trade.getTradeItems().stream().filter(tradeItem -> providerId.equals(tradeItem.getProviderId())).collect(Collectors.toList()));
                    });
                } else {
                    returnOrders.add(providerReturnOrder);
                }
            }
        }

        //商家商品,退单
        List<ReturnItem> returnStoreItemList = returnItemList.stream().filter(returnItem -> Objects.isNull(returnItem.getProviderId())).collect(Collectors.toList());
//        List<TradeItem> giftItemList =
//                trade.getGifts().stream().filter(item -> Objects.isNull(item.getProviderId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(returnStoreItemList) /*|| CollectionUtils.isNotEmpty(giftItemList)*/) {
//            //退款时赠品拆分
//            this.fullReturnGifts(returnOrder, giftItemList, null);
            //没有供应商，且该订单没有被拆分子单
            ProviderTrade providerTrade = providerTradeService.getProviderTradeByIdAndPid(trade.getId(), trade.getSupplier().getStoreId());
            if (CollectionUtils.isEmpty(returnProviderIds) && Objects.isNull(providerTrade)) {
                returnOrders.add(returnOrder);
            } else {
                List<TradeItem> storeItemList =
                        tradeItemList.stream().filter(tradeItem -> Objects.isNull(tradeItem.getProviderId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(storeItemList)/* || CollectionUtils.isNotEmpty(giftItemList)*/) {
                    //对应的子单
                    returnOrder.setPtid(providerTrade.getId());
                    buildReturnOrder(returnOrder, trade, StoreType.SUPPLIER, null, storeItemList, providerTrade, providerDeliveryMap);
                    returnOrders.add(returnOrder);
                }
            }
        }
    }

    /**
     * 退款时，赠品也需要退单拆分
     *
     * @param returnOrder
     * @param giftItemList
     */
    private void fullReturnGifts(ReturnOrder returnOrder, List<TradeItem> giftItemList, Long providerId) {
        if (ReturnType.REFUND.equals(returnOrder.getReturnType())) {
            List<ReturnItem> returnGifts = giftItemList.stream().map(giftItem -> ReturnItem.builder()
                    .skuId(giftItem.getSkuId())
                    .skuName(giftItem.getSkuName())
                    .skuNo(giftItem.getSkuNo())
                    .price(giftItem.getPrice())
                    .pic(giftItem.getPic())
                    .num(giftItem.getNum().intValue())
                    .unit(giftItem.getUnit())
                    .supplyPrice(giftItem.getSupplyPrice())
                    .providerPrice(giftItem.getTotalSupplyPrice())
                    .thirdPlatformType(giftItem.getThirdPlatformType())
                    .thirdPlatformSkuId(giftItem.getThirdPlatformSkuId())
                    .thirdPlatformSpuId(giftItem.getThirdPlatformSpuId())
                    .thirdPlatformSubOrderId(giftItem.getThirdPlatformSubOrderId())
                    .build())
                    .collect(Collectors.toList());
            returnOrder.setReturnGifts(returnGifts);
        } else {
            //退货单赠品的供应商/商家拆分
            List<ReturnItem> newGiftItem;
            if (Objects.nonNull(providerId)) {
                newGiftItem = returnOrder.getReturnGifts().stream()
                        .filter(item -> providerId.equals(item.getProviderId()))
                        .collect(Collectors.toList());
            } else {
                newGiftItem = returnOrder.getReturnGifts().stream()
                        .filter(item -> item.getProviderId() == null)
                        .collect(Collectors.toList());
            }
            returnOrder.setReturnGifts(newGiftItem);
        }
    }

    private void buildReturnOrder(ReturnOrder returnOrder, Trade trade, StoreType storeType, Long providerId, List<TradeItem> storeItemList, ProviderTrade providerTrade, Map<String, Boolean> providerDeliveryMap) {

        if (StoreType.PROVIDER.equals(storeType)) {
            StoreVO storeVO =
                    storeQueryProvider.getById(StoreByIdRequest.builder().storeId(providerId).build()).getContext().getStoreVO();
            if (storeVO != null) {
                String companyCode = storeVO.getCompanyInfo().getCompanyCode();
                Long companyInfoId = storeVO.getCompanyInfo().getCompanyInfoId();
                String supplierName = storeVO.getCompanyInfo().getSupplierName();

                returnOrder.setProviderId(String.valueOf(providerId));
                returnOrder.setProviderCode(companyCode);
                returnOrder.setProviderName(supplierName);
                returnOrder.setProviderCompanyInfoId(companyInfoId);
                // 筛选当前供应商的订单商品信息
                List<TradeItem> providerTradeItems =
                        trade.getTradeItems().stream().filter(tradeItem -> providerId.equals(tradeItem.getProviderId())).collect(Collectors.toList());
                List<ReturnItem> returnItemDTOList = new ArrayList<>();
                //组装退单商品详情
                BigDecimal providerTotalPrice = BigDecimal.ZERO;
                BigDecimal price = BigDecimal.ZERO;
                BigDecimal deliverPrice = BigDecimal.ZERO;
                Long points = 0L;
                Long knowledge = 0L;
                for (TradeItem tradeItemVO : providerTradeItems) {
                    for (ReturnItem returnItemDTO : returnOrder.getReturnItems()) {
                        if (tradeItemVO.getSkuId().equals(returnItemDTO.getSkuId())) {
                            returnItemDTO.setSupplyPrice(tradeItemVO.getSupplyPrice());
                            BigDecimal supplyPrice = Objects.nonNull(tradeItemVO.getSupplyPrice()) ? tradeItemVO.getSupplyPrice() : BigDecimal.ZERO;
                            returnItemDTO.setProviderPrice(supplyPrice.multiply(new BigDecimal(returnItemDTO.getNum())));
                            providerTotalPrice = providerTotalPrice.add(returnItemDTO.getProviderPrice());

                            if (returnItemDTO.getApplyRealPrice() == null) {
                                price = price.add(returnItemDTO.getSplitPrice());
                            } else {
                                price = price.add(returnItemDTO.getApplyRealPrice());
                            }

                            if (returnItemDTO.getApplyRealPrice() == null) {
                                if (returnOrder.getReturnType() == ReturnType.REFUND && tradeItemVO.getPoints() != null) {
                                    points += tradeItemVO.getPoints();
                                } else if (returnOrder.getReturnType() == ReturnType.RETURN && tradeItemVO.getPoints() != null) {
                                    points += returnItemDTO.getSplitPoint();
                                }
                            } else {
                                points += returnItemDTO.getApplyPoint();
                            }

                            if (returnItemDTO.getApplyKnowledge() == null) {
                                if (returnOrder.getReturnType() == ReturnType.REFUND && tradeItemVO.getKnowledge() != null) {
                                    knowledge += tradeItemVO.getKnowledge();
                                } else if (returnOrder.getReturnType() == ReturnType.RETURN && tradeItemVO.getKnowledge() != null) {
                                    knowledge += returnItemDTO.getSplitKnowledge();
                                }
                            } else {
                                knowledge += returnItemDTO.getApplyKnowledge();
                            }

                            returnItemDTOList.add(returnItemDTO);
                        }
                    }
                }

//                //填充赠品供应商信息
//                if (CollectionUtils.isNotEmpty(trade.getGifts()) && CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
//                    // 筛选当前供应商的订单商品信息
//                    Map<String, BigDecimal> providerTradeGifts = trade.getGifts().stream()
//                            .filter(tradeItem -> providerId.equals(tradeItem.getProviderId()) && tradeItem.getSupplyPrice() != null)
//                            .collect(Collectors.toMap(TradeItem::getSkuId, TradeItem::getSupplyPrice, (a, b) -> a));
//                    //组装退单详情
//                    for (ReturnItem returnItemDTO : returnOrder.getReturnGifts()) {
//                        returnItemDTO.setSupplyPrice(providerTradeGifts.getOrDefault(returnItemDTO.getSkuId(), BigDecimal.ZERO));
//                        returnItemDTO.setProviderPrice(returnItemDTO.getSupplyPrice().multiply(new BigDecimal(returnItemDTO.getNum())));
//                        providerTotalPrice = providerTotalPrice.add(returnItemDTO.getProviderPrice());
//                    }
//                }

                //添加运费, 如果存在申请中或者申请完成的订单，则退还运费，否则不退还运费 只有未退款的情况才会退运费
                if (trade.getTradeState().getFlowState().equals(FlowState.AUDIT) && ReturnType.REFUND.equals(returnOrder.getReturnType()) && (providerDeliveryMap.get(providerId.toString()) != null && providerDeliveryMap.get(providerId.toString()))) {
                    //判断当前是否是未发货，同时是最后一个商品
                    if(trade.getTradePrice() != null && trade.getTradePrice().getSplitDeliveryPrice() != null && trade.getTradePrice().getSplitDeliveryPrice().get(providerId) != null){
                        price = price.add(trade.getTradePrice().getSplitDeliveryPrice().get(providerId));
                        deliverPrice = deliverPrice.add(trade.getTradePrice().getSplitDeliveryPrice().get(providerId));
                    }/*else {
                        if(trade.getTradePrice() != null && trade.getTradePrice().getDeliveryPrice() != null){
                            price = price.add(trade.getTradePrice().getDeliveryPrice());
                        }
                    }*/
                }

                //针对退运费的强制
                if (ReturnReason.PRICE_DELIVERY.getType().equals(returnOrder.getReturnReason().getType())) {
                    if (trade.getTradePrice().getSplitDeliveryPrice() == null) {
                        throw new SbcRuntimeException("K-050460");
                    }
                    price = trade.getTradePrice().getSplitDeliveryPrice().get(providerId);
                    if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new SbcRuntimeException("K-050461");
                    }
                    deliverPrice = price;
                    providerTotalPrice = BigDecimal.ZERO;
                    points = 0L;
                    knowledge = 0L;
                    returnItemDTOList = new ArrayList<>();
                }
                returnOrder.setReturnItems(returnItemDTOList);
                returnOrder.getReturnPrice().setProviderTotalPrice(providerTotalPrice);
                returnOrder.getReturnPrice().setApplyPrice(price);
                returnOrder.getReturnPrice().setTotalPrice(price);
                returnOrder.getReturnPrice().setDeliverPrice(deliverPrice);
                returnOrder.getReturnPoints().setApplyPoints(points);
                returnOrder.getReturnKnowledge().setApplyKnowledge(knowledge);
            }
        } else {
            if (storeItemList != null) {
                List<ReturnItem> returnItemDTOList = new ArrayList<>();
                BigDecimal price = BigDecimal.ZERO;
                BigDecimal deliverPrice = BigDecimal.ZERO;
                Long points = 0L;
                Long knowledge = 0L;
                //组装退单详情
                for (TradeItem tradeItemVO : storeItemList) {
                    for (ReturnItem returnItemDTO : returnOrder.getReturnItems()) {
                        if (tradeItemVO.getSkuId().equals(returnItemDTO.getSkuId())) {
                            if (returnItemDTO.getApplyRealPrice() == null) {
                                price = price.add(returnItemDTO.getSplitPrice());
                            } else {
                                price = price.add(returnItemDTO.getApplyRealPrice());
                            }

                            if (returnItemDTO.getApplyRealPrice() == null) {
                                if (returnOrder.getReturnType() == ReturnType.REFUND && tradeItemVO.getPoints() != null) {
                                    points += tradeItemVO.getPoints();
                                } else if (returnOrder.getReturnType() == ReturnType.RETURN && tradeItemVO.getPoints() != null) {
                                    points += returnItemDTO.getSplitPoint();
                                }
                            } else {
                                points += returnItemDTO.getApplyPoint();
                            }

                            if (returnItemDTO.getApplyKnowledge() == null) {
                                if (returnOrder.getReturnType() == ReturnType.REFUND && tradeItemVO.getKnowledge() != null) {
                                    knowledge += tradeItemVO.getKnowledge();
                                } else if (returnOrder.getReturnType() == ReturnType.RETURN && tradeItemVO.getKnowledge() != null) {
                                    knowledge += returnItemDTO.getSplitKnowledge();
                                }
                            } else {
                                knowledge = returnItemDTO.getApplyKnowledge();
                            }

//                            if (returnOrder.getReturnType() == ReturnType.REFUND && tradeItemVO.getPoints() != null) {
//                                points += tradeItemVO.getPoints();
//                            } else if (returnOrder.getReturnType() == ReturnType.RETURN && tradeItemVO.getPoints() != null) {
//                                points += returnItemDTO.getSplitPoint();
//                            }
//                            if (returnOrder.getReturnType() == ReturnType.REFUND && tradeItemVO.getKnowledge() != null) {
//                                knowledge += tradeItemVO.getKnowledge();
//                            } else if (returnOrder.getReturnType() == ReturnType.RETURN && tradeItemVO.getKnowledge() != null) {
//                                knowledge += returnItemDTO.getSplitKnowledge();
//                            }
                            returnItemDTOList.add(returnItemDTO);
                        }
                    }
                }

                //退货的情况不需要退运费
//                if (ReturnType.REFUND.equals(returnOrder.getReturnType())) {
//                    price = price.add(Objects.nonNull(providerTrade.getTradePrice().getDeliveryPrice()) ?
//                            providerTrade.getTradePrice().getDeliveryPrice() : BigDecimal.ZERO);
//                }
                //针对退运费的强制
                if (ReturnReason.PRICE_DELIVERY.getType().equals(returnOrder.getReturnReason().getType())) {
                    price = trade.getTradePrice().getSplitDeliveryPrice().get(providerId);
                    points = 0L;
                    knowledge = 0L;
                    returnItemDTOList = new ArrayList<>();
                }
                returnOrder.setReturnItems(returnItemDTOList);
                returnOrder.getReturnPrice().setApplyPrice(price);
                returnOrder.getReturnPrice().setTotalPrice(price);
                returnOrder.getReturnPrice().setDeliverPrice(deliverPrice);
                returnOrder.getReturnPoints().setApplyPoints(points);
                returnOrder.getReturnKnowledge().setApplyKnowledge(knowledge);
            }

        }
    }

    private void calcReturnPrice(ReturnOrder returnOrder, List<TradeItem> providerTradeItems) {
        Map<String, TradeItem> itemMap = providerTradeItems.stream()
                .collect(Collectors.toMap(TradeItem::getSkuId, i -> i));
        //组装退单详情
        BigDecimal providerTotalPrice = BigDecimal.ZERO;
        BigDecimal price = BigDecimal.ZERO;
        Long points = 0L;
        for (ReturnItem returnItemDTO : returnOrder.getReturnItems()) {
            providerTotalPrice = providerTotalPrice.add(returnItemDTO.getProviderPrice());
            if (returnItemDTO.getSplitPoint() != null) {
                points += returnItemDTO.getSplitPoint();
            }
            TradeItem item = itemMap.get(returnItemDTO.getSkuId());
            if (item != null) {
                BigDecimal splitPrice = Objects.nonNull(item.getSplitPrice()) ? item.getSplitPrice() : BigDecimal.ZERO;
                BigDecimal goodsPrice = splitPrice.divide(BigDecimal.valueOf(item.getNum()), 10, BigDecimal.ROUND_DOWN);
                price = price.add(goodsPrice.multiply(BigDecimal.valueOf(returnItemDTO.getNum())));
            }
        }
        returnOrder.getReturnPrice().setProviderTotalPrice(providerTotalPrice);
        returnOrder.getReturnPrice().setApplyPrice(price);
        returnOrder.getReturnPrice().setTotalPrice(price);
        returnOrder.getReturnPoints().setApplyPoints(points);
    }

    /**
     * 是否存在退单
     * @param returnOrder  申请售后商品
     * @param returnOrderList 已经申请的售后商品
     */
    private void verifyIsExistsItemReturnOrder(ReturnOrder returnOrder, List<ReturnOrder> returnOrderList) {
        //如果当前退单为退运费，则查看当前退单是否有运费订单，如果有运费直接抛出异常
        if (returnOrder.getReturnReason() != null && ReturnReason.PRICE_DELIVERY.getType().equals(returnOrder.getReturnReason().getType())) {
            //查看当前所有的供应商，如果为退还运费，则只能一个供应商
            Map<Long, ReturnItem> providerIdMap = returnOrder.getReturnItems().stream().collect(Collectors.toMap(ReturnItem::getProviderId, Function.identity(), (k1, k2) -> k1));
            if (providerIdMap.size() != 1) {
                throw new SbcRuntimeException("K-050413");
            }

            //退运费的时候，对应的退单的商品列表为空
            List<ReturnOrder> deliveryReturnOrderList = returnOrderList.stream().filter(returnOrderParam ->
                        ReturnReason.PRICE_DELIVERY.getType().equals(returnOrderParam.getReturnReason().getType())
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.VOID
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE
            ).collect(Collectors.toList());

            for (ReturnOrder returnOrderParam : deliveryReturnOrderList) {
                if (providerIdMap.get(Long.valueOf(returnOrderParam.getProviderId())) != null) {
                    throw new SbcRuntimeException("K-050414");
                }

                if (returnOrderParam.getReturnPrice().getDeliverPrice() != null && returnOrderParam.getReturnPrice().getDeliverPrice().compareTo(BigDecimal.ZERO) > 0) {
                    throw new SbcRuntimeException("K-050414");
                }
            }
        }
        //获取申请售后的商品列表 TODO 没有做赠品的处理
        Map<String, String> applyReturnMap = new HashMap<>();
        for (ReturnItem returnItem : returnOrder.getReturnItems()) {
            applyReturnMap.put(returnItem.getSkuId(), returnItem.getSkuName());
        }
        //过滤处理中的退单
        List<ReturnOrder> returnOrderListFilter = returnOrderList.stream().filter(item ->
                        item.getReturnFlowState() != ReturnFlowState.COMPLETED
                                && item.getReturnFlowState() != ReturnFlowState.VOID
                                && item.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                                && item.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE
                                && item.getReturnFlowState() != ReturnFlowState.REFUNDED)
                .collect(Collectors.toList());
        //如果有退款中的商品，则直接抛出来异常
        for (ReturnOrder returnOrderParam : returnOrderListFilter) {
            List<String> returnOrderSkuIdFilter = returnOrderParam.getReturnItems().stream().map(ReturnItem::getSkuId).collect(Collectors.toList());
            for (String skuIdParam : returnOrderSkuIdFilter) {
                String skuName = applyReturnMap.get(skuIdParam);
                if (StringUtils.isNotBlank(skuName)) {
                    throw new SbcRuntimeException("K-050120", new Object[]{skuName});
                }
            }
        }
    }



    /**
     * 退单创建
     *
     * @param returnOrder
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public String create(ReturnOrder returnOrder, Operator operator) {
//        if (returnOrder.getDescription().length() > 100) {
//            throw new SbcRuntimeException("K-000009");
//        }
        //查看当前是否全部为虚拟商品,
        long count = returnOrder.getReturnItems().stream()
                .filter(t -> GoodsType.VIRTUAL_COUPON.equals(t.getGoodsType()) || GoodsType.VIRTUAL_GOODS.equals(t.getGoodsType())).count();
        long giftsCount = returnOrder.getReturnGifts().stream()
                .filter(t -> GoodsType.VIRTUAL_COUPON.equals(t.getGoodsType()) || GoodsType.VIRTUAL_GOODS.equals(t.getGoodsType())).count();
        long returnVirtualTotalCount = count + giftsCount;
        long returnTotalCount = returnOrder.getReturnItems().size() + returnOrder.getReturnGifts().size();
        returnOrder.setReturnType(returnOrder.getReturnWay() == ReturnWay.OTHER ? ReturnType.REFUND : ReturnType.RETURN);
        //表示退款的数量为 虚拟商品的数量相同；
        if (returnVirtualTotalCount == returnTotalCount) {
            // 虚拟商品 只能直接退款
            returnOrder.setReturnWay(ReturnWay.OTHER);
            returnOrder.setReturnType(ReturnType.RETURN);
        }

        //根据订单id获取退单列表
        List<ReturnOrder> returnOrderList = returnOrderRepository.findByTid(returnOrder.getTid());
        //计算该订单下所有已完成退单的总金额
        BigDecimal allReturnCompletePrice = new BigDecimal("0");
        Map<String, Boolean> providerDeliveryMap = new HashMap<>();

        Map<String, Integer> allReturnSkuIdNumMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(returnOrderList)) {
            //校验是否有没有完成的商品行退单
            this.verifyIsExistsItemReturnOrder(returnOrder, returnOrderList);

            //过滤出来所有完成退单的订单列表
            List<ReturnOrder> completedReturnOrderListTmp = new ArrayList<>();
            for (ReturnOrder returnOrderParam : returnOrderList) {
                if (returnOrderParam.getReturnFlowState() == ReturnFlowState.COMPLETED) {
                    completedReturnOrderListTmp.add(returnOrderParam);
                }

                //已经申请退单的商品列表
                if (returnOrderParam.getReturnReason().equals(ReturnReason.PRICE_DELIVERY)
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.VOID
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE) {
                    providerDeliveryMap.put(returnOrderParam.getProviderId(), false);
                }

                if (returnOrderParam.getReturnFlowState() != ReturnFlowState.VOID
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                        && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE) {
                    for (ReturnItem returnItem : returnOrderParam.getReturnItems()) {
                        Integer skuIdNum = allReturnSkuIdNumMap.get(returnItem.getSkuId()) == null ? 0 : allReturnSkuIdNumMap.get(returnItem.getSkuId());
                        skuIdNum += returnItem.getNum();
                        allReturnSkuIdNumMap.put(returnItem.getSkuId(), skuIdNum);
                    }
                }
            }

            //计算已经退款完成的订单总金额
            for (ReturnOrder returnOrderParam : completedReturnOrderListTmp) {
                BigDecimal p = returnOrderParam.getReturnPrice().getApplyStatus() ? returnOrderParam.getReturnPrice().getApplyPrice() : returnOrderParam.getReturnPrice().getTotalPrice();
                allReturnCompletePrice = allReturnCompletePrice.add(p);
            }
        }

        Map<String, List<ReturnItem>> provider2ReturnItemMap = new HashMap<>();
        //申请退单填充数量
        for (ReturnItem returnItem : returnOrder.getReturnItems()) {
            List<ReturnItem> returnItemList = provider2ReturnItemMap.get(returnItem.getProviderId().toString());
            if (CollectionUtils.isEmpty(returnItemList)) {
                returnItemList = new ArrayList<>();
                provider2ReturnItemMap.put(returnItem.getProviderId().toString(), returnItemList);
            }
            returnItemList.add(returnItem);

            if (returnOrder.getReturnReason() != ReturnReason.PRICE_DELIVERY && returnOrder.getReturnReason() != ReturnReason.PRICE_DIFF) {
                Integer skuIdNum = allReturnSkuIdNumMap.get(returnItem.getSkuId()) == null ? 0 : allReturnSkuIdNumMap.get(returnItem.getSkuId());
                skuIdNum += returnItem.getNum();
                allReturnSkuIdNumMap.put(returnItem.getSkuId(), skuIdNum);
            } else {
                returnItem.setNum(0);
            }
        }

        //此处变更 订单的金额、积分、知豆等信息，此处储存的是 订单总共可退金额
        Trade trade = this.queryCanReturnItemNumByTid(returnOrder.getTid(), (operator.getPlatform() == Platform.SUPPLIER || operator.getPlatform() == Platform.WX_VIDEO) ? 1 : null, returnOrder.getReturnReason());

        //当前如果为视频号、退货退款， 则不可以发起退差价和运费
        if (Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP)  &&  Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex()) ) {
            if (Objects.equals(returnOrder.getReturnReason(),ReturnReason.PRICE_DIFF) || Objects.equals(returnOrder.getReturnReason(), ReturnReason.PRICE_DELIVERY)) {
                throw new SbcRuntimeException("K-050462");
            }
        }

        //查看是否需要退还运费
        if (providerDeliveryMap.isEmpty()) {
            Map<String, List<TradeItem>> tradeItemMap = new HashMap<>();
            for (TradeItem tradeItemParam : trade.getTradeItems()) {
                //商品按照管易云和博库分类
                List<TradeItem> tradeItemList = tradeItemMap.get(tradeItemParam.getProviderId().toString());
                if (CollectionUtils.isEmpty(tradeItemList)) {
                    tradeItemList = new ArrayList<>();
                    tradeItemMap.put(tradeItemParam.getProviderId().toString(), tradeItemList);
                }
                tradeItemList.add(tradeItemParam);
            }

            //按照供应商提供运费
            for (Map.Entry<String, List<TradeItem>> entry : tradeItemMap.entrySet()) {
                Boolean isNeedReturnDelivery = providerDeliveryMap.get(entry.getKey());
                if (isNeedReturnDelivery != null && !isNeedReturnDelivery) {
                    continue;
                }
                isNeedReturnDelivery = true;
                for (TradeItem tradeItemParam : entry.getValue()) {
                    Integer returnSkuIdNum =
                            allReturnSkuIdNumMap.get(tradeItemParam.getSkuId()) == null ? 0 : allReturnSkuIdNumMap.get(tradeItemParam.getSkuId());
                    if (tradeItemParam.getNum().intValue() != returnSkuIdNum) {
                        isNeedReturnDelivery = false;
                        break;
                    }
                }
                providerDeliveryMap.put(entry.getKey(), isNeedReturnDelivery);
            }
        }

        //设置退单调用樊登参数
        CustomerDetailVO customerDetailVO = customerCommonService.getCustomerDetailByCustomerId(trade.getBuyer().getId());
        returnOrder.setFanDengUserNo(customerDetailVO.getCustomerVO().getFanDengUserNo());

        //退单申请金额
        ReturnPrice returnPrice = returnOrder.getReturnPrice();

        //退款商品列表
        List<ReturnItem> returnItems = returnOrder.getReturnItems();
        //获取申请退款的商品id列表
        List<String> skuIds = returnItems.stream().map(ReturnItem::getSkuId).collect(Collectors.toList());
        //填充商品信息
        GoodsInfoListByIdsRequest goodsInfoListByIdsRequest = new GoodsInfoListByIdsRequest();
        goodsInfoListByIdsRequest.setGoodsInfoIds(skuIds);
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoQueryProvider.listByIds(goodsInfoListByIdsRequest).getContext().getGoodsInfos();
        if (skuIds.size() != goodsInfoVOList.size()) {
            throw new IllegalArgumentException(CommonErrorCode.PARAMETER_ERROR);
        }

        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            //skuId --> goodsInfoVO
            Map<String, GoodsInfoVO> goodsInfoVOMap = goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity(), (k1,k2) -> k1));
            for (ReturnItem returnItemParam : returnItems) {
                GoodsInfoVO vo = goodsInfoVOMap.get(returnItemParam.getSkuId());
                //填充商品附属信息
                if (Objects.nonNull(vo)) {
                    returnItemParam.setGoodsId(vo.getGoodsId());
                    returnItemParam.setSpuId(vo.getGoodsId());
                    returnItemParam.setSpuName(vo.getGoodsInfoName());
                    returnItemParam.setSkuName(vo.getGoodsInfoName());
                    returnItemParam.setCateTopId(vo.getCateTopId());
                    returnItemParam.setCateId(vo.getCateId());
                    returnItemParam.setGoodsType(GoodsType.fromValue(vo.getGoodsType()));
                    returnItemParam.setBrandId(vo.getBrandId());
                }
            }
        }


        //获取当前已经退款的订单信息
        Map<String, TradeItemSimpleVO> skuId2TradeItemSimpleMap = new HashMap<>();
        ReturnOrderProviderTradeRequest returnOrderProviderTradeRequest = new ReturnOrderProviderTradeRequest();
        returnOrderProviderTradeRequest.setTid(returnOrder.getTid());
        List<ProviderTradeSimpleVO> providerTradeSimpleVOList = this.listReturnProviderTrade(returnOrderProviderTradeRequest);
        for (ProviderTradeSimpleVO providerTradeSimpleParam : providerTradeSimpleVOList) {
            for (TradeItemSimpleVO tradeItemParam : providerTradeSimpleParam.getTradeItems()) {
                skuId2TradeItemSimpleMap.put(tradeItemParam.getSkuId(), tradeItemParam);
            }
        }


        for (ReturnItem returnItem : returnItems) {
            //查看订单中是否存在该商品
            List<TradeItem> tradeItemBySkuIdList = trade.getTradeItems().stream().filter(tradeItem -> returnItem.getSkuId().equals(tradeItem.getSkuId())).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(tradeItemBySkuIdList) || tradeItemBySkuIdList.size() > 1) {
                log.error("ReturnOrderService create returnItem skuId:{} tradeItemListBySkuId is empty or size greater than 1", returnItem.getSkuId());
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            //由于管易云博库只能整个商品一起取消，如果未发货，当前必须的是整个商品行退款，所以数量必须的相同
            //获取订单中的商品,商品购买数量为0，则抛出异常
            TradeItem tradeItem = tradeItemBySkuIdList.get(0);
            if (tradeItem.getNum() == 0) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            returnItem.setOrderSplitPrice(tradeItem.getSplitPrice()); //设置拆单价格
            if (returnItem.getApplyRealPrice() == null) {
                //单价
                BigDecimal itemUnitPrice = tradeItem.getSplitPrice().divide(new BigDecimal(tradeItem.getNum().toString()), 2, BigDecimal.ROUND_HALF_UP);

                if ((tradeItem.getCanReturnNum() - returnItem.getNum()) > 0) {
                    if (itemUnitPrice.compareTo(new BigDecimal("0.01")) == -1) {
                        returnItem.setSplitPrice(BigDecimal.ZERO);
                    } else {
                        returnItem.setSplitPrice(itemUnitPrice.multiply(new BigDecimal(tradeItem.getNum() + "")));
                    }
                } else {
                    //该商品已退完
                    if (itemUnitPrice.compareTo(new BigDecimal("0.01")) == -1) {
                        returnItem.setSplitPrice(tradeItem.getSplitPrice());
                    } else {
                        long returnNum = tradeItem.getNum() - returnItem.getNum();
                        returnItem.setSplitPrice(tradeItem.getSplitPrice().subtract(itemUnitPrice.multiply(new BigDecimal(returnNum + ""))));
                    }
                }
                //非运费
            } else if (!ReturnReason.PRICE_DELIVERY.equals(returnOrder.getReturnReason()) ) {
                //只有有传递apply 对应的信息，则认为是新版本的售后
                if ((returnItem.getApplyKnowledge() == null
                        || returnItem.getApplyPoint() == null
                        || (returnItem.getApplyPoint() == 0L && returnItem.getApplyKnowledge() == 0L && returnItem.getApplyRealPrice().compareTo(BigDecimal.ZERO) == 0)
                        || returnItem.getApplyPoint() < 0
                        || returnItem.getApplyKnowledge() < 0
                        || returnItem.getApplyRealPrice().compareTo(BigDecimal.ZERO) < 0) && !ReturnReason.PRICE_DELIVERY.equals(returnOrder.getReturnReason())) {
                    throw new SbcRuntimeException("K-050459");
                }
                returnItem.setSplitPrice(returnItem.getApplyRealPrice());


                //获取当前商品可退 金额、知豆、积分
                int surplusNum = 0;
                long surplusPoint = 0L;
                long surplusKnowLedge = 0;
                BigDecimal surplusPrice = BigDecimal.ZERO;
                TradeItemSimpleVO tradeItemSimpleVO = skuId2TradeItemSimpleMap.get(returnItem.getSkuId());
                if (tradeItemSimpleVO != null) {
                    surplusPoint = (tradeItemSimpleVO.getPoints() == null ? 0 : tradeItemSimpleVO.getPoints()) - tradeItemSimpleVO.getReturnIngPoint() - tradeItemSimpleVO.getReturnCompletePoint();
                    surplusKnowLedge = (tradeItemSimpleVO.getKnowledge() == null ? 0 : tradeItemSimpleVO.getKnowledge()) - tradeItemSimpleVO.getReturnIngKnowledge() - tradeItemSimpleVO.getReturnCompleteKnowledge();
                    BigDecimal price = tradeItemSimpleVO.getSplitPrice()== null ? BigDecimal.ZERO : tradeItemSimpleVO.getSplitPrice();
                    surplusPrice = price.subtract(new BigDecimal(tradeItemSimpleVO.getReturnIngPrice())).subtract(new BigDecimal(tradeItemSimpleVO.getReturnCompletePrice()));
                    surplusNum = tradeItemSimpleVO.getNum().intValue() - tradeItemSimpleVO.getReturnIngNum() - tradeItemSimpleVO.getReturnCompleteNum();
                }

                //申请金额、数量不能大于剩余金额
                int diffNum = returnItem.getNum() - surplusNum;
                long diffPoint = returnItem.getApplyPoint() - surplusPoint;
                long diffKnowLedge = returnItem.getApplyKnowledge() - surplusKnowLedge;
                BigDecimal diffPrice = returnItem.getApplyRealPrice().subtract(surplusPrice);
                log.info("ReturnOrderService create tid:{} skuId:{} diffNum:{} diffPoint:{} diffKnowLedge:{} diffPrice:{}",
                        returnOrder.getTid(), returnItem.getSkuId(), diffNum, diffPoint, diffKnowLedge, diffPrice);
                if (diffNum > 0) {
                    throw new SbcRuntimeException("K-050453");
                }
                if (diffPoint > 0) {
                    throw new SbcRuntimeException("K-050454");
                }
                if (diffKnowLedge > 0) {
                    throw new SbcRuntimeException("K-050455");
                }
                if (diffPrice.compareTo(BigDecimal.ZERO) > 0) {
                    throw new SbcRuntimeException("K-050456");
                }

//                if (diffNum == 0 && diffPoint == 0 && diffKnowLedge == 0 && diffPrice.compareTo(BigDecimal.ZERO) == 0) {
//                    throw new SbcRuntimeException("K-050457");
//                }

                if (ReturnReason.PRICE_DIFF.equals(returnOrder.getReturnReason())) {
                    continue;
                }

                //表示部分申请，则此时最大申请金额 必须的小于 单价 * 数量
                if (diffNum != 0) {
                    //单价
                    BigDecimal itemUnitPrice = (tradeItem.getSplitPrice() == null ?BigDecimal.ZERO : tradeItem.getSplitPrice()).divide(new BigDecimal(tradeItem.getNum().toString()), 2, BigDecimal.ROUND_HALF_UP);
                    if (returnItem.getApplyRealPrice().compareTo(itemUnitPrice.multiply(new BigDecimal(returnItem.getNum().toString()))) > 0) {
                        throw new SbcRuntimeException("K-050456");
                    }
                    long itemUnitPoint = (tradeItem.getPoints() == null ? 0L : tradeItem.getPoints()) / tradeItem.getNum();
                    if (returnItem.getApplyPoint() - (itemUnitPoint * returnItem.getNum()) > 0) {
                        throw new SbcRuntimeException("K-050454");
                    }
                    long itemUnitKnowledge = tradeItem.getKnowledge() == null ? 0L : tradeItem.getKnowledge() / tradeItem.getNum();
                    if (returnItem.getApplyKnowledge() - (itemUnitKnowledge * returnItem.getNum()) > 0) {
                        throw new SbcRuntimeException("K-050455");
                    }
                }

            }
        }


        //是否boss创建并且申请特价状态
        BigDecimal realReturnPrice = BigDecimal.ZERO;
        Long points = 0L ,knowledge = 0L;
        boolean isSpecial = returnPrice.getApplyStatus() && operator.getPlatform() == Platform.BOSS;
        //这里表示的是用户的拆单价的累加
        realReturnPrice = isSpecial ? returnPrice.getApplyPrice() : returnOrder.getReturnItems().stream().map(ReturnItem::getSplitPrice).reduce(BigDecimal::add).get();
        // 计算积分
        if (Objects.nonNull(trade.getTradePrice().getPoints())) {
            points = getPoints(returnOrder, trade, false);
        }
        // 计算知豆
        if (Objects.nonNull(trade.getTradePrice().getKnowledge())) {
            knowledge = getKnowledge(returnOrder, trade, false);
        }
        returnOrder.getReturnPrice().setTotalPrice(realReturnPrice);
        returnOrder.getReturnPrice().setApplyPrice(realReturnPrice);

        if (returnOrder.getReturnPoints() == null) {
            returnOrder.setReturnPoints(ReturnPoints.builder().applyPoints(points).build());
        }
        if (returnOrder.getReturnKnowledge() == null) {
            returnOrder.setReturnKnowledge(ReturnKnowledge.builder().applyKnowledge(knowledge).build());
        }

        PayOrder payOrder = payOrderService.findPayOrderByOrderCode(returnOrder.getTid()).get();
        BigDecimal payOrderPrice = payOrder.getPayOrderPrice();
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                && trade.getBookingType() == BookingType.EARNEST_MONEY && StringUtils.isNotEmpty(trade.getTailOrderNo())) {
            payOrderPrice = payOrderPrice.add(payOrderService.findPayOrderByOrderCode(trade.getTailOrderNo()).get().getPayOrderPrice());
        }
        if (operator.getPlatform() == Platform.BOSS
                && PayType.fromValue(Integer.parseInt(trade.getPayInfo().getPayTypeId())) == PayType.ONLINE
                && payOrderPrice.compareTo(realReturnPrice.add(allReturnCompletePrice)) == -1) {
            throw new SbcRuntimeException("K-050126");
        }

        //如果当前为退差价或者运费，订单为作废，同时订单进行扭转为完成状态；
        log.info("returnOrderService  tid:{} returnOrder.getReturnReason() :{} flowState:{}", trade.getId(), returnOrder.getReturnReason(), trade.getTradeState().getFlowState());
        if ((returnOrder.getReturnReason() == ReturnReason.PRICE_DELIVERY
                || returnOrder.getReturnReason() == ReturnReason.PRICE_DIFF) && FlowState.VOID.equals(trade.getTradeState().getFlowState())) {
            //如果订单状态为作废，则进行扭转
            tradeService.reverse(returnOrder.getTid(), operator, TradeEvent.REVERSE_RETURN);
        }

        //退款
        if (returnOrder.getReturnType() == ReturnType.REFUND) {
            //创建退款单，会过滤已完成部分退款的的商品
            createRefund(returnOrder, operator, trade);
        } else {
            createReturn(returnOrder, operator, trade);
        }

        List<ReturnOrder> returnOrders = new ArrayList<>();
        //退单拆分
        splitReturnTrade(returnOrder, trade, returnOrders, providerDeliveryMap);
        String returnOrderId = StringUtils.EMPTY;

        for (ReturnOrder newReturnOrder : returnOrders) {
            returnOrderId = generatorService.generate("R");
            newReturnOrder.setBuyer(trade.getBuyer());
            newReturnOrder.setConsignee(trade.getConsignee());
            newReturnOrder.setCreateTime(LocalDateTime.now());
            newReturnOrder.setPayType(PayType.valueOf(trade.getPayInfo().getPayTypeName()));
            newReturnOrder.setPlatform(operator.getPlatform());
            newReturnOrder.setMiniProgramScene(trade.getMiniProgramScene());
            newReturnOrder.setId(returnOrderId);
            newReturnOrder.setMiniProgramScene(trade.getMiniProgramScene() == null ? 0 : trade.getMiniProgramScene());

            //记录日志
            newReturnOrder.appendReturnEventLog(new ReturnEventLog(operator, "创建退单", "创建退单", "", LocalDateTime.now()));

            count = newReturnOrder.getReturnItems().stream()
                    .filter(t -> GoodsType.VIRTUAL_COUPON.equals(t.getGoodsType())
                            || GoodsType.VIRTUAL_GOODS.equals(t.getGoodsType())).count();
            giftsCount = newReturnOrder.getReturnGifts().stream()
                    .filter(t -> GoodsType.VIRTUAL_COUPON.equals(t.getGoodsType())
                            || GoodsType.VIRTUAL_GOODS.equals(t.getGoodsType())).count();
            Boolean virtualFlag = false;
            if (count + giftsCount == newReturnOrder.getReturnItems().size() + newReturnOrder.getReturnGifts().size()) {
                virtualFlag = true;
            }

            if (Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP)
                    && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
                String aftersaleId = "";
                //只有代客退单才做校验
                if (Platform.WX_VIDEO != operator.getPlatform() && returnOrder.getReturnPrice().getApplyPrice().compareTo(BigDecimal.ZERO) > 0) {
                    for (ReturnOrder returnOrderParam : returnOrderList) {
                        //表示申请有现金
                        if (Objects.equals(returnOrderParam.getReturnType(), ReturnType.RETURN) && returnOrderParam.getReturnPrice().getApplyPrice().compareTo(BigDecimal.ZERO) > 0) {
                            throw new SbcRuntimeException("K-050419");
                        }
                    }
                }

                if (StringUtils.isBlank(newReturnOrder.getAftersaleId())) {
                    aftersaleId = wxOrderService.addEcAfterSale(newReturnOrder, trade);
                } else {
                    aftersaleId = newReturnOrder.getAftersaleId();
                }

                //保存退单
                if (StringUtils.isNotBlank(aftersaleId)) {
                    newReturnOrder.setAftersaleId(aftersaleId);
                }
            }

            newReturnOrder.setReturnFlowState(ReturnFlowState.INIT);
            returnOrderService.addReturnOrder(newReturnOrder);

            this.operationLogMq.convertAndSend(operator, "创建退单", "创建退单");


            Boolean auditFlag = true;
            //linkedMall退单，不可以自动审核
            if (ThirdPlatformType.LINKED_MALL.equals(newReturnOrder.getThirdPlatformType())) {
                auditFlag = false;
            }

            //判断是否要有商品
            if (returnOrder.getReturnReason() == null || !returnOrder.getReturnReason().getType().equals(ReturnReason.PRICE_DELIVERY.getType())) {
                //视频号订单不进行自动审核
                if (ChannelType.MINIAPP != trade.getChannelType() && !Objects.equals(MiniProgramSceneType.WECHAT_VIDEO.getIndex(), trade.getMiniProgramScene())) {
                    //虚拟商品自动审核
                    if (virtualFlag || (auditFlag && (operator.getPlatform() == Platform.BOSS || operator.getPlatform() == Platform.SUPPLIER))) {
                        audit(returnOrderId, operator, null);
                    }

                    // 虚拟自动收货
                    if (virtualFlag) {
                        receive(returnOrderId, operator);
                    }
                }
            }
            ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                    .addFlag(Boolean.TRUE)
                    .customerId(trade.getBuyer().getId())
                    .orderId(trade.getId())
                    .returnId(returnOrderId)
                    .build();
            returnOrderProducerService.returnOrderFlow(sendMQRequest);

            //售后单提交成功发送MQ消息
            log.info("ReturnOrderService create 创建退单 tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());
            if (CollectionUtils.isNotEmpty(newReturnOrder.getReturnItems())
                    || CollectionUtils.isNotEmpty(newReturnOrder.getReturnGifts())) {
                List<String> params;
                String pic;
                if (CollectionUtils.isNotEmpty(newReturnOrder.getReturnItems())) {
                    params = Lists.newArrayList(newReturnOrder.getReturnItems().get(0).getSkuName());
                    pic = newReturnOrder.getReturnItems().get(0).getPic();
                } else {
                    params = Lists.newArrayList(newReturnOrder.getReturnGifts().get(0).getSkuName());
                    pic = newReturnOrder.getReturnGifts().get(0).getPic();
                }
                this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                        ReturnOrderProcessType.AFTER_SALE_ORDER_COMMIT_SUCCESS,
                        params,
                        newReturnOrder.getId(),
                        newReturnOrder.getBuyer().getId(),
                        pic,
                        newReturnOrder.getBuyer().getAccount()
                );
            }



        }
        return returnOrderId;
    }
//
//    public Integer transformReturnFlowState(ReturnFlowState state, boolean isRefund) {
//        if (isRefund) {
//            // 仅退款
//            switch (state) {
//                case INIT:
//                    return BigDecimal.ROUND_HALF_UP;
//                case AUDIT:
//                    return BigDecimal.ROUND_HALF_UP;
//                case COMPLETED:
//                    return BigDecimal.ROUND_HALF_UP;
//                case REJECT_REFUND:
//                    return BigDecimal.ROUND_HALF_UP;
//                case VOID:
//                    return BigDecimal.ROUND_HALF_UP;
//                default:
//                    return null;
//            }
//        } else {
//            // 退货
//            switch (state) {
//                case INIT:
//                    return BigDecimal.ROUND_HALF_EVEN;
//                case AUDIT:
//                    return BigDecimal.ROUND_HALF_EVEN;
//                case DELIVERED:
//                    return BigDecimal.ROUND_HALF_DOWN;
//                case RECEIVED:
//                    return BigDecimal.ROUND_HALF_EVEN;
//                case COMPLETED:
//                    return BigDecimal.ROUND_CEILING;
//                case REJECT_RECEIVE:
//                    return BigDecimal.ROUND_DOWN;
//                case REJECT_REFUND:
//                    return BigDecimal.ROUND_HALF_UP;
//                case VOID:
//                    return BigDecimal.ROUND_HALF_UP;
//                default:
//                    return null;
//            }
//        }
//    }

    // 计算积分
    private Long getPoints(ReturnOrder returnOrder, Trade trade, boolean isRefund) {
        Long applyPoints = 0L;
        for (ReturnItem returnItem : returnOrder.getReturnItems()) {
            applyPoints = applyPoints + (returnItem.getApplyPoint() == null ? 0L : returnItem.getApplyPoint());
        }
        if (applyPoints > 0) {
            return applyPoints;
        }


        // 各商品均摊积分
        Map<String, Double> splitPointMap = new HashMap<>();
        // 各商品购买数量
        Map<String, Long> totalNumMap = new HashMap<>();
        // 各商品消耗积分
        Map<String, Long> pointsMap = new HashMap<>();
        List<TradeItem> tradeItems = trade.getTradeItems();
        Long points;
        for (TradeItem tradeItem : tradeItems) {
            points = Objects.nonNull(tradeItem.getPoints()) ? tradeItem.getPoints() : NumberUtils.LONG_ZERO;
            Double splitPoint = new BigDecimal(points).divide(new BigDecimal(tradeItem.getNum()), 2, BigDecimal.ROUND_DOWN).doubleValue();
            String skuId = tradeItem.getSkuId();
            splitPointMap.put(skuId, splitPoint);
            totalNumMap.put(skuId, tradeItem.getNum());
            pointsMap.put(skuId, points);
        }

        List<ReturnItem> returnItems = returnOrder.getReturnItems();
        // 可退积分计算
        return returnItems.stream()
                .map(returnItem -> {
                    String skuId = returnItem.getSkuId();
                    Long shouldPoints;
                    if (isRefund || returnItem.getNum() < returnItem.getCanReturnNum()) {
                        // 小于可退数量,直接均摊积分乘以数量
                        Double totalPoints = returnItem.getNum() * splitPointMap.get(skuId);
                        shouldPoints = totalPoints.longValue();
                    } else {
                        //大于等于可退数量 , 所用积分 - 已退积分(均摊积分*(购买数量-可退数量))
                        Double retiredPoints =
                                splitPointMap.get(skuId) * (totalNumMap.get(skuId) - returnItem.getCanReturnNum());
                        shouldPoints = pointsMap.get(skuId) - retiredPoints.longValue();
                    }
                    //设置单品应退积分
                    returnItem.setSplitPoint(shouldPoints);
                    return shouldPoints;
                })
                .reduce(0L, Long::sum);
    }

    // 计算知豆
    private Long getKnowledge(ReturnOrder returnOrder, Trade trade, boolean isRefund) {
        Long applyKnowledge = 0L;
        for (ReturnItem returnItem : returnOrder.getReturnItems()) {
            applyKnowledge = applyKnowledge + (returnItem.getApplyKnowledge() == null ? 0L : returnItem.getApplyKnowledge());
        }
        if (applyKnowledge > 0) {
            return applyKnowledge;
        }


        // 各商品均摊积分
        Map<String, Double> splitKnowledgeMap = new HashMap<>();
        // 各商品购买数量
        Map<String, Long> totalNumMap = new HashMap<>();
        // 各商品消耗积分
        Map<String, Long> knowledgeMap = new HashMap<>();
        List<TradeItem> tradeItems = trade.getTradeItems();
        Long knowledge;
        for (TradeItem tradeItem : tradeItems) {
            knowledge = Objects.nonNull(tradeItem.getKnowledge()) ? tradeItem.getKnowledge() : NumberUtils.LONG_ZERO;
            Double splitKnowledge = new BigDecimal(knowledge)
                    .divide(new BigDecimal(tradeItem.getNum()), 2, BigDecimal.ROUND_DOWN)
                    .doubleValue();
            String skuId = tradeItem.getSkuId();
            splitKnowledgeMap.put(skuId, splitKnowledge);
            totalNumMap.put(skuId, tradeItem.getNum());
            knowledgeMap.put(skuId, knowledge);
        }

        List<ReturnItem> returnItems = returnOrder.getReturnItems();
        // 可退积分计算
        return returnItems.stream()
                .map(returnItem -> {
                    String skuId = returnItem.getSkuId();
                    Long shouldKnowledge;
                    if (isRefund || returnItem.getNum() < returnItem.getCanReturnNum()) {
                        // 小于可退数量,直接均摊积分乘以数量
                        Double totalKnowledge = returnItem.getNum() * splitKnowledgeMap.get(skuId);
                        shouldKnowledge = totalKnowledge.longValue();
                    } else {
                        //大于等于可退数量 , 所用积分 - 已退积分(均摊积分*(购买数量-可退数量))
                        Double retiredPoints =
                                splitKnowledgeMap.get(skuId) * (totalNumMap.get(skuId) - returnItem.getCanReturnNum());
                        shouldKnowledge = knowledgeMap.get(skuId) - retiredPoints.longValue();
                    }
                    //设置单品应退积分
                    returnItem.setSplitKnowledge(shouldKnowledge);
                    return shouldKnowledge;
                })
                .reduce(0L, Long::sum);
    }

    /**
     * 创建退单快照
     *
     * @param returnOrder
     * @param operator
     */
    @GlobalTransactional
    public void transfer(ReturnOrder returnOrder, Operator operator) {
        Trade trade = tradeService.detail(returnOrder.getTid());
        //查询该订单所有退单
        List<ReturnOrder> returnOrderList = returnOrderRepository.findByTid(trade.getId());
        this.verifyIsExistsItemReturnOrder(returnOrder, returnOrderList);

        this.verifyNum(trade, returnOrder.getReturnItems(), (operator.getPlatform() == Platform.SUPPLIER || operator.getPlatform() == Platform.WX_VIDEO) ? 1 : null, returnOrder.getReturnReason());
        returnOrder.setReturnType(ReturnType.RETURN);

        Buyer buyer = new Buyer();
        buyer.setId(operator.getUserId());

        returnOrder.setBuyer(buyer);

        //计算总金额
//        BigDecimal totalPrice = returnOrder.getReturnItems().stream().map(t -> new BigDecimal(t.getNum()).multiply
// (t.getPrice()))
//                .reduce(BigDecimal::add).get();
//        returnOrder.getReturnPrice().setTotalPrice(totalPrice);

        // 计算并设置需要退的赠品
        if (Objects.nonNull(returnOrder.getReturnGift()) && returnOrder.getReturnGift()) {
            getAndSetReturnGifts(returnOrder, trade, returnOrderList);
        }

        ReturnOrderTransfer returnOrderTransfer = new ReturnOrderTransfer();
        KsBeanUtil.copyProperties(returnOrder, returnOrderTransfer);
        delTransfer(operator.getUserId());
        returnOrderTransfer.setId(UUIDUtil.getUUID());
        returnOrderTransferService.addReturnOrderTransfer(returnOrderTransfer);
    }

    /**
     * 获取并设置本次退单需要退的赠品信息
     *
     * @param returnOrder     本次退单
     * @param trade           对应的订单信息
     * @param returnOrderList 订单对应的所有退单
     * @author bail
     */
    private void getAndSetReturnGifts(ReturnOrder returnOrder, Trade trade, List<ReturnOrder> returnOrderList) {
        List<TradeMarketingVO> tradeMarketings = trade.getTradeMarketings();
        if (CollectionUtils.isNotEmpty(tradeMarketings)) {
            // 1.找到原订单的所有满赠的营销活动marketingList
            List<TradeMarketingVO> giftMarketings = tradeMarketings.stream().filter(tradeMarketing -> MarketingType
                    .GIFT.equals(tradeMarketing.getMarketingType())).collect(Collectors.toList());
            if (giftMarketings.size() > 0) {
                Map<String, TradeItem> tradeItemMap = trade.getTradeItems().stream().collect(Collectors.toMap
                        (TradeItem::getSkuId, Function.identity()));//原订单所有商品的Map,方便根据skuId快速找到对应的商品信息
                Map<String, TradeItem> giftItemMap = trade.getGifts().stream().collect(Collectors.toMap
                        (TradeItem::getSkuId, Function.identity()));//原订单所有赠品的Map,方便根据skuId快速找到对应的赠品信息
                List<ReturnOrder> comReturnOrders = filterFinishedReturnOrder(returnOrderList);//该订单之前已完成的退单list
                // (分批退单的场景)
                Map<String, ReturnItem> comReturnSkus = new HashMap<>();//已经退的商品汇总(根据skuId汇总所有商品的数量)
                Map<String, ReturnItem> currReturnSkus = returnOrder.getReturnItems().stream().collect(Collectors
                        .toMap(ReturnItem::getSkuId, Function.identity()));//本次退的商品汇总
                Map<String, ReturnItem> allReturnGifts = new HashMap<>();//可能需要退的赠品汇总
                Map<String, ReturnItem> comReturnGifts = new HashMap<>();//已经退的赠品汇总
                comReturnOrders.stream().forEach(reOrder -> {
                    reOrder.getReturnItems().stream().forEach(returnItem -> {
                        ReturnItem currItem = comReturnSkus.get(returnItem.getSkuId());
                        if (currItem == null) {
                            comReturnSkus.put(returnItem.getSkuId(), returnItem);
                        } else {
                            currItem.setNum(currItem.getNum().intValue() + returnItem.getNum().intValue());
                        }
                    });

                    if (CollectionUtils.isNotEmpty(reOrder.getReturnGifts())) {
                        reOrder.getReturnGifts().stream().forEach(retrunGift -> {
                            ReturnItem currGiftItem = comReturnGifts.get(retrunGift.getSkuId());
                            if (currGiftItem == null) {
                                comReturnGifts.put(retrunGift.getSkuId(), retrunGift);
                            } else {
                                currGiftItem.setNum(currGiftItem.getNum().intValue() + retrunGift.getNum().intValue());
                            }
                        });
                    }
                });

                // 2.遍历满赠营销活动list,验证每个活动对应的剩余商品(购买数量或金额-已退的总数或总金额)是否还满足满赠等级的条件
                //   PS: 已退的总数或总金额分为两部分: a.该订单关联的所有已完成的退单的商品 b.本次用户准备退货的商品
                giftMarketings.forEach(giftMarketing -> {
                    if (MarketingSubType.GIFT_FULL_AMOUNT.equals(giftMarketing.getSubType())) {
                        BigDecimal leftSkuAmount = giftMarketing.getSkuIds().stream().map(skuId -> {
                            TradeItem skuItem = tradeItemMap.get(skuId);
                            long comReSkuCount = comReturnSkus.get(skuId) == null ? 0L : comReturnSkus.get(skuId)
                                    .getNum().longValue();
                            long currReSkuCount = currReturnSkus.get(skuId) == null ? 0L : currReturnSkus.get(skuId)
                                    .getNum().longValue();
                            return skuItem.getLevelPrice().multiply(new BigDecimal(skuItem.getDeliveredNum() -
                                    comReSkuCount - currReSkuCount));//某商品的发货商品价格 - 已退商品价格 - 当前准备退的商品价格
                        }).reduce(BigDecimal::add).get();//剩余商品价格汇总

                        // 3.若不满足满赠条件,则退该活动的所有赠品,汇总到所有的退货赠品数量中(若满足满赠条件,则无需退赠品)
                        if (leftSkuAmount.compareTo(giftMarketing.getGiftLevel().getFullAmount()) < 0) {
                            setReturnGiftsMap(giftMarketing, allReturnGifts, giftItemMap);
                        }
                    } else if (MarketingSubType.GIFT_FULL_COUNT.equals(giftMarketing.getSubType())) {
                        long leftSkuCount = giftMarketing.getSkuIds().stream().mapToLong(skuId -> {
                            TradeItem skuItem = tradeItemMap.get(skuId);
                            long comReSkuCount = comReturnSkus.get(skuId) == null ? 0L : comReturnSkus.get(skuId)
                                    .getNum().longValue();
                            long currReSkuCount = currReturnSkus.get(skuId) == null ? 0L : currReturnSkus.get(skuId)
                                    .getNum().longValue();
                            return skuItem.getDeliveredNum().longValue() - comReSkuCount - currReSkuCount;//某商品的发货商品数
                            // - 已退商品数 - 当前准备退的商品数
                        }).sum();//剩余商品数量汇总

                        // 3.若不满足满赠条件,则退该活动的所有赠品,汇总到所有的退货赠品数量中(若满足满赠条件,则无需退赠品)
                        if (leftSkuCount < giftMarketing.getGiftLevel().getFullCount()) {
                            setReturnGiftsMap(giftMarketing, allReturnGifts, giftItemMap);
                        }
                    }
                });

                // 4.设置具体的退单赠品信息
                returnOrder.setReturnGifts(getReturnGiftList(trade, allReturnGifts, comReturnGifts));
            }
        }
    }

    /**
     * 不满足满赠条件时,需要退的所有赠品
     *
     * @param giftMarketing  某个满赠营销活动
     * @param allReturnGifts 不满足满赠条件,满赠营销活动中所有需要退的赠品信息
     * @param giftItemMap    赠品具体信息Map(获取除了skuId以外的详细信息)
     */
    private void setReturnGiftsMap(TradeMarketingVO giftMarketing, Map<String, ReturnItem> allReturnGifts, Map<String,
            TradeItem> giftItemMap) {
        // 不满足满赠条件,则退该活动的所有赠品,汇总到所有的退货赠品数量中
        giftMarketing.getGiftLevel().getFullGiftDetailList().stream().forEach(gift -> {
            ReturnItem currGiftItem = allReturnGifts.get(gift.getProductId());
            TradeItem giftDetail = giftItemMap.get(gift.getProductId());
            // 查不到,不做处理
            if (Objects.isNull(giftDetail)) {
                return;
            }
            if (currGiftItem == null) {
                ReturnItem returnItem = ReturnItem.builder().skuId(giftDetail.getSkuId())
                        .num(gift.getProductNum().intValue())
                        .skuName(giftDetail.getSkuName())
                        .skuNo(giftDetail.getSkuNo())
                        .pic(giftDetail.getPic()).price(giftDetail.getPrice())
                        .specDetails(giftDetail.getSpecDetails())
                        .unit(giftDetail.getUnit())
                        .supplyPrice(giftDetail.getSupplyPrice())
                        .providerPrice(giftDetail.getTotalSupplyPrice())
                        .thirdPlatformType(giftDetail.getThirdPlatformType())
                        .thirdPlatformSkuId(giftDetail.getThirdPlatformSkuId())
                        .thirdPlatformSpuId(giftDetail.getThirdPlatformSpuId())
                        .thirdPlatformSubOrderId(giftDetail.getThirdPlatformSubOrderId())
                        .build();
                allReturnGifts.put(gift.getProductId(), returnItem);
            } else {
                currGiftItem.setNum(currGiftItem.getNum().intValue() + gift.getProductNum().intValue());
            }
        });
    }

    /**
     * 获取具体的退单赠品信息
     *
     * @param trade          订单
     * @param allReturnGifts 不满足满赠条件,满赠营销活动中所有需要退的赠品信息
     * @param comReturnGifts 所有已完成退单中的退掉的赠品信息
     * @return
     */
    private List<ReturnItem> getReturnGiftList(Trade trade, Map<String, ReturnItem> allReturnGifts, Map<String,
            ReturnItem> comReturnGifts) {
        // 本次退单的退货赠品总数: 每个商品所有退货赠品数量 - 之前所有退单中已经退掉的赠品总数
        //   PS: 为了保证退单中赠品顺序与订单中的赠品顺序一致,遍历订单赠品,依次计算得出本次退单需要退的赠品list
        List<ReturnItem> returnGiftList = trade.getGifts().stream().map(tradeItem -> {
            ReturnItem readyGiftItem = allReturnGifts.get(tradeItem.getSkuId());//准备退的
            ReturnItem comGiftItem = comReturnGifts.get(tradeItem.getSkuId());//之前已完成退单已经退掉的
            if (readyGiftItem != null) {
                int totalNum = readyGiftItem.getNum() < tradeItem.getDeliveredNum().intValue() ? readyGiftItem.getNum
                        () : tradeItem.getDeliveredNum().intValue();//退货总数 与 发货总数对比,取最小的值
                //仅退款数量设置为赠品总数
                if (trade.getTradeState().getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED || trade
                        .getTradeState().getDeliverStatus()
                        == DeliverStatus.VOID) {
                    totalNum = readyGiftItem.getNum();
                }
                if (comGiftItem != null) {
                    int currNum = totalNum - comGiftItem.getNum();
                    if (currNum > 0) {
                        readyGiftItem.setNum(currNum);
                    } else {
                        return null;
                    }
                } else {
                    readyGiftItem.setNum(totalNum);
                }
                readyGiftItem.setProviderId(tradeItem.getProviderId());
                readyGiftItem.setGoodsType(tradeItem.getGoodsType());
                return readyGiftItem;
            }
            return null;
        }).filter(reGift -> reGift != null).collect(Collectors.toList());
        return returnGiftList;
    }


    /**
     * 删除订单快照
     *
     * @param userId
     */
    //@TccTransaction
    @Transactional
    public void delTransfer(String userId) {
        ReturnOrderTransfer returnOrderTransferByBuyerId = returnOrderTransferRepository.findReturnOrderTransferByBuyerId(userId);
        if (Objects.nonNull(returnOrderTransferByBuyerId)) {
            returnOrderTransferService.deleteReturnOrderTransfer(returnOrderTransferByBuyerId.getId());
        }
    }

    /**
     * 查询退单快照
     *
     * @param userId
     * @return
     */
    public ReturnOrder findTransfer(String userId) {
        ReturnOrder returnOrder = null;
        ReturnOrderTransfer returnOrderTransfer = returnOrderTransferRepository.findReturnOrderTransferByBuyerId(userId);
        if (returnOrderTransfer != null) {
            returnOrder = new ReturnOrder();
            KsBeanUtil.copyProperties(returnOrderTransfer, returnOrder);
        }
        return returnOrder;
    }

    /*@EsCacheAnnotation(name = "returnOrderESCacheService")
    public void insertES(ReturnOrder returnOrder) {
        returnOrderESRepository.delete(returnOrder.getId());
        returnOrderESRepository.index(returnOrder);
    }

    @EsCacheAnnotation(name = "returnOrderESCacheService")
    public void deleteES(ReturnOrder returnOrder) {
        returnOrderESRepository.delete(returnOrder.getId());
    }*/

    /**
     * 创建退货单
     *
     * @param returnOrder
     */
    private void createReturn(ReturnOrder returnOrder, Operator operator, Trade trade) {

        // 新增订单日志
        tradeService.returnOrder(returnOrder.getTid(), operator);

        this.verifyNum(trade, returnOrder.getReturnItems(), (operator.getPlatform() == Platform.SUPPLIER || operator.getPlatform() == Platform.WX_VIDEO) ? 1 : null , returnOrder.getReturnReason());

        returnOrder.setReturnType(ReturnType.RETURN);

        //本次的退或商品去除已完成的退单商品，赠品同理
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(trade.getId());
        this.filterCompletedReturnItem(returnOrders, returnOrder, trade);
        //填充退货商品信息
        Map<String, Integer> itemsCanReturnMap = findLeftItems(trade, (operator.getPlatform() == Platform.SUPPLIER || operator.getPlatform() == Platform.WX_VIDEO) ? 1 : null, returnOrder.getReturnReason());
        returnOrder.getReturnItems().forEach(item ->
                {
                    item.setSkuName(trade.skuItemMap().get(item.getSkuId()).getSkuName());
                    item.setPic(trade.skuItemMap().get(item.getSkuId()).getPic());
                    item.setSkuNo(trade.skuItemMap().get(item.getSkuId()).getSkuNo());
                    item.setSpecDetails(trade.skuItemMap().get(item.getSkuId()).getSpecDetails());
                    item.setUnit(trade.skuItemMap().get(item.getSkuId()).getUnit());
                    item.setCanReturnNum(itemsCanReturnMap.get(item.getSkuId()));
                    item.setBuyPoint(trade.skuItemMap().get(item.getSkuId()).getBuyPoint());
                    item.setBuyKnowledge(trade.skuItemMap().get(item.getSkuId()).getBuyKnowledge());
                    item.setGoodsSource(trade.skuItemMap().get(item.getSkuId()).getGoodsSource());
                    item.setThirdPlatformType(trade.skuItemMap().get(item.getSkuId()).getThirdPlatformType());
                    item.setThirdPlatformSpuId(trade.skuItemMap().get(item.getSkuId()).getThirdPlatformSpuId());
                    item.setThirdPlatformSkuId(trade.skuItemMap().get(item.getSkuId()).getThirdPlatformSkuId());
                    item.setProviderId(trade.skuItemMap().get(item.getSkuId()).getProviderId());
                    item.setSupplyPrice(trade.skuItemMap().get(item.getSkuId()).getSupplyPrice());
                }
        );

    }

    /**
     * 创建退款单
     *
     * @param returnOrder
     */
    private void createRefund(ReturnOrder returnOrder, Operator operator, Trade trade) {
        //校验该订单关联的退款单状态
        List<ReturnOrder> allReturnOrders = returnOrderRepository.findByTid(trade.getId());
//        if (!CollectionUtils.isEmpty(allReturnOrders)) {
//            Optional<ReturnOrder> optional = allReturnOrders.stream().filter(item -> item.getReturnType() == ReturnType.REFUND)
//                    .filter(item -> !(item.getReturnFlowState() == ReturnFlowState.VOID
//                            || item.getReturnFlowState() == ReturnFlowState.REJECT_REFUND
//                            || item.getReturnFlowState() == ReturnFlowState.COMPLETED)).findFirst();
//            if (optional.isPresent()) {
//                throw new SbcRuntimeException("K-050115");
//            }
//        }
        //本次的退单商品去除已完成的退单商品，赠品同理
        //此处订单对应的商品去除；
        //在退单中显示对应的订单信息
        this.filterCompletedReturnItem(allReturnOrders, returnOrder, trade);
        // 新增订单日志
        tradeService.returnOrder(returnOrder.getTid(), operator);
        returnOrder.setReturnType(ReturnType.REFUND);

        List<String> skuIds = trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        //填充商品信息
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder()
                .goodsInfoIds(skuIds).build()).getContext().getGoodsInfos();
        Map<String, GoodsInfoVO> goodsInfoVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            goodsInfoVOMap.putAll(goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, c -> c)));
        }

        //设置退货商品信息
        returnOrder.getReturnItems().forEach(item ->
                {
                    GoodsInfoVO vo = goodsInfoVOMap.get(item.getSkuId());
                    item.setCateTopId(vo != null ? vo.getCateTopId() : null);
                    item.setCateId(vo != null ? vo.getCateId() : null);
                    item.setGoodsId(vo != null ? vo.getGoodsId() : null);
                    item.setSpuId(vo != null ? vo.getGoodsId() : null);
                    item.setSpuName(vo != null ? vo.getGoodsInfoName() : null);
                    item.setBrandId(vo != null ? vo.getBrandId() : null);
                }
        );
    }

    /**
     * 去除已经完成退单的商品
     * @param returnOrders
     * @param trade
     */
    public void filterCompletedReturnItem(List<ReturnOrder> returnOrders, ReturnOrder returnOrder, Trade trade) {
        List<ReturnOrder> completedReturnOrderList = returnOrders.stream().filter(o -> o.getReturnFlowState() == ReturnFlowState.COMPLETED).collect(Collectors.toList());

        Map<String, Integer> completeGoodsInfoIdNumMap = new HashMap<>();
        Map<String, Integer> tradeAllGoodsInfoIdNumMap = new HashMap<>();
//        List<String> completedGiftIds = new ArrayList<>();
        for (ReturnOrder returnOrderParam : completedReturnOrderList) {
            for (ReturnItem returnItemParam : returnOrderParam.getReturnItems()) {
                int skuIdNum = completeGoodsInfoIdNumMap.get(returnItemParam.getSkuId()) == null ? 0 : completeGoodsInfoIdNumMap.get(returnItemParam.getSkuId());
                skuIdNum += returnItemParam.getNum();
                completeGoodsInfoIdNumMap.put(returnItemParam.getSkuId(), skuIdNum);
            }
        }

        List<TradeItem> resultTradeItem = new ArrayList<>();
        for (TradeItem tradeItemParam : trade.getTradeItems()) {
            tradeAllGoodsInfoIdNumMap.put(tradeItemParam.getSkuId(), tradeItemParam.getNum() == null ? 0 :tradeItemParam.getNum().intValue());
            Integer skuIdNum = completeGoodsInfoIdNumMap.get(tradeItemParam.getSkuId());
            if (skuIdNum != null && skuIdNum >= tradeItemParam.getNum()) {
                continue;
            }
            resultTradeItem.add(tradeItemParam);
        }


        List<ReturnItem> resultReturnTradeItem = new ArrayList<>();
        for (ReturnItem returnItemParam : returnOrder.getReturnItems()) {
            Integer skuIdAllNum = tradeAllGoodsInfoIdNumMap.get(returnItemParam.getSkuId());
            if (skuIdAllNum == null) {
                log.error("ReturnOrderService filterCompletedReturnItem 退单商品id:{} skuNo:{}, skuName:{} 参数传递有误", returnItemParam.getSkuId(), returnItemParam.getSkuNo(), returnItemParam.getSkuName());
                continue;
            }
            Integer skuIdCompleteNum = completeGoodsInfoIdNumMap.get(returnItemParam.getSkuId()) == null ? 0 : completeGoodsInfoIdNumMap.get(returnItemParam.getSkuId());
            int skuIdSurplusNum = skuIdAllNum - skuIdCompleteNum;
            if (skuIdSurplusNum <= 0) {
                continue;
            }

            if (returnItemParam.getNum() > skuIdSurplusNum) {
                returnItemParam.setNum(skuIdSurplusNum);
            }
            resultReturnTradeItem.add(returnItemParam);
        }
        //如果当前过滤完毕商品，为空，则查看当前是否是退还差价，如果退换差价则过滤商品
        if (!CollectionUtils.isEmpty(resultReturnTradeItem)) {
            trade.setTradeItems(resultTradeItem);
            returnOrder.setReturnItems(resultReturnTradeItem);
        } else {
            if (returnOrder.getReturnReason() != ReturnReason.PRICE_DIFF && returnOrder.getReturnReason() != ReturnReason.PRICE_DELIVERY) {
                trade.setTradeItems(resultTradeItem);
                returnOrder.setReturnItems(resultReturnTradeItem);
            }
        }
//        if (CollectionUtils.isNotEmpty(completedReturnOrderList)) {
//            List<String> completedGoodsInfoIds = new ArrayList<>();
//            List<String> completedGiftIds = new ArrayList<>();
//            completedReturnOrderList.forEach(o -> {
//                completedGoodsInfoIds.addAll(o.getReturnItems().stream().map(ReturnItem::getSkuId).collect(Collectors.toList()));
//                completedGiftIds.addAll(o.getReturnGifts().stream().map(ReturnItem::getSkuId).collect(Collectors.toList()));
//            });
//            trade.setTradeItems(trade.getTradeItems().stream().filter(item -> !completedGoodsInfoIds.contains(item.getSkuId())).collect(Collectors.toList()));
//            trade.setGifts(trade.getGifts().stream().filter(item -> !completedGiftIds.contains(item.getSkuId())).collect(Collectors.toList()));
//            returnOrder.setReturnItems(returnOrder.getReturnItems().stream().filter(item -> !completedGoodsInfoIds.contains(item.getSkuId())).collect(Collectors.toList()));
//            returnOrder.setReturnGifts(returnOrder.getReturnGifts().stream().filter(item -> !completedGiftIds.contains(item.getSkuId())).collect(Collectors.toList()));
//        }
    }

    /**
     * 根据动态条件统计
     *
     * @param request
     * @return
     */
    public long countNum(ReturnQueryRequest request) {
        Query query = new Query(request.build());
        return mongoTemplate.count(query, ReturnOrder.class);
    }


    /**
     * 分页查询退单列表
     *
     * @param request
     * @return
     */
    public Page<ReturnOrder> page(ReturnQueryRequest request) {
        Query query = new Query(request.build());
        long total = this.countNum(request);
        if (total < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), total);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        List<ReturnOrder> returnOrderList = mongoTemplate.find(query.with(request.getPageRequest()), ReturnOrder.class);
        fillActualReturnPrice(returnOrderList);

        // 填充退款单状态
        if (CollectionUtils.isNotEmpty(returnOrderList)) {
            List<String> ridList = returnOrderList.stream().map(ReturnOrder::getId).collect(Collectors.toList());
            RefundOrderRequest refundOrderRequest = new RefundOrderRequest();
            refundOrderRequest.setReturnOrderCodes(ridList);
            List<RefundOrder> refundOrders = refundOrderService.findAll(refundOrderRequest);
            returnOrderList.forEach(returnOrder -> {
                Optional<RefundOrder> refundOrderOptional = refundOrders.stream().filter(refundOrder -> refundOrder
                        .getReturnOrderCode().equals(returnOrder.getId())).findFirst();

                refundOrderOptional.ifPresent(refundOrder -> returnOrder.setRefundStatus(refundOrder.getRefundStatus()));
            });
        }
        return new PageImpl<>(returnOrderList, request.getPageable(), total);
    }

    public List<ReturnOrder> findByCondition(ReturnQueryRequest request) {
        return mongoTemplate.find(new Query(request.build()), ReturnOrder.class);
    }

    public List<ReturnOrder> findByPage(ReturnQueryRequest request) {
        return mongoTemplate.find(new Query(request.build()).with(request.getPageRequest()).with(request.getSort()), ReturnOrder.class);
    }

    /**
     * 填充实际退款金额，捕获异常，避免对主列表产生影响
     *
     * @param iterable
     */
    private void fillActualReturnPrice(Iterable<ReturnOrder> iterable) {
        try {
            List<String> returnOrderCodes = new ArrayList<>();
            // 如果有已退款的，查询退款流水的金额
            iterable.forEach(returnOrder -> {
                if (returnOrder.getReturnFlowState() == ReturnFlowState.COMPLETED) {
                    returnOrderCodes.add(returnOrder.getId());
                }
            });

            if (returnOrderCodes.size() > 0) {
                RefundOrderRequest request = new RefundOrderRequest();
                request.setReturnOrderCodes(returnOrderCodes);
                // 查询退款单信息
                List<RefundOrder> refundOrderList = refundOrderService.findAll(request);

                if (!CollectionUtils.isEmpty(refundOrderList)) {

                    // 实退金额赋值
                    iterable.forEach(returnOrder ->
                            refundOrderList.stream()
                                    .filter(o -> Objects.equals(returnOrder.getId(), o.getReturnOrderCode()))
                                    .findFirst()
                                    .ifPresent(o -> {
                                        if (Objects.nonNull(o.getRefundBill())) {
                                            //定金预售查询尾款实退金额
                                            RefundOrder tailOrder = null;
                                            if (StringUtils.isNotBlank(returnOrder.getBusinessTailId())) {
                                                tailOrder = refundOrderService.findRefundOrderByReturnOrderNo(returnOrder.getBusinessTailId());
                                            }
                                            BigDecimal tailActualReturnPrice = Objects.nonNull(tailOrder) ? tailOrder.getRefundBill().getActualReturnPrice() : BigDecimal.ZERO;
                                            returnOrder.getReturnPrice().setActualReturnPrice(o.getRefundBill()
                                                    .getActualReturnPrice().add(tailActualReturnPrice));
                                        }
                                    }));
                }
            }
        } catch (Exception e) {
            log.error("ReturnOrderService error", e);
        }

    }

    public ReturnOrder findById(String rid) {
        ReturnOrder returnOrder = returnOrderRepository.findById(rid).orElse(null);
        if (returnOrder == null) {
            throw new SbcRuntimeException("K-050003");
        }
        return returnOrder;
    }

    /**
     * 查找所有退货方式
     *
     * @return
     */
    public List<ReturnWay> findReturnWay() {
        return Arrays.asList(ReturnWay.values());
    }

    /**
     * 所有退货原因
     *
     * @return
     */
    public List<ReturnReason> findReturnReason(Integer replace) {
        ReturnReason[] values = ReturnReason.values();
        List<ReturnReason> result = new ArrayList<>();
        for (ReturnReason returnReason :values){
            if (replace != null && replace == 0) {
                if (replace.equals(returnReason.getReplace())) {
                    result.add(returnReason);
                }
            } else {
                result.add(returnReason);
            }
        }
        return result;
    }


    /**
     * 审核退单
     *
     * @param returnOrderId
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public void audit(String returnOrderId, Operator operator, String addressId) {
        //查询退单详情
        ReturnOrder returnOrder = findById(returnOrderId);

        // 查询订单相关的所有退单
        List<ReturnOrder> returnOrderAllList = returnOrderRepository.findByTid(returnOrder.getTid());

        // 筛选出已完成的退单
        List<ReturnOrder> returnCompleteOrderList = returnOrderAllList.stream()
                .filter(allReturnOrder -> allReturnOrder.getReturnFlowState() == ReturnFlowState.COMPLETED).collect(Collectors.toList());

        //计算所有已完成的退单总价格
        BigDecimal returnOrderCompletePrice = BigDecimal.ZERO;
        for (ReturnOrder returnOrderCompleteParam : returnCompleteOrderList) {
            //总退款价格
            ReturnPrice returnPrice = returnOrderCompleteParam.getReturnPrice();
            BigDecimal p = returnPrice.getApplyStatus() ? returnPrice.getApplyPrice() : returnPrice.getTotalPrice();
            returnOrderCompletePrice = returnOrderCompletePrice.add(p);
        }

        //订单申请的价格
        ReturnPrice returnPrice = returnOrder.getReturnPrice();
        //真实退款价格
        BigDecimal currentReturnOrderPrice = returnPrice.getApplyStatus() ? returnPrice.getApplyPrice() : returnPrice.getTotalPrice();

        //根据订单id获取支付订单
        Optional<PayOrder> payOrderOptional = payOrderService.findPayOrderByOrderCode(returnOrder.getTid());
        if (payOrderOptional.isPresent()) {
            PayOrder payOrder = payOrderOptional.get();
            //下单金额
            BigDecimal payOrderPrice = payOrder.getPayOrderPrice();
            //获取订单信息
            Trade trade = tradeService.detail(returnOrder.getTid());
            //预售商品
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                    && trade.getBookingType() == BookingType.EARNEST_MONEY && StringUtils.isNotEmpty(trade.getTailOrderNo())) {
                payOrderPrice = payOrderPrice.add(payOrderService.findPayOrderByOrderCode(trade.getTailOrderNo()).get().getPayOrderPrice());
            }
            // 退单金额校验 退款金额不可大于可退金额
            if (payOrder.getPayType() == PayType.ONLINE && payOrderPrice.compareTo(currentReturnOrderPrice.add(returnOrderCompletePrice)) == -1) {
                throw new SbcRuntimeException("K-050126");
            }
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY) {
                TradePrice tradePrice = trade.getTradePrice();
                BigDecimal needBackAmount = currentReturnOrderPrice;
                // 应退定金
                BigDecimal earnestPrice = BigDecimal.ZERO;
                // 应退尾款
                BigDecimal tailPrice = BigDecimal.ZERO;
                if (tradePrice.getCanBackEarnestPrice().compareTo(BigDecimal.ZERO) > 0) {
                    // 可退定金充足
                    if (tradePrice.getCanBackEarnestPrice().compareTo(needBackAmount) >= 0) {
                        tradePrice.setCanBackEarnestPrice(tradePrice.getCanBackEarnestPrice().subtract(needBackAmount));
                        earnestPrice = needBackAmount;
                        needBackAmount = BigDecimal.ZERO;
                    } else {
                        earnestPrice = tradePrice.getCanBackEarnestPrice();
                        // 还剩的退款金额
                        needBackAmount = needBackAmount.subtract(earnestPrice);
                        tradePrice.setCanBackEarnestPrice(BigDecimal.ZERO);
                    }
                }
                if (needBackAmount.compareTo(BigDecimal.ZERO) > 0) {
                    if (tradePrice.getCanBackTailPrice().compareTo(needBackAmount) < 0) {
                        throw new SbcRuntimeException("K-050126");
                    }
                    // 可退尾款充足
                    tradePrice.setCanBackTailPrice(tradePrice.getCanBackTailPrice().subtract(needBackAmount));
                    tailPrice = needBackAmount;
                }
                if (tailPrice.compareTo(BigDecimal.ZERO) >= 0 && StringUtils.isEmpty(returnOrder.getBusinessTailId())) {
                    returnOrder.setBusinessTailId(generatorService.generate("RT"));
                }
                // 需更新 订单
                tradeService.updateTrade(trade);
                if (earnestPrice.compareTo(BigDecimal.ZERO) >= 0) {
                    returnPrice.setEarnestPrice(earnestPrice);
                    if (returnOrder.getReturnType() == ReturnType.REFUND) {
                        refundOrderService.generateRefundOrderByReturnOrderCode(returnOrderId, returnOrder.getBuyer().getId(), earnestPrice,
                                returnOrder.getPayType());
                    }
                }
                if (tailPrice.compareTo(BigDecimal.ZERO) >= 0) {
                    returnPrice.setTailPrice(tailPrice);
                    returnPrice.setIsTailApply(true);
                    refundOrderService.generateTailRefundOrderByReturnOrderCode(returnOrder, returnOrder.getBuyer().getId(), tailPrice,
                            returnOrder.getPayType());
                }
                returnOrderService.updateReturnOrder(returnOrder);
            } else if (returnOrder.getReturnType() == ReturnType.REFUND) {
                //生成退款单 fefundOrder
                refundOrderService.generateRefundOrderByReturnOrderCode(returnOrderId, returnOrder.getBuyer().getId(), currentReturnOrderPrice,
                        returnOrder.getPayType());
            }
            ReturnAddress returnAddress = null;
            //构造退单收货地址
            if (StringUtils.isNotBlank(addressId)) {
                // 定制不需要 供应商地址
                returnAddress = wapperReturnAddress(addressId, returnOrder.getCompany().getStoreId());
            } else {
                if (Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN)) {
                    //设置默认退款地址
                    StoreReturnAddressListRequest storeReturnAddressListRequest = new StoreReturnAddressListRequest();
                    storeReturnAddressListRequest.setDelFlag(DeleteFlag.NO);
                    storeReturnAddressListRequest.setIsDefaultAddress(Boolean.TRUE);
                    storeReturnAddressListRequest.setShowAreaNameFlag(Boolean.TRUE);
                    storeReturnAddressListRequest.setStoreId(returnOrder.getCompany().getStoreId());
                    BaseResponse<StoreReturnAddressListResponse> listResponse = returnAddressQueryProvider.list(storeReturnAddressListRequest);
                    List<StoreReturnAddressVO> storeReturnAddressVOList = listResponse.getContext().getStoreReturnAddressVOList();
                    if (CollectionUtils.isNotEmpty(storeReturnAddressVOList)) {
                        returnAddress = this.packageReturnAddress(storeReturnAddressVOList.get(0));
                    }
                }
            }
            log.info("ReturnOrderService audit returnAddress: {} returnOrder.getReturnType(): {}", JSON.toJSONString(returnAddress), returnOrder.getReturnType());
            returnOrder.setReturnAddress(returnAddress);


            //修改退单状态
            ReturnStateRequest request = ReturnStateRequest
                    .builder()
                    .rid(returnOrderId)
                    .operator(operator)
                    .returnEvent(ReturnEvent.AUDIT)
                    .data(returnAddress)
                    .build();
            returnFSMService.changeState(request);


            //视频号不进行自动发货
            if (!Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
                //自动发货
                autoDeliver(returnOrderId, operator);
            } else {
                if (StringUtils.isBlank(returnOrder.getAftersaleId())) {
                    throw new SbcRuntimeException("K-050427");
                }
                //小程序订单
                WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
                wxDealAftersaleRequest.setAftersaleId(Long.valueOf(returnOrder.getAftersaleId()));
                BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponseBaseResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
                WxDetailAfterSaleResponse context = wxDetailAfterSaleResponseBaseResponse.getContext();
                if (context != null) {
                    WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
                    if (Objects.equals(afterSalesOrder.getType(), AfterSalesTypeEnum.RETURN.getCode()) ) {
                        this.addWxAfterSale(returnOrder,null, WxAfterSaleOperateType.RETURN, "退单审核");
                    }
                }
            }

            if(Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.MINI_PROGRAM.getIndex())){
                WxAfterSaleStatus wxAfterSaleStatus = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN) ? WxAfterSaleStatus.WAIT_RETURN : WxAfterSaleStatus.REFUNDING;
                this.addWxAfterSale(returnOrder,wxAfterSaleStatus, null, "小程序退单审核");
            }


            log.info("ReturnOrderService audit 审核订单 tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                    || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
               //售后审核通过发送MQ消息
               List<String> skuNameList;
               String picUrl;
               if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                   skuNameList = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName());
                   picUrl = returnOrder.getReturnItems().get(0).getPic();
               } else {
                   skuNameList = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName());
                   picUrl = returnOrder.getReturnGifts().get(0).getPic();
               }
               this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                       ReturnOrderProcessType.AFTER_SALE_ORDER_CHECK_PASS,
                       skuNameList,
                       returnOrder.getId(),
                       returnOrder.getBuyer().getId(),
                       picUrl,
                       returnOrder.getBuyer().getAccount());
           }

            /**
             * 1.判断订单是否已完成发货
             * 2.调用ERP接口创建退货单
             if (trade.getCycleBuyFlag()) {

             //判断订单里面里面的赠品是否有虚拟或者电子卡券
             List<TradeItem> gifts=trade.getGifts().stream().filter(g->GoodsType.VIRTUAL_GOODS.equals(g.getGoodsType())|| GoodsType.VIRTUAL_COUPON.equals(g.getGoodsType()) ).collect(Collectors.toList());
             //判断里面是否有已经发货日历
             List<DeliverCalendar> deliverCalendarList=trade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus()==CycleDeliverStatus.SHIPPED).collect(Collectors.toList());
             //是否仅退款
             Boolean  isRefund = trade.getTradeState().getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED || trade.getTradeState().getDeliverStatus()== DeliverStatus.VOID || (CollectionUtils.isNotEmpty(gifts) && CollectionUtils.isEmpty(deliverCalendarList));

             if(CollectionUtils.isNotEmpty(deliverCalendarList)){
             isRefund=false;
             }
             if (!isRefund) {
             List<ReturnTradeItemVO> returnOrderItemList = this.getReturnOrderItemList(returnOrder);
             ERPTradePaymentVO erpTradePaymentVO =new ERPTradePaymentVO();
             List<ERPTradePaymentVO> erpTradePaymentVOList = new ArrayList<>();
             //获取订单支付方式
             if(Objects.isNull(trade.getPayWay())){
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
             }else {
             switch(trade.getPayWay()){
             case WECHAT:
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.weixin.getStateId());
             case ALIPAY:
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.aliPay.getStateId());
             default:
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
             break;
             }
             }

             log.info("price==========>:{}",price);
             log.info("returnOrder===============>:{}",returnOrder);
             erpTradePaymentVO.setPayment(String.valueOf(price));
             erpTradePaymentVOList.add(erpTradePaymentVO);

             //收货人信息
             Consignee consignee = trade.getConsignee();

             Map<Enum, String> addrMap = new HashMap<>();
             //排除周期购有赞老订单
             if(trade.getParentId().startsWith(GeneratorService._PREFIX_YOUZAN_TRADE_ID)){
             //todo 增加地址省市区信息字段
             addrMap.put(AddrLevel.PROVINCE,trade.getConsignee().getProvinceName());
             addrMap.put(AddrLevel.CITY,trade.getConsignee().getCityName());
             addrMap.put(AddrLevel.DISTRICT,trade.getConsignee().getAreaName());
             }else{
             //提取平台地址数据
             PlatformAddressListRequest platformAddressListRequest =
             PlatformAddressListRequest.builder().addrIdList(Arrays.asList(consignee.getProvinceId().toString(),
             consignee.getCityId().toString()
             , consignee.getAreaId().toString())).build();
             //不填充叶子节点
             platformAddressListRequest.setLeafFlag(false);
             BaseResponse<PlatformAddressListResponse> platformAddressListResponseBaseResponse = platformAddressQueryProvider.list(platformAddressListRequest);
             List<PlatformAddressVO> platformAddressVOList =
             platformAddressListResponseBaseResponse.getContext().getPlatformAddressVOList();
             platformAddressVOList.stream().forEach(platformAddressVO -> {
             addrMap.put(platformAddressVO.getAddrLevel(),platformAddressVO.getAddrName());
             });
             }


             ReturnTradeCreateRequst returnTradeCreateRequst = ReturnTradeCreateRequst.builder()
             .buyerMobile(trade.getBuyer().getAccount())
             .returnType(ReturnTradeType.RETURN.getCode())
             .typeCode(String.valueOf(returnOrder.getReturnReason().getType()))
             .tradeNo(returnOrder.getPtid())
             .tradeItems(returnOrderItemList)
             .refundDetail(erpTradePaymentVOList)
             .receiveName(consignee.getName())
             .receiverMobile(consignee.getPhone())
             .receiverProvince(addrMap.get(AddrLevel.PROVINCE))
             .receiverCity(addrMap.get(AddrLevel.CITY))
             .receiverDistrict(addrMap.get(AddrLevel.DISTRICT))
             .receiverAddress(consignee.getDetailAddress())
             .build();
             log.info("returnTradeCreateRequst================>:{}",returnTradeCreateRequst);
             guanyierpProvider.createReturnOrder(returnTradeCreateRequst);

             }
             }else {
             if(trade.getTradeState().getDeliverStatus().equals(DeliverStatus.SHIPPED)){
             List<ReturnTradeItemVO> returnOrderItemList = this.getReturnOrderItemList(returnOrder);
             ERPTradePaymentVO erpTradePaymentVO =new ERPTradePaymentVO();
             List<ERPTradePaymentVO> erpTradePaymentVOList = new ArrayList<>();
             //获取订单支付方式
             if(Objects.isNull(trade.getPayWay())){
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
             }else {
             switch(trade.getPayWay()){
             case WECHAT:
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.weixin.getStateId());
             case ALIPAY:
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.aliPay.getStateId());
             default:
             erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
             break;
             }
             }

             log.info("price==========>:{}",price);
             log.info("returnOrder===============>:{}",returnOrder);
             erpTradePaymentVO.setPayment(String.valueOf(price));
             erpTradePaymentVOList.add(erpTradePaymentVO);

             //收货人信息
             Consignee consignee = trade.getConsignee();

             Map<Enum, String> addrMap = new HashMap<>();
             //排除周期购有赞老订单
             if(trade.getParentId().startsWith(GeneratorService._PREFIX_YOUZAN_TRADE_ID)){
             //todo 增加地址省市区信息字段
             addrMap.put(AddrLevel.PROVINCE,trade.getConsignee().getProvinceName());
             addrMap.put(AddrLevel.CITY,trade.getConsignee().getCityName());
             addrMap.put(AddrLevel.DISTRICT,trade.getConsignee().getAreaName());
             }else{
             //提取平台地址数据
             PlatformAddressListRequest platformAddressListRequest =
             PlatformAddressListRequest.builder().addrIdList(Arrays.asList(consignee.getProvinceId().toString(),
             consignee.getCityId().toString()
             , consignee.getAreaId().toString())).build();
             //不填充叶子节点
             platformAddressListRequest.setLeafFlag(false);
             BaseResponse<PlatformAddressListResponse> platformAddressListResponseBaseResponse = platformAddressQueryProvider.list(platformAddressListRequest);
             List<PlatformAddressVO> platformAddressVOList =
             platformAddressListResponseBaseResponse.getContext().getPlatformAddressVOList();
             platformAddressVOList.stream().forEach(platformAddressVO -> {
             addrMap.put(platformAddressVO.getAddrLevel(),platformAddressVO.getAddrName());
             });
             }


             ReturnTradeCreateRequst returnTradeCreateRequst = ReturnTradeCreateRequst.builder()
             .buyerMobile(trade.getBuyer().getAccount())
             .returnType(ReturnTradeType.RETURN.getCode())
             .typeCode(String.valueOf(returnOrder.getReturnReason().getType()))
             .tradeNo(returnOrder.getPtid())
             .tradeItems(returnOrderItemList)
             .refundDetail(erpTradePaymentVOList)
             .receiveName(consignee.getName())
             .receiverMobile(consignee.getPhone())
             .receiverProvince(addrMap.get(AddrLevel.PROVINCE))
             .receiverCity(addrMap.get(AddrLevel.CITY))
             .receiverDistrict(addrMap.get(AddrLevel.DISTRICT))
             .receiverAddress(consignee.getDetailAddress())
             .build();
             log.info("returnTradeCreateRequst================>:{}",returnTradeCreateRequst);
             guanyierpProvider.createReturnOrder(returnTradeCreateRequst);
             }
             }*/
        }
    }

    /**
     * 退单商品列表
     *
     * @param returnOrder
     * @return
     */
    private List<ReturnTradeItemVO> getReturnOrderItemList(ReturnOrder returnOrder) {
        //调用ERP接口创建退货单
        List<ReturnItem> returnItems = returnOrder.getReturnItems();
        List<ReturnItem> returnGifts = returnOrder.getReturnGifts();
        List<ReturnItem> totalReturnItems = Stream.of(returnItems, returnGifts)
                .flatMap(Collection::stream).collect(Collectors.toList());
        List<ReturnTradeItemVO> returnTradeItemVOS = new ArrayList<>();
        totalReturnItems.forEach(returnItem -> {
            BaseResponse<GoodsInfoByIdResponse> goodsInfoByIdResponse = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder()
                    .goodsInfoId(returnItem.getSkuId()).build());
            ReturnTradeItemVO returnTradeItemVO = ReturnTradeItemVO.builder()
                    .spuCode(goodsInfoByIdResponse.getContext().getErpGoodsNo())
                    .skuCode(goodsInfoByIdResponse.getContext().getCombinedCommodity() != null && goodsInfoByIdResponse.getContext().getCombinedCommodity() ? "" : goodsInfoByIdResponse.getContext().getErpGoodsInfoNo())
                    .qty(returnItem.getNum())
                    .build();
            returnTradeItemVOS.add(returnTradeItemVO);
        });
        return returnTradeItemVOS;
    }

    /**
     * 查询可退金额
     *
     * @param rid
     * @return
     */
    public BigDecimal queryRefundPrice(String rid) {
        ReturnOrder returnOrder = findById(rid);
        return returnOrder.getReturnPrice().getTotalPrice();
    }

    /**
     * 退货发出
     *
     * @param rid
     * @param logistics
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public void deliver(String rid, ReturnLogistics logistics, Operator operator) {
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.DELIVER)
                .data(logistics)
                .build();
        returnFSMService.changeState(request);


//        WxAfterSaleOperateType
//        this.addWxAfterSale(findById(rid), null, WxAfterSaleOperateType.UPLOAD_RETURN_INFO.getIndex(), "上传物流信息");
        ReturnOrder returnOrder = this.findById(rid);

        if (Platform.WX_VIDEO != operator.getPlatform() && returnOrder.getReturnPrice().getApplyPrice().compareTo(BigDecimal.ZERO) > 0) {
            wxOrderService.uploadReturnInfo(returnOrder);  //上传物流信息
        }

    }


    /**
     * 收货
     *
     * @param rid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void receive(String rid, Operator operator) {

        // 查询退单信息
        ReturnOrder returnOrder = findById(rid);

        // 生成财务退款单
        ReturnPrice returnPrice = returnOrder.getReturnPrice();
        if (Objects.isNull(returnPrice.getApplyPrice())) {
            return;
        }
        BigDecimal price = returnPrice.getApplyStatus() ? returnPrice.getApplyPrice() : returnPrice.getTotalPrice();
        refundOrderService.generateRefundOrderByReturnOrderCode(rid, returnOrder.getBuyer().getId(), price,
                returnOrder.getPayType());
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.RECEIVE)
                .build();
        returnFSMService.changeState(request);

        //退单的供应商
        if (isProviderFull(returnOrder)) {
            // 更新子单状态
            updateProviderTrade(returnOrder);
        }
        this.addWxAfterSale(returnOrder,WxAfterSaleStatus.REFUNDING,null, "收货");
        Trade trade = tradeService.detail(returnOrder.getTid());

        //周期购订单部分发货退货退款
        if (trade.getCycleBuyFlag()) {
            //作废主订单
            tradeService.voidTrade(returnOrder.getTid(), operator);
            trade = tradeService.detail(returnOrder.getTid());
            trade.setRefundFlag(true);
            tradeService.updateTrade(trade);
        } else {
            //视频号的订单如果 拒绝退款不进行作废处理
            if (Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP)
                    && Objects.equals(returnOrder.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
                return;
            } else {
                if (FlowState.VOID.equals(trade.getTradeState().getFlowState())) {
                    log.info("ReturnOrderService receive tid:{} state flowState is void return", trade.getId());
                    return;
                }
                //判断是否全量退货完成
                if (isReturnFull(returnOrder) && providerTradeAllVoid(returnOrder)) {
                    //作废主订单
                    tradeService.voidTrade(returnOrder.getTid(), operator);
                    trade = tradeService.detail(returnOrder.getTid());
                    trade.setRefundFlag(true);
                    tradeService.updateTrade(trade);
                }
            }
        }
    }

    /**
     * 子订单所对应的商品是否全部退还商家
     *
     * @param returnOrder
     * @return
     */
    private boolean isProviderFull(ReturnOrder returnOrder) {

        String ptid = returnOrder.getPtid();

        if (StringUtils.isNotBlank(ptid)) {
            List<ReturnOrder> returnOrders = returnOrderRepository.findByPtid(ptid);

            List<ReturnItem> returnItems = returnOrders.stream()
                    .filter(item -> item.getReturnFlowState() == ReturnFlowState.COMPLETED
                            || item.getReturnFlowState() == ReturnFlowState.RECEIVED
                            || item.getReturnFlowState() == ReturnFlowState.REJECT_REFUND)
                    .flatMap(item -> item.getReturnItems()
                            .stream())
                    .collect(Collectors.toList());

            List<TradeItem> tradeItems = providerTradeService.findbyId(ptid).getTradeItems();
            log.info("退单商品集合>>>" + JSON.toJSONString(returnItems));
            log.info("子单商品集合>>>" + JSON.toJSONString(tradeItems));
            int returnNum = returnItems.stream().collect(Collectors.summingInt(ReturnItem::getNum));
            Long num = tradeItems.stream().collect(Collectors.summingLong(TradeItem::getNum));
            log.info("退单商品数量>>>" + returnNum);
            log.info("子单商品数量>>>" + num);
            if (returnNum == num.intValue()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void updateProviderTrade(ReturnOrder returnOrder) {
        String ptid = returnOrder.getPtid();
        if (StringUtils.isNotBlank(ptid)) {
            log.info(" request updateProviderTrade 参数为：{}", JSON.toJSONString(returnOrder));
            TradeGetByIdResponse tradeGetByIdResponse = providerTradeQueryProvider.providerGetById(TradeGetByIdRequest.builder().tid(ptid).build()).getContext();
            if (tradeGetByIdResponse != null && tradeGetByIdResponse.getTradeVO() != null) {
                TradeVO tradeVO = tradeGetByIdResponse.getTradeVO();
                ProviderTrade providerTrade = KsBeanUtil.convert(tradeVO, ProviderTrade.class);
                if (providerTrade.getTradeState() != null) {
                    providerTrade.getTradeState().setFlowState(FlowState.VOID);
                    providerTradeService.updateProviderTrade(providerTrade);
                    //第三方作废
                    linkedMallTradeService.voidTrade(providerTrade.getId());
                }
            }
        }

    }


    /**
     * 拒绝收货
     *
     * @param rid
     * @param reason
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public void rejectReceive(String rid, String reason, Operator operator) {
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.REJECT_RECEIVE)
                .data(reason)
                .build();
        returnFSMService.changeState(request);
        // 拒绝退单时，发送MQ消息
        ReturnOrder returnOrder = this.findById(rid);

        if (Platform.WX_VIDEO != operator.getPlatform()) {
            WxAfterSaleOperateType wxAfterSaleType = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN) ? WxAfterSaleOperateType.REJECT : WxAfterSaleOperateType.CANCEL;
            this.addWxAfterSale(returnOrder,WxAfterSaleStatus.REJECT_RETURN, wxAfterSaleType, "拒绝收货");
        }

        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.FALSE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(rid)
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);
        log.info("ReturnOrderService rejectReceive 拒绝收货 tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());

        //退货物品拒收通知发送MQ消息
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.RETURN_ORDER_GOODS_REJECT,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }

    }

    /**
     * 退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void refund(String rid, Operator operator, BigDecimal price) {
        ReturnOrder returnOrder = findById(rid);
        Trade trade = tradeService.detail(returnOrder.getTid());
        ReturnPrice returnPrice = returnOrder.getReturnPrice();
        returnPrice.setActualReturnPrice(price);
        returnOrder.setReturnPrice(returnPrice);
        if (trade.getGrouponFlag()) {
            //拼团订单退款后的处理
            modifyGrouponInfo(returnOrder, trade);
        }
        //退单状态
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.REFUND)
                .data(price)
                .build();
        returnFSMService.changeState(request);
        this.addWxAfterSale(returnOrder,Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN)?WxAfterSaleStatus.RETURNED:WxAfterSaleStatus.REFUNDED, null, "商家退款");


        Map<String, TradeReturn> skuIdTradeReturnMap = new HashMap<>();
        //当前退单申请中的 商品数量
        Map<String, Integer> returnItemSkuIdNumMap = new HashMap<>();
        for (ReturnItem returnItemParam : returnOrder.getReturnItems()) {
            returnItemSkuIdNumMap.put(returnItemParam.getSkuId(), returnItemParam.getNum());
        }
        //获取所有子单
        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(returnOrder.getTid());
        for (ProviderTrade providerTradeParam : providerTradeList) {
            if (!providerTradeParam.getId().equals(returnOrder.getPtid())) {
                continue;
            }
            for (TradeItem tradeItemParam : providerTradeParam.getTradeItems()) {
                Integer skuIdNum = returnItemSkuIdNumMap.get(tradeItemParam.getSkuId());
                skuIdNum = skuIdNum == null ? 0 : skuIdNum;
                TradeReturn tradeReturn = tradeItemParam.getTradeReturn();
                if (tradeReturn == null) {
                    tradeReturn = new TradeReturn();
                    tradeItemParam.setTradeReturn(tradeReturn);
                }
                if (tradeReturn.getCreateTime() == null) {
                    tradeReturn.setCreateTime(LocalDateTime.now());
                }
                tradeReturn.setModifyTime(LocalDateTime.now());
                //商品完成退单的数量
                Integer returnCompleteNum = tradeReturn.getReturnCompleteNum() == null ? 0 : tradeReturn.getReturnCompleteNum();
                returnCompleteNum += skuIdNum;
                //商品下单数量
                Integer tradeItemNum = tradeItemParam.getNum() == null ? 0 : tradeItemParam.getNum().intValue();
                if (returnCompleteNum > tradeItemNum) {
                    log.error("ReturnOrderService tid:{} pid:{} 退款完成以后订单统计数量 skuId:{} skuName:{} 下单数量为:{} 完成的数量为:{} 有误，需要核验",
                            returnOrder.getTid(), returnOrder.getPtid(), tradeItemParam.getSkuId(), tradeItemParam.getSkuName(), tradeItemNum, returnCompleteNum);
                    returnCompleteNum = tradeItemNum;
                    tradeItemParam.setDeliverStatus(DeliverStatus.VOID);
                }
                tradeReturn.setReturnCompleteNum(returnCompleteNum);
                skuIdTradeReturnMap.put(tradeItemParam.getSkuId(), tradeReturn);
            }
            providerTradeService.updateProviderTrade(providerTradeParam);
        }

        trade.setRefundFlag(true);
        //更新主订单的退单数量
        for (TradeItem tradeItemParam : trade.getTradeItems()) {
            TradeReturn tradeReturn = skuIdTradeReturnMap.get(tradeItemParam.getSkuId());
            if (tradeReturn == null) {
                continue;
            }
            tradeItemParam.setTradeReturn(tradeReturn);
        }
        tradeService.updateTrade(trade);


        //退款，如果全部退款啦，则订单作废
        if (returnOrder.getReturnType() == ReturnType.REFUND) {
            //判断当前是否是全部退款，如果是则作废主单，
            boolean isTradeVoid = true;
            for (ProviderTrade providerTradeParam : providerTradeList) {
                if (!providerTradeParam.getId().equals(returnOrder.getPtid())) {
                    //判断当前子单子如果没有完成，则主订单不进行作废
                    if (!FlowState.VOID.equals(providerTradeParam.getTradeState().getFlowState())) {
                        isTradeVoid = false;
                    }
                    continue;
                }
                boolean isReturnOrderAllComplete = true;
                for (TradeItem tradeItemParam : providerTradeParam.getTradeItems()) {
                    if (tradeItemParam.getTradeReturn().getReturnCompleteNum() != tradeItemParam.getNum().intValue()) {
                        isReturnOrderAllComplete = false;
                    }
                }
                if (isReturnOrderAllComplete) {
                    providerTradeParam.getTradeState().setFlowState(FlowState.VOID);
                    providerTradeService.updateProviderTrade(providerTradeParam);
                    //TODO 这里不确定是否要删除 duanlsh
                    mongoTemplate.updateMulti(new Query(Criteria.where("parentId").is(providerTradeParam.getId())), new Update().set("tradeState.flowState", FlowState.VOID), ThirdPlatformTrade.class);
                } else {
                    isTradeVoid = false;
                }
            }
            //释放库存
            freeStock(returnOrder, trade);

            if (isTradeVoid) {
                //作废主订单
                tradeService.voidTrade(returnOrder.getTid(), operator);
                trade.getTradeState().setEndTime(LocalDateTime.now());
            }  else if(providerTradeAllEnd(returnOrder)){
                //zi订单或作废或已经全部发货，修改主订单为待收货
                trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
                trade.getTradeState().setFlowState(FlowState.DELIVERED);
                tradeService.updateTrade(trade);
            }
        }

        if (returnOrder.getReturnType() == ReturnType.RETURN) {

        }
        
        String businessId = trade.getPayInfo().isMergePay() ? trade.getParentId() : trade.getId();
        if (returnOrder.getPayType() == PayType.OFFLINE) {
            saveReconciliation(returnOrder, "", businessId, "", returnOrder.getReturnPrice().getApplyStatus() ?
                    returnOrder.getReturnPrice().getApplyPrice() : returnOrder.getReturnPrice().getTotalPrice(), "");
        }

    }

    /**
     * 判断子单是否全部作废，再决定是否作废主订单
     *
     * @param returnOrder
     * @return
     */
    public boolean providerTradeAllVoid(ReturnOrder returnOrder) {
        //子订单
        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(returnOrder.getTid());
        List<ProviderTrade> noVoidProviderTrades = providerTrades.stream()
                .filter(trade -> trade.getTradeState().getFlowState() != FlowState.VOID)
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(noVoidProviderTrades);
    }

    /**
     * 判断子单是否全部终态，
     *
     * @param returnOrder
     * @return
     */
    public boolean providerTradeAllEnd(ReturnOrder returnOrder) {
        //子订单
        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(returnOrder.getTid());
        List<ProviderTrade> noVoidProviderTrades = providerTrades.stream()
                .filter(trade -> trade.getTradeState().getFlowState() != FlowState.VOID && trade.getTradeState().getFlowState() != FlowState.DELIVERED)
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(noVoidProviderTrades);
    }


    /**
     * 保存退款对账明细
     *
     * @param returnOrder
     * @param payWayStr
     */
    @Transactional
    public void saveReconciliation(ReturnOrder returnOrder, String payWayStr, String businessId, String tradeNo, BigDecimal amount, String returnOrderId) {
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(StringUtils.isNotEmpty(returnOrderId) ? returnOrderId : returnOrder.getId());
        if (Objects.isNull(refundOrder)) {
            return;
        }
        addReconciliation(refundOrder, returnOrder, payWayStr, businessId, tradeNo, amount);
    }

    /**
     * 保存退款对账明细
     *
     * @param returnOrder
     * @param payWayStr
     */
    @Transactional
    public void saveReconciliation(RefundOrder refundOrder, ReturnOrder returnOrder, String payWayStr, String businessId, String tradeNo, BigDecimal amount) {
        if (Objects.isNull(refundOrder)) {
            return;
        }
        addReconciliation(refundOrder, returnOrder, payWayStr, businessId, tradeNo, amount);
    }

    /**
     * 保存退款对账明细
     *
     * @param returnOrder
     * @param payWayStr
     */
    @Transactional
    public void addReconciliation(RefundOrder refundOrder, ReturnOrder returnOrder, String payWayStr, String businessId, String tradeNo, BigDecimal amount) {
        AccountRecordAddRequest reconciliation = new AccountRecordAddRequest();
        if (Objects.nonNull(amount)) {
            reconciliation.setAmount(amount);
        } else {
            reconciliation.setAmount(returnOrder.getReturnPrice().getApplyStatus() ?
                    returnOrder.getReturnPrice().getApplyPrice() : returnOrder.getReturnPrice().getTotalPrice());
        }
        reconciliation.setCustomerId(returnOrder.getBuyer().getId());
        reconciliation.setCustomerName(returnOrder.getBuyer().getName());
        reconciliation.setOrderCode(returnOrder.getTid());
        reconciliation.setOrderTime(returnOrder.getCreateTime());
        reconciliation.setTradeTime(Objects.isNull(refundOrder.getRefundBill()) ? LocalDateTime.now() :
                refundOrder.getRefundBill().getCreateTime());

        if (StringUtils.isNotBlank(businessId)) {
            // 根据订单id查询流水号并存进对账明细
            TradeRecordByOrderCodeRequest request = new TradeRecordByOrderCodeRequest();
            request.setOrderId(businessId);
            BaseResponse<PayTradeRecordResponse> record = payQueryProvider.getTradeRecordByOrderCode(request);
            if (Objects.isNull(record)) {
                record = payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest(businessId));
            }
            if (Objects.nonNull(record) && Objects.nonNull(record.getContext()) && StringUtils.isNotEmpty(record
                    .getContext().getTradeNo())) {
                tradeNo = record.getContext().getTradeNo();
            }
        }
        reconciliation.setTradeNo(tradeNo);
        // 退款金额等于0 退款渠道标记为银联渠道
        if (refundOrder.getReturnPrice().compareTo(BigDecimal.ZERO) == 0) {
            payWayStr = "unionpay";
        }
        PayWay payWay;
        payWayStr = StringUtils.isBlank(payWayStr) ? payWayStr : payWayStr.toUpperCase();
        switch (payWayStr) {
            case "ALIPAY":
                payWay = PayWay.ALIPAY;
                break;
            case "WECHAT":
                payWay = PayWay.WECHAT;
                break;
            case "UNIONPAY":
                payWay = PayWay.UNIONPAY;
                break;
            case "UNIONPAY_B2B":
                payWay = PayWay.UNIONPAY_B2B;
                break;
            case "BALANCE":
                payWay = PayWay.BALANCE;
                break;
            default:
                payWay = PayWay.CASH;
        }
        reconciliation.setPayWay(payWay);
        reconciliation.setReturnOrderCode(returnOrder.getId());
        reconciliation.setStoreId(returnOrder.getCompany().getStoreId());
        reconciliation.setSupplierId(returnOrder.getCompany().getCompanyInfoId());
        reconciliation.setType((byte) 1);
        accountRecordProvider.add(reconciliation);
    }


    /**
     * 商家退款申请(修改退单价格新增流水)
     *
     * @param returnOrder
     * @param refundComment
     * @param actualReturnPoints
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void onlineEditPrice(ReturnOrder returnOrder, String refundComment, BigDecimal actualReturnPrice,
                                Long actualReturnPoints, Long actualReturnKnowledge, Operator operator) {
        log.info("StoreReturnOrderController onlineEditPrice ReturnOrderService onlineEditPrice returnOrderId:{} returnOrder:{}", returnOrder.getId(), JSON.toJSONString(returnOrder));
        ReturnPrice returnPrice = returnOrder.getReturnPrice();
        if (StringUtils.isNotEmpty(returnOrder.getBusinessTailId()) && Objects.nonNull(returnPrice.getIsTailApply()) && returnPrice.getIsTailApply()) {
            BigDecimal refundPrice = returnPrice.getEarnestPrice().add(returnPrice.getTailPrice());
            if (returnPrice.getEarnestPrice().add(returnPrice.getTailPrice()).compareTo(actualReturnPrice) == -1) {
                throw new SbcRuntimeException("K-050132", new Object[]{refundPrice});
            }
            if (actualReturnPrice.compareTo(returnPrice.getEarnestPrice()) > 0) {
                returnPrice.setTailPrice(actualReturnPrice.subtract(returnPrice.getEarnestPrice()));
            } else {
                returnPrice.setEarnestPrice(actualReturnPrice);
                returnPrice.setTailPrice(BigDecimal.ZERO);
            }
            onlineEditPrice(returnOrder, refundComment, returnPrice.getEarnestPrice(), actualReturnPoints, actualReturnKnowledge, operator, returnOrder.getId());
            onlineEditPrice(returnOrder, refundComment, returnPrice.getTailPrice(), 0L,0L, operator, returnOrder.getBusinessTailId());
        } else {
            onlineEditPrice(returnOrder, refundComment, actualReturnPrice, actualReturnPoints, actualReturnKnowledge, operator, returnOrder.getId());
        }
    }


    /**
     * 商家退款申请(修改退单价格新增流水)
     *
     * @param returnOrder
     * @param refundComment
     * @param actualReturnPoints
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void onlineEditPrice(ReturnOrder returnOrder, String refundComment, BigDecimal actualReturnPrice,
                                Long actualReturnPoints, Long actualReturnKnowledge, Operator operator, String returnOrderNo) {
        RefundOrder refundOrder = null;

        String key = "DRAWBACK_KEY_LOCK";  //前端不处理，此处改成后端去处理
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(3, TimeUnit.SECONDS);
            // 查询退款单
            refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(returnOrderNo);
            // 退款单状态不等于待退款 -- 参数错误
            if (refundOrder.getRefundStatus() != RefundStatus.TODO) {
                throw new SbcRuntimeException("K-050002");
            }
            // 填充退款流水
            RefundBill refundBill;
            if ((refundBill = refundOrder.getRefundBill()) == null) {
                refundBill = new RefundBill();
                refundBill.setActualReturnPrice(Objects.isNull(actualReturnPrice) ? refundOrder.getReturnPrice() :
                        actualReturnPrice);
                refundBill.setActualReturnPoints(actualReturnPoints);
                refundBill.setActualReturnKnowledge(actualReturnKnowledge);
                refundBill.setCreateTime(LocalDateTime.now());
                refundBill.setRefundId(refundOrder.getRefundId());
                refundBill.setRefundComment(refundComment);
//            refundBillService.save(refundBill);
            } else {
                refundBill.setActualReturnPrice(Objects.isNull(actualReturnPrice) ? refundOrder.getReturnPrice() :
                        actualReturnPrice);
                refundBill.setActualReturnPoints(actualReturnPoints);
                refundBill.setActualReturnKnowledge(actualReturnKnowledge);
            }
            refundBillService.save(refundBill);
        } finally {
            lock.unlock();
        }
        //设置退款单状态为待平台退款
        refundOrder.setRefundStatus(RefundStatus.APPLY);

        ReturnPrice returnPrice = returnOrder.getReturnPrice();
        //普通订单
        if (returnOrder.getReturnPrice().getTotalPrice().compareTo(actualReturnPrice) == 1 && StringUtils.isBlank(returnOrder.getBusinessTailId())) {
            returnOrder.getReturnPrice().setApplyStatus(true);
            returnOrder.getReturnPrice().setApplyPrice(actualReturnPrice);
        } else if (StringUtils.isNotBlank(returnOrder.getBusinessTailId()) &&
                returnPrice.getTotalPrice().compareTo(returnPrice.getEarnestPrice().add(returnPrice.getTailPrice())) == 1) {
            //定金预售
            returnOrder.getReturnPrice().setApplyStatus(true);
            returnOrder.getReturnPrice().setApplyPrice(returnPrice.getEarnestPrice().add(returnPrice.getTailPrice()));
        }


        if (actualReturnPoints != null && actualReturnPoints > 0) {
            returnOrder.getReturnPoints().setActualPoints(actualReturnPoints);
        } else {
            returnOrder.getReturnPoints().setApplyPoints(0L);
        }

        if (returnOrder.getReturnKnowledge() != null && actualReturnKnowledge > 0) {
            returnOrder.getReturnKnowledge().setApplyKnowledge(actualReturnKnowledge);
        } else {
            returnOrder.setReturnKnowledge(ReturnKnowledge.builder().actualKnowledge(0L).build());
        }

        refundOrderRepository.saveAndFlush(refundOrder);
        String detail = String.format("退单[%s]已添加线上退款单，操作人:%s", returnOrder.getId(), operator.getName());
        returnOrder.appendReturnEventLog(
                ReturnEventLog.builder()
                        .operator(operator)
                        .eventType(ReturnEvent.REFUND.getDesc())
                        .eventTime(LocalDateTime.now())
                        .eventDetail(detail)
                        .build()
        );
        ReturnOrder returnOrderRaw;
        Optional<ReturnOrder> returnOrderRawOptional = returnOrderRepository.findById(returnOrder.getId());
        if (returnOrderRawOptional.isPresent()) {
            returnOrderRaw = returnOrderRawOptional.get();
        } else {
            throw new SbcRuntimeException("K-050003");
        }
        returnOrder.setReturnItems(returnOrderRaw.getReturnItems());
        //此处填充
        returnOrder.setAftersaleId(returnOrderRaw.getAftersaleId());
        returnOrder.setMiniProgramScene(returnOrderRaw.getMiniProgramScene());

        returnOrderService.updateReturnOrder(returnOrder);
        this.operationLogMq.convertAndSend(operator, ReturnEvent.REFUND.getDesc(), detail);




        /**
         * 1.调用ERP接口创建退货单
         */
        if (returnOrder.getReturnType() == ReturnType.RETURN) {
            Trade trade = tradeService.detail(returnOrder.getTid());
            if (trade.getCycleBuyFlag()) {
                //判断订单里面里面的赠品是否有虚拟或者电子卡券
                List<TradeItem> gifts = trade.getGifts().stream().filter(g -> GoodsType.VIRTUAL_GOODS.equals(g.getGoodsType()) || GoodsType.VIRTUAL_COUPON.equals(g.getGoodsType())).collect(Collectors.toList());
                //判断里面是否有已经发货日历
                List<DeliverCalendar> deliverCalendarList = trade.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendar -> deliverCalendar.getCycleDeliverStatus() == CycleDeliverStatus.SHIPPED).collect(Collectors.toList());
                //是否仅退款
                Boolean isRefund = trade.getTradeState().getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED || trade.getTradeState().getDeliverStatus() == DeliverStatus.VOID || (CollectionUtils.isNotEmpty(gifts) && CollectionUtils.isEmpty(deliverCalendarList));

                if (CollectionUtils.isNotEmpty(deliverCalendarList)) {
                    isRefund = false;
                }
                if (!isRefund) {
                    List<ReturnTradeItemVO> returnOrderItemList = this.getReturnOrderItemList(returnOrder);
                    ERPTradePaymentVO erpTradePaymentVO = new ERPTradePaymentVO();
                    List<ERPTradePaymentVO> erpTradePaymentVOList = new ArrayList<>();
                    //获取订单支付方式
                    if (Objects.isNull(trade.getPayWay())) {
                        erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
                    } else {
                        switch (trade.getPayWay()) {
                            case WECHAT:
                                erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.weixin.getStateId());
                                break;
                            case ALIPAY:
                                erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.aliPay.getStateId());
                                break;
                            default:
                                erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
                                break;
                        }
                    }

                    log.info("price==========>:{}", actualReturnPrice);
                    log.info("returnOrder===============>:{}", returnOrder);
                    erpTradePaymentVO.setPayment(String.valueOf(actualReturnPrice));
                    erpTradePaymentVOList.add(erpTradePaymentVO);

                    //收货人信息
                    Consignee consignee = trade.getConsignee();

                    Map<Enum, String> addrMap = new HashMap<>();
                    //排除周期购有赞老订单
                    if (trade.getParentId().startsWith(GeneratorService._PREFIX_YOUZAN_TRADE_ID)) {
                        //todo 增加地址省市区信息字段
                        addrMap.put(AddrLevel.PROVINCE, trade.getConsignee().getProvinceName());
                        addrMap.put(AddrLevel.CITY, trade.getConsignee().getCityName());
                        addrMap.put(AddrLevel.DISTRICT, trade.getConsignee().getAreaName());
                    } else {
                        //提取平台地址数据
                        PlatformAddressListRequest platformAddressListRequest =
                                PlatformAddressListRequest.builder().addrIdList(Arrays.asList(consignee.getProvinceId().toString(),
                                        consignee.getCityId().toString()
                                        , consignee.getAreaId().toString())).build();
                        //不填充叶子节点
                        platformAddressListRequest.setLeafFlag(false);
                        BaseResponse<PlatformAddressListResponse> platformAddressListResponseBaseResponse = platformAddressQueryProvider.list(platformAddressListRequest);
                        List<PlatformAddressVO> platformAddressVOList =
                                platformAddressListResponseBaseResponse.getContext().getPlatformAddressVOList();
                        platformAddressVOList.stream().forEach(platformAddressVO -> {
                            addrMap.put(platformAddressVO.getAddrLevel(), platformAddressVO.getAddrName());
                        });
                    }


                    ReturnTradeCreateRequst returnTradeCreateRequst = ReturnTradeCreateRequst.builder()
                            .buyerMobile(trade.getBuyer().getAccount())
                            .returnType(ReturnTradeType.RETURN.getCode())
                            .typeCode(String.valueOf(returnOrder.getReturnReason().getType()))
                            .tradeNo(returnOrder.getPtid())
                            .tradeItems(returnOrderItemList)
                            .refundDetail(erpTradePaymentVOList)
                            .receiveName(consignee.getName())
                            .receiverMobile(consignee.getPhone())
                            .receiverProvince(addrMap.get(AddrLevel.PROVINCE))
                            .receiverCity(addrMap.get(AddrLevel.CITY))
                            .receiverDistrict(addrMap.get(AddrLevel.DISTRICT))
                            .receiverAddress(consignee.getDetailAddress())
                            .expressName((Objects.nonNull(returnOrder.getReturnLogistics()) && Objects.nonNull(returnOrder.getReturnLogistics().getCompany())) ? returnOrder.getReturnLogistics().getCompany() : null)
                            .expressNum((Objects.nonNull(returnOrder.getReturnLogistics()) && Objects.nonNull(returnOrder.getReturnLogistics().getNo())) ? returnOrder.getReturnLogistics().getNo() : null)
                            .build();
                    log.info("returnTradeCreateRequst================>:{}", returnTradeCreateRequst);
                    guanyierpProvider.createReturnOrder(returnTradeCreateRequst);

                }
            } else {
                if (trade.getTradeState().getDeliverStatus().equals(DeliverStatus.SHIPPED)) {
                    if (Objects.equals(String.valueOf(defaultProviderId), returnOrder.getProviderId())) {

                        List<ReturnTradeItemVO> returnOrderItemList = this.getReturnOrderItemList(returnOrder);
                        ERPTradePaymentVO erpTradePaymentVO = new ERPTradePaymentVO();
                        List<ERPTradePaymentVO> erpTradePaymentVOList = new ArrayList<>();
                        //获取订单支付方式
                        if (Objects.isNull(trade.getPayWay())) {
                            erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
                        } else {
                            switch (trade.getPayWay()) {
                                case WECHAT:
                                    erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.weixin.getStateId());
                                    break;
                                case ALIPAY:
                                    erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.aliPay.getStateId());
                                    break;
                                default:
                                    erpTradePaymentVO.setPayTypeCode(ERPTradePayChannel.other.getStateId());
                                    break;
                            }
                        }

                        log.info("price==========>:{}", actualReturnPrice);
                        log.info("returnOrder===============>:{}", returnOrder);
                        erpTradePaymentVO.setPayment(String.valueOf(actualReturnPrice));
                        erpTradePaymentVOList.add(erpTradePaymentVO);

                        //收货人信息
                        Consignee consignee = trade.getConsignee();

                        Map<Enum, String> addrMap = new HashMap<>();
                        //排除周期购有赞老订单
                        if (trade.getParentId().startsWith(GeneratorService._PREFIX_YOUZAN_TRADE_ID)) {
                            //todo 增加地址省市区信息字段
                            addrMap.put(AddrLevel.PROVINCE, trade.getConsignee().getProvinceName());
                            addrMap.put(AddrLevel.CITY, trade.getConsignee().getCityName());
                            addrMap.put(AddrLevel.DISTRICT, trade.getConsignee().getAreaName());
                        } else {
                            //提取平台地址数据
                            PlatformAddressListRequest platformAddressListRequest =
                                    PlatformAddressListRequest.builder().addrIdList(Arrays.asList(consignee.getProvinceId().toString(),
                                            consignee.getCityId().toString()
                                            , consignee.getAreaId().toString())).build();
                            //不填充叶子节点
                            platformAddressListRequest.setLeafFlag(false);
                            BaseResponse<PlatformAddressListResponse> platformAddressListResponseBaseResponse = platformAddressQueryProvider.list(platformAddressListRequest);
                            List<PlatformAddressVO> platformAddressVOList =
                                    platformAddressListResponseBaseResponse.getContext().getPlatformAddressVOList();
                            platformAddressVOList.stream().forEach(platformAddressVO -> {
                                addrMap.put(platformAddressVO.getAddrLevel(), platformAddressVO.getAddrName());
                            });
                        }


                        ReturnTradeCreateRequst returnTradeCreateRequst = ReturnTradeCreateRequst.builder()
                                .buyerMobile(trade.getBuyer().getAccount())
                                .returnType(ReturnTradeType.RETURN.getCode())
                                .typeCode(String.valueOf(returnOrder.getReturnReason().getType()))
                                .tradeNo(returnOrder.getPtid())
                                .tradeItems(returnOrderItemList)
                                .refundDetail(erpTradePaymentVOList)
                                .receiveName(consignee.getName())
                                .receiverMobile(consignee.getPhone())
                                .receiverProvince(addrMap.get(AddrLevel.PROVINCE))
                                .receiverCity(addrMap.get(AddrLevel.CITY))
                                .receiverDistrict(addrMap.get(AddrLevel.DISTRICT))
                                .receiverAddress(consignee.getDetailAddress())
                                .expressName((Objects.nonNull(returnOrder.getReturnLogistics()) && Objects.nonNull(returnOrder.getReturnLogistics().getCompany())) ? returnOrder.getReturnLogistics().getCompany() : null)
                                .expressNum((Objects.nonNull(returnOrder.getReturnLogistics()) && Objects.nonNull(returnOrder.getReturnLogistics().getCompany())) ? returnOrder.getReturnLogistics().getNo() : null)
                                .build();
                        log.info("returnTradeCreateRequst================>:{}", returnTradeCreateRequst);
                        guanyierpProvider.createReturnOrder(returnTradeCreateRequst);
                    }
                }else{
                    log.info("博库订单需客服在博库平台操作，请知悉");
                }
            }

        }
        log.info("ReturnOrderService onlineEditPrice tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());
        //退款审核通过发送MQ消息
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName());
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName());
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.REFUND_CHECK_PASS,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }

    }


    /**
     * 在线退款
     *
     * @param returnOrder
     * @param refundOrder
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void refundOnline(ReturnOrder returnOrder, RefundOrder refundOrder, Operator operator) {
        try {
            RefundBill refundBill;
            PayTradeRecordResponse payTradeRecordResponse = payQueryProvider.getTradeRecordByOrderCode(new
                    TradeRecordByOrderCodeRequest(returnOrder.getId())).getContext();
            if (Objects.nonNull(refundOrder.getRefundChannel()) && refundOrder.getRefundChannel() == RefundChannel.TAIL) {
                payTradeRecordResponse = payQueryProvider.getTradeRecordByOrderCode(new
                        TradeRecordByOrderCodeRequest(returnOrder.getBusinessTailId())).getContext();
            }
            if (Objects.isNull(payTradeRecordResponse)) {
                Trade trade = tradeService.detail(returnOrder.getTid());
                String tid = trade.getPayInfo().isMergePay() ? trade.getParentId() : trade.getId();
                payTradeRecordResponse = payQueryProvider.getTradeRecordByOrderCode(
                        new TradeRecordByOrderCodeRequest(tid)).getContext();
            }
            PayChannelItemResponse channelItemResponse = payQueryProvider.getChannelItemById(new
                    ChannelItemByIdRequest(Objects.isNull(payTradeRecordResponse) || Objects.isNull(
                    payTradeRecordResponse.getChannelItemId()) ? Constants.DEFAULT_RECEIVABLE_ACCOUNT :
                    payTradeRecordResponse.getChannelItemId())).getContext();
            // 退款流水保存
            if ((refundBill = refundOrder.getRefundBill()) == null) {
                refundBill = new RefundBill();
                refundBill.setPayChannel(channelItemResponse.getName());
                refundBill.setPayChannelId(channelItemResponse.getId());
                refundBill.setActualReturnPrice(refundOrder.getReturnPrice());
                refundBill.setCreateTime(LocalDateTime.now());
                refundBill.setRefundId(refundOrder.getRefundId());
                refundBillService.save(refundBill);
            } else {
                refundBill.setPayChannel(channelItemResponse.getName());
                refundBill.setPayChannelId(channelItemResponse.getId());
                refundBill.setCreateTime(LocalDateTime.now());
                logger.info("refundOnline,refundOrder================:{}", JSON.toJSONString(refundOrder));
                refundBillService.saveAndFlush(refundBill);
            }
            if (returnOrder.getReturnFlowState() != ReturnFlowState.COMPLETED && (Objects.isNull(refundOrder.getRefundChannel()) || refundOrder.getRefundChannel() == RefundChannel.EARNEST)) {
                //退款
                refund(returnOrder.getId(), operator, returnOrder.getReturnPrice().getApplyStatus() ?
                        returnOrder.getReturnPrice().getApplyPrice() : refundBill.getActualReturnPrice());
                refundOrder.setRefundStatus(RefundStatus.FINISH);
                // 更改订单状态
                refundOrderRepository.save(refundOrder);
                //保存退款对账明细
                String tradeNo = Objects.nonNull(payTradeRecordResponse) ? payTradeRecordResponse.getTradeNo() : "";
                saveReconciliation(refundOrder, returnOrder, channelItemResponse.getChannel(), "", tradeNo, returnOrder.getReturnPrice().getEarnestPrice());
                tradeService.returnCoupon(returnOrder.getTid());

                // 退单完成时，发送MQ消息
                ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                        .addFlag(Boolean.FALSE)
                        .customerId(returnOrder.getBuyer().getId())
                        .orderId(returnOrder.getTid())
                        .returnId(returnOrder.getId())
                        .build();
                returnOrderProducerService.returnOrderFlow(sendMQRequest);
                // 返还限售记录 —— 自动退款
                orderProducerService.backRestrictedPurchaseNum(null, returnOrder.getId(), BackRestrictedType.REFUND_ORDER);
            }
            if (Objects.nonNull(refundOrder.getRefundChannel()) && refundOrder.getRefundChannel() == RefundChannel.TAIL) {
                RefundOrder newRefundOrder = refundOrderRepository.findById(refundOrder.getRefundId()).orElse(null);
                if (Objects.isNull(newRefundOrder) || newRefundOrder.getRefundStatus() != RefundStatus.FINISH) {
                    //保存退款对账明细
                    refundOrder.setRefundStatus(RefundStatus.FINISH);
                    // 更改订单状态
                    refundOrderRepository.saveAndFlush(refundOrder);
                    String tradeNo = Objects.nonNull(payTradeRecordResponse) ? payTradeRecordResponse.getTradeNo() : "";
                    saveReconciliation(returnOrder, channelItemResponse.getChannel(), "", tradeNo, returnOrder.getReturnPrice().getTailPrice(), returnOrder.getBusinessTailId());
                    ReturnOrder newReturnOrder = findById(returnOrder.getId());
                    newReturnOrder.getReturnPrice().setActualReturnPrice(newReturnOrder.getReturnPrice().getEarnestPrice().add(newReturnOrder.getReturnPrice().getTailPrice()));
                    returnOrderService.updateReturnOrder(newReturnOrder);
                }
            }
        } catch (SbcRuntimeException e) {
            logger.error("{}退单状态修改异常,error={}", returnOrder.getId(), e);
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        }
    }

    /**
     * b2b线下退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    public void refundOffline(String rid, CustomerAccountAddOrModifyDTO customerAccount, RefundBill refundBill,
                              Operator operator) {
        // 查询退单信息
        ReturnOrder returnOrder = findById(rid);

        // 如果offlineAccount非空，新增后使用
        if (Objects.nonNull(customerAccount)) {

            CustomerAccountByCustomerIdRequest customerAccountByCustomerIdRequest =
                    CustomerAccountByCustomerIdRequest.builder().customerId(customerAccount.getCustomerId()).build();
            //查询会员有几条银行账户信息
            BaseResponse<CustomerAccountByCustomerIdResponse> integerBaseResponse =
                    customerAccountQueryProvider.countByCustomerId(customerAccountByCustomerIdRequest);
            Integer count = integerBaseResponse.getContext().getResult();
            if (null != count && count >= 5) {
                //会员最多有5条银行账户信息
                throw new SbcRuntimeException("K-010005");
            }

            CustomerAccountAddRequest customerAccountAddRequest = new CustomerAccountAddRequest();
            BeanUtils.copyProperties(customerAccount, customerAccountAddRequest);

            // 客户编号
            customerAccountAddRequest.setCustomerId(returnOrder.getBuyer().getId());
            customerAccountAddRequest.setEmployeeId(operator.getUserId());
            BaseResponse<CustomerAccountAddResponse> customerAccountAddResponseBaseResponse =
                    customerAccountProvider.add(customerAccountAddRequest);
            CustomerAccountAddResponse customerAccountAddResponse = customerAccountAddResponseBaseResponse.getContext();

            // 设置财务退款单号
            refundBill.setCustomerAccountId(customerAccountAddResponse.getCustomerAccountId());
        }

        // 根据退款单号查询财务退款单据的编号
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(rid);
        refundBill.setRefundId(refundOrder.getRefundId());

        // 生成退款记录
        refundBillService.save(refundBill);

        // 退单状态修改
        refund(rid, operator, refundBill.getActualReturnPrice());

    }

    /**
     * 商家线下退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void supplierRefundOffline(String rid, CustomerAccountVO customerAccount, RefundBill refundBill, Operator
            operator) {
        // 查询退单信息
        ReturnOrder returnOrder = findById(rid);
//        BigDecimal price = returnOrder.getReturnItems().stream().map(r -> r.getSplitPrice())
//                .reduce(BigDecimal::add).get();
//
//
//        if(refundBill.getActualReturnPrice().compareTo(price) == 1){
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//        }

        // 积分信息
        returnOrder.getReturnPoints().setActualPoints(refundBill.getActualReturnPoints());
        // 积分信息
        if (returnOrder.getReturnKnowledge() == null) {
            returnOrder.setReturnKnowledge(ReturnKnowledge.builder().actualKnowledge(refundBill.getActualReturnKnowledge()).build());
        } else {
            returnOrder.getReturnKnowledge().setActualKnowledge(refundBill.getActualReturnKnowledge());
        }
        // 退货金额
        if (refundBill.getActualReturnPrice().compareTo(returnOrder.getReturnPrice().getApplyPrice()) == -1) {
            returnOrder.getReturnPrice().setApplyStatus(true);
            returnOrder.getReturnPrice().setApplyPrice(refundBill.getActualReturnPrice());
        }
        // 如果customerAccount非空，临时账号，当快照冗余在退单
        if (Objects.nonNull(customerAccount)) {
            // 客户编号
            customerAccount.setCustomerId(returnOrder.getBuyer().getId());
            returnOrder.setCustomerAccount(customerAccount);
        } else {
            //客户账号冗余至退单
            CustomerAccountOptionalRequest customerAccountOptionalRequest = new CustomerAccountOptionalRequest();
            customerAccountOptionalRequest.setCustomerAccountId(refundBill.getCustomerAccountId());
            BaseResponse<CustomerAccountOptionalResponse> customerAccountOptionalResponseBaseResponse =
                    customerAccountQueryProvider.getByCustomerAccountIdAndDelFlag(customerAccountOptionalRequest);
            CustomerAccountOptionalResponse customerAccountOptionalResponse =
                    customerAccountOptionalResponseBaseResponse.getContext();
            if (Objects.nonNull(customerAccountOptionalResponse)) {
                customerAccount = new CustomerAccountVO();
                KsBeanUtil.copyPropertiesThird(customerAccountOptionalResponse, customerAccount);
                returnOrder.setCustomerAccount(customerAccount);
            } else {
                throw new SbcRuntimeException("K-070009");
            }
        }
        // 根据退款单号查询财务退款单据的编号
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(rid);
        if (refundOrder.getRefundStatus().equals(RefundStatus.APPLY) || returnOrder.getReturnFlowState().equals
                (ReturnFlowState.REJECT_REFUND)) {
            throw new SbcRuntimeException("K-050002", new Object[]{"退款"});
        }
        if (!Objects.isNull(refundOrder.getRefundBill())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        refundBill.setRefundId(refundOrder.getRefundId());

        // 生成退款记录
        refundBillService.save(refundBill);
        returnOrder.appendReturnEventLog(
                ReturnEventLog.builder()
                        .operator(operator)
                        .eventType(ReturnEvent.REFUND.getDesc())
                        .eventTime(LocalDateTime.now())
                        .eventDetail(String.format("退单[%s]已添加线下退款单，操作人:%s", returnOrder.getId(), operator.getName()))
                        .build()
        );
        refundOrder.setRefundStatus(RefundStatus.APPLY);
        refundOrderRepository.save(refundOrder);
        returnOrderService.updateReturnOrder(returnOrder);
        // 返还限售记录 —— 线下退款
        orderProducerService.backRestrictedPurchaseNum(null, returnOrder.getId(), BackRestrictedType.REFUND_ORDER);
    }


    /**
     * s2b线下退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void s2bBoosRefundOffline(String rid, RefundBill refundBill, Operator operator, String tid) {
        //修改退款单状态
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(rid);
        Optional<RefundBill> result = refundBillService.save(refundBill);
        refundOrder.setRefundBill(result.get());
        // 退单状态修改
        refund(rid, operator, refundBill.getActualReturnPrice());
        refundOrder.setRefundStatus(RefundStatus.FINISH);
        refundOrderRepository.saveAndFlush(refundOrder);
        tradeService.returnCoupon(tid);

        // 线下退款完成，发送MQ消息
        ReturnOrder returnOrder = this.findById(rid);
        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.FALSE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(rid)
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);
    }


    /**
     * 拒绝退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void refundReject(String rid, String reason, Operator operator) {
        ReturnOrder returnOrder = findById(rid);
        TradeStatus tradeStatus = payQueryProvider.getRefundResponseByOrdercode(new RefundResultByOrdercodeRequest
                (returnOrder.getTid(), returnOrder.getId())).getContext().getTradeStatus();
        if (tradeStatus != null) {
            if (tradeStatus == TradeStatus.SUCCEED) {
                throw new SbcRuntimeException("K-100104");
            } else if (tradeStatus == TradeStatus.PROCESSING) {
                throw new SbcRuntimeException("K-100105");
            }
        }
        //修改财务退款单状态
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(rid);
        if (refundOrder.getRefundStatus().equals(RefundStatus.APPLY)) {
            throw new SbcRuntimeException("K-050002");
        }
        refundOrderService.refuse(refundOrder.getRefundId(), reason);
        //修改退单状态
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.REJECT_REFUND)
                .data(reason)
                .build();
        returnFSMService.changeState(request);
        if (Platform.WX_VIDEO != operator.getPlatform()) {
            WxAfterSaleStatus wxAfterSaleStatus = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN) ? WxAfterSaleStatus.REJECT_RETURN : WxAfterSaleStatus.REJECT_REFUND;
            WxAfterSaleOperateType wxAfterSaleType = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN) ? WxAfterSaleOperateType.REJECT : WxAfterSaleOperateType.CANCEL;
            this.addWxAfterSale(returnOrder, wxAfterSaleStatus, wxAfterSaleType, "拒绝退款");

        }
        // 拒绝退款时，发送MQ消息
        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.FALSE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(rid)
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);

        //退款审核未通过发送MQ消息
        log.info("ReturnOrderService refundReject 拒绝退款 tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.REFUND_CHECK_NOT_PASS,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }
    }

    /**
     * 拒绝退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    public void refundRejectAndRefuse(String rid, String reason, Operator operator) {
        refundOrderService.refuse(rid, reason);
        refundOrderService.findById(rid).ifPresent(refundOrderResponse -> {
            ReturnOrder returnOrder = findById(refundOrderResponse.getReturnOrderCode());
            TradeStatus tradeStatus = payQueryProvider.getRefundResponseByOrdercode(new RefundResultByOrdercodeRequest
                    (returnOrder.getTid(), returnOrder.getId())).getContext().getTradeStatus();
            if (tradeStatus != null) {
                if (tradeStatus == TradeStatus.SUCCEED) {
                    throw new SbcRuntimeException("K-100104");
                } else if (tradeStatus == TradeStatus.PROCESSING) {
                    throw new SbcRuntimeException("K-100105");
                }
            }
            //修改财务退款单状态
            RefundOrder refundOrder =
                    refundOrderService.findRefundOrderByReturnOrderNo(refundOrderResponse.getReturnOrderCode());
            if (refundOrder.getRefundStatus().equals(RefundStatus.APPLY)) {
                throw new SbcRuntimeException("K-050002");
            }
            //修改退单状态
            ReturnStateRequest request = ReturnStateRequest
                    .builder()
                    .rid(refundOrderResponse.getReturnOrderCode())
                    .operator(operator)
                    .returnEvent(ReturnEvent.REJECT_REFUND)
                    .data(refundOrderResponse.getRefuseReason())
                    .build();
            returnFSMService.changeState(request);
            WxAfterSaleStatus wxAfterSaleStatus = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN)?WxAfterSaleStatus.REJECT_RETURN:WxAfterSaleStatus.REJECT_REFUND;
            WxAfterSaleOperateType wxAfterSaleType = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN) ? WxAfterSaleOperateType.REJECT : WxAfterSaleOperateType.CANCEL;
            this.addWxAfterSale(returnOrder, wxAfterSaleStatus,wxAfterSaleType , "拒绝退款");
        });
    }

    /**
     * 驳回退单
     *
     * @param rid
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public void cancel(String rid, Operator operator, String remark, Boolean messageSource) {

        ReturnOrder returnOrder = this.findById(rid);

        // 只有当前退单已经向linkedmall平台申请了退单，该笔退单才会走linkedmall平台取消流程
        if (Objects.nonNull(returnOrder) && ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()) && Objects.nonNull(returnOrder.getThirdReasonId())) {
            String buyerId = returnOrder.getBuyer().getId();
            try {
                returnOrder.getReturnItems().forEach(item -> {
                    SbcQueryRefundApplicationDetailRequest queryRequest =
                            SbcQueryRefundApplicationDetailRequest.builder().bizUid(buyerId).subLmOrderId(item.getThirdPlatformSubOrderId()).build();
                    QueryRefundApplicationDetailResponse.RefundApplicationDetail lmRefundDetail =
                            linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(queryRequest).getContext().getDetail();
                    if (Objects.nonNull(lmRefundDetail) && Objects.nonNull(lmRefundDetail.getDisputeId())) {
                        SbcCancelRefundRequest cancelRequest =
                                SbcCancelRefundRequest.builder().bizUid(buyerId).subLmOrderId(item.getThirdPlatformSubOrderId()).disputeId(lmRefundDetail.getDisputeId()).build();
                        linkedMallReturnOrderProvider.cancelRefund(cancelRequest);
                    }
                });
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "退单取消失败，请重试");
            }
        }

        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.VOID)
                .data(remark)
                .build();
        returnFSMService.changeState(request);

        // 取消退单时，发送MQ消息
        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.FALSE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(rid)
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);
        if (Platform.WX_VIDEO != operator.getPlatform()) {
            WxAfterSaleStatus wxAfterSaleStatus = Objects.equals(returnOrder.getReturnType(),ReturnType.RETURN) ? WxAfterSaleStatus.REJECT_RETURN : WxAfterSaleStatus.REJECT_REFUND;
            this.addWxAfterSale(returnOrder, wxAfterSaleStatus, WxAfterSaleOperateType.CANCEL, "取消");
        }
        //售后审核未通过发送MQ消息
        log.info("ReturnOrderService cancel 驳回订单 rid:{} 原因是：{}", rid, returnOrder.getReturnReason());
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName(), remark);
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName(), remark);
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.AFTER_SALE_ORDER_CHECK_NOT_PASS,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }

    }



    /**
     * 查询订单详情,如已发货则带出可退商品数
     *
     * @param tid
     * @param replace 是否代客退单, replace需要修改的地方很多，所以此处由两个判断，一个是商家创建订单，一个是 传递replace
     * @return
     */
    public Trade queryCanReturnItemNumByTid(String tid, Integer replace, ReturnReason returnReason) {
        Trade trade = tradeService.detail(tid);
        if (Objects.isNull(trade)) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        //校验支付状态
        PayOrderResponse payOrder = payOrderService.findPayOrder(trade.getId());
        if (payOrder.getPayOrderStatus() != PayOrderStatus.PAYED) {
            throw new SbcRuntimeException("K-050105");
        }
        if (trade.getTradeState().getFlowState() == FlowState.GROUPON) {
            throw new SbcRuntimeException("K-050141");
        }



        // 填充订单商品providerID  这里似乎不需要填充，tradeItem中已存放了providerId
        /*********************订单中填充供应商 和可退数量 begin************************/
        //计算商品可退数 SKUID -> canReturnNum
        Map<String, Integer> itemCanReturnNumMap = findLeftItems(trade, replace, returnReason);
        //当前订单的商品列表
        for (TradeItem tradeItemParam : trade.getTradeItems()) {
            //可退数量
            Integer itemCanReturnNum = itemCanReturnNumMap.get(tradeItemParam.getSkuId());
            if (itemCanReturnNum == null) {
                throw new SbcRuntimeException("K-050411");
            }
            if (itemCanReturnNum > tradeItemParam.getNum()) {
                itemCanReturnNum = tradeItemParam.getNum().intValue();
            }
            tradeItemParam.setCanReturnNum(itemCanReturnNum);
            //如果订单中已经有供应商，则不再查询
            if (tradeItemParam.getProviderId() != null && tradeItemParam.getProviderId() > 0) {
                continue;
            }
            GoodsInfoByIdRequest goodsInfoByIdRequest = new GoodsInfoByIdRequest();
            goodsInfoByIdRequest.setGoodsInfoId(tradeItemParam.getSkuId());
            GoodsInfoByIdResponse goodsInfoVo = goodsInfoQueryProvider.getById(goodsInfoByIdRequest).getContext();
            if (goodsInfoVo != null) {
                tradeItemParam.setProviderId(goodsInfoVo.getProviderId());
            }
        }

        /*********************订单中填充供应商 end************************/



        /*********************校验积分和知豆 现金 begin************************/
        List<ReturnOrder> allReturnOrderList = findReturnsNotVoid(tid);
        // 下单支付的总积分数量
        Long canReturnPoints = trade.getTradePrice().getPoints() == null ? 0 : trade.getTradePrice().getPoints();

        // 可退知豆
        Long canReturnKnowLedge = trade.getTradePrice().getKnowledge() == null ? 0 : trade.getTradePrice().getKnowledge();

        BigDecimal canReturnPrice = trade.getTradeItems().stream().map(TradeItem::getSplitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (canReturnPoints > 0 || canReturnKnowLedge > 0) {
            Long completeReturnPoints = 0L;
            Long completeReturnKnowledge = 0L;
            BigDecimal completePrice = BigDecimal.ZERO;
            for (ReturnOrder returnOrderParam : allReturnOrderList) {
                //积分
                if (Objects.nonNull(returnOrderParam.getReturnPoints()) && Objects.nonNull(returnOrderParam.getReturnPoints().getActualPoints())) {
                    completeReturnPoints += returnOrderParam.getReturnPoints().getActualPoints();
                }

                //知豆
                if (Objects.nonNull(returnOrderParam.getReturnKnowledge()) && Objects.nonNull(returnOrderParam.getReturnKnowledge().getActualKnowledge())) {
                    completeReturnKnowledge += returnOrderParam.getReturnKnowledge().getActualKnowledge();
                }

                //现金
                if (Objects.nonNull(returnOrderParam.getReturnPrice().getActualReturnPrice())) {
                    canReturnPrice = canReturnPrice.add(returnOrderParam.getReturnPrice().getActualReturnPrice());
                }
            }
            canReturnPoints = canReturnPoints - completeReturnPoints;
            canReturnKnowLedge = canReturnKnowLedge - completeReturnKnowledge;
            canReturnPrice = canReturnPrice.subtract(completePrice);
        }

        log.info("ReturnOrderService queryCanReturnItemNumByTid orderId:{} canReturnPoints:{} canReturnKnowLedge:{} canReturnPrice:{}", trade.getId(), canReturnPoints, canReturnKnowLedge, canReturnPrice);
        if (canReturnPoints > 0 && canReturnKnowLedge > 0) {
            throw new SbcRuntimeException("K-050410"); //积分或知豆不能都使用 //TODO update by duanlsh
        }
        if (canReturnPoints < 0 || canReturnKnowLedge < 0) {
            throw new SbcRuntimeException("K-050410"); //积分或知豆不能为负数
        }
        if (canReturnPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new SbcRuntimeException("K-050410"); //退款金额不能为负数
        }

        trade.setCanReturnPrice(canReturnPrice);
        trade.setCanReturnKnowledge(canReturnKnowLedge);
        trade.setCanReturnPoints(canReturnPoints);
        /*********************校验积分和知豆 现金 end************************/
        return trade;
    }

//    /**
//     * 查询订单详情,如已发货则带出可退商品数
//     *
//     * @param tid
//     * @return
//     */
//    public Trade queryCanReturnItemNumByTid(String tid) {
//        Trade trade = tradeService.detail(tid);
//        if (Objects.isNull(trade)) {
//            throw new SbcRuntimeException("K-050100", new Object[]{tid});
//        }
//        //校验支付状态
//        PayOrderResponse payOrder = payOrderService.findPayOrder(trade.getId());
//        if (payOrder.getPayOrderStatus() != PayOrderStatus.PAYED) {
//            throw new SbcRuntimeException("K-050105");
//        }
//        if (trade.getTradeState().getFlowState() == FlowState.GROUPON) {
//            throw new SbcRuntimeException("K-050141");
//        }
//        DeliverStatus deliverStatus = trade.getTradeState().getDeliverStatus();
//        if (deliverStatus != DeliverStatus.NOT_YET_SHIPPED && deliverStatus != DeliverStatus.VOID) {
//            //计算商品可退数
//            Map<String, Integer> map = findLeftItems(trade);
//            // 不能超过
//            trade.getTradeItems().forEach(
//                    item -> {
//                        item.setCanReturnNum(map.get(item.getSkuId()));
//                        if (item.getCanReturnNum() > item.getNum()) {
//                            item.setCanReturnNum(item.getNum().intValue());
//                        }
//                    }
//
//            );
//
//            //计算赠品可退数
//            if (CollectionUtils.isNotEmpty(trade.getGifts())) {
//                Map<String, Integer> giftMap = findLeftGiftItems(trade);
//                trade.getGifts().forEach(
//                        item -> item.setCanReturnNum(giftMap.get(item.getSkuId()))
//                );
//            }
//        }
//        List<ReturnOrder> returnsNotVoid = findReturnsNotVoid(tid);
//        // 已退积分
//        Long retiredPoints = returnsNotVoid.stream()
//                .filter(o -> Objects.nonNull(o.getReturnPoints()) && Objects.nonNull(o.getReturnPoints().getActualPoints()))
//                .map(o -> o.getReturnPoints().getActualPoints())
//                .reduce((long) 0, Long::sum);
//        // 可退积分
//        Long points = trade.getTradePrice().getPoints() == null ? 0 : trade.getTradePrice().getPoints();
//        trade.setCanReturnPoints(points - retiredPoints);
//
//        // 已退知豆
//        Long retiredKnowledge = returnsNotVoid.stream()
//                .filter(o -> Objects.nonNull(o.getReturnKnowledge()) && Objects.nonNull(o.getReturnKnowledge().getActualKnowledge()))
//                .map(o -> o.getReturnKnowledge().getActualKnowledge())
//                .reduce((long) 0, Long::sum);
//        // 可退知豆
//        Long knoeledge = trade.getTradePrice().getKnowledge() == null ? 0 : trade.getTradePrice().getKnowledge();
//        trade.setCanReturnKnowledge(knoeledge - retiredKnowledge);
//
//        // 可退金额
//        BigDecimal totalPrice = trade.getTradeItems().stream()
//                .map(TradeItem::getSplitPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal retiredPrice = returnsNotVoid.stream()
//                .filter(o -> Objects.nonNull(o.getReturnPrice().getActualReturnPrice()))
//                .map(o -> o.getReturnPrice().getActualReturnPrice())
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal canReturnPrice = totalPrice.subtract(retiredPrice);
//        trade.setCanReturnPrice(canReturnPrice);
//        // 填充订单商品providerID  这里似乎不需要填充，tradeItem中已存放了providerId
//        trade.getTradeItems().forEach(tradeItem -> {
//            String skuId = tradeItem.getSkuId();
//            GoodsInfoByIdResponse goodsInfoVo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build()).getContext();
//            if (goodsInfoVo != null) {
//                tradeItem.setProviderId(goodsInfoVo.getProviderId());
//            }
//        });
//        //填充赠品 providerID
//        trade.getGifts().forEach(tradeItem -> {
//            String skuId = tradeItem.getSkuId();
//            GoodsInfoByIdResponse goodsInfoVo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build()).getContext();
//            if (goodsInfoVo != null) {
//                tradeItem.setProviderId(goodsInfoVo.getProviderId());
//            }
//        });
//        return trade;
//    }

    /**
     * 查询退单详情,可退商品数
     *
     * @param rid
     * @return
     */
    public ReturnOrder queryCanReturnItemNumById(String rid) {
        ReturnOrder returnOrder = findById(rid);
        Trade trade = tradeService.detail(returnOrder.getTid());
        if (trade.getTradeState().getDeliverStatus() != DeliverStatus.NOT_YET_SHIPPED && trade.getTradeState()
                .getDeliverStatus() != DeliverStatus.VOID) {
            //计算商品可退数
            Map<String, Integer> map = findLeftItems(trade, null, returnOrder.getReturnReason());
            returnOrder.getReturnItems().forEach(item -> item.setCanReturnNum(map.get(item.getSkuId())));
        }
        return returnOrder;
    }

    /**
     * 退款单作废状态扭转
     */
    @Transactional
    public void reverse(String rid, Operator operator) {
        //删除对账记录
        AccountRecordDeleteByReturnOrderCodeAndTypeRequest deleteRequest = new
                AccountRecordDeleteByReturnOrderCodeAndTypeRequest();
        deleteRequest.setReturnOrderCode(rid);
        deleteRequest.setAccountRecordType(AccountRecordType.REFUND);
        accountRecordProvider.deleteByReturnOrderCodeAndType(deleteRequest);
        ReturnOrder returnOrder = returnOrderRepository.findById(rid).orElse(new ReturnOrder());
        ReturnEvent event = returnOrder.getReturnType() == ReturnType.RETURN ? ReturnEvent.REVERSE_RETURN :
                ReturnEvent.REVERSE_REFUND;
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(event)
                .build();
        returnFSMService.changeState(request);
    }

    /**
     * 修改退单
     *
     * @param newReturnOrder
     * @param operator
     */
    @GlobalTransactional
    @Transactional
    public void remedy(ReturnOrder newReturnOrder, Operator operator) {
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(newReturnOrder.getId())
                .operator(operator)
                .returnEvent(ReturnEvent.REMEDY)
                .data(newReturnOrder)
                .build();
        returnFSMService.changeState(request);
    }

    /**
     * 订单中可退货的数量，
     * 1、获取订单里面所有的商品以及购买的数量
     * 2、获取所有申请的已经完成的退款订单列表 商品以及 已经退款的数量
     * 3、可退数量就是 商品购买数量 - 已经退款的数量
     *
     * @param trade
     */
    private Map<String, Integer> findLeftItems(Trade trade, Integer replace, ReturnReason returnReason) {
        final Map<String, Integer> canReturnNum = new HashMap<>();
        //如果为周期购
        if (trade.getCycleBuyFlag()) {
            //周期购只能 未发货、部分发货、全部发货，才可以创建退款单
            if (!trade.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED)
                    && !trade.getTradeState().getDeliverStatus().equals(DeliverStatus.PART_SHIPPED)
                    && !trade.getTradeState().getFlowState().equals(FlowState.COMPLETED)) {
                throw new SbcRuntimeException("K-050002");
            }

            if (CollectionUtils.isNotEmpty(trade.getTradeItems())) {
                //周期购只能是单个商品，此处修改为多个商品 TODO update by duanlsh
//                TradeItem tradeItem = trade.getTradeItems().get(0);
//                canReturnNum.put(tradeItem.getSkuId(), tradeItem.getNum().intValue());
                List<TradeItem> cycleBuyTradeItems = trade.getTradeItems();
                for (TradeItem cycleBuyTradeItemParam : cycleBuyTradeItems) {
                    canReturnNum.put(cycleBuyTradeItemParam.getSkuId(), cycleBuyTradeItemParam.getNum().intValue());
                }
            }

        } else {
            //普通商品
            //表示除 未发货 或者已经完成 不可以操作
            //表示代客退单

            if (replace != null && replace == 1) {
                if (!trade.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED)
                        && !trade.getTradeState().getFlowState().equals(FlowState.COMPLETED)
                        && !trade.getTradeState().getFlowState().equals(FlowState.DELIVERED_PART)
                        && !trade.getTradeState().getFlowState().equals(FlowState.DELIVERED)
                        && (!ReturnReason.PRICE_DIFF.equals(returnReason) && trade.getTradeState().getFlowState().equals(FlowState.VOID))
                        && (!ReturnReason.PRICE_DELIVERY.equals(returnReason) && trade.getTradeState().getFlowState().equals(FlowState.VOID))) {
                    throw new SbcRuntimeException("K-050004");
                }
            } else {
                if (!trade.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED) && !trade.getTradeState().getFlowState().equals(FlowState.COMPLETED)) {
                    throw new SbcRuntimeException("K-050004");
                }
            }

            //对所有商品发货的数量统计
//            Map<String, Long> skuId2DeliverNum = trade.getTradeItems().stream().collect(Collectors.toMap(TradeItem::getSkuId,TradeItem::getDeliveredNum));
            Map<String, Long> skuId2DeliverNum = trade.getTradeItems().stream().collect(Collectors.toMap(TradeItem::getSkuId,TradeItem::getNum));
            //对所有退单列表信息进行统计 不包含已作废状态以及拒绝收货的退货单与拒绝退款的退款单
            List<ReturnItem> allReturnItems = this.findReturnsNotVoid(trade.getId())
                    .stream().map(ReturnOrder::getReturnItems).reduce(new ArrayList<>(), (a, b) -> {
                            a.addAll(b);
                            return a;
                        });
            //根据skuId分组 退单列表 skuId -> ReturnItem

            Map<String, List<ReturnItem>> skuId2GroupMap = IteratorUtils.groupBy(allReturnItems, ReturnItem::getSkuId);
            skuId2DeliverNum.forEach((K, V) -> {
                List<ReturnItem> returnItems = skuId2GroupMap.get(K);
                int completeReturnCount = 0;
                if (returnItems != null) {
                    completeReturnCount = returnItems.stream().mapToInt(ReturnItem::getNum).sum();
                }
                int surplusCount = V.intValue() - completeReturnCount;
                canReturnNum.put(K, surplusCount);
            });
        }
        return canReturnNum;
    }

//    /**
//     * 订单中可退货的数量
//     *
//     * @param trade
//     */
//    private Map<String, Integer> findLeftItems(Trade trade) {
//        Map<String, Integer> canReturnNum = null;
//        if (trade.getCycleBuyFlag()) {
//            if (!trade.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED) && !trade.getTradeState().getDeliverStatus().equals(DeliverStatus.PART_SHIPPED) && !trade.getTradeState().getFlowState().equals(FlowState.COMPLETED)) {
//                throw new SbcRuntimeException("K-050002");
//            }
//            if (CollectionUtils.isNotEmpty(trade.getTradeItems())) {
//                canReturnNum = new HashMap<>();
//                TradeItem tradeItem = trade.getTradeItems().get(0);
//                canReturnNum.put(tradeItem.getSkuId(), tradeItem.getNum().intValue());
//            }
//
//        } else {
//            if (!trade.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED) && !trade.getTradeState().getFlowState().equals(FlowState.COMPLETED)) {
//                throw new SbcRuntimeException("K-050002");
//            }
//
//            Map<String, Long> map = trade.getTradeItems().stream().collect(Collectors.toMap(TradeItem::getSkuId,
//                    TradeItem::getDeliveredNum));
//            List<ReturnItem> allReturnItems = this.findReturnsNotVoid(trade.getId()).stream()
//                    .map(ReturnOrder::getReturnItems)
//                    .reduce(new ArrayList<>(), (a, b) -> {
//                        a.addAll(b);
//                        return a;
//                    });
//            Map<String, List<ReturnItem>> groupMap = IteratorUtils.groupBy(allReturnItems, ReturnItem::getSkuId);
//
//            canReturnNum = map.entrySet().stream()
//                    .collect(Collectors.toMap(
//                            Map.Entry::getKey,
//                            entry -> {
//                                String key = entry.getKey();
//                                Integer total = map.get(key).intValue();
//                                Integer returned = 0;
//                                if (groupMap.get(key) != null) {
//                                    returned = groupMap.get(key).stream().mapToInt(ReturnItem::getNum).sum();
//                                }
//                                return total - returned;
//                            }
//                    ));
//
//        }
//        return canReturnNum;
//    }

    /**
     * 订单中可退赠品的数量
     *
     * @param trade
     */
    private Map<String, Integer> findLeftGiftItems(Trade trade) {
        final Map<String, Integer> canReturnNum = new HashMap<>();
        if (CollectionUtils.isNotEmpty(trade.getGifts())) {
            Map<String, Long> skuId2DeliverNum = trade.getGifts().stream().collect(Collectors.toMap(TradeItem::getSkuId, TradeItem::getDeliveredNum));
            List<ReturnItem> allReturnItems = this.findReturnsNotVoid(trade.getId()).stream()
                    .map(ReturnOrder::getReturnGifts).reduce(new ArrayList<>(), (a, b) -> {
                        a.addAll(b);
                        return a;
                    });
            Map<String, List<ReturnItem>> skuId2GroupMap = IteratorUtils.groupBy(allReturnItems, ReturnItem::getSkuId);
            skuId2DeliverNum.forEach((K, V) -> {
                List<ReturnItem> returnItems = skuId2GroupMap.get(K);
                int completeReturnCount = 0;
                if (returnItems != null) {
                    completeReturnCount = returnItems.stream().mapToInt(ReturnItem::getNum).sum();
                }
                int surplusCount = V.intValue() - completeReturnCount;
                canReturnNum.put(K, surplusCount);
            });
        }
        return canReturnNum;
    }


    private void verifyNum(Trade trade, List<ReturnItem> returnItems, Integer replace, ReturnReason returnReason) {
        Map<String, Integer> map = this.findLeftItems(trade, replace, returnReason);
        //只是退运费的情况下，会出现num为0的情况
        if (!ReturnReason.PRICE_DELIVERY.equals(returnReason) && !ReturnReason.PRICE_DIFF.equals(returnReason) ) {
            returnItems.stream().forEach(
                    t -> {
                        //周期购订单部分发货状态下是可以退货退款
                        if (!trade.getCycleBuyFlag()) {
                            if (map.get(t.getSkuId()) - t.getNum() < 0) {
                                throw new SbcRuntimeException("K-050001", new Object[]{t.getSkuId()});
                            }
                        }
                        if (t.getNum() <= 0) {
                            throw new SbcRuntimeException("K-050102");
                        }

                    }
            );
        }
    }

    /**
     * 查询退单列表，不包含已作废状态以及拒绝收货的退货单与拒绝退款的退款单
     *
     * @return
     */
    public List<ReturnOrder> findReturnsNotVoid(String tid) {
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(tid);
        return filterFinishedReturnOrder(returnOrders);
    }

    /**
     * 过滤出已经收到退货的退单
     * (
     * 作废的不算
     * 拒绝收货不算
     * 仅退款的拒绝退款不算
     * )
     */
    public List<ReturnOrder> filterFinishedReturnOrder(List<ReturnOrder> returnOrders) {
        return returnOrders.stream().filter(t -> !t.getReturnFlowState().equals(ReturnFlowState.VOID)
                && !t.getReturnFlowState().equals(ReturnFlowState.REJECT_RECEIVE)
                && !(t.getReturnFlowState() == ReturnFlowState.REJECT_REFUND)
                && !(t.getReturnReason() != null && ReturnReason.PRICE_DELIVERY.getType().equals(t.getReturnReason().getType())))
                .collect(Collectors.toList());
    }

    /**
     * 根据订单id查询所有退单
     *
     * @param tid
     * @return
     */
    public List<ReturnOrder> findReturnByTid(String tid) {
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(tid);
        return returnOrders == null ? Collections.emptyList() : returnOrders;
    }

    /**
     * 分页
     *
     * @param endDate
     * @return
     */
    public int countReturnOrderByEndDate(LocalDateTime endDate, ReturnFlowState returnFlowState) {
        Query query = getReturnOrderQuery(endDate, returnFlowState);
        return mongoTemplate.find(query, ReturnOrder.class).size();
    }

    /**
     * 分页查询退单
     *
     * @param endDate
     * @param start
     * @param end
     * @return
     */
    public List<ReturnOrder> queryReturnOrderByEndDate(LocalDateTime endDate, int start, int end, ReturnFlowState
            returnFlowState) {
        Query query = getReturnOrderQuery(endDate, returnFlowState);
        return mongoTemplate.find(query, ReturnOrder.class).subList(start, end);
    }

    /**
     * 构建查询条件
     *
     * @param endDate endDate
     * @return Query
     */
    private Query getReturnOrderQuery(LocalDateTime endDate, ReturnFlowState returnFlowState) {
        Criteria criteria = new Criteria();
        if (ReturnFlowState.DELIVERED.equals(returnFlowState)) {
            Criteria expressCriteria = Criteria.where("returnFlowState").is("DELIVERED")
                    .and("returnType").is("RETURN").and("returnWay").is("EXPRESS").and("returnLogistics.createTime")
                    .lte(endDate);
            Criteria otherCriteria = Criteria.where("returnFlowState").is("DELIVERED")
                    .and("returnWay").is("OTHER").and("returnType").is("RETURN").and("auditTime").lte(endDate);
            criteria.orOperator(expressCriteria, otherCriteria);
        } else {
            criteria = Criteria.where("returnFlowState").is("INIT").and("createTime").lte(endDate);
        }

        return new Query(criteria);
    }

    /**
     * 分页查询订单
     *
     * @param endDate         endDate
     * @param returnFlowState returnFlowState
     * @param pageable        pageable
     * @return List<Trade>
     */
//    public List<ReturnOrder> queryReturnOrderByPage(LocalDateTime endDate, ReturnFlowState returnFlowState, Pageable
//            pageable) {
//        val pageSize = 1000;
//        //超过
//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
//        ExistsQueryBuilder filter = QueryBuilders.existsQuery("returnLogistics.createTime");
//        QueryBuilder queryBuilder;
//        //已发货的查询条件
//        if (ReturnFlowState.DELIVERED.equals(returnFlowState)) {
//            queryBuilder = QueryBuilders.boolQuery()
//                    .mustNot(QueryBuilders.boolQuery().filter(filter))
//                    .must(QueryBuilders.matchQuery("returnFlowState", returnFlowState.toValue()))
//                    .must(QueryBuilders.rangeQuery("createTime")
//                            .to(DateUtil.format(endDate, DateUtil.FMT_TIME_4)))
//                    .should(QueryBuilders.boolQuery()
//                            .must(QueryBuilders.matchQuery("returnFlowState", returnFlowState.toValue()))
//                            .must(QueryBuilders.rangeQuery("returnLogistics.createTime")
//                                    .to(DateUtil.format(endDate, DateUtil.FMT_TIME_4))));
//            //客户端带客退单的初始化状态
//        } else {
//            queryBuilder = QueryBuilders.boolQuery()
//                    .must(QueryBuilders.matchQuery("returnFlowState", returnFlowState.toValue()))
//                    .must(QueryBuilders.rangeQuery("createTime")
//                            .to(DateUtil.format(endDate, DateUtil.FMT_TIME_4)));
//        }
//
//
//        builder
//                .withIndices(EsConstants.RETURN_ORDER_INDEX)
//                .withTypes(EsConstants.RETURN_ORDER_TYPE)
//                .withQuery(
//                        queryBuilder
//                ).withPageable(new PageRequest(0, pageSize));
//
//        FacetedPage<ReturnOrder> facetedPage = template.queryForPage(builder.build(), ReturnOrder.class);
//        return facetedPage.getContent();
//    }


    /**
     * 根据退单状态统计退单
     *
     * @param
     * @return
     */
//    public ReturnOrderTodoReponse countReturnOrderByFlowState(ReturnQueryRequest request) {
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        nativeSearchQueryBuilder.withQuery(request.buildEs());
//        nativeSearchQueryBuilder.withIndices("b2b_return_order");
//        nativeSearchQueryBuilder.withTypes("return_order");
//      //  nativeSearchQueryBuilder.withSearchType(SearchType.COUNT);
//        AbstractAggregationBuilder abstractAggregationBuilder = AggregationBuilders.terms("returnType").field
//                ("returnFlowState").size(0);
//        nativeSearchQueryBuilder.addAggregation(abstractAggregationBuilder);
//        return this.template.query(nativeSearchQueryBuilder.build(), ReturnOrderTodoReponse::build);
//    }
    private void autoDeliver(String rid, Operator operator) {
        ReturnOrder returnOrder = findById(rid);

        //非快递退回的退货单，审核通过后变更为已发货状态
        if (returnOrder.getReturnType() == ReturnType.RETURN && returnOrder.getReturnWay() == ReturnWay.OTHER) {
            ReturnStateRequest request = ReturnStateRequest
                    .builder()
                    .rid(rid)
                    .operator(operator)
                    .returnEvent(ReturnEvent.DELIVER)
                    .build();
            returnFSMService.changeState(request);
        }

    }

    /**
     * 释放订单商品库存
     *
     * @param returnOrder
     */
    @Transactional
    @GlobalTransactional
    public void freeStock(ReturnOrder returnOrder, Trade trade) {
//        returnOrder.getReturnItems().stream().forEach(returnItem -> goodsInfoService.addStockById(returnItem.getNum()
//                .longValue(), returnItem.getSkuId()));
//        // 若存在赠品,赠品库存也释放
//        if (CollectionUtils.isNotEmpty(trade.getGifts())) {
//            trade.getGifts().stream().forEach(gift -> goodsInfoService.addStockById(gift.getNum(), gift.getSkuId()));
//        }
        //批量库存释放
        List<GoodsInfoPlusStockDTO> stockList = returnOrder.getReturnItems().stream().map(returnItem -> {
            GoodsInfoPlusStockDTO dto = new GoodsInfoPlusStockDTO();
            dto.setStock(returnItem.getNum().longValue());
            dto.setGoodsInfoId(returnItem.getSkuId());
            return dto;
        }).collect(Collectors.toList());

        //批量SPU库存释放
        Map<String, List<TradeItem>> items = trade.getTradeItems().stream().collect(Collectors.groupingBy(TradeItem::getSkuId));
        List<GoodsPlusStockDTO> spuStockList = returnOrder.getReturnItems().stream()
                .filter(returnItem -> items.containsKey(returnItem.getSkuId()))
                .map(returnItem -> new GoodsPlusStockDTO(returnItem.getNum().longValue(),
                        items.get(returnItem.getSkuId()).get(0).getSpuId())).collect(Collectors.toList());


        if (CollectionUtils.isNotEmpty(trade.getGifts())) {
            trade.getGifts().forEach(gift -> {
                GoodsInfoPlusStockDTO dto = new GoodsInfoPlusStockDTO();
                dto.setStock(gift.getNum());
                dto.setGoodsInfoId(gift.getSkuId());
                stockList.add(dto);

                spuStockList.add(new GoodsPlusStockDTO(gift.getNum(), gift.getSkuId()));
            });
        }

        if (CollectionUtils.isNotEmpty(stockList)) {
            goodsInfoProvider.batchPlusStock(GoodsInfoBatchPlusStockRequest.builder().stockList(stockList).build());
        }

        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()) {
            TradeItem tradeItem = trade.getTradeItems().get(0);
            List<BookingSaleGoodsVO> bookingSaleGoodsVOList = bookingSaleGoodsQueryProvider.list(BookingSaleGoodsListRequest.builder().goodsInfoId(tradeItem.getSkuId()).bookingSaleId(tradeItem.getBookingSaleId()).build()).getContext().getBookingSaleGoodsVOList();
            if (Objects.nonNull(bookingSaleGoodsVOList.get(0).getBookingCount())) {
                bookingSaleGoodsProvider.addCanBookingCount(BookingSaleGoodsCountRequest.builder().goodsInfoId(tradeItem.getSkuId()).
                        bookingSaleId(tradeItem.getBookingSaleId()).stock(stockList.get(0).getStock()).build());
            }
        }

        //SPU库存释放
        goodsStockService.batchAddStock(spuStockList);

        //秒杀扣减抢购销量
        if (Objects.nonNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods() && CollectionUtils.isNotEmpty(stockList)) {
            FlashSaleGoodsBatchStockAndSalesVolumeRequest request = FlashSaleGoodsBatchStockAndSalesVolumeRequest.builder()
                    .id(trade.getTradeItems().get(0).getFlashSaleGoodsId())
                    .num(trade.getTradeItems().get(0).getNum().intValue()).build();
            flashSaleGoodsSaveProvider.subSalesVolumeById(request);
        }
    }

    public boolean isReturnFull(ReturnOrder returnOrder) {
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(returnOrder.getTid());
        returnOrders = returnOrders.stream().filter(item -> item.getReturnType() == ReturnType.RETURN).collect
                (Collectors.toList());

        List<ReturnItem> returnItems = returnOrders.stream().filter(item -> item.getReturnFlowState() ==
                ReturnFlowState.COMPLETED
                || item.getReturnFlowState() == ReturnFlowState.RECEIVED
                || item.getReturnFlowState() == ReturnFlowState.REJECT_REFUND).flatMap(item -> item.getReturnItems()
                .stream())
                .collect(Collectors.toList());
        Map<String, Long> tradeNumMap = tradeService.detail(returnOrder.getTid()).getTradeItems().stream().collect(
                Collectors.toMap(TradeItem::getSkuId, TradeItem::getNum));

        Map<String, Integer> returnNumMap = IteratorUtils.groupBy(returnItems, ReturnItem::getSkuId).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream().mapToInt(ReturnItem::getNum).sum()));
        Optional optional = tradeNumMap.entrySet().stream().filter(entry -> {
            Integer num = returnNumMap.get(entry.getKey());
            return num == null || num != entry.getValue().intValue();
        }).findFirst();
        return !optional.isPresent();
    }

    private void verifyPrice(Map<String, TradeItem> skuItemMap, List<ReturnItem> returnItems) {
        returnItems.forEach(r -> {
            TradeItem item = skuItemMap.get(r.getSkuId());
            if (r.getPrice().compareTo(item.getPrice()) != 0) {
                throw new SbcRuntimeException("K-050207");
            }
        });
    }

    /**
     * 分页查询账期内退单信息
     *
     * @param storeId
     * @param startTime
     * @param endTime
     * @param pageRequest
     * @return
     */
    public Page<ReturnOrder> getReturnListByPage(Long storeId, Date startTime, Date endTime, PageRequest pageRequest) {
        return returnOrderRepository.findByCompany_StoreIdAndFinishTimeBetweenAndReturnFlowState(storeId, startTime,
                endTime, pageRequest, ReturnFlowState.COMPLETED);
    }

    /**
     * 更新退单的结算状态
     *
     * @param storeId
     * @param startDate
     * @param endDate
     */
    public void updateSettlementStatus(Long storeId, Date startDate, Date endDate) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("company.storeId").is(storeId)
                , Criteria.where("returnFlowState").is(ReturnFlowState.COMPLETED)
                , Criteria.where("finishTime").lt(endDate).gt(startDate));
        mongoTemplate.updateMulti(new Query(criteria), new Update().set("hasBeanSettled", true), ReturnOrder.class);
    }


    /**
     * 结算解析获取原始数据，获取退单集合
     *
     * @param storeId
     * @param startDate
     * @param endDate
     */
    public List<ReturnOrder> findReturnOrderListForSettlement(Long storeId, Date startDate, Date endDate, Pageable
            pageRequest) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("company.storeId").is(storeId)
                , Criteria.where("returnFlowState").is(ReturnFlowState.COMPLETED)
                , Criteria.where("finishTime").lt(endDate).gte(startDate)
                , Criteria.where("hasBeanSettled").ne(true)
        );
        return mongoTemplate.find(new Query(criteria).skip(pageRequest.getPageNumber() * pageRequest.getPageSize())
                .limit(pageRequest.getPageSize()), ReturnOrder.class);
    }

    /**
     * 更新退单的业务员
     *
     * @param employeeId 业务员
     * @param customerId 客户
     */
    public void updateEmployeeId(String employeeId, String customerId) {
        mongoTemplate.updateMulti(new Query(Criteria.where("buyer.id").is(customerId)), new Update().set("buyer" +
                ".employeeId", employeeId), ReturnOrder.class);
    }

    /**
     * 完善没有业务员的退单
     */
    public void fillEmployeeId() {
        List<ReturnOrder> trades = mongoTemplate.find(new Query(Criteria.where("buyer.employeeId").is(null)),
                ReturnOrder.class);
        if (CollectionUtils.isEmpty(trades)) {
            return;
        }
        List<String> buyerIds = trades.stream()
                .filter(t -> Objects.nonNull(t.getBuyer()) && StringUtils.isNotBlank(t.getBuyer().getId()))
                .map(ReturnOrder::getBuyer)
                .map(Buyer::getId)
                .distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(buyerIds)) {
            return;
        }

        Map<String, String> customerId = customerCommonService.listCustomerDetailByCondition(
                CustomerDetailListByConditionRequest.builder().customerIds(buyerIds).build())
                .stream()
                .filter(customerDetail -> StringUtils.isNotBlank(customerDetail.getEmployeeId()))
                .collect(Collectors.toMap(CustomerDetailVO::getCustomerId, CustomerDetailVO::getEmployeeId));

        customerId.forEach((key, value) -> this.updateEmployeeId(value, key));
    }

    /**
     * 根据条件统计
     *
     * @param queryBuilder
     * @return
     */
    private long countNum(Criteria queryBuilder) {
        Query query = new Query(queryBuilder);
        long total = mongoTemplate.count(query, ReturnOrder.class);
        return total;
    }

    /**
     * 退款失败
     *
     * @param refundOrderRefundRequest
     */
    @Transactional
    @GlobalTransactional
    public void refundFailed(RefundOrderRefundRequest refundOrderRefundRequest) {
        ReturnOrder returnOrder = returnOrderRepository.findById(refundOrderRefundRequest.getRid()).orElse(null);
        if (Objects.isNull(returnOrder)) {
            logger.error("退单ID:{},查询不到退单信息", refundOrderRefundRequest.getRid());
            return;
        }
        ReturnFlowState flowState = returnOrder.getReturnFlowState();
        // 如果已是退款状态的订单，直接return，不做状态扭转处理
        if (flowState == ReturnFlowState.REFUND_FAILED) {
            return;
        }
        returnOrder.setRefundFailedReason(refundOrderRefundRequest.getFailedReason());
        returnOrderService.updateReturnOrder(returnOrder);
        //修改退单状态
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(refundOrderRefundRequest.getRid())
                .operator(refundOrderRefundRequest.getOperator())
                .returnEvent(ReturnEvent.REFUND_FAILED)
                .build();
        returnFSMService.changeState(request);
    }

    /**
     * 关闭退款
     *
     * @param rid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void closeRefund(String rid, Operator operator) {
        ReturnOrder returnOrder = findById(rid);
        TradeStatus tradeStatus = payQueryProvider.getRefundResponseByOrdercode(new RefundResultByOrdercodeRequest
                (returnOrder.getTid(), returnOrder.getId())).getContext().getTradeStatus();
        if (tradeStatus != null) {
            if (tradeStatus == TradeStatus.SUCCEED) {
                throw new SbcRuntimeException("K-100104");
            } else if (tradeStatus == TradeStatus.PROCESSING) {
                throw new SbcRuntimeException("K-100105");
            }
        }

        //修改财务退款单状态
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(rid);
        refundOrder.setRefundStatus(RefundStatus.FINISH);
        refundOrderRepository.saveAndFlush(refundOrder);

        //修改退单状态
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.CLOSE_REFUND)
                .build();
        returnFSMService.changeState(request);

        Trade trade = tradeService.detail(returnOrder.getTid());
        trade.setRefundFlag(true);
        tradeService.updateTrade(trade);
        // 作废订单也需要释放库存
        freeStock(returnOrder, trade);
        //作废订单
        if (providerTradeAllVoid(returnOrder)) {
            tradeService.voidTrade(returnOrder.getTid(), operator);
        }
        trade.getTradeState().setEndTime(LocalDateTime.now());
    }

    /**
     * 返回掩码后的字符串
     *
     * @param bankNo
     * @return
     */
    public static String getDexAccount(String bankNo) {
        String middle = "****";
        if (bankNo.length() > 4) {
            if (bankNo.length() <= 8) {
                return middle;
            } else {
                // 如果是手机号
                if (bankNo.length() == 11) {
                    bankNo = bankNo.substring(0, 3) + middle + bankNo.substring(bankNo.length() - 4);
                } else {
                    bankNo = bankNo.substring(0, 4) + middle + bankNo.substring(bankNo.length() - 4);
                }
            }
        } else {
            return middle;
        }
        return bankNo;
    }

    /**
     * 拼团退单相应处理
     *
     * @param returnOrder
     * @param trade
     */
    private void modifyGrouponInfo(final ReturnOrder returnOrder, final Trade trade) {
        List<String> tradeItemIds =
                trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        TradeGroupon tradeGroupon = trade.getTradeGroupon();
        //拼团订(退)单只可能有一个sku
        ReturnItem returnItem = returnOrder.getReturnItems().get(0);
        //step1. 修改订单拼团信息
        tradeGroupon.setReturnPrice(returnOrder.getReturnPrice().getActualReturnPrice());
        tradeGroupon.setReturnNum(returnItem.getNum());
        //step2. 修改拼团商品计数
        GrouponGoodsInfoReturnModifyRequest returnModifyRequest;
        if (RefundReasonConstants.Q_ORDER_SERVICE_GROUPON_AUTO_REFUND.equals(returnOrder.getDescription())) {
            returnModifyRequest = GrouponGoodsInfoReturnModifyRequest.builder()
                    .grouponActivityId(tradeGroupon.getGrouponActivityId())
                    .amount(returnOrder.getReturnPrice().getActualReturnPrice())
                    .goodsInfoId(returnItem.getSkuId())
                    .num(NumberUtils.INTEGER_ZERO)
                    .build();
        } else {
            returnModifyRequest = GrouponGoodsInfoReturnModifyRequest.builder()
                    .grouponActivityId(tradeGroupon.getGrouponActivityId())
                    .amount(returnOrder.getReturnPrice().getActualReturnPrice())
                    .goodsInfoId(returnItem.getSkuId())
                    .num(returnItem.getNum())
                    .build();
        }
        grouponGoodsInfoProvider.modifyReturnInfo(returnModifyRequest);
        //step3.修改拼团活动计数
        if (RefundReasonConstants.Q_ORDER_SERVICE_GROUPON_AUTO_REFUND.equals(returnOrder.getDescription()) ||
                RefundReasonConstants.Q_ORDER_SERVICE_GROUPON_AUTO_REFUND_USER.equals(returnOrder.getDescription())) {
            GrouponOrderStatus grouponOrderStatus = GrouponOrderStatus.FAIL;
            //自动成团设置未选中，活动到期参团失败，减待成团数，加失败数
            activityProvider.modifyStatisticsNumById(GrouponActivityModifyStatisticsNumByIdRequest.builder()
                    .grouponActivityId(tradeGroupon.getGrouponActivityId())
                    .grouponNum(1)
                    .grouponOrderStatus(grouponOrderStatus)
                    .build()
            );
            tradeGroupon.setGrouponOrderStatus(grouponOrderStatus);
        }
        //用户支付成功-立即退款 & 拼团失败：团状态更新为拼团失败
        if (RefundReasonConstants.Q_ORDER_SERVICE_GROUPON_AUTO_REFUND.equals(returnOrder.getDescription()) ||
                RefundReasonConstants.Q_ORDER_SERVICE_GROUPON_AUTO_REFUND_USER.equals(returnOrder.getDescription())) {
            if (tradeGroupon.getLeader()) {
                GrouponInstance grouponInstance = grouponOrderService.getGrouponInstanceByActivityIdAndGroupon(tradeGroupon.getGrouponNo());
                grouponInstance.setGrouponStatus(GrouponOrderStatus.FAIL);
                grouponInstance.setFailTime(LocalDateTime.now());
                grouponOrderService.updateGrouponInstance(grouponInstance);
            }
        }
        //step4.减去拼团商品已购买数
        recordProvider.decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(GrouponRecordDecrBuyNumRequest.builder()
                .customerId(returnOrder.getBuyer().getId())
                .grouponActivityId(tradeGroupon.getGrouponActivityId())
                .goodsInfoId(returnItem.getSkuId())
                .buyNum(returnItem.getNum())
                .build()
        );
    }

    /**
     * 根据退单ID在线退款
     *
     * @param returnOrderCode
     * @param operator
     * @return
     */
    public List<Object> refundOnlineByTid(String returnOrderCode, Operator operator) {

        //查询退单
        ReturnOrder returnOrder = returnOrderService.findById(returnOrderCode);

        //退款退单状态需要是已审核
        if (returnOrder != null && returnOrder.getReturnType() == ReturnType.REFUND) {
            if (returnOrder.getReturnFlowState() != ReturnFlowState.AUDIT &&
                    returnOrder.getReturnFlowState() != ReturnFlowState.REFUND_FAILED) {
                throw new SbcRuntimeException("K-050004");
            }
        }
        //退货退单状态需要是已收到退货/退款失败
        if (returnOrder != null && returnOrder.getReturnType() == ReturnType.RETURN) {
            if (returnOrder.getReturnFlowState() != ReturnFlowState.RECEIVED &&
                    returnOrder.getReturnFlowState() != ReturnFlowState.REFUND_FAILED) {
                throw new SbcRuntimeException("K-050004");
            }
        }
        // 查询退款单
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(returnOrderCode);

        Trade trade = tradeService.detail(returnOrder.getTid());

       //根据主单号查询所有发货单并以此判断主单是部分发货还是全部发货
//        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(trade.getId());
        //判断周期购订单赠品是否是虚拟或者电子卡券
        if (trade.getCycleBuyFlag()) {
            List<TradeItem> gifts = trade.getGifts().stream().filter(tradeItem -> tradeItem.getGoodsType() == GoodsType.VIRTUAL_GOODS || tradeItem.getGoodsType() == GoodsType.VIRTUAL_COUPON).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(gifts)) {
                //已发货不允许退款，原因是用户先发起的退款，erp发货同步不及时
                if (returnOrder.getReturnType().equals(ReturnType.REFUND) && !trade.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED)) {
                    //退款单更新，已拒绝
                    this.refundReject(returnOrder, refundOrder);
                    return null;
                }
            }

        }

//        else {
//            //已发货不允许退款，原因是用户先发起的退款，erp发货同步不及时
//           if (returnOrder.getReturnType().equals(ReturnType.REFUND) && providerTrades.stream().anyMatch(p->Objects.equals(p.getId(),returnOrder.getPtid()) && Arrays.asList(DeliverStatus.PART_SHIPPED,DeliverStatus.SHIPPED).contains(p.getTradeState().getDeliverStatus()))) {
//                //退款单更新，已拒绝
//                this.refundReject(returnOrder, refundOrder);
//                return null;
//            }
//        }


        if (Objects.nonNull(trade) && Objects.nonNull(trade.getBuyer()) && StringUtils.isNotEmpty(trade.getBuyer()
                .getAccount())) {
            trade.getBuyer().setAccount(ReturnOrderService.getDexAccount(trade.getBuyer().getAccount()));
        }

        if (Objects.isNull(operator) || StringUtils.isBlank(operator.getUserId())) {
            operator = Operator.builder().ip("127.0.0.1").adminId("1").name("system").platform(Platform
                    .BOSS).build();
        }


        return refundOrderService.autoRefund(Collections.singletonList(trade), Collections.singletonList(returnOrder), Collections.singletonList(refundOrder), operator);
    }


    /**
     * 退单通知节点发送MQ消息
     *
     * @param nodeType
     * @param processType
     * @param params
     * @param rid
     * @param customerId
     */
    private void sendNoticeMessage(NodeType nodeType, ReturnOrderProcessType processType, List<String> params, String rid, String customerId, String pic, String mobile) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", nodeType.toValue());
        map.put("node", processType.toValue());
        map.put("id", rid);
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(processType.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(Lists.newArrayList(params));
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setPic(pic);
        messageMQRequest.setMobile(mobile);
        returnOrderProducerService.sendMessage(messageMQRequest);
    }

    /**
     * 构建退货收件地址
     *
     * @param addressId
     * @return 封装后的
     */
    private ReturnAddress wapperReturnAddress(String addressId, Long storeId) {
        StoreReturnAddressByIdResponse address = returnAddressQueryProvider.getById(
                StoreReturnAddressByIdRequest.builder()
                        .addressId(addressId)
                        .storeId(storeId)
                        .showAreaName(Boolean.TRUE)
                        .build()).getContext();
        if (Objects.nonNull(address) && Objects.nonNull(address.getStoreReturnAddressVO())) {
            return this.packageReturnAddress(address.getStoreReturnAddressVO());
        }
        return null;
    }

    /**
     * 封装对象
     * @param addressVO
     * @return
     */
    private ReturnAddress packageReturnAddress(StoreReturnAddressVO addressVO) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.defaultString(addressVO.getProvinceName()));
        sb.append(StringUtils.defaultString(addressVO.getCityName()));
        sb.append(StringUtils.defaultString(addressVO.getAreaName()));
        sb.append(StringUtils.defaultString(addressVO.getStreetName()));
        sb.append(StringUtils.defaultString(addressVO.getReturnAddress()));
        return ReturnAddress.builder()
                .id(addressVO.getAddressId())
                .name(addressVO.getConsigneeName())
                .phone(addressVO.getConsigneeNumber())
                .provinceId(addressVO.getProvinceId())
                .provinceName(addressVO.getProvinceName())
                .cityId(addressVO.getCityId())
                .cityName(addressVO.getCityName())
                .areaId(addressVO.getAreaId())
                .areaName(addressVO.getAreaName())
                .streetId(addressVO.getStreetId())
                .address(addressVO.getReturnAddress())
                .detailAddress(sb.toString())
                .build();
    }

    /**
     * 补偿拒绝退款
     *
     * @param returnOrder
     * @param refundOrder
     */
    @Transactional
    @GlobalTransactional
    public void refundReject(ReturnOrder returnOrder, RefundOrder refundOrder) {
        //退款单更新，已拒绝
        returnOrder.setReturnFlowState(ReturnFlowState.REJECT_REFUND);
        returnOrder.setRejectReason("已发货不允许退款");
        returnOrderRepository.save(returnOrder);
        //修改财务退款单状态
        if (refundOrder.getRefundStatus().equals(RefundStatus.APPLY)) {
            throw new SbcRuntimeException("K-050002");
        }
        refundOrderService.refuse(refundOrder.getRefundId(), "已发货不允许退款");
        // 拒绝退款时，发送MQ消息
        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.FALSE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(returnOrder.getId())
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);
    }


    /**
     * 拒绝退款视频号
     *
     */
    @Transactional
    public void refundReject(RefundRejectRequest refundRejectRequest){
        //查询退单
        ReturnOrder returnOrder = returnOrderService.findById(refundRejectRequest.getRid());
        // 查询退款单
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(refundRejectRequest.getRid());

        //退款单更新，已拒绝
        returnOrder.setReturnFlowState(ReturnFlowState.REJECT_REFUND);
        returnOrder.setRejectReason(refundRejectRequest.getReturnReanson());
        returnOrder.setForceReject(refundRejectRequest.getForceReject());
        returnOrderRepository.save(returnOrder);

        refundOrderService.refuse(refundOrder.getRefundId(), refundRejectRequest.getReturnReanson());
        // 拒绝退款时，发送MQ消息
        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.FALSE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(returnOrder.getId())
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);
    }




     /**
     * 获取子单的退单列表
     * @param request
     * @return
     */
    public List<ProviderTradeSimpleVO> listReturnProviderTrade(ReturnOrderProviderTradeRequest request) {
        //根据住单获取子订单
        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(request.getTid());
        //订单不存在
        if (CollectionUtils.isEmpty(providerTradeList)) {
            throw new SbcRuntimeException("K-050100", new Object[]{request.getTid()});
        }
        Map<String, BigDecimal> returnDeliveryCompleteMap = new HashMap<>();
        Map<String, BigDecimal> returnDeliveryIngMap = new HashMap<>();
        Map<String, Integer> returnSkuIdNumCompleteMap = new HashMap<>();
        Map<String, Integer> returnSkuIdNumIngMap = new HashMap<>();
        Map<String, BigDecimal> returnSkuIdPriceCompleteMap = new HashMap<>();
        Map<String, BigDecimal> returnSkuIdPriceIngMap = new HashMap<>();
        Map<String, Long> returnSkuIdPointCompleteMap = new HashMap<>();
        Map<String, Long> returnSkuIdPointIngMap = new HashMap<>();
        Map<String, Long> returnSkuIdKnowledgeCompleteMap = new HashMap<>();
        Map<String, Long> returnSkuIdKnowledgeIngMap = new HashMap<>();
        //获取对单列表
        List<ReturnOrder> returnOrderList = returnOrderRepository.findByTid(request.getTid());
        for (ReturnOrder returnOrderParam : returnOrderList) {
            if (returnOrderParam.getReturnFlowState() == ReturnFlowState.VOID) {
                continue;
            }
            if (returnOrderParam.getReturnFlowState() == ReturnFlowState.REJECT_REFUND) {
                continue;
            }
            if (returnOrderParam.getReturnFlowState() == ReturnFlowState.REJECT_RECEIVE) {
                continue;
            }
//            if (returnOrderParam.getReturnType() != ReturnType.REFUND && returnOrderParam.getReturnFlowState() != ReturnFlowState.REJECT_REFUND) {
//                continue;
//            }
            if (returnOrderParam.getReturnReason() != null && returnOrderParam.getReturnReason() == ReturnReason.PRICE_DELIVERY) {
                if (returnOrderParam.getReturnFlowState() == ReturnFlowState.COMPLETED) {
                    returnDeliveryCompleteMap.put(returnOrderParam.getProviderId(), returnOrderParam.getReturnPrice().getApplyPrice());
                } else {
                    returnDeliveryIngMap.put(returnOrderParam.getProviderId(), returnOrderParam.getReturnPrice().getApplyPrice());
                }
                continue;
            }

            for (ReturnItem returnItemParam : returnOrderParam.getReturnItems()) {
                if (returnOrderParam.getReturnFlowState() == ReturnFlowState.COMPLETED) {
                    //完成数量
                    int returnItemNum = returnSkuIdNumCompleteMap.get(returnItemParam.getSkuId()) == null ? 0 : returnSkuIdNumCompleteMap.get(returnItemParam.getSkuId());
                    returnItemNum += (returnItemParam.getNum() == null ? 0 :  returnItemParam.getNum());
                    returnSkuIdNumCompleteMap.put(returnItemParam.getSkuId(), returnItemNum);

                    //完成的金额
                    BigDecimal returnItemPriceSum = returnSkuIdPriceCompleteMap.get(returnItemParam.getSkuId()) == null ? BigDecimal.ZERO : returnSkuIdPriceCompleteMap.get(returnItemParam.getSkuId());
                    returnItemPriceSum = returnItemPriceSum.add(returnItemParam.getApplyRealPrice() == null ? BigDecimal.ZERO : returnItemParam.getApplyRealPrice());
                    returnSkuIdPriceCompleteMap.put(returnItemParam.getSkuId(), returnItemPriceSum);

                    //完成的积分
                    long returnItemPoint = returnSkuIdPointCompleteMap.get(returnItemParam.getSkuId()) == null ? 0L : returnSkuIdPointCompleteMap.get(returnItemParam.getSkuId());
                    returnItemPoint += (returnItemParam.getApplyPoint() == null ? 0L : returnItemParam.getApplyPoint());
                    returnSkuIdPointCompleteMap.put(returnItemParam.getSkuId(), returnItemPoint);

                    //完成的知豆
                    long returnItemKnowledge = returnSkuIdKnowledgeCompleteMap.get(returnItemParam.getSkuId()) == null ? 0L : returnSkuIdKnowledgeCompleteMap.get(returnItemParam.getSkuId());
                    returnItemKnowledge += returnItemParam.getApplyKnowledge() == null ? 0L : returnItemParam.getApplyKnowledge();
                    returnSkuIdKnowledgeCompleteMap.put(returnItemParam.getSkuId(), returnItemKnowledge);
                } else {
                    //退款中的数量
                    int returnItemNum = returnSkuIdNumIngMap.get(returnItemParam.getSkuId()) == null ? 0 : returnSkuIdNumIngMap.get(returnItemParam.getSkuId());
                    returnItemNum += returnItemParam.getNum() == null ? 0 : returnItemParam.getNum();
                    returnSkuIdNumIngMap.put(returnItemParam.getSkuId(), returnItemNum);

                    //退款中的金额
                    BigDecimal returnItemPriceSum = returnSkuIdPriceIngMap.get(returnItemParam.getSkuId()) == null ? BigDecimal.ZERO : returnSkuIdPriceIngMap.get(returnItemParam.getSkuId());
                    returnItemPriceSum = returnItemPriceSum.add(returnItemParam.getApplyRealPrice() == null ? BigDecimal.ZERO : returnItemParam.getApplyRealPrice());
                    returnSkuIdPriceIngMap.put(returnItemParam.getSkuId(), returnItemPriceSum);

                    //退款中的积分
                    long returnItemPoint = returnSkuIdPointIngMap.get(returnItemParam.getSkuId()) == null ? 0L : returnSkuIdPointIngMap.get(returnItemParam.getSkuId());
                    returnItemPoint += returnItemParam.getApplyPoint() == null ? 0L : returnItemParam.getApplyPoint();
                    returnSkuIdPointIngMap.put(returnItemParam.getSkuId(), returnItemPoint);

                    //退款中的知豆
                    long returnItemKnowledge = returnSkuIdKnowledgeIngMap.get(returnItemParam.getSkuId()) == null ? 0L : returnSkuIdKnowledgeIngMap.get(returnItemParam.getSkuId());
                    returnItemKnowledge += returnItemParam.getApplyKnowledge() == null ? 0L : returnItemParam.getApplyKnowledge();
                    returnSkuIdKnowledgeIngMap.put(returnItemParam.getSkuId(), returnItemKnowledge);
                }
            }
        }

        List<ProviderTradeSimpleVO> result = new ArrayList<>();
        for (ProviderTrade providerTradeParam : providerTradeList) {
            ProviderTradeSimpleVO providerTradeSimpleVO = new ProviderTradeSimpleVO();
            providerTradeSimpleVO.setTid(providerTradeParam.getParentId());
            providerTradeSimpleVO.setPid(providerTradeParam.getId());

            Supplier supplier = providerTradeParam.getSupplier();
            String providerId = supplier == null ? "" : supplier.getStoreId().toString();
            providerTradeSimpleVO.setProviderId(providerId);
            providerTradeSimpleVO.setProviderName(supplier == null ? "" : supplier.getStoreName());
            BigDecimal deliverCompletePrice = returnDeliveryCompleteMap.get(providerId);
            BigDecimal deliverIngPrice = returnDeliveryIngMap.get(providerId);

            providerTradeSimpleVO.setDeliveryCompletePrice(deliverCompletePrice == null ? BigDecimal.ZERO.toString() : deliverCompletePrice.toString());
            providerTradeSimpleVO.setDeliveryIngPrice(deliverIngPrice == null ? BigDecimal.ZERO.toString() : deliverIngPrice.toString());
            providerTradeSimpleVO.setDeliveryPrice(providerTradeParam.getTradePrice().getDeliveryPrice().toString());

            List<TradeItemSimpleVO> tradeItemSimpleVOList = new ArrayList<>();
            for (TradeItem tradeItemParam : providerTradeParam.getTradeItems()) {
                TradeItemSimpleVO tradeItemSimpleVO = KsBeanUtil.convert(tradeItemParam, TradeItemSimpleVO.class);
                tradeItemSimpleVO.setReturnCompleteNum(
                        returnSkuIdNumCompleteMap.get(tradeItemParam.getSkuId()) == null ? 0 : returnSkuIdNumCompleteMap.get(tradeItemParam.getSkuId()));
                tradeItemSimpleVO.setReturnIngNum(
                        returnSkuIdNumIngMap.get(tradeItemParam.getSkuId()) == null ? 0 : returnSkuIdNumIngMap.get(tradeItemParam.getSkuId()));
                tradeItemSimpleVO.setReturnCompletePrice(
                        returnSkuIdPriceCompleteMap.get(tradeItemParam.getSkuId()) == null ? BigDecimal.ZERO.toString() : returnSkuIdPriceCompleteMap.get(tradeItemParam.getSkuId()).toString());
                tradeItemSimpleVO.setReturnIngPrice(
                        returnSkuIdPriceIngMap.get(tradeItemParam.getSkuId()) == null ? BigDecimal.ZERO.toString() : returnSkuIdPriceIngMap.get(tradeItemParam.getSkuId()).toString());

                tradeItemSimpleVO.setReturnCompletePoint(
                        returnSkuIdPointCompleteMap.get(tradeItemParam.getSkuId()) == null ? 0L : returnSkuIdPointCompleteMap.get(tradeItemParam.getSkuId()));
                tradeItemSimpleVO.setReturnIngPoint(
                        returnSkuIdPointIngMap.get(tradeItemParam.getSkuId()) == null ? 0L : returnSkuIdPointIngMap.get(tradeItemParam.getSkuId()));

                tradeItemSimpleVO.setReturnCompleteKnowledge(
                        returnSkuIdKnowledgeCompleteMap.get(tradeItemParam.getSkuId()) == null ? 0L : returnSkuIdKnowledgeCompleteMap.get(tradeItemParam.getSkuId()));
                tradeItemSimpleVO.setReturnIngKnowledge(
                        returnSkuIdKnowledgeIngMap.get(tradeItemParam.getSkuId()) == null ? 0L : returnSkuIdKnowledgeIngMap.get(tradeItemParam.getSkuId()));
                tradeItemSimpleVOList.add(tradeItemSimpleVO);
            }
            providerTradeSimpleVO.setTradeItems(tradeItemSimpleVOList);
            result.add(providerTradeSimpleVO);
        }
        return result;
    }

    /**
     * 微信退款单-小程序场景
     * @param returnOrder
     */
    public void addWxAfterSale(ReturnOrder returnOrder, WxAfterSaleStatus status,WxAfterSaleOperateType saleOperateType, String desc){
        if(!Objects.equals(returnOrder.getChannelType(), ChannelType.MINIAPP)){
            return;
        }
        Trade trade = tradeService.detail(returnOrder.getTid());
        if (trade == null) {
            throw new SbcRuntimeException("K-050100", new Object[]{returnOrder.getTid()});
        }
        //小程序售后
        if (Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.MINI_PROGRAM.getIndex())) {
            WxCreateAfterSaleRequest request = new WxCreateAfterSaleRequest();
            request.setCreateTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1));
            request.setFinishAllAftersale(0);
            request.setOutOrderId(returnOrder.getTid());
            request.setOutAftersaleId(returnOrder.getId());
            request.setOpenid(returnOrder.getBuyer().getOpenId());
            request.setType(Objects.equals(ReturnType.RETURN,returnOrder.getReturnType())?2:1);
            request.setPath(orderDetailUrl+returnOrder.getTid());
            request.setStatus(status.getId());
            request.setRefund(returnOrder.getReturnPrice().getTotalPrice().multiply(new BigDecimal(100)).intValue());
            List<WxProductDTO> products = new ArrayList<>();
            returnOrder.getReturnItems().forEach(item->{
                products.add(WxProductDTO.builder().outProductId(item.getSpuId()).outSkuId(item.getSkuId()).prroductNum(item.getNum()).build());
            });
            request.setProductInfos(products);
            try {
                BaseResponse<WxResponseBase> response = wxOrderApiController.createAfterSale(request);
                log.info("微信小程序创建售后request:{},response:{}",request,response);
            }catch (Exception e){
                log.error("微信小程序创建售后失败，returnOrder:{},WxAfterSaleStatus:{}",returnOrder,status,e);
            }
        }else if(Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex()) && saleOperateType !=null) {
            //视频号售后
            switch (saleOperateType) {
//                case 1:
//                    wxOrderService.addEcAfterSale(returnOrder);
//                    break;
                case CANCEL:
                    wxOrderService.cancelAfterSale(returnOrder);
                    break;
                case REJECT:
                    wxOrderService.rejectAfterSale(returnOrder);
                    break;
                case REFUND:
                    wxOrderService.acceptRefundAfterSale(returnOrder);
                    break;
                case RETURN:
                    wxOrderService.acceptReturnAfterSale(returnOrder);
                    break;
                case UPLOAD_RETURN_INFO:
                    wxOrderService.uploadReturnInfo(returnOrder);
                    break;
                default:
                    break;
            }
        }
        //通知
        wxOrderService.sendWxAfterSaleMessage(returnOrder);
    }


    /**
     * 更新物流信息
     * @param rid
     * @param logistics
     * @return
     */
    public BaseResponse updateReturnLogistics(String rid, ReturnLogistics logistics) {

        ReturnOrder returnOrder = returnOrderRepository.findById(rid).orElse(null);
        if (returnOrder == null) {
            throw new SbcRuntimeException("K-050003");
        }

        Update update = new Update();
        if (StringUtils.isNotBlank(logistics.getCode())) {
            update.set("returnLogistics.code", logistics.getCode());
        }
        if (StringUtils.isNotBlank(logistics.getCompany())) {
            update.set("returnLogistics.company", logistics.getCompany());
        }
        if (StringUtils.isNotBlank(logistics.getNo())) {
            update.set("returnLogistics.no", logistics.getNo());
        }
        if (logistics.getCreateTime() != null) {
            update.set("returnLogistics.createTime", logistics.getCreateTime());
        }
        if (!CollectionUtils.isEmpty(logistics.getPicList())) {
            update.set("returnLogistics.picList", logistics.getPicList());
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(returnOrder.getId())), update, ReturnOrder.class);
        log.info("ReturnOrderService updateLogisics rid:{} result:{}", returnOrder.getId(), JSON.toJSONString(updateResult));
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 拒绝退货 转到 DELIVERED 已发退货
     * @param rid
     * @param operator
     * @param reason
     */
    @GlobalTransactional
    @Transactional
    public void rejectReceive2Delivered(String rid, Operator operator, String reason) {
        ReturnOrder returnOrder = this.findById(rid);
        if (returnOrder.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE) {
            throw new SbcRuntimeException("K-050464");
        }
        if (returnOrder.getReturnType() != ReturnType.RETURN) {
            throw new SbcRuntimeException("K-050465");
        }
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.REVERSE_RETURN)
                .data(reason)
                .build();
        returnFSMService.changeState(request);

        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.TRUE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(rid)
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);
        log.info("ReturnOrderService rejectReceive 拒绝收货转化到买家已发货 tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());

        //扭转订单状态通知
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.AFTER_SALE_ORDER_CHECK_PASS,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }
    }

    /**
     * 拒绝退款 2 审核成功  状态扭转
     * @param rid
     * @param reason
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void rejectRefund2Audit(String rid, String reason, Operator operator) {
        ReturnOrder returnOrder = findById(rid);
        if (returnOrder.getReturnFlowState() != ReturnFlowState.REJECT_REFUND) {
            throw new SbcRuntimeException("K-050464");
        }
        if (returnOrder.getReturnType() != ReturnType.RETURN) {
            throw new SbcRuntimeException("K-050465");
        }

        TradeStatus tradeStatus = payQueryProvider.getRefundResponseByOrdercode(new RefundResultByOrdercodeRequest
                (returnOrder.getTid(), returnOrder.getId())).getContext().getTradeStatus();
        if (tradeStatus != null) {
            if (tradeStatus == TradeStatus.SUCCEED) {
                throw new SbcRuntimeException("K-100104");
            } else if (tradeStatus == TradeStatus.PROCESSING) {
                throw new SbcRuntimeException("K-100105");
            }
        }
        //修改财务退款单状态
        RefundOrder refundOrder = refundOrderService.findRefundOrderByReturnOrderNo(rid);
        if (refundOrder.getRefundStatus().equals(RefundStatus.APPLY)) {
            throw new SbcRuntimeException("K-050002");
        }


        refundOrderService.backRefuse(refundOrder.getRefundId(), reason);
        //修改退单状态
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.REVERSE_RETURN)
                .data(reason)
                .build();
        returnFSMService.changeState(request);

        // 拒绝退款时，发送MQ消息
        ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                .addFlag(Boolean.TRUE)
                .customerId(returnOrder.getBuyer().getId())
                .orderId(returnOrder.getTid())
                .returnId(rid)
                .build();
        returnOrderProducerService.returnOrderFlow(sendMQRequest);

        //退款审核未通过发送MQ消息
        log.info("ReturnOrderService rejectRefund2Audit 拒绝退款到审核成功  tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.AFTER_SALE_ORDER_CHECK_PASS,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }
    }


    /**
     * 状态扭转，审核状态到作废状态
     * @param rid
     * @param reason
     * @param operator
     */
    public void audit2Void(String rid, String reason, Operator operator) {
        ReturnOrder returnOrder = findById(rid);
        if (returnOrder.getReturnFlowState() != ReturnFlowState.AUDIT) {
            throw new SbcRuntimeException("K-050464");
        }
        if (returnOrder.getReturnType() != ReturnType.RETURN) {
            throw new SbcRuntimeException("K-050465");
        }

        //修改退单状态
        ReturnStateRequest request = ReturnStateRequest
                .builder()
                .rid(rid)
                .operator(operator)
                .returnEvent(ReturnEvent.VOID)
                .data(reason)
                .build();
        returnFSMService.changeState(request);


        //退款审核未通过发送MQ消息
        log.info("ReturnOrderService audit2Void 审核成功到作废  tid:{}, pid:{} 原因是：{}", returnOrder.getTid(), returnOrder.getPtid(), returnOrder.getReturnReason());
        if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())
                || CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
            List<String> params;
            String pic;
            if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                params = Lists.newArrayList(returnOrder.getReturnItems().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnItems().get(0).getPic();
            } else {
                params = Lists.newArrayList(returnOrder.getReturnGifts().get(0).getSkuName(), reason);
                pic = returnOrder.getReturnGifts().get(0).getPic();
            }
            this.sendNoticeMessage(NodeType.RETURN_ORDER_PROGRESS_RATE,
                    ReturnOrderProcessType.AFTER_SALE_ORDER_CHECK_PASS,
                    params,
                    returnOrder.getId(),
                    returnOrder.getBuyer().getId(),
                    pic,
                    returnOrder.getBuyer().getAccount());
        }
    }


}
