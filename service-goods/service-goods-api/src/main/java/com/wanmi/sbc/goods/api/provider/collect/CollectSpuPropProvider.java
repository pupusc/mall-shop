package com.wanmi.sbc.goods.api.provider.collect;




import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "CollectSpuPropProvider")
public interface CollectSpuPropProvider {

    /**
     * 根据时间获取商品
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/spu/collectSpuIdPropByTime")
    BaseResponse<List<CollectSpuPropResp>> collectSpuIdPropByTime(@RequestBody CollectSpuPropProviderReq req);


    /**
     * 根据id时间获取商品属性
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/spu/collectSpuPropBySpuIds")
    BaseResponse<List<CollectSpuPropResp>> collectSpuPropBySpuIds(@RequestBody CollectSpuPropProviderReq req);

    /**
     * 根据isbn获取商品信息
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/spu/collectSpuPropByISBN")
    BaseResponse<List<CollectSpuPropResp>> collectSpuPropByISBN(@RequestBody CollectSpuPropProviderReq req);
}
