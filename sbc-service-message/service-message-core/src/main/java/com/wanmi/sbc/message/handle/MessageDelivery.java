package com.wanmi.sbc.message.handle;


import com.wanmi.sbc.common.base.MessageMQRequest;


/**
 * 消息处理接口
 */
public interface MessageDelivery {

     void handle(MessageMQRequest request);
}
