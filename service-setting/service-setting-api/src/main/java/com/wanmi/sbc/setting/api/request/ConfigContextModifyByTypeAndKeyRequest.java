package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class ConfigContextModifyByTypeAndKeyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 5020067556450526760L;
    /**
     * 配置键
     */
    @ApiModelProperty(value = "配置键")
    @NotNull
    private ConfigKey configKey;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    @NotNull
    private ConfigType configType;

    /**
     * 配置内容
     */
    @ApiModelProperty(value = "配置内容")
    @NotNull
    private String context;

    /**
     * 状态,0:未启用1:已启用
     */
    @ApiModelProperty(value = "状态,0:未启用1:已启用")
    private Integer status;
}
