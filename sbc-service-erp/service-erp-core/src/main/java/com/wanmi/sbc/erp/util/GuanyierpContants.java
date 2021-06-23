package com.wanmi.sbc.erp.util;

/**
 * @program: sbc-background
 * @description: 管易云ERP接口常量参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 14:12
 **/
public class GuanyierpContants {

    /**
     * 管易云ERP-订单创建接口名
     */
    public static final String PUSH_ORDER_METHOD = "gy.erp.trade.add";

    /**
     * 管易云ERP-商品查询接口名
     */
    public static final String GOODS_QUERY_METHOD= "gy.erp.items.get";

    /**
     * 管易云ERP-商品新库存接口名
     */
    public static final String GOODS_STOCK_METHOD = "gy.erp.new.stock.get";

    /**
     * 管易云ERP-仓库列表查询接口名
     */
    public static final String WAREHOUSE_QUERY_METHOD= "gy.erp.warehouse.get";

    /**
     * 管易云ERP-订单退款状态修改接口名
     */
    public static final String REFUND_UPDATE_METHOD= "gy.erp.trade.refund.update";

    /**
     * 管易云ERP-发货单查询接口名
     */
    public static final String DELIVERY_QUERY_METHOD= "gy.erp.trade.deliverys.get";

    /**
     * 管易云ERP-订单拦截接口名
     */
    public static final String TRADE_INTERCEPT_METHOD = "gy.erp.trade.intercept";

    /**
     * 管易云ERP-创建退货单接口
     */
    public static final String RETURN_TRADE_ADD_METHOD = "gy.erp.trade.return.add";

    /**
     * 管易云ERP-退货单查询接口
     */
    public static final String RETURN_TRADE_GET_METHOD = "gy.erp.trade.return.get";

    /**
     * 管易云ERP-历史发货单查询接口名
     */
    public static final String DELIVERY_HISTORY_QUERY_METHOD= "gy.erp.trade.deliverys.history.get";

}
