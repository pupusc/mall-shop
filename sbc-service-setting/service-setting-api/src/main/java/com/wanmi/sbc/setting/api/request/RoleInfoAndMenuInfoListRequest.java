package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoAndMenuInfoListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -2648079719922994839L;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    @NotNull
    private List<Long> roleInfoIdList;
}
