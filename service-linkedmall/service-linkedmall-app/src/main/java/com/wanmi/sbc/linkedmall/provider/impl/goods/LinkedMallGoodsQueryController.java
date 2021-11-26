package com.wanmi.sbc.linkedmall.provider.impl.goods;

import com.aliyuncs.linkedmall.model.v20180116.QueryBizItemListResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.goods.LinkedMallGoodsQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailByIdRequest;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.goods.LinkedMallGoodsPageRequest;
import com.wanmi.sbc.linkedmall.api.response.goods.LinkedMallGoodsPageResponse;
import com.wanmi.sbc.linkedmall.goods.LinkedMallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LinkedMallGoodsQueryController implements LinkedMallGoodsQueryProvider {
    @Autowired
    private LinkedMallGoodsService linkedMallGoodsService;

    @Override
    public BaseResponse<LinkedMallGoodsPageResponse<QueryBizItemListResponse.Item>> getLinkedMallGoodsPage(LinkedMallGoodsPageRequest linkedMallGoodsPageRequest) {
        LinkedMallGoodsPageResponse<QueryBizItemListResponse.Item> response = linkedMallGoodsService.getGoodsPage(linkedMallGoodsPageRequest.getPageNum(), linkedMallGoodsPageRequest.getPageSize());
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<QueryItemDetailResponse.Item> getGoodsDetailById(GoodsDetailByIdRequest goodsDetailByIdRequest) {
        QueryItemDetailResponse.Item goodsById = linkedMallGoodsService.getGoodsDetailById(goodsDetailByIdRequest.getProviderGoodsId());
        return BaseResponse.success(goodsById);
    }

    @Override
    public BaseResponse<List<QueryItemDetailResponse.Item>> getGoodsDetailBatch(@Valid GoodsDetailQueryRequest request) {
       return BaseResponse.success(linkedMallGoodsService.getGoodsDetailBatch(request));
    }
}
