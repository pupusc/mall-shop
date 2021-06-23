package com.wanmi.sbc.setting.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@ApiModel
@Data
public class FunctionsByRoleIdListResponse implements Serializable {
    private static final long serialVersionUID = 16616082243647577L;
    /**
     * 功能列表
     */
    @ApiModelProperty(value = "功能列表")
    private List<String> functionList = new ArrayList<>();
}
