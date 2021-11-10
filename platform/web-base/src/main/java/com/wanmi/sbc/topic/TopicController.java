package com.wanmi.sbc.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderPageRequest;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    /**
     * @description 根据专题id返回页面数据
     * @menu 专题
     * @param request
     * @status undone
     */
    @Autowired
    private TopicConfigProvider topicConfigProvider;
    @ApiOperation(value = "根据专题id返回页面数据")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public BaseResponse<TopicActivityVO> detail(@RequestBody TopicQueryRequest request) {
        return topicConfigProvider.detail(request);
    }


}
