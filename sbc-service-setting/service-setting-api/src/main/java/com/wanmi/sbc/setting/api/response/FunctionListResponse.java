package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@ApiModel
@Data
public class FunctionListResponse implements Serializable {
    private static final long serialVersionUID = -4056043270094556193L;
    /**
     * 功能列表
     */
    @ApiModelProperty(value = "功能列表")
    private List<String> functionList = new ArrayList<>();
}
