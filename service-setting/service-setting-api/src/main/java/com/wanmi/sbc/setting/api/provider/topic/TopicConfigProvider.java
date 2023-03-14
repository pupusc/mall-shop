package com.wanmi.sbc.setting.api.provider.topic;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.request.RankRelResponse;
import com.wanmi.sbc.setting.api.request.RankRequestListResponse;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.*;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping("/setting/${application.setting.version}/topic/getAllRankRel")
    RankRelResponse getAllRankRel();

    @PostMapping("/setting/${application.setting.version}/topic/enable/storey")
    BaseResponse enableStorey(@RequestBody EnableTopicStoreyRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/modify/storey")
    BaseResponse modifyStorey(@RequestBody TopicStoreyModifyRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/detail")
    BaseResponse<TopicActivityVO> detail(@RequestBody TopicQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/list")
    BaseResponse<List<TopicStoreyDTO>> listStorey(@RequestBody TopicHeadImageQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/rank")
    RankRequestListResponse rank();

    @PostMapping("/setting/${application.setting.version}/topic/storey/rankPageByBookList")
    RankPageResponse rankPageByBookList(@RequestBody RankStoreyRequest storeyRequest);

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

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/listAll")
    List<TopicStoreyColumnDTO> listStoryColumnAll(@RequestBody TopicStoreyColumnQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/rank/list")
    BaseResponse<MicroServicePage<RankListDTO>> listRankList(@RequestBody TopicStoreyColumnQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/add")
    BaseResponse addStoreyColumn(@RequestBody TopicStoreyColumnAddRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/update")
    BaseResponse updateStoreyColumn(@RequestBody TopicStoreyColumnUpdateRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/enable")
    BaseResponse enableStoreyColumn(@RequestBody EnableTopicStoreyColumnRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/list")
    BaseResponse<MicroServicePage<TopicStoreyColumnGoodsDTO>> listStoryColumnGoods(@RequestBody TopicStoreyColumnGoodsQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/column/goods/listByIdAndSpu")
    BaseResponse<List<TopicStoreyColumnGoodsDTO>> listStoryColumnGoodsByIdAndSpu(@RequestBody TopicStoreyColumnGoodsQueryRequest request);

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

    /**
     * @Description 榜单分类添加
     * @Author
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/rank/level/add")
    BaseResponse addRankLevel(@RequestBody RankLevelAddRequest request);

    /**
     * @Description 二级榜单添加
     * @Author
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/rank/relation/add")
    BaseResponse addRankrelation(@RequestBody TopicRalationRequest request);

    /**
     * @Description 二级榜单更新
     * @Author
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/rank/relation/update")
    BaseResponse updateRankrelation(@RequestBody TopicRalationRequest request);

    /**
     * @Description 二级榜单删除
     * @Author
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/rank/relation/delete")
    BaseResponse deleteRankrelation(@RequestBody TopicRalationRequest request);

    /**
     * @Description 榜单分类添加
     * @Author
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/rank/level/update")
    BaseResponse updateRankLevel(@RequestBody RankLevelUpdateRequest request);

    /**
     * @Description 混合标签tab分页
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/tag/page")
    BaseResponse<MicroServicePage<MixedComponentTabDto>> pageMixedComponentTab(@RequestBody MixedComponentTabQueryRequest request);


    /**
     * @Description 混合标签tab列表
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/tab/list")
    BaseResponse<List<MixedComponentTabDto>> listMixedComponentTab(@RequestBody MixedComponentTabQueryRequest request);

    /**
     * @Description 商品池add
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnGoods/add")
    BaseResponse addTopicStoreyColumnGoods(@RequestBody MixedComponentGoodsAddRequest request);

    /**
     * @Description 商品池详情
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/getGoodsPool")
    BaseResponse<MixedComponentGoodsDto> getGoodsPool(@RequestBody MixedComponentTabQueryRequest request);

    /**
     * @Description topic_storey_column表list
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumn/list")
    BaseResponse<MicroServicePage<ColumnDTO>> listTopicStoreyColumn(@RequestBody ColumnQueryRequest request);

    /**
     * @Description topic_storey_column表add
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumn/add")
    BaseResponse addTopicStoreyColumn(@RequestBody ColumnAddRequest request);

    /**
     * @Description topic_storey_column表update
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumn/update")
    BaseResponse updateTopicStoreyColumn(@RequestBody ColumnUpdateRequest request);

    /**
     * @Description topic_storey_column表状态更新
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumn/enable")
    BaseResponse enableTopicStoreyColumn(@RequestBody ColumnEnableRequest request);

    /**
     * @Description topic_storey_column表删除
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumn/delete")
    BaseResponse deleteTopicStoreyColumn(@RequestParam("id") Integer id);

    /**
     * @Description topic_storey_column表根据id获取
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumn/getById")
    BaseResponse<ColumnDTO> getTopicStoreyColumnById(@RequestParam("id") Integer id);

    /**
     * @Description topic_storey_column_content表page
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/list")
    BaseResponse<MicroServicePage<ColumnContentDTO>> pageTopicStoreyColumnContent(@RequestBody ColumnContentQueryRequest request);


    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/AllList")
    BaseResponse<List<ColumnContentDTO>> ListTopicStoreyColumnContent(@RequestBody ColumnContentQueryRequest request);

    /**
     * @Description topic_storey_column_content表add
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/add")
    BaseResponse addTopicStoreyColumnContent(@RequestBody ColumnContentAddRequest request);

    /**
     * @Description topic_storey_column_content表更新
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/update")
    BaseResponse updateTopicStoreyColumnContent(@RequestBody ColumnContentUpdateRequest request);

    /**
     * @Description topic_storey_column_content表状态修改
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/enable")
    BaseResponse enableTopicStoreyColumnContent(@RequestBody ColumnContentEnableRequest request);

    /**
     * @Description topic_storey_column_content表删除
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/delete")
    BaseResponse deleteTopicStoreyColumnContent(@RequestParam("id") Integer id);

    /**
     * @Description topic_storey_column_content表根据id获取
     * @Author zh
     * @Date  2023/2/18 11:50
     */
    @PostMapping("/setting/${application.setting.version}/topic/storey/v2/topicStoreyColumnContent/getById")
    BaseResponse<ColumnContentDTO> getTopicStoreyColumnContentById(@RequestParam("id") Integer id);

}
