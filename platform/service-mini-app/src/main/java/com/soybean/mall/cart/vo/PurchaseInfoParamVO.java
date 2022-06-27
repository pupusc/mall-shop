package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 18:35:00
 */
@Data
public class PurchaseInfoParamVO {
    /**
     * 商品id
     */
    private List<String> tickSkuIds;
    /**
     * 指定规格
     */
    private List<PickSpec> pickSpecs;
    /**
     * 指定促销
     */
    private List<PickMarketing> pickMarketings;

    @Data
    private static class PickSpec {
        /**
         * 商品id
         */
        private String skuId;
        /**
         * 规格id
         */
        private Long specId;
    }

    @Data
    private static class PickMarketing {
        /**
         * 商品id
         */
        private String skuId;
        /**
         * 促销id
         */
        private String marketingId;
    }
}
