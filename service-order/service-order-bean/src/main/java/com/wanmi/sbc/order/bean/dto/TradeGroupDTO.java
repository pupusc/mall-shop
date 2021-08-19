package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.marketing.bean.dto.TradeCouponDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:14 2018/9/30
 * @Description: 订单组
 */
@Data
@ApiModel
public class TradeGroupDTO {

    /**
     * 订单组号
     */
    @ApiModelProperty(value = "订单组号")
    @Id
    private String id;

    /**
     * 订单组中使用的平台优惠券
     */
    @ApiModelProperty(value = "订单组中使用的平台优惠券")
    private TradeCouponDTO commonCoupon;

    /**
     * 使用平台优惠券的商品集合(已作废的商品)
     */
    @ApiModelProperty(value = "使用平台优惠券的商品集合")
    private List<String> commonSkuIds = new ArrayList<>();

    /**
     * 平台券是否已退
     */
    @ApiModelProperty(value = "平台券是否已退",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean commonCouponIsReturn = Boolean.FALSE;

}
