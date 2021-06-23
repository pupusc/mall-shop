package com.wanmi.sbc.elastic.api.provider.searchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordPageRequest;
import com.wanmi.sbc.elastic.api.response.searchterms.EsSearchAssociationalWordPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/17 14:28
 * @description <p> </p>
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSearchAssociationalWordQueryProvider")
public interface EsSearchAssociationalWordQueryProvider {

    /**
     * 搜索词列表查询
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/search_associational_word/page")
    BaseResponse<EsSearchAssociationalWordPageResponse> page(@RequestBody @Valid EsSearchAssociationalWordPageRequest request);
}
