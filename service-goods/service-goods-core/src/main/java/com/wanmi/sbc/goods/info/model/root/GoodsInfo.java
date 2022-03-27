package com.wanmi.sbc.goods.info.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.marketing.CouponLabel;
import com.wanmi.sbc.goods.marketing.MarketingLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "goods_info")
public class GoodsInfo implements Serializable {

    /**
     * 商品SKU编号
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 商品SKU名称
     */
    @Column(name = "goods_info_name")
    private String goodsInfoName;

    /**
     * 商品SKU编码
     */
    @Column(name = "goods_info_no")
    private String goodsInfoNo;

    /**
     * 商品图片
     */
    @Column(name = "goods_info_img")
    @CanEmpty
    private String goodsInfoImg;

    /**
     * 商品条形码
     */
    @Column(name = "goods_info_barcode")
    @CanEmpty
    private String goodsInfoBarcode;

    /**
     * 商品库存
     */
    @Column(name = "stock")
    private Long stock;

    /**
     * 商品市场价
     */
    @Column(name = "market_price")
    private BigDecimal marketPrice;

    /**
     * 商品供货价
     */
    @Column(name = "supply_price")
    private BigDecimal supplyPrice;

    /**
     * 建议零售价价
     */
    @Column(name = "retail_price")
    private BigDecimal retailPrice;

