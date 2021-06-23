package com.wanmi.sbc.pay.model.entity;

import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>封装请求第三方支付结果返回信息</p>
 * Created by of628-wenzhi on 2017-08-08-下午5:38.
 */
@Data
public class PayRecordResult {

    /**
     * 支付对象
     */
    private Object object;

    /**
     * 支付记录，这里同样包含了支付对象的基本信息，考虑到通用性，做扁平化处理
     */
    private PayTradeRecord record;

    /**
     * 支付对象过期时间
     */
    private LocalDateTime timeExpire;
}
