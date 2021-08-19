package com.wanmi.sbc.message;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.message.api.provider.messagesendnode.MessageSendNodeProvider;
import com.wanmi.sbc.message.api.provider.messagesendnode.MessageSendNodeQueryProvider;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeByIdRequest;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeModifyRequest;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodePageRequest;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeUpdateStatusRequest;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeByIdResponse;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodeModifyResponse;
import com.wanmi.sbc.message.api.response.messagesendnode.MessageSendNodePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("MessageNodeController")
@RequestMapping("/messageNode")
@Api(tags = "MessageNodeController", description = "站内信通知节点相关API")
public class MessageNodeController {

    @Autowired
    private MessageSendNodeQueryProvider messageSendNodeQueryProvider;

    @Autowired
    private MessageSendNodeProvider messageSendNodeProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查看站内信通知节点")
    public BaseResponse<MessageSendNodePageResponse> page(@Valid @RequestBody MessageSendNodePageRequest request){
        MessageSendNodePageResponse response = messageSendNodeQueryProvider.page(request).getContext();
        return BaseResponse.success(response);
    }

    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    @ApiOperation(value = "修改通知节点")
    public BaseResponse modify(@Valid @RequestBody MessageSendNodeModifyRequest request){
        request.setUpdatePerson(commonUtil.getOperatorId());
        BaseResponse<MessageSendNodeModifyResponse> response = messageSendNodeProvider.modify(request);
        if (ResultCode.SUCCESSFUL.equals(response.getCode())) {
            operateLogMQUtil.convertAndSend("消息", "修改站内信节点", "站内信节点：" + request.getNodeName());
        }
        return BaseResponse.SUCCESSFUL();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "通知节点id", required = true)
    @ApiOperation(value = "查看通知节点详情")
    public BaseResponse<MessageSendNodeByIdResponse> getNodeById(@PathVariable("id") Long id){
        MessageSendNodeByIdRequest request = new MessageSendNodeByIdRequest(id);
        MessageSendNodeByIdResponse response = messageSendNodeQueryProvider.getById(request).getContext();
        return BaseResponse.success(response);
    }

    @RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "通知节点id", required = true)
    @ApiOperation(value = "通知节点开关")
    public BaseResponse<MessageSendNodeByIdResponse> updateStatus(@PathVariable("id") Long id){
        messageSendNodeProvider.updateStatus(new MessageSendNodeUpdateStatusRequest(id));
        return BaseResponse.SUCCESSFUL();
    }
}
