package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class TradeConfigGetByTypeRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 8704217856819450589L;

    @ApiModelProperty(value = "系统配置KEY")
    @NotNull
    private ConfigType configType;
}
