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
@FeignClient(value = "${application.goods.name}", contextId = "StoreCateQueryProvider")
public interface StoreCateQueryProvider {

    /**
     * 根据店铺ID查询商品分类
     * @param storeCateListByStoreIdRequest {@link StoreCateListByStoreIdRequest }
     * @return 店铺商品分类集合 {@link StoreCateListByStoreIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/list-by-store-id")
    BaseResponse<StoreCateListByStoreIdResponse> listByStoreId(@RequestBody @Valid StoreCateListByStoreIdRequest storeCateListByStoreIdRequest);

    /**
     * 根据店铺ID查询非默认的店铺商品分类
     * @param storeCateListWithoutDefaultByStoreIdRequest {@link StoreCateListWithoutDefaultByStoreIdRequest }
     * @return 店铺商品分类集合 {@link StoreCateListWithoutDefaultByStoreIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/list-without-default-by-store-id")
    BaseResponse<StoreCateListWithoutDefaultByStoreIdResponse> listWithoutDefaultByStoreId(@RequestBody @Valid StoreCateListWithoutDefaultByStoreIdRequest storeCateListWithoutDefaultByStoreIdRequest);

    /**
     * 根据ID查询某个商品店铺分类
     * @param storeCateByStoreCateIdRequest {@link StoreCateByStoreCateIdRequest }
     * @return 商品分类集合 {@link StoreCateByStoreCateIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/get-by-store-cate-id")
    BaseResponse<StoreCateByStoreCateIdResponse> getByStoreCateId(@RequestBody @Valid StoreCateByStoreCateIdRequest storeCateByStoreCateIdRequest);

    /**
     * 验证是否有子类
     * @param storeCateQueryHasChildRequest {@link StoreCateQueryHasChildRequest }
     * @return {@link StoreCateQueryHasChildResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/query-has-child")
    BaseResponse<StoreCateQueryHasChildResponse> queryHasChild(@RequestBody @Valid StoreCateQueryHasChildRequest storeCateQueryHasChildRequest);

    /**
     * 验证分类下是否已经关联商品
     * @param storeCateQueryHasGoodsRequest {@link StoreCateQueryHasGoodsRequest }
     * @return {@link StoreCateQueryHasGoodsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/query-has-goods")
    BaseResponse<StoreCateQueryHasGoodsResponse> queryHasGoods(@RequestBody @Valid StoreCateQueryHasGoodsRequest storeCateQueryHasGoodsRequest);


    /**
     * 根据商品编号查询分类
     * @param storeCateListByGoodsRequest {@link StoreCateListByGoodsRequest }
     * @return {@link StoreCateListByGoodsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/list-by-goods")
    BaseResponse<StoreCateListByGoodsResponse> listByGoods(@RequestBody @Valid StoreCateListByGoodsRequest storeCateListByGoodsRequest);

    /**
     * 根据cateIds批量查询店铺分类列表
     * @param request 包含cateIds批量查询请求结构 {@link StoreCateListByIdsRequest }
     * @return 店铺分类列表 {@link StoreCateListByIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/list-by-ids")
    BaseResponse<StoreCateListByIdsResponse> listByIds(@RequestBody @Valid StoreCateListByIdsRequest request);

    /**
     * 根据分类ID和是否加入包含自身对象获取所有子分类
     * @param storeCateListByStoreCateIdAndIsHaveSelfRequest {@link StoreCateListByStoreCateIdAndIsHaveSelfRequest }
     * @return 子分类集合{@link StoreCateListByStoreCateIdAndIsHaveSelfResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/list-by-store-cate-id-and-is-have-self")
    BaseResponse<StoreCateListByStoreCateIdAndIsHaveSelfResponse> listByStoreCateIdAndIsHaveSelf(@RequestBody @Valid StoreCateListByStoreCateIdAndIsHaveSelfRequest storeCateListByStoreCateIdAndIsHaveSelfRequest);

    /**
     * 根据ID获取所有子分类->所有的商品
     * @param storeCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest {@link StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest }
     * @return 所有子分类->所有的商品{@link StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse}
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/list-goods-rel-by-store-cate-id-and-is-have-self")
    BaseResponse<StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse> listGoodsRelByStoreCateIdAndIsHaveSelf(@RequestBody @Valid StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest storeCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest);
}
