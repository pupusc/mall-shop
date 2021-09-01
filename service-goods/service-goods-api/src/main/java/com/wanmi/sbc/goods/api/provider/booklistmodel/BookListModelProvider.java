package com.wanmi.sbc.goods.api.provider.booklistmodel;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "${application.goods.name}", contextId = "BookListModelProvider")
public interface BookListModelProvider {


    @PostMapping("/goods/${application.goods.version}/booklistmodel/add")
    BaseResponse add(@RequestBody BookListModelProviderRequest bookListModelProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/update")
    BaseResponse update(@RequestBody BookListModelProviderRequest bookListModelProviderRequest);

    @PostMapping("/goods/${application.goods.version}/booklistmodel/listByPage")
    MicroServicePage<BookListModelProviderResponse> listByPage(@RequestBody BookListModelPageProviderRequest bookListModelPageProviderRequest);
}
