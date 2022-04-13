package com.wanmi.sbc.callback.handler;

import com.soybean.mall.wx.mini.customerserver.controller.WxCustomerServerApiController;
import com.soybean.mall.wx.mini.customerserver.request.WxCustomerServerOnlineRequest;
import com.soybean.mall.wx.mini.customerserver.response.WxCustomerServerOnlineResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/13 2:22 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class MessageCallBackHandler implements CallbackHandler {

    @Autowired
    private WxCustomerServerApiController wxCustomerServerApiController;

    @Override
    public boolean support(String eventType) {
        return Arrays.asList("text", "image").contains(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
//        {Content=222, CreateTime=1649787333, ToUserName=gh_acd5c1ee4776, FromUserName=oj6KP5A1Ca0rPVPCVq0kA0aQ6mQM, MsgType=text, MsgId=23619277945538626}
        WxCustomerServerOnlineRequest wxCustomerServerOnlineRequest = new WxCustomerServerOnlineRequest();
        BaseResponse<WxCustomerServerOnlineResponse> wxCustomerServerOnlineResponse
                = wxCustomerServerApiController.listCustomerServerOnline(wxCustomerServerOnlineRequest);
        WxCustomerServerOnlineResponse context = wxCustomerServerOnlineResponse.getContext();
        List<WxCustomerServerOnlineResponse.CustomerServerOnline> result = new ArrayList<>();
        for (WxCustomerServerOnlineResponse.CustomerServerOnline customerServerOnline : context.getCustomerServerOnlineList()) {
            if (customerServerOnline.getStatus() != null && customerServerOnline.getStatus() == 1) {
                result.add(customerServerOnline);
            }
        }

        if (CollectionUtils.isEmpty(result)) {
            log.error("MessageCallBackHandler handle 当前没有在线客服");
            return "fail";
        }

        int index = (int)(Math.random() * result.size());
        WxCustomerServerOnlineResponse.CustomerServerOnline customerServerOnline = result.get(index);

        String createTime = (String) paramMap.get("CreateTime");
        String toUser = customerServerOnline.getOpenId();
        String msgType = "transfer_customer_service";
        String fromUserName = "WANH3081435";
        String format = "<xml>" +
                "<ToUserName><![CDATA[%s]]></ToUserName>" +
                "<FromUserName><![CDATA[%s]]></FromUserName>" +
                "<CreateTime>%s</CreateTime>" +
                "<MsgType><![CDATA[%s]]></MsgType>" +
                "</xml>";
        return String.format(format, toUser, fromUserName, createTime, msgType);
    }
}
