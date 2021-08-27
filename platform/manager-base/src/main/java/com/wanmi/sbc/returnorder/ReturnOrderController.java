package com.wanmi.sbc.returnorder;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.linkedmall.model.v20180116.InitApplyRefundResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryRefundApplicationDetailResponse;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsDetailProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdForEsResponse;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByCompanyIdRequest;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsRequest;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateQueryRequest;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcInitApplyRefundRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderProvider;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderResponseByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.returnorder.*;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnCodeResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderListReponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByIdResponse;
import com.wanmi.sbc.order.bean.dto.RefundOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnLogisticsDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.RefundResultByOrdercodeRequest;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.returnorder.convert.Remedy2ReturnOrder;
import com.wanmi.sbc.returnorder.request.RejectRequest;
import com.wanmi.sbc.returnorder.request.RemedyReturnRequest;
import com.wanmi.sbc.returnorder.request.ReturnExportRequest;
import com.wanmi.sbc.returnorder.request.ReturnRequest;
import com.wanmi.sbc.returnorder.service.ReturnExportService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @menu 退单相关
 * @tag feature_d_cps3
 * @status undone
 */
@Api(tags = "ReturnOrderController", description = "退货 Api")
@RestController
@RequestMapping("/return")
public class ReturnOrderController {

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private ReturnExportService returnExportService;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private RefundOrderProvider refundOrderProvider;

    @Autowired
    private GoodsTobeEvaluateSaveProvider goodsTobeEvaluateSaveProvider;

    @Autowired
    private StoreTobeEvaluateSaveProvider storeTobeEvaluateSaveProvider;

    @Autowired
    private CustomerFundsProvider customerFundsProvider;

