package com.wanmi.sbc.marketing.bean.constant;

/**
 * <p>营销异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:26.
 */
public final class MarketingErrorCode {
    private MarketingErrorCode() {
    }

    /**
     * 营销活动不存在
     */
    public final static String NOT_EXIST = "K-080001";

    /**
     * 营销活动无法删除
     */
    public final static String MARKETING_CANNOT_DELETE = "K-080002";

    /**
     * 营销活动无法暂停
     */
    public final static String MARKETING_CANNOT_PAUSE = "K-080003";

    /**
     * 营销商品活动时间冲突
     */
    public final static String MARKETING_GOODS_TIME_CONFLICT = "K-080004";

    /**
     * 营销满系金额错误
     */
    public final static String MARKETING_FULLAMOUNT_ERROR = "K-080005";

    /**
     * 营销满系数量错误
     */
    public final static String MARKETING_FULLCOUNT_ERROR = "K-080006";

    /**
     * 满折折扣错误
     */
    public final static String MARKETING_DISCOUNT_ERROR = "K-080007";

    /**
     * 减免金额大于条件金额
     */
    public final static String MARKETING_REDUCTION_AMOUNT_ERROR = "K-080008";

    /**
     * 最少选择1种赠品
     */
    public final static String MARKETING_GIFT_TYPE_MIN_1 = "K-080009";

    /**
     * 最多选择20种赠品
     */
    public final static String MARKETING_GIFT_TYPE_MAX_20 = "K-080010";

    /**
     * 多级促销条件金额不可相同
     */
    public final static String MARKETING_MULTI_LEVEL_AMOUNT_NOT_ALLOWED_SAME = "K-080011";

    /**
     * 多级促销条件件数不可相同
     */
    public final static String MARKETING_MULTI_LEVEL_COUNT_NOT_ALLOWED_SAME = "K-080012";

    /**
     * 赠品数量仅限1-999间的整数
     */
    public final static String MARKETING_GIFT_COUNT_BETWEEN_1_AND_999 = "K-080013";

    /**
     * 营销活动无法暂停
     */
    public final static String MARKETING_CANNOT_START = "K-080014";

    /**
     * 当前营销活动已开始或已结束，无法编辑
     */
    public final static String MARKETING_STARTED_OR_ENDED = "K-080015";

    /**
     * 没有权限查看目标营销活动
     */
    public final static String MARKETING_NO_AUTH_TO_VIEW = "K-080016";

    /**
     * 营销活动已暂停
     */
    public final static String MARKETING_SUSPENDED = "K-080017";

    /**
     * 营销活动已过期
     */
    public final static String MARKETING_OVERDUE = "K-080018";

    /**
     * 加价购换购商和活动商品不能一样
     */
    public final static String MARKUP_GOODS_NOT_IN_MARKETING = "K-080026";
}
