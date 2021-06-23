package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@ApiModel
@Data
public class DistributorCustomerInviteInfoReviseRequest {

    @ApiModelProperty(value = "邀请人会员id")
    @NotBlank
    private String customerId;

    /**
     * 分销员标识UUID
     */
    @ApiModelProperty(value = "分销员标识UUID")
    @NotBlank
    private String distributionId;

}
