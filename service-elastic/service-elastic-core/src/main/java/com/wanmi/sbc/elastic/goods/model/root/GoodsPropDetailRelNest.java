package com.wanmi.sbc.elastic.goods.model.root;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * ES存储的商品属性值关系
 *
 * @auther bail
 * @create 2018/03/23 10:04
 */
@Data
@ApiModel
public class GoodsPropDetailRelNest {

    /**
     *属性id
     */
    @ApiModelProperty(value = "属性id")
    @Field(index = false, type = FieldType.Long)
    private Long propId;

    /**
     *属性值id
     */
    @ApiModelProperty(value = "属性值id")
    @Field(index = false, type = FieldType.Long)
    private Long detailId;
}
