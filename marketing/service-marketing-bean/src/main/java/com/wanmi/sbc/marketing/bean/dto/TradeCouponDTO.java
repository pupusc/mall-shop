package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:33 2018/9/29
 * @Description: 订单优惠券
 */
@ApiModel
@Data
public class TradeCouponDTO implements Serializable {

    /**
     * 优惠券码id
     */
    @ApiModelProperty(value = "优惠券码id")
    private String couponCodeId;

    /**
     * 优惠券码值
     */
    @ApiModelProperty(value = "优惠券码值")
    private String couponCode;

    /**
     * 优惠券关联的商品
     */
    @ApiModelProperty(value = "优惠券关联的商品集合")
    private List<String> goodsInfoIds;

    /**
     * 优惠券类型
     */
    @ApiModelProperty(value = "优惠券类型")
    private CouponType couponType;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountsAmount;

    /**
     * 购满多少钱
     */
    @ApiModelProperty(value = "购满多少钱")
    private BigDecimal fullBuyPrice;

}
