package com.soybean.mall.order.api.provider.order;

import com.soybean.mall.order.api.request.order.CreateWxOrderAndPayRequest;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.order.name}", contextId = "MiniAppOrderProvider")
public interface MiniAppOrderProvider {

    /**
     * 同步订单状态到微信
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/batch-sync-delivery-status-wechat")
    BaseResponse batchSyncDeliveryStatusToWechat(@RequestBody @Valid ProviderTradeErpRequest request);

    /**
     * 小程序0元订单推送到小程序并支付同步
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/create-wx-order-and-pay")
    BaseResponse createWxOrderAndPay(@RequestBody TradeDefaultPayBatchRequest request);



}
