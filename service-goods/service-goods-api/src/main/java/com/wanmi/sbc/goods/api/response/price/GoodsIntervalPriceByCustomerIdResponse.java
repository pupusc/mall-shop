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
 * com.wanmi.sbc.goods.api.response.intervalprice.GoodsIntervalPriceByCustomerIdResponse
 *
 * @author lipeng
 * @dateTime 2018/11/13 上午9:49
 */
@ApiModel
@Data
public class GoodsIntervalPriceByCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = 5266547069045715872L;

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
