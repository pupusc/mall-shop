package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 18:35:00
 */
@Data
public class PurchasePriceParamVO {
    /**
     * 指定促销
     */
    private List<PickMarketing> marketings;

    @Data
    private static class PickMarketing {
        /**
         * 促销id
         */
        private String marketingId;
        /**
         * 商品
         */
        private List<GoodsInfo> goodsInfos;
    }
    @Data
    private static class GoodsInfo {
        /**
         * skuId
         */
        private String goodsId;
        /**
         * 数量
         */
        private int count;
    }
}
