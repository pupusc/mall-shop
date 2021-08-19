package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class ConfigStatusModifyByTypeAndKeyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -5655241855447657293L;
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
     * 状态
     */
    @ApiModelProperty(value = "状态", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    @NotNull
    private Integer status;
}
