package com.soybean.elastic.spu.model.sub;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.soybean.elastic.spu.constant.ConstantUtil;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: sku商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:36 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubEsSku {

    @Field(type = FieldType.Keyword)
    private String skuId;

    /**
     * ik_max_word 会对文本做最细 力度的拆分
     * ik_smart：会对文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String skuName;


    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String skuSubName;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Long)
    private Long commentNum;

    /**
     * 商品来源 number【博库 管易 平台】
     */
    @Field(type = FieldType.Long)
    private Long providerId;


    /**
     * 好评数
     */
    @Field(type = FieldType.Long)
    private Long favorCommentNum;


    /**
     * 上下架状态 0 未上架 1 已上架
     */
    @Field(type = FieldType.Long)
    private Integer addedFlag;


    /**
     * 0未删除 1已删除
     */
    @Field(type = FieldType.Long)
    private Integer delFlag;


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
     * 图书信息
     */
    @Field(type = FieldType.Nested)
    private SubBookNew book;

    /**
     * 活动信息
     */
    @Field(type = FieldType.Nested)
    private List<SubActivityNew> activitys;

}
