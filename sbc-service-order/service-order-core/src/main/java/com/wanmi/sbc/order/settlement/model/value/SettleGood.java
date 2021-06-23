package com.wanmi.sbc.order.settlement.model.value;

import com.wanmi.sbc.account.bean.enums.ReturnStatus;
import com.wanmi.sbc.account.bean.enums.SettleCouponType;
import com.wanmi.sbc.account.bean.enums.SettleMarketingType;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettleGood {

    /**
     * 商品名称
     * trade - tradeItem - skuName
     */
    private String goodsName;

    /**
     * 商品sku编码
     * trade - tradeItem - skuNo
     */
    private String skuNo;

    /**
     * 商品规格描述
     * trade - tradeItem - specDetails
     */
    private String specDetails;

    /**
     * 商品价格
     * trade - tradeItem - price
     */
    private BigDecimal goodsPrice;

    /**
     * 三级分类名称
     * trade - tradeItem - cateName ?
     */
    private String cateName;

    /**
     * 扣点
     */
    private BigDecimal cateRate;

    /**
     * 商品数量
     * trade - tradeItem - num
     */
    private long num;

    /**
     * 实付金额
     */
    private BigDecimal splitPayPrice;

    /**
     * 退货数量
     */
    private Long returnNum;

    /**
     * 发起退单时的可退货数量
     */
    private Long canReturnNum;

    /**
     * 应退均摊金额
     */
    private BigDecimal shouldReturnPrice;

    /**
     * 货品skuId
     */
    private String skuId;

    /**
     * 退货状态
     */
    private ReturnStatus returnStatus;

    /**
     * 营销相关的均摊价对象
     */
    private List<MarketingSettlement> marketingSettlements;

    /**
     * 优惠券商品结算信息
     */
    private List<CouponSettlement> couponSettlements;

    /**
     * 积分抵扣金额
     */
    private BigDecimal pointPrice;

    /**
     * 佣金比例
     */
    private BigDecimal commissionRate;

    /**
     * 分销佣金
     */
    private BigDecimal commission;

    /**
     * 供货金额
     */
    private BigDecimal providerPrice;

    /**
     * 供货价
     */
    private BigDecimal supplyPrice;


    /**
     * 营销优惠商品结算Bean
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MarketingSettlement {

        /**
         * 营销类型
         */
        private SettleMarketingType marketingType;

        /**
         * 除去营销优惠金额后的商品均摊价
         */
        private BigDecimal splitPrice;
    }

    /**
     * 优惠券优惠商品结算Bean
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CouponSettlement implements Serializable {

        private static final long serialVersionUID = -5694365272429246275L;

        /**
         * 优惠券码id
         */
        private String couponCodeId;

        /**
         * 优惠券码
         */
        private String couponCode;

        /**
         * 优惠券类型
         */
        private SettleCouponType couponType;

        /**
         * 除去优惠金额后的商品均摊价
         */
        private BigDecimal splitPrice;

        /**
         * 优惠金额
         */
        private BigDecimal reducePrice;

    }
}
