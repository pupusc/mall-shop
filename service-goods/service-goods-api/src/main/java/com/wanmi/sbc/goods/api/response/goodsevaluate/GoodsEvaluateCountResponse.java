package com.wanmi.sbc.goods.api.response.goodsevaluate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 店铺评价统计
 * @create: 2019-04-04 16:25
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateCountResponse implements Serializable {
    private static final long serialVersionUID = 2206855635096754315L;

    //评价总数
    private Long evaluateConut;

    //晒单总数
    private Long postOrderCount;

    //好评率
    private String praise;

    /**
     *goodsId
     */
    private String goodsId;
}