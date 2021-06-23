package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@ApiModel
@Data
public class DistributorCustomerInviteInfoUpdateRequest {

    @ApiModelProperty(value = "分销员id")
    @NotBlank
    private String distributeId;

    @ApiModelProperty(value = "发放邀新奖金人数")
    @Min(0)
    private Integer rewardCashCount = 0;

    @ApiModelProperty(value = "未发放邀新奖金人数")
    @Min(0)
    private Integer rewardCashLimitCount = 0;

    @ApiModelProperty(value = "未发放邀新奖金有效邀新人数")
    @Min(0)
    private Integer rewardCashAvailableLimitCount = 0;

    @ApiModelProperty(value = "发放邀新奖励金额")
    @Min(0)
    private BigDecimal rewardCash;

    @ApiModelProperty(value = "发放邀新优惠券人数")
    @Min(0)
    private Integer rewardCouponCount = 0;

    @ApiModelProperty(value = "未发放邀新优惠券人数")
    @Min(0)
    private Integer rewardCouponLimitCount = 0;

    @ApiModelProperty(value = "未发放邀新优惠券有效邀新人数")
    @Min(0)
    private Integer rewardCouponAvailableLimitCount = 0;

}
