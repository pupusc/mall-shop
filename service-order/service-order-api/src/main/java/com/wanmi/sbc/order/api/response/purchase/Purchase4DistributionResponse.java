package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车处理社交分销业务
 */
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase4DistributionResponse implements Serializable {


    private static final long serialVersionUID = 1677230495271710577L;
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

    /**
     * 商品SKU信息-排除分销商品
     */
    @ApiModelProperty(value = "商品SKU信息-排除分销商品")
    private List<GoodsInfoVO> goodsInfoComList;

    /**
     * 是否自购-显示返利
     */
    @ApiModelProperty(value = "是否自购")
    private boolean selfBuying = false;
}
