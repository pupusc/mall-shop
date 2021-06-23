package com.wanmi.sbc.goods.api.constant;

/**
 * @author yang
 * @since 2019/5/13
 */
public class PointsGoodsCateErrorCode {

    private PointsGoodsCateErrorCode() {
    }

    /**
     * 分类名称已存在
     */
    public final static String NAME_ALREADY_EXIST = "K-030901";

    /**
     * 分类最多维护30个
     */
    public final static String CATE_OVERSTEP = "K-030902";

    /**
     * 已关联商品无法删除
     */
    public final static String UNABLED_DELETE = "K-030903";
}
