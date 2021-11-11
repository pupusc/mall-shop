package com.wanmi.sbc.setting.provider.impl.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.HeadImageConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyAddRequest;
import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.service.TopicConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicConfigController implements TopicConfigProvider {

    @Autowired
    private TopicConfigService topicConfigService;

    @Override
    public BaseResponse add(TopicConfigAddRequest request) {
         topicConfigService.addTopic(request);
         return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<MicroServicePage<TopicConfigVO>> page(TopicQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopic(request));
    }


    @Override
    public BaseResponse addHeadImage(HeadImageConfigAddRequest request) {
        topicConfigService.addHeadImage(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteHeadImage(Integer id) {
        topicConfigService.deleteHeadImage(id);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addStorey(TopicStoreyAddRequest request) {
        topicConfigService.addStorey(request);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse<TopicActivityVO> detail(TopicQueryRequest request) {
        return BaseResponse.success(topicConfigService.detail(request.getTopicKey()));
    }
}
