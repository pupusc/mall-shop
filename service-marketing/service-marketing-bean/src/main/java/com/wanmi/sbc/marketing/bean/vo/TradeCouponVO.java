package com.wanmi.sbc.marketing.bean.vo;

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
public class TradeCouponVO implements Serializable {

    private static final long serialVersionUID = 6417804267369151123L;

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
    @ApiModelProperty(value = "优惠券关联的商品id集合")
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

    public static void main(String[] args) {
        /*for (int i = 100; i < 1000; i++) {
            //百分位
            int p = i / 100;
            //十分位
            int t = i % 100 / 10;
            //个分位
            int c = i % 100 % 10;
            double total = Math.pow(p, 3) + Math.pow(t, 3) + Math.pow(c, 3);
            if (total == (double) i) {
                System.out.println(i);
            }
        }*/
        char[] a = "complete".toCharArray();
        String b = "meter";
        String temp = "";
        for (int i = 0; i < a.length; i++) {
            temp = "";
            if (b.contains(a[i]+"")) {
                temp = String.valueOf(a[i]);
                for (int j = i + 1; j < b.length(); j++) {
                    if (!b.contains(temp + a[j])) {
                        break;
                    }
                    temp = temp + a[j];
                }
            }
        }
    }

}
