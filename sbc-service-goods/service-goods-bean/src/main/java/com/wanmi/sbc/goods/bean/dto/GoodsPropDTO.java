package com.wanmi.sbc.goods.bean.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
public class GoodsPropDTO implements Serializable {

    private static final long serialVersionUID = -4171911792203319182L;

    @ApiModelProperty(value = "属性Id")
    private Long propId;

    @ApiModelProperty(value = "分类Id")
    private Long cateId;

    @ApiModelProperty(value = "属性名")
    private String propName;

    @ApiModelProperty(value = "是否开启索引")
    @Enumerated
    private DefaultFlag indexFlag;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除标记")
    @Enumerated
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "商品属性详情")
    private List<GoodsPropDetailDTO> goodsPropDetails;

    @ApiModelProperty(value = "商品属性详情字符串")
    private String propDetailStr;
}
