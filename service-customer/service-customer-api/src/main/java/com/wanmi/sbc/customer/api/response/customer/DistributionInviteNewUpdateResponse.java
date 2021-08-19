package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionInviteNewUpdateResponse implements Serializable{
    /**
     *
     */
    @ApiModelProperty(value = "邀新人数")
    @NotBlank
    private Integer inviteNum;

    @ApiModelProperty("邀新奖励金额")
    private BigDecimal inviteAmount;

}
