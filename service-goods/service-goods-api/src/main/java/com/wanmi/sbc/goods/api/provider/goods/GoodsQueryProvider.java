package com.wanmi.sbc.goods.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.request.info.GoodsCacheInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsCountByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.goods.*;
import com.wanmi.sbc.goods.api.response.info.GoodsCountByConditionResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider
 *
 * @author lipeng
 * @dateTime 2018/11/5 上午9:31
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsQueryProvider")
public interface GoodsQueryProvider {

    /**
     * 分页查询商品信息
     *
     * @param goodsPageRequest {@link GoodsPageRequest}
     * @return 分页商品信息 {@link GoodsPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/page")
    BaseResponse<GoodsPageResponse> page(@RequestBody @Valid GoodsPageRequest goodsPageRequest);

    /**
     * 分页查询商品信息 (魔方)
     *
     * @param goodsPageRequest {@link GoodsPageRequest}
     * @return 分页商品信息 {@link GoodsPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/pageForX-site")
    BaseResponse<GoodsPageForXsiteResponse> pageForXsite(@RequestBody @Valid GoodsPageRequest goodsPageRequest);

    /**
     * 普通分页查询商品信息
     *
     * @param request {@link GoodsPageByConditionRequest}
     * @return 分页商品信息 {@link GoodsPageByConditionResponse}
     */
    @PostMapping("/goods/${application.goods.version}/page-by-condition")
    BaseResponse<GoodsPageByConditionResponse> pageByCondition(@RequestBody @Valid GoodsPageByConditionRequest request);

    /**
     * 根据编号查询商品视图信息
     *
     * @param goodsByIdRequest {@link GoodsViewByIdRequest}
     * @return 商品信息 {@link GoodsViewByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/get-view-by-id")
    BaseResponse<GoodsViewByIdResponse> getViewById(@RequestBody @Valid GoodsViewByIdRequest goodsByIdRequest);


    /**
     * 根据编号查询商品视图信息(优化，商品基本信息放置redis缓存中，未缓存规格、属性、商品图文信息)
     *
     * @param goodsInfoByIdRequest {@link GoodsCacheInfoByIdRequest}
     * @return 商品信息 {@link GoodsViewByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/get-view-by-id-new")
    BaseResponse<GoodsViewByIdResponse> getCacheViewById(@RequestBody @Valid GoodsCacheInfoByIdRequest goodsCacheInfoByIdRequest);

    /**
     * 查询商品属性、商品图文信息
     *
     * @param goodsByIdRequest {@link GoodsViewByIdRequest}
     * @return 商品信息 {@link GoodsViewByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/get-goods-detail")
    BaseResponse<GoodsDetailProperResponse> getGoodsDetail(@RequestBody @Valid GoodsDetailProperBySkuIdRequest goodsByIdRequest);

    /**
     * 查询商品简易信息
     *
     * @param goodsByIdRequest {@link GoodsDetailSimpleRequest}
     * @return 商品信息 {@link GoodsDetailSimpleResponse}
     */
    @PostMapping("/goods/${application.goods.version}/get-goods-detail-simple")
    BaseResponse<GoodsDetailSimpleResponse> getGoodsDetailSimple(@RequestBody @Valid GoodsDetailSimpleRequest goodsByIdRequest);


    /**
     * 根据编号查询商品信息
     *
     * @param goodsByIdRequest {@link GoodsByIdRequest}
     * @return 商品信息 {@link GoodsByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/get-by-id")
    BaseResponse<GoodsByIdResponse> getById(@RequestBody @Valid GoodsByIdRequest goodsByIdRequest);

    /**
     * 根据多个SpuID查询属性关联
     *
     * @param goodsPropDetailRelByIdsRequest {@link GoodsPropDetailRelByIdsRequest}
     * @return 属性关联信息 {@link GoodsPropDetailRelByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/ref-by-goods-ids")
    BaseResponse<GoodsPropDetailRelByIdsResponse> getRefByGoodIds(@RequestBody @Valid GoodsPropDetailRelByIdsRequest goodsPropDetailRelByIdsRequest);

    /**
     * 待审核商品统计
     *
     * @param goodsUnAuditCountRequest {@link GoodsUnAuditCountRequest}
     * @return 统计结果 {@link GoodsUnAuditCountResponse}
     */
    @PostMapping("/goods/${application.goods.version}/count-un-audit")
    BaseResponse<GoodsUnAuditCountResponse> countUnAudit(@RequestBody @Valid GoodsUnAuditCountRequest goodsUnAuditCountRequest);


