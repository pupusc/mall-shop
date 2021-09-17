package com.wanmi.sbc.elastic.goods.model.root;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@ApiModel
public class GoodsExtProps {

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "出版社")
    private String publisher;

    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "价格")
    private Double price;

    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "评分")
    private Double score;
}
