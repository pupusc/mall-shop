package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.order.bean.vo.PurchaseGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseMiniListResponse implements Serializable {

    private static final long serialVersionUID = 3966204473008555064L;

    /**
     * 商品
     */
    @ApiModelProperty(value = "商品列表")
    private List<PurchaseGoodsVO> goodsList;

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    /**
     * 采购单数量
     */
    @ApiModelProperty(value = "采购单数量")
    private Integer purchaseCount;

    /**
     * 购买商品总计
     */
    @ApiModelProperty(value = "购买商品总计")
    private Long num;

}
