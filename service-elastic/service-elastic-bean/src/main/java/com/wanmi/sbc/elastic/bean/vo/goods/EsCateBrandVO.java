package com.wanmi.sbc.elastic.bean.vo.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据商品分类-品牌,只在商品ES索引时引用
 * Created by dyt on 2017/4/21.
 */
@ApiModel
@Data
class EsCateBrandVO {

    /**
     * documentId组成成分:分类ID_品牌ID
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 商品分类信息
     */
    @ApiModelProperty(value = "商品分类信息")
    private GoodsCateNestVO goodsCate;

    /**
     * 商品品牌信息
     */
    @ApiModelProperty(value = "商品品牌信息")
    private GoodsBrandNestVO goodsBrand;


}
