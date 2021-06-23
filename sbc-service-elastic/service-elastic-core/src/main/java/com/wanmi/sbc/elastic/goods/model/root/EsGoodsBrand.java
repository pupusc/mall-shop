package com.wanmi.sbc.elastic.goods.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = EsConstants.ES_GOODS_BRAND, type = EsConstants.ES_GOODS_BRAND)
public class EsGoodsBrand implements Serializable {

    /**
     * 品牌编号
     */
    @Id
    private Long brandId;

    /**
     * 品牌名称
     */
    @Field(type = FieldType.Keyword)
    private String brandName;

    /**
     * 拼音
     */
    @Field(type = FieldType.Keyword)
    private String pinYin;

    /**
     * 简拼
     */
    @Field(type = FieldType.Keyword)
    private String sPinYin;

    /**
     * 店铺id(平台默认为0)
     */
    @Field(type = FieldType.Keyword)
    private Long storeId = 0L;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @Field(type = FieldType.Keyword)
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 品牌别名
     */
    @Field(type = FieldType.Keyword)
    private String nickName;


    /**
     * 品牌logo
     */
    @Field(type = FieldType.Keyword)
    private String logo;
}