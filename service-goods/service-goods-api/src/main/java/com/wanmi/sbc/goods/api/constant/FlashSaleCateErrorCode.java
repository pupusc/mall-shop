package com.wanmi.sbc.goods.api.constant;

/**
 * @author yxz
 * @since 2019/6/13
 */
public class FlashSaleCateErrorCode {

    private FlashSaleCateErrorCode() {
    }

    /**
     * 分类名称已存在
     */
    public final static String NAME_ALREADY_EXIST = "K-031101";

    /**
     * 分类最多维护16个
     */
    public final static String CATE_OVERSTEP = "K-031102";

    /**
     * 已关联商品无法删除
     */
    public final static String UNABLED_DELETE = "K-031103";
}
