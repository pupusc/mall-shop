package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.GrouponLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GoodsInfoDTO
 * 商品信息传输对象
 * @author lipeng
 * @dateTime 2018/11/6 下午2:29
 */
@ApiModel
@Data
public class GoodsInfoDTO implements Serializable {

    private static final long serialVersionUID = 9040193270990319318L;

    /**
     * 商品SKU编号
     */
    @ApiModelProperty(value = "商品SKU编号")
    private String goodsInfoId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * 商品SKU名称
     */
    @ApiModelProperty(value = "商品SKU名称")
    private String goodsInfoName;

    /**
     * 商品SKU编码
     */
    @ApiModelProperty(value = "商品SKU编码")
    private String goodsInfoNo;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsInfoImg;

    /**
     * 商品条形码
     */
    @ApiModelProperty(value = "商品条形码")
    private String goodsInfoBarcode;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存")
    private Long stock;


    /**
     * 总库存数量
     */
    private Long totalStock;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 商品供货价
     */
    @ApiModelProperty(value = "商品供货价")
    private BigDecimal supplyPrice;

    /**
     * 商品建议零售价
     */
    @ApiModelProperty(value = "商品建议零售价")
    private BigDecimal retailPrice;

    /**
     * 商品成本价
     */
    @ApiModelProperty(value = "商品成本价")
    private BigDecimal costPrice;

