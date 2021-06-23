package com.wanmi.sbc.customer.api.request.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>分销员修改参数</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class DistributionCustomerModifyRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 1L;


    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    @NotBlank
    @Length(max = 32)
    private String customerId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    @NotBlank
    @Length(max = 128)
    private String customerName;

    /**
     * 会员登录账号|手机号
     */
    @ApiModelProperty(value = "会员登录账号|手机号")
    @NotBlank
    @Length(max = 20)
    private String customerAccount;


    /**
     * 是否有分销员资格0：否，1：是
     */
    @ApiModelProperty(value = "是否有分销员资格0：否，1：是")
    @NotNull
    @Max(127)
    private DefaultFlag distributorFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 邀新人数
     */
    @ApiModelProperty(value = "邀新人数")
    @Max(9999999999L)
    private Integer inviteCount;

    /**
     * 有效邀新人数
     */
    @ApiModelProperty(value = "有效邀新人数")
    @Max(9999999999L)
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
    @Max(9999999999L)
    private Integer distributionTradeCount;

    /**
     * 销售额(元)
     */
    @ApiModelProperty(value = "销售额(元)")
    private BigDecimal sales;

    /**
     * 分销佣金(元)
     */
    @ApiModelProperty(value = "分销佣金(元)")
    private BigDecimal commission;

    /**
     * 未入账分销佣金(元)
     */
    @ApiModelProperty(value = "未入账分销佣金(元)")
    private BigDecimal commissionNotRecorded;

}