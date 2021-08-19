package com.wanmi.sbc.xsite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ImageForms {

    @ApiModelProperty(value = "魔方建站参数")
    private String advice;

    @ApiModelProperty(value = "文件")
    private byte[] duration;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer height;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "魔方建站参数")
    private String scene;

    @ApiModelProperty(value = "魔方建站参数")
    private Long size;

    @ApiModelProperty(value = "魔方建站参数")
    private String url;

    @ApiModelProperty(value = "魔方建站参数")
    private Integer width;

}
