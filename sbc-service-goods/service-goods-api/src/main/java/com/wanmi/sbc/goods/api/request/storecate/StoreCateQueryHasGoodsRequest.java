package com.wanmi.sbc.goods.api.request.storecate;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:22
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCateQueryHasGoodsRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 2568679058279462936L;

    /**
     * 店铺分类标识
     */
    @ApiModelProperty(value = "店铺分类标识")
    private Long storeCateId;

    /**
     * 批量店铺分类标识
     */
    @ApiModelProperty(value = "批量店铺分类标识")
    private List<Long> storeCateIds;

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
     * 模糊查询，分类路径
     */
    @ApiModelProperty(value = "模糊查询，分类路径")
    private String likeCatePath;
}
