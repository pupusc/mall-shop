package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ${DESCRIPTION}
 *
 * @auther ruilinxin
 * @create 2018/03/20 10:04
 */
@ApiModel
@Data
public class GoodsPropDetailRelVO  implements Serializable {

    private static final long serialVersionUID = -7826451740574928116L;

    /**
     * 编号，
     */
    @ApiModelProperty(value = "编号")
    private Long relId;

    /**
     * SPU标识
     */
    @ApiModelProperty(value = "SPU标识")
    private String goodsId;

    /**
     * 属性值id
     */
    @ApiModelProperty(value = "属性值id")
    private Long detailId;

    /**
     *属性值（文本框输入）
     */
    @Column(name = "prop_value")
    private String propValue;

    /**
     * 属性id
     */
    @ApiModelProperty(value = "属性id")
    private Long propId;

    /**
     * 属性名字
     */
    @ApiModelProperty(value = "属性名字")
    private String propName;

    /**
     * 属性类型
     */
    @ApiModelProperty(value = "属性类型")
    private Integer propType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    @Enumerated
    private DeleteFlag delFlag;
}
