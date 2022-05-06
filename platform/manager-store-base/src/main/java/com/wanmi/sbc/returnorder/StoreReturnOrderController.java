package com.wanmi.sbc.returnorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.linkedmall.model.v20180116.QueryRefundApplicationDetailResponse;
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
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
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
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
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
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderProviderTradeRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnOrderNoResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderAddResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.RefundBillDTO;
import com.wanmi.sbc.order.bean.dto.ReturnCustomerAccountDTO;
import com.wanmi.sbc.order.bean.dto.ReturnItemDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.returnorder.request.ReturnOfflineRefundRequest;
import com.wanmi.sbc.returnorder.service.AbstractCRMService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private Map<String, AbstractCRMService> abstractCRMServiceMap;

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
     * 创建退单 服务端退单 duanlsh
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
        if (CollectionUtils.isEmpty(returnOrder.getReturnItems())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //商品数量不能有为空的
        if (returnOrder.getReturnItems().stream().anyMatch(tt -> tt.getNum() == null || tt.getNum() <= 0)
                && returnOrder.getReturnReason() != ReturnReason.PRICE_DIFF
                && returnOrder.getReturnReason() != ReturnReason.PRICE_DELIVERY) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        returnOrder.setReturnType(returnOrder.getReturnWay() == ReturnWay.OTHER ? ReturnType.REFUND : ReturnType.RETURN);
        returnOrder.setCompany(CompanyDTO.builder().companyInfoId(companyInfo.getCompanyInfoId())
                .companyCode(companyInfo.getCompanyCode()).supplierName(companyInfo.getSupplierName())
                .storeId(commonUtil.getStoreId()).storeName(store.getStoreName()).companyType(store.getCompanyType()).build());
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(returnOrder.getTid()).build()).getContext().getTradeVO();
//        //提示客服去收货
//        if (trade.getTradeState().getFlowState() == FlowState.DELIVERED) {
//            throw new SbcRuntimeException("K-050413");
//        }
        //判断当前申请的退单中包含退单
        Map<String, ReturnItemDTO> returnItemDTOMap =
                returnOrder.getReturnItems().stream().collect(Collectors.toMap(ReturnItemDTO::getSkuId, Function.identity(), (k1, k2) -> k1));
        for (TradeItemVO tradeItemParam : trade.getTradeItems()) {
            ReturnItemDTO returnItemParam = returnItemDTOMap.get(tradeItemParam.getSkuId());
            if (returnItemParam == null) {
                continue;
            }
            if (DeliverStatus.PART_SHIPPED == tradeItemParam.getDeliverStatus()) {
                throw new SbcRuntimeException("K-050411");
            }
        }

        returnOrder.setChannelType(trade.getChannelType());
        returnOrder.setDistributorId(trade.getDistributorId());
        returnOrder.setInviteeId(trade.getInviteeId());
        returnOrder.setShopName(trade.getShopName());
        returnOrder.setDistributorName(trade.getDistributorName());
        returnOrder.setDistributeItems(trade.getDistributeItems());
        returnOrder.setTerminalSource(TerminalSource.SUPPLIER);

        BaseResponse<ReturnOrderAddResponse> response = null;
        if (Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
            for (ReturnItemDTO returnItemParam : returnOrder.getReturnItems()) {
//                if (returnItemParam.getApplyRealPrice() == null ||
//                        returnItemParam.getApplyRealPrice().compareTo(BigDecimal.ZERO) <= 0) {
//                    throw new SbcRuntimeException("K-050419");
//                }
                List<ReturnItemDTO> returnItemDTONewList = new ArrayList<>();
                returnItemDTONewList.add(returnItemParam);
                returnOrder.setReturnItems(returnItemDTONewList);
                response = returnOrderProvider.add(ReturnOrderAddRequest.builder().returnOrder(returnOrder).operator(operator).build());
                operateLogMQUtil.convertAndSend("订单", "代客退单", "退单号" + response.getContext().getReturnOrderId());
            }
        } else {
            response = returnOrderProvider.add(ReturnOrderAddRequest.builder().returnOrder(returnOrder).operator(operator).build());
            operateLogMQUtil.convertAndSend("订单", "代客退单", "退单号" + response.getContext().getReturnOrderId());
        }
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
     *
     * 审核退款接口
     *
     * 商家退款申请(修改退单价格新增流水)
     *
     * @param returnOrderId
     * @param request
     * @return
     */
    @ApiOperation(value = "商家退款申请(修改退单价格新增流水)")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/edit/price/{returnOrderId}", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse onlineEditPrice(@PathVariable("returnOrderId") String returnOrderId, @RequestBody @Valid ReturnOfflineRefundRequest request) {

        //验证一下
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(returnOrderId)
                .build()).getContext();
        BigDecimal refundPrice = returnOrder.getReturnPrice().getTotalPrice();
        if (refundPrice.compareTo(request.getActualReturnPrice()) == -1) {
            throw new SbcRuntimeException("K-050132", new Object[]{refundPrice});
        }
