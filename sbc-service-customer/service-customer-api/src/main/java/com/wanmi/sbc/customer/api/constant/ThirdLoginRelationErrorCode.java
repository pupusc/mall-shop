package com.wanmi.sbc.customer.api.constant;

/**
 * @Author: songhanlin
 * @Date: Created In 3:18 PM 2018/8/8
 * @Description: 第三方登录关系错误码
 */
public final class ThirdLoginRelationErrorCode {
    private ThirdLoginRelationErrorCode() {
    }

    /**
     * 信息过多
     */
    public final static String MORE_INFORMATION = "K-010301";

    /**
     * 未找到相应的值
     */
    public final static String UNDEFINED = "K-010302";

    /**
     * 时间太短, 30天以后才可以解绑
     */
    public final static String TIME_TOO_SHORT = "K-010303";

    /**
     * K-010304 已废弃, 后期可以加入
     */

    /**
     * 时间超时
     */
    public final static String TIME_OUT = "K-010305";

    /**
     * 手机已经被绑定
     */
    public final static String PHONE_ALREADY_BINDING = "K-010306";

    /**
     * 微信已经被绑定
     */
    public final static String WX_ALREADY_BINDING = "K-010307";

    /**
     * 网络不给力，请重试
     */
    public final static String WX_DECRYPT_ERROR = "K-010308";
}
