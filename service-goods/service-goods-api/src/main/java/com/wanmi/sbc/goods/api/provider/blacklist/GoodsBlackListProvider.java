package com.wanmi.sbc.goods.api.provider.blacklist;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.blacklist.*;
import com.wanmi.sbc.goods.api.request.blacklist.response.GoodsBlackListData;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 黑名单列表信息
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsBlackListProvider")
public interface GoodsBlackListProvider {

    /**
     * 新增黑名单
     * @param goodsBlackListProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/add")
    BaseResponse add(@Validated @RequestBody GoodsBlackListProviderRequest goodsBlackListProviderRequest);


    /**
     * 删除黑名单
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/goods-black-list/delete/{id}")
    BaseResponse delete(@PathVariable("id") Integer id);

    /**
     * 启用停用黑名单
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/update")
    BaseResponse update(@RequestBody GoodsBlackListCreateOrUpdateRequest goodsBlackListCreateOrUpdateRequest);

    /**
     * 刷新黑名单
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/flushBlackListCache")
    BaseResponse<GoodsBlackListPageProviderResponse> flushBlackListCache(@RequestBody GoodsBlackListCacheProviderRequest goodsBlackListPageProviderRequest);

    /**
     * 获取黑名单列表其中一类
     * @param goodsBlackListCacheProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/list")
    BaseResponse<MicroServicePage<GoodsBlackListData>> list(@RequestBody GoodsBlackListCacheProviderRequest goodsBlackListCacheProviderRequest);

    /**
     * 获取黑名单列表
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/listNoPage")
    BaseResponse<GoodsBlackListPageProviderResponse> listNoPage(@RequestBody GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest);
}
