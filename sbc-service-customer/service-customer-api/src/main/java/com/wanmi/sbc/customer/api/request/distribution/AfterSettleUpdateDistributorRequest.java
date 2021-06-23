package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class AfterSettleUpdateDistributorRequest {

    @ApiModelProperty(value = "分销员id")
    @NotBlank
    private String distributeId;

    @ApiModelProperty(value = "邀新人数")
    @Min(0)
    private Integer inviteNum = 1;

    @ApiModelProperty(value = "邀新奖励金额")
    @Min(0)
    private BigDecimal inviteAmount;

    @ApiModelProperty(value = "订单数")
    @Min(0)
    private Integer orderNum = 0;

    @ApiModelProperty(value = "减少的销售额")
    @Min(0)
    private BigDecimal amount;

    @ApiModelProperty(value = "发放的分销佣金")
    @Min(0)
    private BigDecimal grantAmount;

    @ApiModelProperty(value = "原来预计的分销佣金")
    @Min(0)
    private BigDecimal totalDistributeAmount;

    /**
     * 分销员等级设置信息
     */
    @ApiModelProperty("分销员等级设置信息")
    private List<DistributorLevelVO> distributorLevelVOList;

    /**
     * 基础邀新奖励限制
     */
    @ApiModelProperty("基础邀新奖励限制")
    private DefaultFlag baseLimitType;

    /**
     * 是否有分销员资格false:否，true:是
     */
    @ApiModelProperty("是否有分销员资格false:否，true:是")
    private Boolean distributorFlag;
}
