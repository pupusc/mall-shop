package com.wanmi.sbc.elastic.provider.impl.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityQueryProvider;
import com.wanmi.sbc.elastic.api.request.groupon.EsGrouponActivityPageRequest;
import com.wanmi.sbc.elastic.api.response.groupon.EsGrouponActivityPageResponse;
import com.wanmi.sbc.elastic.groupon.service.EsGrouponActivityQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: HouShuai
 * @date: 2020/12/8 10:18
 * @description:
 */
@RestController
public class EsGrouponActivityQueryController implements EsGrouponActivityQueryProvider {

    @Autowired
    private EsGrouponActivityQueryService esGrouponActivityQueryService;

    @Override
    public BaseResponse<EsGrouponActivityPageResponse> page(@RequestBody @Valid EsGrouponActivityPageRequest request) {
        return esGrouponActivityQueryService.page(request);
    }
}
