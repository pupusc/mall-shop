package com.wanmi.sbc.elastic.provider.impl.sku;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.elastic.sku.service.EsSkuService;
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
public class EsSkuQueryController implements EsSkuQueryProvider {

    @Autowired
    private EsSkuService esSkuService;

    @Override
    public BaseResponse<EsSkuPageResponse> page(@RequestBody EsSkuPageRequest request) {
        return BaseResponse.success(esSkuService.page(request));
    }
}
