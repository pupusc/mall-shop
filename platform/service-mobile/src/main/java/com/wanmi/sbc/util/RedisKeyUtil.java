package com.wanmi.sbc.util;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/10 3:12 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class RedisKeyUtil {

    /**
     * 首页刷新次数
     */
    public static String KEY_LIST_PREFIX_INDEX_REFRESH_COUNT = "KEY_LIST_PREFIX_INDEX_REFRESH_COUNT";

    /**
     * 分类下的商品
     */
    public static String KEY_LIST_PREFIX_INDEX_CLASSIFY_GOODS = "KEY_LIST_PREFIX_INDEX_CLASSIFY_GOODS";

    /**
     * 分类下的书单key
     */
    public static String KEY_LIST_PREFIX_INDEX_BOOK_LIST_MODEL = "KEY_LIST_PREFIX_INDEX_BOOK_LIST_MODEL";


    /**
     * 新上书籍
     */
    public final static String KEY_HOME_NEW_BOOK_LIST = "KEY_HOME_NEW_BOOK_LIST";

    /**
     * 畅销书
     */
    public final static String KEY_HOME_SELL_WELL_BOOK_LIST = "KEY_HOME_SELL_WELL_BOOK_LIST";

    /**
     * 特价书
     */
    public final static String KEY_HOME_SPECIAL_OFFER_BOOK_LIST = "KEY_HOME_SPECIAL_OFFER_BOOK_LIST";

    /**
     * 支付检测key
     */
    public final static String KEY_PAY_CHECK_LOCK = "KEY_PAY_CHECK_LOCK";

    /**
     * 混合组件
     */
    public final static String MIXED_COMPONENT = "ELASTIC_SAVE:HOMEPAGE:";

    /**
     * 混合组件
     */
    public final static String MIXED_COMPONENT_TAB = "ELASTIC_SAVE:HOMEPAGE:";


    /**
     * 首页榜单
     */
    public final static String HOME_RANK = "ELASTIC_SAVE:HOMEPAGE:";

    /**
     * 新书速递（返积分）
     */
    public final static String NEW_BOOK_POINT = "ELASTIC_SAVE:HOMEPAGE:";

    /**
     * 榜单聚合页
     */
    public final static String RANK_PAGE = "ELASTIC_SAVE:RANK_PAGE:";

    //图书详情
    public static final String ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID = "ELASTIC_SAVE:BOOKS_DETAIL_SPU_ID:";   //spu_id && isbn
}
