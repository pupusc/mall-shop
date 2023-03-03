package com.soybean.elastic.spu.model.sub;

import com.soybean.elastic.collect.constant.ConstantUtil;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class SubSpuLabelNew {
    @Field(type = FieldType.Text)
    private String showName;

    @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Integer)
    private Integer orderType;

    @Field(type = FieldType.Integer)
    private Integer type = 1;
}
