package com.wanmi.sbc.order.open.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-04-02 17:57:00
 */
@AllArgsConstructor
@Data
public class FddsOrderCreateResultData {
    /**
     * 合作方交易单号
     */
    private String tradeNo;
    /**
     * 订单编号
     */
    private Long orderNumber;
}
