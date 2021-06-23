package com.wanmi.sbc.goods.api.constant;

/**
 * <p>商品标签异常码定义</p>
 */
public final class GoodsLabelErrorCode {
    private GoodsLabelErrorCode() {
    }
    /**
     * 该标签名称已存在！
     */
    public final static String LABEL_NAME_ALREADY_EXIST = "K-031301";

    /**
     * 标签数量不能超过20个
     */
    public final static String LABEL_MAX_LENGTH_LIMIT = "K-031302";

    /**
     * 商品标签不存在
     */
    public final static String LABEL_NOT_EXIST = "K-031303";

}
