package com.wanmi.sbc.goods.api.provider.booklistmodel;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
    MicroServicePage<BookListModelProviderResponse> listByPage(@RequestBody BookListModelPageProviderRequest bookListModelPageProviderRequest);

    @PostMapping("/goods/${application.goods.version}/booklistmodel/publish")
    BaseResponse publish(@Validated(BookListModelProviderRequest.Publish.class)
                         @RequestBody BookListModelProviderRequest bookListModelProviderRequest);
}
