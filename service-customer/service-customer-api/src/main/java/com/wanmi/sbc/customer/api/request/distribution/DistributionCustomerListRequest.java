package com.wanmi.sbc.customer.api.request.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>分销员列表查询请求参数</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerListRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 1L;
    /**
     * 批量查询-分销员标识UUIDList
     */
    @ApiModelProperty(value = "批量查询-分销员标识")
    private List<String> distributionIdList;

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
     * 搜索条件:创建时间开始
     */
    @ApiModelProperty(value = "搜索条件:创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @ApiModelProperty(value = "搜索条件:创建时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;


    /**
     * 是否禁止分销 0: 启用中  1：禁用中
     */
    @ApiModelProperty(value = "是否禁止分销")
    private DefaultFlag forbiddenFlag;


    /**
     * 是否有分销员资格0：否，1：是
     */
    @ApiModelProperty(value = "是否有分销员资格")
    private DefaultFlag distributorFlag;

    /**
     * 邀新人数-从
     */
    @ApiModelProperty(value = "邀新人数-从")
    private Integer inviteCountStart;

    /**
     * 邀新人数-至
     */
    @ApiModelProperty(value = "邀新人数-至")
    private Integer inviteCountEnd;

    /**
     * 有效邀新人数-从
     */
    @ApiModelProperty(value = "有效邀新人数-从")
    private Integer inviteAvailableCountStart;

    /**
     * 有效邀新人数-至
     */
    @ApiModelProperty(value = "有效邀新人数-至")
    private Integer inviteAvailableCountEnd;

    /**
     * 邀新奖金(元)-从
     */
    @ApiModelProperty(value = "邀新奖金(元)-从")
    private BigDecimal rewardCashStart;
    /**
     * 邀新奖金(元)-至
     */
    @ApiModelProperty(value = "邀新奖金(元)-至")
    private BigDecimal rewardCashEnd;

    /**
     * 分销订单(笔)-从
     */
    @ApiModelProperty(value = "分销订单(笔)-从")
    private Integer distributionTradeCountStart;

    /**
     * 分销订单(笔)-至
     */
    @ApiModelProperty(value = "分销订单(笔)-至")
    private Integer distributionTradeCountEnd;

    /**
     * 销售额(元)-从
     */
    @ApiModelProperty(value = "销售额(元)-从")
    private BigDecimal salesStart;

    /**
     * 销售额(元)-至
     */
    @ApiModelProperty(value = "销售额(元)-至")
    private BigDecimal salesEnd;

    /**
     * 分销佣金(元)-从
     */
    @ApiModelProperty(value = "分销佣金(元)-从")
    private BigDecimal commissionStart;

    /**
     * 分销佣金(元)-至
     */
    @ApiModelProperty(value = "分销佣金(元)-至")
    private BigDecimal commissionEnd;

    /**
     * 未入账分销佣金(元)
     */
    @ApiModelProperty(value = "未入账分销佣金(元)")
    private BigDecimal commissionNotRecorded;


}