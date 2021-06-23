package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>商品库图片 dto</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardImageDTO implements Serializable {

    private static final long serialVersionUID = 9218621286950621197L;

    /**
     * 图片编号
     */
    @ApiModelProperty(value = "图片编号")
    private Long imageId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * 原图路径
     */
    @ApiModelProperty(value = "原图路径")
    private String artworkUrl;

    /**
     * 中图路径
     */
    @ApiModelProperty(value = "中图路径")
    private String middleUrl;

    /**
     * 小图路径
     */
    @ApiModelProperty(value = "小图路径")
    private String thumbUrl;

    /**
     * 大图路径
     */
    @ApiModelProperty(value = "大图路径")
    private String bigUrl;

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
