package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 签约分类拖拽排序请求
 * Created by chenli on 2018/9/13.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCateSortDTO implements Serializable {

    private static final long serialVersionUID = 3313179843927882868L;

    /**
     * 商品分类标识
     */
    @ApiModelProperty(value = "商品分类标识", required = true)
    @NotNull
    private Long cateId;


    /**
     * 商品分类排序顺序
     */
    @ApiModelProperty(value = "商品分类排序顺序", required = true)
    @NotNull
    private Integer cateSort;
}
