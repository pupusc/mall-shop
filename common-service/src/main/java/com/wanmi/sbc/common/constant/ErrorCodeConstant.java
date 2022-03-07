package com.wanmi.sbc.common.constant;

/**
 * @ClassName ErrorCodeConstant
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/6/15 14:39
 **/
public final class ErrorCodeConstant {

    private ErrorCodeConstant(){

    }

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购活动还未开始
     */
    public static final String FLASH_SALE_ACTIVITY_NOT_START = "K-140001";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购商品数量超过最大抢购数量
     */
    public static final String FLASH_SALE_GOODS_HAVE_PANIC_BUYING_NUM_MORE_THAN_MAX_NUM = "K-140002";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购商品数量小于最小起购数
     */
    public static final String FLASH_SALE_GOODS_PANIC_BUYING_NUM_LESS_THAN_MIN_NUM = "K-140003";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购商品数量小于最小起购数
     */
    public static final String FLASH_SALE_GOODS_PANIC_BUYING_NUM_MORE_THAN_STOCK = "K-140004";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购活动不存在
     */
    public static final String FLASH_SALE_ACTIVITY_NOT_PRESENCE = "K-140005";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购活动已结束
     */
    public static final String FLASH_SALE_ACTIVITY_END = "K-140006";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购活动抢购商品失败
     */
    public static final String FLASH_SALE_PANIC_BUYING_FAIL = "K-140007";

    /**
     * 秒杀+积分兑换优惠券迭代---秒杀商品抢购活动抢购，已加入抢购未提交订单，再次抢购，提示"抢购业务繁忙，稍后再试"
     */
    public static final String FLASH_SALE_PANIC_BUYING_BUSY = "K-140009";

    /**
     * 支付回调业务改造---支付回调结果已存在
     */
    public static final String PAY_CALL_BACK_RESULT_EXIT = "K-059999";
}
