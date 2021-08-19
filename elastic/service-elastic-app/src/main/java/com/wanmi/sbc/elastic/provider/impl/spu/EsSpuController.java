package com.wanmi.sbc.elastic.provider.impl.spu;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.spu.EsSpuQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsListRequest;
import com.wanmi.sbc.elastic.api.request.spu.EsSpuPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.api.response.spu.EsSpuPageResponse;
import com.wanmi.sbc.elastic.spu.serivce.EsSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@RestController
@Validated
public class EsSpuController implements EsSpuQueryProvider {

    @Autowired
    private EsSpuService esSpuService;

    @Override
    public BaseResponse<EsSpuPageResponse> page(@RequestBody EsSpuPageRequest request) {
        return BaseResponse.success(esSpuService.page(request));
    }

    @Override
    public BaseResponse<EsGoodsResponse> listGoodsByIds(@RequestBody @Valid EsGoodsListRequest request) {
        return BaseResponse.success(esSpuService.listGoodsByIds(request));
    }

}
