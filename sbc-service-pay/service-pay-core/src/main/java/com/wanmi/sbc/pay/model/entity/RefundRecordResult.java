package com.wanmi.sbc.pay.model.entity;

import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import lombok.Data;

/**
 * <p>封装第三方退款结果的返回信息</p>
 * Created by of628-wenzhi on 2017-08-10-下午3:59.
 */
@Data
public class RefundRecordResult {

    /**
     * 退款对象
     */
    private Object object;

    /**
     * 退款记录，这里同样包含了退款对象的基本信息，考虑到通用性，做扁平化处理
     */
    private PayTradeRecord record;
}
