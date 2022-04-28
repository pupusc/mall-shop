package com.wanmi.sbc.order.refund.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
import com.soybean.mall.order.enums.WxAfterSaleOperateType;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.account.api.provider.offline.OfflineQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsAddAmountRequest;
import com.wanmi.sbc.account.api.request.offline.OfflineAccountGetByIdRequest;
import com.wanmi.sbc.account.api.response.offline.OfflineAccountGetByIdResponse;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.order.api.request.refund.RefundOrderAccountRecordRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRefundRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderAccountRecordResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderPageResponse;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.bean.vo.RefundOrderResponse;
import com.wanmi.sbc.order.customer.service.CustomerCommonService;
import com.wanmi.sbc.order.refund.model.root.RefundBill;
import com.wanmi.sbc.order.refund.model.root.RefundOrder;
import com.wanmi.sbc.order.refund.repository.RefundOrderRepository;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPoints;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPrice;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderRepository;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.util.XssUtils;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.RefundRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

/**
 * 退款单服务
 * Created by zhangjin on 2017/4/21.
 */
@Slf4j
@Service
public class RefundOrderService {

    @Autowired
    private CustomerCommonService customerCommonService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private RefundOrderRepository refundOrderRepository;

    @Autowired
    private RefundBillService refundBillService;

    @Autowired
//    private OfflineService offlineService;
    private OfflineQueryProvider offlineQueryProvider;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReturnOrderRepository returnOrderRepository;


    @Autowired
    private MongoTemplate mongoTemplate;


    public static final String FMT_TIME_1 = "yyyy-MM-dd HH:mm:ss";


    @Transactional
    public List<RefundOrder> batchAdd(List<RefundOrder> refundOrderList) {
        return refundOrderRepository.saveAll(refundOrderList);
    }

