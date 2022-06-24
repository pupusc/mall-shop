package com.wanmi.sbc.goods.api.provider.collect;




import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuImageProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuImageResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "CollectSpuProvider")
public interface CollectSpuProvider {

    /**
     * 根据时间获取商品
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/spu/collectSpuIdByTime")
    BaseResponse<List<CollectSpuVO>> collectSpuIdByTime(@RequestBody CollectSpuProviderReq req);


    /**
     * 根据id时间获取商品
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/spu/collectSpuBySpuIds")
    BaseResponse<List<CollectSpuVO>> collectSpuBySpuIds(@RequestBody CollectSpuProviderReq req);

    /**
     * 根据时间获取商品图片
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/spu/collectSpuIdImageByTime")
    BaseResponse<List<CollectSpuImageResp>> collectSpuIdImageByTime(@RequestBody CollectSpuImageProviderReq req);
}
