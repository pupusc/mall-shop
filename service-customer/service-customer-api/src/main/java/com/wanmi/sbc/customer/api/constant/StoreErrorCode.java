package com.wanmi.sbc.customer.api.constant;

/**
 * <p>店铺异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午3:38.
 */
public final class StoreErrorCode {
    /**
     * 店铺不存在
     */
    public final static String NOT_EXIST = "K-110201";

    /**
     * 店铺名称已存在
     */
    public final static String NAME_ALREADY_EXISTS = "K-110202";

    /**
     * 审核未通过
     */
    public final static String REJECTED = "K-110203";

    /**
     * 审核已完成
     */
    public final static String COMPLETED = "K-110205";

    /**
     * 店铺已关店
     */
    public final static String CLOSE = "K-110206";

    /**
     * 店铺已过期
     */
    public final static String OVER_DUE = "K-110207";

    /**
     * 店铺收藏最多支持{0}家
     */
    public final static String FOLLOW_LIMIT = "K-110208";

    private StoreErrorCode() {
    }
}
