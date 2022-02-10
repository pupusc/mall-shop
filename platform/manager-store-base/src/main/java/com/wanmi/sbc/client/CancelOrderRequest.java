package com.wanmi.sbc.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderRequest {
    private String orderId;
    private String pid;
    /**
     * 取消类型1=>单品取消，2=>整单取消
     */
    private Integer type;

    private String erpGoodsInfoNo;
}
