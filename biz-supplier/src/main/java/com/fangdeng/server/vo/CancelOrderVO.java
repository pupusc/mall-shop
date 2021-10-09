package com.fangdeng.server.vo;

import lombok.Data;

@Data
public class CancelOrderVO {
    private Integer status;
    private String errorMsg;
    private String orderID;
}
