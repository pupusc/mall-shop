package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * @Description: 分销员奖励更新请求
 * @Autho qiaokang
 * @Date：2019-03-14 14:17:31
 */
@ApiModel
@Data
public class DistributionCustomerModifyRewardRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -7874334103821642420L;

    /**
     * 分销员标识UUID
     */
    @ApiModelProperty(value = "分销员标识UUID")
    @NotBlank
    private String distributionId;

    /**
     * 邀新人数
     */
    @ApiModelProperty(value = "邀新人数")
    private Integer inviteCount = 0;

    /**
     * 邀新奖金(元)
     */
    @ApiModelProperty(value = "邀新奖金(元)")
    private BigDecimal rewardCash =  BigDecimal.ZERO;

    /**
     * 未入账邀新奖金(元)
     */
    @ApiModelProperty(value = "未入账邀新奖金(元)")
    private BigDecimal rewardCashNotRecorded =  BigDecimal.ZERO;

    /**
     * 分销订单(笔)
     */
    @ApiModelProperty(value = "分销订单(笔)")
    private Integer distributionTradeCount = 0 ;

    /**
     * 销售额(元)
     */
    @ApiModelProperty(value = "销售额(元)")
    private BigDecimal sales =  BigDecimal.ZERO;

    /**
     * 未入账分销佣金(元)
     */
    @ApiModelProperty(value = "未入账分销佣金(元)")
    private BigDecimal commissionNotRecorded =  BigDecimal.ZERO;

    /**
     * 佣金总额
     */
    @ApiModelProperty(value = "邀新奖金(元)")
    private BigDecimal commissionTotal =  BigDecimal.ZERO;

}
