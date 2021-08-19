package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsBrandPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/10 10:22
 * @description <p> </p>
 */

@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodsBrandQueryProvider")
public interface EsGoodsBrandQueryProvider {

    /**
     * 商品品牌分页列表
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/brand/page")
    BaseResponse<EsGoodsBrandPageResponse> page(@RequestBody @Valid EsGoodsBrandPageRequest request);

}
