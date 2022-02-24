package com.soybean.mall.wx.mini.order.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxOrderPayRequest implements Serializable {
    private static final long serialVersionUID = -4764200723202766279L;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("out_order_id")
    private String outOrderId;
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("action_type")
    private Integer actionId;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("pay_time")
    private String payTime;

}
