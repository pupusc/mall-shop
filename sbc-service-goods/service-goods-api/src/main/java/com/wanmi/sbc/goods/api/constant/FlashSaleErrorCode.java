package com.wanmi.sbc.goods.api.constant;

/**
 * @program: sbc-micro-service
 * @description: 秒杀错误代码
 * @create: 2019-06-14 15:56
 **/
public final class FlashSaleErrorCode {
    /**
     * 秒杀活动商品未结束重复添加
     */
    public final static String REPEATEDLY_ADDED = "K-031201";

    /**
     * 活动开始前一小时后无法修改
     */
    public final static String NOT_MODIFY = "K-031202";

    /**
     * 活动开始前一小时后无法删除
     */
    public final static String NOT_DELETE = "K-031203";

    /**
     * 分类已删除，请重新添加
     */
    public final static String CATE_DELETE = "K-031204";

    /**
     * 活动开始前一小时后禁止添加
     */
    public final static String NOT_ADDED = "K-031205";
}