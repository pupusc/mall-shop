package com.wanmi.sbc.goods.api.response.price;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>企业购商品设价返回结构</p>
 * Created by of628-wenzhi on 2020-03-04-下午507.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsPriceSetBatchByIepResponse implements Serializable {
    private static final long serialVersionUID = -8113960455965430995L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private List<GoodsInfoVO> goodsInfos;

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;
}
