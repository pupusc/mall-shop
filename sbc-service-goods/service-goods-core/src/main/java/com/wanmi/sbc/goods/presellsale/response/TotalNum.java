package com.wanmi.sbc.goods.presellsale.response;

import lombok.Data;

@Data
public class TotalNum {
    /**
     * 支付定金
     */
    private Integer handselTotalNum;

    /**
     * 支付尾款
     */
    private Integer finalPaymentNum;

    /**
     * 支付全款
     */
    private Integer fullPaymentNum;

}
