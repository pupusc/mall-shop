package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;

@ApiModel
@Data
public class DistributionInviteNewSupplyAgainRequest {


    /**
     * 邀新记录表主键
     */
    @ApiModelProperty(value = "邀新记录表主键ID")
    @NotBlank
    private String recordId;

    /**
     * 奖励是否入账，0：否，1：是
     */
    @ApiModelProperty(value = "奖励是否入账，0：否，1：是")
    private InvalidFlag rewardRecorded;

    /**
     * 奖励金额(元)
     */
    @ApiModelProperty(value = "奖励金额(元)")
    private BigDecimal rewardCash;

    /**
     * 奖励优惠券
     */
    @ApiModelProperty(value = "奖励优惠券")
    private String rewardCoupon;

    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    private TerminalSource terminalSource;

}
