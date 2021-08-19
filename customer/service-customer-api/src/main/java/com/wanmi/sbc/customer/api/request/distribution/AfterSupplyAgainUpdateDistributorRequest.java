package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@ApiModel
@Data
public class AfterSupplyAgainUpdateDistributorRequest {

    @ApiModelProperty(value = "分销员id")
    @NotBlank
    private String distributeId;

    @ApiModelProperty(value = "发放邀新奖励金额")
    @Min(0)
    private BigDecimal rewardCash;

    @ApiModelProperty(value = "未发放的邀新奖励金额")
    @Min(0)
    private BigDecimal rewardCashNotRecorded;
}
