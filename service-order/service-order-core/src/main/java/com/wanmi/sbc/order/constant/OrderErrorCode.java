package com.wanmi.sbc.order.constant;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:49 2019/5/25
 * @Description: 订单服务错误码
 */
public final class OrderErrorCode {


    /**
     * 拼团单校验--商品未关联进行中的拼团活动
     */
    public final static String ORDER_MARKETING_GROUPON_NO_ACTIVITY = "K-050401";

    /**
     * 拼团单校验--当前已开团，不可再开团
     */
    public final static String ORDER_MARKETING_GROUPON_WAIT = "K-050402";

    /**
     * 拼团单校验--当前已参同一团活动，不可再参团
     */
    public final static String ORDER_MARKETING_GROUPON_ALREADY_JOINED = "K-050403";

    /**
     * 拼团单校验--已成团/团已作废，不可参团
     */
    public final static String ORDER_MARKETING_GROUPON_ALREADY_END = "K-050404";

    /**
     * 拼团单校验--未达到拼团商品起售数
     */
    public final static String ORDER_MARKETING_GROUPON_START_NUM = "K-050405";

    /**
     * 拼团单校验--已超过拼团商品限购数
     */
    public final static String ORDER_MARKETING_GROUPON_LIMIT_NUM = "K-050406";

    /**
     * 拼团单校验--拼团订单不存在
     */
    public final static String ORDER_MARKETING_GROUPON_ORDER_NOT_FUND = "K-050409";

    /**
     * 添加购物车校验-该商品不支持加入购物车
     */
    public final static String PURCHASE_GOODS_NOT_ALLOWED = "K-050450";

    /**
     * 商品活动发生变更
     */
    public final static String MARKETING_CHANGE = "K-050451";

    public final static String COUPON_SUIT_NOT_ALLOWED ="K-050410";


}
