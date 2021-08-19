package com.wanmi.sbc.goods.api.constant;

/**
 * <p>商品库异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午5:07.
 */
public final class StandardGoodsErrorCode {
    private StandardGoodsErrorCode() {
    }

    /**
     * 商品库不存在
     */
    public final static String NOT_EXIST = "K-030601";

    /**
     * 商品库被商家引用
     */
    public final static String COMPANY_USED = "K-030602";

    /**
     * 商品库已被导入
     */
    public final static String COMPANY_IMPORT = "K-030603";
}
