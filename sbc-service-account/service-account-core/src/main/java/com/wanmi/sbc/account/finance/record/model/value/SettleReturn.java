package com.wanmi.sbc.account.finance.record.model.value;

import com.wanmi.sbc.account.bean.enums.ReturnStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by hht on 2017/12/7.
 */
@Data
public class SettleReturn {

    /**
     * 退单状态
     */
    private ReturnStatus returnStatus;

    /**
     * 退货数量
     */
    private long returnNum;

    /**
     * 退单的应退均摊价
     */
    private BigDecimal splitReturnPrice;
}
