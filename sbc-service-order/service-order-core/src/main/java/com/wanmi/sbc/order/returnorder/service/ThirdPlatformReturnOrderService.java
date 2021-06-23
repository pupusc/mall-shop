package com.wanmi.sbc.order.returnorder.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdListRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsListResponse;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.*;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.common.enums.node.ReturnOrderProcessType;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailListByCustomerIdsResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailBaseVO;
import com.wanmi.sbc.order.api.constant.RefundReasonConstants;
import com.wanmi.sbc.order.api.request.distribution.ReturnOrderSendMQRequest;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.common.OrderCommonService;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.refund.model.root.RefundOrder;
import com.wanmi.sbc.order.refund.service.RefundOrderService;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPoints;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPrice;
import com.wanmi.sbc.order.returnorder.mq.ReturnOrderProducerService;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Company;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 第三方订单-自动退款业务处理
 *
 * @author: Geek Wang
 * @createDate: 2019/5/21 18:17
 * @version: 1.0
 */
@Slf4j
@Service
public class ThirdPlatformReturnOrderService {

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private ReturnOrderProducerService returnOrderProducerService;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private OrderProducerService orderProducerService;

    @Autowired
    private OrderCommonService orderCommonService;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    /**
     * 根据订单号处理自动退款业务
     *
     * @param businessId
     */
    public void autoOrderRefundByBusinessId(String businessId){
        this.autoOrderRefund(orderCommonService.findTradesByBusinessId(businessId).stream()
                .filter(trade -> ThirdPlatformType.LINKED_MALL.equals(trade.getThirdPlatformType())).collect(Collectors.toList()));
    }

    /**
     * 根据订单集合处理自动退款业务
     *
     * @param tradeList
     */
    public void autoOrderRefund(List<Trade> tradeList) {
        String businessId = tradeList.get(0).getId();
        if (tradeList.size() > 0) {
            businessId = tradeList.get(0).getParentId();
        }
        log.info("===========订单业务id：{}，自动退款开始========", businessId);
        Operator operator =
                Operator.builder().adminId("1").name("system").account("system").ip("127.0.0.1").platform(Platform
                        .PLATFORM).build();
        //1、生成退单数据
        List<ReturnOrder> returnOrders = getReturnOrderByTradeList(tradeList, operator);
        log.info("===========生成退单数据,详细信息：{}========", returnOrders);
        List<ReturnOrder> returnOrderList = this.addReturnOrder(tradeList, returnOrders);

        //新增更改会员资金es
        CustomerFundsByCustomerIdListRequest byCustomerIdRequest = new CustomerFundsByCustomerIdListRequest();
        byCustomerIdRequest.setCustomerIds(tradeList.stream().map(Trade::getBuyer).map(Buyer::getId).collect(Collectors.toList()));
        BaseResponse<CustomerFundsListResponse> response = customerFundsQueryProvider.getByCustomerIdListForEs(byCustomerIdRequest);
        if (Objects.nonNull(response.getContext())) {
            orderProducerService.sendEsCustomerFundsList(response.getContext().getLists());
        }

        //2、处理其他无关业务
        handleOther(tradeList, returnOrderList);
        log.info("===========订单业务id：{}，自动退款结束========", businessId);
    }

