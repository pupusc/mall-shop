package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel
@Data
public class InvoiceConfigModifyResponse implements Serializable {
    private static final long serialVersionUID = -7178116380897151939L;

    @ApiModelProperty(value = "总数")
    private int count;
}
