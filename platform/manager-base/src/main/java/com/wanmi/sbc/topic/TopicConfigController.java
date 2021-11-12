package com.wanmi.sbc.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
     * @description 头图列表
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("头图列表")
    @PostMapping("/headimage/list")
    public  BaseResponse<List<TopicHeadImageDTO>> listHeadImage(@RequestBody TopicHeadImageQueryRequest request){
        return  topicConfigProvider.listHeadImage(request);
    }


    /**
     * @description 新增头图
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("新增头图")
    @PostMapping("/add/headimage")
    public BaseResponse addHeadImage(@RequestBody HeadImageConfigAddRequest request){
        return topicConfigProvider.addHeadImage(request);
    }

    /**
     * @description 编辑头图
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("编辑头图")
    @PostMapping("/modify/headimage")
    public BaseResponse modifyHeadImage(@RequestBody TopicHeadImageModifyRequest request){
        return  topicConfigProvider.modifyHeadImage(request);
    }

    /**
     * @description 删除头图
     * @menu 专题
     * @param id
     * @status undone
     */
    @ApiOperation("删除头图")
    @PostMapping("/delete/headimage")
    public BaseResponse deleteHeadImage(@RequestParam("id") Integer id){
        return  topicConfigProvider.deleteHeadImage(id);
    }

    /**
     * @description 新增楼层
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("新增楼层")
    @PostMapping("/add/storey")
    public BaseResponse addStorey(@RequestBody TopicStoreyAddRequest request){
        return  topicConfigProvider.addStorey(request);
    }

    /**
     * @description 楼层列表
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层列表")
    @PostMapping("/storey/list")
    public BaseResponse<List<TopicStoreyDTO>> listStorey(@RequestBody TopicHeadImageQueryRequest request) {
        return topicConfigProvider.listStorey(request);
    }

    /**
     * @description 启用或禁用楼层
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("启用或禁用楼层")
    @PostMapping("/enable/storey")
    public BaseResponse enableStorey(@RequestBody EnableTopicStoreyRequest request){
        return topicConfigProvider.enableStorey(request);
    }

    /**
     * @description 新增楼层内容
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("新增楼层内容")
    @PostMapping("/add/storey/content")
    public  BaseResponse addStoryContent(@RequestBody TopicStoreyContentAddRequest request){
        return topicConfigProvider.addStoryContent(request);
    }

    /**
     * @description 楼层内容列表
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层内容列表")
    @PostMapping("/storey/content/list")
    public  BaseResponse<List<TopicStoreyContentDTO>> listStoryContent(@RequestBody TopicStoreyContentQueryRequest request){
        return topicConfigProvider.listStoryContent(request);
    }
}
