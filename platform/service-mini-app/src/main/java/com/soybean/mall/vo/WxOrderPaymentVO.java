package com.soybean.mall.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxOrderPaymentVO implements Serializable {
    private static final long serialVersionUID = 7529014926249816819L;

    /**
     * 时间戳，从 1970 年 1 月 1 日 00:00:00 至今的秒数，即当前的时间
     */
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

    private WxOrderCommitResultVO orderInfo;

    private String orderInfoStr;

    private Boolean couponFlag;

}
