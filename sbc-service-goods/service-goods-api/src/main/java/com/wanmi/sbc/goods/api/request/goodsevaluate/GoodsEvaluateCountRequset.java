package com.wanmi.sbc.goods.api.request.goodsevaluate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 商品评价总数
 * @create: 2019-04-04 16:22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateCountRequset implements Serializable {
    private static final long serialVersionUID = 8934906968152032193L;

    //商品ID
    private String goodsId;

    //是否晒单 0：否，1：是
//    private int isUpload;
}