package com.wanmi.sbc.customer.api.constant;

/**
 * Created by feitingting on 2019/4/18.
 */
public final class DistributionCommissionRedisKey {
    /**
     * 佣金总额
     */
    public final static String DISTRIBUTION_COMMISSION_TOTAL="customer:commission:commissionTotal";

    /**
     * 分销佣金总额
     */
    public final static String DISTRIBUTION_COMMISSION="customer:commission:commission";

    /**
     * 邀新奖励总额
     */
    public final static String DISTRIBUTION_REWARD_CASH="customer:commission:rewardCash";

    /**
     * 未入账分销佣金总额
     */
    public final static String DISTRIBUTION_COMMISSION_NOTRECORDED="customer:commission:commissionNotRecorded";

    /**
     * 未入账邀新奖金总额
     */
    public final static String DISTRIBUTION_REWARD_CASH_NOTRECORDED="customer:commission:rewardCashNotRecorded";
}
