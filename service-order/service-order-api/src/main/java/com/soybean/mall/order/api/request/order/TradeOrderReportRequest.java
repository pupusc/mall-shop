package com.soybean.mall.order.api.request.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class TradeOrderReportRequest  implements Serializable {
    private static final long serialVersionUID = 981932685479709613L;

    private String tid;
}
