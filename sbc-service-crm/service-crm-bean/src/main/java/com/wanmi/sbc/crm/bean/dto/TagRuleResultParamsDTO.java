package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 二级维度-参数信息
 */
@ApiModel
@Data
public class TagRuleResultParamsDTO {

    @ApiModelProperty(value = "标签参数id")
    @NotNull
    private Long paramId;

    @ApiModelProperty(value = "字段名称", hidden = true)
    private String columnMame;

    @ApiModelProperty(value = "维度配置类型", hidden = true)
    private Integer type;

    @ApiModelProperty(value = "标签维度类型", hidden = true)
    private Integer tagDimensionType;

    @ApiModelProperty(value = "标签参数值")
    private List<String> values;

    @ApiModelProperty(value = "范围选项列表")
    private List<String> ranges;
}
