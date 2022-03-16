package com.wanmi.sbc.elastic.goods.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ES商品实体类
 * 以SKU维度
 * Created by dyt on 2017/4/21.
 */
@Document(indexName = EsConstants.DOC_GOODS_INFO_TYPE, type = EsConstants.DOC_GOODS_INFO_TYPE)
@Data
public class EsGoodsInfo implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String goodsId;

    /**
     * 转化为小写
     */
    @Field(type = FieldType.Text)
    private String lowGoodsName;

    /**
     * 转化为小写
     */
    @Field(searchAnalyzer = EsConstants.PINYIN_ANALYZER, analyzer = EsConstants.PINYIN_ANALYZER, type = FieldType.Text)
    private String pinyinGoodsName;

    /**
     * SKU信息
     */
    @Field(type = FieldType.Object)
    private GoodsInfoNest goodsInfo;

    /**
     * 商品分类信息
     */
    @Field(type = FieldType.Object)
    private GoodsCateNest goodsCate;

    /**
     * 商品品牌信息
     */
    @Field(type = FieldType.Object)
    private GoodsBrandNest goodsBrand;

    /**
     * 上下架时间
     */
    @Field(index = false, type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    @Field(type = FieldType.Long)
    private List<Long> propDetailIds;

    /**
     * 等级价数据
     */
    private List<GoodsLevelPriceNest> goodsLevelPrices;

    /**
     * 客户价数据
     */
    private List<GoodsCustomerPriceNest> customerPrices;

    /**
     * 签约开始日期
     */
    @Field( type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;

    /**
     * 签约结束日期
     */
    @Field( type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @Field( type = FieldType.Integer)
    private Integer storeState;

    /**
     * 可售状态 0不可售 1可售
     */
    @Field( type = FieldType.Integer)
    private Integer vendibilityStatus;


    /**
     * 禁售状态
     */
    @Field( type = FieldType.Integer)
    private Integer forbidStatus;

    /**
     * 审核状态
     */
    @Field( type = FieldType.Integer)
    private Integer auditStatus;

    /**
     * 多对多关系，多个店铺分类编号
     */
    @Field( type = FieldType.Long)
    private List<Long> storeCateIds;

    /**
     * 分销商品状态，配合分销开关使用
     */
    @Field(type = FieldType.Integer)
    private Integer distributionGoodsStatus;

    /**
     * 排序号
     */
    @Field(type = FieldType.Long)
    private Long sortNo;

    @Field(index = false, type = FieldType.Text)
    @ApiModelProperty(value = "计算单位")
    private String goodsUnit;

    @ApiModelProperty(value = "划线价格")
    @Field(index = false, type = FieldType.Double)
    private BigDecimal linePrice;

    /**
     * 商品标签集合
     */
    @Field(type = FieldType.Nested)
    private List<GoodsLabelNest> goodsLabelList;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @Field(type = FieldType.Integer)
    private Integer goodsSource;

    /**
     * 商品SPU编码
     */
    @ApiModelProperty(value = "商品SPU编码")
    @Field(type = FieldType.Keyword)
    private String goodsNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    //@Field(type = FieldType.Keyword)
    @Field(searchAnalyzer = EsConstants.DEF_ANALYZER, analyzer = EsConstants.DEF_ANALYZER, type = FieldType.Text)
    private String goodsName;


    /**
     * 商品渠道 1 h5 2小程序
     */
    @Field(type = FieldType.Integer)
    private List<Integer> goodsChannelTypeList;
}
