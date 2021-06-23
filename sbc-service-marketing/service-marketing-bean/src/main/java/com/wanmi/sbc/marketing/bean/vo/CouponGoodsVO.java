package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 商品SKU视图响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponGoodsVO implements Serializable {

    private static final long serialVersionUID = 6220398465688816611L;

    /**
     * 分页商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息分页")
    private MicroServicePage<GoodsInfoVO> goodsInfoPage;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息列表")
    private List<GoodsInfoVO> goodsInfos;

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息列表")
    private List<GoodsVO> goodses;

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    /**
     * 品牌列表
     */
    @ApiModelProperty(value = "商品品牌列表")
    private List<GoodsBrandVO> brands;

    /**
     * 分类列表
     */
    @ApiModelProperty(value = "商品分类列表")
    private List<GoodsCateVO> cates;
}
