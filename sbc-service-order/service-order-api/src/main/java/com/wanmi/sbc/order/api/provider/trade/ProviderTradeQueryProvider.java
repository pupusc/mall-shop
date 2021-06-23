package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Description: 供应商订单查询
 * @Autho qiaokang
 * @Date：2020-03-27 09:08
 */
@FeignClient(value = "${application.order.name}", contextId = "ProviderTradeQueryProvider")
public interface ProviderTradeQueryProvider {

    /**
     * 条件分页
     *
     * @param tradePageCriteriaRequest 带参分页参数
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/provider-page-criteria")
    BaseResponse<TradePageCriteriaResponse> providerPageCriteria(@RequestBody @Valid ProviderTradePageCriteriaRequest tradePageCriteriaRequest);

    /**
     * 通过id获取交易单信息
     *
     * @param tradeGetByIdRequest 交易单id {@link TradeGetByIdRequest}
     * @return 交易单信息 {@link TradeGetByIdResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-get-by-id")
    BaseResponse<TradeGetByIdResponse> providerGetById(@RequestBody @Valid TradeGetByIdRequest tradeGetByIdRequest);

    /**
     * 通过ids批量获取交易单信息
     *
     * @param providerTradeGetByIdListRequest 交易单id {@link TradeGetByIdRequest}
     * @return 交易单信息 {@link TradeGetByIdResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-get-by-ids")
    BaseResponse<ProviderTradeGetByIdsResponse> providerGetByIds(@RequestBody @Valid ProviderTradeGetByIdListRequest providerTradeGetByIdListRequest);

    /**
     * 通过父订单号获取交易单集合
     *
     * @param tradeListByParentIdRequest 父交易单id {@link TradeListByParentIdRequest}
     * @return 交易单信息 {@link TradeListByParentIdResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/get-provider-list-by-parent-id")
    BaseResponse<TradeListByParentIdResponse> getProviderListByParentId(@RequestBody @Valid TradeListByParentIdRequest tradeListByParentIdRequest);

    /**
     * 查询导出数据
     *
     * @param tradeListExportRequest 查询条件 {@link TradeListExportRequest}
     * @return 验证结果 {@link TradeListExportResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-list-export")
    BaseResponse<TradeListExportResponse> providerListTradeExport(@RequestBody @Valid TradeListExportRequest tradeListExportRequest);

    /**
     * 条件分页
     *
     * @param tradeCountCriteriaRequest 带参分页参数 {@link TradeCountCriteriaRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/provider-count-criteria")
    BaseResponse<ProviderTradeCountCriteriaResponse> countCriteria(@RequestBody @Valid ProviderTradeCountCriteriaRequest tradeCountCriteriaRequest);


    /**
     * 定时任务推送周期购订单
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/batch-push-cycle-order")
    BaseResponse batchPushCycleOrder(@RequestBody @Valid ProviderTradeErpRequest providerTradePushErpRequest);

    /**
     * 定时任务推送周期购订单
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/batch-sync-delivery-status")
    BaseResponse batchSyncDeliveryStatus(@RequestBody @Valid ProviderTradeErpRequest request);

    /**
     * 定时任务推送周期购订单
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/batch-push-order")
    BaseResponse batchPushOrder(@RequestBody @Valid ProviderTradeErpRequest request);

    /**
     * 批量重置扫描次数（已发货，推送次数达到3次）
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/batch-reset-push-count")
    BaseResponse batchResetScanCount(@RequestBody @Valid ProviderTradeErpRequest request);

    /**
     * 扫描未发货订单
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/scan-not-yet-shipped")
    BaseResponse scanNotYetShippedTrade(@RequestBody @Valid ProviderTradeErpRequest request);

    /**
     * 同步历史未发货订单(7天前)
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/batch-sync-history-order-status")
    BaseResponse batchSyncHistoryOrderStatus(@RequestBody @Valid ProviderTradeErpRequest request);
}
