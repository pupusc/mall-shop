package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺分类拖拽排序请求
 * @Author: chenli
 * @Date:  2018/9/13
 * @Description: 店铺分类排序request
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCateSortDTO implements Serializable {

    private static final long serialVersionUID = 3971038638337648402L;

    /**
     * 店铺分类标识
     */
    @ApiModelProperty(value = "店铺分类标识", required = true)
    @NotNull
    private Long storeCateId;


    /**
     * 店铺分类排序顺序
     */
    @ApiModelProperty(value = "店铺分类排序顺序", required = true)
    @NotNull
    private Integer cateSort;
}
