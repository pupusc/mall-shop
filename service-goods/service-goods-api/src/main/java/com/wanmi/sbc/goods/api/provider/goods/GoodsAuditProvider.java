package com.wanmi.sbc.goods.api.provider.goods;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;



@FeignClient(value = "${application.goods.name}", contextId = "GoodsAuditProvider")
public interface GoodsAuditProvider {
    /**
     * 商品标签
     */
    @PostMapping("/goods/${application.goods.version}/audit/list")
    BaseResponse<Page<GoodsSyncVO>> list(@RequestBody GoodsAuditQueryRequest request);
}
