package com.wanmi.sbc.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class QueryByIdRequest {

    @ApiModelProperty(value = "id")
    private String id;
}
