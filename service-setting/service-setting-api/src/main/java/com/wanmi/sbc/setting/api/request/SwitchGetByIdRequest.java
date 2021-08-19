package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class SwitchGetByIdRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -3550590607668030808L;

    @ApiModelProperty(value = "开关id")
    @NotNull
    private String id;
}
