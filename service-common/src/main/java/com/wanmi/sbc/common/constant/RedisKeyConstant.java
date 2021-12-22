package com.wanmi.sbc.common.constant;

/**
 * @ClassName RedisKeyConstant
 * @Description redis key对应常量
 * @Author lvzhenwei
 * @Date 2019/6/15 14:15
 **/
public final class RedisKeyConstant {

    private RedisKeyConstant() {

    }

    /**
     * 秒杀+积分兑换优惠券迭代---商品秒杀抢购商品信息对应key前缀
     */
    public static final String FLASH_SALE_GOODS_INFO = "flashSaleGoodsInfo:";

    /**
     * 上一次第三方库存缓存
     */
    public static final String GOODS_INFO_LAST_STOCK_PREFIX = "GOODS_INFO_LAST_STOCK_";

    /**
     * 秒杀+积分兑换优惠券迭代---商品秒杀抢购资格对应key前缀
     */
    public static final String FLASH_SALE_GOODS_QUALIFICATIONS = "flashSaleGoodsQualifications:";

    /**
     * 抢购---商品抢购商品资格生成失败对应key前缀
     */
    public static final String FLASH_SALE_GOODS_QUALIFICATIONS_FAILED = "flashSaleGoodsQualificationsFailed:";

    /**
     * 秒杀+积分兑换优惠券迭代---商品秒杀抢购已抢购商品数量对应key前缀
     */
    public static final String FLASH_SALE_GOODS_HAVE_PANIC_BUYING = "flashSaleGoodsHavePanicBuying:";

    /**
     * 秒杀+积分兑换优惠券迭代---商品秒杀抢购商品库存对应key前缀
     */
    public static final String FLASH_SALE_GOODS_INFO_STOCK = "flashSaleGoodsInfoStock:";


    //订单快照加锁
    public static final String CUSTOMER_TRADE_SNAPSHOT_LOCK_KEY = "customer:trade:snapshot:lock:";

    //打印单配置
    public static final String STORE_PRINT_SETTING_KEY = "store:printSetting:";

    public static final String TRADE_COUPON_SNAPSHOT = "trade:coupon:snapshot:";

    public static final String TRADE_ITME_SNAPSHOT = "trade:item:snapshot:";

    /**
     * 预约---商品预约数量对应key前缀
     */
    public static final String APPOINTMENT_SALE_GOODS_INFO_COUNT = "appointmentSaleGoodsInfoCount:";

    /**
     * 抢购---商品抢购库存对应key前缀
     */
    public static final String RUSH_SALE_GOODS_INFO_COUNT = "rushSaleGoodsInfoStock:";

    /**
     * 抢购---商品抢购商品信息对应key前缀
     */
    public static final String APPOINTMENT_SALE_GOODS_INFO = "appointmentSaleGoodsInfo:";

    /**
     * 抢购---商品抢购商品资格对应key前缀
     */
    public static final String APPOINTMENT_SALE_GOODS_QUALIFICATIONS = "appointmentSaleGoodsQualifications:";

    /**
     * 库存在redis中key的前缀，后缀为skuId
     */
    public static final String GOODS_INFO_STOCK_PREFIX = "GOODS_INFO_STOCK_";

    public static final String STOCK_SYNC_TIME_PREFIX = "STOCK_SYNC_TIME_";

    /**
     * LinkedMall---验证支付并自动退款， 后缀为订单id
     */
    public static final String LINKED_MALL_CHECKED_PEY_AUTO_REFUND = "linkedMallCheckPayStatus";

    /**
     * LinkedMall---限制重复消费
     */
    public static final String LINKED_MALL_MQ_REPEATED = "linkedMallMqRepeated:";

    public static final String GOODS_DETAIL_CACHE = "goodsdetail:";

    /**
     * 二维码缓存
     */
    public static final String QR_CODE_CACHE = "qrCode_image_cache";

    /**
     * 虚拟卡券券码--校验券码存在
     * virtual_coupon_code:coupon-no-check:{couponId}
     */
    public static final String VIRTUAL_COUPON_NO_CHECK_KEY= "virtual_coupon_code:coupon-no-check:";

    public static final  String ACCESS_TOKEN_KEY="access:token";

    public static final String GOODS_INFO_LAST_STOCK_PREFIX = "GOODS_INFO_LAST_STOCK_";
}