    /**
     * 根据退单生成退款单 //todo 操作人
     *
     * @param returnOrderCode returnOrderCode
     * @param customerId      customerId
     * @param price           price 应退金额
     * @param price           payType 应退金额
     * @return 退款单
     */
    @Transactional
    public Optional<RefundOrder> generateRefundOrderByReturnOrderCode(String returnOrderId, String customerId,
                                                                      BigDecimal returnOrderPrice, PayType payType) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId).orElse(null);
        if (Objects.isNull(returnOrder)) {
            log.error("退单编号：{},查询不到退单信息", returnOrderId);
            return Optional.empty();
        }
        Optional<RefundOrder> returnOrderCodeAndDelFlag = refundOrderRepository.findAllByReturnOrderCodeAndDelFlag(returnOrderId,DeleteFlag.NO);
        if(returnOrderCodeAndDelFlag.isPresent()){
            return returnOrderCodeAndDelFlag;
        }
        RefundOrder refundOrder = new RefundOrder();
        CustomerDetailVO customerDetail = customerCommonService.getCustomerDetailByCustomerId(customerId);
        refundOrder.setReturnOrderCode(returnOrderId);
        refundOrder.setCustomerDetailId(customerDetail.getCustomerDetailId());
        refundOrder.setCreateTime(LocalDateTime.now());
        refundOrder.setRefundCode(generatorService.generateRid());
        refundOrder.setRefundStatus(RefundStatus.TODO);
        refundOrder.setReturnPrice(returnOrderPrice);

        long actualPointsOrKnowledge = 0L;
        if (Objects.nonNull(returnOrder.getReturnPoints())) {
            actualPointsOrKnowledge = returnOrder.getReturnPoints().getApplyPoints();
        }

        if (actualPointsOrKnowledge <= 0 && Objects.nonNull(returnOrder.getReturnKnowledge())) {
            actualPointsOrKnowledge = returnOrder.getReturnKnowledge().getApplyKnowledge();
        }
        refundOrder.setReturnPoints(actualPointsOrKnowledge);
        refundOrder.setDelFlag(DeleteFlag.NO);
        refundOrder.setPayType(payType);
        refundOrder.setSupplierId(returnOrder.getCompany().getCompanyInfoId());
        return Optional.of(refundOrderRepository.saveAndFlush(refundOrder));
    }


    /**
     * 根据退单生成尾款退款单 //todo 操作人
     *
     * @param customerId      customerId
     * @param price           price 应退金额
     * @param price           payType 应退金额
     * @return 退款单
     */
    @Transactional
    public Optional<RefundOrder> generateTailRefundOrderByReturnOrderCode(ReturnOrder returnOrder, String customerId,
                                                                      BigDecimal price, PayType payType) {
        RefundOrder refundOrder = new RefundOrder();
        CustomerDetailVO customerDetail = customerCommonService.getCustomerDetailByCustomerId(customerId);
        refundOrder.setReturnOrderCode(returnOrder.getBusinessTailId());
        refundOrder.setCustomerDetailId(customerDetail.getCustomerDetailId());
        refundOrder.setCreateTime(LocalDateTime.now());
        refundOrder.setRefundCode(generatorService.generateRid());
        refundOrder.setRefundStatus(RefundStatus.TODO);
        refundOrder.setReturnPrice(price);
        refundOrder.setReturnPoints(null);
        refundOrder.setDelFlag(DeleteFlag.NO);
        refundOrder.setPayType(payType);
        refundOrder.setSupplierId(returnOrder.getCompany().getCompanyInfoId());
        return Optional.ofNullable(refundOrderRepository.saveAndFlush(refundOrder));
    }

    /**
     * 查询退款单
     *
     * @param refundOrderRequest refundOrderRequest
     * @return Page<RefundOrder>
     */
    public RefundOrderPageResponse findByRefundOrderRequest(RefundOrderRequest refundOrderRequest) {
        RefundOrderPageResponse refundOrderPageResponse = new RefundOrderPageResponse();
        refundOrderPageResponse.setData(EMPTY_LIST);
        refundOrderPageResponse.setTotal(0L);

        //模糊匹配会员/商户名称，不符合条件直接返回
        if (!this.likeCustomerAndSupplierName(refundOrderRequest)) {
            return refundOrderPageResponse;
        }

        Page<RefundOrder> refundOrderPage = refundOrderRepository.findAll(findByRequest(refundOrderRequest),
                PageRequest.of(refundOrderRequest.getPageNum(), refundOrderRequest.getPageSize()));

        if (Objects.isNull(refundOrderPage) || CollectionUtils.isEmpty(refundOrderPage.getContent())) {
            return refundOrderPageResponse;
        }
        List<RefundOrderResponse> refundOrderResponses = generateRefundOrderResponseNew(refundOrderPage.getContent());
//        List<RefundOrderResponse> refundOrderResponses = refundOrderPage.getContent().stream()
//                .map(this::generateRefundOrderResponse).collect(Collectors.toList());

        refundOrderPageResponse.setTotal(refundOrderPage.getTotalElements());
        refundOrderPageResponse.setData(refundOrderResponses);
        return refundOrderPageResponse;
    }

    /**
     * 查询不带分页的退款单
     *
     * @param refundOrderRequest refundOrderRequest
     * @return RefundOrderPageResponse
     */
    public RefundOrderPageResponse findByRefundOrderRequestWithNoPage(RefundOrderRequest refundOrderRequest) {
        RefundOrderPageResponse refundOrderPageResponse = new RefundOrderPageResponse();
        refundOrderPageResponse.setData(EMPTY_LIST);
        refundOrderPageResponse.setTotal(0L);

        //模糊匹配会员/商户名称，不符合条件直接返回
        if (!this.likeCustomerAndSupplierName(refundOrderRequest)) {
            return refundOrderPageResponse;
        }

        List<RefundOrder> refundOrders = refundOrderRepository.findAll(findByRequest(refundOrderRequest));
        if (Objects.isNull(refundOrders) || CollectionUtils.isEmpty(refundOrders)) {
            return refundOrderPageResponse;
        }
        List<RefundOrderResponse> refundOrderResponses = refundOrders.stream().map(this::generateRefundOrderResponse)
                .collect(Collectors.toList());
        refundOrderPageResponse.setData(refundOrderResponses);
        return refundOrderPageResponse;
    }

    /**
     * 根据条件查询，不分页
     *
     * @param refundOrderRequest refundOrderRequest
     * @return List
     */
    public List<RefundOrder> findAll(RefundOrderRequest refundOrderRequest) {
        //模糊匹配会员/商户名称，不符合条件直接返回
        if (!this.likeCustomerAndSupplierName(refundOrderRequest)) {
            return EMPTY_LIST;
        }
        return refundOrderRepository.findAll(findByRequest(refundOrderRequest));
    }

    public Optional<RefundOrder> findById(String refundId) {
        return refundOrderRepository.findByRefundIdAndDelFlag(refundId, DeleteFlag.NO);
    }

    /**
     * 根据退单编号查询退款单
     *
     * @param returnOrderCode 退单编号
     * @return 退款单信息
     */
    public RefundOrder findRefundOrderByReturnOrderNo(String returnOrderCode) {
        Optional<RefundOrder> refundOrderOptional =
                refundOrderRepository.findAllByReturnOrderCodeAndDelFlag(returnOrderCode, DeleteFlag.NO);
        if (refundOrderOptional.isPresent()) {
            return refundOrderOptional.get();
        } else {
            throw new SbcRuntimeException("K-050003");
        }
    }

    /**
     * 根据退单编号查询退款单
     *
     * @param returnOrderCode 退单编号
     * @return 退款单信息
     */
    public RefundOrderResponse findRefundOrderRespByReturnOrderNo(String returnOrderCode) {
        return generateRefundOrderResponse(findRefundOrderByReturnOrderNo(returnOrderCode));
    }



    /**
     * @Author yangzhen
     * @Description //TODO  根据 RefundOrders 生成 RefundOrderResponse 对象 优化，去除循环查询
     * @Date 13:51 2020/11/28
     * @Param [refundOrder]
     * @return java.util.List<com.wanmi.sbc.order.bean.vo.RefundOrderResponse>
     **/
    private List<RefundOrderResponse> generateRefundOrderResponseNew(List<RefundOrder> refundOrder) {
        List<RefundOrderResponse> responseList = new ArrayList<>();

        List<ReturnOrder> returnOrders = org.apache.commons.collections4.IteratorUtils.toList(
                returnOrderRepository.findAllById(refundOrder.stream().map(RefundOrder::getReturnOrderCode)
                        .collect(Collectors.toList())).iterator());

        Map<String, ReturnOrder> returnOrderMap
                = returnOrders.stream().collect(Collectors.toMap(ReturnOrder::getId, Function.identity(), (k1, k2) -> k1));

        List<CompanyInfoVO> companyInfoVOS = customerCommonService.listCompanyInfoByCondition(
                CompanyListRequest.builder().companyInfoIds(refundOrder.stream().map(RefundOrder::getSupplierId)
                        .collect(Collectors.toList())).build());

        Map<Long, CompanyInfoVO> companyInfoMap
                = companyInfoVOS.stream().collect(Collectors.toMap(CompanyInfoVO::getCompanyInfoId, Function.identity(), (k1, k2) -> k1));

        refundOrder.forEach(v -> {
            RefundOrderResponse refundOrderResponse = new RefundOrderResponse();
            BeanUtils.copyProperties(v, refundOrderResponse);
            ReturnOrder returnOrder = returnOrderMap.get(v.getReturnOrderCode());
            if (Objects.nonNull(returnOrder)) {
                refundOrderResponse.setCustomerId(returnOrder.getBuyer().getId());
                refundOrderResponse.setCustomerName(returnOrder.getBuyer().getName());
            }

            CompanyInfoVO companyInfo = companyInfoMap.get(v.getSupplierId());
            if (Objects.nonNull(companyInfo)) {
                refundOrderResponse.setSupplierName(companyInfo.getSupplierName());
                if (CollectionUtils.isNotEmpty(companyInfo.getStoreVOList())) {
                    StoreVO store = companyInfo.getStoreVOList().get(0);
                    refundOrderResponse.setStoreId(store.getStoreId());
                }
            }

            if (Objects.nonNull(v.getRefundBill()) && DeleteFlag.NO.equals(v.getRefundBill().getDelFlag())) {
                //从退单冗余信息中获取用户账户信息(防止用户修改后,查询的不是当时退单的账户信息)
                ReturnOrder returnOrderEntity = returnOrderMap.get(v.getReturnOrderCode());
                if (returnOrderEntity != null && returnOrderEntity.getCustomerAccount() != null) {
                    log.info("客户账户信息customerAccount: {}", returnOrderEntity.getCustomerAccount());
                    refundOrderResponse.setCustomerAccountName(returnOrderEntity.getCustomerAccount().getCustomerBankName() + "" +
                            " " + (
                            StringUtils.isNotBlank(returnOrderEntity.getCustomerAccount().getCustomerAccountNo()) ?
                                    getDexAccount(returnOrderEntity.getCustomerAccount().getCustomerAccountNo()) : ""
                    ));
                }

                refundOrderResponse.setActualReturnPrice(v.getRefundBill().getActualReturnPrice());
                refundOrderResponse.setActualReturnPoints(v.getRefundBill().getActualReturnPoints());
                refundOrderResponse.setActualReturnKnowledge(v.getRefundBill().getActualReturnKnowledge());
                refundOrderResponse.setReturnAccount(v.getRefundBill().getOfflineAccountId());
                refundOrderResponse.setOfflineAccountId(v.getRefundBill().getOfflineAccountId());
                refundOrderResponse.setComment(v.getRefundBill().getRefundComment());
                refundOrderResponse.setRefundBillCode(v.getRefundBill().getRefundBillCode());
                refundOrderResponse.setReturnAccountName(parseAccount(v));
                // 退款时间以boss端审核时间为准
                if (Objects.equals(RefundStatus.FINISH, v.getRefundStatus())) {
                    refundOrderResponse.setRefundBillTime(v.getRefundBill().getCreateTime());
                }
                refundOrderResponse.setPayChannel(v.getRefundBill().getPayChannel());
                refundOrderResponse.setPayChannelId(v.getRefundBill().getPayChannelId());
            }
            responseList.add(refundOrderResponse);

        });
        return responseList;
    }


    /**
     * 根据 RefundOrder 生成 RefundOrderResponse 对象
     *
     * @param refundOrder refundOrder
     * @return new RefundOrderResponse()
     */
    private RefundOrderResponse generateRefundOrderResponse(RefundOrder refundOrder) {
        RefundOrderResponse refundOrderResponse = new RefundOrderResponse();
        BeanUtils.copyProperties(refundOrder, refundOrderResponse);

        if (StringUtils.isNotBlank(refundOrder.getReturnOrderCode())) {
            ReturnOrder returnOrder = returnOrderRepository.findById(refundOrder.getReturnOrderCode()).orElse(null);
            if (returnOrder != null && returnOrder.getBuyer() != null) {
                refundOrderResponse.setCustomerId(returnOrder.getBuyer().getId());
                refundOrderResponse.setCustomerName(returnOrder.getBuyer().getName());
            }
        }
        CompanyInfoVO companyInfo = null;
        if (Objects.nonNull(refundOrder.getSupplierId())) {
            companyInfo = customerCommonService.getCompanyInfoById(refundOrder.getSupplierId());
        }
        if (Objects.nonNull(companyInfo)) {
            refundOrderResponse.setSupplierName(companyInfo.getSupplierName());
            if (CollectionUtils.isNotEmpty(companyInfo.getStoreVOList())) {
                StoreVO store = companyInfo.getStoreVOList().get(0);
                refundOrderResponse.setStoreId(store.getStoreId());
            }
        }

        if (Objects.nonNull(refundOrder.getRefundBill()) && DeleteFlag.NO.equals(refundOrder.getRefundBill().getDelFlag())) {
            //从退单冗余信息中获取用户账户信息(防止用户修改后,查询的不是当时退单的账户信息)
            ReturnOrder returnOrder = returnOrderRepository.findById(refundOrder.getReturnOrderCode()).orElse(null);
            if (returnOrder != null && returnOrder.getCustomerAccount() != null) {
                log.info("客户账户信息customerAccount: {}", returnOrder.getCustomerAccount());
                refundOrderResponse.setCustomerAccountName(returnOrder.getCustomerAccount().getCustomerBankName() + "" +
                        " " + (
                        StringUtils.isNotBlank(returnOrder.getCustomerAccount().getCustomerAccountNo()) ?
                                getDexAccount(returnOrder.getCustomerAccount().getCustomerAccountNo()) : ""
                ));
            }

            refundOrderResponse.setActualReturnPrice(refundOrder.getRefundBill().getActualReturnPrice());
            refundOrderResponse.setActualReturnPoints(refundOrder.getRefundBill().getActualReturnPoints());
            refundOrderResponse.setActualReturnKnowledge(refundOrder.getRefundBill().getActualReturnKnowledge());
            refundOrderResponse.setReturnAccount(refundOrder.getRefundBill().getOfflineAccountId());
            refundOrderResponse.setOfflineAccountId(refundOrder.getRefundBill().getOfflineAccountId());
            refundOrderResponse.setComment(refundOrder.getRefundBill().getRefundComment());
            refundOrderResponse.setRefundBillCode(refundOrder.getRefundBill().getRefundBillCode());
            refundOrderResponse.setReturnAccountName(parseAccount(refundOrder));
            // 退款时间以boss端审核时间为准
            if (Objects.equals(RefundStatus.FINISH, refundOrder.getRefundStatus())) {
                refundOrderResponse.setRefundBillTime(refundOrder.getRefundBill().getCreateTime());
            }
            refundOrderResponse.setPayChannel(refundOrder.getRefundBill().getPayChannel());
            refundOrderResponse.setPayChannelId(refundOrder.getRefundBill().getPayChannelId());
        }

        return refundOrderResponse;
    }

    /**
     * 作废退款单
     *
     * @param id id
     */
    @Transactional
    public void destory(String id) {
        refundBillService.deleteByRefundId(id);
        batchDestory(Lists.newArrayList(id));
    }

    /**
     * 批量确认
     *
     * @param ids ids
     */
    @Transactional
    public void batchConfirm(List<String> ids) {
        //updateRefundConsumer.accept(ids, RefundStatus.FINISH);
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<RefundOrder> refundOrders = refundOrderRepository.findAllById(ids);
        refundOrders.forEach(refundOrder -> {
            refundOrder.setRefundStatus(RefundStatus.FINISH);
        });
//        refundOrderRepository.updateRefundOrderStatus(RefundStatus.FINISH, ids);
    }

    /**
     * 批量作废
     *
     * @param ids ids
     */
    @Transactional
    public void batchDestory(List<String> ids) {
        updateRefundConsumer.accept(ids, RefundStatus.TODO);
    }


    /**
     * 拒绝退款添加退款原因
     *
     * @param id id
     */
    @Transactional
    public void refuse(String id, String refuseReason) {
        RefundOrder refundOrder = refundOrderRepository.findById(id).orElse(null);
        if (Objects.isNull(refundOrder)) {
            return;
        }
        refundOrder.setRefuseReason(refuseReason);
        refundOrderRepository.saveAndFlush(refundOrder);
        updateRefundConsumer.accept(Lists.newArrayList(id), RefundStatus.REFUSE);
    }

    /**
     * 扭转拒绝退款2审核成功
     * @param id
     * @param refuseReason
     */
    @Transactional
    public void backRefuse(String id, String refuseReason) {
        RefundOrder refundOrder = refundOrderRepository.findById(id).orElse(null);
        if (Objects.isNull(refundOrder)) {
            return;
        }
        refundOrder.setRefuseReason(refuseReason);
        refundOrderRepository.saveAndFlush(refundOrder);
        updateRefundConsumer.accept(Lists.newArrayList(id), RefundStatus.TODO);
    }


    /**
     * 合计退款金额
     *
     * @return BigDecimal
     */
    public BigDecimal sumReturnPrice(RefundOrderRequest refundOrderRequest) {
        //模糊匹配会员/商户名称，不符合条件直接返回0
        if (!this.likeCustomerAndSupplierName(refundOrderRequest)) {
            return BigDecimal.ZERO;
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);

        Root<RefundOrder> root = query.from(RefundOrder.class);
        query.select(builder.sum(root.get("refundBill").get("actualReturnPrice")));
        query.where(buildWhere(refundOrderRequest, root, query, builder));

        return entityManager.createQuery(query).getSingleResult();
    }

    /**
     * 修改退款单
     */
    private BiConsumer<List<String>, RefundStatus> updateRefundConsumer = (ids, refundStatus) -> {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        refundOrderRepository.updateRefundOrderStatus(refundStatus, ids);
    };

    /**
     * 替代关联查询-模糊商家名称、模糊会员名称，以并且关系的判断
     *
     * @param refundOrderRequest
     * @return true:有符合条件的数据,false:没有符合条件的数据
     */
    private boolean likeCustomerAndSupplierName(final RefundOrderRequest refundOrderRequest) {
        boolean supplierLike = true;
        //商家名称
        if (StringUtils.isNotBlank(refundOrderRequest.getSupplierName())) {
            CompanyListRequest request = CompanyListRequest.builder()
                    .supplierName(refundOrderRequest.getSupplierName())
                    .build();
            refundOrderRequest.setCompanyInfoIds(customerCommonService.listCompanyInfoIdsByCondition(request));
            if (CollectionUtils.isEmpty(refundOrderRequest.getCompanyInfoIds())) {
                supplierLike = false;
            }
        }
        //模糊会员名称
        boolean customerLike = true;
        if (StringUtils.isNotBlank(refundOrderRequest.getCustomerName())) {
            CustomerDetailListByConditionRequest request = CustomerDetailListByConditionRequest.builder().customerName
                    (refundOrderRequest.getCustomerName()).build();
            refundOrderRequest.setCustomerDetailIds(customerCommonService.listCustomerDetailIdsByCondition(request));
            if (CollectionUtils.isEmpty(refundOrderRequest.getCustomerDetailIds())) {
                customerLike = false;
            }
        }
        return supplierLike & customerLike;
    }

    private Specification<RefundOrder> findByRequest(final RefundOrderRequest refundOrderRequest) {
        return (Root<RefundOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> buildWhere(refundOrderRequest,
                root, query, cb);
    }

    /**
     * 构造列表查询的where条件
     *
     * @param refundOrderRequest request
     * @param root               root
     * @param query              query
     * @param cb                 cb
     * @return Predicates
     */
    private Predicate buildWhere(RefundOrderRequest refundOrderRequest, Root<RefundOrder> root,
                                 CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Join<RefundOrder, RefundBill> refundOrderRefundBillJoin = root.join("refundBill", JoinType.LEFT);
        refundOrderRefundBillJoin.on(cb.equal(refundOrderRefundBillJoin.get("delFlag"), DeleteFlag.NO));


        if (!StringUtils.isEmpty(refundOrderRequest.getAccountId())) {
            predicates.add(cb.equal(refundOrderRefundBillJoin.get("offlineAccountId"),
                    refundOrderRequest.getAccountId()));
        }

        if (!StringUtils.isEmpty(refundOrderRequest.getReturnOrderCode()) && !StringUtils.isEmpty(refundOrderRequest.getReturnOrderCode().trim())) {
            predicates.add(cb.like(root.get("returnOrderCode"), buildLike(refundOrderRequest.getReturnOrderCode())));
        }

        if (CollectionUtils.isNotEmpty(refundOrderRequest.getCompanyInfoIds())) {
            predicates.add(root.get("supplierId").in(refundOrderRequest.getCompanyInfoIds()));
        }

        if (CollectionUtils.isNotEmpty(refundOrderRequest.getCustomerDetailIds())) {
            predicates.add(root.get("customerDetailId").in(refundOrderRequest.getCustomerDetailIds()));
        }

        if (!CollectionUtils.isEmpty(refundOrderRequest.getReturnOrderCodes())) {
            Path path = root.get("returnOrderCode");
            CriteriaBuilder.In in = cb.in(path);
            for (String returnOrderCode : refundOrderRequest.getReturnOrderCodes()) {
                in.value(returnOrderCode);
            }
            predicates.add(in);
        }

        if (!CollectionUtils.isEmpty(refundOrderRequest.getRefundIds())) {
            Path path = root.get("refundId");
            CriteriaBuilder.In in = cb.in(path);
            for (String refundId : refundOrderRequest.getRefundIds()) {
                in.value(refundId);
            }
            predicates.add(in);
        }

        if (!StringUtils.isEmpty(refundOrderRequest.getRefundBillCode()) && !StringUtils.isEmpty(refundOrderRequest.getRefundBillCode().trim())) {
            predicates.add(cb.like(refundOrderRefundBillJoin.get("refundBillCode"),
                    buildLike(refundOrderRequest.getRefundBillCode())));
        }

        if (Objects.nonNull(refundOrderRequest.getPayChannelId())) {
            predicates.add(cb.equal(refundOrderRefundBillJoin.get("payChannelId"),
                    refundOrderRequest.getPayChannelId()));
        }

        if (Objects.nonNull(refundOrderRequest.getPayType())) {
            predicates.add(cb.equal(root.get("payType"), refundOrderRequest.getPayType()));
        }

        //待商家退款，拒绝退款的订单平台不应该看到
        predicates.add(cb.notEqual(root.get("refundStatus"), RefundStatus.TODO.toValue()));
        predicates.add(cb.notEqual(root.get("refundStatus"), RefundStatus.REFUSE.toValue()));
        if (Objects.nonNull(refundOrderRequest.getRefundStatus())) {
            predicates.add(cb.equal(root.get("refundStatus"), refundOrderRequest.getRefundStatus()));
        }

        //收款开始时间
        if (!StringUtils.isEmpty(refundOrderRequest.getBeginTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            predicates.add(cb.greaterThanOrEqualTo(refundOrderRefundBillJoin.get("createTime"),
                    LocalDateTime.of(LocalDate
                            .parse(refundOrderRequest.getBeginTime(), formatter), LocalTime.MIN)));
        }

        //收款
        if (!StringUtils.isEmpty(refundOrderRequest.getEndTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            predicates.add(cb.lessThan(refundOrderRefundBillJoin.get("createTime"),
                    LocalDateTime.of(LocalDate
                            .parse(refundOrderRequest.getEndTime(), formatter), LocalTime.MIN).plusDays(1)));
        }

        //删除条件
        predicates.add(cb.equal(root.get("delFlag"), DeleteFlag.NO));

        query.orderBy(cb.desc(root.get("createTime")));

        return cb.and(predicates.toArray(new Predicate[]{}));
    }

    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }


    /**
     * 解析收款账号
     *
     * @param refundOrder refundOrder
     * @return string
     */
    private String parseAccount(RefundOrder refundOrder) {
        StringBuilder accountName = new StringBuilder();
        if (PayType.OFFLINE.equals(refundOrder.getPayType()) && Objects.nonNull(refundOrder.getRefundBill().getOfflineAccountId())) {
            OfflineAccountGetByIdResponse offlineAccount = offlineQueryProvider.getById(new OfflineAccountGetByIdRequest
                    (refundOrder
                            .getRefundBill()
                            .getOfflineAccountId())).getContext();

            if (offlineAccount.getAccountId() != null) {
                log.info("解析收款账号offlineAccount: {}", offlineAccount);
                Integer length = offlineAccount.getAccountName().length();
                accountName.append(offlineAccount.getBankName())
                        .append(" ").append(StringUtils.isNotEmpty(offlineAccount.getBankNo()) ?
                        getDexAccount(offlineAccount.getBankNo()) : "");
            }
        }
        return accountName.toString();
    }

    /**
     * 更新退款单的备注字段
     *
     * @param refundId      id
     * @param refundComment comment
     */
    void updateRefundOrderReason(String refundId, String refundComment) {
        refundOrderRepository.updateRefundOrderReason(refundId, refundComment);
    }

    /**
     * 根据退单编号查询退款单
     *
     * @param returnOrderCode 退单编号
     * @return 退款单信息
     */
    public RefundOrder getRefundOrderByReturnOrderNo(String returnOrderCode) {
        return refundOrderRepository.findAllByReturnOrderCodeAndDelFlag(returnOrderCode, DeleteFlag.NO).orElseGet(() -> null);
    }

    /**
     * 返回掩码后的字符串
     *
     * @param bankNo
     * @return
     */
    public String getDexAccount(String bankNo) {
        String middle = "**********";
        if (bankNo.length() > 4) {
            if (bankNo.length() <= 8) {
                return middle;
            } else {
                bankNo = bankNo.substring(0, 4) + middle + bankNo.substring(bankNo.length() - 4);
            }
        } else {
            return middle;
        }
        return bankNo;
    }

    @Autowired
    private CustomerFundsProvider customerFundsProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private PayQueryProvider payQueryProvider;

    /**
     * 自动退款
     *
     * @param tradeList
     * @param returnOrderList
     * @param refundOrderList
     * @param operator
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Object> autoRefund(List<Trade> tradeList, List<ReturnOrder> returnOrderList,
                                   List<RefundOrder> refundOrderList, Operator operator) {
        List<Object> rsultObject = new ArrayList<>();
        RefundRequest refundRequest = new RefundRequest();
        Map<String, RefundOrder> refundOrderMap =
                refundOrderList.stream().collect(Collectors.toMap(RefundOrder::getReturnOrderCode,
                        refundOrder -> refundOrder));
        Map<String, Trade> tradeMap = tradeList.stream().collect(Collectors.toMap(Trade::getId, trade -> trade));
        for (ReturnOrder returnOrder : returnOrderList) {
            RefundOrder refundOrder = refundOrderMap.get(returnOrder.getId());
            //拼团订单-非0元订单退商品总金额
            Trade trade = tradeMap.get(returnOrder.getTid());
            refundRequest.setRefundBusinessId(returnOrder.getId());
            // 拼接描述信息
            String body = trade.getTradeItems().get(0).getSkuName() + " " + (trade.getTradeItems().get(0).getSpecDetails
                    () == null ? "" : trade.getTradeItems().get(0).getSpecDetails());
            if (trade.getTradeItems().size() > 1) {
                body = body + " 等多件商品";
            }
            refundRequest.setDescription(body);
            refundRequest.setClientIp("127.0.0.1");
            refundRequest.setStoreId(Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)?-2L:  Constants.BOSS_DEFAULT_STORE_ID);
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                    && trade.getBookingType() == BookingType.EARNEST_MONEY && StringUtils.isNotEmpty(trade.getTailOrderNo())) {
                BigDecimal needBackAmount = returnOrder.getReturnPrice().getApplyStatus() ? returnOrder.getReturnPrice()
                        .getApplyPrice() : refundOrder.getReturnPrice();
                if (needBackAmount.compareTo(BigDecimal.ZERO) == 0) {
                    refundRequest.setAmount(BigDecimal.ZERO);
                    refundRequest.setBusinessId(trade.getId());
                    refundBooking(refundRequest, returnOrder, refundOrder, operator, trade);
                    continue;
                }
                ReturnPrice returnPrice = returnOrder.getReturnPrice();
                // 应退定金
                BigDecimal earnestPrice = returnPrice.getEarnestPrice();
                // 应退尾款
                BigDecimal tailPrice = returnPrice.getTailPrice();
                if (earnestPrice.compareTo(BigDecimal.ZERO) >= 0) {
                    refundRequest.setAmount(earnestPrice);
                    refundRequest.setBusinessId(trade.getId());
                    refundBooking(refundRequest, returnOrder, refundOrder, operator, trade);
                }
                if (tailPrice.compareTo(BigDecimal.ZERO) >= 0) {
                    // 两笔交易时需两个单号处理
                    refundRequest.setRefundBusinessId(returnOrder.getBusinessTailId());
                    RefundOrder refund = this.findRefundOrderByReturnOrderNo(returnOrder.getBusinessTailId());
                    refund.setRefundChannel(RefundChannel.TAIL);
                    refundRequest.setAmount(tailPrice);
                    refundRequest.setBusinessId(trade.getTailOrderNo());
                    refundBooking(refundRequest, returnOrder, refund, operator, trade);
                }
            } else {
                refundRequest.setAmount(returnOrder.getReturnPrice().getApplyStatus() ? returnOrder.getReturnPrice()
                        .getApplyPrice() : refundOrder.getReturnPrice());
                refundRequest.setBusinessId(trade.getPayInfo().isMergePay() ? trade.getParentId() : trade.getId());
                Object object;
                try {
                    // 退款金额等于0 直接添加流水 修改退单状态
                    if (refundRequest.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                        returnOrderService.refundOnline(returnOrder, refundOrder, operator);
                    } else if(Objects.equals(trade.getChannelType(),ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())){
                        //视频号订单直接调同意退款接口
                        returnOrderService.addWxAfterSale(returnOrder,null, WxAfterSaleOperateType.REFUND.getIndex(), "运营退款");
                    }else {
                        BigDecimal totalPrice =
                                payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest(refundRequest.getBusinessId()))
                                        .getContext().getPracticalPrice();
                        refundRequest.setTotalPrice(totalPrice);
                        // 调用网关退款，退款公用接口
                        object = payProvider.refund(refundRequest).getContext().getObject();
                        //支付宝退款没有回调方法，故支付宝的交易流水在此添加
                        if (object != null) {
                            Map result = (Map) object;
                            if (result.containsKey("payType") && ("ALIPAY").equals(result.get("payType"))) {
                                returnOrderService.refundOnline(returnOrder, refundOrder, operator);
                            }
                            //余额退款不需要回调
                            if (result.containsKey("payType") && ("BALANCE").equals(result.get("payType"))) {
                                returnOrderService.refundOnline(returnOrder, refundOrder, operator);
                                customerFundsProvider.addAmount(CustomerFundsAddAmountRequest.builder()
                                        .amount(refundRequest.getAmount())
                                        .customerId(trade.getBuyer().getId())
                                        .businessId(trade.getId())
                                        .build());

                            }
                        }
                        rsultObject.add(object);
                    }
                } catch (SbcRuntimeException e) {
                    // 已退款 更新退单状态
                    if (e.getErrorCode() != null && e.getErrorCode().equals("K-100104")) {
                        returnOrderService.refundOnline(returnOrder, refundOrder, operator);
                    } else if (e.getErrorCode() != null && (e.getErrorCode().equals("K-100211") || e.getErrorCode()
                            .equals("K-100212"))) {
                        //K-100211、k-100212编码异常是支付宝、微信自定义异常。表示退款异常，详细信息见异常信息
                        RefundOrderRefundRequest refundOrderRefundRequest = new RefundOrderRefundRequest();
                        refundOrderRefundRequest.setRid(returnOrder.getId());
                        refundOrderRefundRequest.setFailedReason(e.getResult());
                        refundOrderRefundRequest.setOperator(operator);
                        returnOrderService.refundFailed(refundOrderRefundRequest);
                    }
                    log.error("refund error,", e);
                    //throw e;
                }
            }
        }
        return rsultObject;
    }

    @Transactional
    @GlobalTransactional
    public void refundBooking(RefundRequest refundRequest, ReturnOrder returnOrder, RefundOrder refundOrder, Operator operator, Trade trade) {
        Object object;
        try {
            // 退款金额等于0 直接添加流水 修改退单状态
            if (refundRequest.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                returnOrderService.refundOnline(returnOrder, refundOrder, operator);
            } else {
                BigDecimal totalPrice =
                        payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest(refundRequest.getBusinessId()))
                                .getContext().getPracticalPrice();
                refundRequest.setTotalPrice(totalPrice);
                // 调用网关退款，退款公用接口
                object = payProvider.refund(refundRequest).getContext().getObject();
                //支付宝退款没有回调方法，故支付宝的交易流水在此添加
                if (object != null) {
                    Map result = (Map) object;
                    if (result.containsKey("payType") && ("ALIPAY").equals(result.get("payType"))) {
                        returnOrderService.refundOnline(returnOrder, refundOrder, operator);
                    }
                    //余额退款不需要回调
                    if (result.containsKey("payType") && ("BALANCE").equals(result.get("payType"))) {
                        returnOrderService.refundOnline(returnOrder, refundOrder, operator);
                        customerFundsProvider.addAmount(CustomerFundsAddAmountRequest.builder()
                                .amount(refundRequest.getAmount())
                                .customerId(trade.getBuyer().getId())
                                .businessId(trade.getId())
                                .build());

                    }
                }
            }
        } catch (SbcRuntimeException e) {
            // 已退款 更新退单状态
            if (e.getErrorCode() != null && e.getErrorCode().equals("K-100104")) {
                returnOrderService.refundOnline(returnOrder, refundOrder, operator);
            } else if (e.getErrorCode() != null && (e.getErrorCode().equals("K-100211") || e.getErrorCode()
                    .equals("K-100212"))) {
                //K-100211、k-100212编码异常是支付宝、微信自定义异常。表示退款异常，详细信息见异常信息
                RefundOrderRefundRequest refundOrderRefundRequest = new RefundOrderRefundRequest();
                refundOrderRefundRequest.setRid(returnOrder.getId());
                refundOrderRefundRequest.setFailedReason(e.getResult());
                refundOrderRefundRequest.setOperator(operator);
                returnOrderService.refundFailed(refundOrderRefundRequest);
            }
            log.error("refund error,", e);
            //throw e;
        }
    }


    /**
     * 查询对账数据
     */
    public RefundOrderAccountRecordResponse getRefundOrderAccountRecord(RefundOrderAccountRecordRequest recordRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_TIME_1);
        LocalDateTime beginTime = LocalDateTime.parse(recordRequest.getBeginTime(),formatter);
        LocalDateTime endTime = LocalDateTime.parse(recordRequest.getEndTime(),formatter);

        List<Criteria> criterias = new ArrayList<>();
        //criterias.add(Criteria.where("supplier.storeId").is(recordRequest.getCompanyInfoId()));
        criterias.add(Criteria.where("returnFlowState").is(ReturnFlowState.COMPLETED.getStateId()));
        criterias.add(Criteria.where("finishTime").gte(beginTime));
        criterias.add(Criteria.where("finishTime").lt(endTime));
        criterias.add(Criteria.where("returnPoints.actualPoints").exists(true));

        Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        Query query = new Query(newCriteria);

        List<ReturnOrder> trades = mongoTemplate.find(query, ReturnOrder.class);

        log.info("===========查询查询条件：{}===============",query);

        log.info("===========查询查询条件出来的数据：{}===============",trades);

        List<ReturnPoints> tradePrices=trades.stream().map(ReturnOrder::getReturnPoints).collect(Collectors.toList());

        //计算积分的总和
        Long points=tradePrices.stream().mapToLong(ReturnPoints::getActualPoints).sum();

        BigDecimal bigDecimal=new BigDecimal(points);
        //计算积分金额的总和
        BigDecimal pointsPrice =bigDecimal.divide(new BigDecimal(100));

        log.info("===========查询对账积分：{}===============",points);
        log.info("===========查询对账积分金额：{}===============",pointsPrice);

        return RefundOrderAccountRecordResponse.builder().points(points).pointsPrice(pointsPrice).build();
    }



}
