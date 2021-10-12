package com.wanmi.sbc.returnorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.client.BizSupplierClient;
import com.wanmi.sbc.client.CancelOrderRequest;
import com.wanmi.sbc.client.CancelOrderResponse;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.erp.api.request.RefundTradeRequest;
import com.wanmi.sbc.erp.api.request.TradeQueryRequest;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import com.wanmi.sbc.erp.api.response.QueryTradeResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderNoRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderOfflineRefundForSupplierRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderOnlineModifyPriceRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderPageRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderQueryRefundPriceRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnOrderNoResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderAddResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.RefundBillDTO;
import com.wanmi.sbc.order.bean.dto.ReturnCustomerAccountDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.vo.DeliverCalendarVO;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeCycleBuyInfoVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.returnorder.request.ReturnOfflineRefundRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 退货
 * Created by sunkun on 2017/11/23.
 */
@Api(tags = "StoreReturnOrderController", description = "退货服务API")
@RestController
@RequestMapping("/return")
@Validated
@Slf4j
public class StoreReturnOrderController {

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Resource
    private StoreQueryProvider storeQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Resource
    private PayOrderQueryProvider payOrderQueryProvider;

    @Resource
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private GuanyierpProvider guanyierpProvider;

    @Value("${default.providerId}")
    private Long defaultProviderId;

    @Autowired
    private BizSupplierClient bizSupplierClient;


