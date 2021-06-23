package com.wanmi.sbc.elastic.bean.dto.goods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库实体类
 * Created by dyt on 2017/4/11.
 */
@Data
public class EsStandardGoodsDTO implements Serializable {

    private static final long serialVersionUID = 8270069437627689630L;

    /**
     * 商品库id
     */
    @ApiModelProperty(value = "商品库id")
    private String goodsId;

    /**
     * 商品分类id
     */
    @ApiModelProperty(value = "商品分类id")
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
     * SKU编码
     */
    @ApiModelProperty(value = "SKU编码")
    private List<String> goodsInfoNos;

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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @Enumerated
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    /**
     * 是否多规格标记
     */
    @ApiModelProperty(value = "是否多规格标记")
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
    @ApiModelProperty(value = "商品体积")
    private BigDecimal goodsCubage;

    /**
     * 商品视频链接
     */
    @ApiModelProperty(value = "商品视频链接")
    private String goodsVideo;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家,2 linkedmall ")
    private Integer goodsSource;

    /**
     * 删除原因
     */
    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    /**
     * 第三方平台的spuId
     */
    @ApiModelProperty(value = "第三方平台的spuId")
    private String thirdPlatformSpuId;
    /**
     * 第三方卖家id
     */
    @ApiModelProperty(value = "第三方卖家id")
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @ApiModelProperty(value = "三方渠道类目id")
    private Long thirdCateId;

    /**
     * 上下架状态,0:下架1:上架2:部分上架
     */
    @ApiModelProperty(value = "上下架状态,0:下架1:上架2:部分上架")
    private Integer addedFlag;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty(value = "库存")
    private long stock;

    @ApiModelProperty(value = "供应商商品id")
    private String providerGoodsId;

    /**
     * 已导入的店铺id
     */
    @ApiModelProperty(value = "已导入的店铺id")
    private List<Long> relStoreIds;
}
