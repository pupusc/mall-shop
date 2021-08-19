package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分基础获取规则
 */
@Data
public class PointsBasicRuleListResponse implements Serializable {
    private static final long serialVersionUID = 4478424927598531949L;

    /**
     * 积分基础获取规则列表
     */
    @ApiModelProperty(value = "积分基础获取规则列表")
    private List<ConfigVO> configVOList = new ArrayList<>();
}
