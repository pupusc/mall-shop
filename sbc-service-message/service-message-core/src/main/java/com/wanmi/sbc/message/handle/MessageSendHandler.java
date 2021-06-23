package com.wanmi.sbc.message.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageAddRequest;
import com.wanmi.sbc.message.appmessage.service.AppMessageService;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.message.bean.enums.SwitchFlag;
import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import com.wanmi.sbc.message.messagesendnode.model.root.MessageSendNode;
import com.wanmi.sbc.message.messagesendnode.service.MessageSendNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class MessageSendHandler implements MessageDelivery {

    @Autowired
    private MessageSendNodeService messageSendNodeService;

    @Autowired
    private AppMessageService appMessageService;

    @Override
    public void handle(MessageMQRequest request) {
        log.info("站内信节点{}接收消息", request.getNodeCode());
        List<String> params = request.getParams();
        MessageSendNode node = messageSendNodeService.findByNodeTypeAndCode(NodeType.fromValue(request.getNodeType()),request.getNodeCode());
        //开关开启组装消息
        if(node.getStatus().equals(SwitchFlag.OPEN)){
            StringBuffer nodeContent = new StringBuffer();
            String[] contents = node.getNodeContent().split("\\{(.*?)\\}");
            for(int i = 0; i < contents.length; i++){
                nodeContent.append(contents[i]);
                if(CollectionUtils.isNotEmpty(params) && i < params.size()){
                    nodeContent.append(params.get(i));
                }
            }
            if(request.getRouteParam() != null){
                int type = (int)request.getRouteParam().get("type") + 7;
                request.getRouteParam().put("type", type);
            }

            //封装app消息
            AppMessageAddRequest appMessageAddRequest = new AppMessageAddRequest();
            AppMessageVO appMessageVO = new AppMessageVO();
            appMessageVO.setMessageType(MessageType.Notice);
            appMessageVO.setTitle(node.getNodeTitle());
            appMessageVO.setContent(nodeContent.toString());
            appMessageVO.setSendTime(LocalDateTime.now());
            appMessageVO.setRouteParam(JSON.toJSONString(request.getRouteParam()));
            appMessageVO.setJoinId(node.getId());
            appMessageVO.setImgUrl(request.getPic());
            appMessageAddRequest.setAppMessageVO(appMessageVO);
            appMessageAddRequest.setCustomerIds(Lists.newArrayList(request.getCustomerId()));
            appMessageService.addBatch(appMessageAddRequest);
        }


    }

}
