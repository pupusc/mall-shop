package com.wanmi.sbc.linkedmall.provider.impl.stock;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.linkedmall.stock.LinkedMallStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LinkedMallStockQueryController implements LinkedMallStockQueryProvider {
    @Autowired
    private LinkedMallStockService linkedMallStockService;
    @Override
    public BaseResponse<List<QueryItemInventoryResponse.Item>> batchGoodsStockByDivisionCode(GoodsStockGetRequest request) {
        List<QueryItemInventoryResponse.Item> items = linkedMallStockService.batchGoodsStockByDivisionCode(request.getProviderGoodsIds(), request.getDivisionCode());
        return BaseResponse.success(items);
    }

    @Override
    public BaseResponse<List<QueryItemInventoryResponse.Item>> batchGoodsStockByIp(GoodsStockGetRequest request) {
        List<QueryItemInventoryResponse.Item> items = linkedMallStockService.batchGoodsStockByIp(request.getProviderGoodsIds(), request.getIp());
        return BaseResponse.success(items);
    }
}
