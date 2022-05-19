package com.soybean.elastic.goods.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.soybean.elastic.goods.model.sub.SubBookAttrNew;
import com.soybean.elastic.goods.model.sub.SubClassifyNew;
import com.soybean.elastic.goods.model.sub.SubEsBookListModelNew;
import com.soybean.elastic.goods.model.sub.SubEsGoodsInfoNew;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 新的es 商品搜索引擎
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:12 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
@Document(indexName = "es_goods_new", type = "es_goods_new")
public class EsGoodsNew {

    /**
     * id
     */
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String spuId;


    /**
     * ik_max_word 会对文本做最细 力度的拆分
     * ik_smart：会对文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String goodsName;

    /**
     * 商品分类 1 图书 2 商品
     */
    @Field(type = FieldType.Integer)
    private Integer goodsCategory;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Long)
    private Long commentNum;

    /**
     * 好评数
     */
    @Field(type = FieldType.Long)
    private Long favorableCommentNum;

    /**
     * 销量
     */
    @Field(type = FieldType.Long)
    private Long salesNum;

    /**
     * 销售价格
     */
    @Field(type = FieldType.Double)
    private BigDecimal salesPrice;


    /**
     * 上下架状态 0 未上架 1 已上架 2 部分上架
     */
    @Field(type = FieldType.Integer)
    private Integer addedFlag;

    /**
     * 上架时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 索引时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime indexTime;

    /**
     * 0未删除 1已删除
     */
    @Field(type = FieldType.Integer)
    private Integer deleteFlag;


    /**
     * sku列表
     */
    @Field(type = FieldType.Nested)
    private List<SubEsGoodsInfoNew> goodsInfos;


    /**
     * 书单
     */
    @Field(type = FieldType.Nested)
    private List<SubEsBookListModelNew> bookListModels;


    /**
     * 榜单
     */
    @Field(type = FieldType.Nested)
    private List<SubEsBookListModelNew> rankingListModels;

    /**
     * 店铺分类
     */
    @Field(type = FieldType.Nested)
    private List<SubClassifyNew> classifys;

    /**
     * 图书属性
     */
    @Field(type = FieldType.Object)
    private SubBookAttrNew bookAttrs;
}
