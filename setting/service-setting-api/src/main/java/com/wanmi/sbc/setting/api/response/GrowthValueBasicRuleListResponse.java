package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 成长值基础规则
 */
@Data
public class GrowthValueBasicRuleListResponse implements Serializable {
    private static final long serialVersionUID = 7569636691712248537L;
    /**
     * 成长值基础规则列表
     */
    @ApiModelProperty(value = "成长值基础规则列表")
    private List<ConfigVO> configVOList = new ArrayList<>();
}
