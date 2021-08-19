package com.wanmi.sbc.goods.bean.vo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel
@Data
public class GoodsPropDetailVO implements Serializable {

    private static final long serialVersionUID = 2638835271465760660L;

    @ApiModelProperty(value = "详情id")
    private Long detailId;

    @ApiModelProperty(value = "属性id")
    private Long propId;

    @ApiModelProperty(value = "详情名")
    private String detailName;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除", notes = "0: 否, 1: 是")
    @Enumerated
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
