package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * GoodsInfoSpecDetailRelVO
 *
 * @author lipeng
 * @dateTime 2018/11/9 下午2:36
 */
@ApiModel
@Data
public class GoodsInfoSpecDetailSimpleVO implements Serializable {

    private static final long serialVersionUID = -1500902715149038145L;

    /**
     * SKU与规格值关联ID
     */
    @ApiModelProperty(value = "SKU与规格值关联ID")
    private Long specDetailRelId;

    /**
     * 规格值自定义名称
     * 分词搜索
     */
    @ApiModelProperty(value = "规格值自定义名称", notes = "分词搜索")
    private String detailName;
}
