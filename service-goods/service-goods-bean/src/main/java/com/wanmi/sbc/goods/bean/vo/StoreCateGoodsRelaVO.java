package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品-店铺分类关联实体类
 * Created by bail on 2017/11/13.
 */
@ApiModel
@Data
public class StoreCateGoodsRelaVO implements Serializable {

    private static final long serialVersionUID = 650143495830151484L;

    /**
     * 商品标识
     */
    @ApiModelProperty(value = "商品标识")
    private String goodsId;

    /**
     * 店铺分类标识
     */
    @ApiModelProperty(value = "店铺分类标识")
    private Long storeCateId;

}

