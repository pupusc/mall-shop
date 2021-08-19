package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>分销员邀新信息VO</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
public class DistributionCustomerInviteInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 分销员标识UUID
     */
    @ApiModelProperty(value = "分销员标识UUID")
    private String distributionId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 已发放邀新奖励现金人数
     */
    @ApiModelProperty(value = "已发放邀新奖励现金人数")
    private Integer rewardCashCount;

    /**
     * 达到上限未发放奖励现金人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励现金人数")
    private Integer rewardCashLimitCount;


    /**
     * 达到上限未发放奖励现金有效邀新人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励现金有效邀新人数")
    private Integer rewardCashAvailableLimitCount;

    /**
     * 已发放邀新奖励优惠券人数-至
     */
    @ApiModelProperty(value = "已发放邀新奖励优惠券人数")
    private Integer rewardCouponCount;

    /**
     * 达到上限未发放奖励优惠券人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励优惠券金人数")
    private Integer rewardCouponLimitCount;

    /**
     * 达到上限未发放奖励优惠券有效邀新人数-从
     */
    @ApiModelProperty(value = "达到上限未发放奖励优惠券金有效邀新人数")
    private Integer rewardCouponAvailableLimitCount;

}