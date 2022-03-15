package com.fangdeng.server.dto;

import lombok.Data;

@Data
public class CancelOrderDTO {
    private String orderId;
    private String pid;
    /**
     * 取消类型1=>单品取消，2=>整单取消
     */
    private Integer type = 2;

    private String erpGoodsInfoNo;
}
