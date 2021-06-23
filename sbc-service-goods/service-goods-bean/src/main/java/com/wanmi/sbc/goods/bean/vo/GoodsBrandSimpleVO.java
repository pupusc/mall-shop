package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品品牌实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsBrandSimpleVO implements Serializable {

    private static final long serialVersionUID = -3369219732567679167L;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
}
