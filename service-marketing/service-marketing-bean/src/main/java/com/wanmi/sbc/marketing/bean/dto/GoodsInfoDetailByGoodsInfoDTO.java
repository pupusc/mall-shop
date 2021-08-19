package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.goods.bean.dto.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-21
 */
@ApiModel
@Data
public class GoodsInfoDetailByGoodsInfoDTO implements Serializable {

    private static final long serialVersionUID = -201216091878611519L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private GoodsInfoDTO goodsInfo;

    /**
     * 相关商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private GoodsDTO goods;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<GoodsPropDetailRelDTO> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<GoodsSpecDTO> goodsSpecs = new ArrayList();

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<GoodsSpecDetailDTO> goodsSpecDetails = new ArrayList();

    /**
     * 商品等级价格列表
     */
    @ApiModelProperty(value = "商品等级价格列表")
    private List<GoodsLevelPriceDTO> goodsLevelPrices = new ArrayList();

    /**
     * 商品客户价格列表
     */
    @ApiModelProperty(value = "商品客户价格列表")
    private List<GoodsCustomerPriceDTO> goodsCustomerPrices = new ArrayList();

    /**
     * 商品订货区间价格列表
     */
    @ApiModelProperty(value = "商品订货区间价格列表")
    private List<GoodsIntervalPriceDTO> goodsIntervalPrices = new ArrayList();

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片列表")
    private List<GoodsImageDTO> images = new ArrayList();

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    /**
     * 商家类型
     */
    @ApiModelProperty(value = "是否是商家")
    private BoolFlag companyType;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;
}
