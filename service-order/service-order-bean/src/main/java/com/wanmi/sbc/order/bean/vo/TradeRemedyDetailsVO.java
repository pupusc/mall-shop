package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>用于编辑订单前的订单信息展示结构</p>
 * Created by of628-wenzhi on 2018-05-25-下午4:17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeRemedyDetailsVO implements Serializable {

    /**
     * 订单详情
     */
    @ApiModelProperty(value = "订单详情")
    private TradeVO trade;

    /**
     * 包含skuId和计算会员，区间价后的最新单价
     */
    @ApiModelProperty(value = "包含skuId和计算会员，区间价后的最新单价")
    private Map<String, TradeItemPriceVO> tradeItemPriceMap;
}
