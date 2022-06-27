package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
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
    private CalcPriceSum calcPrice;

    @Data
    private static class PromoteInfo {
        /**
         * 开始时间
         */
        private Date startTime;
        /**
         * 结束时间
         */
        private Date endTime;
        /**
         * 提示文案
         */
        private String tipText;
    }
}
