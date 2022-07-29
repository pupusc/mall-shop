package com.wanmi.sbc.goods.api.constant;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/23 5:49 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class RedisKeyConstant {

    /**
     * 新上列表 黑名单KEY
     */
    public static final String KEY_NEW_BOOKS_BLACK_LIST = "KEY_BLACK_LIST_NEW_BOOKS";

    /**
     * 畅销列表 黑名单KEY
     */
    public static final String KEY_SELL_WELL_BOOKS = "KEY_BLACK_LIST_SELL_WELL_BOOKS";

    /**
     * 特价书 黑名单KEY
     */
    public static final String KEY_SPECIAL_OFFER_BOOKS = "KEY_BLACK_LIST_SPECIAL_OFFER_BOOKS";


    /**
     * 不享受会员优惠商品 黑名单KEY
     */
    public static final String KEY_UN_SHOW_VIP_PRICE = "KEY_BLACK_LIST_UN_SHOW_VIP_PRICE";


    /**
     * 库存黑名单
     */
    public static final String KEY_UN_WARE_HOUSE = "KEY_BLACK_LIST_WARE_HOUSE";

    /**
     * 不能使用积分商品 黑名单KEY
     */
    public static final String KEY_POINT_NOT_SPLIT = "KEY_BLACK_LIST_POINT_NOT_SPLIT";

    /**
     * 底部分类 黑名单KEY
     */
    public static final String KEY_CLASSIFY_AT_BOTTOM = "KEY_BLACK_LIST_CLASSIFY_AT_BOTTOM";

    /**
     * 首页商品搜索H5和领阅不展示 黑名单KEY
     */
    public static final String KEY_GOODS_SEARCH_AT_INDEX = "KEY_BLACK_LIST_GOODS_SEARCH_AT_INDEX";

    /**
     * 首页商品搜索H5不展示 黑名单KEY
     */
    public static final String KEY_GOODS_SEARCH_H5_AT_INDEX = "KEY_BLACK_LIST_GOODS_SEARCH_H5_AT_INDEX";

    /**
     * 下单不使用优惠券 黑名单KEY
     */
    public static final String KEY_UN_USE_GOODS_COUPON = "KEY_BLACK_LIST_UN_USE_GOODS_COUPON";

    /**
     * 下单不返还积分
     */
    public static final String KEY_UN_BACK_POINT_AFTER_PAY = "KEY_BLACK_LIST_UN_BACK_POINT_AFTER_PAY";

    /**
     * SPU_ID hashKey
     */
    public static final String KEY_SPU_ID = "KEY_BLACK_LIST_SPU_ID";

    /**
     * CLASSIFY_ID_SECOND hashKey
     */
    public static final String KEY_CLASSIFY_ID_SECOND = "KEY_BLACK_LIST_CLASSIFY_ID_SECOND";


    /**
     * NORMAL hashKey
     */
    public static final String KEY_NORMAL_KEY = "KEY_BLACK_LIST_NORMAL_KEY";
}
