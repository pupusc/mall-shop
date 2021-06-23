package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class AllRoleMenuInfoListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 13289165783525494L;
    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    @NotNull
    private Platform systemTypeCd;
}
