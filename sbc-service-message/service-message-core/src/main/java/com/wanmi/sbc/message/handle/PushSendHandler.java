package com.wanmi.sbc.message.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeRequest;
import com.wanmi.sbc.message.bean.enums.SwitchFlag;
import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import com.wanmi.sbc.message.pushcustomerenable.service.PushCustomerEnableService;
import com.wanmi.sbc.message.pushsendnode.model.root.PushSendNode;
import com.wanmi.sbc.message.pushsendnode.service.PushSendNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PushSendHandler implements MessageDelivery {

    @Autowired
    private PushSendNodeService pushSendNodeService;

    @Autowired
    private PushCustomerEnableService pushCustomerEnableService;

    @Override
    public void handle(MessageMQRequest request) {
        log.info("Push节点{}接收消息", request.getNodeCode());
        log.info("Push节点接收消息request:", JSONObject.toJSONString(request));
        List<String> params = request.getParams();
        PushSendNode node = pushSendNodeService.findByNodeTypeAndCode(request.getNodeType(), request.getNodeCode());
        PushCustomerEnable pushCustomerEnable = pushCustomerEnableService.getOne(request.getCustomerId());
        if(Objects.nonNull(pushCustomerEnable)){
            SwitchFlag appSwitch = this.getAppSwitch(node, pushCustomerEnable);
            boolean time = checkTime();
            //节点开关开启组装消息
            if(node.getStatus() == 1 && SwitchFlag.OPEN.equals(appSwitch) && time){
                StringBuffer nodeContent = new StringBuffer();
                String[] contents = node.getNodeContext().split("\\{(.*?)\\}");
                for(int i = 0; i < contents.length; i++){
                    nodeContent.append(contents[i]);
                    if(CollectionUtils.isNotEmpty(params) && i < params.size()){
                        nodeContent.append(params.get(i));
                    }
                }

                PushSendNodeRequest pushSendNodeRequest = new PushSendNodeRequest();
                pushSendNodeRequest.setContent(nodeContent.toString());
                pushSendNodeRequest.setCustomerId(request.getCustomerId());
                pushSendNodeRequest.setNodeId(node.getId());
                pushSendNodeRequest.setTitle(node.getNodeTitle());
                pushSendNodeRequest.setRouter(JSON.toJSONString(request.getRouteParam()));
                log.info("Push参数：{}", pushSendNodeRequest);
                pushSendNodeService.pushNode(pushSendNodeRequest);
            }
        }

    }

    /**
     * 移动端会员push开关状态
     * @param node
     * @param pushCustomerEnable
     * @return
     */
    private SwitchFlag getAppSwitch(PushSendNode node, PushCustomerEnable pushCustomerEnable){
        SwitchFlag switchFlag;
        switch (node.getNodeType()){
            case 0:
                switchFlag =  SwitchFlag.fromValue(pushCustomerEnable.getAccountSecurity());
                break;
            case 1:
                switchFlag = SwitchFlag.fromValue(pushCustomerEnable.getAccountAssets());
                break;
            case 2:
                switchFlag =  SwitchFlag.fromValue(pushCustomerEnable.getOrderProgressRate());
                break;
            case 3:
                switchFlag =  SwitchFlag.fromValue(pushCustomerEnable.getReturnOrderProgressRate());
                break;
            case 4:
                switchFlag =  SwitchFlag.fromValue(pushCustomerEnable.getDistribution());
                break;
            default:
                switchFlag = SwitchFlag.OPEN;
        }

        return switchFlag;
    }

    /**
     * 校验时间，push推送时间需在22：00到7：00之间
     * @return
     */
    private boolean checkTime(){
        int hour = LocalDateTime.now().getHour();
        if(hour >= 22 || hour<7){
            return false;
        }else{
            return true;
        }
    }
}
