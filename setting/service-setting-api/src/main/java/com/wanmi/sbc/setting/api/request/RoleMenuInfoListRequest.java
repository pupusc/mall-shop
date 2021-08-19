package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
public class RoleMenuInfoListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -2648079719922994839L;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    @NotNull
    private List<Long> roleInfoId;
}
