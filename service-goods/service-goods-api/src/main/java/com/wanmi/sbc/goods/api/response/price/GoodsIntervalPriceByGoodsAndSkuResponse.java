package com.wanmi.sbc.goods.api.response.price;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByGoodsAndSkuResponse
 *
 * @author lipeng
 * @dateTime 2018/11/13 上午9:49
 */
@ApiModel
@Data
public class GoodsIntervalPriceByGoodsAndSkuResponse implements Serializable {

    private static final long serialVersionUID = 5266547069045715872L;

    /**
     * 区间价列表
     */
    @ApiModelProperty(value = "区间价列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPriceVOList = new ArrayList<>();

    /**
     * 商品Sku列表
     */
    @ApiModelProperty(value = "商品Sku列表")
    private List<GoodsInfoVO> goodsInfoVOList;

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<GoodsVO> goodsDTOList;
}
