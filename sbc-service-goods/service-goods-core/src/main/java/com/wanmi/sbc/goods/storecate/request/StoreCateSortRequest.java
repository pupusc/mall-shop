package com.wanmi.sbc.goods.storecate.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: chenli
 * @Date:  2018/9/13
 * @Description: 店铺分类排序request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCateSortRequest {

    /**
     * 店铺分类标识
     */
    @NotNull
    private Long storeCateId;


    /**
     * 店铺分类排序顺序
     */
    @NotNull
    private Integer cateSort;
}
