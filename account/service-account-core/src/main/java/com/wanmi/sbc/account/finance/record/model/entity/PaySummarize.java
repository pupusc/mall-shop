package com.wanmi.sbc.account.finance.record.model.entity;

import com.wanmi.sbc.account.bean.enums.PayWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>对账支付/退款汇总</p>
 * Created by of628-wenzhi on 2017-12-11-下午3:26.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaySummarize {

    private PayWay payWay;

    private BigDecimal sumAmount;
}
