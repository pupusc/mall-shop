package com.wanmi.sbc.goods.api.constant;

/**
 * <p>商品属性异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午4:32.
 */
public final class GoodsPropErrorCode {
    private GoodsPropErrorCode() {
    }

    /**
     * 非第三级商品类目不能添加属性
     */
    public final static String NOT_CHILD_NODE = "K-030501";

    /**
     * 商品类目最多可关联20个属性
     */
    public final static String GOODSPROP_OVERSTEP = "K-030502";

    /**
     * 商品类目属性值最多100个
     */
    public final static String GOODSPROPDETAIL_OVERSTEP = "K-030503";

    /**
     * 商品类目属性值不能为空
     */
    public final static String GOODSPROP_NOT_EMPTY = "K-030504";

    /**
     * 商品类目属性名称已存在
     */
    public final static String GOODSPROPNAME_ALREADY_EXIST = "K-030505";

    /**
     * 商品类目属性值已存在
     */
    public final static String GOODSPROPDETAIL_REPEAT = "K-030506";

    /**
     * 包含已删除商品
     */
    public final static String GOODSPROPDETAIL_DELETE = "K-030507";

}
