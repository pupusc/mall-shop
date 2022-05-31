package com.soybean.elastic.spu.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.soybean.elastic.spu.constant.ConstantUtil;
import com.soybean.elastic.spu.model.sub.SubBookNew;
import com.soybean.elastic.spu.model.sub.SubClassifyNew;
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
//@Document(indexName = "es_goods_new", type = "es_goods_new")
@Document(indexName = "es_book_list_model")
public class EsSpuNew {


    @Id
    @Field(type = FieldType.Keyword)
    private String spuId;


    /**
     * ik_max_word 会对文本做最细 力度的拆分
     * ik_smart：会对文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String spuName;


    /**
     * 商品副标题
     * ik_max_word 会对文本做最细 力度的拆分
     * ik_smart：会对文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String spuSubName;

    /**
     * 商品分类 1 图书 2 商品
     */
    @Field(type = FieldType.Long)
    private Integer spuCategory;

    /**
     * 商品渠道
     */
    @Field(type = FieldType.Long)
    private List<Integer> spuChannels;

    /**
     * 主播推荐列表 1、樊登讲书 2非凡精读 3樊登直播 4李蕾慢读
     */
    @Field(type = FieldType.Long)
    private List<Integer> anchorRecoms;

//    /**
//     * 审核状态 0: 未审核1 审核通过2审核失败3禁用中
//     */
//    @Field(type = FieldType.Long)
//    private Integer spuAuditType;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Long)
    private Long commentNum;

    /**
     * 好评数
     */
    @Field(type = FieldType.Long)
    private Long favorCommentNum;

    /**
     * 销量
     */
    @Field(type = FieldType.Long)
    private Long salesNum;

    /**
     * 销售价格(最小sku价格)
     */
    @Field(type = FieldType.Double)
    private BigDecimal salesPrice;


    /**
     * 上下架状态 0 未上架 1 已上架 2 部分上架
     */
    @Field(type = FieldType.Long)
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
    @Field(type = FieldType.Long)
    private Integer delFlag;

    @Field(type = FieldType.Nested)
    private SubBookNew book;


    /**
     * 店铺分类
     */
    @Field(type = FieldType.Object)
    private List<SubClassifyNew> classifys;


}
