package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>积分订单项</p>
 * Created by yinxianzhi on 2019-05-20-下午6:12.
 */
@Data
@ApiModel
public class PointsTradeItemGroupVO {
    /**
     * 订单商品sku
     */
    @ApiModelProperty(value = "订单商品sku")
    private TradeItemVO tradeItem;

    /**
     * 商家与店铺信息
     */
    @ApiModelProperty(value = "商家与店铺信息")
    private SupplierVO supplier;
}
