package com.wanmi.sbc.account.api.constant;

/**
 * <p>账户异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午4:22.
 */
public final class AccountErrorCode {
    private AccountErrorCode() {
    }

    /**
     * 修改线下账户失败
     */
    public final static String MODIFY_ACCOUNT_FAILED = "K-020001";

    /**
     * 开票项目已存在
     */
    public final static String INVOICE_PROJECT_EXIST = "K-020003";

    /**
     * 支持的开票类型不存在
     */
    public final static String INVOICE_SWITCH_NOT_EXIST = "K-020011";

    /**
     * 不支持开票
     */
    public final static String INVOICE_NOT_SUPPORT = "K-020012";

    /**
     * 一个会员最多可保存5个银行账号
     */
    public final static String ACCOUNT_MAX_FAILED = "K-020013";

    /**
     * 银行账号{0}已存在
     */
    public final static String BANK_ACCOUNT_EXIST = "K-070006";

    /**
     * 账户总数限制错误
     */
    public final static String ACCOUNT_MAX_LIMIT = "K-070007";



}
