package com.wanmi.sbc.goods.mq;


import com.wanmi.sbc.goods.api.constant.GoodsInfoJmsDestinationConstants;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 库存Sink
 * @author: hehu
 * @createDate: 2020-03-17 11:19:26
 * @version: 1.0
 */
@EnableBinding
public interface GoodsInfoStockSink {
	/**
	 * 扣减库存消息接收
	 * @return
	 */
	@Input(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_SUB_INPUT)
	SubscribableChannel subInput();

	/**
	 * 扣减库存消息发送
	 * @return
	 */
	@Output(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_SUB_OUTPUT)
	MessageChannel subOutput();

	/**
	 * 增加库存消息接收
	 * @return
	 */
	@Input(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_ADD_INPUT)
	SubscribableChannel addInput();

	/**
	 * 增加库存消息发送
	 * @return
	 */
	@Output(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_ADD_OUTPUT)
	MessageChannel addOutput();

	/**
	 * 更新库存消息接收
	 * @return
	 */
	@Input(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_RESET_INPUT)
	SubscribableChannel resetInput();

	/**
	 * 更新库存消息发送
	 * @return
	 */
	@Output(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_RESET_OUTPUT)
	MessageChannel resetOutput();

}
