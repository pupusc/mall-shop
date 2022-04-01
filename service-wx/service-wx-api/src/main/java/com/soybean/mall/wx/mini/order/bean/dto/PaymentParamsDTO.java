package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentParamsDTO implements Serializable {
    private static final long serialVersionUID = -2910968197817656443L;

    private String timeStamp;

    /**
     * nonceStr
     */
    private String nonceStr;

    /**
     * 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***
     */
    @JSONField(name="package")
    private String prepayId;

    private String paySign;

    private String signType;

}
