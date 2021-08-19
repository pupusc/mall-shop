package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:39 2018/10/8
 * @Description:订单营销插件响应类
 */
@ApiModel
@Data
public class TradeMarketingWrapperVO implements Serializable {

    private static final long serialVersionUID = -7335292671596183470L;

    /**
     * 满系营销实体 {@link TradeMarketingVO}
     */
    @ApiModelProperty(value = "订单营销信息")
    private TradeMarketingVO tradeMarketing;

    /**
     * 优惠券实体 {@link TradeCouponVO}
     */
    @ApiModelProperty(value = "订单优惠券信息")
    private TradeCouponVO tradeCoupon;

}
