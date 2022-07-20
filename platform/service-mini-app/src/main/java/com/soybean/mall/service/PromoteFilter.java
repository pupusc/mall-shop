package com.soybean.mall.service;

import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;

/**
 * @author Liang Jun
 * @desc 小程序促销活动过滤
 * @date 2022-07-08 15:07:00
 */
public class PromoteFilter {
    /**
     * 小程序是否支持该促销活动
     */
    public static boolean supportMkt(MarketingSubType subType) {
        return MarketingSubType.REDUCTION_FULL_AMOUNT.equals(subType)
                || MarketingSubType.REDUCTION_FULL_COUNT.equals(subType)
                || MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(subType)
                || MarketingSubType.DISCOUNT_FULL_COUNT.equals(subType);
    }
}
