package com.wanmi.sbc.message;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.provider.appmessage.AppMessageProvider;
import com.wanmi.sbc.message.api.provider.appmessage.AppMessageQueryProvider;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageDelByIdRequest;
import com.wanmi.sbc.message.api.request.appmessage.AppMessagePageRequest;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageSetReadRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendDelByIdRequest;
import com.wanmi.sbc.message.api.response.appmessage.AppMessagePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "MessageController", description = "站内信 API")
@RestController
@RequestMapping("/appMessage")
@Validated
public class MessageController {

    @Autowired
    private AppMessageQueryProvider appMessageQueryProvider;

    @Autowired
    private AppMessageProvider appMessageProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 分页查询app消息
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询app消息")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<AppMessagePageResponse> page(@RequestBody AppMessagePageRequest request){
        request.setCustomerId(commonUtil.getOperatorId());
        AppMessagePageResponse response = appMessageQueryProvider.page(request).getContext();
        return BaseResponse.success(response);
    }

    /**
     * 将某条未读消息置为已读
     * @param id
     * @return
     */
    @ApiOperation(value = "将某条未读消息置为已读")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "APP消息id", required = true)
    @RequestMapping(value = "/setMessageRead/{id}", method = RequestMethod.PUT)
    public BaseResponse setMessageRead(@PathVariable("id")String id){
        AppMessageSetReadRequest request = AppMessageSetReadRequest.builder()
                .messageId(id)
                .customerId(commonUtil.getOperatorId())
                .build();
        appMessageProvider.setMessageRead(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 将所有未读消息置为已读
     * @param
     * @return
     */
    @ApiOperation(value = "将所有未读消息置为已读")
    @RequestMapping(value = "/setMessageAllRead", method = RequestMethod.PUT)
    public BaseResponse setMessageAllRead(){
        AppMessageSetReadRequest request = AppMessageSetReadRequest.builder()
                .customerId(commonUtil.getOperatorId()).build();
        appMessageProvider.setMessageAllRead(request);
        return BaseResponse.SUCCESSFUL();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "APP消息id", required = true)
    @ApiOperation(value = "删除消息")
    public BaseResponse deleteById(@PathVariable("id") String id){
        AppMessageDelByIdRequest request = new AppMessageDelByIdRequest(id, commonUtil.getOperatorId());
        appMessageProvider.deleteById(request);
        return BaseResponse.SUCCESSFUL();
    }


}
