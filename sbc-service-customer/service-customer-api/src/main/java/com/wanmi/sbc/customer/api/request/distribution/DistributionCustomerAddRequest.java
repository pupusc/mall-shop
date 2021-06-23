package com.wanmi.sbc.customer.api.request.distribution;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * <p>分销员新增参数</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class DistributionCustomerAddRequest extends CustomerBaseRequest {
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
    private Integer inviteCount;

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
     * 邀请人会员ID集合
     */
    @ApiModelProperty(value = "邀请人会员ID集合")
    private String inviteCustomerIds;
}