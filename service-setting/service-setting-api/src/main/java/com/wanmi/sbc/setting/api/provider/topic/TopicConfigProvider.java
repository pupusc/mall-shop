package com.wanmi.sbc.setting.api.provider.topic;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicConfigAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(value = "${application.setting.name}", contextId = "TopicConfigProvider")
public interface TopicConfigProvider {

    /**
     * 新增专题
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/topicconfig/add")
    BaseResponse add(@RequestBody TopicConfigAddRequest request);
}
