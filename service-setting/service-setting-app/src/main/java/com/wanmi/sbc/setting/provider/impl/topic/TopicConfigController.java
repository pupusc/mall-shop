package com.wanmi.sbc.setting.provider.impl.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnGoodsResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyColumnResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreyContentResponse;
import com.wanmi.sbc.setting.api.response.TopicStoreySearchContentRequest;
import com.wanmi.sbc.setting.api.response.mixedcomponentV2.TopicStoreyMixedComponentResponse;
import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStorey;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreySearchContent;
import com.wanmi.sbc.setting.topicconfig.service.TopicConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public BaseResponse modifyTopic(TopicConfigModifyRequest request) {
        topicConfigService.modifyTopic(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse enableTopic(EnableTopicRequest request) {
        topicConfigService.enableTopic(request);
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
    public BaseResponse<List<TopicHeadImageDTO>> listHeadImage(TopicHeadImageQueryRequest request) {
        return BaseResponse.success(topicConfigService.listHeadImage(request));
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
    public BaseResponse modifyStorey(TopicStoreyModifyRequest request) {
        topicConfigService.modifyStorey(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<List<TopicStoreyDTO>> listStorey(TopicHeadImageQueryRequest request) {
        return BaseResponse.success(topicConfigService.listStorey(request));
    }

    public List<RankRequest> rank(RankStoreyRequest storeyRequest) {
        return topicConfigService.rank(storeyRequest);
    }

    @Override
    public BaseResponse<TopicActivityVO> detail(TopicQueryRequest request) {
        return BaseResponse.success(topicConfigService.detail(request.getTopicKey()));
    }

    @Override
    public BaseResponse enableStorey(EnableTopicStoreyRequest request) {
        topicConfigService.enableStorey(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addStoryContent(TopicStoreyContentAddRequest request) {
        topicConfigService.addStoreyContents(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<TopicStoreyContentResponse> listStoryContent(TopicStoreyContentQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopicStoreyContent(request));
    }

    @Override
    public BaseResponse modifyHeadImage(TopicHeadImageModifyRequest request) {
        topicConfigService.modifyHeadImage(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteStorey(Integer storeyId) {
        topicConfigService.deleteStorey(storeyId);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<MicroServicePage<TopicStoreyColumnDTO>> listStoryColumn(TopicStoreyColumnQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopicStoreyColumn(request));
    }

    @Override
    public BaseResponse addStoreyColumn(TopicStoreyColumnAddRequest request) {
        topicConfigService.addStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateStoreyColumn(TopicStoreyColumnUpdateRequest request) {
        topicConfigService.updateStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse enableStoreyColumn(EnableTopicStoreyColumnRequest request) {
        topicConfigService.enableStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<MicroServicePage<TopicStoreyColumnGoodsDTO>> listStoryColumnGoods(TopicStoreyColumnGoodsQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopicStoreyColumnGoods(request));
    }

    @Override
    public BaseResponse addStoreyColumnGoods(TopicStoreyColumnGoodsAddRequest request) {
        topicConfigService.addStoreyColumnGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateStoreyColumnGoods(TopicStoreyColumnGoodsUpdateRequest request) {
        topicConfigService.updateStoreyColumnGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse enableStoreyColumnGoods(EnableTopicStoreyColumnGoodsRequest request) {
        topicConfigService.enableStoreyColumnGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteStoreyColumnGoods(EnableTopicStoreyColumnGoodsRequest request) {
        topicConfigService.deleteStoreyColumnGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<TopicStoreyMixedComponentResponse> mixedComponentContent(MixedComponentQueryRequest request) {
        return BaseResponse.success(topicConfigService.getMixedComponent(request));
    }

    @Override
    public List<TopicStoreyDTO> getStoreyIdByType(Integer storeyType) {
        return topicConfigService.listTopicStoreyIdByType(storeyType);
    }

    @Override
    public List<TopicStoreyContentDTO> getContentByStoreyId(TopicStoreyContentRequest request) {
        return topicConfigService.listTopicStoreyContentByPage(request);
    }
}
