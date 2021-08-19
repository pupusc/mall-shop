package com.wanmi.sbc.goods.api.request.goods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 统计店铺商品总数
 * @create: 2019-04-03 10:38
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCountByStoreIdRequest implements Serializable {

    private static final long serialVersionUID = -4120755635749674465L;

    //店铺id
    private long storeId;

}