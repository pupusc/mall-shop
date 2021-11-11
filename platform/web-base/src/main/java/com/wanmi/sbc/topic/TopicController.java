package com.wanmi.sbc.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
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
    private TopicConfigProvider topicConfigProvider;

    /**
     * @description 根据专题id返回页面数据
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "根据专题id返回页面数据")
    @PostMapping(value = "detail")
    public BaseResponse<TopicActivityVO> detail(@RequestBody TopicQueryRequest request) {
        return topicConfigProvider.detail(request);
    }




}
