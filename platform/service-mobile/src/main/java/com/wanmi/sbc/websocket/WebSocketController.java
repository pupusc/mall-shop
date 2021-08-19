package com.wanmi.sbc.websocket;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.provider.trade.GrouponInstanceQueryProvider;
import com.wanmi.sbc.order.api.request.trade.GrouponInstanceByGrouponNoRequest;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * lq
 */
@EnableBinding
@RestController
@Slf4j
public class WebSocketController {

    @Autowired
    private GrouponInstanceQueryProvider grouponInstanceQueryProvider;

    @Autowired
    private BinderAwareChannelResolver resolver;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    /**
     * 测试接口
     * 根据团实例编号查询团信息
     */
    @RequestMapping(value = "/testmq/grouponInstanceInfo/{grouponNo}", method = RequestMethod.GET)
    public BaseResponse<GrouponInstanceVO> getGrouponInstanceInfoByMQ(@PathVariable String grouponNo) {
        log.info("getGrouponInstanceInfo",grouponNo);
        GrouponInstanceByGrouponNoRequest request = GrouponInstanceByGrouponNoRequest.builder().grouponNo(grouponNo)
                .build();
        GrouponInstanceVO grouponInstanceVO = grouponInstanceQueryProvider.detailByGrouponNo(request).getContext
                ().getGrouponInstance();
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_MESSAGE_PUSH_TO_C).send
                (new GenericMessage<>(JSONObject.toJSONString(grouponInstanceVO)));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 测试接口
     * 根据团实例编号查询团信息
     */
    @RequestMapping(value = "/test/grouponInstanceInfo/{grouponNo}", method = RequestMethod.GET)
    public BaseResponse<GrouponInstanceVO> getGrouponInstanceInfo(@PathVariable String grouponNo) {
        log.info("getGrouponInstanceInfo",grouponNo);
        GrouponInstanceByGrouponNoRequest request = GrouponInstanceByGrouponNoRequest.builder().grouponNo(grouponNo)
                .build();
        GrouponInstanceVO grouponInstanceVO = grouponInstanceQueryProvider.detailByGrouponNo(request).getContext
                ().getGrouponInstance();
        //发送消息
        messagingTemplate.convertAndSend("/topic/getGrouponInstance", grouponInstanceVO);
        return BaseResponse.SUCCESSFUL();
    }
}
