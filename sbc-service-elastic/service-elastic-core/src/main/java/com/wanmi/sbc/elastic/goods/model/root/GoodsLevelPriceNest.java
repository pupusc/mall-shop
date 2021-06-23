package com.wanmi.sbc.elastic.goods.model.root;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 商品级别价格实体
 * Created by dyt on 2017/4/17.
 */
@Data
@ApiModel
public class GoodsLevelPriceNest implements Serializable {

    /**
     * 级别价格ID
     */
    @ApiModelProperty(value = "级别价格ID")
    private Long levelPriceId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    @Field(index = false, type = FieldType.Keyword)
    private String goodsId;

    /**
     * 等级ID
     */
    @ApiModelProperty(value = "等级ID")
    @Field(index = false, type = FieldType.Long)
    private Long levelId;

    /**
     * 订货价
     */
    @ApiModelProperty(value = "订货价")
    @Field(index = false, type = FieldType.Double)
    private BigDecimal price;

    /**
     * 起订量
     */
    @ApiModelProperty(value = "起订量")
    @Field(index = false, type = FieldType.Long)
    private Long count;

    /**
     * 限订量
     */
    @ApiModelProperty(value = "限订量")
    @Field(index = false, type = FieldType.Long)
    private Long maxCount;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @Field(index = false, type = FieldType.Keyword)
    private String goodsInfoId;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private PriceType type;
}
