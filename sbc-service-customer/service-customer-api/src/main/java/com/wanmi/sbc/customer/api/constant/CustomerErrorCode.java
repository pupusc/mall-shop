package com.wanmi.sbc.customer.api.constant;

/**
 * <p>会员异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午3:50.
 */
public final class CustomerErrorCode {
    private CustomerErrorCode() {
    }

    /**
     * 会员不存在
     */
    public final static String NOT_EXIST = "K-010001";

    /**
     * 客户不存在
     */
    public final static String CUSTOMER_NOT_EXIST = "K-010016";

    /**
     * 用户在store_customer_rela表中不存在
     */
    public final static String CUSTOMER_NOT_EXIST_IN_COMPANY = "K-010201";

    /**
     * 客户已经存在于该商家名下
     */
    public final static String CUSTOMER_EXIST_IN_COMPANY = "K-010202";

    /**
     * 用户状态异常
     */
    public final static String CUSTOMER_STATUS_ERROR = "K-010101";

    /**
     * 重置的账号重复
     */
    public final static String CUSTOMER_ACCOUNT_EXISTS_FOR_RESET = "K-010203";

    /**
     * 会员支付密码错误
     */
    public final static String CUSTOMER_PAY_PASSWORD_ERROR = "K-010204";

    /**
     * 会员支付密码错误,账户冻结
     */
    public final static String CUSTOMER_PAY_LOCK_ERROR = "K-010205";

    /**
     * 未设置会员支付密码
     */
    public final static String NO_CUSTOMER_PAY_PASSWORD = "K-010206";

    /**
     * 会员支付密码错误,账户冻结，提示解冻时间
     */
    public final static String CUSTOMER_PAY_LOCK_TIME_ERROR = "K-010207";

    /**
     * 会员积分不足
     */
    public final static String CUSTOMER_POINTS_NOT_ENOUGH_ERROR = "K-010208";

    /**
     * 分销员等级已关联了分销员
     */
    public final static String DISTRIBUTOR_LEVEL_BE_RELATED = "K-010209";

    /**
     * 分销员等级数量不能超过5个
     */
    public final static String DISTRIBUTOR_LEVEL_COUNT_ERROR = "K-010210";

    /**
     * 下级门槛要比上级的大
     */
    public final static String DISTRIBUTOR_LEVEL_THRESHOLD_ERROR = "K-010211";

    /**
     * 存在佣金比例+佣金提成比例相加大于100%的情况
     */
    public final static String DISTRIBUTOR_LEVEL_COMMISSION_ERROR = "K-010212";

    /**
     * 必须勾选一个升级规则
     */
    public final static String DISTRIBUTOR_LEVEL_THRESHOLD_EMPTY_ERROR = "K-010213";

    /**
     * 已经是付费会员
     */
    public final static String PAID_CUSTOMER_EXIST = "K-010214";

}
