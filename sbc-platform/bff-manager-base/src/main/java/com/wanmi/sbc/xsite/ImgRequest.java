package com.wanmi.sbc.xsite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ImgRequest {

    @ApiModelProperty(value = "分类")
    private String cateId;

    @ApiModelProperty(value = "图片列表")
    private List<ImageForms> imageForms;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;
}
