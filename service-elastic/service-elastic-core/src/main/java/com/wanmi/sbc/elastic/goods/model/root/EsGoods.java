package com.wanmi.sbc.elastic.goods.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.goods.api.response.classify.ClassifySimpleProviderResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ES商品实体类
 * 以SKU维度
 * Created by dyt on 2017/4/21.
 */
@Document(indexName = EsConstants.DOC_GOODS_TYPE, type = EsConstants.DOC_GOODS_TYPE)
@Data
public class EsGoods implements Serializable {

    @Id
    private String id;

    /**
     * 转化为小写
     */
    @Field(type = FieldType.Text)
    private String lowGoodsName;

    /**
     * 无背景图
     */
    @Field(type = FieldType.Text)
    private String goodsUnBackImg;

    /**
     * 转化为小写
     */
    @Field(searchAnalyzer = EsConstants.PINYIN_ANALYZER, analyzer = EsConstants.PINYIN_ANALYZER, type = FieldType.Text)
    private String pinyinGoodsName;

    /**
     * SKU信息auditStatus
     */
    @Field(type = FieldType.Object)
    private List<GoodsInfoNest> goodsInfos;

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
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    /**
     * 上下架时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTimeNew;

    /**
     * SKU相关规格
     */
    @Field(type = FieldType.Nested)
    private List<GoodsInfoSpecDetailRelNest> specDetails;

    /**
     * 商品属性
     */
    private GoodsExtProps goodsExtProps;
//    @Field(type = FieldType.Nested)
//    private List<GoodsPropDetailNested> propDetailNesteds;

    @Field(type = FieldType.Long)
    private List<Long> propDetailIds;

    /**
     * 等级价数据
     */
    private List<GoodsLevelPriceNest> goodsLevelPrices = new ArrayList<>();

    /**
     * 客户价数据
     */
    private List<GoodsCustomerPriceNest> customerPrices = new ArrayList<>();

    /**
     * 签约开始日期
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;

    /**
     * 签约结束日期
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @Field(type = FieldType.Integer)
    private Integer storeState;

    /**
     * 可售状态 0不可收 1可售
     */
    @Field(type = FieldType.Integer)
    private Integer vendibilityStatus;

    /**
     * 禁售状态
     */
    @Field(type = FieldType.Integer)
    private Integer forbidStatus;

    /**
     * 审核状态
     */
    @Field(type = FieldType.Integer)
    private Integer auditStatus;

    /**
     * 多对多关系，多个店铺分类编号
     */
    @Field(type = FieldType.Long)
    private List<Long> storeCateIds;

    /**
     * 店铺分类
     */
    private List<ClassifySimpleProviderResponse> classify;

    /**
     * 营销信息
     */
    private List<MarketingForEndVO> marketingList = new ArrayList<>();

    /**
     * 分销商品状态，配合分销开关使用
     */
    @Field(type = FieldType.Integer)
    private Integer distributionGoodsStatus;

    /**
     * 商品评论数
     */
    @Field(index = false, type = FieldType.Long)
    private Long goodsEvaluateNum;

    /**
     * 商品收藏量
     */
    @Field(index = false, type = FieldType.Long)
    private Long goodsCollectNum;

    /**
     * 商品销量
     */
    @Field(type = FieldType.Long)
    private Long goodsSalesNum;
    /**
     * 商品销量
     */
    @Field(type = FieldType.Long)
    private Long goodsSalesNumNew;

    /**
     * 真实的商品销量
     */
    @Field(index = false, type = FieldType.Long)
    private Long realGoodsSalesNum;

    /**
     * 商品好评数量
     */
    @Field(index = false, type = FieldType.Long)
    private Long goodsFavorableCommentNum;

    /**
     * 商品好评率
     */
    @Field(index = false, type = FieldType.Long)
    private Long goodsFeedbackRate;

    /**
     * 排序号
     */
    @Field(type = FieldType.Long)
    private Long sortNo;

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    @Field(type = FieldType.Long)
    private Long buyPoint;

    /**
     * 排序的价格
     */
    @ApiModelProperty(value = "排序的价格")
    @Field(type = FieldType.Double)
    private BigDecimal esSortPrice;

    /**
     * 排序的价格
     */
    @ApiModelProperty(value = "排序的价格")
    @Field(type = FieldType.Double)
    private BigDecimal sortPrice;

    @Field(index = false, type = FieldType.Text)
    @ApiModelProperty(value = "计算单位")
    private String goodsUnit;

    @ApiModelProperty(value = "划线价格")
    @Field(index = false, type = FieldType.Double)
    private BigDecimal linePrice;

    /**
     * 三方渠道类型，0 linkedmall
     */
    @Field(type = FieldType.Integer)
    private ThirdPlatformType thirdPlatformType;

    /**
     * 商品标签集合
     */
    @Field(type = FieldType.Nested)
    private List<GoodsLabelNest> goodsLabelList;

    /**
     * 供应商id
     */
    @Field(type = FieldType.Long)
    private Long providerId;

    /**
     * 供应商名称
     */
    @Field(type = FieldType.Keyword)
    private String providerName;

    /**
     * 公司信息ID
     */
    @Field(type = FieldType.Long)
    private Long companyInfoId;

    /**
     * 公司名称
     */
    @Field(type = FieldType.Keyword)
    private String supplierName;


    /**
     * 店铺ID
     */
    @Field(type = FieldType.Long)
    private Long storeId;

    /**
     * 店铺名称
     */
    @Field(type = FieldType.Keyword)
    private String storeName;

    /**
     * 商品编码
     */
    @Field(type = FieldType.Keyword)
    private String goodsNo;

    /**
     * 上下架状态
     */
    @Field(type = FieldType.Integer)
    private Integer addedFlag;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @Field(type = FieldType.Integer)
    private Integer goodsSource;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @Field(type = FieldType.Integer)
    private Integer goodsType;
    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    @Field(type = FieldType.Integer)
    private Integer cpsSpecial;

    /**
     * 主播推荐 1樊登解读,2非凡精读,3樊登直播 内容以 相隔
     */
    @ApiModelProperty(value = "主播推荐 1樊登解读,2非凡精读,3樊登直播 内容以 相隔")
    @Field(searchAnalyzer = EsConstants.PINYIN_ANALYZER, analyzer = EsConstants.PINYIN_ANALYZER, type = FieldType.Text)
    private String anchorPushs;

    /**
     * 主播推荐 1樊登解读
     */
    @Field(type = FieldType.Integer)
    private Integer fdjd = 0;
    /**
     * 商品库存
     */
    @Field(type = FieldType.Long)
    private Long stock;

    /**
     * 签约结束日期
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 模糊查询
     */
    //@Field(type = FieldType.Keyword)
    @Field(searchAnalyzer = EsConstants.DEF_ANALYZER, analyzer = EsConstants.DEF_ANALYZER, type = FieldType.Text)
    private String goodsName;


    /**
     * 所属供应商商品spuId
     */
    @Field(type = FieldType.Keyword)
    private String providerGoodsId;

    /*
     * 商品渠道 1 h5 2小程序
     */
    @Field(type = FieldType.Integer)
    private Set<Integer> goodsChannelTypeSet;
}
