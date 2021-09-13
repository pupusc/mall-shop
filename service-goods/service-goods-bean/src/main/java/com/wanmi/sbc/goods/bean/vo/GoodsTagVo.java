package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class GoodsTagVo {

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "标签名称")
    private String tagName;
}
