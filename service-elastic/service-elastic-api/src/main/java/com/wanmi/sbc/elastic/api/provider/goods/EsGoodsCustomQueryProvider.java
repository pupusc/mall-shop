package com.wanmi.sbc.elastic.api.provider.goods;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodSCustomQueryProvider")
public interface EsGoodsCustomQueryProvider {



    /**
     * 查询goods列表信息
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/goods/custom/list/normal")
    BaseResponse<MicroServicePage<EsGoodsVO>> listEsGoodsNormal(@RequestBody EsGoodsCustomQueryProviderRequest request);
}
