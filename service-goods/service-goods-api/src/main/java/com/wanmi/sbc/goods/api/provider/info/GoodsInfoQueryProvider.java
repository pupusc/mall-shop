package com.wanmi.sbc.goods.api.provider.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseGoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseGoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.info.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>对商品sku查询接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsInfoQueryProvider")
public interface GoodsInfoQueryProvider {

    /**
     * 分页查询商品sku视图列表
     *
     * @param request 商品sku视图分页条件查询结构 {@link GoodsInfoViewPageRequest}
     * @return 商品sku视图分页列表 {@link GoodsInfoViewPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/page-view")
    BaseResponse<GoodsInfoViewPageResponse> pageView(@RequestBody @Valid GoodsInfoViewPageRequest request);

    /**
     * 分页查询商品sku列表
     *
     * @param request 商品sku分页条件查询结构 {@link GoodsInfoPageRequest}
     * @return 商品sku分页列表 {@link GoodsInfoPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/page")
    BaseResponse<GoodsInfoPageResponse> page(@RequestBody @Valid GoodsInfoPageRequest request);

    /**
     * 根据商品skuId批量查询商品sku视图列表
     *
     * @param request 根据批量商品skuId查询结构 {@link GoodsInfoViewByIdsRequest}
     * @return 商品sku视图列表 {@link GoodsInfoViewByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/list-view-by-ids")
    BaseResponse<GoodsInfoViewByIdsResponse> listViewByIds(@RequestBody @Valid GoodsInfoViewByIdsRequest request);

    /**
     * 根据商品skuId查询商品sku视图
     *
     * @param request 根据商品skuId查询结构 {@link GoodsInfoViewByIdRequest}
     * @return 商品sku视图 {@link GoodsInfoViewByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/get-view-by-id")
    BaseResponse<GoodsInfoViewByIdResponse> getViewById(@RequestBody @Valid GoodsInfoViewByIdRequest request);

    /**
     * 根据商品skuId批量查询商品sku列表
     *
     * @param request 根据批量商品skuId查询结构 {@link GoodsInfoListByIdsRequest}
     * @return 商品sku列表 {@link GoodsInfoListByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/list-by-ids")
    BaseResponse<GoodsInfoListByIdsResponse> listByIds(@RequestBody @Valid GoodsInfoListByIdsRequest request);

    /**
     * 根据商品skuId查询商品sku
     * @param request 根据商品skuId查询结构 {@link GoodsInfoByIdRequest}
     * @return 商品sku {@link GoodsInfoByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/get-by-id")
    BaseResponse<GoodsInfoByIdResponse> getById(@RequestBody @Valid GoodsInfoByIdRequest request);

    /**
     * 根据skuid查询spuid和所有skuid
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/get-simple-by-id")
    BaseResponse<Map<String, Object>> getSimpleById(@RequestBody List<String> goodsInfoIds);

    /**
     * 根据动态条件查询商品sku列表
     *
     * @param request 根据动态条件查询结构 {@link GoodsInfoListByConditionRequest}
     * @return 商品sku列表 {@link GoodsInfoListByConditionResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/list-by-condition")
    BaseResponse<GoodsInfoListByConditionResponse> listByCondition(@RequestBody @Valid GoodsInfoListByConditionRequest
                                                                           request);

    /**
     * 根据动态条件统计商品sku个数
     *
     * @param request 根据动态条件统计结构 {@link GoodsInfoCountByConditionRequest}
     * @return 商品sku个数 {@link GoodsInfoCountByConditionResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/count-by-condition")
    BaseResponse<GoodsInfoCountByConditionResponse> countByCondition(@RequestBody @Valid
                                                                             GoodsInfoCountByConditionRequest
                                                                           request);

    /**
     * 分页查询分销商品sku视图列表
     *
     * @param request 分销商品sku视图分页条件查询结构 {@link DistributionGoodsPageRequest}
     * @return 分销商品sku视图分页列表 {@link DistributionGoodsInfoPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/page-distribution")
    BaseResponse<DistributionGoodsInfoPageResponse> distributionGoodsInfoPage(@RequestBody @Valid DistributionGoodsPageRequest request);

    /**
     * @Description: 商品ID<spu> 查询sku信息
     * @Author: Bob
     * @Date: 2019-03-11 20:43
    */
    @PostMapping("/goods/${application.goods.version}/info/get-by-goodsid")
    BaseResponse<GoodsInfoByGoodsIdresponse> getByGoodsId(@RequestBody @Valid DistributionGoodsChangeRequest request);

    /**
     * 分页查询企业购商品sku视图列表
     *
     * @param request 分页查询企业购商品sku视图列表 {@link EnterpriseGoodsInfoPageRequest}
     * @return 分销商品sku视图分页列表 {@link EnterpriseGoodsInfoPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/page-enterprise")
    BaseResponse<EnterpriseGoodsInfoPageResponse> enterpriseGoodsInfoPage(@RequestBody @Valid EnterpriseGoodsInfoPageRequest request);

    /**
     * 根据skuid获取storeid
     *
     * @param   {@link GoodsInfoStoreIdBySkuIdRequest}
     * @return  {@link GoodsInfoStoreIdBySkuIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/get-storeId-By-GoodsId")
    BaseResponse<GoodsInfoStoreIdBySkuIdResponse> getStoreIdByGoodsId(@RequestBody @Valid GoodsInfoStoreIdBySkuIdRequest request);

    /**
     * 根据商品id查询商品的积分价
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/list-integral-goods-by-ids")
    BaseResponse<GoodsInfoListByIdsResponse> listIntegralPriceGoodsByIds(@RequestBody @Valid GoodsInfoListByIdsRequest request);

    /**
     * 根据商品id批量查询商品sku部分字段列表
     *
     * @param request 根据商品id批量查询结构 {@link GoodsInfoPartColsByIdsRequest}
     * @return 商品sku部分字段列表 {@link GoodsInfoPartColsByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/list-part-cols-by-ids")
    BaseResponse<GoodsInfoPartColsByIdsResponse> listPartColsByIds(@RequestBody @Valid GoodsInfoPartColsByIdsRequest
                                                                           request);

    /**
     * 根据SKU NO批量查询商品SKU市场价信息
     * @param request 包含SKU ID 集合的条件参数 {@link GoodsInfoMarketingPriceByNosRequest}
     * @return SKU市场价列表 {@link GoodsInfoMarketingPriceByNosResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/list-marketing-price-by-nos")
    BaseResponse<GoodsInfoMarketingPriceByNosResponse> listMarketingPriceByNos(@RequestBody @Valid GoodsInfoMarketingPriceByNosRequest request);


    /**
     * 获取sku simple信息
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/list-simple-view")
    BaseResponse<GoodsInfoViewByIdsResponse> listSimpleView(@RequestBody GoodsInfoViewByIdsRequest request);

    /**
     * 通过isbnList获取sku simple信息
     * @param isbnList
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/goods-info-by-isbns")
    BaseResponse<GoodsInfoViewByIdsResponse> goodsInfoByIsbns(@RequestBody GoodsInfoViewByIdsRequest isbnList);

    /**
     * 通过isbnList获取sku simple信息
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/isbnBySkuId")
    String isbnBySkuId(@RequestParam(value = "skuId") String skuId);

    /**
     * 通过isbnList获取sku simple信息
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/getRedis")
    BaseResponse<String> getRedis(@RequestBody String spuNo);
}
