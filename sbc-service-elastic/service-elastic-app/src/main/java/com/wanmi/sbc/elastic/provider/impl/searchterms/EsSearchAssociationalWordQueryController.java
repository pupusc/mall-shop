package com.wanmi.sbc.elastic.provider.impl.searchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.searchterms.EsSearchAssociationalWordQueryProvider;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordPageRequest;
import com.wanmi.sbc.elastic.api.response.searchterms.EsSearchAssociationalWordPageResponse;
import com.wanmi.sbc.elastic.searchterms.service.EsSearchAssociationalWordQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/17 14:31
 * @description <p> </p>
 */
@RestController
public class EsSearchAssociationalWordQueryController implements EsSearchAssociationalWordQueryProvider {

    @Autowired
    private EsSearchAssociationalWordQueryService esSearchAssociationalWordQueryService;

    @Override
    public BaseResponse<EsSearchAssociationalWordPageResponse> page(@Valid EsSearchAssociationalWordPageRequest request) {

        return esSearchAssociationalWordQueryService.page(request);
    }
}
