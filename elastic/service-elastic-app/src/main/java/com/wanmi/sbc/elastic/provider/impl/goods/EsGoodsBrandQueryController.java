package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsBrandQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsBrandPageResponse;
import com.wanmi.sbc.elastic.goods.service.EsGoodsBrandQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/10 10:30
 * @description <p> </p>
 */
@RestController
public class EsGoodsBrandQueryController implements EsGoodsBrandQueryProvider {

    @Autowired
    private EsGoodsBrandQueryService esGoodsBrandQueryService;

    @Override
    public BaseResponse<EsGoodsBrandPageResponse> page(@RequestBody @Valid EsGoodsBrandPageRequest request) {

        return esGoodsBrandQueryService.page(request);
    }
}
