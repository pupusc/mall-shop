package com.wanmi.sbc.goods.api.constant;

/**
 * <p>商品分类异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午4:35.
 */
public final class GoodsCateErrorCode {
    private GoodsCateErrorCode() {
    }
    /**
     * 相关父类不存在
     */
    public final static String PARENT_CATE_NOT_EXIST = "K-030101";

    /**
     * 默认分类不存在
     */
    public final static String DEFAULT_CATE_NOT_EXIST = "K-030102";

    /**
     * 该父类下最多支持20个分类
     */
    public final static String CHILD_CATE_MAX = "K-030103";
    /**
     * 分类名称已存在
     */
    public final static String NAME_ALREADY_EXIST = "K-030104";

    /**
     * 您只能添加20个一级分类
     */
    public final static String LEVEL_ONE_MAX = "K-030105";

    /**
     * 平台类目不存在
     */
    public final static String NOT_EXIST = "K-030106";

    /**
     * 含有商品不能删除类目
     */
    public final static String NOT_DELETE_FOR_GOODS = "K-030107";
}
