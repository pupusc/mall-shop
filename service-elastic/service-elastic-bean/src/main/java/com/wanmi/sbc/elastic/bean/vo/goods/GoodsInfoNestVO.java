package com.wanmi.sbc.elastic.bean.vo.goods;

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
import com.wanmi.sbc.goods.bean.vo.CouponLabelVO;
import com.wanmi.sbc.goods.bean.vo.GrouponLabelVO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
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
@ApiModel
public class GoodsInfoNestVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 拼团价
     */
    @ApiModelProperty(value = "拼团价")
    private BigDecimal grouponPrice;

    /**
     * 商品成本价
     */
    @ApiModelProperty(value = "商品成本价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "商品供货价")
    private BigDecimal supplyPrice;

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
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态",dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

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
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 按客户单独定价
     */
    @ApiModelProperty(value = "按客户单独定价")
    private Integer customFlag;

    /**
     * 是否叠加客户等级折扣
     */
    @ApiModelProperty(value = "是否叠加客户等级折扣")
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
    @ApiModelProperty(value = "最新计算的会员价")
    private BigDecimal salePrice;

    /**
     * 设价类型 0:客户,1:订货
     */
    @ApiModelProperty(value = "设价类型")
    private Integer priceType;

    /**
     * 新增时，模拟多个规格ID
     * 查询详情返回响应，扁平化多个规格ID
     */
    @ApiModelProperty(value = "扁平化多个规格ID")
    private List<Long> mockSpecIds;

    /**
     * 新增时，模拟多个规格值 ID
     * 查询详情返回响应，扁平化多个规格值ID
     */
    @ApiModelProperty(value = "扁平化多个规格值ID")
    private  List<Long> mockSpecDetailIds;

    /**
     * 商品分页，扁平化多个商品规格值ID
     */
    @ApiModelProperty(value = "商品分页")
    private List<Long> specDetailRelIds;

    /**
     * 购买量
     */
    @ApiModelProperty(value = "购买量")
    private Long buyCount = 0L;

    /**
     * 最新计算的起订量
     * 为空，则不限
     */
    @ApiModelProperty(value = "最新计算的起订量")
    private Long count;

    /**
     * 最新计算的限定量
     * 为空，则不限
     */
    @ApiModelProperty(value = "最新计算的限定量")
    private Long maxCount;

    /**
     * 一对多关系，多个订货区间价格编号
     */
    @ApiModelProperty(value = "多个订货区间价格编号")
    private List<Long> intervalPriceIds;

    /**
     * 规格名称规格值 颜色:红色;大小:16G
     */
    @ApiModelProperty(value = "规格名称规格值")
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
    @ApiModelProperty(value = "有效状态")
    private Integer validFlag;

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
     * 多对多关系，多个店铺分类编号
     */
    @ApiModelProperty(value = "多对多关系，多个店铺分类编号")
    private List<Long> storeCateIds;

    /**
     * 预估佣金
     */
    @ApiModelProperty(value = "预估佣金")
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
    @Enumerated
    private DistributionGoodsAudit distributionGoodsAudit;

    /**
     * 分销商品审核不通过或禁止分销原因
     */
    @ApiModelProperty(value = "分销商品审核不通过或禁止分销原因")
    private String distributionGoodsAuditReason;

    /**
     * 前端是否选中
     */
    @ApiModelProperty(value = "前端是否选中")
    private Boolean checked = false;

    /**
     * 商品状态 0：正常 1：缺货 2：失效
     */
    @ApiModelProperty(value = "商品状态")
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
    private List<MarketingLabelVO> marketingLabels = new ArrayList<>();

    /**
     * 拼团标签
     */
    @ApiModelProperty(value = "促销标签")
    private GrouponLabelVO grouponLabel;

    /**
     * 优惠券标签
     */
    @ApiModelProperty(value = "优惠券标签")
    private List<CouponLabelVO> couponLabels = new ArrayList<>();

    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积")
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
    @ApiModelProperty(value = "商品详情小程序码")
    private String  smallProgramCode;

    /**
     * 是否已关联分销员
     */
    @ApiModelProperty(value = "是否已关联分销员，0：否，1：是")
    private Integer joinDistributior;

    /**
     * 分销员商品表ID
     */
    @ApiModelProperty(value = "分销员商品表ID")
    private String distributionGoodsInfoId;

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
     * 商品好评数量
     */
    @ApiModelProperty(value = "商品好评数量")
    private Long goodsFavorableCommentNum;

    /**
     * 商品好评率
     */
    private Long goodsFeedbackRate;

    /**
     * 企业购商品的价格
     */
    @ApiModelProperty(value = "企业购商品的销售价格")
    private BigDecimal enterPrisePrice;


    /**
     * 企业购商品的最低格
     */
    @ApiModelProperty(value = "企业购商品的最低格")
    private BigDecimal enterPriseMinPrice;

    /**
     * 企业购商品的最高格
     */
    @ApiModelProperty(value = "企业购商品的最高格")
    private BigDecimal enterPriseMaxPrice;

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
     * 企业购商品审核的状态
     */
    @ApiModelProperty(value = "企业购商品的审核状态" ,dataType = "com.wanmi.sbc.customer.bean.enums.EnterpriseAuditState")
    private Integer enterPriseAuditStatus = EnterpriseAuditState.INIT.toValue();

    /**
     * 企业购商品审核未通过的原因
     */
    @ApiModelProperty(value = "企业购商品审核未通过的原因")
    private String enterPriseGoodsAuditReason;

    /**
     * 排序的价格
     */
    @ApiModelProperty(value = "排序的价格")
    private BigDecimal esSortPrice;

    public void setEsSortPrice(){
        this.esSortPrice = enterPriseAuditStatus == EnterpriseAuditState.CHECKED.toValue() ? enterPrisePrice : marketPrice;
    }

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 三方渠道类型，0 linkedmall
     */
    private ThirdPlatformType thirdPlatformType;
    /**
     * 三方渠道spuid
     */
    private String thirdPlatformSpuId;
    /**
     * 三方渠道skuid
     */
    private String thirdPlatformSkuId;

    /**
     * 所属供应商商品skuId
     */
    private String providerGoodsInfoId;

    /**
     * 供应商Id
     */
    private Long providerId;

    /**
     * 可售状态 0不可收 1可售
     */
    private Integer vendibilityStatus;

    /**
     * 供应商店铺状态 0：关店 1：开店
     */
    private Integer providerStatus;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 付费会员图标
     */
    @ApiModelProperty(value = "付费会员图标")
    private String paidCardIcon;

    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    @ApiModelProperty("氛围类型1通用类型2积分类型")
    private Integer atmosType;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("元素1,左侧第一行文字")
    private String elementOne;

    @ApiModelProperty("元素2：左侧第二行文字")
    private String elementTwo;

    @ApiModelProperty("元素3：右侧第一行文字")
    private String elementThree;

    @ApiModelProperty("元素4：右侧第二行文字")
    private String elementFour;

}
