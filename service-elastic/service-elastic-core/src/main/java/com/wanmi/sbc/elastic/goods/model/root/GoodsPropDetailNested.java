package com.wanmi.sbc.elastic.goods.model.root;

import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@ApiModel
public class GoodsPropDetailNested implements Serializable {

    @ApiModelProperty(value = "属性id")
    private Long PropId;

    @ApiModelProperty(value = "属性名字")
    private String propName;

    @Field(searchAnalyzer = EsConstants.DEF_ANALYZER, analyzer = EsConstants.DEF_ANALYZER, type = FieldType.Text)
    @ApiModelProperty(value = "属性值")
    private String propValue;
}
