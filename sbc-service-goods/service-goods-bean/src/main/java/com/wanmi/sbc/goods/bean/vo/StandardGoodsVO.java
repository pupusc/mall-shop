package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商品库</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardGoodsVO implements Serializable {

    private static final long serialVersionUID = -7485234599937997645L;

    /**
     * 商品编号，采用UUID
     */
    @ApiModelProperty(value = "商品编号，采用UUID")
    private String goodsId;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    private String goodsNo;

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
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位")
    private String goodsUnit;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    private String goodsImg;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal goodsWeight;

    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价")
    private BigDecimal costPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 建议零售价
     */
    @ApiModelProperty(value = "建议零售价")
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
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否多规格标记
     */
    @ApiModelProperty(value = "是否多规格标记", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer moreSpecFlag;

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
     * 一对多关系，多个SKU编号
     */
    @ApiModelProperty(value = "一对多关系，多个SKU编号")
    private List<String> goodsInfoIds;

    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积", notes = "单位：m3")
    private BigDecimal goodsCubage;

    /**
     * 商品视频链接
     */
    @ApiModelProperty(value = "商品视频链接")
    private String goodsVideo;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 商品来源，0供应商，1商家
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家,2linkedmall")
    private Integer goodsSource;

    /**
     * 删除原因
     */
    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    /**
     * 第三方平台的spuId
     */
    @ApiModelProperty("第三方平台的spuId")
    private String thirdPlatformSpuId;

    /**
     * 第三方卖家id
     */
    @ApiModelProperty("第三方卖家id")
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @ApiModelProperty("三方渠道类目id")
    private Long thirdCateId;

    @ApiModelProperty("上下架状态，0下架，1上架，2部分上架")
    private Integer addedFlag;

    @ApiModelProperty("库存")
    private Long stock;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("供应商goodsId")
    private String providerGoodsId;

    @ApiModelProperty("批量店铺分类")
    private List<Long> storeCateIds;

}
