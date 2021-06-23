package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.dto.PointsBasicRuleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class PointsBasicRuleModifyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -4547849565284838080L;

    /**
     * 积分基础获取规则列表
     */
    @ApiModelProperty(value = "积分基础获取规则列表")
    @NotNull
    @Valid
    private List<PointsBasicRuleDTO> pointsBasicRuleDTOList;
}
