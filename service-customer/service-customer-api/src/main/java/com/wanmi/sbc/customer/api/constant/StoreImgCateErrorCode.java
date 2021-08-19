package com.wanmi.sbc.customer.api.constant;

/**
 * <p>店铺图片分类异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:03.
 */
public final class StoreImgCateErrorCode {
    private StoreImgCateErrorCode() {
    }

    /**
     * 图片父类不存在
     */
    public final static String PARE_CATE_NOT_EXIST = "K-110601";

    /**
     * 默认分类不存在
     */
    public final static String DEFAULT_NOT_EXIST = "K-110602";

    /**
     * 该父类下最多支持20个分类
     */
    public final static String CATE_NUM_LIMIT = "K-110603";

    /**
     * 分类名称已存在
     */
    public final static String NAME_EXIST = "K-110604";

    /**
     * 店铺分类层次最多允许3级
     */
    public final static String GRADE_NUM_LIMIT = "K-110605";
}
