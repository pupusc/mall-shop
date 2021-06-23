package com.wanmi.sbc.crm.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 二级维度-聚合目标类参数信息
 */
@ApiModel
@Data
public class TagRuleResultParamsVO {

    @ApiModelProperty(value = "标签参数id")
    private Long paramId;

    @ApiModelProperty(value = "字段名称")
    private String columnMame;

    @ApiModelProperty(value = "维度配置类型")
    private Integer type;

    @ApiModelProperty(value = "标签维度类型")
    private Integer tagDimensionType;

    @ApiModelProperty(value = "范围选项列表")
    private List<String> ranges;

    @ApiModelProperty(value = "标签参数值")
    private List<String> values;
}
