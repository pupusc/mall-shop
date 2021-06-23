package com.wanmi.sbc.xsite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class GoodsInfoRequest {

    @ApiModelProperty(value = "分类名称")
    private Long catName;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页几条")
    private Integer pageSize;

    @ApiModelProperty(value = "查询条件")
    private String q;

    @ApiModelProperty(value = "类型")
    private Integer type;
}
