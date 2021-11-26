package com.wanmi.sbc.crm.bean.vo;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 一级维度-规则信息
 */
@ApiModel
@Data
public class TagRuleVO {

    @ApiModelProperty(value = "标签维度id")
    private Long dimensionId;

    @ApiModelProperty(value = "二级维度且或关系，0：且，1：或")
    private RelationType type;

    @ApiModelProperty(value = "表名", hidden = true)
    private String tableName;

    @ApiModelProperty(value = "二级维度-聚合目标类参数")
    private TagRuleResultParamsVO resultParam;

    @ApiModelProperty(value = "二级维度-参数信息列表")
    private List<TagRuleParamsVO> ruleParamList;
}