    @GlobalTransactional
    @Transactional
    public List<ReturnOrder> addReturnOrder(List<Trade> tradeList, List<ReturnOrder> returnOrders){
        List<ReturnOrder> returnOrderList = returnOrderService.addReturnOrders(returnOrders);

        //1、根据退单数据，获取出会员详情基础信息集合
        List<CustomerDetailBaseVO> customerDetailBaseVOList = listCustomerDetailBaseByCustomerIds(returnOrderList);
        log.info("===========会员详情基础信息集合,详细信息：{}========", customerDetailBaseVOList);

        //2、生成退款单数据集合
        List<RefundOrder> refundOrderList =
                getRefundOrderByReturnOrderListAndCustomerDetailBaseVOList(returnOrderList, customerDetailBaseVOList);

        refundOrderList = refundOrderService.batchAdd(refundOrderList);
        log.info("===========生成退款单数据集合,详细信息：{}========", refundOrderList);

        //3、自动退款-仅处理线上支付
        List<Trade> trades = tradeList.stream()
                .filter(t -> !Integer.valueOf(PayType.OFFLINE.toValue()).equals(NumberUtils.toInt(t.getPayInfo().getPayTypeId()))).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(trades)) {
            Operator operator =
                    Operator.builder().adminId("1").name("system").account("system").ip("127.0.0.1").platform(Platform
                            .PLATFORM).build();
            refundOrderService.autoRefund(trades, returnOrderList, refundOrderList, operator);



        }
        return returnOrderList;
    }


    /**
     * 根据订单数据生成对应退单数据
     *
     * @param tradeList
     * @return
     */
    private List<ReturnOrder> getReturnOrderByTradeList(List<Trade> tradeList, Operator operator) {
        List<ReturnOrder> returnOrderList = new ArrayList<>(tradeList.size());
        List<String> tidList = tradeList.stream().map(Trade::getId).collect(Collectors.toList());
        Map<String, List<ProviderTrade>> providerTradeMap = providerTradeService.findListByParentIdList(tidList).stream().collect(Collectors.groupingBy(ProviderTrade::getParentId));
        for (Trade trade : tradeList) {
            List<ProviderTrade> providerTrades = providerTradeMap.getOrDefault(trade.getId(), Collections.emptyList());
            if(CollectionUtils.isNotEmpty(providerTrades)) {
                for (ProviderTrade providerTrade : providerTrades) {
                    returnOrderList.add(generateReturn(trade, providerTrade, operator));
                }
            }else {
                returnOrderList.add(generateReturn(trade, null, operator));
            }
        }
        return returnOrderList;
    }

    private ReturnOrder generateReturn(Trade trade, ProviderTrade providerTrade, Operator operator){
        List<TradeItem> tradeItems = trade.getTradeItems();
        if(providerTrade != null){
            tradeItems = providerTrade.getTradeItems();
        }

        ReturnOrder returnOrder = new ReturnOrder();
        String rid = generatorService.generate("R");
        //退单号
        returnOrder.setId(rid);
        if(providerTrade!=null) {
            returnOrder.setPtid(providerTrade.getId());
        }
        //订单编号
        returnOrder.setTid(trade.getId());
        //购买人信息
        returnOrder.setBuyer(trade.getBuyer());
        //商家信息
        returnOrder.setCompany(Company.builder().companyInfoId(trade.getSupplier().getSupplierId())
                .companyCode(trade.getSupplier().getSupplierCode()).supplierName(trade.getSupplier().getSupplierName())
                .storeId(trade.getSupplier().getStoreId()).storeName(trade.getSupplier().getStoreName())
                .companyType(trade.getSupplier().getIsSelf() ? BoolFlag.NO : BoolFlag.YES)
                .build());
        //描述信息
        returnOrder.setDescription(RefundReasonConstants.Q_ORDER_SERVICE_THIRD_AUTO_REFUND_USER);
        //退货商品
        returnOrder.setReturnItems(tradeItems.stream().map(item -> ReturnItem.builder()
                .num(item.getNum().intValue())
                .skuId(item.getSkuId())
                .skuNo(item.getSkuNo())
                .pic(item.getPic())
                .skuName(item.getSkuName())
                .unit(item.getUnit())
                .price(item.getPrice())
                .supplyPrice(item.getSupplyPrice())
                .providerPrice(Objects.isNull(item.getSupplyPrice())?BigDecimal.ZERO:item.getSupplyPrice().multiply(BigDecimal.valueOf(item.getNum())))
                .splitPrice(item.getSplitPrice())
                .specDetails(item.getSpecDetails())
                .splitPoint(item.getPoints())
                .build()).collect(Collectors.toList()));
        TradePrice tradePrice = trade.getTradePrice();
        if(providerTrade!=null){
            tradePrice = providerTrade.getTradePrice();
        }
        ReturnPrice returnPrice = new ReturnPrice();

        returnPrice.setApplyPrice(tradePrice.getTotalPrice());
        returnPrice.setProviderTotalPrice(returnOrder.getReturnItems().stream().map(ReturnItem::getProviderPrice).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        //实退金额
        returnPrice.setActualReturnPrice(returnPrice.getApplyPrice());
        //商品总金额
        returnPrice.setTotalPrice(tradePrice.getTotalPrice());
        //申请金额可用
        returnPrice.setApplyStatus(Boolean.TRUE);
        //退货总金额
        returnOrder.setReturnPrice(returnPrice);
        //收货人信息
        returnOrder.setConsignee(trade.getConsignee());
        //退货单状态
        returnOrder.setReturnFlowState(ReturnFlowState.AUDIT);
        //记录日志
        returnOrder.appendReturnEventLog(
                new ReturnEventLog(operator, "订单自动退单", "订单自动退单", "订单自动退款", LocalDateTime.now())
        );

        // 支付方式
        returnOrder.setPayType(PayType.valueOf(trade.getPayInfo().getPayTypeName()));
        //退单类型
        returnOrder.setReturnType(ReturnType.REFUND);
        //退单来源
        returnOrder.setPlatform(Platform.CUSTOMER);
        //退积分信息
        returnOrder.setReturnPoints(ReturnPoints.builder()
                .applyPoints(Objects.isNull(tradePrice.getPoints()) ? NumberUtils.LONG_ZERO :
                        tradePrice.getPoints())
                .actualPoints(Objects.isNull(tradePrice.getPoints()) ? NumberUtils.LONG_ZERO :
                        tradePrice.getPoints()).build());
        //创建时间
        returnOrder.setCreateTime(LocalDateTime.now());
        return returnOrder;
    }

    /**
     * 订单日志、发送MQ消息
     *
     * @param tradeList
     * @param returnOrderList
     */
    private void handleOther(List<Trade> tradeList, List<ReturnOrder> returnOrderList) {
        Map<String, List<ReturnOrder>> map = returnOrderList.stream().collect(Collectors.groupingBy(ReturnOrder::getTid));
        for (Trade trade : tradeList) {
            map.getOrDefault(trade.getId(), Collections.emptyList()).forEach(returnOrder -> {
                ReturnOrderSendMQRequest sendMQRequest = ReturnOrderSendMQRequest.builder()
                        .addFlag(Boolean.TRUE)
                        .customerId(trade.getBuyer().getId())
                        .orderId(trade.getId())
                        .returnId(returnOrder.getId())
                        .build();
                returnOrderProducerService.returnOrderFlow(sendMQRequest);
            });

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("type", NodeType.RETURN_ORDER_PROGRESS_RATE.toValue());
            paramMap.put("node", ReturnOrderProcessType.REFUND_CHECK_PASS.toValue());
            paramMap.put("id", map.get(trade.getId()).get(0).getId());

            MessageMQRequest messageMQRequest = new MessageMQRequest();
            messageMQRequest.setNodeCode(OrderProcessType.THIRD_PAY_ERROR_AUTO_REFUND.getType());
            messageMQRequest.setNodeType(NodeType.RETURN_ORDER_PROGRESS_RATE.toValue());
            messageMQRequest.setParams(Lists.newArrayList(trade.getTradeItems().get(0).getSkuName()));
            messageMQRequest.setRouteParam(paramMap);
            messageMQRequest.setCustomerId(trade.getBuyer().getId());
            messageMQRequest.setPic(trade.getTradeItems().get(0).getPic());
            messageMQRequest.setMobile(trade.getBuyer().getAccount());
            orderProducerService.sendMessage(messageMQRequest);
        }

    }

    /**
     * 根据退单数据、会员详情信息集合，生成对应的退款单数据集合
     *
     * @param returnOrderList
     * @param customerDetailBaseVOList
     * @return
     */
    private List<RefundOrder> getRefundOrderByReturnOrderListAndCustomerDetailBaseVOList(List<ReturnOrder> returnOrderList, List<CustomerDetailBaseVO> customerDetailBaseVOList) {
        List<RefundOrder> refundOrderList = new ArrayList<>(returnOrderList.size());
        Map<String, String> map =
                customerDetailBaseVOList.stream().collect(Collectors.toMap(CustomerDetailBaseVO::getCustomerId,
                        CustomerDetailBaseVO::getCustomerDetailId));
        for (ReturnOrder returnOrder : returnOrderList) {
            RefundOrder refundOrder = new RefundOrder();
            refundOrder.setReturnOrderCode(returnOrder.getId());
            refundOrder.setCustomerDetailId(map.get(returnOrder.getBuyer().getId()));
            refundOrder.setCreateTime(LocalDateTime.now());
            refundOrder.setRefundCode(generatorService.generateRid());
            refundOrder.setRefundStatus(RefundStatus.TODO);
            refundOrder.setReturnPrice(returnOrder.getReturnPrice().getApplyPrice());
            refundOrder.setReturnPoints(Objects.nonNull(returnOrder.getReturnPoints()) ?
                    returnOrder.getReturnPoints().getApplyPoints() : null);
            refundOrder.setDelFlag(DeleteFlag.NO);
            refundOrder.setPayType(returnOrder.getPayType());
            refundOrder.setSupplierId(returnOrder.getCompany().getCompanyInfoId());
            refundOrderList.add(refundOrder);
        }
        return refundOrderList;
    }

    /**
     * 根据会员ID集合查询会员详情基础信息集合
     *
     * @param returnOrderList
     * @return
     */
    private List<CustomerDetailBaseVO> listCustomerDetailBaseByCustomerIds(List<ReturnOrder> returnOrderList) {
        List<String> customerIds =
                returnOrderList.stream().map(returnOrder -> returnOrder.getBuyer().getId()).collect(Collectors.toList());
        BaseResponse<CustomerDetailListByCustomerIdsResponse> baseResponse =
                customerDetailQueryProvider.listCustomerDetailBaseByCustomerIds(new CustomerDetailListByCustomerIdsRequest(customerIds));
        return baseResponse.getContext().getList();
    }
}
