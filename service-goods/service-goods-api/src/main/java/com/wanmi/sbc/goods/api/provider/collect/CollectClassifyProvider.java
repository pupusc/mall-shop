package com.wanmi.sbc.goods.api.provider.collect;




import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.collect.CollectClassifyProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "CollectClassifyProvider")
public interface CollectClassifyProvider {

    /**
     * 根据时间获取店铺分类变更的数据
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/classify/collectClassifySpuIdByTime")
    BaseResponse<List<CollectClassifyRelSpuResp>> collectClassifySpuIdByTime(@RequestBody CollectClassifyProviderReq req);


    /**
     * 根据id时间获取商品
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/classify/collectClassifyBySpuIds")
    BaseResponse<List<CollectClassifyRelSpuDetailResp>> collectClassifyBySpuIds(@RequestBody CollectClassifyProviderReq req);


    /**
     * 获取关系表里面更新的店铺分类数据
     * @param req
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/classify/collectClassifySpuIdRelByTime")
    BaseResponse<List<CollectClassifyRelSpuResp>> collectClassifySpuIdRelByTime(@RequestBody CollectClassifyProviderReq req);
}
