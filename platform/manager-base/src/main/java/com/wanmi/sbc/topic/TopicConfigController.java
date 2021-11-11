package com.wanmi.sbc.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.HeadImageConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyAddRequest;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @menu 专题
 * @tag topic
 * @status undone
 */
@Api(tags = "TopicConfigController", description = "专题设置")
@RestController
@RequestMapping("/topic/config")
public class TopicConfigController {


    @Autowired
    private TopicConfigProvider topicConfigProvider;

    /**
     * @description 新增专题
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "新增专题")
    @PostMapping(value = "/add")
    public BaseResponse addTopic(@RequestBody TopicConfigAddRequest request) {
        return topicConfigProvider.add(request);
    }

    /**
     * @description 新增专题
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "专题列表")
    @PostMapping("/page")
    public BaseResponse<MicroServicePage<TopicConfigVO>> page(TopicQueryRequest request){
        return topicConfigProvider.page(request);
    }

    /**
     * @description 新增头图
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("新增头图")
    @PostMapping("/add/headimage")
    public BaseResponse addHeadImage(@RequestParam HeadImageConfigAddRequest request){
        return topicConfigProvider.addHeadImage(request);
    }

    /**
     * @description 新增楼层
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("新增楼层")
    @PostMapping("/add/storey")
    public BaseResponse addStorey(@RequestParam TopicStoreyAddRequest request){
        return  topicConfigProvider.addStorey(request);
    }
}
