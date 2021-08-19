package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class FunctionDeleteRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 298106298150783777L;

    @ApiModelProperty(value = "功能标识")
    @NotNull
    private String functionId;
}
