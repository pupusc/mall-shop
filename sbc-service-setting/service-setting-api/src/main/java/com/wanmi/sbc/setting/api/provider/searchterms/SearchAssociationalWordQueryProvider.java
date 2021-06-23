package com.wanmi.sbc.setting.api.provider.searchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.searchterms.AssociationLongTailWordByIdsRequest;
import com.wanmi.sbc.setting.api.request.searchterms.AssociationLongTailWordLikeRequest;
import com.wanmi.sbc.setting.api.request.searchterms.SearchAssociationalWordPageRequest;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordByIdsResponse;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordLikeResponse;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>搜索词APIProvider</p>
 *
 * @author weiwenhao
 * @date 2020-04-26
 */
@FeignClient(value = "${application.setting.name}", contextId = "SearchAssociationalWordQueryProvider")
public interface SearchAssociationalWordQueryProvider {

    /**
     * 搜索词列表查询
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/page")
    BaseResponse<SearchAssociationalWordPageResponse> page(@RequestBody @Valid SearchAssociationalWordPageRequest request);

    /**
     * 搜索词列表查询
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/like_associational_word")
    BaseResponse<AssociationLongTailWordLikeResponse> likeAssociationalWord(@RequestBody @Valid AssociationLongTailWordLikeRequest request);

    /**
     * 根据关联id查询
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/search_associational_word/list-by-ids")
    BaseResponse<AssociationLongTailWordByIdsResponse> listByIds(@RequestBody @Valid AssociationLongTailWordByIdsRequest request);



}
