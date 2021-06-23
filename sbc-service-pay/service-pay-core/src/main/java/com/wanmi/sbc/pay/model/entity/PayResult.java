package com.wanmi.sbc.pay.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>渠道方返回的支付结果数据</p>
 * Created by of628-wenzhi on 2017-08-05-上午11:29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayResult {

    /**
     * 支付对象id
     */
    private String objectId;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 支付创建时间
     */
    private LocalDateTime createTime;

    /**
     * 渠道方返回的具体支付对象
     */
    private Object data;

}