    /**
     * 商品成本价
     */
    @CanEmpty
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 上下架时间
     */
    @Column(name = "added_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 上下架状态
     */
    @Column(name = "added_flag")
    private Integer addedFlag;

    /**
     * 是否定时上架
     */
    @Column(name = "added_timing_flag")
    private Boolean addedTimingFlag;

    /**
     * 定时上架时间
     */
    @Column(name = "added_timing_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTimingTime;

    /**
     * 公司信息ID
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 按客户单独定价
     */
    @Column(name = "custom_flag")
    private Integer customFlag;

    /**
     * 是否可售
     */
    @Column(name = "vendibility")
    private Integer vendibility;

    /**
     * 是否叠加客户等级折扣
     */
    @Column(name = "level_discount_flag")
    private Integer levelDiscountFlag;

    /**
     * 审核状态
     */
    @Enumerated
    @Column(name = "audit_status")
    private CheckStatus auditStatus;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @Enumerated
    @Column(name = "company_type")
    private BoolFlag companyType;

    /**
     * 是否独立设价 1:是 0:否
     */
    @Column(name = "alone_flag")
    private Boolean aloneFlag;

    /**
     * 商品详情小程序码
     */
    @Column(name = "small_program_code")
    private String smallProgramCode;

    /**
     * 预估佣金
     */
    @Column(name = "distribution_commission")
    private BigDecimal distributionCommission;

    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    /**
     * 分销销量
     */
    @Column(name = "distribution_sales_count")
    private Integer distributionSalesCount;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @Column(name = "distribution_goods_audit")
    private DistributionGoodsAudit distributionGoodsAudit;

    /**
     * 分销商品审核不通过或禁止分销原因
     */
    @Column(name = "distribution_goods_audit_reason")
    private String distributionGoodsAuditReason;

    /**
     * 商品一级分类ID
     */
    @Column(name = "cate_top_id")
    private Long cateTopId;

    /**
     * 商品分类ID
     */
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 品牌ID
     */
    @Column(name = "brand_id")
    private Long brandId;

//    @OneToOne(fetch = FetchType.LAZY, optional = true)
//    @JoinColumn(name = "goods_id", insertable = false, updatable = false)
//    private Goods goods;

    /**
     * 销售类型 0:批发, 1:零售
     */
    @Column(name = "sale_type")
    private Integer saleType;

    /**
     * 企业购商品的价格
     */
    @Column(name = "enterprise_price")
    private BigDecimal enterPrisePrice;

    /**
     * 企业购商品审核状态
     */
    @Column(name = "enterprise_goods_audit")
    @Enumerated
    private EnterpriseAuditState enterPriseAuditState;

    /**
     * 企业购商品审核被驳回的原因
     */
    @Column(name = "enterprise_goods_audit_reason")
    private String enterPriseGoodsAuditReason;

    /**
     * 购买积分
     */
    @Column(name = "buy_point")
    private Long buyPoint;

    /**
     * 所属供应商商品skuId
     */
    @Column(name = "provider_goods_info_id")
    private String providerGoodsInfoId;

    /**
     * 供应商Id
     */
    @Column(name = "provider_id")
    private Long providerId;

    /**
     * 是否允许独立设价 0:不允许, 1:允许
     */
    @Transient
    private Integer allowPriceSet;

    /**
     * 多对多关系，多个店铺分类编号
     */
    @Transient
    private List<Long> storeCateIds;

    /**
     * 最新计算的会员价
     * 为空，以市场价为准
     */
    @Transient
    private BigDecimal salePrice;

    /**
     * 设价类型 0:按客户 1:按订货量 2:按市场价
     */
    @Transient
    private Integer priceType;

    /**
     * 新增时，模拟多个规格ID
     * 查询详情返回响应，扁平化多个规格ID
     */
    @Transient
    private List<Long> mockSpecIds;

    /**
     * 新增时，模拟多个规格值 ID
     * 查询详情返回响应，扁平化多个规格值ID
     */
    @Transient
    private List<Long> mockSpecDetailIds;

    /**
     * 新增时，模拟多个规格值名称
     * 查询详情返回响应，扁平化多个规格值名称
     */
    @Transient
    private List<String> mockSpecDetailName;
    /**
     * 商品分页，扁平化多个商品规格值ID
     */
    @Transient
    private List<Long> specDetailRelIds;

    /**
     * 购买量
     */
    @Transient
    private Long buyCount = 0L;

    /**
     * 最新计算的起订量
     * 为空，则不限
     */
    @Transient
    private Long count;

    /**
     * 最新计算的限定量
     * 为空，则不限
     */
    @Transient
    private Long maxCount;

    /**
     * 一对多关系，多个订货区间价格编号
     */
    @Transient
    private List<Long> intervalPriceIds;

    /**
     * 规格名称规格值 颜色:红色;大小:16G
     */
    @Transient
    private String specText;

    /**
     * 最小区间价
     */
    @Transient
    private BigDecimal intervalMinPrice;

    /**
     * 最大区间价
     */
    @Transient
    private BigDecimal intervalMaxPrice;

    /**
     * 有效状态 0:无效,1:有效
     */
    @Transient
    private Integer validFlag;

    /**
     * 前端是否选中
     */
    @Transient
    private Boolean checked = false;

    /**
     * 商品状态 0：正常 1：缺货 2：失效
     */
    @Transient
    private GoodsStatus goodsStatus = GoodsStatus.OK;

    /**
     * 计算单位
     */
    @Transient
    private String goodsUnit;

    /**
     * 促销标签
     */
    @Transient
    private List<MarketingLabel> marketingLabels = new ArrayList<>();

    /**
     * 优惠券标签
     */
    @Transient
    private List<CouponLabel> couponLabels = new ArrayList<>();

    /**
     * 商品体积 单位：m3
     */
    @Transient
    private BigDecimal goodsCubage;

    /**
     * 商品重量
     */
    @Transient
    private BigDecimal goodsWeight;

    /**
     * 运费模板ID
     */
    @Transient
    private Long freightTempId;

    /**
     * 商品评论数
     */
    @Transient
    private Long goodsEvaluateNum;

    /**
     * 商品收藏量
     */
    @Transient
    private Long goodsCollectNum;

    /**
     * 商品销量
     */
    @Transient
    private Long goodsSalesNum;

    /**
     * 商品好评数
     */
    @Transient
    private Long goodsFavorableCommentNum;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @Column(name = "goods_source")
    private Integer goodsSource;

    /**
     * 第三方平台的skuId
     */
    @Column(name = "third_platform_sku_id")
    private String thirdPlatformSkuId;

    /**
     * 第三方平台的spuId
     */
    @Column(name = "third_platform_spu_id")
    private String thirdPlatformSpuId;

    /**
     * 第三方卖家id
     */
    @Column(name = "seller_id")
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @Column(name = "third_cate_id")
    private Long thirdCateId;
    /**
     * 三方平台类型，0，linkedmall
     */
    @Column(name = "third_platform_type")
    @Enumerated
    private ThirdPlatformType thirdPlatformType;

    /**
     * 供应商店铺状态 0：关店 1：开店
     */
    @Column(name = "provider_status")
    private Integer providerStatus;

    /**
     * 店铺名称
     */
    @Transient
    private String storeName;


    /**
     * ERP商品编码
     */
    @Column(name = "erp_goods_info_no")
    private String erpGoodsInfoNo;

    /**
     * 电子卡券id
     */
    @Column(name = "virtual_coupon_id")
    private Long virtualCouponId;

    /**
     * 电子卡券名称
     */
    @Transient
    private String virtualCouponName;

    /**
     * 周期购商品期数
     */
    @Column(name = "cycle_num")
    private Integer cycleNum;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @Column(name = "goods_type")
    private Integer goodsType;
    /**
     * ISBN编码
     */
    @Column(name = "isbn_no")
    private String isbnNo;

    /**
     * erp--spu编码
     */
    @Column(name = "erp_goods_no")
    private String erpGoodsNo;

    /**
     * 是否是组合商品，0：否，1：是
     */
    @Column(name = "combined_commodity")
    private Boolean combinedCommodity;

    /**
     * 组合商品类型 0-组合商品 1-跨分类组合商品
     */
    @Column(name = "combined_type")
    private Integer combinedType;

    /**
     * 企业购 设价类型,0:按市场价 1:按会员等级设价 2:按购买数量设价
     */
    @Column(name = "enterprise_price_type")
    private Integer enterprisePriceType;

    /**
     * 企业购 以折扣设价 0:否 1:是
     */
    @Column(name = "enterprise_discount_flag")
    private Boolean enterpriseDiscountFlag;

    /**
     * 企业购 按客户单独定价,0:否 1:是
     */
    @Column(name = "enterprise_customer_flag")
    private Boolean enterpriseCustomerFlag;

    /**
     * 企业购商品的最低格
     */
    @Transient
    private BigDecimal enterPriseMinPrice;

    /**
     * 企业购商品的最高格
     */
    @Transient
    private BigDecimal enterPriseMaxPrice;

    /**
     * 库存同步 0否 1是
     */
    @Column(name = "stock_sync_flag")
    private Integer stockSyncFlag;

    /**
     * 成本价同步标记,0:否 1:是
     */
    @Column(name = "cost_price_sync_flag")
    private Integer costPriceSyncFlag;

    /**
     * 促销价格开始时间
     */
    @Column(name = "promotion_start_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime promotionStartTime;

    /**
     * 促销价格结束时间
     */
    @Column(name = "promotion_end_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime promotionEndTime;

    /**
     * 商品渠道 1 H5 2、小程序 3、普通分类 多个以,分割
     */
    @Column(name = "channel_type")
    private String goodsChannelType;
}
