package com.wanmi.sbc.setting.api.provider.searchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.searchterms.*;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>搜索词APIProvider</p>
 *
 * @author weiwenhao
 * @date 2020-04-26
 */
@FeignClient(value = "${application.setting.name}", contextId = "SearchAssociationalWordProvider")
public interface SearchAssociationalWordProvider {

    /**
     * 新增搜索词
     *
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/add")
    BaseResponse<SearchAssociationalWordResponse> add(@RequestBody @Valid SearchAssociationalWordRequest request);

    /**
     * 修改搜索词
     *
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/modify")
    BaseResponse<SearchAssociationalWordResponse> modify(@RequestBody @Valid SearchAssociationalWordModifyRequest request);

    /**
     * 删除搜索词
     *
     * @param searchAssociationalWordId
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/delete_search_associational_word")
    BaseResponse deleteSearchAssociationalWord(@RequestBody @Valid SearchAssociationalWordDeleteByIdRequest searchAssociationalWordId);


    /**
     * 新增联想词
     *
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/add_associational_word")
    BaseResponse addAssociationalWord(@RequestBody @Valid AssociationLongTailWordRequest request);

    /**
     * 修改搜索词
     *
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/modify_associational_word")
    BaseResponse modifyAssociationalWord(@RequestBody @Valid AssociationLongTailWordModifyRequest request);

    /**
     * 联想词排序
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/sort_associational_word")
    BaseResponse sortAssociationalWord(@RequestBody @Valid List<AssociationLongTailWordSortRequest> request);

    /**
     * 删除联想词
     * @param associationLongTailWordId
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/delete_associational_long_tail_word")
    BaseResponse deleteAssociationLongTailWord(@RequestBody @Valid AssociationLongTailWordDeleteByIdRequest associationLongTailWordId);


}
