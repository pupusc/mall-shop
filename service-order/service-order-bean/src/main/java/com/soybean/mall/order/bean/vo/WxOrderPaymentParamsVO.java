package com.soybean.mall.order.bean.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class WxOrderPaymentParamsVO implements Serializable {
    private static final long serialVersionUID = -133798207502185193L;

    private String timeStamp;

    /**
     * nonceStr
     */
    private String nonceStr;

    /**
     * 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***
     */
    private String prepayId;

    private String paySign;

    private String signType;
}
