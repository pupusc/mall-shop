package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class RoleMenuFuncIdsQueryRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -8543775758061616473L;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    @NotNull
    private Long roleInfoId;
}
