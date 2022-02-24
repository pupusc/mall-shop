package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxOrderPayRequest implements Serializable {
    private static final long serialVersionUID = -4764200723202766279L;
    @JSONField(name ="order_id")
    private String orderId;
    @JSONField(name ="out_order_id")
    private String outOrderId;
    @JSONField(name ="openid")
    private String openId;
    @JSONField(name ="action_type")
    private Integer actionId;
    @JSONField(name ="transaction_id")
    private String transactionId;
    @JSONField(name ="pay_time")
    private String payTime;

}
