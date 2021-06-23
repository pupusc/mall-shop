package com.wanmi.sbc.goods.api.constant;

/**
 * @author yang
 * @since 2019/5/30
 */
public final class PointsGoodsErrorCode {

    private PointsGoodsErrorCode() {
    }

    /**
     * sku编码错误
     */
    public final static String SKU_ERROR = "K-031001";

    /**
     * 店铺已关店
     */
    public final static String STORE_CLOSE = "K-031002";

    /**
     * 店铺已禁用
     */
    public final static String STORE_DISABLE = "K-031003";

    /**
     * 活动日期错误
     */
    public final static String TIME_ERROR = "K-031004";

    /**
     * 兑换活动已结束，无法启用
     */
    public final static String START_ERROR = "K-031005";

    /**
     * 兑换开始时间应大于当前时间
     */
    public final static String BEGIN_DATE_ERROR = "K-031006";

    /**
     * 兑换结束时间应在开始时间之后
     */
    public final static String END_DATE_ERROR = "K-031007";

    /**
     * 库存为0，无法启用
     */
    public final static String ENABLE_ERROR = "K-031008";

    /**
     * 店铺已删除
     */
    public final static String STORE_DELETE = "K-031009";

    /**
     * 活动已开始，无法编辑
     */
    public final static String MODIFY_ERROR = "K-031010";

    /**
     * 活动已开始，无法删除
     */
    public final static String DELETED_ERROR = "K-031011";

    /**
     * 积分商品分类已删除，无法添加
     */
    public final static String CATE_ERROR = "K-031012";
}
