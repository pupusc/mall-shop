package com.wanmi.sbc.pay.gateway.handler;


import com.wanmi.sbc.pay.api.request.PayExtraRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Ping++支付参数扩展</p>
 * Created by of628-wenzhi on 2017-08-11-下午5:34.
 */
public class PingPayExtra {

    public static Map<String, Object> channelExtra(String channelCode, PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();

        switch (channelCode) {
            case "alipay":
                extra = alipayExtra(request);
                break;
            case "alipay_wap":
                extra = alipayWapExtra(request);
                break;
            case "alipay_pc_direct":
                extra = alipayPcDirectExtra(request);
                break;
            case "alipay_qr":
                extra = alipayQrExtra(request);
                break;
            case "wx":
                extra = wxExtra(request);
                break;
            case "wx_pub":
                extra = wxPubExtra(request);
                break;
            case "wx_pub_qr":
                extra = wxPubQrDirectExtra(request);
                break;
            case "upacp":
                extra = upacpExtra(request);
                break;
            case "upacp_wap":
                extra = upacpWapExtra(request);
                break;
            case "upacp_pc":
                extra = upacpPcExtra(request);
                break;
        }

        return extra;
    }

    private static Map<String, Object> alipayExtra(PayExtraRequest request) {
        return null;
    }

    private static Map<String, Object> alipayWapExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();

        // 必须，支付成功的回调地址，在本地测试不要写 localhost ，请写 127.0.0.1。URL 后面不要加自定义参数。
        extra.put("success_url", request.getSuccessUrl());

        return extra;
    }

    private static Map<String, Object> alipayPcDirectExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();
        // 必须，支付成功的回调地址，在本地测试不要写 localhost ，请写 127.0.0.1。URL 后面不要加自定义参数。
        extra.put("success_url", request.getSuccessUrl());

        return extra;
    }

    private static Map<String, Object> alipayQrExtra(PayExtraRequest request) {
        return null;
    }

    private static Map<String, Object> wxExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();
        extra.put("open_id", request.getOpenId());

        return extra;
    }

    private static Map<String, Object> wxPubExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();
        extra.put("open_id", request.getOpenId());

        return extra;
    }

    private static Map<String, Object> wxPubQrDirectExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();
        extra.put("product_id", request.getBusinessId());
       return extra;
    }


    private static Map<String, Object> upacpExtra(PayExtraRequest request) {
        return null;
    }

    private static Map<String, Object> upacpWapExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();
        // 必须，支付完成的回调地址，在本地测试不要写 localhost ，请写 127.0.0.1。URL 后面不要加自定义参数。
        extra.put("result_url", request.getSuccessUrl());
        return extra;
    }

    private static Map<String, Object> upacpPcExtra(PayExtraRequest request) {
        Map<String, Object> extra = new HashMap<>();
        // 必须，支付完成的回调地址，在本地测试不要写 localhost ，请写 127.0.0.1。URL 后面不要加自定义参数。
        extra.put("result_url", request.getSuccessUrl());
        return extra;
    }

}
