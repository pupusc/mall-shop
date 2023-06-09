package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.IsAccountStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单商品
 * Created by jinwei on 19/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeItemVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 2973899410241708605L;

    @ApiModelProperty(value = "oid")
    private String oid;

    /**
     * 是否在积分黑名单
     */
    private Boolean inPointBlackList;

    /**
     * 商品所属的userId storeId?
     */
    @ApiModelProperty(value = "商品所属的userId storeId?")
    private String adminId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String supplierCode;

    @ApiModelProperty(value = "spuId")
    private String spuId;

    @ApiModelProperty(value = "spuName")
    private String spuName;

    @ApiModelProperty(value = "skuId")
    private String skuId;

    @ApiModelProperty(value = "skuName")
    private String skuName;

    @ApiModelProperty(value = "skuNo")
    private String skuNo;

    @ApiModelProperty(value = "第三方平台的spuId")
    private String thirdPlatformSpuId;

    @ApiModelProperty(value = "第三方平台的skuId")
    private String thirdPlatformSkuId;

    /**
     *第三方平台类型，0，linkedmall
     */
    @ApiModelProperty(value = "第三方平台类型，0，linkedmall")
    private ThirdPlatformType thirdPlatformType;

    /**
     * 第三方平台商品对应的子单号
     */
    @ApiModelProperty(value = "第三方平台商品对应的子单号")
    private String thirdPlatformSubOrderId;

    /**
     * 商品来源，0供应商，1商家 2linkedMall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家 2linkedMall")
    private Integer goodsSource;

    /**
     * 电子卡券id
     */
    @ApiModelProperty(value = "电子卡券id")
    private Long virtualCouponId;
    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal goodsWeight;
    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积")
    private BigDecimal goodsCubage;
    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTempId;

    /**
     * 顶级分类
     */
    @ApiModelProperty(value = "顶级分类id")
    private Long cateTopId;

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类id")
    private Long cateId;

    @ApiModelProperty(value = "分类名称")
    private String cateName;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String pic;

    /**
     * 商品品牌
     */
    @ApiModelProperty(value = "商品品牌")
    private Long brand;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Long num;

    /**
     * 已发货数量
     */
    @ApiModelProperty(value = "已发货数量")
    private Long deliveredNum = 0L;

    /**
     *  发货完成后更新字段
     */
    private Long deliveredNumHis = 0L;

    /**
     * 发货状态
     */
    @ApiModelProperty(value = "发货状态")
    private DeliverStatus deliverStatus;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 成交价格
     */
    @ApiModelProperty(value = "成交价格")
    private BigDecimal price;

    /**
     * 商品属性的定价
     */
    private Double propPrice;

    /**
     * 商品售价
     */
    private BigDecimal marketPrice;

    /**
     * 定金
     */
    @ApiModelProperty(value = "定金")
    private BigDecimal earnestPrice;

    /**
     * 定金膨胀
     */
    @ApiModelProperty(value = "定金膨胀")
    private BigDecimal swellPrice;

    /**
     * 尾款
     */
    @ApiModelProperty(value = "尾款")
    private BigDecimal tailPrice;

    /**
     * 定金支付开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelStartTime;

    /**
     * 定金支付结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelEndTime;

    /**
     * 尾款支付开始时间
     */
    @ApiModelProperty(value = "尾款支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailStartTime;

    /**
     * 尾款支付结束时间
     */
    @ApiModelProperty(value = "尾款支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailEndTime;

    /**
     * 商品原价 - 建议零售价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品价格 - 会员价 & 阶梯设价
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal levelPrice;

    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

    /**
     * 平摊小计 - 最初由 levelPrice*num（购买数量） 计算
     */
    @ApiModelProperty(value = "平摊小计")
    private BigDecimal splitPrice;

    /**
     * 货物id
     * added by shenchunnan
     */
    @ApiModelProperty(value = "货物id")
    private String bn;

    /**
     * 可退数量
     */
    @ApiModelProperty(value = "可退数量")
    private Integer canReturnNum;

//    /**
//     * 真实可退数量 无效
//     */
//    private Integer canReturnNumHis;

    /**
     * 规格描述信息
     */
    @ApiModelProperty(value = "规格描述信息")
    private String specDetails;

    /**
     * 分类扣率
     */
    @ApiModelProperty(value = "分类扣率")
    private BigDecimal cateRate;

    /**
     * 分销商品审核状态
     */
    @ApiModelProperty(value = "分销商品审核状态")
    private DistributionGoodsAudit distributionGoodsAudit;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal distributionCommission = BigDecimal.ZERO;

    /**
     * 分销佣金比例
     */
    @ApiModelProperty(value = "分销佣金比例")
    private BigDecimal commissionRate;

    /**
     * 商品参加的营销活动id集合
     */
    @ApiModelProperty(value = "商品参加的营销活动id集合")
    private List<Long> marketingIds = new ArrayList<>();

    /**
     * 商品参加的营销活动levelid集合
     */
    @ApiModelProperty(value = "商品参加的营销活动levelId集合")
    private List<Long> marketingLevelIds = new ArrayList<>();

    /**
     * 营销商品结算信息
     */
    @ApiModelProperty(value = "营销商品结算信息")
    private List<MarketingSettlementVO> marketingSettlements;
    /**
     * 虚拟卡券商品详细信息
     */
    private List<VirtualCouponVO> virtualCoupons = new ArrayList<>();

    /**
     * 是否入账状态
     */
    @ApiModelProperty(value = "是否入账状态")
    private IsAccountStatus isAccountStatus;

    /**
     * 优惠券商品结算信息(包括商品参加的优惠券信息)
     */
    @ApiModelProperty(value = "优惠券商品结算信息(包括商品参加的优惠券信息)")
    private List<CouponSettlementVO> couponSettlements = new ArrayList<>();

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Long points;

    /**
     * 积分价
     */
    @ApiModelProperty(value = "积分价")
    private Long buyPoint;

    /**
     * 积分兑换金额
     */
    @ApiModelProperty(value = "积分兑换金额")
    private BigDecimal pointsPrice;

    /**
     * 知豆，被用于知豆订单的商品知豆，普通订单的均摊知豆
     */
    private Long knowledge;
    /**
     * 积分兑换金额
     */
    private BigDecimal knowledgePrice;

    /**
     * 积分商品Id
     */
    @ApiModelProperty(value = "积分商品Id")
    private String pointsGoodsId;

    /**
     * 结算价格
     */
    @ApiModelProperty(value = "结算价格")
    private BigDecimal settlementPrice;

    /**
     * 是否是秒杀抢购商品
     */
    @ApiModelProperty(value = "是否是秒杀抢购商品")
    private Boolean isFlashSaleGoods=Boolean.FALSE;

    /**
     * 秒杀抢购商品Id
     */
    private Long flashSaleGoodsId;
    /**
     * 是否是加价购换购商品
     */
    private Boolean isMarkupGoods;
    /**
     * 是否是预约抢购商品
     */
    private Boolean isAppointmentSaleGoods = Boolean.FALSE;

    /**
     * 抢购活动Id
     */
    private Long appointmentSaleId;


    /**
     * 是否是预售商品
     */
    private Boolean isBookingSaleGoods = Boolean.FALSE;

    /**
     * 预售活动Id
     */
    private Long bookingSaleId;

    /**
     * 预售类型
     */
    private BookingType bookingType;

    /**
     * 企业购商品的审核状态
     */
    private EnterpriseAuditState enterPriseAuditState;

    /**
     * 企业购商品的价格
     */
    private BigDecimal enterPrisePrice;

    /**
     * 供应商id
     */
    private Long providerId;

    /**
     * 供货价
     */
    private BigDecimal supplyPrice;

    /**
     * 供货价总额
     */
    private BigDecimal totalSupplyPrice;

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 供应商编号
     */
    private String providerCode;

    /**
     * 供应商SKU编码
     */
    private String providerSkuNo;

    /**
     * 商品状态
     */
    private GoodsStatus goodsStatus;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    private Integer cpsSpecial;

    /**
     * 期数
     */
    private Integer cycleNum;


    /**
     * ERP商品SKU编码
     */
    private String erpSkuNo;

    /**
     * ERP商品SPU编码
     */
    private String erpSpuNo;


    /**
     * 是否是组合商品，0：否，1：是
     */
    @ApiModelProperty(name = "是否是组合商品，0：否，1：是")
    private Boolean combinedCommodity;


     /**
     * 退款信息
     */
    private TradeReturnVO tradeReturn;

    /**
     * 打包id
     */
    private String packId;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Long stock;

    /**
     * 引导文案
     */
    private String guideText;

    /**
     * 引导图片
     */
    private String guideImg;

    /**
     * 显示电话输入框
     */
    private Boolean showPhoneNum = false;

    /**
     * 活动信息
     */
    private NormalActivity activity;

    /**
     * 营销优惠商品结算Bean
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MarketingSettlementVO implements Serializable {

        private static final long serialVersionUID = -1739585650762904572L;

        /**
         * 营销类型
         */
        @ApiModelProperty(value = "营销类型")
        private MarketingType marketingType;

        /**
         * 除去营销优惠金额后的商品均摊价
         */
        @ApiModelProperty(value = "除去营销优惠金额后的商品均摊价")
        private BigDecimal splitPrice;
    }
    /**
     * 电子卡券详细信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VirtualCouponVO implements Serializable {

        private static final long serialVersionUID = -1739585650762904572L;

        /**
         * 电子卡券ID
         */
        @ApiModelProperty(value = "电子卡券ID")
        private Long couponId;
        /**
         * 券码ID
         */
        @ApiModelProperty(value = "券码ID")
        private Long id;
        /**
         * 有效期
         */
        @ApiModelProperty(value = "有效期")
        private Integer validDays;

        /**
         * 0:兑换码 1:券码+密钥 2:链接
         */
        @ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
        private Integer provideType;

        /**
         * 兑换码/券码/链接
         */
        @ApiModelProperty(value = "兑换码/券码/链接")
        private String couponNo;

        /**
         * 密钥
         */
        @ApiModelProperty(value = "密钥")
        private String couponSecret;

        /**
         * 领取结束时间
         */
        @ApiModelProperty(value = "领取结束时间")
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime receiveEndTime;

        /**
         * 兑换开始时间
         */
        @ApiModelProperty(value = "兑换开始时间")
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime exchangeStartTime;

        /**
         * 兑换结束时间
         */
        @ApiModelProperty(value = "兑换结束时间")
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime exchangeEndTime;

    }
    /**
     * 优惠券优惠商品结算Bean
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CouponSettlementVO implements Serializable {

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
        private CouponType couponType;

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




    /**
     * 活动信息
     */
    @Data
    public static class NormalActivity {
        /**
         * 份数
         */
        private Integer num;

        /**
         * 活动展示
         */
        private String activityShow;

    }
}
