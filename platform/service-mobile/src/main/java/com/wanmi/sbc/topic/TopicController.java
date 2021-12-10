package com.wanmi.sbc.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.topic.response.TopicResponse;
import com.wanmi.sbc.topic.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @menu 专题
 * @tag topic
 * @status undone
 */

@Api(tags = "TopicController", description = "专题")
@RestController
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * @description 根据专题id返回页面数据
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "根据专题id返回页面数据，")
    @PostMapping(value = "detail")
    public BaseResponse<TopicResponse> detail(@RequestBody TopicQueryRequest request) {
        return topicService.detail(request,true);
    }


    @ApiOperation(value = "根据专题id返回数据，第一次加载只返回1，2楼层数据信息")
    @PostMapping(value = "/headTopic")
    public BaseResponse<TopicResponse> storey(@RequestBody TopicQueryRequest request) {
        return topicService.detail(request,false);
    }



}
