package com.wanmi.sbc.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.request.AppointmentStockRequest;
import com.wanmi.sbc.setting.api.request.RankPageRequest;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentContentRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.dto.MixedComponentDto;
import com.wanmi.sbc.task.HomeIndexGoodsJobHandler;
import com.wanmi.sbc.task.NewRankJobHandler;
import com.wanmi.sbc.topic.response.RankPageRespones;
import com.wanmi.sbc.topic.response.TopicResponse;
import com.wanmi.sbc.topic.service.TopicService;
import com.wanmi.sbc.util.DitaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * @description 根据专题id返回页面数据_V2
     * @menu 专题
     * @param request
     * @status undone
     */
    @ApiOperation(value = "根据专题id返回页面数据，")
    @PostMapping(value = "/v2/detail")
    public BaseResponse<TopicResponse> detailV2(@RequestBody TopicQueryRequest request) {
        System.out.println("detailV2~begin:" + DitaUtil.getCurrentAllDate());
        BaseResponse<TopicResponse> response = topicService.detailV2(request,true);
        System.out.println("detailV2~  end:" + DitaUtil.getCurrentAllDate());
        return response;
    }

    @ApiOperation(value = "根据专题id返回数据，第一次加载只返回1，2楼层数据信息")
    @PostMapping(value = "/v2/headTopic")
    public BaseResponse<TopicResponse> storeyV2(@RequestBody TopicQueryRequest request) {
        BaseResponse<TopicResponse> response = topicService.detailV2(request,false);
        return response;
    }

    @Autowired
    private HomeIndexGoodsJobHandler homeIndexGoodsJobHandler;

    @Autowired
    private NewRankJobHandler rankJobHandler;
    @ApiOperation(value = "榜单聚合页")
    @PostMapping(value = "/v2/rankPage")
    public BaseResponse<RankPageRequest> rankPage(@RequestBody RankStoreyRequest request) throws Exception {
//        BaseResponse<RankPageRequest> response = topicService.rankPage(request);

//        homeIndexGoodsJobHandler.execute("H5,MINIPROGRAM");
//        rankJobHandler.execute(null);
        return BaseResponse.SUCCESSFUL();
    }

    @PostMapping(value = "/v2/getMixedComponentContent")
    public BaseResponse<List<MixedComponentDto>> getMixedComponentContent(@RequestBody MixedComponentContentRequest request) {
        List<MixedComponentDto> mixedComponentContent = topicService.getMixedComponentContent(request.getTopicStoreyId(), request.getTabId(), request.getKeyWord(), null, request.getPageNum(), request.getPageSize());
        return BaseResponse.success(mixedComponentContent);
    }

    @ApiOperation(value = "榜单聚合页")
    @PostMapping(value = "/v2/rankPageV2")
    public BaseResponse<RankPageRequest> rankPageV2(@RequestBody RankStoreyRequest request) {
        BaseResponse<RankPageRequest> response = BaseResponse.success(topicService.rankPageByBookList(request));
        return response;
    }

    /**
     * 添加到货提醒
     * @param request
     * @return
     */
    @PostMapping(value = "/addAppointment")
    public BaseResponse addAppointment(@RequestBody AppointmentStockRequest request){
        return topicService.addAppointment(request);
    }

    /**
     * 取消到货提醒
     * @param request
     * @return
     */
    @PostMapping(value = "/deleteAppointment")
    public BaseResponse deleteAppointment(@RequestBody AppointmentStockRequest request){
        return topicService.deleteAppointment(request);
    }

    /**
     * 获取用户所有到货提醒
     * @param request
     * @return
     */
    @PostMapping(value = "/findAppointment")
    public BaseResponse<AppointmentRequest> findAppointment(@RequestBody AppointmentStockRequest request){
        return topicService.findAppointment(request);
    }

}
