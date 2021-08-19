package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class FunctionListByRoleIdRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -4043548085492219042L;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;

    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    @NotNull
    private Platform systemTypeCd;
}
