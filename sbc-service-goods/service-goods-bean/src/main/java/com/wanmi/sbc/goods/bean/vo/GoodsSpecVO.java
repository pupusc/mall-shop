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
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品规格实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsSpecVO implements Serializable {

    private static final long serialVersionUID = -2989422675878672487L;

    /**
     * 规格ID
     */
    @ApiModelProperty(value = "规格ID")
    private Long specId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    /**
     * 规格名称
     */
    @ApiModelProperty(value = "规格名称")
    private String specName;

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

    /**
     * 新增时，模拟规格ID
     */
    @ApiModelProperty(value = "新增时，模拟规格ID")
    @Transient
    private Long mockSpecId;

    /**
     * 多个规格值ID
     * 查询时，遍平化响应
     */
    @ApiModelProperty(value = "多个规格值ID", notes = "查询时，遍平化响应")
    @Transient
    private List<Long> specDetailIds;

}
