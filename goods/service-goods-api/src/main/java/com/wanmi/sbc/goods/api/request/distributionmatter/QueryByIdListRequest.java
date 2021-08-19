package com.wanmi.sbc.goods.api.request.distributionmatter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel
public class QueryByIdListRequest {

    @ApiModelProperty(value = "id列表")
    @Size(min = 1)
    private List ids;
}
