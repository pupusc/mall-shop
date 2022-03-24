package com.wanmi.sbc.goods.mq;


import com.wanmi.sbc.goods.api.constant.GoodsJmsDestinationConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/10 13:58
 * @version: 1.0
 */
public interface GoodsSink {

	/**
	 * 更新已成团人数
	 * @return
	 */
	@Input(GoodsJmsDestinationConstants.Q_GROUPON_GOODS_INFO_MODIFY_ALREADY_GROUPON_NUM)
	SubscribableChannel updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId();

	/**
	 * 更新商品销售量、订单量、交易额
	 * @return
	 */
	@Input(GoodsJmsDestinationConstants.Q_GROUPON_GOODS_INFO_MODIFY_ORDER_PAY_STATISTICS)
	SubscribableChannel updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId();

	@Input(GoodsJmsDestinationConstants.Q_GOODS_MODIFY_PROVIDER_STATUS)
	SubscribableChannel updateGoodsProviderStatus();

	/**
	 * 订单发货状态变更
	 * */
	@Input(GoodsJmsDestinationConstants.Q_SITE_SEARCH_SYNC_CONSUMER)
	SubscribableChannel siteSearchSync();
}
