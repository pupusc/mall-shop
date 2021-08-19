package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>订单确认返回项</p>
 * Created by of628-wenzhi on 2018-03-08-下午6:12.
 */
@Data
@ApiModel
public class TradeConfirmItemDTO {

    /**
     * 订单商品sku
     */
    @ApiModelProperty(value = "订单商品sku")
    private List<TradeItemDTO> tradeItems;

    /**
     * 赠品列表
     */
    @ApiModelProperty(value = "赠品列表")
    private List<TradeItemDTO> gifts;

    /**
     * 商家与店铺信息
     */
    @ApiModelProperty(value = "商家与店铺信息")
    private SupplierDTO supplier;

    /**
     * 订单项小计
     */
    @ApiModelProperty(value = "订单项小计")
    private TradePriceDTO tradePrice;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private List<DiscountsDTO> discountsPrice;

}
