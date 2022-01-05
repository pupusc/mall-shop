package com.wanmi.sbc.vote;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "VoteController", description = "投票服务")
@RestController
@RequestMapping("/vote")
@Slf4j
public class VoteController {

    @Value("${vote.headimage}")
    private String headimage;
    /**
     * @description 投票图片
     * @menu 投票
     * @param
     * @status undone
     */
    @ApiOperation(value = "投票图片")
    @GetMapping(value = "image")
    public BaseResponse<String> images() {
        return BaseResponse.success(headimage);
    }


}
