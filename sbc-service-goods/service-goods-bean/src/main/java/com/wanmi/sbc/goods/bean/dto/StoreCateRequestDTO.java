package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类实体类
 * Created by bail on 2017/11/13.
 */
@ApiModel
@Data
public class StoreCateRequestDTO implements Serializable {

    private static final long serialVersionUID = 5863317607969619866L;

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
     * 分类图片
     */
    @ApiModelProperty(value = "分类图片")
    private String cateImg;

    /**
     * 分类路径
     */
    @ApiModelProperty(value = "分类路径")
    private String catePath;

    /**
     * 分类层次
     */
    @ApiModelProperty(value = "分类层次")
    private Integer cateGrade;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 默认标记
     */
    @ApiModelProperty(value = "默认标记", notes = "0: 否, 1: 是")
    private DefaultFlag isDefault;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 排序的集合
     */
    @ApiModelProperty(value = "排序的集合")
    private List<StoreCateDTO> storeCateList = new ArrayList<>();
}

