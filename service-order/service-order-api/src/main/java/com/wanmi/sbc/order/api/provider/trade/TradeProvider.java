package com.wanmi.sbc.order.api.provider.trade;

import com.soybean.mall.order.api.response.OrderCommitResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.PointsTradeCommitResponse;
import com.wanmi.sbc.order.api.response.trade.TradeAddBatchResponse;
import com.wanmi.sbc.order.api.response.trade.TradeAddBatchWithGroupResponse;
import com.wanmi.sbc.order.api.response.trade.TradeAddWithOpResponse;
import com.wanmi.sbc.order.api.response.trade.TradeCommitResponse;
import com.wanmi.sbc.order.api.response.trade.TradeConfirmPayOrderResponse;
import com.wanmi.sbc.order.api.response.trade.TradeCountByPayStateResponse;
import com.wanmi.sbc.order.api.response.trade.TradeDefaultPayResponse;
import com.wanmi.sbc.order.api.response.trade.TradeDeliverResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetBookingTypeByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 17:24
 */
@FeignClient(value = "${application.order.name}", contextId = "TradeProvider")
public interface TradeProvider {

    /**
     * 新增交易单
     *
     * @param tradeAddWithOpRequest 交易单 操作信息 {@link TradeAddWithOpRequest}
     * @return 交易单 {@link TradeAddWithOpResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/add-withOp")
    BaseResponse<TradeAddWithOpResponse> addWithOp(@RequestBody @Valid TradeAddWithOpRequest tradeAddWithOpRequest);

    /**
     * 批量新增交易单
     *
     * @param tradeAddBatchRequest 交易单集合 操作信息 {@link TradeAddBatchRequest}
     * @return 交易单提交结果集合 {@link TradeAddBatchResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/add-batch")
    BaseResponse<TradeAddBatchResponse> addBatch(@RequestBody @Valid TradeAddBatchRequest tradeAddBatchRequest);


    /**
     * 批量分组新增交易单
     *
     * @param tradeAddBatchWithGroupRequest 交易单集合 分组信息 操作信息 {@link TradeAddBatchWithGroupRequest}
     * @return 交易单提交结果集合 {@link TradeAddBatchWithGroupResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/add-batch-with-group")
    BaseResponse<TradeAddBatchWithGroupResponse> addBatchWithGroup(@RequestBody @Valid TradeAddBatchWithGroupRequest tradeAddBatchWithGroupRequest);

    /**
     * 订单改价
     *
     * @param tradeModifyPriceRequest 价格信息 交易单号 操作信息 {@link TradeModifyPriceRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/modify-price")
    BaseResponse modifyPrice(@RequestBody @Valid TradeModifyPriceRequest tradeModifyPriceRequest);

    /**
     * C端提交订单
     *
     * @param tradeCommitRequest 提交订单请求对象  {@link TradeCommitRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/commit")
    BaseResponse<TradeCommitResponse>  commit(@RequestBody @Valid TradeCommitRequest tradeCommitRequest);

    /**
     * C端提交尾款订单
     *
     * @param tradeCommitRequest 提交订单请求对象  {@link TradeCommitRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/commit-tail")
    BaseResponse<TradeCommitResponse> commitTail(@RequestBody @Valid TradeCommitRequest tradeCommitRequest);

    /**
     * 移动端提交积分商品订单
     *
     * @param pointsTradeCommitRequest 提交订单请求对象  {@link PointsTradeCommitRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/points-commit")
    BaseResponse<PointsTradeCommitResponse> pointsCommit(@RequestBody @Valid PointsTradeCommitRequest pointsTradeCommitRequest);

    /**
     * 移动端提交积分优惠券订单
     *
     * @param pointsTradeCommitRequest 提交订单请求对象  {@link PointsTradeCommitRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/points-coupon-commit")
    BaseResponse<PointsTradeCommitResponse> pointsCouponCommit(@RequestBody @Valid PointsCouponTradeCommitRequest pointsTradeCommitRequest);

    /**
     * 修改订单
     *
     * @param tradeRemedyRequest 店铺信息 交易单信息 操作信息 {@link TradeModifyRemedyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/remedy")
    BaseResponse remedy(@RequestBody @Valid TradeModifyRemedyRequest tradeRemedyRequest);

    /**
     * 修改订单-部分修改
     *
     * @param tradeRemedyPartRequest 店铺信息 交易单信息 操作信息 {@link TradeRemedyPartRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/remedy-part")
    BaseResponse remedyPart(@RequestBody @Valid TradeRemedyPartRequest tradeRemedyPartRequest);

    /**
     * 取消订单
     *
     * @param tradeCancelRequest 店铺信息 交易单信息 操作信息 {@link TradeCancelRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/cancel")
    BaseResponse cancel(@RequestBody @Valid TradeCancelRequest tradeCancelRequest);

    /**
     * 订单审核
     *
     * @param tradeAuditRequest 订单审核相关必要信息 {@link TradeAuditRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/audit")
    BaseResponse audit(@RequestBody @Valid TradeAuditRequest tradeAuditRequest);

    /**
     * 订单回审
     *
     * @param tradeRetrialRequest 订单审核相关必要信息 {@link TradeRetrialRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/retrial")
    BaseResponse retrial(@RequestBody @Valid TradeRetrialRequest tradeRetrialRequest);

    /**
     * 订单批量审核
     *
     * @param tradeAuditBatchRequest 订单审核相关必要信息 {@link TradeAuditBatchRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/audit-batch")
    BaseResponse auditBatch(@RequestBody @Valid TradeAuditBatchRequest tradeAuditBatchRequest);

    /**
     * 修改卖家备注
     *
     * @param tradeRemedySellerRemarkRequest 订单修改相关必要信息 {@link TradeRemedySellerRemarkRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/remedy-seller-remark")
    BaseResponse remedySellerRemark(@RequestBody @Valid TradeRemedySellerRemarkRequest tradeRemedySellerRemarkRequest);

    /**
     * 发货
     *
     * @param tradeDeliverRequest 物流信息 操作信息 {@link TradeDeliverRequest}
     * @return 物流编号 {@link TradeDeliverResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/deliver")
    BaseResponse<TradeDeliverResponse> deliver(@RequestBody @Valid TradeDeliverRequest tradeDeliverRequest);

    /**
     * 批量发货
     */
    @PostMapping("/order/${application.order.version}/trade/batch-deliver")
    BaseResponse batchDeliver(@RequestBody @Valid TradeBatchDeliverRequest tradeBatchDeliverRequest);

