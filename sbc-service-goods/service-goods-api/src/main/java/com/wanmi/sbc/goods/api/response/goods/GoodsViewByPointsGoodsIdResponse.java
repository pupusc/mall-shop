package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 根据积分商品编号查询商品视图信息响应对象
 * @author yinxianzhi
 * @dateTime 2019/05/24 上午9:39
 */
@ApiModel
@Data
public class GoodsViewByPointsGoodsIdResponse implements Serializable {

    private static final long serialVersionUID = 5307158550923639596L;

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
     * 积分商品列表
     */
    @ApiModelProperty(value = "积分商品列表")
    private List<PointsGoodsVO> pointsGoodsList;

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
}
