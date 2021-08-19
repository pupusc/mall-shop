package com.wanmi.sbc.order.trade.model.entity;

import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>在线支付批量回调参数</p>
 * Created by of628-wenzhi on 2019-07-25-16:13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayCallBackOnlineBatch {
    /**
     * 交易单
     */
    private Trade trade;

    /**
     * 支付单
     */
    private PayOrder payOrderOld;
}
