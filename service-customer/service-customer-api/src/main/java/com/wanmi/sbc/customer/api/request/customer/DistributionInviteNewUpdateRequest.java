package com.wanmi.sbc.customer.api.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel
@Data
public class DistributionInviteNewUpdateRequest {
    /**
     * 受邀人ID
     */
    @ApiModelProperty(value = "受邀人ID")
    @NotBlank
    private String invitedNewCustomerId;

    /**
     * 是否有效邀新，0：否，1：是
     */
    @ApiModelProperty(value = "是否有效邀新，0：否，1：是")
    @NotNull
    private InvalidFlag availableDistribution;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @NotBlank
    private String orderCode;

    /**
     * 订单完成时间
     */
    @ApiModelProperty(value = "订单完成时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime orderFinishTime;

    /**
     * 奖励是否入账，0：否，1：是
     */
    @ApiModelProperty(value = "奖励是否入账，0：否，1：是")
    @NotNull
    private InvalidFlag rewardRecorded;

    /**
     * 奖励入账时间
     */
    @ApiModelProperty(value = "奖励入账时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    private LocalDateTime rewardRecordedTime;

    @ApiModelProperty(value = "是否超过邀新奖励上限")
    @NotNull
    private DefaultFlag overLimit;

    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    private TerminalSource terminalSource;

}
