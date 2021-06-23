package com.wanmi.sbc.goods.request;

import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.order.bean.vo.GrouponDetailWithGoodsVO;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceWithCustomerInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse
 * 根据编号查询商品视图信息响应对象
 *
 * @author lipeng
 * @dateTime 2018/11/5 上午9:39
 */
@ApiModel
@Data
public class GrouponGoodsViewByIdResponse implements Serializable {

    private static final long serialVersionUID = 3025498538253170134L;
    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private GoodsVO goods;

    /**
     * 商品相关图片
     */
    @ApiModelProperty(value = "商品相关图片")
    private List<GoodsImageVO> images;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<GoodsPropDetailRelVO> goodsPropDetailRels;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<GoodsSpecVO> goodsSpecs;

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<GoodsSpecDetailVO> goodsSpecDetails;

    /**
     * 商品SKU列表
     */
    @ApiModelProperty(value = "商品SKU列表")
    private List<GoodsInfoVO> goodsInfos;

    /**
     * 商品等级价格列表
     */
    @ApiModelProperty(value = "商品等级价格列表")
    private List<GoodsLevelPriceVO> goodsLevelPrices;

    /**
     * 商品客户价格列表
     */
    @ApiModelProperty(value = "商品客户价格列表")
    private List<GoodsCustomerPriceVO> goodsCustomerPrices;

    /**
     * 商品订货区间价格列表
     */
    @ApiModelProperty(value = "商品订货区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    /**
     * 商品详情模板关联
     */
    @ApiModelProperty(value = "商品详情模板关联")
    private List<GoodsTabRelaVO> goodsTabRelas;

    /**
     * 商品模板配置
     */
    @ApiModelProperty(value = "商品模板配置")
    private List<StoreGoodsTabVO> storeGoodsTabs;

    /**
     * 是否是分销商品
     */
    @ApiModelProperty(value = "是否是分销商品")
    private Boolean distributionGoods;


    /**
     * 是否是拼团商品
     */
    @ApiModelProperty(value = "是否是拼团商品")
    private Boolean grouponGoods;



    @ApiModelProperty(value = "拼团信息")
    private GrouponDetailWithGoodsVO grouponDetails;

    /**
     * 进行中团活动
     */
    private List<GrouponInstanceWithCustomerInfoVO> grouponInstanceList;




}
