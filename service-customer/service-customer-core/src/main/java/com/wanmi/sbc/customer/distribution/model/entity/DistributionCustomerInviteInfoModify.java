package com.wanmi.sbc.customer.distribution.model.entity;


import com.wanmi.sbc.customer.bean.enums.FailReasonFlag;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerInviteInfoModify implements Serializable {

    /**
     * 分销员
     */
    private DistributionCustomer distributionCustomer;

    /**
     * 奖金
     */
    private InviteNewRecord rewardCashResult;

    /**
     * 优惠券
     */
    private InviteNewRecord rewardCouponResult;

    /**
     * 奖金-失败原因（旧）
     */
    private FailReasonFlag failReasonFlag_cash_old;

    /**
     * 优惠券-失败原因（旧）
     */
    private FailReasonFlag failReasonFlag_coupon_old;

    /**
     * 奖金-奖励是否入账（旧）
     */
    private InvalidFlag rewardRecorded_cash_old;

    /**
     * 优惠券-奖励是否入账（旧）
     */
    private InvalidFlag rewardRecorded_coupon_old;
}