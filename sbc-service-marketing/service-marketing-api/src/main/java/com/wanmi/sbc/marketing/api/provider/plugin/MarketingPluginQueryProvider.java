package com.wanmi.sbc.marketing.api.provider.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginByGoodsInfoListAndCustomerRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingPluginByGoodsInfoListAndCustomerLevelResponse;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingPluginGetCustomerLevelsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>主插件服务查询接口</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingPluginQueryProvider")
public interface MarketingPluginQueryProvider {

    /**
     * 获取营销
     * @param request 获取营销请求结构 {@link MarketingPluginByGoodsInfoListAndCustomerRequest}
     * @return {@link MarketingPluginByGoodsInfoListAndCustomerLevelResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/get-marketing-by-goods-info-list-and-customer")
    BaseResponse<MarketingPluginByGoodsInfoListAndCustomerLevelResponse> getByGoodsInfoListAndCustomer(
            @RequestBody @Valid MarketingPluginByGoodsInfoListAndCustomerRequest request);

    /**
     * 获取会员等级
     * @param request 获取会员等级请求结构 {@link MarketingPluginGetCustomerLevelsRequest}
     * @return {@link MarketingPluginGetCustomerLevelsResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/get-customer-levels-by-goods-info-list-and-customer")
    BaseResponse<MarketingPluginGetCustomerLevelsResponse> getCustomerLevelsByGoodsInfoListAndCustomer(
            @RequestBody @Valid MarketingPluginGetCustomerLevelsRequest request);

    /**
     * 获取会员等级
     * @param request 获取会员等级请求结构 {@link MarketingPluginGetCustomerLevelsByStoreIdsRequest}
     * @return {@link MarketingPluginGetCustomerLevelsResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/get-customer-levels-by-store-ids")
    BaseResponse<MarketingPluginGetCustomerLevelsResponse> getCustomerLevelsByStoreIds(@RequestBody @Valid MarketingPluginGetCustomerLevelsByStoreIdsRequest request);
}
