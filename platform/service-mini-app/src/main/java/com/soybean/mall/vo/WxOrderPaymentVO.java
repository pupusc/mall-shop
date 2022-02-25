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

    private String prepayId;

    private WxOrderCommitResultVO orderInfo;

}
