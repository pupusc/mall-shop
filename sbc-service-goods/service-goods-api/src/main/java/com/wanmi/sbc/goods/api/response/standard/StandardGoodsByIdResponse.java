package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据ID查询商品库返回视图</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardGoodsByIdResponse implements Serializable {

    private static final long serialVersionUID = -7185910179056639330L;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private StandardGoodsVO goods;

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片")
    private List<StandardImageVO> images;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<StandardPropDetailRelVO> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<StandardSpecVO> goodsSpecs;

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<StandardSpecDetailVO> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    @ApiModelProperty(value = "商品SKU列表")
    private List<StandardSkuVO> goodsInfos;
}
