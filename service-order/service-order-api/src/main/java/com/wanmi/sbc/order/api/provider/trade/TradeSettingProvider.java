package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.ReturnOrderModifyAutoAuditRequest;
import com.wanmi.sbc.order.api.request.trade.ReturnOrderModifyAutoReceiveRequest;
import com.wanmi.sbc.order.api.request.trade.TradeSettingModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 11:11
 * @version: 1.0
 */
@FeignClient(value = "${application.order.name}", contextId = "TradeSettingProvider")
public interface TradeSettingProvider {

    /**
     * 修改订单配置
     * @param tradeSettingModifyRequest {@link TradeSettingModifyRequest}
     * @return  {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/modify-trade-configs")
    BaseResponse modifyTradeConfigs(@RequestBody @Valid TradeSettingModifyRequest tradeSettingModifyRequest);

    /**
     * 退单自动审核
     * @param returnOrderModifyAutoAuditRequest {@link ReturnOrderModifyAutoAuditRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/modify-return-order-auto-audit")
    BaseResponse modifyReturnOrderAutoAudit(@RequestBody @Valid ReturnOrderModifyAutoAuditRequest returnOrderModifyAutoAuditRequest);

    /**
     * 退单自动确认收货 由于es索引的问题，用mongodb 分页查询，考虑把订单，退单从es中移除
     * @param returnOrderModifyAutoReceiveRequest {@link ReturnOrderModifyAutoReceiveRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/trade/modify-return-order-auto-receive")
    BaseResponse modifyReturnOrderAutoReceive(@RequestBody @Valid ReturnOrderModifyAutoReceiveRequest returnOrderModifyAutoReceiveRequest);

    /**
     * 订单代发货自动收货
     * @return
    */
    @PostMapping("/order/${application.order.version}/trade/order-auto-receive/{pageNum}/{pageSize}/{orderType}")
    BaseResponse orderAutoReceive(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize, @PathVariable("orderType") Integer orderType);

}
