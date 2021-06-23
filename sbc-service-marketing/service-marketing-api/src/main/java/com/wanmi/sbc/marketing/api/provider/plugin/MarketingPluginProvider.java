package com.wanmi.sbc.marketing.api.provider.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsDetailFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingPluginGoodsDetailFilterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>主插件服务操作接口</p>
 * author: sunkun
 * Date: 2018-11-15
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingPluginProvider")
public interface MarketingPluginProvider {

    /**
     * 商品列表处理
     * @param request 商品列表处理结构 {@link MarketingPluginGoodsListFilterRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/goods/list-filter")
    BaseResponse<GoodsInfoListByGoodsInfoResponse> goodsListFilter(@RequestBody @Valid MarketingPluginGoodsListFilterRequest request);

    /**
     * 商品详情处理
     * @param request 商品详情处理结构 {@link MarketingPluginGoodsDetailFilterRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/goods/detail-filter")
    BaseResponse<MarketingPluginGoodsDetailFilterResponse> goodsDetailFilter(@RequestBody @Valid MarketingPluginGoodsDetailFilterRequest request);

    /**
     * 热销商品列表处理
     * @param request 商品列表处理结构 {@link MarketingPluginGoodsListFilterRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/distribution/goods/list-filter")
    BaseResponse<GoodsInfoListByGoodsInfoResponse> distributionGoodsListFilter(@RequestBody @Valid MarketingPluginGoodsListFilterRequest request);

}
