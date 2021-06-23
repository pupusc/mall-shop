package com.wanmi.sbc.account.finance.record.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p>财务模块Utils</p>
 * Created by of628-wenzhi on 2017-12-08-下午3:13.
 */
public class FinanceUtils {

    public static final String _AMOUNT_PATTERN_STYLE_NORM = "#,##0.00";

    /**
     * 自定义格式化金额
     *
     * @return 金额格式化String
     */
    public static String amountFormatter(BigDecimal amount, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(amount);
    }

    /**
     * 格式化金额，格式：#,###.00
     *
     * @return 金额格式化String
     */
    public static String amountFormatter(BigDecimal amount) {
        return amountFormatter(amount, _AMOUNT_PATTERN_STYLE_NORM);
    }

}
