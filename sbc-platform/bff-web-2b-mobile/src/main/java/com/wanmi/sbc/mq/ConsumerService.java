package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@EnableBinding(MobileSink.class)
public class ConsumerService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 团长开团-消息推送C端
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_MESSAGE_PUSH_TO_C)
    public void openGrouponMsgToC(String json) {
        log.info("团长开团-消息推送C端(消费端)========》{}",json);
        GrouponInstanceVO vo = JSONObject.parseObject(json,GrouponInstanceVO.class);
        //发送消息
        messagingTemplate.convertAndSend("/topic/getGrouponInstance", vo);
    }
}
