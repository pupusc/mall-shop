package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GoodsSpecDetailDTO
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午1:59
 */
@ApiModel
@Data
public class GoodsSpecDetailDTO implements Serializable {

    private static final long serialVersionUID = -6786453141635611679L;

    /**
     * 规格明细ID
     */
    @ApiModelProperty(value = "规格明细ID")
    private Long specDetailId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    /**
     * 规格ID
     */
    @ApiModelProperty(value = "规格ID")
    private Long specId;

    /**
     * 规格值明称
     */
    @ApiModelProperty(value = "规格值明称")
    private String detailName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 新增时，规格摸拟ID
     */
    @ApiModelProperty(value = "新增时，规格摸拟ID")
    private Long mockSpecId;

    /**
     * 新增时，规格值摸拟ID
     */
    @ApiModelProperty(value = "新增时，规格值摸拟ID")
    private Long mockSpecDetailId;
}
