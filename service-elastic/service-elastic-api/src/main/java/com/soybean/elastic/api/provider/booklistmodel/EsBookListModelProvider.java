package com.soybean.elastic.api.provider.booklistmodel;


import com.soybean.elastic.api.req.EsBookListQueryProviderReq;
import com.soybean.elastic.api.req.EsKeyWordBookListQueryProviderReq;
import com.soybean.elastic.api.req.collect.CollectJobReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "${application.elastic.name}", contextId = "EsBookListModelProvider")
public interface EsBookListModelProvider {

    /**
     * 包含关键词的搜索
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/booklistmodel/listKeyWorldEsBookListModel")
    BaseResponse<CommonPageResp<List<EsBookListModelResp>>> listKeyWorldEsBookListModel(@RequestBody @Valid EsKeyWordBookListQueryProviderReq request);

    /**
     * 包含关键词的搜索
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/booklistmodel/listKeyWorldEsBookListModelV2")
    BaseResponse<CommonPageResp<List<EsBookListModelResp>>> listKeyWorldEsBookListModelV2(@RequestBody @Valid EsKeyWordBookListQueryProviderReq request);


    /**
     * 书单信息搜索
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/booklistmodel/listEsBookListModel")
    BaseResponse<CommonPageResp<List<EsBookListModelResp>>> listEsBookListModel(@RequestBody EsBookListQueryProviderReq request);

    /**
     * 采集商品信息
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/booklistmodel/init")
    BaseResponse init(@RequestBody CollectJobReq collectJobReq);
}
