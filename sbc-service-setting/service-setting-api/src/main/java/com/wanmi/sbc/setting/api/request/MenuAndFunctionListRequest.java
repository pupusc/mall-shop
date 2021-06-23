package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.enums.Platform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class MenuAndFunctionListRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 7979385965307928715L;
    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    @NotNull
    private Platform systemTypeCd;
}
