package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class MenuAndAuthorityListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 908230430306593133L;
    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    @NotNull
    private Platform systemTypeCd;
}
