package com.wanmi.sbc.linkedmall.api.provider.stock;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${application.linkedmall.name}",contextId = "LinkedMallStockQueryProvider")
public interface LinkedMallStockQueryProvider {
    /**
     * 根据四级配送区域编码批量查询spu库存属性
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/stock/by-DivisionCode")
    BaseResponse<List<QueryItemInventoryResponse.Item>> batchGoodsStockByDivisionCode(@RequestBody GoodsStockGetRequest request);
    /**
     * 根据ip批量查询spu库存属性
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/stock/by-ip")
    BaseResponse<List<QueryItemInventoryResponse.Item>> batchGoodsStockByIp(@RequestBody GoodsStockGetRequest request);
}
