package com.wanmi.sbc.goods.api.constant;

/**
 * MQ消息目的地
 * @author: Geek Wang
 * @createDate: 2019/2/25 13:57
 * @version: 1.0
 */
public class GoodsJmsDestinationConstants {

    /**
     * 更新已成团人数
     */
    public static final String Q_GROUPON_GOODS_INFO_MODIFY_ALREADY_GROUPON_NUM = "q.groupon.goods.info.modify.already.groupon.num";

    /**
     * 更新商品销售量、订单量、交易额
     */
    public static final String Q_GROUPON_GOODS_INFO_MODIFY_ORDER_PAY_STATISTICS = "q.groupon.goods.info.modify.order.pay.statistics";

    /**
     * 更新商品所属的供应商店铺状态
     */
    public static final String Q_GOODS_MODIFY_PROVIDER_STATUS = "q.goods.modify.provider.status";
}
