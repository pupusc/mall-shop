package com.wanmi.sbc.elastic.provider.impl.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.systemresource.EsSystemResourceQueryProvider;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.elastic.api.response.systemresource.EsSystemRessourcePageResponse;
import com.wanmi.sbc.elastic.systemresource.service.EsSystemResourceQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/14 11:25
 * @description <p> </p>
 */
@RestController
public class EsSystemResourceQueryController implements EsSystemResourceQueryProvider {

    @Autowired
    private EsSystemResourceQueryService esSystemResourceQueryService;

    @Override
    public BaseResponse<EsSystemRessourcePageResponse> page(@RequestBody @Valid EsSystemResourcePageRequest request) {

        return esSystemResourceQueryService.page(request);
    }
}
