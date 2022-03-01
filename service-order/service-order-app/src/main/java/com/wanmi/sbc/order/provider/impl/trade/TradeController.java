package com.wanmi.sbc.order.provider.impl.trade;
import com.soybean.mall.order.api.response.OrderCommitResponse;
import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import com.soybean.mall.order.miniapp.service.TradeOrderService;
import com.soybean.mall.order.model.OrderCommitResult;
import com.soybean.mall.order.trade.service.OrderService;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.order.bean.vo.*;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.*;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.PayCallBackType;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.receivables.request.ReceivableAddRequest;
import com.wanmi.sbc.order.trade.model.entity.*;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroup;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.request.TradePriceChangeRequest;
import com.wanmi.sbc.order.trade.request.TradeRemedyRequest;
import com.wanmi.sbc.order.trade.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-04 10:04
 */
@Slf4j
@Validated
@RestController
public class TradeController implements TradeProvider {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private TradeOptimizeService tradeOptimizeService;

    @Autowired
    private CycleBuyDeliverTimeService cycleBuyDeliverTimeService;

    @Autowired
    private ProviderTradeService providerTradeService;


    @Autowired
    private TradePushERPService tradePushERPService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TradeOrderService tradeOrderService;


    /**
     * 新增交易单
     *
     * @param tradeAddWithOpRequest 交易单 操作信息 {@link TradeAddWithOpRequest}
     * @return 交易单 {@link TradeAddWithOpResponse}
     */
    @Override
    public BaseResponse<TradeAddWithOpResponse> addWithOp(@RequestBody @Valid TradeAddWithOpRequest tradeAddWithOpRequest) {
        Trade trade = KsBeanUtil.convert(tradeAddWithOpRequest.getTrade(), Trade.class);
        trade.setParentId(generatorService.generatePoId());
        trade = tradeService.create(trade,
                tradeAddWithOpRequest.getOperator());
        return BaseResponse.success(TradeAddWithOpResponse.builder().tradeVO(KsBeanUtil.convert(trade, TradeVO.class)).build());
    }

    /**
     * 批量新增交易单
     *
     * @param tradeAddBatchRequest 交易单集合 操作信息 {@link TradeAddBatchRequest}
     * @return 交易单提交结果集合 {@link TradeAddBatchResponse}
     */
    @Override
    public BaseResponse<TradeAddBatchResponse> addBatch(@RequestBody @Valid TradeAddBatchRequest tradeAddBatchRequest) {
        List<TradeCommitResult> trades =
                tradeService.createBatch(KsBeanUtil.convert(tradeAddBatchRequest.getTradeDTOList(), Trade.class),null,
                        tradeAddBatchRequest.getOperator());
        return BaseResponse.success(TradeAddBatchResponse.builder().tradeCommitResultVOS(KsBeanUtil.convert(trades,
                TradeCommitResultVO.class)).build());
    }

    /**
     * 批量分组新增交易单
     *
     * @param tradeAddBatchWithGroupRequest 交易单集合 分组信息 操作信息 {@link TradeAddBatchWithGroupRequest}
     * @return 交易单提交结果集合 {@link TradeAddBatchWithGroupResponse}
     */
    @Override
    public BaseResponse<TradeAddBatchWithGroupResponse> addBatchWithGroup(@RequestBody @Valid TradeAddBatchWithGroupRequest tradeAddBatchWithGroupRequest) {
        List<TradeCommitResult> trades = tradeService.createBatchWithGroup(
                KsBeanUtil.convert(tradeAddBatchWithGroupRequest.getTradeDTOList(), Trade.class),
                KsBeanUtil.convert(tradeAddBatchWithGroupRequest.getTradeGroup(), TradeGroup.class),
                tradeAddBatchWithGroupRequest.getOperator());
        return BaseResponse.success(TradeAddBatchWithGroupResponse.builder().tradeCommitResultVOS(KsBeanUtil.convert(trades,
                TradeCommitResultVO.class)).build());
    }

