package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:25
 */
@ApiModel
@Data
public class StoreCateResponseVO implements Serializable {

    /**
     * 店铺分类标识
     */
    @ApiModelProperty(value = "店铺分类标识")
    private Long storeCateId;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 店铺分类名称
     */
    @ApiModelProperty(value = "店铺分类名称")
    private String cateName;

    /**
     * 父分类标识
     */
    @ApiModelProperty(value = "父分类标识")
    private Long cateParentId;

    /**
     * 分类层次
     */
    @ApiModelProperty(value = "分类层次")
    private Integer cateGrade;

    /**
     * 分类路径
     */
    @ApiModelProperty(value = "分类路径")
    private String catePath;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 默认标记
     */
    @ApiModelProperty(value = "默认标记", notes = "0: 否, 1: 是")
    private DefaultFlag isDefault;

    /**
     * 一对多关系，子分类
     */
    @ApiModelProperty(value = "一对多关系，子分类")
    private List<StoreCateVO> storeCateList;
}
