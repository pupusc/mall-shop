package com.wanmi.sbc.xsite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RefreshRequest {

    @ApiModelProperty(value = "魔方建站参数")
    private String platform;

    @ApiModelProperty(value = "魔方建站参数")
    private String pageType;

    @ApiModelProperty(value = "魔方建站参数")
    private String pageCode;

    @ApiModelProperty(value = "魔方建站参数")
    private String path;
}