    /**
     * 订单改价
     *
     * @param tradeModifyPriceRequest 价格信息 交易单号 操作信息 {@link TradeModifyPriceRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyPrice(@RequestBody @Valid TradeModifyPriceRequest tradeModifyPriceRequest) {
        tradeService.changePrice(KsBeanUtil.convert(tradeModifyPriceRequest.getTradePriceChangeDTO(),
                TradePriceChangeRequest.class),
                tradeModifyPriceRequest.getTid(),
                tradeModifyPriceRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * C端提交订单
     *
     * @param tradeCommitRequest 提交订单请求对象
     * @return 提交订单结果
     */
    @Override
    public BaseResponse<TradeCommitResponse> commit(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        List<TradeCommitResult> results = tradeOptimizeService.commit(tradeCommitRequest);
        return BaseResponse.success(new TradeCommitResponse(KsBeanUtil.convert(results, TradeCommitResultVO.class)));
    }


    @Override
    public BaseResponse<String> addProviderTrade(String oid, String userId) {
        // tradeService.virtualCouponHandle();
        tradeService.addProviderTrade(oid, userId);
        return BaseResponse.success("s");
    }


    /**
     * 补单 根据trade 生成 papayOrder
     * @param oid
     */
    @Override
    public BaseResponse<String> addFixPayOrder(String oid){
        tradeService.addFixPayOrder(oid);
        return BaseResponse.success("success");
    }


    /**
     * C端提交尾款订单
     *
     * @param tradeCommitRequest 提交订单请求对象
     * @return 提交订单结果
     */
    @Override
    public BaseResponse<TradeCommitResponse> commitTail(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        List<TradeCommitResult> results = tradeService.commitTail(tradeCommitRequest);
        return BaseResponse.success(new TradeCommitResponse(KsBeanUtil.convert(results, TradeCommitResultVO.class)));
    }

    /**
     * 移动端提交积分商品订单
     *
     * @param pointsTradeCommitRequest 提交订单请求对象  {@link PointsTradeCommitRequest}
     * @return
     */
    @Override
    public BaseResponse<PointsTradeCommitResponse> pointsCommit(@RequestBody @Valid PointsTradeCommitRequest pointsTradeCommitRequest) {
        PointsTradeCommitResult result = tradeService.pointsCommit(pointsTradeCommitRequest);
        return BaseResponse.success(new PointsTradeCommitResponse(KsBeanUtil.convert(result,
                PointsTradeCommitResultVO.class)));
    }

    /**
     * 移动端提交积分优惠券订单
     *
     * @param pointsTradeCommitRequest 提交订单请求对象  {@link PointsTradeCommitRequest}
     * @return
     */
    @Override
    public BaseResponse<PointsTradeCommitResponse> pointsCouponCommit(@RequestBody @Valid PointsCouponTradeCommitRequest pointsTradeCommitRequest) {
        PointsTradeCommitResult result = tradeService.pointsCouponCommit(pointsTradeCommitRequest);
        return BaseResponse.success(new PointsTradeCommitResponse(KsBeanUtil.convert(result,
                PointsTradeCommitResultVO.class)));
    }

