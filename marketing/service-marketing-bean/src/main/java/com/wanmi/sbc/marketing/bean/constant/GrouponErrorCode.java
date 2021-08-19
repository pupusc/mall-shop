package com.wanmi.sbc.marketing.bean.constant;

/**
 * @Author: chenli
 * @Date: Created In 2:10 PM 2019/5/16
 * @Description: 拼团业务错误类
 */
public final class GrouponErrorCode {
    private GrouponErrorCode() {
    }

    /**
     * 拼团分类名称已存在
     */
    public final static String GROUPON_CATE_NAME_EXIST = "K-080301";

    /**
     * 拼团分类已经到达上限
     */
    public final static String GROUPON_CATE_ALREADY_MAX = "K-080302";

    /**
     * 拼团分类名称不存在
     */
    public final static String GROUPON_CATE_NOT_EXIST = "K-080303";

    /**
     * 该分类存在商品不可进行删除
     */
    public final static String EXIST_GOODS_FORBID_DELETE = "K-080304";



    /**
     * 拼团活动已失效，订单提交或者支付
     */
    public static final String ORDER_MARKETING_GROUPON_DISABLE = "K-080361";

    /**
     * 存在同一个拼团营销活动处于待成团状态，不可开团(团长)
     */
    public static final String ORDER_MARKETING_GROUPON_WAIT = "K-080362";

    /**
     * 已参同一团活动，不可再参团
     */
    public static final String ORDER_MARKETING_GROUPON_END = "K-080363";

    /**
     * 已超限购数量
     */
    public static final String ORDER_MARKETING_GROUPON_LIMITCOUNT = "K-080364";

    /**
     * 团发起已超过24小时(倒计时结束)/已成团，不可参团
     */
    public static final String ORDER_MARKETING_GROUPON_COUNT_DOWN_END = "K-080365";

    /**
     * 不是拼团订单（普通订单）
     */
    public static final String NOT_ORDER_MARKETING_GROUPON = "K-080366";

    /**
     * 拼团活动过期或拼团商品不存在
     */
    public final static String MARKETING_GROUPON_GOODS_NON_EXIST = "K-080367";

}
