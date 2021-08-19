package com.wanmi.sbc.elastic.api.constant;

/**
 * @Author yangzhen
 * @Description //MQ消息目的地
 * @Date 10:13 2020/12/16
 * @Param
 * @return
 **/
public class JmsDestinationConstants {


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
     * 邀新注册-发放奖励奖金
     */
    public static final String Q_ES_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT = "q.es.account.funds.invite.grant.amount";

    /**
     * 修改es会员信息
     */
    public static final String Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS = "q.es.modify.or.add.customer.funds";


    /**
     * 批量修改es会员信息
     */
    public static final String Q_ES_MODIFY_OR_ADD_CUSTOMER_FUNDS_LIST = "q.es.modify.or.add.customer.funds.list";
}
