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
 * 商品图片实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsImageVO implements Serializable {

    private static final long serialVersionUID = 5286167225209101397L;

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
     * 图片排序字段
     */
    @ApiModelProperty(value = "图片排序字段")
    private Integer sort;

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
    @ApiModelProperty(value = "删除标记")
    @Enumerated
    private DeleteFlag delFlag;

}
