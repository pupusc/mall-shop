package com.wanmi.sbc.mini.mq;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.mini.mq.config.WxLiveMessageSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(WxLiveMessageSink.class)
public class WxMiniMessageConsumer {

    @Autowired
    private WxLiveAssistantProvider wxLiveAssistantProvider;

    @StreamListener(WxLiveMessageSink.MSG_DELAY_LIVE_ASSISTANT_CONSUMER)
    public void wxLiveDelayMessageConsume(String message) {
        log.info("直播助手事件 receive:{}", message);
        BaseResponse baseResponse = wxLiveAssistantProvider.afterWxLiveEnd(message);
        if(baseResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)){
            log.info("直播助手事件 执行完成");
        }else {
            log.info("直播助手事件 执行失败");
        }
    }
}
