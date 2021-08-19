package com.wanmi.sbc.account.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 提现金额状态
 * @author: Geek Wang
 * @createDate: 2019/2/20 14:19
 * @version: 1.0
 */
@ApiEnum
public enum WithdrawAmountStatus {

    /**
     * 提交
     */
    @ApiEnumProperty("提交")
    SUBMIT,

    /**
     * 同意
     */
    @ApiEnumProperty("同意")
    AGREE,

    /**
     * 驳回
     */
    @ApiEnumProperty("驳回")
    REJECT;

    WithdrawAmountStatus() {
    }
}
