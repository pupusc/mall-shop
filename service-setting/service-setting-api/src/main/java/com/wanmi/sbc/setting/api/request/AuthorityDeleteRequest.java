package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class AuthorityDeleteRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -2215270754163855523L;

    @ApiModelProperty(value = "主键")
    @NotNull
    private String authorityId;
}
