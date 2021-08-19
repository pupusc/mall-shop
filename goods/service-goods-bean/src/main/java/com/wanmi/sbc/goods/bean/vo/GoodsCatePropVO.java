package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.vo.GoodsPropListVO
 *
 * @author lipeng
 * @dateTime 2018/11/1 下午4:30
 */
@ApiModel
@Data
public class GoodsCatePropVO implements Serializable {

    private static final long serialVersionUID = -6178619655122934361L;

    @ApiModelProperty(value = "属性id")
    private Long propId;

    @ApiModelProperty(value = "分类id")
    private Long cateId;

    @ApiModelProperty(value = "属性名")
    private String propName;

    @ApiModelProperty(value = "默认标识")
    private DefaultFlag indexFlag;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除")
    private DeleteFlag delFlag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "商品属性明细")
    private List<GoodsPropDetailVO> goodsPropDetails;

    @ApiModelProperty(value = "属性明细")
    private String propDetailStr;
}
