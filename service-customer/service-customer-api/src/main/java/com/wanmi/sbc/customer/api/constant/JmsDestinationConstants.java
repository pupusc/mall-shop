package com.wanmi.sbc.customer.api.constant;

/**
 * MQ消息目的地
 * @author: Geek Wang
 * @createDate: 2019/2/25 13:57
 * @version: 1.0
 */
public class JmsDestinationConstants {

    /**
     * 会员账户修改,触发账户模块-会员资金的会员账户修改
     */
    public static final String Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT = "q.account.funds.modify.customer.account";


    /**
     * 会员名称修改,触发账户模块-会员资金的会员名称修改
     */
    public static final String Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME = "q.account.funds.modify.customer.name";

    /**
     * 会员名称、账号修改,触发账户模块-会员资金的会员名称、账号修改
     */
    public static final String Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT = "q.account.funds.modify.customer.name.and.account";

    /**
     * 新增会员，初始化会员资金信息
     */
    public static final String Q_ACCOUNT_FUNDS_ADD_INIT = "q.account.funds.add.init";

    /**
     * 新增分销员，更新会员资金-是否分销员字段
     */
    public static final String Q_ACCOUNT_FUNDS_MODIFY_IS_DISTRIBUTOR = "q.account.funds.modify.is.distributor";

    /**
     *  会员账户信息修改,触发账户模块-会员提现管理的会员名称修改
     */
    public static final String Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_NAME = "q.account.draw.cash.modify.customer.name";

    /**
     *  会员账户信息修改,触发账户模块-会员提现管理的会员账户修改
     */
    public static final String Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_ACCOUNT = "q.account.draw.cash.modify.customer.account";

    /**
     * 邀新注册-发放奖励奖金
     */
    public static final String Q_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT = "q.account.funds.invite.grant.amount";

    /**
     * 邀新注册-发放优惠券
     */
    public static final String Q_MARKET_COUPON_INVITE_NEW_ADD = "q.market.coupon.invite.new.add";

    /**
     * 消息发送
     */
    public static final String Q_SMS_SERVICE_MESSAGE_SEND = "q.sms.service.message.send";


    /**
     * 业务员交接
     */
    public static final String Q_ORDER_SERVICE_MODIFY_EMPLOYEE_DATA = "q.order.service.modify.employee.data";




    /**
     * 会员账户修改,触发账户模块-ES会员资金的会员账户修改
     */
    public static final String Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT = "q.es.account.funds.modify.customer.account";


    /**
     * 会员名称修改,触发账户模块-会员资金的会员名称修改
     */
    public static final String Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME = "q.es.account.funds.modify.customer.name";

    /**
     * 会员名称、账号修改,触发账户模块-会员资金的会员名称、账号修改
     */
    public static final String Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT = "q.es.account.funds.modify.customer.name.and.account";

    /**
     * 新增会员，初始化会员资金信息
     */
    public static final String Q_ES_ACCOUNT_FUNDS_ADD_INIT = "q.es.account.funds.add.init";

    /**
     * 新增分销员，更新会员资金-是否分销员字段
     */
    public static final String Q_ES_ACCOUNT_FUNDS_MODIFY_IS_DISTRIBUTOR = "q.es.account.funds.modify.is.distributor";


    /**
     * 邀新注册-发放奖励奖金
     */
    public static final String Q_ES_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT = "q.es.account.funds.invite.grant.amount";


}