    /**
     * 根据不同条件查询商品信息
     *
     * @param goodsByConditionRequest {@link GoodsByConditionRequest}
     * @return  {@link GoodsByConditionResponse}
     */
    @PostMapping("/goods/${application.goods.version}/list-by-condition")
    BaseResponse<GoodsByConditionResponse> listByCondition(@RequestBody @Valid GoodsByConditionRequest goodsByConditionRequest);

    /**
     * 根据不同条件查询商品信息
     *
     * @param goodsByConditionRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/count-by-condition")
    BaseResponse<GoodsCountByConditionResponse> countByCondition(@RequestBody @Valid GoodsCountByConditionRequest goodsByConditionRequest);

    /**
     * 根据goodsId批量查询商品信息列表
     *
     * @param request 包含goodsIds的批量查询请求结构 {@link GoodsListByIdsRequest}
     * @return 商品信息列表 {@link GoodsListByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/list-by-ids")
    BaseResponse<GoodsListByIdsResponse> listByIds(@RequestBody @Valid GoodsListByIdsRequest request);

    /**
     * 根据SPU编号和SkuId集合获取商品详情信息
     * @param request {@link GoodsViewByIdAndSkuIdsRequest}
     * @return {@link GoodsViewByIdAndSkuIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/get-view-by-id-and-sku-ids")
    BaseResponse<GoodsViewByIdAndSkuIdsResponse> getViewByIdAndSkuIds(@RequestBody @Valid GoodsViewByIdAndSkuIdsRequest request);

    /**
     * 根据积分商品Id获取商品详情信息
     * @param request {@link GoodsViewByPointsGoodsIdRequest}
     * @return {@link GoodsViewByPointsGoodsIdRequest}
     */
    @PostMapping("/goods/${application.goods.version}/get-view-by-points-goods-id")
    BaseResponse<GoodsViewByPointsGoodsIdResponse> getViewByPointsGoodsId(@RequestBody @Valid GoodsViewByPointsGoodsIdRequest request);


    /**
     * @Description: 店铺ID统计店铺商品总数
     * @param request {@link GoodsCountByStoreIdRequest}
     * @Author: Bob
     * @Date: 2019-04-03 10:47
     */
    @PostMapping("/goods/${application.goods.version}/count-by-storeid")
    BaseResponse<GoodsCountByStoreIdResponse> countByStoreId(@RequestBody @Valid GoodsCountByStoreIdRequest request);

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/list-by-providerGoodsIds")
    BaseResponse<GoodsListByIdsResponse> listByProviderGoodsId(@RequestBody @Valid GoodsListByIdsRequest request);

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/list-need-syn")
    BaseResponse<GoodsListNeedSynResponse> listNeedSyn(@RequestBody @Valid GoodsQueryNeedSynRequest request);

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/list-erp-no")
    BaseResponse vaildErpNo(@RequestBody @Valid GoodsErpNoRequest request);

    @PostMapping("/goods/${application.goods.version}/list-goods-sync")
    BaseResponse<List<GoodsSyncVO>> listGoodsSync();

    @GetMapping("/goods/${application.goods.version}/count-goods-stock-sync")
    BaseResponse<Integer> countGoodsStockSync();

    @GetMapping("/goods/${application.goods.version}/count-goods-price-sync")
    BaseResponse<Integer> countGoodsPriceSync();
}