    /**
     * 修改订单
     *
     * @param tradeModifyRemedyRequest 店铺信息 交易单信息 操作信息 {@link TradeModifyRemedyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse remedy(@RequestBody @Valid TradeModifyRemedyRequest tradeModifyRemedyRequest) {
        tradeService.remedy(KsBeanUtil.convert(tradeModifyRemedyRequest.getTradeRemedyDTO(), TradeRemedyRequest.class),
                tradeModifyRemedyRequest.getOperator(),
                KsBeanUtil.convert(tradeModifyRemedyRequest.getStoreInfoDTO(), StoreInfoResponse.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改订单-部分修改
     *
     * @param tradeRemedyPartRequest 店铺信息 交易单信息 操作信息 {@link TradeRemedyPartRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse remedyPart(@RequestBody @Valid TradeRemedyPartRequest tradeRemedyPartRequest) {
        tradeService.remedyPart(KsBeanUtil.convert(tradeRemedyPartRequest.getTradeRemedyDTO(),
                TradeRemedyRequest.class),
                tradeRemedyPartRequest.getOperator(),
                KsBeanUtil.convert(tradeRemedyPartRequest.getStoreInfoDTO(), StoreInfoResponse.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 取消订单
     *
     * @param tradeCancelRequest 交易编号 操作信息 {@link TradeCancelRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse cancel(@RequestBody @Valid TradeCancelRequest tradeCancelRequest) {
        tradeService.cancel(tradeCancelRequest.getTid(), tradeCancelRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 订单审核
     *
     * @param tradeAuditRequest 订单审核相关必要信息 {@link TradeAuditRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse audit(@RequestBody @Valid TradeAuditRequest tradeAuditRequest) {
        tradeService.audit(tradeAuditRequest.getTid(), tradeAuditRequest.getAuditState(), tradeAuditRequest.getReason(),
                tradeAuditRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 订单回审
     *
     * @param tradeRetrialRequest 订单审核相关必要信息 {@link TradeRetrialRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse retrial(@RequestBody @Valid TradeRetrialRequest tradeRetrialRequest) {
        tradeService.retrial(tradeRetrialRequest.getTid(), tradeRetrialRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 订单批量审核
     *
     * @param tradeAuditBatchRequest 订单审核相关必要信息 {@link TradeAuditBatchRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse auditBatch(@RequestBody @Valid TradeAuditBatchRequest tradeAuditBatchRequest) {
        tradeService.batchAudit(tradeAuditBatchRequest.getIds(), tradeAuditBatchRequest.getAuditState(),
                tradeAuditBatchRequest.getReason(),
                tradeAuditBatchRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改卖家备注
     *
     * @param tradeRemedySellerRemarkRequest 订单修改相关必要信息 {@link TradeRemedySellerRemarkRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse remedySellerRemark(@RequestBody @Valid TradeRemedySellerRemarkRequest tradeRemedySellerRemarkRequest) {
        tradeService.remedySellerRemark(tradeRemedySellerRemarkRequest.getTid(),
                tradeRemedySellerRemarkRequest.getSellerRemark(),
                tradeRemedySellerRemarkRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发货
     *
     * @param tradeDeliverRequest 物流信息 操作信息 {@link TradeDeliverRequest}
     * @return 物流编号 {@link TradeDeliverResponse}
     */
    @Override
    public BaseResponse<TradeDeliverResponse> deliver(@RequestBody @Valid TradeDeliverRequest tradeDeliverRequest) {
        String deliverId = tradeService.deliver(tradeDeliverRequest.getTid(),
                KsBeanUtil.convert(tradeDeliverRequest.getTradeDeliver(), TradeDeliver.class),
                tradeDeliverRequest.getOperator(), BoolFlag.NO);
        return BaseResponse.success(TradeDeliverResponse.builder().deliverId(deliverId).build());
    }

