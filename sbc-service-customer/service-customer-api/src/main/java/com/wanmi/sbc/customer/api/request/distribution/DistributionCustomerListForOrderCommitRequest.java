package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.bean.enums.CommissionPriorityType;
import com.wanmi.sbc.customer.bean.enums.CommissionUnhookType;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:49 2019/7/5
 * @Description:
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerListForOrderCommitRequest {

    /**
     * 店主id
     */
    @ApiModelProperty(value = "店主id")
    private String InviteeId;

    /**
     * 下单用户id
     */
    @ApiModelProperty(value = "下单用户id")
    private String buyerId;

    /**
     * 下单用户是否分销员
     */
    @ApiModelProperty(value = "下单用户是否分销员")
    private DefaultFlag isDistributor = DefaultFlag.NO;

    /**
     * 佣金返利优先级
     */
    @ApiModelProperty(value = "佣金返利优先级")
    private CommissionPriorityType commissionPriorityType;

    /**
     * 佣金提成脱钩
     */
    @ApiModelProperty(value = "佣金提成脱钩")
    private CommissionUnhookType commissionUnhookType;

    /**
     * 分销员等级列表
     */
    @ApiModelProperty(value = "分类员等级列表")
    private List<DistributorLevelVO> distributorLevels = new ArrayList<>();

}