    /**
     * 确认收货
     *
     * @param tradeConfirmReceiveRequest 订单编号 操作信息 {@link TradeConfirmReceiveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/confirm-receive")
    BaseResponse confirmReceive(@RequestBody @Valid TradeConfirmReceiveRequest tradeConfirmReceiveRequest);

    /**
     * 退货 | 退款
     *
     * @param tradeReturnOrderRequest 订单编号 操作信息 {@link TradeReturnOrderRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/return-order")
    BaseResponse returnOrder(@RequestBody @Valid TradeReturnOrderRequest tradeReturnOrderRequest);

    /**
     * 作废订单
     *
     * @param tradeVoidedRequest 订单编号 操作信息 {@link TradeVoidedRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/voided")
    BaseResponse voided(@RequestBody @Valid TradeVoidedRequest tradeVoidedRequest);

    /**
     * 退单作废后的订单状态扭转
     *
     * @param tradeReverseRequest 订单编号 操作信息 {@link TradeReverseRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/reverse")
    BaseResponse reverse(@RequestBody @Valid TradeReverseRequest tradeReverseRequest);

    /**
     * 发货记录作废
     *
     * @param tradeDeliverRecordObsoleteRequest 订单编号 物流单号 操作信息 {@link TradeDeliverRecordObsoleteRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/deliver-record-obsolete")
    BaseResponse deliverRecordObsolete(@RequestBody @Valid TradeDeliverRecordObsoleteRequest tradeDeliverRecordObsoleteRequest);

    /**
     * 保存发票信息
     *
     * @param tradeAddInvoiceRequest 订单编号 发票信息 {@link TradeAddInvoiceRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/save-invoice")
    BaseResponse saveInvoice(@RequestBody @Valid TradeAddInvoiceRequest tradeAddInvoiceRequest);

    /**
     * 支付作废
     *
     * @param tradePayRecordObsoleteRequest 订单编号 操作信息 {@link TradePayRecordObsoleteRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/pay-pecord-obsolete")
    BaseResponse payRecordObsolete(@RequestBody @Valid TradePayRecordObsoleteRequest tradePayRecordObsoleteRequest);

    /**
     * 线上订单支付回调
     *
     * @param tradePayCallBackOnlineRequest 订单 支付单 操作信息 {@link TradePayCallBackOnlineRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/pay-callBack-online")
    BaseResponse payCallBackOnline(@RequestBody @Valid TradePayCallBackOnlineRequest tradePayCallBackOnlineRequest);

    /**
     * 线上订单支付批量回调
     *
     * @param tradePayCallBackOnlineBatchRequest 订单 支付单 操作批量信息 {@link TradePayCallBackOnlineBatchRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/pay-callBack-online-batch")
    BaseResponse payCallBackOnlineBatch(@RequestBody @Valid TradePayCallBackOnlineBatchRequest tradePayCallBackOnlineBatchRequest);

    /**
     * 订单支付回调
     *
     * @param tradePayCallBackRequest 订单号 支付金额 操作信息 支付方式{@link TradePayCallBackRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/pay-callBack")
    BaseResponse payCallBack(@RequestBody @Valid TradePayCallBackRequest tradePayCallBackRequest);

    /**
     * 0 元订单默认支付
     *
     * @param tradeDefaultPayRequest 订单号{@link TradeDefaultPayRequest}
     * @return 支付结果 {@link TradeDefaultPayResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/default-pay")
    BaseResponse<TradeDefaultPayResponse> defaultPay(@RequestBody @Valid TradeDefaultPayRequest tradeDefaultPayRequest);

    /**
     * 0 元订单默认批量支付
     *
     * @param tradeDefaultPayBatchRequest 订单号{@link TradeDefaultPayBatchRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/default-pay-batch")
    BaseResponse defaultPayBatch(@RequestBody @Valid TradeDefaultPayBatchRequest tradeDefaultPayBatchRequest);

    /**
     * 新增线下收款单(包含线上线下的收款单)
     *
     * @param tradeAddReceivableRequest 收款单平台信息{@link TradeAddReceivableRequest}
     * @return 支付单 {@link TradeDefaultPayResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/add-receivable")
    BaseResponse addReceivable(@RequestBody @Valid TradeAddReceivableRequest tradeAddReceivableRequest);

    /**
     * 确认支付单
     *
     * @param tradeConfirmPayOrderRequest 支付单id集合{@link TradeConfirmPayOrderRequest}
     * @return 支付单集合 {@link TradeConfirmPayOrderResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/confirm-payOrder")
    BaseResponse confirmPayOrder(@RequestBody @Valid TradeConfirmPayOrderRequest tradeConfirmPayOrderRequest);

    /**
     * 根据支付状态统计订单
     *
     * @param tradeUpdateSettlementStatusRequest 查询参数 {@link TradeUpdateSettlementStatusRequest}
     * @return 支付单集合 {@link TradeCountByPayStateResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/update-settlement-status")
    BaseResponse updateSettlementStatus(@RequestBody @Valid TradeUpdateSettlementStatusRequest tradeUpdateSettlementStatusRequest);


    /**
     * 添加订单
     *
     * @param tradeAddRequest 订单信息 {@link TradeAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/add")
    BaseResponse add(@RequestBody @Valid TradeAddRequest tradeAddRequest);

    /**
     * 更新订单的业务员
     *
     * @param tradeUpdateEmployeeIdRequest 业务员信息 {@link TradeUpdateEmployeeIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/update-employeeId")
    BaseResponse updateEmployeeId(@RequestBody @Valid TradeUpdateEmployeeIdRequest tradeUpdateEmployeeIdRequest);

    /**
     * 更新返利标志
     *
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/update-commission-flag")
    BaseResponse updateCommissionFlag(@RequestBody @Valid TradeUpdateCommissionFlagRequest request);

    /**
     * 更新最终时间
     *
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/update-final-time")
    BaseResponse updateFinalTime(@RequestBody @Valid TradeFinalTimeUpdateRequest request);

    /**
     * 更新正在进行的退单数量
     *
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/update-return-order-num")
    BaseResponse updateReturnOrderNum(@RequestBody @Valid TradeReturnOrderNumUpdateRequest request);

    /**
     * 完善没有业务员的订单
     *
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/fill-employeeId")
    BaseResponse fillEmployeeId();

    /**
     * 更新订单
     *
     * @param tradeUpdateRequest 订单信息 {@link TradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/update")
    BaseResponse update(@RequestBody @Valid TradeUpdateRequest tradeUpdateRequest);

    /**
     * 更新订单集合
     *
     * @param tradeUpdateListTradeRequest 订单信息 {@link TradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/update-list")
    BaseResponse updateListTrade(@RequestBody @Valid TradeUpdateListTradeRequest tradeUpdateListTradeRequest);

    /**
     * 订单支付回调处理
     *
     * @param tradePayOnlineCallBackRequest 订单信息 {@link TradePayOnlineCallBackRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/pay-online-call-back")
    BaseResponse payOnlineCallBack(@RequestBody @Valid TradePayOnlineCallBackRequest tradePayOnlineCallBackRequest);

    /**
     * 周期购订单：顺延/取消顺延
     * @param cycleBuyPostponementRequest 订单信息 {@link TradePayOnlineCallBackRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/cycle-buy-postponement")
    BaseResponse cycleBuyPostponement(@RequestBody @Valid CycleBuyPostponementRequest cycleBuyPostponementRequest);


    /**
     * 周期购订单  定时器推送失败---手动推送
     * @param cycleBuyPostponementRequest 订单信息 {@link TradePayOnlineCallBackRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade//cyclebuy-supplementary-push")
    BaseResponse cycleBuySupplementaryPush(@RequestBody @Valid CycleBuyPostponementRequest cycleBuyPostponementRequest);


    /**
     * erp订单推送
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/push-order-to-erp")
    BaseResponse pushOrderToERP(@RequestBody @Valid TradePushRequest request);

    /**
     * 根据订单号查询订单信息
     * @param tradeId 订单号
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/query-trade-information")
    BaseResponse<TradeGetBookingTypeByIdResponse> queryTradeInformation(@RequestBody @Valid String tradeId);

    /**
     * 推送发货单同步状态
     * @param request 订单号
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/sync-provider-trade-status")
    BaseResponse syncProviderTradeStatus(@RequestBody @Valid ProviderTradeStatusSyncRequest request);

    /**
     * 同步物流状态
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/sync-provider-trade-delivery-status")
    BaseResponse syncProviderTradeDeliveryStatus(@RequestBody ProviderTradeDeliveryStatusSyncRequest request);


    /**
     * 新增子单
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/add-provider-trade/{oid}/{userId}")
    BaseResponse<String> addProviderTrade(@PathVariable("oid") String oid, @PathVariable("userId") String userId);

    /**
     * 修复 新增订单
     * @param oid
     * @return
     */
    @GetMapping("/order/${application.order.version}/trade/add-fix-pay-order/{oid}")
    BaseResponse<String> addFixPayOrder(@PathVariable("oid") String oid);

    @PostMapping("/order/${application.order.version}/trade/commit-new")
    BaseResponse<OrderCommitResponse> commitTrade(@RequestBody @Valid TradeCommitRequest tradeCommitRequest);

    /**
     * 微信支付回调
     * @param WxTradePayCallBackRequest
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/wx-pay-call-back")
    BaseResponse wxPayCallBack(@RequestBody  WxTradePayCallBackRequest request);


    /**
     * 更新发票信息
     * @param autoUpdateInvoiceRequest
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/update-invoice")
    BaseResponse updateInvoice(@RequestBody AutoUpdateInvoiceRequest autoUpdateInvoiceRequest);
}
