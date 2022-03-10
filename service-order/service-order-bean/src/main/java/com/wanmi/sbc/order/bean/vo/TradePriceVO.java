package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单金额归总
 * Created by jinwei on 19/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradePriceVO implements Serializable {

    private static final long serialVersionUID = 5258208696186469295L;
    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    private BigDecimal goodsPrice;

    /**
     * 配送费用，可以从TradePriceInfo获取
     */
    @ApiModelProperty(value = "配送费用")
    private BigDecimal deliveryPrice;

    /**
     * 特价金额，可以从TradePriceInfo获取
     */
    @ApiModelProperty(value = "特价金额")
    private BigDecimal privilegePrice;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountsPrice;
    /**
     * 换购商品金额
     */
    private BigDecimal markupPrice;
    /**
     * 积分数量，可以从TradePriceInfo获取(此变量没用到)
     */
    @ApiModelProperty(value = "积分数量")
    private Integer integral;

    /**
     * 积分兑换金额，可以从TradePriceInfo获取(此变量没用到)
     */
    @ApiModelProperty(value = "积分兑换金额")
    private BigDecimal integralPrice;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Long points;
    /**
     * 知豆，被用于知豆订单的商品知豆，普通订单的均摊知豆
     */
    private Long knowledge;

    /**
     * 购买积分
     */
    private Long buyPoints;

    /**
     * 购买知豆
     */
    private Long buyKnowledge;

    /**
     * 积分兑换金额
     */
    @ApiModelProperty(value = "积分兑换金额")
    private BigDecimal pointsPrice;


    /**
     * 知豆兑换金额
     */
    private BigDecimal knowledgePrice;

    /**
     * 积分价值
     */
    @ApiModelProperty(value = "积分价值")
    private Long pointWorth;

    /**
     * 知豆价值
     */
    private Long knowledgeWorth;

    /**
     * 是否特价单
     */
    @ApiModelProperty(value = "是否特价单", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean special;

    /**
     * 是否开启运费
     */
    @ApiModelProperty(value = "是否开启运费", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean enableDeliveryPrice;

    /**
     * 原始金额, 不作为付费金额
     */
    @ApiModelProperty(value = "原始金额, 不作为付费金额")
    private BigDecimal originPrice;

    /**
     * 订单应付金额
     */
    @ApiModelProperty(value = "订单应付金额")
    private BigDecimal totalPrice;

    /**
     * 订单实际支付金额
     * 账务中心每次回调的支付金额之和：订单已支付金额
     * add wumeng
     */
    @ApiModelProperty(value = "订单实际支付金额")
    private BigDecimal totalPayCash;

    /**
     * 支付手续费
     * 账务中心支付时银行收取的支付费率
     * add wumeng
     */
    @ApiModelProperty(value = "支付手续费")
    private BigDecimal rate;

    /**
     * 平台佣金
     * add wumeng
     */
    @ApiModelProperty(value = "平台佣金")
    private BigDecimal cmCommission;

    /**
     * 发票费用
     * added by shenchunnan
     */
    @ApiModelProperty(value = "发票费用")
    private BigDecimal invoiceFee;

    /**
     * 订单优惠金额明细
     */
    @ApiModelProperty(value = "订单优惠金额明细")
    private List<DiscountsPriceDetailVO> discountsPriceDetails;

    /**
     * 优惠券优惠金额
     */
    @ApiModelProperty(value = "优惠券优惠金额")
    private BigDecimal couponPrice = BigDecimal.ZERO;

//    /**
//     * 单个订单返利总金额
//     */
//    @ApiModelProperty(value = "单个订单返利总金额")
//    private BigDecimal orderDistributionCommission = BigDecimal.ZERO;

    /**
     * 订单供货价总额
     */
    @ApiModelProperty(value = "订单供货价总额")
    private BigDecimal orderSupplyPrice;

    /**
     * 定金
     */
    private BigDecimal earnestPrice;

    /**
     * 定金膨胀
     */
    private BigDecimal swellPrice;

    /**
     * 尾款
     */
    private BigDecimal tailPrice;


    /**
     * 可退定金
     */
    private BigDecimal canBackEarnestPrice;

    /**
     * 可退尾款
     */
    private BigDecimal canBackTailPrice;

    /**
     * 活动优惠总额
     */
    @ApiModelProperty(value = "活动优惠总额")
    private BigDecimal marketingDiscountPrice;


    /**
     * 根据供应商拆分后的运费
     */
    private Map<Long, BigDecimal> splitDeliveryPrice;

    /**
     * 实际支付金额
     */
    private BigDecimal actualPrice;

    /**
     * 实际积分
     */
    private BigDecimal actualPoints;
    /**
     * 实际知豆
     */
    private Long actualKnowledge;
}
