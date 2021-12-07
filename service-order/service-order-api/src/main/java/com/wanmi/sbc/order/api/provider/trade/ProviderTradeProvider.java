package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.TradeDeliverRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDeliveryCheckRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdAndPidRequest;
import com.wanmi.sbc.order.api.request.trade.TradeUpdateRequest;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeDeliverResponse;
import com.wanmi.sbc.order.api.response.trade.TradeProviderResponse;
import com.wanmi.sbc.order.api.response.trade.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 供应商订单处理
 * @Autho qiaokang
 * @Date：2020-03-27 09:08
 */
@FeignClient(value = "${application.order.name}", contextId = "ProviderTradeProvider")
public interface ProviderTradeProvider {

    /**
     * 更新供应商订单
     *
     * @param tradeUpdateRequest 订单信息 {@link TradeUpdateRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-update")
    BaseResponse providerUpdate(@RequestBody @Valid TradeUpdateRequest tradeUpdateRequest);

    /**
     * 发货校验,检查请求发货商品数量是否符合应发货数量
     *
     * @param tradeDeliveryCheckRequest 订单号 物流信息 {@link TradeDeliveryCheckRequest}
     * @return 处理结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-delivery-check")
    BaseResponse providerDeliveryCheck(@RequestBody @Valid TradeDeliveryCheckRequest tradeDeliveryCheckRequest);

    /**
     * 发货
     *
     * @param tradeDeliverRequest 物流信息 操作信息 {@link TradeDeliverRequest}
     * @return 物流编号 {@link TradeDeliverResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-deliver")
    BaseResponse<TradeDeliverResponse> providerDeliver(@RequestBody @Valid TradeDeliverRequest tradeDeliverRequest);

    /**
     * 修改备注
     *
     * @param providerTradeRemedyBuyerRemarkRequest 订单修改相关必要信息 {@link ProviderTradeRemedyBuyerRemarkRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/provider-remedy-seller-remark")
    BaseResponse remedyBuyerRemark(@RequestBody @Valid ProviderTradeRemedyBuyerRemarkRequest providerTradeRemedyBuyerRemarkRequest);

    /** 查询子订单
     * @param findProviderTradeRequest
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/findByParentId")
    BaseResponse<FindProviderTradeResponse> findByParentIdList(@RequestBody @Valid FindProviderTradeRequest findProviderTradeRequest);

    /**
     *
     * @param tradeGetByIdAndPidRequest
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/provider-by-id-pid")
    BaseResponse<TradeProviderResponse> providerByidAndPid(@RequestBody @Valid TradeGetByIdAndPidRequest tradeGetByIdAndPidRequest);


    /**
     * 发货记录作废
     *
     * @param tradeDeliverRecordObsoleteRequest 订单编号 物流单号 操作信息 {@link TradeDeliverRecordObsoleteRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/provider/trade/deliver-record-obsolete")
    BaseResponse deliverRecordObsolete(@RequestBody @Valid TradeDeliverRecordObsoleteRequest tradeDeliverRecordObsoleteRequest);

    /**
     * 更新退单数量
     * @param request 退单编号 加减标识  {@link ProviderTradeModifyReturnOrderNumByRidRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/provider/trade/modify-return-num-by-rid")
    BaseResponse updateReturnOrderNumByRid(@RequestBody @Valid ProviderTradeModifyReturnOrderNumByRidRequest request);

    /**
     * 更新供应商，作废原订单并重新生成发货单
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/provider/trade/change-trade-provider")
    BaseResponse<String> changeTradeProvider(@RequestBody ChangeTradeProviderRequest request);

    /**
     * 更新供应商，作废原订单并重新生成发货单
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/provider/trade/reset-pushcount")
    BaseResponse resetPushCount(@RequestBody ProviderTradeErpRequest request);
}
