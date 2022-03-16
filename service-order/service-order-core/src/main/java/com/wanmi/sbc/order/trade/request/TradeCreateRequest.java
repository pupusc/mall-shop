package com.wanmi.sbc.order.trade.request;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class TradeCreateRequest extends TradeRemedyRequest {

    private static final long serialVersionUID = 3529565997588014310L;
    private String custom;
    private String outTradeNo;
}
