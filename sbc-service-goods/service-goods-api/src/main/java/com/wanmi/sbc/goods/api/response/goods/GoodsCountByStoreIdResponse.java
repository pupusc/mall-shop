package com.wanmi.sbc.goods.api.response.goods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 店铺商品总数
 * @create: 2019-04-03 10:42
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCountByStoreIdResponse implements Serializable {

    private static final long serialVersionUID = 2192849205098090535L;

    //商品总数
    private long goodsCount;
}