package com.wanmi.sbc.elastic.bean.vo.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品库实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsStandardGoodsPageVO implements Serializable {

    /**
     * 商品编号，采用UUID
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    private String goodsNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;


    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    private String goodsImg;


    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家,2 linkedmall ")
    private Integer goodsSource;

    /**
     * 上下架状态,0:下架1:上架2:部分上架
     */
    @ApiModelProperty(value = "上下架状态,0:下架1:上架2:部分上架")
    private Integer addedFlag;

    @ApiModelProperty(value = "库存")
    private Long stock;
}
