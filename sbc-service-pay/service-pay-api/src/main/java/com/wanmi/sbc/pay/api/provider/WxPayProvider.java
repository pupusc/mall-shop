package com.wanmi.sbc.pay.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 微信支付接口
 */
@FeignClient(value = "${application.pay.name}", contextId = "WxPayProvider")
public interface WxPayProvider {

    @PostMapping("/pay/${application.pay.version}/wx-pay-for-native")
    BaseResponse<WxPayForNativeResponse> wxPayForNative(@RequestBody WxPayForNativeRequest request);

    @PostMapping("/pay/${application.pay.version}/wx-pay-for-mweb")
    BaseResponse<WxPayForMWebResponse> wxPayForMWeb(@RequestBody WxPayForMWebRequest mWebRequest);

    @PostMapping("/pay/${application.pay.version}/wx-pay-for-jsapi")
    BaseResponse<Map<String,String>> wxPayForJSApi(@RequestBody WxPayForJSApiRequest jsApiRequest);

    @PostMapping("/pay/${application.pay.version}/wx-pay-for-little-program")
    BaseResponse<Map<String,String>> wxPayForLittleProgram(@RequestBody WxPayForJSApiRequest jsApiRequest);

    @PostMapping("/pay/${application.pay.version}/wx-pay-for-app")
    BaseResponse<Map<String,String>> wxPayForApp(@RequestBody WxPayForAppRequest appRequest);

    @PostMapping("/pay/${application.pay.version}/get-wx-pay-order-detail")
    BaseResponse<WxPayOrderDetailReponse> getWxPayOrderDetail(@RequestBody WxPayOrderDetailRequest request);

    @PostMapping("/pay/${application.pay.version}/wx-pay-refund")
    BaseResponse<WxPayRefundResponse> wxPayRefund(@RequestBody WxPayRefundInfoRequest refundInfoRequest);

    @PostMapping("/pay/${application.pay.version}/wx-pay-company-payment")
    BaseResponse<WxPayCompanyPaymentRsponse> wxPayCompanyPayment(@RequestBody WxPayCompanyPaymentInfoRequest request);

}
