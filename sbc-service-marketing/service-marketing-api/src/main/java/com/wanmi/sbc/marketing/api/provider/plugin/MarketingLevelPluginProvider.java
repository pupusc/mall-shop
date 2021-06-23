package com.wanmi.sbc.marketing.api.provider.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingLevelGoodsListFilterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>主插件服务操作接口</p>
 * author: daiyitian
 * Date: 2018-11-28
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingLevelPluginProvider")
public interface MarketingLevelPluginProvider {

    /**
     * 商品列表处理
     * @param request 商品列表处理结构 {@link MarketingLevelGoodsListFilterRequest}
     * @return 处理后的商品列表 {@link MarketingLevelGoodsListFilterResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/level/list-filter")
    BaseResponse<MarketingLevelGoodsListFilterResponse> goodsListFilter(@RequestBody @Valid
                                                                               MarketingLevelGoodsListFilterRequest request);

}
