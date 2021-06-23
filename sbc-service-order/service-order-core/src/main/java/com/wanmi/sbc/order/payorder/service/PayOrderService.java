package com.wanmi.sbc.order.payorder.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.account.api.provider.offline.OfflineQueryProvider;
import com.wanmi.sbc.account.api.request.offline.OfflineAccountGetByIdRequest;
import com.wanmi.sbc.account.api.request.offline.OfflineAccountListWithoutDeleteFlagByBankNoRequest;
import com.wanmi.sbc.account.api.response.offline.OfflineAccountGetByIdResponse;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.order.api.request.trade.TradePayRecordObsoleteRequest;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.customer.service.CustomerCommonService;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.payorder.repository.PayOrderRepository;
import com.wanmi.sbc.order.payorder.request.PayOrderGenerateRequest;
import com.wanmi.sbc.order.payorder.request.PayOrderRequest;
import com.wanmi.sbc.order.payorder.response.PayOrderPageResponse;
import com.wanmi.sbc.order.payorder.response.PayOrderResponse;
import com.wanmi.sbc.order.receivables.model.root.Receivable;
import com.wanmi.sbc.order.receivables.repository.ReceivableRepository;
import com.wanmi.sbc.order.receivables.service.ReceivableService;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.service.TradeCacheService;
import com.wanmi.sbc.order.trade.service.TradeService;
import com.wanmi.sbc.order.util.XssUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 支付单服务
 * Created by zhangjin on 2017/4/20.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class PayOrderService {

    @Autowired
    private PayOrderRepository payOrderRepository;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private ReceivableService receivableService;

    @Autowired
    private OfflineQueryProvider offlineQueryProvider;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerCommonService customerCommonService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ReceivableRepository receivableRepository;

    @Autowired
    private TradeCacheService tradeCacheService;

    /**
     * 根据订单号生成支付单
     *
     * @param payOrderGenerateRequest payOrderGenerateRequest
     * @return Optional<payorder>
     */
    @Transactional
    public Optional<PayOrder> generatePayOrderByOrderCode(PayOrderGenerateRequest payOrderGenerateRequest) {
        PayOrder payOrder = new PayOrder();
        BaseResponse<CustomerDetailGetCustomerIdResponse> response = tradeCacheService.getCustomerDetailByCustomerId(payOrderGenerateRequest.getCustomerId());
        CustomerDetailVO customerDetail = response.getContext();
        payOrder.setCustomerDetailId(customerDetail.getCustomerDetailId());
        payOrder.setOrderCode(payOrderGenerateRequest.getOrderCode());
        payOrder.setUpdateTime(LocalDateTime.now());
        payOrder.setCreateTime(payOrderGenerateRequest.getOrderTime());
        payOrder.setDelFlag(DeleteFlag.NO);
        payOrder.setCompanyInfoId(payOrderGenerateRequest.getCompanyInfoId());
        payOrder.setPayOrderNo(generatorService.generateOid());
        if (payOrderGenerateRequest.getPayOrderPrice() == null) {
            payOrder.setPayOrderStatus(PayOrderStatus.PAYED);
        } else {
            payOrder.setPayOrderStatus(PayOrderStatus.NOTPAY);
        }
        payOrder.setPayOrderPrice(payOrderGenerateRequest.getPayOrderPrice());
        payOrder.setPayOrderPoints(payOrderGenerateRequest.getPayOrderPoints());
        payOrder.setPayType(payOrderGenerateRequest.getPayType());

        if (OrderType.POINTS_ORDER.equals(payOrderGenerateRequest.getOrderType())) {
            payOrderRepository.saveAndFlush(payOrder);
            // 积分订单生成收款单
            Receivable receivable = new Receivable();
            receivable.setPayOrderId(payOrder.getPayOrderId());
            receivable.setReceivableNo(generatorService.generateSid());
            receivable.setPayChannel("积分支付");
            receivable.setPayChannelId((Constants.DEFAULT_RECEIVABLE_ACCOUNT));
            receivable.setCreateTime(payOrderGenerateRequest.getOrderTime());
            receivable.setDelFlag(DeleteFlag.NO);
            receivableRepository.save(receivable);
            return Optional.ofNullable(payOrder);
        }

        return Optional.ofNullable(payOrderRepository.saveAndFlush(payOrder));
    }

    /**
     * 作废支付单
     *
     * @param payOrders payOrderIds
     */
    @Transactional
    @GlobalTransactional
    public void destoryPayOrder(List<PayOrder> payOrders, Operator operator) {
        //返回修改对应订单状态
        //只有支付完的订单那才改状态
        List<String> orderIds = payOrders.stream().filter(payOrder -> PayOrderStatus.PAYED.equals(payOrder
                .getPayOrderStatus()) || PayOrderStatus.TOCONFIRM.equals(payOrder
                .getPayOrderStatus())).map(PayOrder::getOrderCode).collect(Collectors.toList());

        List<String> payIds = payOrders.stream().map(PayOrder::getPayOrderId).collect(Collectors.toList());

        receivableService.deleteReceivables(payIds);
        if (!CollectionUtils.isEmpty(payIds)) {
            payOrderRepository.updatePayOrderStatus(payIds, PayOrderStatus.NOTPAY);
        }

        orderIds.forEach(orderId -> {
            TradePayRecordObsoleteRequest tradePayRecordObsoleteRequest =
                    TradePayRecordObsoleteRequest.builder().tid(orderId).operator(operator).build();
            tradeService.payRecordObsolete(tradePayRecordObsoleteRequest.getTid(),
                    tradePayRecordObsoleteRequest.getOperator());

        });
    }


    /**
     * pay模块无法引入tradeService，此处将OrderList传到controller，判断trade是否过了账期
     *
     * @param payOrderIds
     * @return
     */
    public List<PayOrder> findPayOrderByPayOrderIds(List<String> payOrderIds) {
        if (CollectionUtils.isEmpty(payOrderIds)) {
            throw new SbcRuntimeException("K-020002");
        }
        return payOrderRepository.findAllById(payOrderIds);
    }


    /**
     * 修改收款单状态
     *
     * @param payOrderId payOrderId
     * @param payOrderId payOrderStatus
     */
    @Transactional
    public void updatePayOrder(String payOrderId, PayOrderStatus payOrderStatus) {
        if (Objects.isNull(payOrderId)) {
            throw new SbcRuntimeException("K-020002");
        }
        payOrderRepository.updatePayOrderStatus(Lists.newArrayList(payOrderId), payOrderStatus);
    }

    /**
     * 根据订单编号查询支付单，支付单状态..
     *
     * @param orderCode orderCode
     * @return 支付单
     */
    public Optional<PayOrder> findPayOrderByOrderCode(String orderCode) {
        return payOrderRepository.findByOrderCodeAndDelFlag(orderCode, DeleteFlag.NO);
    }


    /**
     * 根据订单编号批量查询支付单，支付单状态..
     *
     * @param  orderCodes
     * @return 支付单
     */
    public List<PayOrder> findPayOrderByOrderCodes(List<String> orderCodes) {
        return payOrderRepository.findByOrderCodes(orderCodes);
    }



    public PayOrderResponse findPayOrder(String orderNo) {
        PayOrder payOrder = payOrderRepository.findByOrderCodeAndDelFlag(orderNo, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException("K-070001"));

        payOrder.setCustomerDetail(customerCommonService.getAnyCustomerDetailById(payOrder.getCustomerDetailId()));
        payOrder.setCompanyInfo(customerCommonService.getCompanyInfoById(payOrder.getCompanyInfoId()));
        return getPayOrderResponse(payOrder);
    }


    /**
     * 根据查询条件做收款单分页查询
     *
     * @param payOrderRequest payOrderRequest
     * @return Page<payorder>
     */
    public PayOrderPageResponse findPayOrders(PayOrderRequest payOrderRequest) {
        PayOrderPageResponse payOrderPageResponse = new PayOrderPageResponse();
        payOrderPageResponse.setPayOrderResponses(Collections.emptyList());
        payOrderPageResponse.setTotal(0L);
        payOrderPageResponse.setCurrentPage(payOrderRequest.getPageNum());
        payOrderPageResponse.setPageSize(payOrderRequest.getPageSize());

        //模糊匹配会员/商户名称，不符合条件直接返回
        if (!this.likeCustomerAndSupplierNameAndBankNo(payOrderRequest)) {
            return payOrderPageResponse;
        }

        Page<PayOrder> payOrders = payOrderRepository.findAll(findByRequest(payOrderRequest),
                payOrderRequest.getPageable());
        payOrderPageResponse.setPayOrderResponses(this.getPayOrderResponses(payOrders.getContent()));
        payOrderPageResponse.setTotal(payOrders.getTotalElements());
        return payOrderPageResponse;
    }

    /**
     * 根据查询条件做收款单查询
     *
     * @param payOrderRequest payOrderRequest
     * @return PayOrderPageResponse
     */
    public PayOrderPageResponse findPayOrdersWithNoPage(PayOrderRequest payOrderRequest) {
        //模糊匹配会员/商户名称，不符合条件直接返回
        if (!this.likeCustomerAndSupplierNameAndBankNo(payOrderRequest)) {
            return PayOrderPageResponse.builder().payOrderResponses(Collections.EMPTY_LIST).build();
        }

        List<PayOrder> payOrders = payOrderRepository.findAll(findByRequest(payOrderRequest));
        if (Objects.isNull(payOrders) || CollectionUtils.isEmpty(payOrders)) {
            return PayOrderPageResponse.builder().payOrderResponses(Collections.EMPTY_LIST).build();
        }
        return PayOrderPageResponse.builder().payOrderResponses(this.getPayOrderResponses(payOrders)).build();
    }


    /**
     * 获取多个支付单返回数据
     *
     * @param payOrders payOrder
     * @return PayOrderResponse
     */
    private List<PayOrderResponse> getPayOrderResponses(List<PayOrder> payOrders) {
        List<Long> companyIds = payOrders.stream()
                .map(PayOrder::getCompanyInfoId).collect(Collectors.toList());

        Map<Long, CompanyInfoVO> companyInfoVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(companyIds)) {
            companyInfoVOMap.putAll(customerCommonService.listCompanyInfoByCondition(
                    CompanyListRequest.builder().companyInfoIds(companyIds).build()
            ).stream().collect(Collectors.toMap(CompanyInfoVO::getCompanyInfoId, c -> c)));
        }

        Map<String, Trade> tradeMap = new HashMap<>();
        List<String> orderCodes = payOrders.stream().map(PayOrder::getOrderCode).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(orderCodes)) {
            List<Trade> trades = tradeService
                    .queryAll(TradeQueryRequest.builder().ids(orderCodes.toArray(new String[orderCodes.size()])).orderType(OrderType.ALL_ORDER).build())
                    .stream()
                    .collect(Collectors.toList());

            //尾款订单号
            List<String> tailOrderNos = orderCodes.stream().filter(orderCode -> orderCode.startsWith("OT")).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(tailOrderNos)) {
                tailOrderNos.forEach(tailOrderNo -> {
                    Optional<Trade> optional = trades.stream().filter(trade -> tailOrderNo.equals(trade.getTailOrderNo())).findFirst();
                    //尾款订单号所代表的订单对象在trades找不到再去查库
                    if(!optional.isPresent()){
                        List<Trade> tradeList = tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(tailOrderNo).build());
                        if(CollectionUtils.isNotEmpty(tradeList)) {
                            trades.addAll(tradeList);
                        }
                    }
                });
            }
            orderCodes.forEach(orderCode -> {
                List<Trade> list = trades.stream().filter(trade -> orderCode.equals(trade.getId()) || orderCode.equals(trade.getTailOrderNo())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(list)){
                    tradeMap.put(orderCode, list.get(0));
                }
            });
            tradeMap.putAll(trades.stream().collect(Collectors.toMap(Trade::getId, t -> t, (k1,k2) -> k1)));
            if(CollectionUtils.isNotEmpty(trades)){
                Trade trade = trades.get(0);
                if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                        && StringUtils.isNotEmpty(trade.getTailOrderNo())) {
                    tradeMap.put(trade.getTailOrderNo(), trade);
                }
            }
        }

        if (companyInfoVOMap.isEmpty() || tradeMap.isEmpty()) return new ArrayList<>();

        return payOrders.stream().map(payOrder -> {
            payOrder.setCompanyInfo(companyInfoVOMap.get(payOrder.getCompanyInfoId()));
            Trade trade = tradeMap.get(payOrder.getOrderCode());
            if (Objects.nonNull(trade)){
                Buyer buyer = trade.getBuyer();
                CustomerDetailVO customerDetailVO = new CustomerDetailVO();
                customerDetailVO.setCustomerId(buyer.getId());
                customerDetailVO.setCustomerName(buyer.getName());
                payOrder.setCustomerDetail(customerDetailVO);
            }
            return this.getPayOrderResponse(payOrder);
        }).collect(Collectors.toList());
    }

    /**
     * 获取支付单返回数据
     *
     * @param payOrder payOrder
     * @return PayOrderResponse
     */
    private PayOrderResponse getPayOrderResponse(PayOrder payOrder) {
        PayOrderResponse payOrderResponse = new PayOrderResponse();
        BeanUtils.copyProperties(payOrder, payOrderResponse);
        payOrderResponse.setTotalPrice(payOrder.getPayOrderPrice());
        payOrderResponse.setPayOrderPoints(payOrder.getPayOrderPoints());
        if (Objects.nonNull(payOrder.getCustomerDetail())) {
            payOrderResponse.setCustomerId(payOrder.getCustomerDetail().getCustomerId());
            payOrderResponse.setCustomerName(payOrder.getCustomerDetail().getCustomerName());

        }
        if (Objects.nonNull(payOrder.getCompanyInfo())) {
            payOrderResponse.setCompanyInfoId(payOrder.getCompanyInfo().getCompanyInfoId());
            payOrderResponse.setSupplierName(payOrder.getCompanyInfo().getSupplierName());
        }

        if (Objects.nonNull(payOrder.getReceivable())) {
            payOrderResponse.setReceivableNo(payOrder.getReceivable().getReceivableNo());
            payOrderResponse.setComment(payOrder.getReceivable().getComment());
            //收款时间
            payOrderResponse.setReceiveTime(payOrder.getReceivable().getCreateTime());
            payOrderResponse.setReceivableAccount(parseAccount(payOrder,false));
            payOrderResponse.setNormalReceivableAccount(parseAccount(payOrder,true));
            //支付渠道
            payOrderResponse.setPayChannel(payOrder.getReceivable().getPayChannel());
            payOrderResponse.setPayChannelId(payOrder.getReceivable().getPayChannelId());
            payOrderResponse.setEncloses(payOrder.getReceivable().getEncloses());
            //online todo
        }
        Trade trade = tradeService.detail(payOrder.getOrderCode());
        if (Objects.isNull(trade)) {
            // 尾款订单
            List<Trade> tradeList = tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(payOrder.getOrderCode()).build());
            trade = CollectionUtils.isNotEmpty(tradeList) ? tradeList.get(0) : new Trade();
        }
        payOrderResponse.setTradeState(trade.getTradeState());

        return payOrderResponse;
    }


    /**
     *
     * @param payOrder
     * @param normal 是否需要正常直接展示
     * @return
     */
    private String parseAccount(PayOrder payOrder, boolean normal) {
        StringBuilder accountName = new StringBuilder();
        if (PayType.OFFLINE.equals(payOrder.getPayType()) && Objects.nonNull(payOrder.getReceivable()
                .getOfflineAccountId())) {
            OfflineAccountGetByIdResponse response = offlineQueryProvider.getById(new OfflineAccountGetByIdRequest
                    (payOrder
                            .getReceivable()
                            .getOfflineAccountId())).getContext();
            if (response.getAccountId() != null) {
                //正常展示
                if (normal) {
                    accountName.append(response.getBankName()).append(response.getBankNo());
                } else {
                    accountName.append(response.getBankName()).append(" ")
                            .append(ReturnOrderService.getDexAccount(response.getBankNo()));
                }
            }
        }
        return accountName.toString();
    }

    /**
     * 通过订单编号列表查询支付单
     *
     * @param orderNos
     * @return
     */
    public List<PayOrder> findByOrderNos(List<String> orderNos, PayOrderStatus payOrderStatus) {
        return payOrderRepository.findByOrderNos(orderNos, payOrderStatus);
    }

    /**
     * 删除收款单
     *
     * @param payOrderId
     * @return rows
     */
    @Transactional
    public int deleteByPayOrderId(String payOrderId) {
        return payOrderRepository.deletePayOrderById(payOrderId);
    }


    /**
     * 合计收款金额
     *
     * @param payOrderRequest request
     * @return sum
     */
    public BigDecimal sumPayOrderPrice(PayOrderRequest payOrderRequest) {
        //模糊匹配会员/商户名称，不符合条件直接返回0
        if (!likeCustomerAndSupplierNameAndBankNo(payOrderRequest)) {
            return BigDecimal.ZERO;
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);

        Root<PayOrder> root = query.from(PayOrder.class);
        query.select(builder.sum(root.get("payOrderPrice")));
        query.where(buildWhere(payOrderRequest, root, query, builder));

        return entityManager.createQuery(query).getSingleResult();
    }

    /**
     * 替代关联查询-模糊商家名称、模糊会员名称、银行账号，以并且关系的判断
     *
     * @param payOrderRequest
     * @return true:有符合条件的数据,false:没有符合条件的数据
     */
    private boolean likeCustomerAndSupplierNameAndBankNo(final PayOrderRequest payOrderRequest) {
        boolean supplierLike = true;
        //商家名称
        if (StringUtils.isNotBlank(payOrderRequest.getSupplierName()) && StringUtils.isNotBlank(payOrderRequest
                .getSupplierName().trim())) {
            CompanyListRequest request = CompanyListRequest.builder()
                    .supplierName(payOrderRequest.getSupplierName())
                    .build();
            payOrderRequest.setCompanyInfoIds(customerCommonService.listCompanyInfoIdsByCondition(request));

            if (CollectionUtils.isEmpty(payOrderRequest.getCompanyInfoIds())) {
                supplierLike = false;
            }
        }
        //模糊会员名称
        boolean customerLike = true;
        if (StringUtils.isNotBlank(payOrderRequest.getCustomerName()) && StringUtils.isNotBlank(payOrderRequest
                .getCustomerName().trim())) {
            List<Trade> trades = tradeService.queryAll(TradeQueryRequest.builder().buyerName(payOrderRequest.getCustomerName()).build());
            if (CollectionUtils.isEmpty(trades)) {
                customerLike = false;
            } else {
                List<String> customerIds = trades.stream().map(Trade::getBuyer).map(Buyer::getId).collect(Collectors.toList());
                CustomerDetailListByConditionRequest request = CustomerDetailListByConditionRequest.builder().customerIds(customerIds).build();
                payOrderRequest.setCustomerDetailIds(customerCommonService.listCustomerDetailIdsByCondition(request));

                if (CollectionUtils.isEmpty(payOrderRequest.getCustomerDetailIds())) {
                    customerLike = false;
                }
            }
        }
        // 模糊银行账号
        boolean OfflineAccountLike = true;

        if (StringUtils.isNotBlank(payOrderRequest.getAccount())) {
            List<Long> offlineAccountIds = customerCommonService.listOfflineAccountIdsByBankNo(new
                    OfflineAccountListWithoutDeleteFlagByBankNoRequest(payOrderRequest.getAccount
                    ()));

            payOrderRequest.setAccountIds(offlineAccountIds);

            if (CollectionUtils.isEmpty(offlineAccountIds)) {
                OfflineAccountLike = false;
            }
        }

        return supplierLike && customerLike && OfflineAccountLike;
    }


    /**
     * 构建动态查
     *
     * @param payOrderRequest
     * @return
     */
    private Specification<PayOrder> findByRequest(final PayOrderRequest payOrderRequest) {
        return (Root<PayOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> buildWhere(payOrderRequest, root,
                query, cb);
    }

    /**
     * 构建列表查询的where条件
     *
     * @param payOrderRequest request
     * @param root            root
     * @param query           query
     * @param cb              bc
     * @return predicates
     */
    private Predicate buildWhere(PayOrderRequest payOrderRequest, Root<PayOrder> root, CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Join<PayOrder, Receivable> payOrderReceivableJoin = root.join("receivable", JoinType.LEFT);
        payOrderReceivableJoin.on(cb.equal(payOrderReceivableJoin.get("delFlag"), DeleteFlag.NO));

        if (!StringUtils.isEmpty(payOrderRequest.getOrderNo()) && !StringUtils.isEmpty(payOrderRequest.getOrderNo()
                .trim())) {
            predicates.add(cb.equal(root.get("orderCode"), payOrderRequest.getOrderNo()));
        }

        if (!StringUtils.isEmpty(payOrderRequest.getOrderCode()) && !StringUtils.isEmpty(payOrderRequest.getOrderCode
                ().trim())) {
            predicates.add(cb.like(root.get("orderCode"), buildLike(payOrderRequest.getOrderCode())));
        }

        if (CollectionUtils.isNotEmpty(payOrderRequest.getOrderNoList())) {
            predicates.add(root.get("orderCode").in(payOrderRequest.getOrderNoList()));
        }

        //收款单编号
        if (!StringUtils.isEmpty(payOrderRequest.getPayBillNo()) && !StringUtils.isEmpty(payOrderRequest.getPayBillNo
                ().trim())) {
            predicates.add(cb.like(payOrderReceivableJoin.get("receivableNo"), buildLike(payOrderRequest.getPayBillNo
                    ())));
        }
        //支付单状态
        if (Objects.nonNull(payOrderRequest.getPayOrderStatus())) {
            predicates.add(cb.equal(root.get("payOrderStatus"), payOrderRequest.getPayOrderStatus()));
        }

        if (Objects.nonNull(payOrderRequest.getCompanyInfoId())) {
            predicates.add(cb.equal(root.get("companyInfoId"), payOrderRequest.getCompanyInfoId()));
        }

        if (CollectionUtils.isNotEmpty(payOrderRequest.getCompanyInfoIds())) {
            predicates.add(root.get("companyInfoId").in(payOrderRequest.getCompanyInfoIds()));
        }

        if (CollectionUtils.isNotEmpty(payOrderRequest.getCustomerDetailIds())) {
            predicates.add(root.get("customerDetailId").in(payOrderRequest.getCustomerDetailIds()));
        }

        if (Objects.nonNull(payOrderRequest.getPayChannelId())) {
            predicates.add(cb.equal(payOrderReceivableJoin.get("payChannelId"), payOrderRequest.getPayChannelId()));
        }

        if (Objects.nonNull(payOrderRequest.getPayType())) {
            predicates.add(cb.equal(root.get("payType"), payOrderRequest.getPayType()));
        }

//        //账号名称离线查询
//        if (!StringUtils.isEmpty(payOrderRequest.getAccount())) {
//            predicates.add(cb.like(receivableOfflineAccountJoin.get("bankNo"), buildLike(payOrderRequest.getAccount()
//            )));
//        }
        if (!StringUtils.isEmpty(payOrderRequest.getAccount())) {
            predicates.add(payOrderReceivableJoin.get("offlineAccountId").in(payOrderRequest.getAccountIds()));
        }

        //收款开始时间
        if (!StringUtils.isEmpty(payOrderRequest.getStartTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            predicates.add(cb.greaterThanOrEqualTo(payOrderReceivableJoin.get("createTime"), LocalDateTime.of(LocalDate
                    .parse(payOrderRequest.getStartTime(), formatter), LocalTime.MIN)));
        }

        //收款
        if (!StringUtils.isEmpty(payOrderRequest.getEndTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            predicates.add(cb.lessThanOrEqualTo(payOrderReceivableJoin.get("createTime"),
                    LocalDateTime.of(LocalDate
                            .parse(payOrderRequest.getEndTime(), formatter), LocalTime.MIN)));
        }

        //线下账户查询
        if (!StringUtils.isEmpty(payOrderRequest.getAccountId())) {
            predicates.add(cb.equal(payOrderReceivableJoin.get("offlineAccountId"), payOrderRequest.getAccountId()));
        }

        if (!CollectionUtils.isEmpty(payOrderRequest.getPayOrderIds())) {
            predicates.add(root.get("payOrderId").in(payOrderRequest.getPayOrderIds()));
        }

        //删除条件
        predicates.add(cb.equal(root.get("delFlag"), DeleteFlag.NO));
        if (payOrderRequest.getSortByReceiveTime()) {
            query.orderBy(cb.desc(payOrderReceivableJoin.get("createTime")));
        } else {
            query.orderBy(cb.desc(root.get("createTime")));
        }

        return cb.and(predicates.toArray(new Predicate[]{}));
    }

    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }
}
