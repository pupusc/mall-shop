package com.wanmi.sbc.customer.api.constant;

/**
 * <p>店铺分类异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:02.
 */
public final class StoreCateErrorCode {
    private StoreCateErrorCode() {
    }

    /**
     * 店铺商品分类名称重复
     */
    public final static String NAME_EXIST = "K-110501";

    /**
     * 一级分类超过限制个数(默认最多20个)
     */
    public final static String FIRST_GRADE_LIMIT = "K-110502";

    /**
     * 店铺分类最多可添加2个层级
     */
    public final static String GRADE_NUM_LIMIT = "K-110503";

    /**
     * 一级分类的二级分类超过限制个数(默认最多20个)
     */
    public final static String SECOND_GRADE_LIMIT = "K-110504";

    /**
     * 父分类不存在
     */
    public final static String PARENT_NOT_EXIST = "K-110505";

    /**
     * 默认分类不存在
     */
    public final static String DEFAULT_NOT_EXIST = "K-110506";

    /**
     * 店铺分类不存在
     */
    public final static String NOT_EXIST = "K-110507";

    /**
     * 店铺分类已存在
     */
    public final static String StoreCate_EXIST = "K-110508";
}
