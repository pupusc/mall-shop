package com.wanmi.sbc.elastic.api.provider.spu;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsListRequest;
import com.wanmi.sbc.elastic.api.request.spu.EsSpuPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.api.response.spu.EsSpuPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSpuQueryProvider")
public interface EsSpuQueryProvider {

    /**
     * 根据条件分页查询商品SPU分页列表
     *
     * @param request 条件分页查询请求结构 {@link EsSpuPageRequest}
     * @return 分页列表 {@link EsSpuPageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/spu/page")
    BaseResponse<EsSpuPageResponse> page(@RequestBody EsSpuPageRequest request);

    /**
     * 根据spuId列表查询spu信息
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/goods/spu/list-by-ids")
    BaseResponse<EsGoodsResponse> listGoodsByIds(@RequestBody @Valid EsGoodsListRequest request);
}
