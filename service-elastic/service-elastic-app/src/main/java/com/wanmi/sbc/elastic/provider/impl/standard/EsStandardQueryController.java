package com.wanmi.sbc.elastic.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardQueryProvider;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardPageRequest;
import com.wanmi.sbc.elastic.api.response.standard.EsStandardPageResponse;
import com.wanmi.sbc.elastic.standard.service.EsStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@RestController
@Validated
public class EsStandardQueryController implements EsStandardQueryProvider {

    @Autowired
    private EsStandardService esStandardService;

    @Override
    public BaseResponse<EsStandardPageResponse> page(@RequestBody EsStandardPageRequest request) {
        return BaseResponse.success(esStandardService.page(request));
    }
}
