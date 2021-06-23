package com.wanmi.sbc.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class QueryByIdListRequest {

    @ApiModelProperty(value = "id列表")
    private List<String> idList;
}
