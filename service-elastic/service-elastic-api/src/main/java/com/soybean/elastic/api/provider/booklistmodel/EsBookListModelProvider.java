package com.soybean.elastic.api.provider.booklistmodel;


import com.soybean.elastic.api.req.EsKeyWordQueryProviderReq;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.elastic.name}", contextId = "EsBookListModelProvider")
public interface EsBookListModelProvider {

    /**
     * 包含关键词的搜索
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/booklistmodel/listKeyWorldEsBookListModel")
    BaseResponse listKeyWorldEsBookListModel(@RequestBody @Valid EsKeyWordQueryProviderReq request);
}
