package com.soybean.mall.order.provider.impl.order;

import com.soybean.mall.order.api.provider.order.MiniAppOrderProvider;
import com.soybean.mall.order.api.request.order.CreateWxOrderAndPayRequest;
import com.soybean.mall.order.miniapp.service.TradeOrderService;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
public class MiniAppOrderController implements MiniAppOrderProvider {

    @Autowired
    private TradeOrderService tradeOrderService;
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

    /**
     * 小程序0元订单推送到小程序并支付同步
     * @param request
     * @return
     */
    @Override
    public BaseResponse createWxOrderAndPay(WxCreateOrderRequest request) {
        return null;
    }
}
