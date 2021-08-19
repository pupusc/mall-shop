package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 周期购发货日期规则对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class CycleBuySendDateRuleDTO {

    @ApiModelProperty(value = "发货日期")
    private String sendDateRule;

    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
}
