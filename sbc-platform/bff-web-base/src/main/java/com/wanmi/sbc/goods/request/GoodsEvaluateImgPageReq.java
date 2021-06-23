package com.wanmi.sbc.goods.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sbc-micro-service
 * @description: 商品详情晒图查询
 * @create: 2019-05-14 11:50
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsEvaluateImgPageReq extends BaseQueryRequest {

    //spuID
    private String goodsId;
}