package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@ApiModel
@Data
public class InvoiceConfigModifyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -4757424655357618640L;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    @NotNull
    private Integer status;
}
