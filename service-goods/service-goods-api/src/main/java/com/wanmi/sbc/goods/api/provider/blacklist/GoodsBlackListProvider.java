package com.wanmi.sbc.goods.api.provider.blacklist;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListCacheProviderRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
     * 刷新黑名单
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/flushBlackListCache")
    BaseResponse<GoodsBlackListPageProviderResponse> flushBlackListCache(@RequestBody GoodsBlackListCacheProviderRequest goodsBlackListPageProviderRequest);


    /**
     * 获取黑名单列表
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods-black-list/listNoPage")
    BaseResponse<GoodsBlackListPageProviderResponse> listNoPage(@RequestBody GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest);
}
