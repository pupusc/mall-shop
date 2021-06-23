package com.wanmi.sbc.customer.api.constant;

/**
 * <p>会员权益异常码定义</p>
 */
public final class CustomerLevelRightsErrorCode {
    private CustomerLevelRightsErrorCode() {
    }

    /**
     * 会员权益不存在
     */
    public final static String RIGHTS_NO_EXIST = "K-010701";

    /**
     * 已关联到客户等级，不可删除
     */
    public final static String DEL_RELATED_TO_LEVEL = "K-010702";

    /**
     * 该权益名称已存在
     */
    public final static String RIGHTS_ALREADY_EXISTS = "K-010703";

    /**
     * 权益绑定的优惠券数量超出限制
     */
    public final static String RIGHTS_COUPON_NUM_EXCEED = "K-010704";

    /**
     * 已关联到客户等级，不可禁用
     */
    public final static String UPDATE_RELATED_TO_LEVEL = "K-010705";

}
