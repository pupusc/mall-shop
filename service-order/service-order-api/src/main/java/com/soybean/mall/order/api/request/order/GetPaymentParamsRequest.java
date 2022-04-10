package com.soybean.mall.order.api.request.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetPaymentParamsRequest implements Serializable {
    private static final long serialVersionUID = 588317631734366192L;
    private String tid;
}
