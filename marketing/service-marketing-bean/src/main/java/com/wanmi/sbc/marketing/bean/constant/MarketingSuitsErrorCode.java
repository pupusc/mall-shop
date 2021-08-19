package com.wanmi.sbc.marketing.bean.constant;

/**
 * <p>组合购异常码定义</p>
 */
public final class MarketingSuitsErrorCode {

    private MarketingSuitsErrorCode(){}

    /**
     * 最少选择2个商品
     */
    public final static String MARKETING_SUIT_GOODS_NUM_MIN_2 = "K-081001";

    /**
     * 最多选择20个商品
     */
    public final static String MARKETING_SUIT_GOODS_NUM_MAX_20 = "K-081002";

    /**
     * 商品折扣价不可大于单价
     */
    public final static String MARKETING_SUIT_GOODS_DISCOUNT_PRICE = "K-081003";

    /**
     * 商品不存在
     */
    public final static String MARKETING_SUIT_GOODS_EXIST = "K-081004";

    /**
     * 当前组合购活动已存在，请修改活动时间或活动商品
     */
    public final static String MARKETING_IS_EXIST = "K-081006";





}
