package com.soybean.mall.order.api.provider.order;

import com.soybean.mall.order.api.request.order.CreateWxOrderAndPayRequest;
import com.soybean.mall.order.api.request.order.GetPaymentParamsRequest;
import com.soybean.mall.order.api.request.order.TradeOrderReportRequest;
import com.soybean.mall.order.api.request.order.WxMiniProgramCallbackRequest;
import com.soybean.mall.order.bean.vo.MiniProgramOrderReportVO;
import com.soybean.mall.order.bean.vo.WxOrderPaymentParamsVO;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    /**
     * 小程序报表数据
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/create-wx-order-report-cache")
    BaseResponse addOrderReportCache(@RequestBody TradeOrderReportRequest request);

    /**
     * 小程序回调数据添加
     */
    @PostMapping("/order/${application.order.version}/callback/add")
    BaseResponse<Long> addCallback(@RequestBody WxMiniProgramCallbackRequest wxMiniProgramCallbackRequest);

    /**
     * 小程序回调数据修改
     */
    @PostMapping("/order/${application.order.version}/callback/update")
    BaseResponse updateCallback(@RequestBody WxMiniProgramCallbackRequest wxMiniProgramCallbackRequest);

    /**
     * 获取小程序报表数据
     * @param
     * @return
     */
    @GetMapping("/order/${application.order.version}/trade/get-wx-order-report-cache")
    BaseResponse<MiniProgramOrderReportVO> getOrderReportCache();

    /**
     * 创建微信订单和获取预支付参数
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/trade/create-wx-order-and-get-payments-params")
    BaseResponse<WxOrderPaymentParamsVO> createWxOrderAndGetPaymentsParams(@RequestBody GetPaymentParamsRequest request);

    /**
     * 获取预支付参数
     * @return
     */
    @GetMapping("/order/${application.order.version}/trade/get-wx-order-payments-params/{openId}/{tid}")
    BaseResponse<WxOrderPaymentParamsVO> getWxOrderPaymentParams(@PathVariable("openId") String openId, @PathVariable("tid") String tid);


}
