package com.wanmi.sbc.marketing.api.provider.market;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.market.MarketingGoodsForXsiteRequest;
import com.wanmi.sbc.marketing.api.request.market.*;
import com.wanmi.sbc.marketing.api.response.market.*;
import com.wanmi.sbc.marketing.bean.dto.MarketingPointBuyLevelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description: 营销查询接口Feign客户端
 * @Date: 2018-11-16 16:56
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingQueryProvider")
public interface MarketingQueryProvider {

    /**
     * 通过营销种类查询存在的SKU
     * @param request 查询参数 {@link ExistsSkuByMarketingTypeRequest}
     * @return sku编号列表 {@link List}
     */
    @PostMapping("/marketing/${application.marketing.version}/query-exists-sku-by-marketingtype")
    BaseResponse<List<String>> queryExistsSkuByMarketingType(@RequestBody @Valid ExistsSkuByMarketingTypeRequest request);


    /**
     * 分页查询营销数据
     * @param marketingPageRequest 分页查询参数 {@link MarketingPageRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/page")
    BaseResponse<MarketingPageResponse> page(@RequestBody @Valid MarketingPageRequest marketingPageRequest);


    /**
     * 根据id获取营销实体
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/get-by-id")
    BaseResponse<MarketingGetByIdResponse> getById(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest);

    /**
     * 商家端根据id获取营销实体
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/get-by-id-for-supplier")
    BaseResponse<MarketingGetByIdForSupplierResponse> getByIdForSupplier(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest);

    /**
     * 商家端根据id获取营销实体
     *  查询组合购营销活动
     */
    @PostMapping("/marketing/${application.marketing.version}/get-by-marketingid")
    BaseResponse<MarketingByIdAndSubtypeResponse> getByMarketingId(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest);

    /**
     * 会员端根据id获取营销实体
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/get-by-id-for-customer")
    BaseResponse<MarketingGetByIdForCustomerResponse> getByIdForCustomer(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest);

    /**
     * 根据多个id获取多个营销实体
     * @param queryByIdsRequest 唯一编号参数列表 {@link MarketingGetByIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/query-by-ids")
    BaseResponse<MarketingQueryByIdsResponse> queryByIds(@RequestBody @Valid MarketingQueryByIdsRequest queryByIdsRequest);

    /**
     * 根据多个id获取多个营销View实体
     * @param queryByIdsRequest 唯一编号参数列表 {@link MarketingGetByIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/query-view-by-ids")
    BaseResponse<MarketingViewQueryByIdsResponse> queryViewByIds(@RequestBody @Valid MarketingViewQueryByIdsRequest queryByIdsRequest);

    /**
     * 根据id获取营销对应的商品信息
     * @param getByIdRequest 唯一编号参数 {@link MarketingGetByIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/get-goods-by-id")
    BaseResponse<MarketingGetGoodsByIdResponse> getGoodsById(@RequestBody @Valid MarketingGetByIdRequest getByIdRequest);

    /**
     * 获取验证进行中的营销
     * @param queryByIdsRequest 唯一编号列表参数 {@link MarketingQueryByIdsRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/query-starting-by-ids")
    BaseResponse<MarketingQueryStartingByIdsResponse> queryStartingByIds(@RequestBody @Valid MarketingQueryByIdsRequest queryByIdsRequest);

    /**
     * 将营销活动集合，map成 { goodsId - list<Marketing> } 结构
     * @param request 查询参数 {@link MarketingMapGetByGoodsIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/get-marketing-map-by-goods-id")
    BaseResponse<MarketingMapGetByGoodsIdResponse> getMarketingMapByGoodsId(@RequestBody @Valid MarketingMapGetByGoodsIdRequest request);

    @PostMapping("/marketing/${application.marketing.version}/groupon/queryGrouponInfoForXsite")
    BaseResponse<MarketingGoodsForXsiteResponse> queryForXsite(@RequestBody @Valid MarketingGoodsForXsiteRequest request);

    /**
     * 查找积分换购活动详情
     * @param id
     * @return
     */
    @GetMapping("/marketing/${application.marketing.version}/point-buy")
    BaseResponse<MarketingPointBuyLevelDto> getPointBuyLevel(@RequestParam Long id);
}
