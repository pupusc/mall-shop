package com.wanmi.sbc.goods.api.constant;

/**
 * <p>限售商品的返回信息</p>
 * Created by of628-wenzhi on 2018-06-21-下午4:29.
 */
public final class GoodsRestrictedErrorTips {
    private GoodsRestrictedErrorTips() {
    }

    /**
     * 最少起订量为x件
     */
    public final static String GOODS_PURCHASE_MIN_NUMBER="最少起订量为%d件";

    /**
     * 最多可购买x件
     */
    public final static String GOODS_PURCHASE_MOST_NUMBER="最多可购买%d件";


    /**
     * 您没有购买该商品权限
     */
    public final static String WITHOUT_GOODS_AUTHORITY="您没有购买该商品权限";

    /**
     * 您没有购买【%s】商品权限
     */
    public final static String WITHOUT_GOODS_INFO_AUTHORITY="您没有购买【%s】商品权限";


    /**
     * 最少起订量为x件 —— 购物车指定商品
     */
    public final static String GOODS_PURCHASE_MIN_NUMBER_PURCHASE="%s最少起订量为%d件";

    /**
     * 最多可购买x件 —— 购物车指定商品
     */
    public final static String GOODS_PURCHASE_MOST_NUMBER_PURCHASE="%s最多可购买%d件";
}