    @Override
    public BaseResponse batchDeliver(@RequestBody @Valid TradeBatchDeliverRequest tradeBatchDeliverRequest) {
        if (CollectionUtils.isEmpty(tradeBatchDeliverRequest.getBatchDeliverDTOList())){
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        tradeOptimizeService.batchDeliver(tradeBatchDeliverRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 确认收货
     *
     * @param tradeConfirmReceiveRequest 订单编号 操作信息 {@link TradeConfirmReceiveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse confirmReceive(@RequestBody @Valid TradeConfirmReceiveRequest tradeConfirmReceiveRequest) {
        tradeService.confirmReceive(tradeConfirmReceiveRequest.getTid(), tradeConfirmReceiveRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 退货 | 退款
     *
     * @param tradeReturnOrderRequest 订单编号 操作信息 {@link TradeReturnOrderRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse returnOrder(@RequestBody @Valid TradeReturnOrderRequest tradeReturnOrderRequest) {
        tradeService.returnOrder(tradeReturnOrderRequest.getTid(), tradeReturnOrderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 作废订单
     *
     * @param tradeVoidedRequest 订单编号 操作信息 {@link TradeVoidedRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse voided(@RequestBody @Valid TradeVoidedRequest tradeVoidedRequest) {
        tradeService.voidTrade(tradeVoidedRequest.getTid(), tradeVoidedRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 退单作废后的订单状态扭转
     *
     * @param tradeReverseRequest 订单编号 操作信息 {@link TradeReverseRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse reverse(@RequestBody @Valid TradeReverseRequest tradeReverseRequest) {
        tradeService.reverse(tradeReverseRequest.getTid(), tradeReverseRequest.getOperator(),
                tradeReverseRequest.getReturnType());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发货记录作废
     *
     * @param tradeDeliverRecordObsoleteRequest 订单编号 物流单号 操作信息 {@link TradeDeliverRecordObsoleteRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse deliverRecordObsolete(@RequestBody @Valid TradeDeliverRecordObsoleteRequest tradeDeliverRecordObsoleteRequest) {
        tradeService.deliverRecordObsolete(tradeDeliverRecordObsoleteRequest.getTid(),
                tradeDeliverRecordObsoleteRequest.getDeliverId(),
                tradeDeliverRecordObsoleteRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 保存发票信息
     *
     * @param tradeAddInvoiceRequest 订单编号 发票信息 {@link TradeAddInvoiceRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse saveInvoice(@RequestBody @Valid TradeAddInvoiceRequest tradeAddInvoiceRequest) {
        tradeService.saveInvoice(tradeAddInvoiceRequest.getTid(),
                KsBeanUtil.convert(tradeAddInvoiceRequest.getInvoiceDTO(), Invoice.class));
        return BaseResponse.SUCCESSFUL();


    }

    /**
     * 支付作废
     *
     * @param tradePayRecordObsoleteRequest 订单编号 操作信息 {@link TradePayRecordObsoleteRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse payRecordObsolete(@RequestBody @Valid TradePayRecordObsoleteRequest tradePayRecordObsoleteRequest) {
        tradeService.payRecordObsolete(tradePayRecordObsoleteRequest.getTid(),
                tradePayRecordObsoleteRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 线上订单支付回调
     *
     * @param tradePayCallBackOnlineRequest 订单 支付单 操作信息 {@link TradePayCallBackOnlineRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse payCallBackOnline(@RequestBody @Valid TradePayCallBackOnlineRequest tradePayCallBackOnlineRequest) {
        tradeService.payCallBackOnline(
                KsBeanUtil.convert(tradePayCallBackOnlineRequest.getTrade(), Trade.class),
                KsBeanUtil.convert(tradePayCallBackOnlineRequest.getPayOrderOld(), PayOrder.class),
                tradePayCallBackOnlineRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse payCallBackOnlineBatch(@RequestBody @Valid TradePayCallBackOnlineBatchRequest tradePayCallBackOnlineBatchRequest) {
        List<PayCallBackOnlineBatch> request = tradePayCallBackOnlineBatchRequest.getRequestList().stream().map(i -> {
            PayCallBackOnlineBatch data = new PayCallBackOnlineBatch();
            data.setPayOrderOld(KsBeanUtil.convert(i.getPayOrderOld(), PayOrder.class));
            data.setTrade(KsBeanUtil.convert(i.getTrade(), Trade.class));
            return data;
        }).collect(Collectors.toList());
        tradeService.payCallBackOnlineBatch(request, tradePayCallBackOnlineBatchRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 订单支付回调
     *
     * @param tradePayCallBackRequest 订单号 支付金额 操作信息 支付方式{@link TradePayCallBackRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse payCallBack(@RequestBody @Valid TradePayCallBackRequest tradePayCallBackRequest) {
        tradeService.payCallBack(
                tradePayCallBackRequest.getTid(),
                tradePayCallBackRequest.getPayOrderPrice(),
                tradePayCallBackRequest.getOperator(),
                tradePayCallBackRequest.getPayWay());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 0 元订单默认支付
     *
     * @param tradeDefaultPayRequest 订单号{@link TradeDefaultPayRequest}
     * @return 支付结果 {@link TradeDefaultPayResponse}
     */
    @Override
    public BaseResponse<TradeDefaultPayResponse> defaultPay(@RequestBody @Valid TradeDefaultPayRequest tradeDefaultPayRequest) {
        Trade trade = tradeService.detail(tradeDefaultPayRequest.getTid());
        log.info("0元支付:{}", trade.getId());
        if (Objects.isNull(trade)) {
            throw new SbcRuntimeException("K-050100", new Object[]{tradeDefaultPayRequest.getTid()});
        }
        return BaseResponse.success(TradeDefaultPayResponse.builder()
                .payResult(tradeService.tradeDefaultPay(trade, tradeDefaultPayRequest.getPayWay())).build());
    }

    @Override
    public BaseResponse defaultPayBatch(@RequestBody @Valid TradeDefaultPayBatchRequest tradeDefaultPayBatchRequest) {
        List<Trade> trades = tradeService.details(tradeDefaultPayBatchRequest.getTradeIds());
        //请求包含不存在的订单
        if (trades.size() != tradeDefaultPayBatchRequest.getTradeIds().size()) {
            throw new SbcRuntimeException("K-050100");
        }
        tradeService.tradeDefaultPayBatch(trades, tradeDefaultPayBatchRequest.getPayWay());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 新增线下收款单(包含线上线下的收款单)
     *
     * @param tradeAddReceivableRequest 收款单平台信息{@link TradeAddReceivableRequest}
     * @return 支付结果 {@link TradeDefaultPayResponse}
     */
    @Override
    public BaseResponse addReceivable(@RequestBody @Valid TradeAddReceivableRequest tradeAddReceivableRequest) {
        tradeService.addReceivable(KsBeanUtil.convert(tradeAddReceivableRequest.getReceivableAddDTO(),
                ReceivableAddRequest.class), tradeAddReceivableRequest.getPlatform(),
                tradeAddReceivableRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 确认支付单
     *
     * @param tradeConfirmPayOrderRequest 支付单id集合{@link TradeConfirmPayOrderRequest}
     * @return 支付单集合 {@link TradeConfirmPayOrderResponse}
     */
    @Override
    public BaseResponse confirmPayOrder(@RequestBody @Valid TradeConfirmPayOrderRequest tradeConfirmPayOrderRequest) {
        tradeService.confirmPayOrder(tradeConfirmPayOrderRequest.getPayOrderIds(),
                tradeConfirmPayOrderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新订单的结算状态
     *
     * @param tradeUpdateSettlementStatusRequest 店铺id 起始时间 {@link TradeUpdateSettlementStatusRequest}
     * @return 支付单集合 {@link TradeCountByPayStateResponse}
     */
    @Override
    public BaseResponse updateSettlementStatus(@RequestBody @Valid TradeUpdateSettlementStatusRequest tradeUpdateSettlementStatusRequest) {
        tradeService.updateSettlementStatus(tradeUpdateSettlementStatusRequest.getStoreId(),
                tradeUpdateSettlementStatusRequest.getStartTime()
                , tradeUpdateSettlementStatusRequest.getEndTime());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加订单
     *
     * @param tradeAddRequest 订单信息 {@link TradeAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse add(@RequestBody @Valid TradeAddRequest tradeAddRequest) {
        tradeService.addTrade(KsBeanUtil.convert(tradeAddRequest.getTrade(), Trade.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新订单的业务员
     *
     * @param tradeUpdateEmployeeIdRequest 业务员信息 {@link TradeUpdateEmployeeIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse updateEmployeeId(@RequestBody @Valid TradeUpdateEmployeeIdRequest tradeUpdateEmployeeIdRequest) {
        tradeService.updateEmployeeId(tradeUpdateEmployeeIdRequest.getEmployeeId(),
                tradeUpdateEmployeeIdRequest.getCustomerId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新返利标志
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse updateCommissionFlag(@RequestBody @Valid TradeUpdateCommissionFlagRequest request) {
        tradeService.updateCommissionFlag(request.getTradeId(), request.getCommissionFlag());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新最终时间
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse updateFinalTime(@RequestBody @Valid TradeFinalTimeUpdateRequest request) {
        tradeService.updateFinalTime(request.getTid(), request.getOrderReturnTime());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新正在进行的退单数量
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse updateReturnOrderNum(@RequestBody @Valid TradeReturnOrderNumUpdateRequest request) {
        tradeService.updateReturnOrderNum(request.getTid(), request.isAddFlag());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 完善没有业务员的订单
     *
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse fillEmployeeId() {
        tradeService.fillEmployeeId();
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 更新订单
     *
     * @param tradeUpdateRequest 订单信息 {@link TradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse update(@RequestBody @Valid TradeUpdateRequest tradeUpdateRequest) {
        tradeService.updateTradeInfo(tradeUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 更新订单集合
     *
     * @param tradeUpdateListTradeRequest 订单信息 {@link TradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse updateListTrade(@RequestBody @Valid TradeUpdateListTradeRequest tradeUpdateListTradeRequest) {
        tradeService.updateListTradeInfo(tradeUpdateListTradeRequest);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse payOnlineCallBack(@Valid TradePayOnlineCallBackRequest tradePayOnlineCallBackRequest) {
        try {
            if(tradePayOnlineCallBackRequest.getPayCallBackType()== PayCallBackType.WECAHT){
                tradeService.wxPayOnlineCallBack(tradePayOnlineCallBackRequest);
            } else if(tradePayOnlineCallBackRequest.getPayCallBackType()== PayCallBackType.ALI){
                tradeService.aliPayOnlineCallBack(tradePayOnlineCallBackRequest);
            }
        } catch (Exception e) {
            log.error("TradeController.payOnlineCallBack exception" , e);
        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 周期购订单：顺延/取消顺延
     * @param cycleBuyPostponementRequest 订单信息 {@link TradePayOnlineCallBackRequest}
     * @return
     */
    @Override
    public BaseResponse cycleBuyPostponement(@RequestBody @Valid CycleBuyPostponementRequest cycleBuyPostponementRequest) {
        Trade trade = tradeService.detail(cycleBuyPostponementRequest.getTid());
        TradeCycleBuyInfo tradeCycleBuyInfo= trade.getTradeCycleBuyInfo();
        List<DeliverCalendar> deliverCalendars=tradeCycleBuyInfo.getDeliverCalendar();
        //顺延
        if (cycleBuyPostponementRequest.getIsPostponement()){
            //拿到顺延的时间和集合中的时间做对比把找到的数据状态设置成已顺延
            deliverCalendars.forEach(deliverCalendar -> {
                if (Objects.equals(deliverCalendar.getDeliverDate(),cycleBuyPostponementRequest.getLocalDate().toLocalDate())) {
                    deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.POSTPONE);
                }
            });
            //创建一个新的发货日历
            DeliverCalendar deliverCalendar= deliverCalendars.get(deliverCalendars.size()-1);
            LocalDate localDate= cycleBuyDeliverTimeService.getLatestDeliverTime(deliverCalendar.getDeliverDate(),tradeCycleBuyInfo.getDeliveryCycle(),tradeCycleBuyInfo.getCycleBuySendDateRule().getSendDateRule());
            DeliverCalendar newDeliverCalendar=new DeliverCalendar();
            newDeliverCalendar.setDeliverDate(localDate);
            newDeliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);
            deliverCalendars.add(newDeliverCalendar);


        }else {//取消顺延
            //拿到取消顺延的时间和集合中的事件做对比把状态设置成代配送
            deliverCalendars.forEach(deliverCalendar -> {
                if (Objects.equals(deliverCalendar.getDeliverDate(),cycleBuyPostponementRequest.getLocalDate().toLocalDate())) {
                    deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);
                }
            });
            //删除之前顺延的数据
            deliverCalendars.remove(deliverCalendars.size()-1);

            //删除最后是已顺延的发货日历
            this.removecycleBuy(deliverCalendars);


        }
        tradeCycleBuyInfo.setDeliverCalendar(deliverCalendars);
        trade.setTradeCycleBuyInfo(tradeCycleBuyInfo);
        tradeService.updateTrade(trade);



        List<ProviderTrade>  providerTrades= providerTradeService.findListByParentId(cycleBuyPostponementRequest.getTid());

        providerTrades.forEach(providerTrade -> {
             providerTrade.setTradeCycleBuyInfo(tradeCycleBuyInfo);
        });

        //更新子单的周期购信息
        providerTradeService.updateProviderTradeList(providerTrades);

        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 周期购订单  定时器推送失败---手动推送
     * @param cycleBuyPostponementRequest 订单信息 {@link TradePayOnlineCallBackRequest}
     * @return
     */
    @Override
    public BaseResponse cycleBuySupplementaryPush(@RequestBody @Valid CycleBuyPostponementRequest cycleBuyPostponementRequest) {
        List<ProviderTrade>  providerTrades= providerTradeService.findListByParentId(cycleBuyPostponementRequest.getTid());
        providerTrades.forEach(providerTrade -> {
            TradeCycleBuyInfo tradeCycleBuyInfo = providerTrade.getTradeCycleBuyInfo();
            List<DeliverCalendar> deliverCalendarList = tradeCycleBuyInfo.getDeliverCalendar();
            deliverCalendarList.forEach(deliverCalendar -> {
                if (deliverCalendar.getCycleDeliverStatus() == CycleDeliverStatus.PUSHED_FAIL && Objects.equals(deliverCalendar.getDeliverDate(),cycleBuyPostponementRequest.getLocalDate().toLocalDate())) {
                    int index = deliverCalendarList.indexOf(deliverCalendar);
                    boolean isFirstCycle = Boolean.FALSE;
                    if (index == 0) {
                        isFirstCycle = Boolean.TRUE;
                    }

                    //推送订单
                    tradePushERPService.pushCycleOrderToERP(providerTrade, deliverCalendar, index + 1, isFirstCycle);

                    log.info("================订单推送周期购订单---手动推送===:{}", providerTrade);

                }
            });
        });
        return BaseResponse.SUCCESSFUL();
    }





    /**
     * 删除最后是已顺延的发货日历
     * @param deliverCalendars
     */
    private void removecycleBuy( List<DeliverCalendar> deliverCalendars) {

        Collections.reverse(deliverCalendars);

        List<DeliverCalendar> removecycleBuy=new ArrayList<>();

        for (DeliverCalendar deliverCalendar:deliverCalendars){
            if (deliverCalendar.getCycleDeliverStatus()==CycleDeliverStatus.NOT_SHIPPED || deliverCalendar.getCycleDeliverStatus()==CycleDeliverStatus.SHIPPED) {
                break;
            }
            removecycleBuy.add(deliverCalendar);
        }

        if (CollectionUtils.isNotEmpty(removecycleBuy)) {
            deliverCalendars.removeAll(removecycleBuy);
        }

        Collections.sort(deliverCalendars, new Comparator<DeliverCalendar>() {
            @Override
            public int compare(DeliverCalendar o1, DeliverCalendar o2) {
                return o1.getDeliverDate().compareTo(o2.getDeliverDate());
            }
        });

    }


    /**
     * 推送订单至ERP系统进行发货
     * @param request
     * @return
     */
    @Override
    public BaseResponse pushOrderToERP(@RequestBody @Valid TradePushRequest request) {
        tradeService.pushTradeToErp(request.getTid());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  根据订单号查询订单
     * @param tradeId 订单号
     * @return
     */
    @Override
    public BaseResponse<TradeGetBookingTypeByIdResponse> queryTradeInformation(@Valid String tradeId) {
        Trade detail = tradeService.detail(tradeId);

        TradeStateVO tradeStateVO = new TradeStateVO();
        KsBeanUtil.copyPropertiesThird(detail.getTradeState(),tradeStateVO);

        TradeGetBookingTypeByIdResponse tradeResponse = new TradeGetBookingTypeByIdResponse();
        tradeResponse.setId(detail.getId());
        tradeResponse.setBookingType(detail.getBookingType());
        tradeResponse.setTradeState(tradeStateVO);

        return BaseResponse.success(tradeResponse);
    }

    @Override
    public BaseResponse syncProviderTradeStatus(ProviderTradeStatusSyncRequest request) {
        return tradePushERPService.syncProviderTradeStatus(request);
    }

    @Override
    public BaseResponse syncProviderTradeDeliveryStatus(ProviderTradeDeliveryStatusSyncRequest request) {
        return tradePushERPService.syncProviderTradeDeliveryStatus(request);
    }

    /**
     * C端提交订单-新,不用快照，无优惠信息
     * @param tradeCommitRequest 提交订单请求对象
     * @return 提交订单结果
     */
    @Override
    public BaseResponse<OrderCommitResponse> commitTrade(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        List<OrderCommitResult> results = orderService.commitTrade(tradeCommitRequest);
        return BaseResponse.success(new OrderCommitResponse(KsBeanUtil.convert(results, OrderCommitResultVO.class)));
    }


    /**
     * 同步订单到微信
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchSyncDeliveryStatusToWechat(@RequestBody @Valid ProviderTradeErpRequest request) {
        tradeOrderService.batchSyncDeliveryStatusToWechat(request.getPageSize(),request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }
}
