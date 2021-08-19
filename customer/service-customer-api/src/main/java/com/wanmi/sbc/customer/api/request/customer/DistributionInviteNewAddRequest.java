package com.wanmi.sbc.customer.api.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 新增邀新记录请求
 * @Autho qiaokang
 * @Date：2019-03-04 14:35:39
 */
@ApiModel
@Data
public class DistributionInviteNewAddRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -7136373208496469361L;

    /**
     * 受邀人ID
     */
    @ApiModelProperty(value = "受邀人ID")
    @NotNull
    private String invitedNewCustomerId;

    /**
     * 是否有效邀新，0：否，1：是
     */
    @ApiModelProperty(value = "是否有效邀新，0：否，1：是")
    private InvalidFlag availableDistribution;

    /**
     * 注册时间
     */
    @ApiModelProperty(value = "注册时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime registerTime;

    /**
     * 首次下单时间
     */
    @ApiModelProperty(value = "首次下单时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime firstOrderTime;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /**
     * 订单完成时间
     */
    @ApiModelProperty(value = "订单完成时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderFinishTime;

    /**
     * 奖励是否入账，0：否，1：是
     */
    @ApiModelProperty(value = "奖励是否入账，0：否，1：是")
    private InvalidFlag rewardRecorded;

    /**
     * 奖励入账时间
     */
    @ApiModelProperty(value = "奖励入账时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime rewardRecordedTime;

    /**
     * 奖励金额(元)
     */
    @ApiModelProperty(value = "奖励金额(元)")
    private BigDecimal rewardCash;

    /**
     * 后台配置的奖励优惠券id，多个以逗号分隔
     */
    @ApiModelProperty(value = "后台配置的奖励优惠券id，多个以逗号分隔")
    private String settingCoupons;

    /**
     * 预计奖励金额(元)
     */
    @ApiModelProperty(value = "后台配置的奖励金额")
    private BigDecimal settingAmount;

    /**
     * 邀请人id
     */
    @ApiModelProperty(value = "邀请人id")
    private String requestCustomerId;

    /**
     * 是否分销员，0：否 1：是
     */
    @ApiModelProperty(value = "是否分销员，0：否 1：是")
    private Integer distributor;


    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    private TerminalSource terminalSource;

}
