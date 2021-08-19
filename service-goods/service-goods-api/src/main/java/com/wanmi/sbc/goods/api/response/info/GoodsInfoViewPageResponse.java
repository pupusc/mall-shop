package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU视图分页响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
public class GoodsInfoViewPageResponse implements Serializable {

    private static final long serialVersionUID = -4370164109574914820L;

    /**
     * 分页商品SKU信息
     */
    @ApiModelProperty(value = "分页商品SKU信息")
    private MicroServicePage<GoodsInfoVO> goodsInfoPage;

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private List<GoodsVO> goodses;

    /**
     * 品牌列表
     */
    @ApiModelProperty(value = "品牌列表")
    private List<GoodsBrandVO> brands = new ArrayList<>();

    /**
     * 分类列表
     */
    @ApiModelProperty(value = "分类列表")
    private List<GoodsCateVO> cates = new ArrayList<>();

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;
}
