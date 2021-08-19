package com.wanmi.sbc.marketing.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.marketing.bean.enums.CommissionPriorityType;
import com.wanmi.sbc.marketing.bean.enums.CommissionUnhookType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:16 2019/6/19
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultistageSettingGetResponse {


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
