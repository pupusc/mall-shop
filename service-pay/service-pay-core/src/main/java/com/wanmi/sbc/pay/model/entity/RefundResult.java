package com.wanmi.sbc.pay.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>渠道方返回的退款结果数据</p>
 * Created by of628-wenzhi on 2017-08-05-上午11:30.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundResult {

    /**
     * 退款对象id
     */
    private String refundObjectId;

    /**
     * 支付对象id
     */
    private String payObjectId;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 退款创建时间
     */
    private LocalDateTime createTime;

    /**
     * 渠道方返回的退款对象
     */
    private Object data;

}
