package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 18:35:00
 */
@Data
public class PurchaseInfoParamVO {
    /**
     * 指定促销
     */
    private List<Marketing> marketings = new ArrayList<>();


    @Data
    public static class Marketing {
        /**
         * 促销id
         */
        private Long marketingId;
        /**
         * 商品信息
         */
        private List<GoodsInfo> goodsInfos;
    }

    @Data
    public static class GoodsInfo {
        /**
         * spuId
         */
        private String goodsId;
        /**
         * skuId
         */
        private String goodsInfoId;
        /**
         * 是否选中
         */
        private boolean checked;
    }
}
