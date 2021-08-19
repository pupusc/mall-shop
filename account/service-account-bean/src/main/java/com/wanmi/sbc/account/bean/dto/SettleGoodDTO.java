package com.wanmi.sbc.account.bean.dto;

import com.wanmi.sbc.account.bean.enums.ReturnStatus;
import com.wanmi.sbc.account.bean.enums.SettleCouponType;
import com.wanmi.sbc.account.bean.enums.SettleMarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hht on 2017/12/7.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettleGoodDTO implements Serializable{

    private static final long serialVersionUID = -8381794612097526184L;

    /**
     * 商品名称
     * trade - tradeItem - skuName
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品sku编码
     * trade - tradeItem - skuNo
     */
    @ApiModelProperty(value = "商品sku编码")
    private String skuNo;

    /**
     * 商品规格描述
     * trade - tradeItem - specDetails
     */
    @ApiModelProperty(value = "商品规格描述")
    private String specDetails;

    /**
     * 商品价格
     * trade - tradeItem - price
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal goodsPrice;

    /**
     * 三级分类名称
     * trade - tradeItem - cateName ?
     */
    @ApiModelProperty(value = "三级分类名称")
    private String cateName;

    /**
     * 扣点
     */
    @ApiModelProperty(value = "扣点")
    private BigDecimal cateRate;

    /**
     * 商品数量
     * trade - tradeItem - num
     */
    @ApiModelProperty(value = "商品数量")
    private long num;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    private BigDecimal splitPayPrice;

    /**
     * 退货数量
     */
    @ApiModelProperty(value = "退货数量")
    private Long returnNum;

    /**
     * 发起退单时的可退货数量
     */
    @ApiModelProperty(value = "发起退单时的可退货数量")
    private Long canReturnNum;

    /**
     * 应退均摊金额
     */
    @ApiModelProperty(value = "应退均摊金额")
    private BigDecimal shouldReturnPrice;

    /**
     * 货品skuId
     */
    @ApiModelProperty(value = "货品skuId")
    private String skuId;

    /**
     * 退货状态
     */
    @ApiModelProperty(value = "退货状态")
    private ReturnStatus returnStatus;

    /**
     * 营销相关的均摊价对象
     */
    @ApiModelProperty(value = "营销相关的均摊价对象")
    private List<MarketingSettlementDTO> marketingSettlements;

    /**
     * 优惠券商品结算信息
     */
    @ApiModelProperty(value = "优惠券商品结算信息")
    private List<CouponSettlementDTO> couponSettlements;

    /**
     * 积分抵扣金额
     */
    @ApiModelProperty(value = "积分抵扣金额")
    private BigDecimal pointPrice;

    /**
     * 佣金比例
     */
    @ApiModelProperty(value = "佣金比例")
    private BigDecimal commissionRate;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal commission;

    /**
     * 供货金额
     */
    @ApiModelProperty(value = "供货金额")
    private BigDecimal providerPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;




    /**
     * 营销优惠商品结算Bean
     */
    @ApiModel
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MarketingSettlementDTO {

        /**
         * 营销类型
         */
        @ApiModelProperty(value = "营销类型")
        private SettleMarketingType marketingType;

        /**
         * 除去营销优惠金额后的商品均摊价
         */
        @ApiModelProperty(value = "除去营销优惠金额后的商品均摊价")
        private BigDecimal splitPrice;
    }

    /**
     * 优惠券优惠商品结算Bean
     */
    @ApiModel
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CouponSettlementDTO implements Serializable {

        private static final long serialVersionUID = -5694365272429246275L;

        /**
         * 优惠券码id
         */
        @ApiModelProperty(value = "优惠券码id")
        private String couponCodeId;

        /**
         * 优惠券码
         */
        @ApiModelProperty(value = "优惠券码")
        private String couponCode;

        /**
         * 优惠券类型
         */
        @ApiModelProperty(value = "优惠券类型")
        private SettleCouponType couponType;

        /**
         * 除去优惠金额后的商品均摊价
         */
        @ApiModelProperty(value = "除去优惠金额后的商品均摊价")
        private BigDecimal splitPrice;

        /**
         * 优惠金额
         */
        @ApiModelProperty(value = "优惠金额")
        private BigDecimal reducePrice;

    }
}
