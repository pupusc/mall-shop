package com.wanmi.sbc.order.bean.vo;

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
public class TradeConfirmItemVO {
    /**
     * 订单商品sku
     */
    @ApiModelProperty(value = "订单商品sku")
    private List<TradeItemVO> tradeItems;

    /**
     * 赠品列表
     */
    @ApiModelProperty(value = "赠品列表")
    private List<TradeItemVO> gifts;

    /**
     * 订单营销信息
     */
    @ApiModelProperty(value = "营销信息")
    private List<TradeConfirmMarketingVO> tradeConfirmMarketingList;

    /**
     * 商家与店铺信息
     */
    @ApiModelProperty(value = "商家与店铺信息")
    private SupplierVO supplier;

    /**
     * 订单项小计
     */
    @ApiModelProperty(value = "订单项小计")
    private TradePriceVO tradePrice;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private List<DiscountsVO> discountsPrice;

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    private CycleBuyInfoVO cycleBuyInfo;

}
