package com.wanmi.sbc.account.finance.record.model.entity;

import com.wanmi.sbc.account.bean.enums.PayWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>对账单支付项金额统计结构</p>
 * Created by of628-wenzhi on 2017-12-08-上午10:38.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayItemRecord {

    private Long storeId;

    private PayWay payWay;

    private BigDecimal amount;
}