    /**
     * 商品sku定价
     */
    @ApiModelProperty(value = "商品sku定价")
    private BigDecimal fixPrice;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 上下架时间
     */
    @ApiModelProperty(value = "上下架时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    /**
     * 是否定时上架
     */
    @ApiModelProperty(value = "是否定时上架 true:是,false:否")
    private Boolean addedTimingFlag;

    /**
     * 定时上架时间
     */
    @ApiModelProperty(value = "定时上架时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTimingTime;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 按客户单独定价
     */
    @ApiModelProperty(value = "按客户单独定价", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer customFlag;


    /**
     * 是否可售
     */
    @ApiModelProperty(value = "是否可售", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer vendibility;

    /**
     * 是否叠加客户等级折扣
     */
    @ApiModelProperty(value = "是否叠加客户等级折扣", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer levelDiscountFlag;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private CheckStatus auditStatus;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型")
    private BoolFlag companyType;

    /**
     * 是否独立设价 1:是 0:否
     */
    @ApiModelProperty(value = "是否独立设价")
    private Boolean aloneFlag;

    /**
     * 最新计算的会员价
     * 为空，以市场价为准
     */
    @ApiModelProperty(value = "最新计算的会员价", notes = "为空，以市场价为准")
    private BigDecimal salePrice;

    /**
     * 设价类型 0:客户,1:订货
     */
    @ApiModelProperty(value = "设价类型", dataType = "com.wanmi.sbc.goods.bean.enums.PriceType")
    private Integer priceType;

    /**
     * 新增时，模拟多个规格ID
     * 查询详情返回响应，扁平化多个规格ID
     */
    @ApiModelProperty(value = "新增时，模拟多个规格ID", notes = "查询详情返回响应，扁平化多个规格ID")
    private List<Long> mockSpecIds;

    /**
     * 新增时，模拟多个规格值 ID
     * 查询详情返回响应，扁平化多个规格值ID
     */
    @ApiModelProperty(value = "新增时，模拟多个规格值 ID", notes = "查询详情返回响应，扁平化多个规格值ID")
    private  List<Long> mockSpecDetailIds;

    /**
     * 商品分页，扁平化多个商品规格值ID
     */
    @ApiModelProperty(value = "商品分页，扁平化多个商品规格值ID")
    private List<Long> specDetailRelIds;

    /**
     * 商品分页，扁平化多个商品规格值名称
     */
    private List<String> mockSpecDetailName;

    /**
     * 购买量
     */
    @ApiModelProperty(value = "购买量")
    private Long buyCount = 0L;

    /**
     * 最新计算的起订量
     * 为空，则不限
     */
    @ApiModelProperty(value = "最新计算的起订量", notes = "为空，则不限")
    private Long count;

    /**
     * 最新计算的限定量
     * 为空，则不限
     */
    @ApiModelProperty(value = "最新计算的限定量", notes = "为空，则不限")
    private Long maxCount;

    /**
     * 一对多关系，多个订货区间价格编号
     */
    @ApiModelProperty(value = "一对多关系，多个订货区间价格编号")
    private List<Long> intervalPriceIds;

    /**
     * 规格名称规格值 颜色:红色;大小:16G
     */
    @ApiModelProperty(value = "规格名称规格值", example = "颜色:红色;大小:16G")
    private String specText;

    /**
     * 最小区间价
     */
    @ApiModelProperty(value = "最小区间价")
    private BigDecimal intervalMinPrice;

    /**
     * 最大区间价
     */
    @ApiModelProperty(value = "最大区间价")
    private BigDecimal intervalMaxPrice;

    /**
     * 有效状态 0:无效,1:有效
     */
    @ApiModelProperty(value = "有效状态", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer validFlag;

    /**
     * 商品一级分类ID
     */
    @ApiModelProperty(value = "商品一级分类ID")
    private Long cateTopId;

    /**
     * 商品分类ID
     */
    @ApiModelProperty(value = "商品分类ID")
    private Long cateId;

    /**
     * 品牌ID
     */
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal distributionCommission;

    /**
     * 佣金比例
     */
    @ApiModelProperty(value = "佣金比例")
    private BigDecimal commissionRate;

    /**
     * 分销销量
     */
    @ApiModelProperty(value = "分销销量")
    private Integer distributionSalesCount;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @ApiModelProperty(value = "分销商品审核状态")
    private DistributionGoodsAudit distributionGoodsAudit;

    /**
     * 分销商品审核不通过或禁止分销原因
     */
    @ApiModelProperty(value = "分销商品审核不通过或禁止分销原因")
    private String distributionGoodsAuditReason;

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 前端是否选中
     */
    @ApiModelProperty(value = "前端是否选中", notes = "true: 是, false: 否")
    private Boolean checked = false;

    /**
     * 商品状态 0：正常 1：缺货 2：失效
     */
    @ApiModelProperty(value = "商品状态", notes = "0：正常 1：缺货 2：失效")
    private GoodsStatus goodsStatus = GoodsStatus.OK;

    /**
     * 计算单位
     */
    @ApiModelProperty(value = "计算单位")
    private String goodsUnit;

    /**
     * 促销标签
     */
    @ApiModelProperty(value = "促销标签")
    private List<MarketingLabelDTO> marketingLabels = new ArrayList<>();

    /**
     * 拼团标签
     */
    @ApiModelProperty(value = "促销标签")
    private GrouponLabelVO grouponLabel;

    /**
     * 优惠券标签
     */
    @ApiModelProperty(value = "优惠券标签")
    private List<CouponLabelDTO> couponLabels = new ArrayList<>();

    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积 单位：m3")
    private BigDecimal goodsCubage;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal goodsWeight;

    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTempId;

    /**
     * 销售类型 0:批发, 1:零售
     */
    @ApiModelProperty(value = "销售类型", dataType = "com.wanmi.sbc.goods.bean.enums.SaleType")
    private Integer saleType;

    /**
     * 是否允许独立设价 0:不允许, 1:允许
     */
    @ApiModelProperty(value = "是否允许独立设价", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer allowPriceSet;

    /**
     * 商品详情小程序码
     */
    private String  smallProgramCode;

    /**
     * 商品评论数
     */
    @ApiModelProperty(value = "商品评论数")
    private Long goodsEvaluateNum;

    /**
     * 商品收藏量
     */
    @ApiModelProperty(value = "商品收藏量")
    private Long goodsCollectNum;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量")
    private Long goodsSalesNum;

    /**
     * 商品好评数
     */
    @ApiModelProperty(value = "商品好评数")
    private Long goodsFavorableCommentNum;

    /**
     * 企业购商品的价格
     */
    @ApiModelProperty(value = "企业购商品的销售价格")
    private BigDecimal enterPrisePrice;

    /**
     * 企业购商品的审核状态
     */
    @ApiModelProperty(value = "企业购商品的审核状态" ,dataType = "com.wanmi.sbc.customer.bean.enums.EnterpriseAuditState")
    private EnterpriseAuditState enterPriseAuditState;

    /**
     * 企业购商品审核被驳回的原因
     */
    @ApiModelProperty(value = "企业购商品审核被驳回的原因")
    private String enterPriseGoodsAuditReason;

    /**
     * 商品来源，0供应商，1商家
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家")
    private Integer goodsSource;

    /**
     * 预售活动信息
     */
    @ApiModelProperty(value = "预售活动信息")
    private BookingSaleVO bookingSaleVO;


    /**
     * 预约活动信息
     */
    @ApiModelProperty(value = "预约活动信息")
    private AppointmentSaleVO appointmentSaleVO;

    /**
     * 所属供应商商品skuId
     */
    @ApiModelProperty(value = "所属供应商商品skuId")
    private String providerGoodsInfoId;

    /**
     * 供应商Id
     */
    @ApiModelProperty(value = "供应商Id")
    private Long providerId;

    /**
     * 第三方平台的spuId
     */
    @ApiModelProperty(value = "第三方平台的spuId")
    private String thirdPlatformSpuId;

    @ApiModelProperty("第三方平台的skuId")
    private String thirdPlatformSkuId;

    /**
     *三方平台类型，0，linkedmall
     */
    @ApiModelProperty("三方平台类型，0，linkedmall")
    private ThirdPlatformType thirdPlatformType;

    @ApiModelProperty("linkedmall卖家id")
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @ApiModelProperty("三方渠道类目id")
    private Long thirdCateId;

    /**
     * 供应商店铺状态 0：关店 1：开店
     */
    @ApiModelProperty(value = "供应商店铺状态 0：关店 1：开店")
    private Integer providerStatus;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    /**
     * ERP商品编码
     */
    @ApiModelProperty(value = "ERP商品编码")
    private String erpGoodsInfoNo;
    /**
     * 电子卡券id
     */
    @ApiModelProperty(value = "电子卡券id")
    private Long virtualCouponId;
    /**
     * 电子卡券名称
     */
    @ApiModelProperty(value = "电子卡券名称")
    private String virtualCouponName;
    /**
     * 周期购商品期数
     */
    @ApiModelProperty(value = "周期购商品期数")
    private Integer cycleNum;
    /**
     * ISBN编码
     */
    @ApiModelProperty(value = "ISBN编码")
    private String isbnNo;
    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @ApiModelProperty(value = "商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品")
    private Integer goodsType;


    /**
     * erp--spu编码
     */
    @ApiModelProperty(name = "erp--spu编码")
    private String erpGoodsNo;

    /**
     * 是否是组合商品，0：否，1：是
     */
    @ApiModelProperty(name = "是否是组合商品，0：否，1：是")
    private Boolean combinedCommodity;

    /**
     * 组合商品类型 0-组合商品 1-跨分类组合商品
     */
    @ApiModelProperty(name = "组合商品类型 0-组合商品 1-跨分类组合商品")
    private Integer combinedType;


    /**
     * 企业购 设价类型,0:按市场价 1:按会员等级设价 2:按购买数量设价
     */
    @ApiModelProperty(value = "企业购 设价类型")
    private Integer enterprisePriceType;

    /**
     * 企业购 以折扣设价 0:否 1:是
     */
    @ApiModelProperty(value = "企业购 以折扣设价")
    private Boolean enterpriseDiscountFlag;

    /**
     * 企业购 按客户单独定价,0:否 1:是
     */
    @ApiModelProperty(value = "企业购 按客户单独定价")
    private Boolean enterpriseCustomerFlag;

    /**
     * 企业购 最低价格
     */
    @ApiModelProperty(name = "企业购 最低价格")
    private BigDecimal enterpriseMinPrice;

    /**
     * 企业购 最高价格
     */
    @ApiModelProperty(name = "企业购 最高价格")
    private BigDecimal enterpriseMaxPrice;

    /**
     * 库存同步标记,0:否 1:是
     */
    @ApiModelProperty(value = "库存同步标记")
    private Integer stockSyncFlag;

    /**
     * 成本价同步标记,0:否 1:是
     */
    @ApiModelProperty(value = "成本价同步标记")
    private Integer costPriceSyncFlag;

    /**
     * 促销价格开始时间
     */
    @ApiModelProperty(value = "有效期开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime promotionStartTime;

    /**
     * 促销价格结束时间
     */
    @ApiModelProperty(value = "促销价格结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime promotionEndTime;

    /**
     * 商品渠道 1 H5 2、小程序 3、普通分类
     */
    private List<String> goodsChannelTypeSet;

    private String goodsChannelType;
}