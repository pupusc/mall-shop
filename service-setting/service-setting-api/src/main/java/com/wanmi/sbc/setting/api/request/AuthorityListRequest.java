package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class AuthorityListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 5905013957181564663L;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    @NotNull
    private Long roleInfoId;
}
