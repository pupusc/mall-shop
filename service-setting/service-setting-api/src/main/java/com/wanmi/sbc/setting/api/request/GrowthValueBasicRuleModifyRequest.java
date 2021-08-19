package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.dto.GrowthValueBasicRuleDTO;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class GrowthValueBasicRuleModifyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -1525533704140001026L;

    /**
     * 成长值基础获取规则列表
     */
    @ApiModelProperty(value = "成长值基础获取规则列表")
    @NotNull
    @Valid
    private List<GrowthValueBasicRuleDTO> growthValueBasicRuleDTOList;

    /**
     * 成长值规则
     */
    @ApiModelProperty(value = "成长值规则")
    @NotNull
    private GrowthValueRule rule;
}
