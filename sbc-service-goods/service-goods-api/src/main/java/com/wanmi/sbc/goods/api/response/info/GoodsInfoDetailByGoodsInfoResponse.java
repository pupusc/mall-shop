package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU编辑视图响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoDetailByGoodsInfoResponse implements Serializable {

    private static final long serialVersionUID = 9155380513851223919L;

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
    @ApiModelProperty(value = "商家类型")
    private BoolFlag companyType;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 拼团活动
     */
    @ApiModelProperty(value = "拼团活动")
    private Boolean grouponFlag;

}
