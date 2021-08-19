package com.wanmi.sbc.elastic.provider.impl.sensitivewords;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.sensitivewords.EsSensitiveWordsQueryProvider;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.response.sensitivewords.EsSensitiveWordsPageResponse;
import com.wanmi.sbc.elastic.sensitivewords.service.EsSensitiveWordsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/11 16:16
 * @description <p> </p>
 */
@RestController
public class EsSensitiveWordsQueryController implements EsSensitiveWordsQueryProvider {

    @Autowired
    private EsSensitiveWordsQueryService esSensitiveWordsQueryService;


    @Override
    public BaseResponse<EsSensitiveWordsPageResponse> page(@RequestBody @Valid EsSensitiveWordsQueryRequest request) {

        return esSensitiveWordsQueryService.page(request);
    }
}
