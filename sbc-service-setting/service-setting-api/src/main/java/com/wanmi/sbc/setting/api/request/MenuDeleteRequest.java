package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class MenuDeleteRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 7620321032723099799L;

    @ApiModelProperty(value = "菜单标识")
    @NotNull
    private String menuId;
}
