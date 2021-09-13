package com.wanmi.sbc.elastic.api.provider.goods;


import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodSCustomQueryProvider")
public interface EsGoodsCustomQueryProvider {



    /**
     * 查询goods列表信息
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/goods/custom/list/normal")
    MicroServicePage<EsGoodsVO> listEsGoodsNormal(EsGoodsCustomProviderRequest request);
}
