package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel
@Data
public class FunctionListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 6191346154585256351L;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "角色标识")
    private Long roleInfoId;

    @ApiModelProperty(value = "角色名")
    private List<String> authorityNames;
}
