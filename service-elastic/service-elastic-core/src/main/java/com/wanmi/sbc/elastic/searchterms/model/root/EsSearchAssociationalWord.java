package com.wanmi.sbc.elastic.searchterms.model.root;

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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author houshuai
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = EsConstants.SEARCH_ASSOCIATIONAL_WORD, type = EsConstants.SEARCH_ASSOCIATIONAL_WORD)
public class EsSearchAssociationalWord implements Serializable {


    private static final long serialVersionUID = 6936898177466868637L;

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String searchTerms;

    /**
     * 是否删除 0 否  1 是
     */
    @Field(type = FieldType.Keyword)
    private DeleteFlag delFlag;

    /**
     * 创建人
     */
    @Field(type = FieldType.Keyword)
    private String createPerson;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Date)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Date)
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Field(type = FieldType.Keyword)
    private String updatePerson;


    /**
     * 删除时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Date)
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @Field(type = FieldType.Keyword)
    private String deletePerson;


}