package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.setting.bean.vo.OperateDataLogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse
 * 根据编号查询商品视图信息响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午9:39
 */
@ApiModel
@Data
public class GoodsViewByIdResponse implements Serializable {

    private static final long serialVersionUID = -6641896293423917872L;

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

    @ApiModelProperty(value = "标签")
    private List<TagVO> tags;

    @ApiModelProperty(value = "是否书籍")
    private Integer bookFlag;

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
     * 拼团活动
     */
    @ApiModelProperty(value = "拼团活动")
    private Boolean grouponFlag;

    /**
     * 操作日志
     */
    @ApiModelProperty(value = "操作日志")
    private List<OperateDataLogVO> operateDataLogVOList;

    /**
     *    是否参与全量营销活动
     *    秒杀在购物车视为普通商品fullMarketing=false
     */
    @ApiModelProperty(value = "是否参与全量营销活动")
    Boolean fullMarketing=Boolean.TRUE;


    /**
     * 周期购活动信息
     */
    @ApiModelProperty(value = "周期购活动信息")
    private CycleBuyVO cycleBuyVO;

    /**
     * 商品额外属性
     */
    @ApiModelProperty(value = "商品额外属性")
    private Map<String, String> extProps;



}
