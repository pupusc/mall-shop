package com.wanmi.sbc.account.api.constant;

/**
 * <p>账户异常码定义</p>
 * Created by cl on 2019-05-09-下午4:22.
 */
public final class CustomerDrawCashErrorCode {
    private CustomerDrawCashErrorCode() {
    }

    /**
     * 账户可提现金额不足
     */
    public final static String BALANCE_NOT_ENOUGH = "K-091011";

    /**
     * 提现金额超出当天最大提现金额
     */
    public final static String BEYOND_MAX_DRAW_CASH = "K-091012";

}
