package com.wanmi.sbc.account.finance.record.model.response;

import com.wanmi.sbc.account.bean.enums.SettleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TotalSettlement implements Serializable {

    /**
     * 结算金额
     */
    BigDecimal totalAmount;

    /**
     * 结算状态
     */
    private SettleStatus settleStatus;

    private Long storeId;
}
