package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxPrePayOrderRequest implements Serializable {
    private String appid;
    /**
     * 直连商户号
     */
    private String mchid;

    private String description;

    /**
     * 商户id
     */
    @JSONField(name ="out_trade_no")
    private String outTradeNo;

    /**
     * 回调地址
     */
    @JSONField(name="notify_url")
    private String notifyUrl;

    private PriceInfo amount;

    private PayerInfo payer;

    @Data
    public static class PriceInfo{
        /**
         * 订单总金额，单位为分
         */
        private Integer total;
        private String currency ="CNY";
    }

    @Data
    public static class PayerInfo{
        private String openid;
    }
}
