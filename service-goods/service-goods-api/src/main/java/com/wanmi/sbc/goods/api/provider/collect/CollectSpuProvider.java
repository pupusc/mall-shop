package com.wanmi.sbc.goods.api.provider.collect;




import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "CollectSpuProvider")
public interface CollectSpuProvider {

    /**
     * 根据时间获取商品
     * @param req
     * @return
     */
    BaseResponse<List<GoodsVO>> collectSpuIdByTime(@RequestBody CollectSpuProviderReq req);


    /**
     * 根据id时间获取商品
     * @param req
     * @return
     */
    BaseResponse<List<GoodsVO>> collectSpuBySpuIds(@RequestBody CollectSpuProviderReq req);
}
