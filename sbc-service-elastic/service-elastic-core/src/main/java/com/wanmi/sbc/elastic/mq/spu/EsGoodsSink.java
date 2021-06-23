package com.wanmi.sbc.elastic.mq.spu;


import com.wanmi.sbc.common.constant.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author dyt
 * @Description //TODO
 * @Date 10:18 2020/12/16
 * @Param
 * @return
 **/
public interface EsGoodsSink {

	/**
	 * 初始化商品
	 * @return
	 */
	@Input(MQConstant.Q_ES_GOODS_INIT)
	SubscribableChannel init();
}
