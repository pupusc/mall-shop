package com.wanmi.sbc.xsite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ImgPageReq{

    @ApiModelProperty(value = "分类")
    private Long cateId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "第几页")
    private Integer pageNo;

    @ApiModelProperty(value = "每页显示多少条")
    private Integer pageSize;
}