    /**
     * 分页查询 from ES
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询 from ES")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<ReturnOrderVO>> page(@RequestBody ReturnOrderPageRequest request) {
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        MicroServicePage<ReturnOrderVO> page = returnOrderQueryProvider.page(request).getContext().getReturnOrderPage();
        page.getContent().forEach(returnOrder -> {
            RefundOrderByReturnOrderNoResponse refundOrderByReturnCodeResponse = refundOrderQueryProvider.getByReturnOrderNo(new RefundOrderByReturnOrderNoRequest(returnOrder.getId())).getContext();
            if (Objects.nonNull(refundOrderByReturnCodeResponse)) {
                returnOrder.setRefundStatus(refundOrderByReturnCodeResponse.getRefundStatus());
            }
        });
        return BaseResponse.success(page);
    }

    /**
     * 创建退单
     *
     * @param returnOrder
     * @return
     */
    @ApiOperation(value = "创建退单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse<ReturnOrderAddResponse> create(@RequestBody ReturnOrderDTO returnOrder) {
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if (Objects.isNull(companyInfo)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Operator operator = commonUtil.getOperator();
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(commonUtil.getStoreId())
        ).getContext().getStoreVO();
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        returnOrder.setCompany(CompanyDTO.builder().companyInfoId(companyInfo.getCompanyInfoId())
                .companyCode(companyInfo.getCompanyCode()).supplierName(companyInfo.getSupplierName())
                .storeId(commonUtil.getStoreId()).storeName(store.getStoreName()).companyType(store.getCompanyType()).build());
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(returnOrder.getTid()).build()).getContext().getTradeVO();
        returnOrder.setChannelType(trade.getChannelType());
        returnOrder.setDistributorId(trade.getDistributorId());
        returnOrder.setInviteeId(trade.getInviteeId());
        returnOrder.setShopName(trade.getShopName());
        returnOrder.setDistributorName(trade.getDistributorName());
        returnOrder.setDistributeItems(trade.getDistributeItems());
        returnOrder.setTerminalSource(TerminalSource.SUPPLIER);
        BaseResponse<ReturnOrderAddResponse> response = returnOrderProvider.add(
                ReturnOrderAddRequest.builder().returnOrder(returnOrder).operator(operator).build());
        operateLogMQUtil.convertAndSend(
                "订单", "代客退单", "退单号" + response.getContext().getReturnOrderId());
        return response;
    }


    /**
     * 线下退款
     *
     * @param rid
     * @param request
     * @return
     */
    @ApiOperation(value = "线下退款")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/refund/{rid}/offline", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse refundOffline(@PathVariable String rid,
                                      @RequestBody @Valid ReturnOfflineRefundRequest request) {
        //客户账号
        ReturnCustomerAccountDTO customerAccount = null;
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                .build()).getContext();
        if (returnOrder.getReturnPrice().getTotalPrice().compareTo(request.getActualReturnPrice()) == -1) {
            throw new SbcRuntimeException("K-050132", new Object[]{returnOrder.getReturnPrice().getTotalPrice()});
        }
        if (Objects.equals(request.getCustomerAccountId(), "0")) {
            customerAccount = new ReturnCustomerAccountDTO();
            customerAccount.setCustomerAccountName(request.getCustomerAccountName());
            customerAccount.setCustomerBankName(request.getCustomerBankName());
            customerAccount.setCustomerAccountNo(request.getCustomerAccountNo());
            customerAccount.setCustomerId(request.getCustomerId());
        }
        //退款流水
        RefundBillDTO refundBill = new RefundBillDTO();
        refundBill.setActualReturnPrice(request.getActualReturnPrice());
        refundBill.setActualReturnPoints(request.getActualReturnPoints());
        refundBill.setRefundComment(request.getRefundComment());
        // 客户账号
        refundBill.setCustomerAccountId(request.getCustomerAccountId());
        refundBill.setCreateTime(StringUtils.isNotEmpty(request.getCreateTime()) ? DateUtil.parseDate(request.getCreateTime()) :
                LocalDateTime.now());
        return returnOrderProvider.offlineRefundForSupplier(ReturnOrderOfflineRefundForSupplierRequest.builder().rid(rid)
                .customerAccount(customerAccount).refundBill(refundBill).operator(commonUtil.getOperator()).build());
    }

    /**
     * 商家退款申请(修改退单价格新增流水)
     *
     * @param rid
     * @param request
     * @return
     */
    @ApiOperation(value = "商家退款申请(修改退单价格新增流水)")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/edit/price/{rid}", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse onlineEditPrice(@PathVariable String rid, @RequestBody @Valid ReturnOfflineRefundRequest request) {
        BigDecimal refundPrice = returnOrderQueryProvider.queryRefundPrice(ReturnOrderQueryRefundPriceRequest.builder()
                .rid(rid).build()).getContext().getRefundPrice();
        if (refundPrice.compareTo(request.getActualReturnPrice()) == -1) {
            throw new SbcRuntimeException("K-050132", new Object[]{refundPrice});
        }
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                .build()).getContext();

        return returnOrderProvider.onlineModifyPrice(ReturnOrderOnlineModifyPriceRequest.builder()
                .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                .refundComment(request.getRefundComment())
                .actualReturnPrice(request.getActualReturnPrice())
                .actualReturnPoints(request.getActualReturnPoints())
                .actualReturnKnowledge(request.getActualReturnKnowledge())
                .operator(commonUtil.getOperator()).build());
    }


    /**
     * 商家操作退款校验erp订单是否发货
     * flag:true->检查订单中是否包含电子卡券或虚拟商品
     * flag:false->进行退款操作
     * @param rid
     * @return
     */
    @ApiOperation(value = "商家操作退款校验erp订单是否发货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/checkErpDeliverStatus/{rid}/{flag}", method = RequestMethod.GET)
    public BaseResponse checkErpDeliverStatus(@PathVariable String rid,@PathVariable Boolean flag){
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                .build()).getContext();
        TradeGetByIdRequest tradeGetByIdRequest = TradeGetByIdRequest.builder().tid(returnOrder.getTid()).build();
        BaseResponse<TradeGetByIdResponse> tradeResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeVO tradeVO = tradeResponse.getContext().getTradeVO();
        if (CollectionUtils.isEmpty(tradeResponse.getContext().getTradeVO().getTradeVOList())){
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        //判断订单中是否包含虚拟商品和电子卡券，防止用户刷商品
        if (flag){
            List<TradeItemVO> tradeItemVOList = tradeVO.getGifts().stream().filter(tradeItemVO ->
                    tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_GOODS) || tradeItemVO.getGoodsType()
                            .equals(GoodsType.VIRTUAL_COUPON)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tradeItemVOList)){
                throw new SbcRuntimeException("K-050319");
            }
        }
        if (tradeVO.getCycleBuyFlag()) {
            List<DeliverCalendarVO> deliverCalendar=tradeVO.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()==CycleDeliverStatus.PUSHED).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deliverCalendar)) {
                Long providerId = tradeVO.getTradeItems().get(0).getProviderId();
                deliverCalendar.forEach(deliverCalendarVO -> {
                    //查询providerId查询订单的发货记录
                    DeliveryQueryRequest deliveryQueryRequest = DeliveryQueryRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).providerId(providerId).build();
                    BaseResponse<DeliveryStatusResponse> response = this.getDeliveryStatus(deliveryQueryRequest);
                    //已发货并且没有确认收货的订单无法退款
                    if(!tradeVO.getTradeState().getFlowState().equals(FlowState.VOID)
                            && !CollectionUtils.isEmpty(response.getContext().getDeliveryInfoVOList())){
                        response.getContext().getDeliveryInfoVOList().stream().forEach(deliveryInfoVO -> {
                            if (deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                                throw new SbcRuntimeException("K-050106");
                            }
                        });
                    }
                });
            }
        }else {
            //已发货并且没有确认收货的订单无法退款
            DeliveryQueryRequest deliveryQueryRequest = DeliveryQueryRequest.builder().tid(returnOrder.getPtid()).build();
            BaseResponse<DeliveryStatusResponse> response = this.getDeliveryStatus(deliveryQueryRequest);
            if(!tradeVO.getTradeState().getFlowState().equals(FlowState.VOID)
                    && !tradeVO.getTradeState().getFlowState().equals(FlowState.COMPLETED)
                    && !CollectionUtils.isEmpty(response.getContext().getDeliveryInfoVOList())){
                response.getContext().getDeliveryInfoVOList().stream().forEach(deliveryInfoVO -> {
                    if (deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                        throw new SbcRuntimeException("K-050106");
                    }
                });
            }
        }


        //通知erp系统停止发货,走系统退款逻辑
        if (tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED)){
            tradeResponse.getContext().getTradeVO().getTradeVOList().forEach(providerTradeVO -> {
                if(!Objects.equals(providerTradeVO.getTradeItems().get(0).getProviderId(),defaultProviderId)){
                    BaseResponse<CancelOrderResponse> response = bizSupplierClient.cancelOrder(CancelOrderRequest.builder().orderId(providerTradeVO.getDeliveryOrderId()).pid(providerTradeVO.getId()).build());
                    if(response == null || response.getContext() == null || Objects.equals(response.getContext().getStatus,1)){
                        throw new SbcRuntimeException("K-050106");
                    }
                    return;
                }
                //拦截主商品
                providerTradeVO.getTradeItems().forEach(tradeItemVO -> {
                    RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(providerTradeVO.getId()).oid(tradeItemVO.getOid()).build();
                    guanyierpProvider.RefundTrade(refundTradeRequest);
                });
                //拦截赠品
                if (!CollectionUtils.isEmpty(providerTradeVO.getGifts())){
                    providerTradeVO.getGifts().forEach(giftVO -> {
                        RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder()
                                .tid(providerTradeVO.getId())
                                .oid(giftVO.getOid()).build();
                        guanyierpProvider.RefundTrade(refundTradeRequest);
                    });
                }
            });
        }

        //拦截周期购订单
        tradeResponse.getContext().getTradeVO().getTradeVOList().forEach(providerTradeVO -> {
            if (providerTradeVO.getCycleBuyFlag()) {
                TradeCycleBuyInfoVO tradeCycleBuyInfo= providerTradeVO.getTradeCycleBuyInfo();
                List<DeliverCalendarVO> deliverCalendar=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deliverCalendar)) {
                    String  oid=providerTradeVO.getTradeItems().get(0).getOid();
                    Long providerId = providerTradeVO.getTradeItems().get(0).getProviderId();

                    deliverCalendar.forEach(deliverCalendarVO -> {
                        RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).oid(oid).providerId(providerId).deliveryOrderId("").build();
                        guanyierpProvider.RefundTrade(refundTradeRequest);
                        //获取订单期数，判断是否是第一期，周期购订单只有第一期才有赠品
                        String cyclNum= deliverCalendarVO.getErpTradeCode().substring(deliverCalendarVO.getErpTradeCode().length()-1);
                        if (CollectionUtils.isNotEmpty(providerTradeVO.getGifts()) && Objects.equals(cyclNum,"1")) {
                            providerTradeVO.getGifts().forEach(giftVO -> {
                                RefundTradeRequest refundRequest = RefundTradeRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).oid(giftVO.getOid()).build();
                                guanyierpProvider.RefundTrade(refundRequest);
                            });
                        }
                    });


