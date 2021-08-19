package com.wanmi.sbc.setting.bean.dto;

import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
public class GrowthValueBasicRuleDTO implements Serializable {
    private static final long serialVersionUID = -3968478870151974825L;

    @ApiModelProperty(value = "系统配置Type")
    @NotNull
    private ConfigType configType;

    @ApiModelProperty(value = "系统配置KEY")
    @NotNull
    private ConfigKey configKey;

    /**
     * 开关状态
     */
    @ApiModelProperty(value = "开关状态", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    @NotNull
    private Integer status;

    /**
     * 规则说明
     */
    @ApiModelProperty(value = "规则说明")
    private String context;
}
