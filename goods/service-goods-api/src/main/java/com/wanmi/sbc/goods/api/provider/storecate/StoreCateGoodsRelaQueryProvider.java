package com.wanmi.sbc.goods.api.provider.storecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.storecate.*;
import com.wanmi.sbc.goods.api.response.storecate.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 9:52
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "StoreCateGoodsRelaQueryProvider")
public interface StoreCateGoodsRelaQueryProvider {

    /**
     * 根据商品ID查询
     *
     * @param storeCateGoodsRelaListByGoodsIdsRequest 包含：商品ID {@link StoreCateGoodsRelaListByGoodsIdsRequest }
     * @return  {@link StoreCateGoodsRelaListByGoodsIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/rela/list-by-goods-ids")
    BaseResponse<StoreCateGoodsRelaListByGoodsIdsResponse> listByGoodsIds(@RequestBody @Valid StoreCateGoodsRelaListByGoodsIdsRequest storeCateGoodsRelaListByGoodsIdsRequest);

}
