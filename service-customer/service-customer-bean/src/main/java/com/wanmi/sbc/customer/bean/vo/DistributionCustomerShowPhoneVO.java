package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author:  houshuai
 * @date: 2020/12/9 16:15
 * @description:
 */
@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerShowPhoneVO {
    private static final long serialVersionUID = 1L;

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
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 会员登录账号|手机号
     */
    @ApiModelProperty(value = "会员登录账号|手机号")
    private String customerAccount;

    /**
     * 会员头像
     */
    @ApiModelProperty(value = "会员头像")
    private String headImg;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人(后台新增分销员)
     */
    @ApiModelProperty(value = "创建人(后台新增分销员)")
    private String createPerson;

    /**
     * 是否删除标志 0：否，1：是
     */
    @ApiModelProperty(value = "是否删除标志 0：否，1：是")
    private DeleteFlag delFlag;

    /**
     * 是否禁止分销 0: 启用中  1：禁用中
     */
    @ApiModelProperty(value = "是否禁止分销 0: 启用中  1：禁用中")
    private DefaultFlag forbiddenFlag;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbiddenReason;

    /**
     * 是否有分销员资格0：否，1：是
     */
    @ApiModelProperty(value = "是否有分销员资格0：否，1：是")
    private DefaultFlag distributorFlag;

    /**
     * 邀新人数
     */
    @ApiModelProperty(value = "邀新人数")
    private Integer inviteCount;

    /**
     * 有效邀新人数
     */
    @ApiModelProperty(value = "有效邀新人数")
    private Integer inviteAvailableCount;

    /**
     * 邀新奖金(元)
     */
    @ApiModelProperty(value = "邀新奖金(元)")
    private BigDecimal rewardCash;

    /**
     * 未入账邀新奖金(元)
     */
    @ApiModelProperty(value = "未入账邀新奖金(元)")
    private BigDecimal rewardCashNotRecorded;

    /**
     * 分销订单(笔)
     */
    @ApiModelProperty(value = "分销订单(笔)")
    private Integer distributionTradeCount;


    /**
     * 销售额(元)
     */
    @ApiModelProperty(value = "销售额(元) ")
    private BigDecimal sales;

    /**
     * 分销佣金(元)
     */
    @ApiModelProperty(value = "分销佣金(元) ")
    private BigDecimal commission;

    /**
     * 未入账分销佣金(元)
     */
    @ApiModelProperty(value = "未入账分销佣金(元) ")
    private BigDecimal commissionNotRecorded;

    /**
     * 佣金总额(元)
     */
    @ApiModelProperty(value = "佣金总额(元) ")
    private BigDecimal commissionTotal;

    /**
     * 分销员等级ID
     */
    @ApiModelProperty(value = "分销员等级ID ")
    private String distributorLevelId;

    /**
     * 分销员等级名称
     */
    @ApiModelProperty(value = "分销员等级名称 ")
    private String distributorLevelName;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码 ")
    private String inviteCode;

    /**
     * 邀请人会员ID集合，后期可扩展N级
     */
    @ApiModelProperty(value = "邀请人会员ID集合，后期可扩展N级")
    private String inviteCustomerIds;
}
