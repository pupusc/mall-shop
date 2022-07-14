package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 19:39:00
 */
@Data
public class PromoteGoodsResultVO {
    /**
     * 索引SKU
     */
    private List<PromoteFitGoodsResultVO> fitGoods = new ArrayList<>();
    /**
     * 活动信息
     */
    private PromoteInfo promoteInfo;
    /**
     * 价格信息
     */
    private PurchasePriceResultVO calcPrice;
    /**
     *  条目总数
     */
    private Long total;

    @Data
    public static class PromoteInfo {
        /**
         * 促销名称
         */
        private String name;
        /**
         * 开始时间
         */
        private String startTime;
        /**
         * 结束时间
         */
        private String endTime;
        /**
         * 提示文案
         */
        private String tipText;
    }
}
