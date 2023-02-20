package com.wanmi.sbc.setting.provider.impl.topic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.RankRequestListResponse;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.*;
import com.wanmi.sbc.setting.api.response.mixedcomponentV2.TopicStoreyMixedComponentResponse;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
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

    public RankRequestListResponse rank(RankStoreyRequest storeyRequest) {
        return topicConfigService.rank(storeyRequest);
    }

    public RankPageResponse rankPage(RankStoreyRequest storeyRequest){
        return topicConfigService.rankPage(storeyRequest);
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
    public BaseResponse<MicroServicePage<RankListDTO>> listRankList(TopicStoreyColumnQueryRequest request) {
        return BaseResponse.success(topicConfigService.listRankList(request));
    }

    @Override
    public BaseResponse addStoreyColumn(TopicStoreyColumnAddRequest request) {
        topicConfigService.addStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addRankLevel(RankLevelAddRequest request) {
        topicConfigService.addRankLevel(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateRankLevel(RankLevelUpdateRequest request) {
        topicConfigService.updateRankLevel(request);
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
    public BaseResponse<List<TopicStoreyColumnGoodsDTO>> listStoryColumnGoodsByIdAndSpu(TopicStoreyColumnGoodsQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopicStoreyColumnGoodsByIdAndSpu(request));
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
    public List<TopicStoreyDTO> getStoreyIdByType(Integer storeyType) {
        return topicConfigService.listTopicStoreyIdByType(storeyType);
    }

    @Override
    public List<TopicStoreyContentDTO> getContentByStoreyId(TopicStoreyContentRequest request) {
        return topicConfigService.listTopicStoreyContentByPage(request);
    }

    /**
     * @Description 混合标签tab列表
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse<MicroServicePage<MixedComponentTabDto>> listMixedComponentTab(MixedComponentTabQueryRequest request) {
        return BaseResponse.success(topicConfigService.listMixedComponentTab(request));
    }

    /**
     * @Description 混合标签tab添加
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse addMixedComponentTab(MixedComponentTabAddRequest request) {
        topicConfigService.addMixedComponentTab(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column表list
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse<MicroServicePage<ColumnDTO>> listTopicStoreyColumn(ColumnQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopicStoreyColumn(request));
    }

    /**
     * @Description topic_storey_column表add
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse addTopicStoreyColumn(ColumnAddRequest request) {
        topicConfigService.addTopicStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column表更新
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse updateTopicStoreyColumn(ColumnUpdateRequest request) {
        topicConfigService.updateTopicStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column表状态修改
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse enableTopicStoreyColumn(ColumnEnableRequest request) {
        topicConfigService.enableTopicStoreyColumn(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column表删除
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse deleteTopicStoreyColumn(Integer id) {
        topicConfigService.deleteTopicStoreyColumn(id);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column表根据id获取
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse<ColumnDTO> getTopicStoreyColumnById(Integer id) {
        return BaseResponse.success(topicConfigService.getTopicStoreyColumnById(id));
    }

    /**
     * @Description topic_storey_column_content表list
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse<MicroServicePage<ColumnContentDTO>> listTopicStoreyColumnContent(ColumnContentQueryRequest request) {
        return BaseResponse.success(topicConfigService.listTopicStoreyColumnContent(request));
    }

    /**
     * @Description topic_storey_column_content表add
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse addTopicStoreyColumnContent(ColumnContentAddRequest request) {
        topicConfigService.addTopicStoreyColumnContent(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column_content表更新
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse updateTopicStoreyColumnContent(ColumnContentUpdateRequest request) {
        topicConfigService.updateTopicStoreyColumnContent(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column_content表状态修改
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse enableTopicStoreyColumnContent(ColumnContentEnableRequest request) {
        topicConfigService.enableTopicStoreyColumnContent(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column_content表删除
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse deleteTopicStoreyColumnContent(Integer id) {
        topicConfigService.deleteTopicStoreyColumnContent(id);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @Description topic_storey_column_content表根据id获取
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @Override
    public BaseResponse<ColumnContentDTO> getTopicStoreyColumnContentById(Integer id) {
        return BaseResponse.success(topicConfigService.getTopicStoreyColumnContentById(id));
    }
}
