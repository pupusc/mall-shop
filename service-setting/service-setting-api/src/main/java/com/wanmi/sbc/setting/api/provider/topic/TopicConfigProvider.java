package com.wanmi.sbc.setting.api.provider.topic;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
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


    @PostMapping("/setting/${application.setting.version}/topic/detail")
    BaseResponse<TopicActivityVO> detail(@RequestBody TopicQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/list")
    BaseResponse<List<TopicStoreyDTO>> listStorey(@RequestBody TopicHeadImageQueryRequest request);


    @PostMapping("/setting/${application.setting.version}/topic/add/storey/content")
    BaseResponse addStoryContent(@RequestBody TopicStoreyContentAddRequest request);
}
