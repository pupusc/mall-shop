package com.wanmi.sbc.elastic.goods.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@ApiModel
@Data
public class GoodsLabelNest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "标签id")
    private Long goodsLabelId;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    @Field(searchAnalyzer = EsConstants.DEF_ANALYZER, analyzer = EsConstants.DEF_ANALYZER, type = FieldType.Text)
    private String labelName;

    /**
     * 排序规则
     */
    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "排序规则")
    private Integer labelSort;

    /**
     * 前端是否展示 0: 关闭 1:开启
     */
    @Field(type = FieldType.Boolean)
    @ApiModelProperty(value = "前端是否展示 0: 关闭 1:开启")
    private Boolean labelVisible;

    /**
     * 删除标识 0:未删除1:已删除
     */
    @ApiModelProperty(value = "删除标识 0:未删除1:已删除")
    private DeleteFlag delFlag;
}