//                    deliverCalendar.forEach(deliverCalendarVO -> {
//                        RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).oid(oid).providerId(providerId).build();
//                        this.refundTrade(refundTradeRequest);
//                        //获取订单期数，判断是否是第一期，周期购订单只有第一期才有赠品
//                        String cyclNum= deliverCalendarVO.getErpTradeCode().substring(deliverCalendarVO.getErpTradeCode().length()-1);
//                        if (CollectionUtils.isNotEmpty(providerTradeVO.getGifts()) && Objects.equals(cyclNum,"1")) {
//                            providerTradeVO.getGifts().forEach(giftVO -> {
//                                RefundTradeRequest refundRequest = RefundTradeRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).oid(giftVO.getOid()).providerId(providerId).build();
//                                this.refundTrade(refundRequest);
//                            });
//                        }
//                    });
                }
            }
        });

        if (returnOrder.getReturnType().equals(ReturnType.RETURN)) {
            log.info("=============组合商品退货退款拦截未发货的商品：{}==================",tradeVO.getId());
            List<ReturnItemVO> returnGoods = returnOrder.getReturnItems();
            List<ReturnItemVO> returnGifts = returnOrder.getReturnGifts();
            List<ReturnItemVO> totalReturnItemList =
                    Stream.of(returnGoods, returnGifts).flatMap(Collection::stream).collect(Collectors.toList());
            List<String> returnItemSkuIds = totalReturnItemList.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            List<TradeVO> providerTrades =tradeVO.getTradeVOList();
            providerTrades.forEach(providerTrade -> {
                List<TradeItemVO>  tradeItems= providerTrade.getTradeItems();
                List<TradeItemVO>  gifts= providerTrade.getGifts();
                List<TradeItemVO> totalTradeItemList =
                        Stream.of(gifts, tradeItems).flatMap(Collection::stream).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(totalTradeItemList)) {
                    totalTradeItemList.forEach(tradeItem -> {
                        if (Objects.nonNull(tradeItem.getCombinedCommodity()) && tradeItem.getCombinedCommodity() && returnItemSkuIds.contains(tradeItem.getSkuId())) {
                            RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(providerTrade.getId()).oid(tradeItem.getOid()).providerId(tradeItem.getProviderId()).build();
                            //如果是组合商品,查询ERP订单发货状态，ERP订单已发货就不需要走拦截
                            TradeQueryRequest tradeQueryRequest = TradeQueryRequest.builder().tid(providerTrade.getId()).flag(0).build();
                            BaseResponse<QueryTradeResponse> tradeInfoResponse= guanyierpProvider.getTradeInfo(tradeQueryRequest);
                            //默认查询七天内的订单,如果没有加过就再查询历史订单
                            if (StringUtils.isBlank(tradeInfoResponse.getContext().getPlatformCode())){
                                tradeQueryRequest.setFlag(1);
                                BaseResponse<QueryTradeResponse> historyTradeResponse = guanyierpProvider.getTradeInfo(tradeQueryRequest);
                                if (StringUtils.isNoneBlank(historyTradeResponse.getContext().getPlatformCode())) {
                                    if (DeliverStatus.NOT_YET_SHIPPED.equals(historyTradeResponse.getContext().getDeliveryState())
                                            || DeliverStatus.PART_SHIPPED.equals(historyTradeResponse.getContext().getDeliveryState())) {
                                        this.refundTrade(refundTradeRequest);
                                        log.info("=============组合商品退货退款拦截未发货的商品：{}==================", refundTradeRequest);
                                    }
                                }
                            }else {
                                if (DeliverStatus.NOT_YET_SHIPPED.equals(tradeInfoResponse.getContext().getDeliveryState())
                                        || DeliverStatus.PART_SHIPPED.equals(tradeInfoResponse.getContext().getDeliveryState())){
                                    this.refundTrade(refundTradeRequest);
                                    log.info("=============组合商品退货退款拦截未发货的商品：{}==================",refundTradeRequest);
                                }
                            }
                        }
                    });
                }
            });
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 是否可创建退单
     *
     * @return
     */
    @ApiOperation(value = "是否可创建退单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tid", value = "退单Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Boolean",
                    name = "isRefund", value = "是否可以退货", required = true)
    })
    @RequestMapping(value = "/returnable/{tid}/{isRefund}", method = RequestMethod.GET)
    public BaseResponse isReturnable(@PathVariable String tid, @PathVariable Boolean isRefund) {
        BaseResponse<FindPayOrderResponse> findPayOrderResponseBaseResponse = payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(tid).build());
        FindPayOrderResponse payOrder = findPayOrderResponseBaseResponse.getContext();
        if (Objects.isNull(payOrder) || Objects.isNull(payOrder.getPayOrderStatus()) || payOrder.getPayOrderStatus() != PayOrderStatus.PAYED) {
            throw new SbcRuntimeException("K-050105");
        }
        verifyIsReturnable(tid, isRefund);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 校验是否可退
     *
     * @param tid
     */
    private void verifyIsReturnable(String tid, boolean isRefund) {
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);

        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        if (!isRefund) {
            if (config.getStatus() == 0) {
                throw new SbcRuntimeException("K-050208");
            }
            JSONObject content = JSON.parseObject(config.getContext());
            Integer day = content.getObject("day", Integer.class);

            if (Objects.isNull(trade.getTradeState().getEndTime())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (trade.getTradeState().getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < LocalDateTime.now().minusDays(day).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
                throw new SbcRuntimeException("K-050208");
            }
        }
    }

    public void refundTrade(RefundTradeRequest refundTradeRequest){
        if(!Objects.equals(refundTradeRequest.getProviderId(),defaultProviderId)) {
            CancelOrderRequest request = new CancelOrderRequest();
            request.setPid(refundTradeRequest.getTid());
            bizSupplierClient.cancelOrder(request);
            return;
        }
        guanyierpProvider.RefundTrade(refundTradeRequest);
    }

    private BaseResponse<DeliveryStatusResponse> getDeliveryStatus(DeliveryQueryRequest deliveryQueryRequest){
        if(!Objects.equals(deliveryQueryRequest.getProviderId(),defaultProviderId)) {
             return bizSupplierClient.getDeliveryStatus(deliveryQueryRequest.getTid());
        }
        return guanyierpProvider.getDeliveryStatus(deliveryQueryRequest);
    }


}
