package com.wanmi.sbc.goods.bean.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class GoodsPropRequestDTO implements Serializable {

    private static final long serialVersionUID = 2482930479449054108L;

    @ApiModelProperty(value = "最后一个属性Id")
    private Long lastPropId;

    @ApiModelProperty(value = "商品属性")
    private GoodsPropDTO goodsProp;

    @ApiModelProperty(value = "商品属性集合")
    private List<GoodsPropDTO> goodsProps;
}
