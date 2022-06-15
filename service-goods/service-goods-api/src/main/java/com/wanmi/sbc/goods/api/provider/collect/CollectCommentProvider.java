package com.wanmi.sbc.goods.api.provider.collect;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.collect.CollectCommentProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "CollectCommentProvider")
public interface CollectCommentProvider {

    /**
     * 根据时间获取店铺分类变更的数据
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/comment/collectCommentSpuIdByTime")
    BaseResponse<List<CollectCommentRelSpuResp>> collectCommentSpuIdByTime(@RequestBody CollectCommentProviderReq req);


    /**
     * 根据id时间获取商品
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/comment/collectCommentBySpuIds")
    BaseResponse<List<CollectCommentRelSpuDetailResp>> collectCommentBySpuIds(@RequestBody CollectCommentProviderReq req);


}
