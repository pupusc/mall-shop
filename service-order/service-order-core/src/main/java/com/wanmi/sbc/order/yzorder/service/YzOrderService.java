package com.wanmi.sbc.order.yzorder.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerYzUidListRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderPageQueryRequest;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderRecordQueryRequest;
import com.wanmi.sbc.order.bean.dto.CycleBuySendDateRuleDTO;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.*;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.ProviderTradeRepository;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.order.trade.request.ProviderTradeQueryRequest;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.service.CycleBuyDeliverTimeService;
import com.wanmi.sbc.order.yzorder.model.root.*;
import com.wanmi.sbc.order.yzorder.model.root.deliver.ExpressInfo;
import com.wanmi.sbc.order.yzorder.model.root.deliver.YzOrderDeliver;
import com.wanmi.sbc.order.yzorder.repository.YzOrderRepository;
import com.wanmi.sbc.order.yzorder.request.YzOrderQueryRequest;

import com.wanmi.sbc.order.yzsalesmancustomer.model.root.YzOrderCustomer;
import com.wanmi.sbc.order.yzsalesmancustomer.service.YzOrderCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class YzOrderService {

    @Autowired
    private YzOrderRepository yzOrderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private CycleBuyDeliverTimeService cycleBuyDeliverTimeService;

    @Autowired
    private ProviderTradeRepository providerTradeRepository;

    @Autowired
    private GeneratorService generatorService;

    private Long weixing[] = {1L, 19L, 20L, 29L, 72L, 102L, 110L};

    private Long aliPay[] = {2L, 3L, 30L, 103L, 111L, 118L};

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private YzOrderCustomerService yzOrderCustomerService;

    private static final String _YZORDER = "E";

    @Autowired
    private YzOrderRecordService yzOrderRecordService;

    /**
     * 批量保存
     * @param yzOrders
     */
    @Transactional
    public void addAll(List<YzOrder> yzOrders, Boolean isUpdate) {
        List<YzOrder> newOrders = new ArrayList<>();
        if(isUpdate) {
            newOrders = yzOrders;
        } else {
            List<String> ids = yzOrders.stream().map(yzOrder -> yzOrder.getId().toLowerCase()).collect(Collectors.toList());
            YzOrderQueryRequest request = new YzOrderQueryRequest();
            request.setIds(ids);
            request.setPageSize(ids.size());

            Map fieldsObject = new HashMap();
            fieldsObject.put("_id", true);
            Query query = new BasicQuery(new Document(), new Document(fieldsObject));
            query.addCriteria(request.getCriteria());
            List<YzOrder> olds = mongoTemplate.find(query.with(request.getPageRequest()), YzOrder.class);
            List<String> oldIds = olds.stream().map(old -> old.getId().toUpperCase()).collect(Collectors.toList());

            newOrders = yzOrders.stream().filter(yzOrder -> !oldIds.contains(yzOrder.getId())).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(newOrders)) {
            yzOrderRepository.saveAll(newOrders);
        }

        log.info("实际订单入库数据量：{}", newOrders.size());
    }

    /**
     * 订单分页
     *
     * @param request       参数
     * @return
     */
    public Page<YzOrder> page(YzOrderQueryRequest request) {
        Criteria whereCriteria = request.getCriteria();
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), YzOrder.class), request
                .getPageable(), totalSize);
    }

    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, YzOrderQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, YzOrder.class);
        return totalSize;
    }

    /**
     * 订单入库
     */
    public void convert(YzOrderQueryRequest queryRequest, Boolean compenstateFalg){
        long lastCount = 0L;
        while(true) {
            try {
                log.info("-----订单入库第{}页-------", queryRequest.getPageNum());
                Page<YzOrder> page = this.yzOrderToTrade(queryRequest, compenstateFalg);
                long total = new MicroServicePage<>(page, page.getPageable()).getTotal();
                if(page.getContent().size() == 0) {
                    break;
                }
                log.info("当前数据total:{},lastCount:{}", total, lastCount);
                if(total == lastCount) {
                    queryRequest.setPageNum(queryRequest.getPageNum() + 1);
                }
                lastCount = total;
            } catch (Exception e) {
                e.printStackTrace();
                queryRequest.setPageNum(queryRequest.getPageNum() + 1);
            }
        }
    }

    /**
     * 有赞订单临时表转换业务表
     */
    public Page<YzOrder> yzOrderToTrade(YzOrderQueryRequest request, Boolean compenstateFalg){

        long start = System.currentTimeMillis();
        //分页查询有赞订单
        Page<YzOrder> page = page(request);
        List<YzOrder> yzOrders = page.getContent();

        log.info("------订单数据量：{}--------", yzOrders.size());

        if(CollectionUtils.isEmpty(yzOrders)) {
            return page;
        }
        List<Trade> tradeList = new ArrayList<>();
        List<ProviderTrade> providerTradeList = new ArrayList<>();

        //店铺信息
        List<StoreVO> providerStore = storeQueryProvider.listNoDeleteStoreByIds(ListNoDeleteStoreByIdsRequest.builder()
                .storeIds(Lists.newArrayList(123458038L))
                .build()).getContext().getStoreVOList();
        List<StoreVO> storeVOList = storeQueryProvider.listNoDeleteStoreByIds(ListNoDeleteStoreByIdsRequest.builder()
                .storeIds(Lists.newArrayList(123458039L))
                .build()).getContext().getStoreVOList();

        //查询会员信息
//        List<Long> buyerIds = yzOrders.stream().map(yzOrder -> yzOrder.getFull_order_info().getBuyer_info().getBuyer_id()).collect(Collectors.toList());
//        CustomerYzUidListRequest idRequest = new CustomerYzUidListRequest();
//        idRequest.setYzUids(buyerIds);
//        List<CustomerVO> customerVOList = customerQueryProvider.getCustomerByYzUserId(idRequest).getContext().getCustomerVOList();
//        log.info("-----会员数量：{}-----",customerVOList.size());
//        if(CollectionUtils.isEmpty(customerVOList)) {
//            return page;
//        }

        //组装数据
        yzOrders.forEach(yzOrder -> {

            Trade oldTrade = tradeRepository.findByYzTid(yzOrder.getId().toUpperCase()).get();
            Trade trade = new Trade();
            trade.setId(Objects.isNull(oldTrade) ? generatorService.generateTid() : oldTrade.getId());
            trade.setParentId(Objects.isNull(oldTrade) ? generatorService.generatePoId() : oldTrade.getParentId());
            trade.setYzTid(yzOrder.getId().toUpperCase());
            trade.setYzOrderFlag(Boolean.TRUE);

            //买家信息
            Buyer buyer = oldTrade.getBuyer();
            trade.setBuyer(buyer);
            if(CollectionUtils.isNotEmpty(storeVOList)) {
                StoreVO storeVO = storeVOList.get(0);
                Supplier supplier = new Supplier();
                supplier.setStoreId(storeVO.getStoreId());
                supplier.setStoreName(storeVO.getStoreName());
                supplier.setIsSelf(Boolean.TRUE);
                supplier.setSupplierName(storeVO.getSupplierName());
                supplier.setSupplierCode(storeVO.getCompanyInfo().getCompanyCode());
                supplier.setSupplierId(storeVO.getCompanyInfo().getCompanyInfoId());
                supplier.setFreightTemplateType(storeVO.getFreightTemplateType());
                trade.setSupplier(supplier);
            }

            if(Objects.nonNull(yzOrder.getFull_order_info().getRemark_info())) {
                trade.setBuyerRemark(yzOrder.getFull_order_info().getRemark_info().getBuyer_message());
                trade.setSellerRemark(yzOrder.getFull_order_info().getRemark_info().getTrade_memo());
            }

            //发票
            InvoiceInfo invoiceInfo = yzOrder.getFull_order_info().getInvoice_info();
            if(invoiceInfo != null) {
                Invoice invoice = new Invoice();
                GeneralInvoice generalInvoice = new GeneralInvoice();
                generalInvoice.setFlag(StringUtils.isNotBlank(invoiceInfo.getRaise_type()) && "personal".equals(invoiceInfo.getRaise_type())
                        ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE);
                generalInvoice.setTitle(invoiceInfo.getUser_name());
                generalInvoice.setIdentification(invoiceInfo.getTaxpayer_id());
                invoice.setGeneralInvoice(generalInvoice);
                invoice.setType(NumberUtils.INTEGER_ZERO);
                invoice.setSperator(Boolean.FALSE);
                invoice.setTaxNo(invoiceInfo.getTaxpayer_id());
                trade.setInvoice(invoice);
            }

            //订单状态
            List<YzOrderDeliver> delivery_order_detail = yzOrder.getDelivery_order_detail();
            OrderInfo orderInfo = yzOrder.getFull_order_info().getOrder_info();
            PayInfo payInfo = yzOrder.getFull_order_info().getPay_info();
            TradeState tradeState = new TradeState();
            tradeState.setAuditState(AuditState.CHECKED);
            switch (orderInfo.getStatus()) {
                case "TRADE_SUCCESS" :
                    tradeState.setFlowState(FlowState.COMPLETED);
                    tradeState.setDeliverStatus(DeliverStatus.SHIPPED);
                    break;
                case "TRADE_CLOSED" :
                    tradeState.setFlowState(FlowState.VOID);
                    tradeState.setDeliverStatus(orderInfo.getConsign_time() != null ? DeliverStatus.SHIPPED :
                            CollectionUtils.isNotEmpty(yzOrder.getDelivery_order()) ? DeliverStatus.PART_SHIPPED : DeliverStatus.NOT_YET_SHIPPED);
                    break;
                case "WAIT_SELLER_SEND_GOODS" :
                    tradeState.setFlowState(FlowState.AUDIT);
                    tradeState.setDeliverStatus(CollectionUtils.isNotEmpty(yzOrder.getDelivery_order()) ? DeliverStatus.PART_SHIPPED : DeliverStatus.NOT_YET_SHIPPED);
                    break;
                case "WAIT_BUYER_CONFIRM_GOODS" :
                    tradeState.setFlowState(FlowState.DELIVERED);
                    tradeState.setDeliverStatus(DeliverStatus.SHIPPED);
                    break;
            }
            if(Objects.nonNull(orderInfo.getOrder_tags()) && Objects.nonNull(orderInfo.getOrder_tags().getIs_payed())) {
                tradeState.setPayState(Boolean.TRUE.equals(orderInfo.getOrder_tags().getIs_payed())  ? PayState.PAID : PayState.NOT_PAID);
            } else {
                tradeState.setPayState(Objects.isNull(orderInfo.getPay_time()) ? PayState.NOT_PAID : PayState.PAID);

            }

            tradeState.setDeliverTime(orderInfo.getConsign_time());
            tradeState.setCreateTime(orderInfo.getCreated());
            tradeState.setModifyTime(orderInfo.getUpdate_time());
            tradeState.setPayTime(orderInfo.getPay_time());
            tradeState.setEndTime(orderInfo.getSuccess_time());
            trade.setTradeState(tradeState);

            //收货人信息
            AddressInfo addressInfo = yzOrder.getFull_order_info().getAddress_info();
            Consignee consignee = null;
            if(Objects.nonNull(addressInfo)) {
                consignee = new Consignee();
                String detailAddress = addressInfo.getDelivery_province()
                        .concat(addressInfo.getDelivery_city())
                        .concat(addressInfo.getDelivery_district())
                        .concat(addressInfo.getDelivery_address());
                consignee.setProvinceName(addressInfo.getDelivery_province());
                consignee.setCityName(addressInfo.getDelivery_city());
                consignee.setAreaName(addressInfo.getDelivery_district());
                consignee.setAddress(addressInfo.getDelivery_address());
                consignee.setDetailAddress(detailAddress);
                consignee.setName(addressInfo.getReceiver_name());
                consignee.setPhone(addressInfo.getReceiver_tel());
                trade.setConsignee(consignee);
            }

            //订单金额
            TradePrice tradePrice = new TradePrice();
            OrderPromotion orderPromotion = yzOrder.getOrder_promotion();

            List<Order> orders = yzOrder.getFull_order_info().getOrders();
            //积分
            Long points = NumberUtils.LONG_ZERO;
            for (Order order : orders) {
                if(StringUtils.isNotBlank(order.getPoints_price())) {
                    points = points + Long.parseLong(order.getPoints_price());
                }
            }
            tradePrice.setGoodsPrice(Objects.isNull(payInfo)? BigDecimal.ZERO : new BigDecimal(payInfo.getTotal_fee()));
            tradePrice.setOriginPrice(Objects.isNull(payInfo)? BigDecimal.ZERO : new BigDecimal(payInfo.getTotal_fee()));
            tradePrice.setSpecial(Boolean.FALSE);
            tradePrice.setDeliveryPrice(Objects.isNull(payInfo)? BigDecimal.ZERO : new BigDecimal(payInfo.getPost_fee()));
            tradePrice.setEnableDeliveryPrice(tradePrice.getDeliveryPrice() != null);
            tradePrice.setTotalPrice(Objects.isNull(payInfo)? BigDecimal.ZERO : new BigDecimal(payInfo.getPayment()));
            tradePrice.setTotalPayCash(Objects.isNull(payInfo)? BigDecimal.ZERO : new BigDecimal(payInfo.getReal_payment()));

            //优惠金额
            BigDecimal orderDiscount = BigDecimal.ZERO;
            if(orderPromotion != null && StringUtils.isNotBlank(orderPromotion.getOrder_discount_fee())) {
                orderDiscount = orderDiscount.add(new BigDecimal(orderPromotion.getOrder_discount_fee()));
            }
            if(orderPromotion != null &&  StringUtils.isNotBlank(orderPromotion.getItem_discount_fee())) {
                orderDiscount = orderDiscount.add(new BigDecimal(orderPromotion.getItem_discount_fee()));
            }
            tradePrice.setMarketingDiscountPrice(orderDiscount);
            tradePrice.setDiscountsPrice(orderDiscount);
            trade.setTradePrice(tradePrice);

            //订单商品
            List<TradeItem> items = new ArrayList<>();
            List<TradeItem> gifts = new ArrayList<>();

            orders.forEach(order -> {
                TradeItem tradeItem = new TradeItem();
                tradeItem.setOid(order.getOid());
                tradeItem.setSpuId(order.getItem_id().toString());
                tradeItem.setSkuId(order.getOuter_sku_id());
                tradeItem.setSkuName(order.getTitle());
                tradeItem.setSkuNo(order.getSku_unique_code());
                tradeItem.setGoodsSource(GoodsSource.SELLER.toValue());
                if(StringUtils.isNotBlank(order.getWeight())) {
                    tradeItem.setGoodsWeight(new BigDecimal(order.getWeight()));
                }
                tradeItem.setPic(order.getPic_path());
                tradeItem.setNum(order.getNum());

                tradeItem.setDeliverStatus(tradeState.getDeliverStatus());
                tradeItem.setPrice(Objects.isNull(order.getPrice()) ? BigDecimal.ZERO : new BigDecimal(order.getPrice()));
                tradeItem.setLevelPrice(Objects.isNull(order.getPrice()) ? BigDecimal.ZERO : new BigDecimal(order.getPrice()));
                tradeItem.setOriginalPrice(Objects.isNull(order.getPrice()) ? BigDecimal.ZERO : new BigDecimal(order.getPrice()));
                tradeItem.setSplitPrice(Objects.isNull(order.getPrice()) ? BigDecimal.ZERO : new BigDecimal(order.getPrice()).divide(BigDecimal.valueOf(order.getNum()), 2, BigDecimal.ROUND_HALF_UP));
                tradeItem.setCanReturnNum(NumberUtils.INTEGER_ZERO);

                //周期购类型
                if(order.getItem_type() == 24) {
                    trade.setCycleBuyFlag(Boolean.TRUE);
                    tradeItem.setGoodsType(GoodsType.CYCLE_BUY);
                    if(Objects.nonNull(yzOrder.getFull_order_info().getMultiPeriodDetail())) {
                        tradeItem.setCycleNum(yzOrder.getFull_order_info().getMultiPeriodDetail().getTotal_issue());
                    }

                }

                if(Boolean.FALSE.equals(order.getIs_present())) {
                    items.add(tradeItem);
                } else {
                    gifts.add(tradeItem);
                }
            });
            //计算商品分摊价
            caleSplitPrice(orders, items);
            trade.setTradeItems(items);
            trade.setGifts(gifts);

            //发货单
            List<TradeDeliver> tradeDelivers = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(delivery_order_detail)) {
                Consignee finalConsignee = consignee;
                delivery_order_detail.forEach(detail -> {
                    if(detail.getStatus() == 1) {
                        TradeDeliver tradeDeliver = new TradeDeliver();

                        ExpressInfo expressInfo = detail.getDist_order_d_t_os().get(0).getExpress_info();

                        tradeDeliver.setTradeId(trade.getId());

                        Logistics logistics = new Logistics();
                        if(Objects.nonNull(expressInfo) && Objects.nonNull(expressInfo.getExpress_detail())) {
                            logistics.setLogisticCompanyName(expressInfo.getExpress_detail().getExpress_company_name());
                            logistics.setLogisticStandardCode(expressInfo.getExpress_detail().getCom());
                            logistics.setLogisticNo(expressInfo.getExpress_no());
                            tradeDeliver.setExpressProgressInfo(expressInfo.getExpress_detail().getExpress_progress_info());
                        }
                        logistics.setBuyerId(buyer.getId());
                        logistics.setLogisticFee(new BigDecimal(detail.getReal_delivery_fee()));
                        tradeDeliver.setLogistics(logistics);
                        tradeDeliver.setConsignee(finalConsignee);
                        tradeDeliver.setIsVirtualCoupon(Boolean.FALSE);


                        List<ShippingItem> shippingItems = detail.getDelivery_order_items().stream().map(item -> {
                            ShippingItem shippingItem = new ShippingItem();
                            shippingItem.setItemNum(Long.parseLong(item.getNum().toString()));
                            return shippingItem;
                        }).collect(Collectors.toList());

                        tradeDeliver.setShippingItems(shippingItems);
                        tradeDeliver.setDeliverTime(detail.getCreate_time());
                        tradeDeliver.setStatus(detail.getStatus() == 0 ? DeliverStatus.NOT_YET_SHIPPED : DeliverStatus.SHIPPED);
                        tradeDeliver.setShipperType(ShipperType.SUPPLIER);
                        tradeDelivers.add(tradeDeliver);
                    }
                });
                trade.setTradeDelivers(tradeDelivers);
            }

            //支付信息

            trade.setPayInfo(com.wanmi.sbc.order.trade.model.entity.PayInfo.builder()
                    .payTypeId(String.format("%d", PayType.ONLINE.toValue()))
                    .payTypeName(PayType.ONLINE.name())
                    .desc(PayType.ONLINE.getDesc())
                    .build());


            //配送方式
            trade.setDeliverWay(DeliverWay.EXPRESS);
            trade.setPlatform(Platform.CUSTOMER);
            trade.setPaymentOrder(PaymentOrder.PAY_FIRST);
            trade.setCanReturnFlag(Boolean.FALSE);
            trade.setOrderSource(OrderSource.WECHAT);
            trade.setOrderType(OrderType.NORMAL_ORDER);

            //支付方式
            if(Lists.newArrayList(weixing).contains(orderInfo.getPay_type())) {
                trade.setPayWay(PayWay.WECHAT);
            } else if(Lists.newArrayList(aliPay).contains(orderInfo.getPay_type())){
                trade.setPayWay(PayWay.ALIPAY);
            } else {
                trade.setPayWay(PayWay.OTHER);
            }


            //周期购
            if(trade.getCycleBuyFlag() && Objects.nonNull(yzOrder.getFull_order_info().getMultiPeriodDetail())) {
                //周期购已发货数量

                MultiPeriodDetail multiPeriodDetail = yzOrder.getFull_order_info().getMultiPeriodDetail();
                MultiPeriodLatestPlan multiPeriodLatestPlan = yzOrder.getFull_order_info().getMultiPeriodLatestPlan();
                TradeCycleBuyInfo tradeCycleBuyInfo = new TradeCycleBuyInfo();
                tradeCycleBuyInfo.setDeliveryCycle(DeliveryCycle.fromValue(multiPeriodDetail.getDist_time_dim()));
                tradeCycleBuyInfo.setCycleNum(multiPeriodDetail.getTotal_issue());
                tradeCycleBuyInfo.setCycleBuySendDateRule(getCycleBuySendDateRuleVOList(tradeCycleBuyInfo.getDeliveryCycle(), multiPeriodDetail.getDist_time_mode().toString()));
                tradeCycleBuyInfo.setDeliveryPlan(DeliveryPlan.BUSINESS);

                //发货日历
                List<DeliverCalendar> calendarList = new ArrayList<>();
                //用户下单时选择的发货时间
                LocalDate date = multiPeriodLatestPlan.getMultiPeriodDetail().getDelivery_time().toLocalDate();
                //已发次数
                long deliverCount = yzOrder.getDelivery_order().stream().filter(deliveryOrder -> deliveryOrder.getExpress_state() == 1).count();
                for (int i = 0; i < tradeCycleBuyInfo.getCycleNum(); i++) {
                    LocalDate deliverDate = cycleBuyDeliverTimeService.getLatestDeliverTime(date, tradeCycleBuyInfo.getDeliveryCycle(), tradeCycleBuyInfo.getCycleBuySendDateRule().getSendDateRule());
                    DeliverCalendar deliverCalendar = new DeliverCalendar();
                    if(i < deliverCount) {
                        deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.SHIPPED);
                    } else {
                        deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);
                    }
                    deliverCalendar.setDeliverDate(deliverDate);
                    calendarList.add(deliverCalendar);
                    date = deliverDate;
                }
                tradeCycleBuyInfo.setDeliverCalendar(calendarList);
                trade.setTradeCycleBuyInfo(tradeCycleBuyInfo);

                //周期购已发货数量
                trade.getTradeItems().forEach(item -> {
                    item.setDeliveredNum(deliverCount * item.getNum());
                });

            }
            tradeList.add(trade);

            ProviderTrade providerTrade = KsBeanUtil.convert(trade, ProviderTrade.class);
            ProviderTrade oldProviderTrade = providerTradeRepository.findByYzTid(yzOrder.getId().toUpperCase());
            providerTrade.setParentId(trade.getId());
            providerTrade.setId(Objects.isNull(oldProviderTrade) ? generatorService.generateProviderTid() : oldProviderTrade.getId());


            if(CollectionUtils.isNotEmpty(providerStore)) {
                StoreVO provider = providerStore.get(0);
                Supplier supplier = new Supplier();
                supplier.setStoreId(provider.getStoreId());
                supplier.setStoreName(provider.getStoreName());
                supplier.setIsSelf(Boolean.FALSE);
                supplier.setSupplierName(provider.getSupplierName());
                supplier.setSupplierCode(provider.getCompanyInfo().getCompanyCode());
                supplier.setSupplierId(provider.getCompanyInfo().getCompanyInfoId());
                supplier.setFreightTemplateType(provider.getFreightTemplateType());
                providerTrade.setSupplier(supplier);
            }
            providerTradeList.add(providerTrade);
        });
        //批量保存
        addTrades(tradeList, providerTradeList);

        if(Boolean.TRUE.equals(compenstateFalg)) {
            List<String> tids = tradeList.stream().map(Trade::getYzTid).collect(Collectors.toList());
            yzOrderRecordService.updateFlag(Boolean.TRUE, tids);
        }

        long end = System.currentTimeMillis();
        log.info("当前查询条件：{}，订单状态{},当前条件已处理页数{},共{}条,当前页耗费时间:{}",
                JSON.toJSONString(request),
                request.getStatus(),
                request.getPageNum(),
                page.getTotalElements(),
                (end-start));
        return page;
    }

    /**
     * 批量保存订单
     * @param trades
     */
    @Transactional
    public void addTrades(List<Trade> trades, List<ProviderTrade> providerTrades) {
        List<String> yzTids = trades.stream().map(Trade::getYzTid).collect(Collectors.toList());

        //providerTradeRepository.deleteAllByYzTidIn(yzTids);
        //tradeRepository.deleteAllByYzTidIn(yzTids);
        tradeRepository.saveAll(trades);
        providerTradeRepository.saveAll(providerTrades);

        Update update = new Update();
        update.set("convertFlag", true);
        Query query = new Query(Criteria.where("_id").in(yzTids));
        mongoTemplate.updateMulti(query, update, YzOrder.class);

        log.info("实际入库量Trade：{},Provider：{}", trades.size(), providerTrades.size());

    }

    /**
     * 组装发货日期规则列表
     * @param deliveryCycle
     *
     * @return
     */
    public CycleBuySendDateRuleDTO getCycleBuySendDateRuleVOList(DeliveryCycle deliveryCycle, String rule){

        //配送周期和发货日期都不为空
        if(Objects.nonNull(deliveryCycle) && StringUtils.isNotBlank(rule)) {
            CycleBuySendDateRuleDTO ruleDTO = new CycleBuySendDateRuleDTO();
            switch (deliveryCycle) {
                case EVERYDAY:
                    DailyIssueType type = DailyIssueType.fromValue(Integer.parseInt(rule));
                    ruleDTO.setSendDateRule(rule);
                    ruleDTO.setRuleDescription(type.getDescription());
                    return ruleDTO;
                case WEEKLY:
                    DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(rule));
                    ruleDTO.setSendDateRule(rule);
                    ruleDTO.setRuleDescription("每周" + dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.CHINESE));
                    return ruleDTO;
                case MONTHLY:
                    ruleDTO.setSendDateRule(rule);
                    ruleDTO.setRuleDescription("每月-号".replace("-", rule));
                    return ruleDTO;
                default:
            }
        }
        return null;
    }

    /**
     * 计算商品分摊价
     */
    public void caleSplitPrice(List<Order> orders, List<TradeItem> tradeItems){
        //原价
        BigDecimal total = BigDecimal.ZERO;
        //去除优惠后的总价
        BigDecimal newTotal = BigDecimal.ZERO;
        orders.forEach(order -> {
            total.add(Objects.isNull(order.getPrice()) ? BigDecimal.ZERO : new BigDecimal(order.getPrice()));
            newTotal.add(Objects.isNull(order.getTotal_fee()) ? BigDecimal.ZERO : new BigDecimal(order.getTotal_fee()));
        });

        //内部总价为零或相等不用修改
        if(total.equals(newTotal)) {
            return;
        }

        int size = tradeItems.size();
        BigDecimal splitPriceTotal = BigDecimal.ZERO;//累积平摊价，将剩余扣给最后一个元素
        Long totalNum = tradeItems.stream().map(tradeItem -> tradeItem.getNum()).reduce(0L, Long::sum);

        for (int i = 0; i < size; i++) {
            TradeItem tradeItem = tradeItems.get(i);
            if (i == size - 1) {
                tradeItem.setSplitPrice(newTotal.subtract(splitPriceTotal));
            } else {
                BigDecimal splitPrice = tradeItem.getSplitPrice() != null ? tradeItem.getSplitPrice() : BigDecimal.ZERO;
                //全是零元商品按数量均摊
                if (BigDecimal.ZERO.equals(total)) {
                    tradeItem.setSplitPrice(
                            newTotal.multiply(BigDecimal.valueOf(tradeItem.getNum()))
                                    .divide(BigDecimal.valueOf(totalNum), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    tradeItem.setSplitPrice(
                            splitPrice
                                    .divide(total, 10, BigDecimal.ROUND_DOWN)
                                    .multiply(newTotal)
                                    .setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                splitPriceTotal = splitPriceTotal.add(tradeItem.getSplitPrice());
            }
        }
    }


    public void compare(YzOrderQueryRequest queryRequest){
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(10000);
        queryRequest.putSort("_id", "desc");
        Set<Long> errorIds = new HashSet<>();


        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);
        fieldsObject.put("full_order_info.buyer_info.buyer_id",true);
        Map<Long, String> map = new HashMap<>();
        while(true) {
            long totalSize = this.countNum(queryRequest.getCriteria(), queryRequest);
            if (totalSize < 1) {
                break;
            }

            Query query = new BasicQuery(new Document(), new Document(fieldsObject));
            query.addCriteria(queryRequest.getCriteria());
            List<YzOrder> yzOrders = mongoTemplate.find(query.with(queryRequest.getPageRequest()), YzOrder.class);

            if(CollectionUtils.isEmpty(yzOrders)) {
                break;
            }

            List<Long> yzUids = yzOrders.stream().map(yzOrder -> yzOrder.getFull_order_info().getBuyer_info().getBuyer_id()).collect(Collectors.toList());
            CustomerYzUidListRequest request = new CustomerYzUidListRequest();
            request.setYzUids(yzUids);
            List<Long> notExistIds = customerQueryProvider.findYzUidNotExist(request).getContext();
            if(CollectionUtils.isNotEmpty(notExistIds)) {
                errorIds.addAll(notExistIds);
                yzOrders.forEach(yzOrder -> {
                    Long buyerId = yzOrder.getFull_order_info().getBuyer_info().getBuyer_id();
                    Optional<Long> first = notExistIds.stream().filter(id -> id.equals(buyerId)).findFirst();
                    if(first.isPresent()) {
                        map.put(first.get(), yzOrder.getId().toUpperCase());
                    }
                });
            }
            log.info("当前页数：{},总数据量：{}", queryRequest.getPageNum(), totalSize);
            queryRequest.setPageNum(queryRequest.getPageNum() + 1);
        }

        List<YzOrderCustomer> yzOrderCustomers = new ArrayList<>();
        map.forEach((k, v) -> {
            YzOrderCustomer yzOrderCustomer = new YzOrderCustomer();
            yzOrderCustomer.setYzUid(k);
            yzOrderCustomer.setOrderNo(v);
            yzOrderCustomer.setFlag(DefaultFlag.NO.toValue());
            yzOrderCustomers.add(yzOrderCustomer);
        });
        yzOrderCustomerService.addAll(yzOrderCustomers);

        log.info("记录缺失数据量：{}", map.size());
        log.info("缺失会员数量：{},详细yz_uid：{}", errorIds.size(), errorIds);

    }

    /***
     * 查询有赞订单id
     * @return
     */
    public List<String> getYzOrderIds(YzOrderQueryRequest queryRequest){
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(10000);
        queryRequest.setStatus(queryRequest.getStatus());
        Set<String> yzOrderIds = new HashSet<>();


        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);

        while(true) {
            Query query = new BasicQuery(new Document(), new Document(fieldsObject));
            query.addCriteria(queryRequest.getCriteria());
            List<YzOrder> yzOrders = mongoTemplate.find(query.with(queryRequest.getPageRequest()), YzOrder.class);

            if(CollectionUtils.isEmpty(yzOrders)) {
                break;
            }

            List<String> ids = yzOrders.stream().map(yzOrder -> yzOrder.getId().toUpperCase()).collect(Collectors.toList());
            yzOrderIds.addAll(ids);

            queryRequest.setPageNum(queryRequest.getPageNum() + 1);
        }

        return new ArrayList<>(yzOrderIds);
    }

    /**
     * 补偿
     */
    public void convert2(YzOrderPageQueryRequest yzOrderPageQueryRequest){
        YzOrderQueryRequest queryRequest = new YzOrderQueryRequest();
        queryRequest.setPageNum(yzOrderPageQueryRequest.getPageNum());
        queryRequest.setPageSize(10000);
        queryRequest.setConvertFlag(Boolean.TRUE);

        Set<String> notExistIds = new HashSet<>();

        Map fields = new HashMap();
        fields.put("_id",true);
        fields.put("yzTid",true);

        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);
        Query query = new BasicQuery(new Document(), new Document(fieldsObject));
        Query query2 = new BasicQuery(new Document(), new Document(fields));
        Query query3 = new BasicQuery(new Document(), new Document(fields));

        while(true) {
            query.addCriteria(queryRequest.getCriteria());
            List<YzOrder> yzOrders = mongoTemplate.find(query.with(queryRequest.getPageRequest()), YzOrder.class);

            if(CollectionUtils.isEmpty(yzOrders)) {
                break;
            }

            List<String> ids = yzOrders.stream().map(yzOrder -> yzOrder.getId().toUpperCase()).collect(Collectors.toList());

            TradeQueryRequest request = new TradeQueryRequest();
            request.setYzTids(ids);
            request.setPageNum(0);
            request.setPageSize(ids.size());

            query2.addCriteria(request.getWhereCriteria());
            List<Trade> tradeList = mongoTemplate.find(query2.with(request.getPageRequest()), Trade.class);

            ProviderTradeQueryRequest providerTradeQueryRequest = new ProviderTradeQueryRequest();
            providerTradeQueryRequest.setYzTids(ids);
            providerTradeQueryRequest.setPageNum(0);
            providerTradeQueryRequest.setPageSize(ids.size());
            query3.addCriteria(providerTradeQueryRequest.getWhereCriteria());
            List<ProviderTrade> providerTradeList = mongoTemplate.find(query3.with(providerTradeQueryRequest.getPageRequest()), ProviderTrade.class);

            if(CollectionUtils.isNotEmpty(tradeList)) {
                List<String> yzTids = tradeList.stream().map(Trade::getYzTid).collect(Collectors.toList());
                List<String> notIds1 = ids.stream().filter(id -> !yzTids.contains(id)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(notIds1)) {
                    notExistIds.addAll(notIds1);
                }
            } else {
                notExistIds.addAll(ids);
            }

            if(CollectionUtils.isNotEmpty(providerTradeList)) {
                List<String> yzTids = providerTradeList.stream().map(ProviderTrade::getYzTid).collect(Collectors.toList());
                List<String> notIds2 = ids.stream().filter(id -> !yzTids.contains(id)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(notIds2)) {
                    notExistIds.addAll(notIds2);
                }
            } else {
                notExistIds.addAll(ids);
            }

            List<YzOrderRecord> yzOrderRecords = notExistIds.stream().map(id -> {
                YzOrderRecord record = new YzOrderRecord();
                record.setTid(id);
                record.setFlag(Boolean.FALSE);
                return record;
            }).collect(Collectors.toList());

            yzOrderRecordService.addBatch(yzOrderRecords);
            notExistIds.clear();

            log.info("当前页数：{},入库数据量：{}", queryRequest.getPageNum(), yzOrderRecords.size());
            queryRequest.setPageNum(queryRequest.getPageNum() + 1);
        }
    }

    public void convertAgain(){

        YzOrderRecordQueryRequest queryRequest = new YzOrderRecordQueryRequest();
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(200);
        queryRequest.setFlag(Boolean.FALSE);
        while(true) {

            log.info("当前页码：{}", queryRequest.getPageNum());

            List<YzOrderRecord> yzOrderRecords = yzOrderRecordService.page(queryRequest).getContent();
            List<String> tids = yzOrderRecords.stream().map(yzOrderRecord -> yzOrderRecord.getTid().toLowerCase()).collect(Collectors.toList());
            log.info("补偿订单数量：{}",tids.size());
            if(CollectionUtils.isEmpty(tids)) {
                break;
            }

            if(CollectionUtils.isNotEmpty(tids)) {
                YzOrderQueryRequest request = new YzOrderQueryRequest();
                request.setPageNum(0);
                request.setPageSize(tids.size());
                request.setConvertFlag(Boolean.TRUE);
                request.setIds(tids);
                yzOrderToTrade(request, Boolean.TRUE);
            }
            log.info("------订单入库完成----");
            queryRequest.setPageNum(queryRequest.getPageNum() + 1);
        }

    }

    public void getCycleBuyTrade() {
        String[] orderStatus = {"WAIT_BUYER_PAY","WAIT_SELLER_SEND_GOODS"};
        YzOrderQueryRequest queryRequest = new YzOrderQueryRequest();
        queryRequest.setStatus(Lists.newArrayList(orderStatus));
        queryRequest.setCycleBuyFlag(Boolean.TRUE);
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(10000);
        Map<Long, YzOrder> map = new HashMap<>();

        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);
        fieldsObject.put("full_order_info.orders.item_id",true);
        fieldsObject.put("full_order_info.orders.sku_id", true);
        fieldsObject.put("full_order_info.orders.title", true);
        while(true) {
            Query query = new BasicQuery(new Document(), new Document(fieldsObject));
            query.addCriteria(queryRequest.getCriteria());
            List<YzOrder> yzOrders = mongoTemplate.find(query.with(queryRequest.getPageRequest()), YzOrder.class);

            if(CollectionUtils.isEmpty(yzOrders)) {
                break;
            }

            yzOrders.forEach(yzOrder -> {
                map.put(yzOrder.getFull_order_info().getOrders().get(0).getSku_id(), yzOrder);
            });

            queryRequest.setPageNum(queryRequest.getPageNum() + 1);
        }

        map.forEach((k,v)-> {
            Order order = v.getFull_order_info().getOrders().get(0);
            log.info("周期购商品----skuId:{},itemId:{},name:{}", order.getSku_id(), order.getItem_id(), order.getTitle());
        });
    }


}
