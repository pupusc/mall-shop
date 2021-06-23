package com.wanmi.sbc.goods.bean.vo;

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
 * GoodsInfoSpecDetailRelVO
 *
 * @author lipeng
 * @dateTime 2018/11/9 下午2:36
 */
@ApiModel
@Data
public class GoodsInfoSpecDetailRelVO implements Serializable {

    private static final long serialVersionUID = -1500902715149038145L;

    /**
     * SKU与规格值关联ID
     */
    @ApiModelProperty(value = "SKU与规格值关联ID")
    private Long specDetailRelId;

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
     * 规格值ID
     */
    @ApiModelProperty(value = "规格值ID")
    private Long specDetailId;

    /**
     * 规格ID
     */
    @ApiModelProperty(value = "规格ID")
    private Long specId;

    /**
     * 规格值自定义名称
     * 分词搜索
     */
    @ApiModelProperty(value = "规格值自定义名称", notes = "分词搜索")
    private String detailName;

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
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 新增商品时，模拟规格ID
     * 表明与SKU的关系
     */
    @ApiModelProperty(value = "新增商品时，模拟规格ID", notes = "表明与SKU的关系")
    private Long mockSpecId;

    /**
     * 新增商品时，模拟规格值ID
     * 表明与SKU的关系
     */
    @ApiModelProperty(value = "新增商品时，模拟规格值ID", notes = "表明与SKU的关系")
    private Long mockSpecDetailId;

    /**
     * 规格项名称
     * 用于存储ES结构
     */
    @ApiModelProperty(value = "规格项名称", notes = "用于存储ES结构")
    private String specName;

    /**
     * 规格项值
     * 用于存储ES结构，主要解决ES的聚合结果不以分词显示
     */
    @ApiModelProperty(value = "规格项值", notes = "用于存储ES结构，主要解决ES的聚合结果不以分词显示")
    private String allDetailName;
}
