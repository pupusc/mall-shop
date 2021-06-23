package com.wanmi.sbc.elastic.standard.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = EsConstants.DOC_STANDARD_GOODS, type = EsConstants.DOC_STANDARD_GOODS)
public class EsStandardGoods {

    /**
     * 商品编号，采用UUID
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String goodsId;

    /**
     * 分类编号
     */
    @Field(type = FieldType.Long)
    private Long cateId;

    /**
     * 品牌编号
     */
    @Field(type = FieldType.Long)
    private Long brandId;

    /**
     * SPU编码
     */
    @Field(type = FieldType.Keyword)
    private String goodsNo;

    /**
     * SKU编码
     */
    @Field(type = FieldType.Keyword)
    private List<String> goodsInfoNos;

    /**
     * 商品名称
     */
    @Field(type = FieldType.Keyword)
    private String goodsName;

    /**
     * 商品副标题
     */
    @Field(type = FieldType.Keyword)
    private String goodsSubtitle;

    /**
     * 计量单位
     */
    @Field(type = FieldType.Keyword)
    private String goodsUnit;

    /**
     * 商品主图
     */
    @Field(type = FieldType.Keyword)
    private String goodsImg;

    /**
     * 商品重量
     */
    @Field(type = FieldType.Double)
    private BigDecimal goodsWeight;

    /**
     * 市场价
     */
    @Field(type = FieldType.Double)
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    @Field(type = FieldType.Double)
    private BigDecimal costPrice;

    /**
     * 供货价
     */
    @Field(type = FieldType.Double)
    private BigDecimal supplyPrice;

    /**
     * 建议零售价
     */
    @Field(type = FieldType.Double)
    private BigDecimal recommendedRetailPrice;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(index = false, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Date)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(index = false, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Date)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @Enumerated
    @Field(type = FieldType.Integer)
    private DeleteFlag delFlag;

    /**
     * 是否多规格标记
     */
    @Field(type = FieldType.Integer)
    private Integer moreSpecFlag;

    /**
     * 商品详情
     */
    @Field(type = FieldType.Text)
    private String goodsDetail;

    /**
     * 商品移动端详情
     */
    @Field(type = FieldType.Text)
    private String goodsMobileDetail;

    /**
     * 一对多关系，多个SKU编号
     */
    @Field(type = FieldType.Keyword)
    private List<String> goodsInfoIds;

    /**
     * 商品体积 单位：m3
     */
    @Field(type = FieldType.Double)
    private BigDecimal goodsCubage;

    /**
     * 商品视频链接
     */
    @Field(type = FieldType.Keyword)
    private String goodsVideo;

    /**
     * 供应商名称
     */
    @Field(type = FieldType.Keyword)
    private String providerName;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @Field(type = FieldType.Integer)
    private Integer goodsSource;

    /**
     * 删除原因
     */
    @Field(type = FieldType.Text)
    private String deleteReason;

    /**
     * 第三方平台的spuId
     */
    @Field(type = FieldType.Keyword)
    private String thirdPlatformSpuId;
    /**
     * 第三方卖家id
     */
    @Field(type = FieldType.Long)
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @Field(type = FieldType.Long)
    private Long thirdCateId;

    /**
     * 上下架状态,0:下架1:上架2:部分上架
     */
    @Field(type = FieldType.Integer)
    private Integer addedFlag;

    /**
     * 店铺id
     */
    @Field(type = FieldType.Long)
    private Long storeId;

    @Field(type = FieldType.Long)
    private long stock;

    @Field(type = FieldType.Keyword)
    private String providerGoodsId;

    /**
     * 已导入的店铺id
     */
    @Field(type = FieldType.Long)
    private List<Long> relStoreIds;
}
