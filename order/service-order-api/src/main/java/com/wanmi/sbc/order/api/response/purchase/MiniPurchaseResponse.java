package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by sunkun on 2017/8/16.
 */
@Data
@ApiModel
public class MiniPurchaseResponse {

    /**
     * 商品
     */
    @ApiModelProperty(value = "商品")
    private List<PurchaseGoodsReponse> goodsList;

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    /**
     * 采购单数量
     */
    @ApiModelProperty(value = "采购单数量",example = "0")
    private Integer purchaseCount;

    /**
     * 购买商品总计
     */
    @ApiModelProperty(value = "购买商品总计")
    private Long num;

}
