package com.wanmi.sbc.searchterms.searchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.searchterms.EsSearchAssociationalWordProvider;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordRequest;
import com.wanmi.sbc.elastic.bean.vo.searchterms.EsSearchAssociationalWordVO;
import com.wanmi.sbc.setting.api.provider.searchterms.SearchAssociationalWordProvider;
import com.wanmi.sbc.setting.api.request.searchterms.*;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordResponse;
import com.wanmi.sbc.setting.bean.vo.SearchAssociationalWordVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>搜索词APIProvider</p>
 *
 * @author weiwenhao
 * @date 2020-04-20
 */
@RestController
@ApiModel
@Api(tags = "SearchAssociationalWordController", description = "搜索词增删改服务API")
@RequestMapping("/search_associational_word")
public class SearchAssociationalWordController {

    @Autowired
    private SearchAssociationalWordProvider searchAssociationalWordProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsSearchAssociationalWordProvider esSearchAssociationalWordProvider;

    /**
     * 新增搜索词
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "新增搜索词")
    @PostMapping("/add")
    BaseResponse<SearchAssociationalWordResponse> add(@RequestBody @Valid SearchAssociationalWordRequest request) {
        request.setCreatePerson(commonUtil.getOperatorId());
        BaseResponse<SearchAssociationalWordResponse> response = searchAssociationalWordProvider.add(request);
        operateLogMQUtil.convertAndSend("搜索词", "新增搜索词", "新增搜索词：" + request.getSearchTerms());
        return this.addAssociationalWord(response);
    }

    /**
     * 修改搜索词
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改搜索词")
    @PostMapping("/modify")
    BaseResponse<SearchAssociationalWordResponse> modify(@RequestBody @Valid SearchAssociationalWordModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("搜索词", "修改搜索词", "修改搜索词：" + request.getSearchTerms());

        BaseResponse<SearchAssociationalWordResponse> response = searchAssociationalWordProvider.modify(request);
        return this.addAssociationalWord(response);
    }

    /**
     * 删除搜索词
     *
     * @param searchAssociationalWordId
     * @return
     */
    @ApiOperation(value = "删除搜索词")
    @PostMapping("/delete_search_associational_word")
    BaseResponse deleteSearchAssociationalWord(@RequestBody @Valid SearchAssociationalWordDeleteByIdRequest searchAssociationalWordId) {
        operateLogMQUtil.convertAndSend("搜索词", "删除搜索词", "删除搜索词：" + searchAssociationalWordId.getId());
        //删除es搜索词
        esSearchAssociationalWordProvider.deleteById(searchAssociationalWordId.getId());
        return searchAssociationalWordProvider.deleteSearchAssociationalWord(searchAssociationalWordId);
    }


    /**
     * 新增联想词
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "新增联想词")
    @PostMapping("/add_associational_word")
    BaseResponse addAssociationalWord(@RequestBody @Valid AssociationLongTailWordRequest request) {
        request.setDeletePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("搜索词&联想词", "新增联想词", "新增联想词：" + request.getAssociationalWord());
        return searchAssociationalWordProvider.addAssociationalWord(request);
    }

    /**
     * 修改联想词
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改联想词")
    @PostMapping("/modify_associational_word")
    BaseResponse modifyAssociationalWord(@RequestBody @Valid AssociationLongTailWordModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("搜索词&联想词", "修改联想词", "修改联想词：" + request.getAssociationLongTailWordId());
        return searchAssociationalWordProvider.modifyAssociationalWord(request);
    }

    /**
     * 联想词排序
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "联想词排序")
    @PostMapping("/sort_associational_word")
    BaseResponse sortAssociationalWord(@RequestBody @Valid List<AssociationLongTailWordSortRequest> request) {
        operateLogMQUtil.convertAndSend("搜索词&联想词", "联想词排序", "联想词排序");
        return searchAssociationalWordProvider.sortAssociationalWord(request);
    }

    /**
     * 删除联想词
     *
     * @param associationLongTailWordId
     * @return
     */
    @ApiOperation(value = "删除联想词")
    @PostMapping("/delete_associational_long_tail_word")
    BaseResponse deleteAssociationLongTailWord(@RequestBody @Valid AssociationLongTailWordDeleteByIdRequest associationLongTailWordId) {
        operateLogMQUtil.convertAndSend("搜索词&联想词", "删除联想词",
                "删除联想词：" + associationLongTailWordId.getAssociationLongTailWordId());
        return searchAssociationalWordProvider.deleteAssociationLongTailWord(associationLongTailWordId);
    }

    /**
     * 数据同步到es
     * @param response
     * @return
     */
    private BaseResponse<SearchAssociationalWordResponse> addAssociationalWord(BaseResponse<SearchAssociationalWordResponse> response) {
        SearchAssociationalWordResponse wordResponse = response.getContext();
        //数据同步到es
        SearchAssociationalWordVO wordVO = wordResponse.getSearchAssociationalWordVO();
        if (Objects.nonNull(wordVO)) {
            EsSearchAssociationalWordVO newVo = KsBeanUtil.convert(wordVO, EsSearchAssociationalWordVO.class);
            EsSearchAssociationalWordRequest wordRequest = EsSearchAssociationalWordRequest.builder()
                    .searchAssociationalWordVOList(Collections.singletonList(newVo))
                    .build();
            esSearchAssociationalWordProvider.add(wordRequest);
        }
        return response;
    }

}
