package com.fangdeng.server.dto;

import lombok.Data;

@Data
public class CancelOrderDTO {
    private String orderId;
    private String pid;
}
