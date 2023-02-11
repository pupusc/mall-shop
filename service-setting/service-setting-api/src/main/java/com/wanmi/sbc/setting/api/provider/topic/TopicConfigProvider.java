package com.wanmi.sbc.setting.api.provider.topic;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnGoodsResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyContentResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreySearchContentRequest;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "${application.setting.name}", contextId = "TopicConfigProvider")
public interface TopicConfigProvider {

    /**
     * 新增专题
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/topic/add")
    BaseResponse add(@RequestBody TopicConfigAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/modify")
    BaseResponse modifyTopic(@RequestBody TopicConfigModifyRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/enable")
    BaseResponse enableTopic(@RequestBody EnableTopicRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/page")
    BaseResponse<MicroServicePage<TopicConfigVO>> page(@RequestBody TopicQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/add/headimage")
    BaseResponse addHeadImage(@RequestBody HeadImageConfigAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/headimage/list")
    BaseResponse<List<TopicHeadImageDTO>> listHeadImage(@RequestBody TopicHeadImageQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/delete/headimage")
    BaseResponse deleteHeadImage(@RequestParam("id") Integer id);

    @PostMapping("/setting/${application.setting.version}/topic/add/storey")
    BaseResponse addStorey(@RequestBody TopicStoreyAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/enable/storey")
    BaseResponse enableStorey(@RequestBody EnableTopicStoreyRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/modify/storey")
    BaseResponse modifyStorey(@RequestBody TopicStoreyModifyRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/detail")
    BaseResponse<TopicActivityVO> detail(@RequestBody TopicQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/list")
    BaseResponse<List<TopicStoreyDTO>> listStorey(@RequestBody TopicHeadImageQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/rank")
    List<RankRequest> rank(@RequestBody RankStoreyRequest storeyRequest);


    @PostMapping("/setting/${application.setting.version}/topic/add/storey/content")
    BaseResponse addStoryContent(@RequestBody TopicStoreyContentAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/content/list")
    BaseResponse<TopicStoreyContentResponse> listStoryContent(@RequestBody TopicStoreyContentQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/modify/headimage")
    BaseResponse modifyHeadImage(@RequestBody TopicHeadImageModifyRequest request);


    @PostMapping("/setting/${application.setting.version}/topic/delete/storey")
    BaseResponse deleteStorey(@RequestParam("storeyId") Integer storeyId);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/list")
    BaseResponse<MicroServicePage<TopicStoreyColumnDTO>> listStoryColumn(@RequestBody TopicStoreyColumnQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/add")
    BaseResponse addStoreyColumn(@RequestBody TopicStoreyColumnAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/update")
    BaseResponse updateStoreyColumn(@RequestBody TopicStoreyColumnUpdateRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/enable")
    BaseResponse enableStoreyColumn(@RequestBody EnableTopicStoreyColumnRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/list")
    BaseResponse<MicroServicePage<TopicStoreyColumnGoodsDTO>> listStoryColumnGoods(@RequestBody TopicStoreyColumnGoodsQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/add")
    BaseResponse addStoreyColumnGoods(@RequestBody TopicStoreyColumnGoodsAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/update")
    BaseResponse updateStoreyColumnGoods(@RequestBody TopicStoreyColumnGoodsUpdateRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/enable")
    BaseResponse enableStoreyColumnGoods(@RequestBody EnableTopicStoreyColumnGoodsRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/delete")
    BaseResponse deleteStoreyColumnGoods(@RequestBody EnableTopicStoreyColumnGoodsRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/getStoreyIdByType")
    List<TopicStoreyDTO> getStoreyIdByType(@RequestBody Integer storeyType);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/getContentByStoreyId")
    List<TopicStoreyContentDTO> getContentByStoreyId(@RequestBody TopicStoreyContentRequest request);
}
