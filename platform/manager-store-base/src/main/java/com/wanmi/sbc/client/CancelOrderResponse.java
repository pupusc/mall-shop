package com.wanmi.sbc.client;

import lombok.Data;

@Data
public class CancelOrderResponse {
    private Integer status;
    private String errorMsg;
    private String orderID;
}
