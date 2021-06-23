package com.wanmi.sbc.customer.distribution.model.root;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>分销员邀新信息</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Data
@Entity
@Table(name = "distribution_customer_invite_info")
public class DistributionCustomerInviteInfo implements Serializable {


    private static final long serialVersionUID = 3937959811458148990L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 分销员标识UUID
     */
    @Column(name = "distribution_id")
    private String distributionId;

    /**
     * 会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 已发放邀新奖励现金人数
     */
    @Column(name = "reward_cash_count")
    private Integer rewardCashCount;

    /**
     * 达到上限未发放奖励现金人数-从
     */
    @Column(name = "reward_cash_limit_count")
    private Integer rewardCashLimitCount;

    /**
     * 达到上限未发放奖励现金有效邀新人数-从
     */
    @Column(name = "reward_cash_available_limit_count")
    private Integer rewardCashAvailableLimitCount;

    /**
     * 已发放邀新奖励优惠券人数-至
     */
    @Column(name = "reward_coupon_count")
    private Integer rewardCouponCount;

    /**
     * 达到上限未发放奖励优惠券人数-从
     */
    @Column(name = "reward_coupon_limit_count")
    private Integer rewardCouponLimitCount;

    /**
     * 达到上限未发放奖励优惠券有效邀新人数-从
     */
    @Column(name = "reward_coupon_available_limit_count")
    private Integer rewardCouponAvailableLimitCount;
}