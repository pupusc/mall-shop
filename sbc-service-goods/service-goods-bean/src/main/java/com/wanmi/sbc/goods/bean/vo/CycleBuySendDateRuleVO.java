package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 周期购发货日期规则对象
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuySendDateRuleVO implements Serializable {

    private static final long serialVersionUID = 4992895923446185928L;

    @ApiModelProperty(value = "发货日期")
    private String sendDateRule;

    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
}
