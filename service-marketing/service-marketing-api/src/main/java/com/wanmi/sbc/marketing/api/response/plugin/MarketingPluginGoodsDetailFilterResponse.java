package com.wanmi.sbc.marketing.api.response.plugin;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-14
 */
@ApiModel
@Data
public class MarketingPluginGoodsDetailFilterResponse implements Serializable {

    private static final long serialVersionUID = 7264985232500893828L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private GoodsInfoVO goodsInfo;

    /**
     * 相关商品SPU信息
     */
    @ApiModelProperty(value = "相关商品SPU信息")
    private GoodsVO goods;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<GoodsPropDetailRelVO> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<GoodsSpecVO> goodsSpecs = new ArrayList<>();

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<GoodsSpecDetailVO> goodsSpecDetails = new ArrayList<>();

    /**
     * 商品等级价格列表
     */
    @ApiModelProperty(value = "商品等级价格列表")
    private List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();

    /**
     * 商品客户价格列表
     */
    @ApiModelProperty(value = "商品客户价格列表")
    private List<GoodsCustomerPriceVO> goodsCustomerPrices = new ArrayList<>();

    /**
     * 商品订货区间价格列表
     */
    @ApiModelProperty(value = "商品订货区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices = new ArrayList<>();

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片")
    private List<GoodsImageVO> images = new ArrayList<>();

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
