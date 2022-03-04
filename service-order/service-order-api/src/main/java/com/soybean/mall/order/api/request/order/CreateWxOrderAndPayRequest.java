package com.soybean.mall.order.api.request.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateWxOrderAndPayRequest implements Serializable {
    private static final long serialVersionUID = 1071264710390642647L;

    private String tid;


}
