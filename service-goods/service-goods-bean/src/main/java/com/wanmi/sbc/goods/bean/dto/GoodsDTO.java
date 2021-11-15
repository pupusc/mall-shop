package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsDTO implements Serializable {

    private static final long serialVersionUID = -8807674093765870168L;

    /**
     * 商品编号，采用UUID
     */
    @ApiModelProperty(value = "商品编号，采用UUID")
    private String goodsId;

    /**
     * 分类一级编号
     */
    @ApiModelProperty(value = "分类一级编号")
    private Long cateTopId;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    @CanEmpty
    private Long brandId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubtitle;

    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    private String goodsNo;

    /**
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位")
    @CanEmpty
    private String goodsUnit;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    @CanEmpty
    private String goodsImg;

    /**
     * 无背景图
     */
    @ApiModelProperty(value = "无背景图")
    private String goodsUnBackImg;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal goodsWeight;

    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    @CanEmpty
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    @CanEmpty
    private BigDecimal costPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    @CanEmpty
    private BigDecimal supplyPrice;

    /**
     * 建议零售价
     */
    @ApiModelProperty(value = "建议零售价")
    @CanEmpty
    private BigDecimal recommendedRetailPrice;

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
    @Enumerated
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
     * 是否多规格标记
     */
    @ApiModelProperty(value = "是否多规格标记")
    private Integer moreSpecFlag;

    /**
     * 设价类型 0:客户,1:订货
     */
    @ApiModelProperty(value = "设价类型", dataType = "com.wanmi.sbc.goods.bean.enums.PriceType")
    private Integer priceType;

    /**
     * 是否按客户单独定价
     */
    @ApiModelProperty(value = "是否按客户单独定价", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer customFlag;

    /**
     * 是否叠加客户等级折扣
     */
    @ApiModelProperty(value = "是否叠加客户等级折扣", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer levelDiscountFlag;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String supplierName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    /**
     * 所属供应商商品Id
     */
    @ApiModelProperty(value = "所属供应商商品Id")
    private String providerGoodsId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 提交审核时间
     */
    @ApiModelProperty(value = "提交审核时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime submitTime;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", notes = "0：待审核 1：已审核 2：审核失败 3：禁售中")
    @Enumerated
    private CheckStatus auditStatus;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty(value = "审核驳回原因")
    private String auditReason;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String goodsDetail;

    /**
     * 商品移动端详情
     */
    @ApiModelProperty(value = "商品移动端详情")
    private String goodsMobileDetail;

    /**
     * 库存，根据相关所有SKU库存来合计
     */
    @ApiModelProperty(value = "库存，根据相关所有SKU库存来合计")
    @Transient
    private Long stock;

    /**
     * 一对多关系，多个SKU编号
     */
    @ApiModelProperty(value = "一对多关系，多个SKU编号")
    @Transient
    private List<String> goodsInfoIds;

    /**
     * 多对多关系，多个店铺分类编号
     */
    @ApiModelProperty(value = "多对多关系，多个店铺分类编号")
    @Transient
    private List<Long> storeCateIds;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型", notes = "0、平台自营 1、第三方商家")
    @Enumerated
    private BoolFlag companyType;

    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积 单位：m3")
    private BigDecimal goodsCubage;

    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTempId;

    /**
     * 运费模板名称
     */
    @ApiModelProperty(value = "运费模板名称")
    @Transient
    private String freightTempName;

    /**
     * 销售类别
     */
    @ApiModelProperty(value = "销售类别", dataType = "com.wanmi.sbc.goods.bean.enums.SaleType")
    private Integer saleType;

    /**
     * 商品视频地址
     */
    @ApiModelProperty(value = "商品视频地址")
    @CanEmpty
    private String goodsVideo;

    /**
     * 划线价格
     */
    @ApiModelProperty(value = "划线价格")
    @CanEmpty
    private BigDecimal linePrice;

    /**
     * 订货量设价时,是否允许sku独立设阶梯价(0:不允许,1:允许)
     */
    @ApiModelProperty(value = "订货量设价时,是否允许sku独立设阶梯价", notes = "0:不允许,1:允许")
    private Integer allowPriceSet;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    private Integer goodsType;

    /**
     * 知识顾问专享 0：不是，1：是
     */
    private Integer cpsSpecial = 0;

    /**
     * 主播推荐 1樊登解读,2非凡精读,3樊登直播 内容以,相隔
     */
    @ApiModelProperty(value = "主播推荐 1樊登解读,2非凡精读,3樊登直播 内容以,相隔")
    private String anchorPushs = StringUtils.EMPTY;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    private Integer goodsSource;

    /**
     * 购买方式 0立即购买,1购物车,内容以,相隔
     */
    @ApiModelProperty(value = "购买方式 0立即购买,1购物车,内容以,相隔")
    private String goodsBuyTypes;

    /**
     * 是否单规格
     */
    @ApiModelProperty(value = "是否单规格")
    private Boolean singleSpecFlag;


    /**
     * 是否需要同步 0：不需要同步 1：需要同步
     */
    @ApiModelProperty(value = "是否需要同步 0：不需要同步 1：需要同步")
    @Enumerated
    private BoolFlag needSynchronize;

    /**
     * 删除原因
     */
    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    @ApiModelProperty("第三方平台的spuId")
    private String thirdPlatformSpuId;

    @ApiModelProperty("三方渠道的类型,0 linkedmall")
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
     * 标签id，以逗号拼凑
     */
    @ApiModelProperty(value = "标签id，以逗号拼凑")
    private String labelIdStr;

    /**
     * 标签名称，用于敏感词验证
     */
    @ApiModelProperty(value = "标签名称，用于敏感词验证", hidden = true)
    private String labelName;
    /**
     * erp商品编码
     */
    @ApiModelProperty(value = "erp商品编码")
    private String erpGoodsNo;
    /**
     * 商品标签列表
     */
    @ApiModelProperty(value = "商品标签列表", hidden = true)
    private List<GoodsLabelDTO> goodsLabelList;
}