    @Autowired
    private CustomerFundsDetailProvider customerFundsDetailProvider;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private EsCustomerFundsProvider esCustomerFundsProvider;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 导出文件名后的时间后缀
     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * @description 商家-运营查询退单
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查询退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/{rid}")
    public BaseResponse<ReturnOrderVO> findById(@PathVariable String rid) {
        ReturnOrderVO returnOrder = null;
        if(commonUtil.getOperator().getPlatform() == Platform.SUPPLIER) {
            returnOrder = checkOperatorByReturnOrder(rid);
        }else{
            returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                    .build()).getContext();
        }
        String accountName = employeeQueryProvider.getByCompanyId(
                EmployeeByCompanyIdRequest.builder().companyInfoId(returnOrder.getCompany().getCompanyInfoId()).build()
        ).getContext().getAccountName();
        returnOrder.getCompany().setAccountName(accountName);
        return BaseResponse.success(returnOrder);
    }

    /**
     * 导出退单
     *
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void exportByParams(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted));
        ReturnExportRequest returnExportRequest = JSON.parseObject(decrypted, ReturnExportRequest.class);

        Operator operator = commonUtil.getOperator();
        logger.debug("/return/export/params, employeeId=" + operator.getUserId());
        Platform platform = operator.getPlatform();
        if (platform == Platform.SUPPLIER) {
            returnExportRequest.setCompanyInfoId(Long.parseLong(operator.getAdminId()));
        }

        ReturnOrderByConditionRequest conditionRequest = new ReturnOrderByConditionRequest();
        KsBeanUtil.copyPropertiesThird(returnExportRequest, conditionRequest);
        List<ReturnOrderVO> returnOrders =
                returnOrderQueryProvider.listByCondition(conditionRequest).getContext().getReturnOrderList();

        String headerKey = "Content-Disposition";
        LocalDateTime dateTime = LocalDateTime.now();
        String fileName = String.format("批量导出退单_%s.xls", dateTime.format(formatter));
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("/return/export/params, fileName={}, error={}", fileName, e);
        }
        String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
        response.setHeader(headerKey, headerValue);
        try {

            returnExportService.export(returnOrders, response.getOutputStream(),
                    platform == Platform.PLATFORM);
            response.flushBuffer();
        } catch (IOException e) {
            throw new SbcRuntimeException(e);
        }
    }

    @ApiOperation(value = "退单方式查询")
    @RequestMapping(value = "/ways", method = RequestMethod.GET)
    public BaseResponse<List<ReturnWay>> findReturnWay() {
        return BaseResponse.success(returnOrderQueryProvider.listReturnWay().getContext().getReturnWayList());
    }

    @ApiOperation(value = "退单原因查询")
    @RequestMapping(value = "/reasons", method = RequestMethod.GET)
    public BaseResponse<List<ReturnReason>> findReturnReason() {
        return BaseResponse.success(returnOrderQueryProvider.listReturnReason().getContext().getReturnReasonList());
    }


    @ApiOperation(value = "linkedmall订单逆向渲染接口")
    @GetMapping("/findLinkedMallInitApplyRefundData")
    public BaseResponse<InitApplyRefundResponse.InitApplyRefundData> findLinkedMallInitApplyRefundData(SbcInitApplyRefundRequest sbcInitApplyRefundRequest) {
        return linkedMallReturnOrderQueryProvider.initApplyRefund(sbcInitApplyRefundRequest);
    }

    /**
     * 审核
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "审核")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "addressId", value = "退货收货地址Id", required = false)
    })
    @RequestMapping(value = {"/audit/{rid}", "/audit/{rid}/{addressId}"}, method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse audit(@PathVariable String rid, @PathVariable(value = "addressId", required = false) String addressId) {
        List<ReturnOrderVO> returnOrderVOList = returnOrderQueryProvider.listByCondition(
                ReturnOrderByConditionRequest.builder().rids(new String[]{rid}).build()).getContext().getReturnOrderList();
        if (CollectionUtils.isEmpty(returnOrderVOList)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ReturnOrderVO returnOrderVO = returnOrderVOList.get(0);
        //linkedMall订单，且未支付失败
        if ((!Boolean.TRUE.equals(returnOrderVO.getThirdPlatformPayErrorFlag()))
                && ThirdPlatformType.LINKED_MALL.equals(returnOrderVO.getThirdPlatformType())
                && CollectionUtils.isNotEmpty(returnOrderVO.getReturnItems())) {
            String subLmOrderId = returnOrderVO.getReturnItems().get(0).getThirdPlatformSubOrderId();
            if (StringUtils.isNotBlank(subLmOrderId)) {
                SbcQueryRefundApplicationDetailRequest detailRequest = new SbcQueryRefundApplicationDetailRequest();
                detailRequest.setBizUid(returnOrderVO.getBuyer().getId());
                detailRequest.setSubLmOrderId(subLmOrderId);
                QueryRefundApplicationDetailResponse.RefundApplicationDetail detail =
                        linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(detailRequest).getContext().getDetail();
                if (detail != null) {
                    if (!(Integer.valueOf(2).equals(detail.getDisputeStatus())
                            || Integer.valueOf(3).equals(detail.getDisputeStatus())
                            || Integer.valueOf(5).equals(detail.getDisputeStatus()))) {
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"linkedMall商家没有同意退款"});
                    }
                }
            }
        }

        return returnOrderProvider.audit(ReturnOrderAuditRequest.builder().rid(rid).addressId(addressId).operator(commonUtil.getOperator()).build());
    }

    /**
     * 批量审核
     *
     * @param returnRequest
     * @return
     */
    @ApiOperation(value = "批量审核")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> batchAudit(@RequestBody ReturnRequest returnRequest) {
        returnRequest.getRids().forEach(rid -> audit(rid, null));
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 校验退单退款状态
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "校验退单退款状态")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping("/verifyRefundStatus/{rid}")
    @GlobalTransactional
    public BaseResponse verifyRefundStatus(@PathVariable String rid) {
        ReturnOrderVO returnOrder;
        if(commonUtil.getOperator().getPlatform().equals(Platform.PLATFORM)) {
             returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                    .build()).getContext();
        }else{
             returnOrder = checkOperatorByReturnOrder(rid);
        }
        TradeStatus tradeStatus = payQueryProvider.getRefundResponseByOrdercode(new RefundResultByOrdercodeRequest
                (returnOrder.getTid(), returnOrder.getId())).getContext().getTradeStatus();
        if (tradeStatus != null) {
            if (tradeStatus == TradeStatus.PROCESSING) {
                throw new SbcRuntimeException("K-100105");
            } else if (tradeStatus == TradeStatus.SUCCEED) {
                RefundOrderByReturnCodeResponse refundOrder =
                        refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(rid)).getContext();
                Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("1").name("system")
                        .account("system").platform(Platform.BOSS).build();
                returnOrderProvider.onlineRefund(ReturnOrderOnlineRefundRequest.builder().operator(operator)
                        .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                        .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());
                throw new SbcRuntimeException("K-100104");
            }
        }
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "退单派送")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/deliver/{rid}", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse deliver(@PathVariable String rid, @RequestBody ReturnLogisticsDTO logistics) {
        checkOperatorByReturnOrder(rid);
        return returnOrderProvider.deliver(ReturnOrderDeliverRequest.builder().rid(rid).logistics(logistics)
                .operator(commonUtil.getOperator()).build());
    }

    /**
     * 收货
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "收货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/receive/{rid}", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse receive(@PathVariable String rid) {
        ReturnOrderVO returnOrder = checkOperatorByReturnOrder(rid);

        returnOrder.getReturnItems().forEach(goodsInfo -> {
            goodsTobeEvaluateSaveProvider.deleteByOrderAndSku(GoodsTobeEvaluateQueryRequest.builder()
                    .orderNo(returnOrder.getTid()).goodsInfoId(goodsInfo.getSkuId()).build());
        });

        //linkedMall订单，且未支付失败
        if ((!Boolean.TRUE.equals(returnOrder.getThirdPlatformPayErrorFlag()))
                && ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType())
                && CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
            String subLmOrderId = returnOrder.getReturnItems().get(0).getThirdPlatformSubOrderId();
            if (StringUtils.isNotBlank(subLmOrderId)) {
                SbcQueryRefundApplicationDetailRequest detailRequest = new SbcQueryRefundApplicationDetailRequest();
                detailRequest.setBizUid(returnOrder.getBuyer().getId());
                detailRequest.setSubLmOrderId(subLmOrderId);
                QueryRefundApplicationDetailResponse.RefundApplicationDetail detail =
                        linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(detailRequest).getContext().getDetail();
                if (detail != null) {
                    if (!(Integer.valueOf(5).equals(detail.getDisputeStatus())
                            || Integer.valueOf(11).equals(detail.getDisputeStatus()))) {
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"linkedMall商家没有同意收货"});
                    }
                }
            }
        }

        storeTobeEvaluateSaveProvider.deleteByOrderAndStoreId(StoreTobeEvaluateQueryRequest.builder()
                .storeId(returnOrder.getCompany().getStoreId()).orderNo(returnOrder.getTid()).build());
        return returnOrderProvider.receive(ReturnOrderReceiveRequest.builder().operator(commonUtil.getOperator())
                .rid(rid).build());
    }

    /**
     * 批量收货
     *
     * @param returnRequest
     * @return
     */
    @ApiOperation(value = "批量收货")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public BaseResponse batchReceive(@RequestBody ReturnRequest returnRequest) {
        returnRequest.getRids().forEach(this::receive);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "退单拒绝收货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/receive/{rid}/reject", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse rejectReceive(@PathVariable String rid, @RequestBody RejectRequest request) {
        List<ReturnOrderVO> returnOrderVOList = returnOrderQueryProvider.listByCondition(
                ReturnOrderByConditionRequest.builder().rids(new String[]{rid}).build()).getContext().getReturnOrderList();
        if (CollectionUtils.isEmpty(returnOrderVOList)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ReturnOrderVO returnOrder = returnOrderVOList.get(0);
        if(commonUtil.getOperator().getPlatform() == Platform.SUPPLIER) {
            if(!Objects.equals(commonUtil.getStoreId(), returnOrder.getCompany().getStoreId())){
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
        }
        //linkedMall订单，且未支付失败
        if ((!Boolean.TRUE.equals(returnOrder.getThirdPlatformPayErrorFlag()))
                && ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType())
                && CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
            String subLmOrderId = returnOrder.getReturnItems().get(0).getThirdPlatformSubOrderId();
            if (StringUtils.isNotBlank(subLmOrderId)) {
                SbcQueryRefundApplicationDetailRequest detailRequest = new SbcQueryRefundApplicationDetailRequest();
                detailRequest.setBizUid(returnOrder.getBuyer().getId());
                detailRequest.setSubLmOrderId(subLmOrderId);
                QueryRefundApplicationDetailResponse.RefundApplicationDetail detail =
                        linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(detailRequest).getContext().getDetail();
                if (detail != null) {
                    if (Integer.valueOf(4).equals(detail.getDisputeStatus())) {
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"linkedMall商家没有拒绝收货"});
                    }
                }
            }
        }

        returnOrderProvider.rejectReceive(ReturnOrderRejectReceiveRequest.builder().rid(rid).reason(request.getReason())
                .operator(commonUtil.getOperator()).build());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量拒绝收货
     *
     * @param returnRequest
     * @return
     */
    @ApiOperation(value = "批量拒绝收货")
    @RequestMapping(value = "/receive/reject", method = RequestMethod.POST)
    public BaseResponse rejectReceive(@RequestBody ReturnRequest returnRequest) {
        RejectRequest request = new RejectRequest();
        request.setReason(StringUtils.EMPTY);
        returnRequest.getRids().forEach(rid -> this.rejectReceive(rid, request));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 在线退款
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "在线退款")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/refund/{rid}/online", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse<Object> refundOnline(@PathVariable String rid) {
        Operator operator = commonUtil.getOperatorWithNull();
        BaseResponse<Object> res = returnOrderProvider.refundOnlineByTid(ReturnOrderOnlineRefundByTidRequest.builder().returnOrderCode(rid)
                .operator(operator).build());
        Object data = res.getContext();

        // es 同步会员资金
        BaseResponse<ReturnOrderByIdResponse> response = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build());
        if (Objects.nonNull(response.getContext())) {
            String customerId = response.getContext().getBuyer().getId();

            CustomerFundsByCustomerIdRequest byCustomerIdRequest = new CustomerFundsByCustomerIdRequest();
            byCustomerIdRequest.setCustomerId(customerId);
            BaseResponse<CustomerFundsByCustomerIdForEsResponse> baseResponse = customerFundsQueryProvider.getByCustomerIdForEs(byCustomerIdRequest);
            if (Objects.nonNull(baseResponse.getContext())) {
                //新增更改会员资金es
                esCustomerFundsProvider.initCustomerFunds(KsBeanUtil.convert(baseResponse.getContext(), EsCustomerFundsRequest.class));
            }
        }


        if (Objects.isNull(data)) {
            //无返回信息，追踪退单退款状态
            ReturnFlowState state = response.getContext().getReturnFlowState();
            if (state.equals(ReturnFlowState.REFUND_FAILED)) {
                return BaseResponse.FAILED();
            }
        }
        return res;
    }


    /**
     * @description 商家-运营根据订单id查询退单(过滤拒绝退款、拒绝收货、已作废)
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "根据订单id查询退单(过滤拒绝退款、拒绝收货、已作废)")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "退单Id", required = true)
    @RequestMapping(value = "/findByTid/{tid}", method = RequestMethod.GET)
    public BaseResponse<List<ReturnOrderVO>> findByTid(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        List<ReturnOrderVO> returnOrders = returnOrderQueryProvider.listByTid(ReturnOrderListByTidRequest.builder()
                .tid(tid).build()).getContext().getReturnOrderList();
        List<ReturnOrderVO> returnOrderVOList = returnOrders.stream().filter(o -> o.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                && o.getReturnFlowState() != ReturnFlowState.REJECT_RECEIVE
                && o.getReturnFlowState() != ReturnFlowState.VOID).collect(Collectors.toList());
        //如果是拆分的退单，有部分完成退款，则退款完成的退单也过滤掉（避免拆分的退单中有的被驳回，有的是退款完成，导致前端不能再次申请退款）
        if (CollectionUtils.isNotEmpty(returnOrders) && StringUtils.isNotBlank(returnOrders.get(0).getPtid())) {
            List<ReturnOrderVO> completedReturnOrderList
                    = returnOrders.stream().filter(o -> o.getReturnFlowState() == ReturnFlowState.COMPLETED).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(completedReturnOrderList) && completedReturnOrderList.size() < returnOrders.size()) {
                returnOrderVOList = returnOrderVOList.stream().filter(o -> o.getReturnFlowState() != ReturnFlowState.COMPLETED).collect(Collectors.toList());
            }
        }
        return BaseResponse.success(returnOrderVOList);
    }

    /**
     * 根据订单id查询已完成的退单
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "根据订单id查询已完成的退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "退单Id", required = true)
    @RequestMapping(value = "/findCompletedByTid/{tid}", method = RequestMethod.GET)
    public BaseResponse<List<ReturnOrderVO>> findCompletedByTid(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        List<ReturnOrderVO> returnOrders = returnOrderQueryProvider.listNotVoidByTid(ReturnOrderNotVoidByTidRequest
                .builder().tid(tid).build()).getContext().getReturnOrderList();
        return BaseResponse.success(returnOrders);
    }

    /**
     * 根据订单id查询全量退单
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "根据订单id查询全量退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "退单Id", required = true)
    @RequestMapping(value = "/find-all/{tid}", method = RequestMethod.GET)
    public BaseResponse<List<ReturnOrderVO>> findAllByTid(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        //判断订单是否计入了账期，如果计入了账期，不允许作废
        Boolean settled =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO().getHasBeanSettled();
        if (settled != null && settled) {
            throw new SbcRuntimeException("K-050131");
        }
        return BaseResponse.success(returnOrderQueryProvider.listByTid(ReturnOrderListByTidRequest.builder().tid(tid)
                .build()).getContext().getReturnOrderList());
    }

    /**
     * 拒绝收款
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "拒绝收款")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/refund/{rid}/reject", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse refundReject(@PathVariable String rid, @RequestBody RejectRequest request) {
        return returnOrderProvider.rejectRefund(ReturnOrderRejectRefundRequest.builder().operator(commonUtil.getOperator())
                .rid(rid).reason(request.getReason()).build());
    }

    /**
     * 批量拒绝退款
     *
     * @param returnRequest
     * @return
     */
    @ApiOperation(value = "批量拒绝退款")
    @RequestMapping(value = "/refund/reject", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> refundReject(@RequestBody ReturnRequest returnRequest) {
        RejectRequest rejectRequest = new RejectRequest();
        rejectRequest.setReason(StringUtils.EMPTY);
        returnRequest.getRids().forEach(rid -> this.refundReject(rid, rejectRequest));
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    @ApiOperation(value = "取消退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/cancel/{rid}", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse cancel(@PathVariable String rid, @RequestParam("reason") String reason) {
        List<ReturnOrderVO> returnOrderVOList = returnOrderQueryProvider.listByCondition(
                ReturnOrderByConditionRequest.builder().rids(new String[]{rid}).build()).getContext().getReturnOrderList();
        if (CollectionUtils.isEmpty(returnOrderVOList)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        ReturnOrderVO returnOrderVO = returnOrderVOList.get(0);
        //linkedMall订单，且未支付失败
        if ((!Boolean.TRUE.equals(returnOrderVO.getThirdPlatformPayErrorFlag()))
                && ThirdPlatformType.LINKED_MALL.equals(returnOrderVO.getThirdPlatformType())
                && CollectionUtils.isNotEmpty(returnOrderVO.getReturnItems())) {
            String subLmOrderId = returnOrderVO.getReturnItems().get(0).getThirdPlatformSubOrderId();
            if (StringUtils.isNotBlank(subLmOrderId)) {
                SbcQueryRefundApplicationDetailRequest detailRequest = new SbcQueryRefundApplicationDetailRequest();
                detailRequest.setBizUid(returnOrderVO.getBuyer().getId());
                detailRequest.setSubLmOrderId(subLmOrderId);
                QueryRefundApplicationDetailResponse.RefundApplicationDetail detail =
                        linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(detailRequest).getContext().getDetail();
                if (detail != null) {
                    if (!(Integer.valueOf(4).equals(detail.getDisputeStatus())
                            || Integer.valueOf(6).equals(detail.getDisputeStatus()))) {
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"linkedMall商家没有拒绝退款"});
                    }
                }
            }
        }
        return returnOrderProvider.cancel(ReturnOrderCancelRequest.builder().operator(commonUtil.getOperator()).rid(rid)
                .remark(reason).build());
    }

    @ApiOperation(value = "修改退单")
    @RequestMapping(value = "/remedy", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse remedy(@RequestBody RemedyReturnRequest request) {
        return returnOrderProvider.remedy(ReturnOrderRemedyRequest.builder().operator(commonUtil.getOperator())
                .newReturnOrder(Remedy2ReturnOrder.convert(request)).build());
    }


    /**
     * @description 商家-运营查看退货订单详情和可退商品数
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查看退货订单详情和可退商品数")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "退单Id", required = true)
    @RequestMapping(value = "/trade/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeVO> tradeDetails(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        TradeVO trade = returnOrderQueryProvider.queryCanReturnItemNumByTid(CanReturnItemNumByTidRequest.builder()
                .tid(tid).build()).getContext();
        return BaseResponse.success(trade);
    }

    /**
     * @description 商家-运营查询退货退单及可退商品数
     * @menu 退单相关
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查询退货退单及可退商品数")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/detail/{rid}")
    public ResponseEntity<ReturnOrderVO> returnDetail(@PathVariable String rid) {
        checkOperatorByReturnOrder(rid);
        ReturnOrderVO returnOrder = returnOrderQueryProvider.queryCanReturnItemNumById(CanReturnItemNumByIdRequest
                .builder().rid(rid).build()).getContext();
        return ResponseEntity.ok(returnOrder);
    }


    /**
     * 查询退单退款记录
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "查询退单退款记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @RequestMapping(value = "/refundOrder/{rid}", method = RequestMethod.GET)
    public BaseResponse<RefundOrderListReponse> refundOrder(@PathVariable String rid) {
        checkOperatorByReturnOrder(rid);
        return refundOrderQueryProvider.getRefundOrderRespByReturnOrderCode(new RefundOrderResponseByReturnOrderCodeRequest(rid));
    }

    /**
     * 关闭退款
     *
     * @param rid
     * @return
     */
    @RequestMapping(value = "/refund/{rid}/closeRefund", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse closeRefund(@PathVariable String rid) {
        checkOperatorByReturnOrder(rid);
        return returnOrderProvider.closeRefund(ReturnOrderCloseRequest.builder()
                .operator(commonUtil.getOperator())
                .rid(rid).build());
    }

    private TradeVO checkOperatorByTrade(String tid) {
        TradeVO trade = null;
        Operator operator = commonUtil.getOperator();
        if(operator.getPlatform() == Platform.SUPPLIER){
            if (tid.startsWith("O")){
                trade =
                        tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
            }else if(tid.startsWith("S")){
                trade =
                        providerTradeQueryProvider.providerGetById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
            }
            if(Objects.nonNull(trade) && !Objects.equals(commonUtil.getStoreId(), trade.getSupplier().getStoreId())){
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
        }
        return trade;
    }

    private ReturnOrderVO checkOperatorByReturnOrder(String rid){
        ReturnOrderVO returnOrder = null;
        Operator operator = commonUtil.getOperator();
        if(operator.getPlatform() == Platform.SUPPLIER) {
             returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                    .build()).getContext();
            if(!Objects.equals(commonUtil.getStoreId(), returnOrder.getCompany().getStoreId())){
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
        }else if (operator.getPlatform() == Platform.PROVIDER){
            returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                    .build()).getContext();
            if(!Objects.equals(commonUtil.getStoreId(), returnOrder.getTradeVO().getSupplier().getStoreId())){
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
        }
        return returnOrder;
    }
}
