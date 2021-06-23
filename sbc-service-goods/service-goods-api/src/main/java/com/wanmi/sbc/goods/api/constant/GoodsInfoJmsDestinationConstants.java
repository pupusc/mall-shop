package com.wanmi.sbc.goods.api.constant;

/**
 * MQ消息目的地
 * @author: hehu
 * @createDate: 2020-03-17 14:04:32
 * @version: 1.0
 */
public class GoodsInfoJmsDestinationConstants {

    /**
     * sku库存扣减发送消息
     */
    public static final String GOODS_INFO_STOCK_SUB_OUTPUT = "goods-info-stock-sub-output";

    /**
     * sku库存扣减接收消息
     */
    public static final String GOODS_INFO_STOCK_SUB_INPUT = "goods-info-stock-sub-input";

    /**
     * sku库存增加发送消息
     */
    public static final String GOODS_INFO_STOCK_ADD_OUTPUT = "goods-info-stock-add-output";

    /**
     * sku库存增加接收消息
     */
    public static final String GOODS_INFO_STOCK_ADD_INPUT = "goods-info-stock-add-input";

    /**
     * sku库存覆盖更新接收参数
     */
    public static final String GOODS_INFO_STOCK_RESET_INPUT= "goods-info-stock-reset-input";

    /**
     * sku库存覆盖更新发送参数
     */
    public static final String GOODS_INFO_STOCK_RESET_OUTPUT= "goods-info-stock-reset-output";

}
