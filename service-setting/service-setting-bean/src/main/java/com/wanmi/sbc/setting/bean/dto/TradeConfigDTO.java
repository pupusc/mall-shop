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
public class TradeConfigDTO implements Serializable {
    private static final long serialVersionUID = -2846726765693831313L;

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
     * 设置天数
     */
    @ApiModelProperty(value = "设置天数")
    private String context;
}
