package com.wanmi.sbc.setting.api.provider.defaultsearchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.DefaultSearchTermsListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: 默认搜索词
 * @Author zh
 * @Date 2023/2/4 11:14
 */
@FeignClient(value = "${application.setting.name}",contextId = "DefaultSearchTermsQueryProvider")
public interface DefaultSearchTermsQueryProvider {
    /**
     * 默认搜索词
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/default_search_terms/v2/list")
    BaseResponse<List<DefaultSearchTermsListResponse>> listDefaultSearchTerms(@RequestParam(value = "defaultChannel", required = false) Integer defaultChannel);
}
