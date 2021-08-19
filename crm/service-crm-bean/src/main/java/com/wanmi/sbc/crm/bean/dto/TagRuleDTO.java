package com.wanmi.sbc.crm.bean.dto;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 一级维度-规则信息
 */
@ApiModel
@Data
public class TagRuleDTO {

    @ApiModelProperty(value = "标签维度id")
    @NotNull
    private Long dimensionId;

    @ApiModelProperty(value = "二级维度且或关系，0：且，1：或")
    @NotNull
    private RelationType type;

    @ApiModelProperty(value = "表名", hidden = true)
    private String tableName;

    @ApiModelProperty(value = "二级维度-聚合目标类参数")
    private TagRuleResultParamsDTO resultParam;

    @ApiModelProperty(value = "二级维度-条件类参数列表")
    @NotEmpty
    private List<TagRuleParamsDTO> ruleParamList;
}
