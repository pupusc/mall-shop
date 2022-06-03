package com.soybean.elastic.booklistmodel.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.soybean.elastic.booklistmodel.model.sub.EsBookListSubSpuNew;
import com.soybean.elastic.collect.constant.ConstantUtil;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 书单对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:36 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Document(indexName = "es_book_list_model", type = "es_book_list_model")
public class EsBookListModel {

    @Id
    @Field(type = FieldType.Long)
    private Long bookListId;

    /**
     * 书单类型 1 排行榜 2 书单
     */
    @Field(type = FieldType.Integer)
    private Integer bookListBusinessType;;

    /**
     * ik_max_word 会对文本做最细 力度的拆分
     * ik_smart：会对文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String bookListName;

    /**
     * 简介
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String bookListDesc;

    /**
     * 是否置顶 0否 1 是
     */
    @Field(type = FieldType.Integer)
    private Integer hasTop;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    @Field(type = FieldType.Integer)
    private Integer publishState;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;


    /**
     * 索引时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime indexTime;

    /**
     * 删除 已删除：1，未删除：0
     */
    @Field(type = FieldType.Integer)
    private Integer delFlag;

    /**
     * 商品总数量
     */
    @Field(type = FieldType.Integer)
    private Integer spuNum;

    /**
     * 商品信息
     */
    @Field(type = FieldType.Nested)
    private List<EsBookListSubSpuNew> spus;
}
