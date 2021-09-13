package com.wanmi.sbc.goods.api.provider.classify;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "ClassifyProvider")
public interface ClassifyProvider {


    @PostMapping("/goods/${application.goods.version}/classify/all")
    BaseResponse<List<ClassifyProviderResponse>> listClassify();

    @PostMapping("/goods/${application.goods.version}/classify/listPublishGoodsByIds")
    BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByIds(@NotNull @RequestBody Collection<Integer> bookListModelIdCollection);
}
