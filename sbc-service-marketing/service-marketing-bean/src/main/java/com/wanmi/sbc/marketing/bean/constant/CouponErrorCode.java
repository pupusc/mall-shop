package com.wanmi.sbc.marketing.bean.constant;

/**
 * @Author: songhanlin
 * @Date: Created In 3:10 PM 2018/9/13
 * @Description: 优惠券错误类
 */
public final class CouponErrorCode {
    private CouponErrorCode() {
    }

    /**
     * 优惠券分类名称已存在
     */
    public final static String COUPON_CATE_NAME_EXIST = "K-080101";

    /**
     * 优惠券分类已经到达上限
     */
    public final static String COUPON_CATE_ALREADY_MAX = "K-080102";

    /**
     * 优惠券分类不存在
     */
    public final static String COUPON_CATE_NOT_EXIST = "K-080103";

    /**
     * 优惠券不存在
     */
    public final static String COUPON_INFO_NOT_EXIST = "K-080104";

    /**
     * 优惠券已经关联活动
     */
    public final static String COUPON_INFO_IN_USE = "K-080105";

    /**
     * 优惠券已经关联会员权益
     */
    public final static String COUPON_RELATED_TO_RIGHTS = "K-080114";

    /**
     *  选择的优惠券有问题
     */
    public final static String ACTIVITY_ERROR_COUPON ="K-080106";

    /**
     * 活动正在进行中
     */
    public final static String ACTIVITY_GOING ="K-080107";

    /**
     * 活动未开始
     */
    public final static String ACTIVITY_NOT_START ="K-080108";

    /**
     * 活动已结束
     */
    public final static String ACTIVITY_FINISH ="K-080109";

    /**
     * 活动不存在
     */
    public final static String ACTIVITY_NOT_EXIST = "K-080110";

    /**
     * 活动和优惠券的规则关联不存在
     */
    public final static String ACTIVITY_COUPON_CONFIG_NOT_EXIST = "K-080111";

    /**
     * 活动失效
     */
    public final static String ACTIVITY_NOT_INUSE = "K-080112";

    /**
     * 可选活动日期发生了变化，请重新选择
     */
    public final static String ACTIVITY_TIME_CHANGE = "K-080113";

    /**
     * C端查询商品不存在
     */
    public final static String GOODS_NOT_EXIST = "K-080201";

    /**
     * C端查询会员不存在
     */
    public final static String CUSTOMER_NOT_EXIST = "K-080202";

    /**
     * C端用户领券不符合要求
     */
    public final static String CUSTOMER_CAN_NOT_FETCH_COUPON_INFO = "K-080203";

    /**
     * 优惠券已经被领光
     */
    public final static String COUPON_INFO_NO_LEFT = "K-080204";

    /**
     * 该用户已经领取过该优惠券
     */
    public final static String CUSTOMER_HAS_FETCHED_COUPON = "K-080205";

    /**
     * 订单中存在不符合使用条件的优惠券
     */
    public final static String CUSTOMER_ORDER_COUPON_INVALID = "K-080206";

    /**
     * 该用户已经全部领取(领取已达到上限)
     */
    public final static String CUSTOMER_FETCHED_ALL = "K-080207";

    /**
     * 优惠券已删除
     */
    public final static String COUPON_INFO_DELETED = "K-080208";

    /**
     * 优惠券截止时间小于兑换结束时间
     */
    public final static String POINTS_COUPON_DATE_ERROR = "K-080209";

    /**
     * 兑换结束时间应在开始时间之后
     */
    public final static String END_DATE_ERROR = "K-080210";

    /**
     * 兑换日期错误
     */
    public final static String TIME_ERROR = "K-080211";

    /**
     * 兑换开始时间应大于当前时间
     */
    public final static String BEGIN_DATE_ERROR = "K-080212";

    /**
     * 兑换活动已结束，无法启用
     */
    public final static String START_ERROR = "K-080213";

    /**
     * 库存为0，无法启用
     */
    public final static String ENABLE_ERROR = "K-080214";

    /**
     * 活动已开始，无法编辑
     */
    public final static String MODIFY_ERROR = "K-080215";

    /**
     * 活动已开始，无法删除
     */
    public final static String DELETED_ERROR = "K-080216";

    /**
     * 精准发券时间大于当前时间
     */
    public final static String START_TIME_ERROR = "K-081005";
}
