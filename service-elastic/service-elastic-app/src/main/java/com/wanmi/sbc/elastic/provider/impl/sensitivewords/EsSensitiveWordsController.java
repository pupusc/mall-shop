package com.wanmi.sbc.elastic.provider.impl.sensitivewords;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.sensitivewords.EsSensitiveWordsProvider;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsSaveRequest;
import com.wanmi.sbc.elastic.sensitivewords.service.EsSensitiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/11 18:08
 * @description <p> </p>
 */
@RestController
public class EsSensitiveWordsController implements EsSensitiveWordsProvider {

    @Autowired
    private EsSensitiveWordsService esSensitiveWordsService;

    @Override
    public BaseResponse addSensitiveWords(@RequestBody @Valid EsSensitiveWordsSaveRequest request) {
        esSensitiveWordsService.addSensitiveWords(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse init(EsSensitiveWordsQueryRequest request) {
        esSensitiveWordsService.init(request);
        return BaseResponse.SUCCESSFUL();
    }
}
