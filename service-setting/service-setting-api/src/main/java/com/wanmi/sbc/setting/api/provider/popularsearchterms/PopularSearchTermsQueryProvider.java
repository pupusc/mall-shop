package com.wanmi.sbc.setting.api.provider.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * <p>热门搜索词Provider</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@FeignClient(value = "${application.setting.name}",contextId = "PopularSearchTermsQueryProvider")
public interface PopularSearchTermsQueryProvider {


    /**
     * 新增热门搜索词
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/popular_search_terms/list")
    BaseResponse<PopularSearchTermsListResponse>listPopularSearchTerms();

}