//        //只有不是审核状态，则会进入审核流程
//        if (returnOrder.getReturnFlowState() != ReturnFlowState.AUDIT) {
//            //1、走审核流程，
//            ReturnOrderAuditRequest returnOrderAuditRequest = new ReturnOrderAuditRequest();
//            returnOrderAuditRequest.setRid(returnOrderId);
//            returnOrderAuditRequest.setAddressId(null); // TODO
//            returnOrderAuditRequest.setOperator(commonUtil.getOperator());
//            returnOrderProvider.audit(returnOrderAuditRequest);
//        }

//        //2、管易云拦截
//        if (request.getHasInvokeGuanYiYun()) {
//            boolean flag = true;
//            //管易云
//            if (Objects.equals(returnOrder.getProviderId(),String.valueOf(defaultProviderId))) {
//                abstractCRMServiceMap.get("guanYiYunService").interceptorErpDeliverStatus(returnOrder, flag);
//            } else {
//                //博库
//                abstractCRMServiceMap.get("boKuService").interceptorErpDeliverStatus(returnOrder, flag);
//            }
//        }

        //3、退款
        log.info("StoreReturnOrderController onlineEditPrice returnOrderId:{} returnOrder:{}", returnOrder.getId(), JSON.toJSONString(returnOrder));
        return returnOrderProvider.onlineModifyPrice(ReturnOrderOnlineModifyPriceRequest.builder()
                .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                .refundComment(request.getRefundComment())
                .actualReturnPrice(request.getActualReturnPrice())
                .actualReturnPoints(request.getActualReturnPoints())
                .actualReturnKnowledge(request.getActualReturnKnowledge())
                .operator(commonUtil.getOperator()).build());
    }


    /**
     * 商家操作退款校验erp订单是否发货  duanlsh  当前管易云和博库对应的取消订单 作废，改成到
     * flag:true->检查订单中是否包含电子卡券或虚拟商品
     * flag:false->进行退款操作
     * @param rid
     * @return
     */
    @ApiOperation(value = "商家操作退款校验erp订单是否发货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/checkErpDeliverStatus/{rid}/{flag}", method = RequestMethod.GET)
    public BaseResponse checkErpDeliverStatus(@PathVariable String rid,@PathVariable Boolean flag){
        //获取退单信息
        ReturnOrderVO returnOrderVO = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build()).getContext();
        if (returnOrderVO == null) {
            //退单不存在
            throw new SbcRuntimeException("K-050003");
        }
        //管易云
        if (Objects.equals(returnOrderVO.getProviderId(),String.valueOf(defaultProviderId))) {
            abstractCRMServiceMap.get("guanYiYunService").interceptorErpDeliverStatus(returnOrderVO, flag);
        } else {
            //博库
            abstractCRMServiceMap.get("boKuService").interceptorErpDeliverStatus(returnOrderVO, flag);
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
        //根据订单id获取
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
            log.info("verifyIsReturnable tradeId:{} 当前发货状态是:{} 如果为全部发货则需要校验时间信息，如果非全部发货则不需要校验", tid, trade.getTradeState().getDeliverStatus());
            if (trade.getTradeState().getDeliverStatus() != DeliverStatus.SHIPPED) {
                return;
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
//
//    public void refundTrade(RefundTradeRequest refundTradeRequest){
//        if(!Objects.equals(refundTradeRequest.getProviderId(),defaultProviderId)) {
//            CancelOrderRequest request = new CancelOrderRequest();
//            request.setPid(refundTradeRequest.getTid());
//            bizSupplierClient.cancelOrder(request);
//            return;
//        }
//        guanyierpProvider.RefundTrade(refundTradeRequest);
//    }
//
//    private BaseResponse<DeliveryStatusResponse> getDeliveryStatus(DeliveryQueryRequest deliveryQueryRequest){
//        if(!Objects.equals(deliveryQueryRequest.getProviderId(),defaultProviderId)) {
//             return bizSupplierClient.getDeliveryStatus(deliveryQueryRequest.getTid());
//        }
//        return guanyierpProvider.getDeliveryStatus(deliveryQueryRequest);
//    }


    @PostMapping("/list-return-provider-trade")
    public BaseResponse findReturnOrderInfo(@RequestBody @Validated ReturnOrderProviderTradeRequest request) {
        return returnOrderProvider.listReturnProviderTrade(request);
    }

}
