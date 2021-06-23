package com.wanmi.sbc.elastic.provider.impl.searchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.searchterms.EsSearchAssociationalWordProvider;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordPageRequest;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordRequest;
import com.wanmi.sbc.elastic.searchterms.service.EsSearchAssociationalWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/17 15:35
 * @description <p> </p>
 */
@RestController
public class EsSearchAssociationalWordController implements EsSearchAssociationalWordProvider {

    @Autowired
    private EsSearchAssociationalWordService esSearchAssociationalWordService;

    @Override
    public BaseResponse add(@Valid EsSearchAssociationalWordRequest request) {
        esSearchAssociationalWordService.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(Long id) {

        esSearchAssociationalWordService.deleteById(id);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse init(@Valid EsSearchAssociationalWordPageRequest request) {

        esSearchAssociationalWordService.init(request);
        return BaseResponse.SUCCESSFUL();
    }
}
