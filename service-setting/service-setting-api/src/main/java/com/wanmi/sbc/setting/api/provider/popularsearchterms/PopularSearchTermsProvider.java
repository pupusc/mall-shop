package com.wanmi.sbc.setting.api.provider.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsDeleteRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsModifyRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsSortRequest;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsDeleteResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;


/**
 * <p>热门搜索词Provider</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@FeignClient(value = "${application.setting.name}",contextId = "PopularSearchTermsProvider")
public interface PopularSearchTermsProvider {


    /**
     * 新增热门搜索词
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popular_search_terms/add")
    BaseResponse<PopularSearchTermsResponse>add(@RequestBody @Valid PopularSearchTermsRequest request);

    /**
     * 删除热门搜索词
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popular_search_terms/delete")
    BaseResponse<PopularSearchTermsDeleteResponse> deleteById(@RequestBody @Valid PopularSearchTermsDeleteRequest request);

    /**
     * 修改热门信息
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popular_search_terms/modify")
    BaseResponse<PopularSearchTermsDeleteResponse> modify(@RequestBody @Valid PopularSearchTermsModifyRequest request);


    /**
     * 联想词排序
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popular_search_terms/sort_popular_search_terms")
    BaseResponse sortPopularSearchTerms(@RequestBody @Valid List<PopularSearchTermsSortRequest> request);

}
