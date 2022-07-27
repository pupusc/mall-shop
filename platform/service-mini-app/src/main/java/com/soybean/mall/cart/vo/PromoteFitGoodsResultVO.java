package com.soybean.mall.cart.vo;

import com.soybean.mall.goods.response.SpuNewBookListResp;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class PromoteFitGoodsResultVO extends SpuNewBookListResp {
    /**
     * 规格名称
     */
    private String specText;

    /**
     * 选购数量
     */
    private Integer buyCount;
//    /**
//     * 最大数量
//     */
//    private Integer maxCount;
}
