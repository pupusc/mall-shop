package com.wanmi.sbc.goods.api.response.price;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.intervalprice.GoodsIntervalPriceResponse
 * 商品区间价格响应对象
 * @author lipeng
 * @dateTime 2018/11/6 下午2:32
 */
@ApiModel
@Data
public class GoodsIntervalPriceResponse implements Serializable {

    private static final long serialVersionUID = -1870419990822651307L;

    /**
     * 区间价列表
     */
    @ApiModelProperty(value = "区间价列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPriceVOList = new ArrayList<>();

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<GoodsInfoVO> goodsInfoVOList;
}
