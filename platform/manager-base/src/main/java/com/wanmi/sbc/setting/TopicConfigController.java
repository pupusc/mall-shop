package com.wanmi.sbc.setting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnGoodsResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyContentResponse;
import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnGoodsDTO;
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
    public BaseResponse<MicroServicePage<TopicConfigVO>> page(@RequestBody  TopicQueryRequest request){
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
    public  BaseResponse<TopicStoreyContentResponse> listStoryContent(@RequestBody TopicStoreyContentQueryRequest request){
        return topicConfigProvider.listStoryContent(request);
    }

    /**
     * @description 编辑专题
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "编辑专题")
    @PostMapping(value = "/modify")
    public BaseResponse modifyTopic(@RequestBody TopicConfigModifyRequest request) {
        return topicConfigProvider.modifyTopic(request);
    }

    /**
     * @description 启用/禁用专题
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "启用/禁用专题")
    @PostMapping(value = "/enable")
    public BaseResponse enableTopic(@RequestBody EnableTopicRequest request) {
        return topicConfigProvider.enableTopic(request);
    }

    /**
     * @description 编辑楼层
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "编辑楼层")
    @PostMapping(value = "/modify/storey")
    public BaseResponse modifyTopic(@RequestBody TopicStoreyModifyRequest request) {
        return topicConfigProvider.modifyStorey(request);
    }

    /**
     * @description 删除楼层
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("删除楼层")
    @PostMapping("/delete/storey")
    public BaseResponse deleteStorey(@RequestParam("storeyId") Integer storeyId){
        return  topicConfigProvider.deleteStorey(storeyId);
    }

    /**
     * @description 楼层栏目列表
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目列表")
    @PostMapping("/storey/v2/column/list")
    public BaseResponse<MicroServicePage<TopicStoreyColumnDTO>> listStoryColumn(@RequestBody TopicStoreyColumnQueryRequest request){
        return topicConfigProvider.listStoryColumn(request);
    }

    /**
     * @description 楼层栏目添加
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目添加")
    @PostMapping("/storey/v2/column/add")
    public BaseResponse addStoryColumn(@RequestBody TopicStoreyColumnAddRequest request){
        return topicConfigProvider.addStoreyColumn(request);
    }

    /**
     * @description 楼层栏目修改
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目添加")
    @PostMapping("/storey/v2/column/update")
    public BaseResponse updateStoryColumn(@RequestBody TopicStoreyColumnUpdateRequest request){
        return topicConfigProvider.updateStoreyColumn(request);
    }

    /**
     * @description 楼层栏目启用/禁用
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目启用/禁用")
    @PostMapping("/storey/v2/column/enable")
    public BaseResponse enableStoryColumn(@RequestBody EnableTopicStoreyColumnRequest request){
        return topicConfigProvider.enableStoreyColumn(request);
    }



    /**
     * @description 楼层栏目商品列表
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目商品列表")
    @PostMapping("/storey/v2/column/goods/list")
    public BaseResponse<MicroServicePage<TopicStoreyColumnGoodsDTO>> listStoryColumnGoods(@RequestBody TopicStoreyColumnGoodsQueryRequest request){
        return topicConfigProvider.listStoryColumnGoods(request);
    }

    /**
     * @description 楼层栏目商品列表添加
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目商品添加")
    @PostMapping("/storey/v2/column/goods/add")
    public BaseResponse addStoryColumnGoods(@RequestBody TopicStoreyColumnGoodsAddRequest request){
        return topicConfigProvider.addStoreyColumnGoods(request);
    }

    /**
     * @description 楼层栏目商品修改
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目商品修改")
    @PostMapping("/storey/v2/column/goods/update")
    public BaseResponse updateStoryColumnGoods(@RequestBody TopicStoreyColumnGoodsUpdateRequest request){
        return topicConfigProvider.updateStoreyColumnGoods(request);
    }

    /**
     * @description 楼层栏目商品启用/禁用
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目商品启用/禁用")
    @PostMapping("/storey/v2/column/goods/enable")
    public BaseResponse enableStoryColumnGoods(@RequestBody EnableTopicStoreyColumnGoodsRequest request){
        return topicConfigProvider.enableStoreyColumnGoods(request);
    }

    /**
     * @description 楼层栏目商品删除
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation("楼层栏目商品删除")
    @PostMapping("/storey/v2/column/goods/delete")
    public BaseResponse deleteStoryColumnGoods(@RequestBody EnableTopicStoreyColumnGoodsRequest request){
        return topicConfigProvider.deleteStoreyColumnGoods(request);
    }


}
