package com.wanmi.sbc.customer.account.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 客户账户信息编辑
 * Created by CHENLI on 2017/4/21.
 */
@Data
public class CustomerAccountSaveRequest {
    /**
     * 账户ID
     */
    private String customerAccountId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 账户名字
     */
    @NotBlank
    private String customerAccountName;

    /**
     * 银行账号
     */
    @NotBlank
    private String customerAccountNo;

    /**
     * 开户行
     */
    @NotBlank
    private String customerBankName;
}
