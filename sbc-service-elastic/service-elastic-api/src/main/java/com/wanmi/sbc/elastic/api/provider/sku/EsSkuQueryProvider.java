package com.wanmi.sbc.elastic.api.provider.sku;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSkuQueryProvider")
public interface EsSkuQueryProvider {

    /**
     * 根据条件分页查询商品SKU分页列表
     *
     * @param request 条件分页查询请求结构 {@link EsSkuPageRequest}
     * @return 分页列表 {@link EsSkuPageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/sku/page")
    BaseResponse<EsSkuPageResponse> page(@RequestBody EsSkuPageRequest request);
}
