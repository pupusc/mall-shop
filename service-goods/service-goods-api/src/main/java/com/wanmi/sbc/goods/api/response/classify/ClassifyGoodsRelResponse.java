package com.wanmi.sbc.goods.api.response.classify;

import lombok.Data;

import java.util.List;

@Data
public class ClassifyGoodsRelResponse {

    /**
     * 商品id
     */
    private String goodsId;

    private List<ClassifySimpleProviderResponse> classifies;
}
