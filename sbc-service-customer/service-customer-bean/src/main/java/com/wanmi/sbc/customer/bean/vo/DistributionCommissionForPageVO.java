package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by feitingting on 2019/2/26.
 */
@ApiModel
@Data
public class DistributionCommissionForPageVO {
    /**
     * 分销员ID
     */
    @ApiModelProperty(value = "分销员ID")
    private String  distributionId;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    private String customerName;

    /**
     * 会员登录账号|手机号
     */
    @ApiModelProperty(value = "分销员账号")
    private String customerAccount;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "分销员账号")
    private String customerId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 账号状态 0：启用中 1：禁用中
     */
    @ApiModelProperty(value = "禁用标识")
    private DefaultFlag forbiddenFlag = DefaultFlag.NO;


    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbiddenReason;

    /**
     * 佣金总额
     */
    @ApiModelProperty(value = "佣金总额")
    private BigDecimal commissionTotal;

    /**
     * 已入账分销佣金
     */
    @ApiModelProperty(value = "已入账分销佣金")
    private BigDecimal commission;

    /**
     * 已入账邀新奖金
     */
    @ApiModelProperty(value = "已入账邀新奖金")
    private BigDecimal rewardCash;

    /**
     * 未入账分销佣金
     */
    @ApiModelProperty(value = "未入账分销佣金")
    private BigDecimal commissionNotRecorded;

    /**
     * 未入账邀新奖金
     */
    @ApiModelProperty(value = "未入账邀新奖金")
    private BigDecimal rewardCashNotRecorded;

    /**
     * 分销员等级ID
     */
    @ApiModelProperty(value = "分销员等级ID")
    private String distributorLevelId;

    /**
     * 分销员等级名称
     */
    @ApiModelProperty(value = "分销员等级名称 ")
    private String distributorLevelName;
}
