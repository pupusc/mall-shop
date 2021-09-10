package com.wanmi.sbc.goods.api.provider.booklistmodel;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "BookListModelProvider")
public interface BookListModelProvider {


    @PostMapping("/goods/${application.goods.version}/booklistmodel/add")
    BaseResponse add(@Validated(BookListModelProviderRequest.Add.class)
                     @RequestBody BookListMixProviderRequest bookListMixProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/update")
    BaseResponse update(@Validated(BookListModelProviderRequest.Update.class)
                        @RequestBody BookListMixProviderRequest bookListMixProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/delete")
    BaseResponse delete(@Validated(BookListModelProviderRequest.Delete.class)
                        @RequestBody BookListModelProviderRequest bookListModel);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/listByPage")
    MicroServicePage<BookListModelProviderResponse> listByPage(
                        @RequestBody BookListModelPageProviderRequest bookListModelPageProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/publish")
    BaseResponse publish(@Validated(BookListModelProviderRequest.Publish.class)
                         @RequestBody BookListModelProviderRequest bookListModelProviderRequest);

    /**
     * 根据id获取 书单模版详细信息【这里是获取的书单不一定发布】
     * @param bookListModelProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/findById")
    BaseResponse<BookListMixProviderResponse> findById(
                        @Validated(BookListModelProviderRequest.FindById.class)
                        @RequestBody BookListModelProviderRequest bookListModelProviderRequest);

    /**
     * 根据id获取 书单模版详细信息 【这里获取的书单是发布的】
     * @param bookListModelIdCollection
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/listPublishGoodsByIds")
    BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByIds(
            @NotNull @RequestBody Collection<Integer> bookListModelIdCollection);

    /**
     * 根据不同的类型获取推荐的书单信息
     * @param businessTypeId
     * @param spuId
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/booklistmodel/listBusinessTypeBookListModel/{businessTypeId}/{spuId}")
    BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBusinessTypeBookListModel(
            @PathVariable("businessTypeId") Integer businessTypeId, @PathVariable("spuId") String spuId);
}
