package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-03-02 10:38
 **/
@ApiModel
@Data
public class GoodsForXsiteVO extends GoodsVO implements Serializable {

    private static final long serialVersionUID = -2166675422878041081L;
    @ApiModelProperty(value = "所属店铺名称")
    private String storeName;

    @ApiModelProperty(value = "平台类目名称")
    private String cateName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;
}