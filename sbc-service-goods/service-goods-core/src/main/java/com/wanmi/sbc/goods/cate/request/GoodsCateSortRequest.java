package com.wanmi.sbc.goods.cate.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 签约分类拖拽排序请求
 * Created by chenli on 2018/9/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCateSortRequest implements Serializable {

    private static final long serialVersionUID = 3313179843927882868L;

    /**
     * 商品分类标识
     */
    @NotNull
    private Long cateId;


    /**
     * 商品分类排序顺序
     */
    @NotNull
    private Integer cateSort;
}
