package com.wanmi.sbc.client;

import lombok.Data;

@Data

public class CancelOrderRequest {
    private String orderId;
    private String pid;
}
