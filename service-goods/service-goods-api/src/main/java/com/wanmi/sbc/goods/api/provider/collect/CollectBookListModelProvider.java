package com.wanmi.sbc.goods.api.provider.collect;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CollectBookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "CollectBookListModelProvider")
public interface CollectBookListModelProvider {

    /**
     *
     * 获取发布的商品列表
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectBookListGoodsPublishId")
    BaseResponse<List<CollectBookListGoodsPublishResponse>> collectBookListGoodsPublishId(@RequestBody CollectBookListModelProviderRequest request);


    /**
     *
     * 根据id获取发布的商品列表
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectBookListGoodsPublishIdByBookListIds")
    BaseResponse<List<CollectBookListGoodsPublishResponse>> collectBookListGoodsPublishIdByBookListIds(@RequestBody CollectBookListModelProviderRequest request);


    /**
     * 获取书单列表
     */
    BaseResponse<List<BookListModelProviderResponse>> collectBookListId(@RequestBody CollectBookListModelProviderRequest request);


    /**
     * 根据书单id获取书单列表
     * @param request
     * @return
     */
    BaseResponse<List<BookListModelProviderResponse>> collectBookListByBookListIds(@RequestBody CollectBookListModelProviderRequest request);

}
