package com.wanmi.sbc.elastic.searchterms.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author houshuai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsAssociationLongTailWord implements Serializable {

    private static final long serialVersionUID = -7415692901579678977L;
    /**
     * 主键id
     */
    @Field(type = FieldType.Keyword)
    private Long associationLongTailWordId;

    /**
     * 联想词
     */
    @Field(type = FieldType.Keyword)
    private String associationalWord;

    /**
     * 长尾词
     */
    @Field(type = FieldType.Keyword)
    private String longTailWord;

    /**
     * 关联搜索词id
     */
    @Field(type = FieldType.Keyword)
    private Long searchAssociationalWordId;

    /**
     * 排序号
     */
    @Field(type = FieldType.Keyword)
    private Long sortNumber;


}