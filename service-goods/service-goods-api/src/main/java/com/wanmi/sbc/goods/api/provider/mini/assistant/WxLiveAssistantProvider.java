package com.wanmi.sbc.goods.api.provider.mini.assistant;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.goods.name}", contextId = "WxLiveAssistantProvider")
public interface WxLiveAssistantProvider {

    @PostMapping("/wx/assistang/${application.goods.version}/list")
    BaseResponse listGoods(@RequestBody WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest);
}
