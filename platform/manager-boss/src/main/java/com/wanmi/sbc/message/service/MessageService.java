package com.wanmi.sbc.message.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.message.api.provider.messagesend.MessageSendProvider;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendAddRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendDelByIdRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendModifyRequest;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendAddResponse;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendModifyResponse;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.vo.MessageSendVO;
import com.wanmi.sbc.quartz.service.TaskJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @program: sbc-micro-service
 * @description: 创建站内信
 * @create: 2020-01-13 10:08
 **/
@Service
public class MessageService {

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private MessageSendProvider messageSendProvider;


    public BaseResponse addMessage(MessageSendAddRequest request){
        BaseResponse<MessageSendAddResponse> response = messageSendProvider.add(request);
        MessageSendVO messageSendVO = response.getContext().getMessageSendVO();
        if (ResultCode.SUCCESSFUL.equals(response.getCode())) {
            addOrModifyTaskJob(messageSendVO);
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 消息发送定时任务
     * @param messageSendVO
     * @param messageSendVO
     */
    public void addOrModifyTaskJob(MessageSendVO messageSendVO){
        if(Objects.nonNull(messageSendVO) && Objects.equals(MessageType.Preferential, messageSendVO.getMessageType())){
            taskJobService.messageSendTaskJob(String.valueOf(messageSendVO.getMessageId()), messageSendVO.getSendTime());
        }
    }

    @GlobalTransactional
    public BaseResponse modify(MessageSendModifyRequest request){
        BaseResponse<MessageSendModifyResponse> response = messageSendProvider.modify(request);
        MessageSendVO messageSendVO = response.getContext().getMessageSendVO();
        if (ResultCode.SUCCESSFUL.equals(response.getCode())) {
            this.addOrModifyTaskJob(messageSendVO);
        }
        return BaseResponse.SUCCESSFUL();
    }

    public BaseResponse deleteById(MessageSendDelByIdRequest request){
        messageSendProvider.deleteById(request);
        return BaseResponse.SUCCESSFUL();
    }
}