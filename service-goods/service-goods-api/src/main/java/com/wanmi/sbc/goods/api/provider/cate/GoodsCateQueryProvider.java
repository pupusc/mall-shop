package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.*;
import com.wanmi.sbc.goods.api.response.cate.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * com.wanmi.sbc.goods.api.provider.goodscate.GoodsCateQueryProvider
 * 商品分类查询接口
 * @author lipeng
 * @dateTime 2018/11/1 下午3:09
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCateQueryProvider")
public interface GoodsCateQueryProvider {

    /**
     * 根据条件查询商品分类列表信息
     *
     * @param request {@link GoodsCateListByConditionRequest}
     * @return 商品分类列表信息 {@link GoodsCateListByConditionResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/list-by-condition")
    BaseResponse<GoodsCateListByConditionResponse> listByCondition(
            @RequestBody @Valid GoodsCateListByConditionRequest request);

    /**
     * 根据编号查询分类信息
     * @param request {@link GoodsCateByIdRequest}
     * @return 商品分类信息 {@link GoodsCateByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/get-by-id")
    BaseResponse<GoodsCateByIdResponse> getById(@RequestBody @Valid GoodsCateByIdRequest request);

    /**
     * 根据编号批量查询分类信息
     * @param request {@link GoodsCateByIdsRequest}
     * @return 商品分类列表信息 {@link GoodsCateByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/get-by-ids")
    BaseResponse<GoodsCateByIdsResponse> getByIds(@RequestBody @Valid GoodsCateByIdsRequest request);

    /**
     * 根据编号查询当前分类是否存在子分类
     *
     * @param request {@link GoodsCateExistsChildByIdRequest}
     * @return 是否存在 {@link GoodsCateExistsChildByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/exists-child-by-id")
    BaseResponse<GoodsCateExistsChildByIdResponse> existsChildById(
            @RequestBody @Valid GoodsCateExistsChildByIdRequest request);

    /**
     * 根据编号查询当前分类是否存在商品
     *
     * @param request {@link GoodsCateExistsGoodsByIdRequest}
     * @return 是否存在 {@link GoodsCateExistsGoodsByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/exists-goods-by-id")
    BaseResponse<GoodsCateExistsGoodsByIdResponse> existsGoodsById(
            @RequestBody @Valid GoodsCateExistsGoodsByIdRequest request);

    /**
     * 从缓存中获取商品分类信息
     *
     * @return 商品分类结果数据 {@link GoodsCateByCacheResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/get-by-cache")
    BaseResponse<GoodsCateByCacheResponse> getByCache();

    /**
     * 根据编号查询当前分类下面所有的子分类编号
     *
     * @param request {@link GoodsCateChildCateIdsByIdRequest}
     * @return 子商品分类编号集合 {@link GoodsCateChildCateIdsByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/get-child-cate-id-by-id")
    BaseResponse<GoodsCateChildCateIdsByIdResponse> getChildCateIdById(
            @RequestBody @Valid GoodsCateChildCateIdsByIdRequest request);

    /**
     * 查询所有的分类信息
     *
     * @return 分类列表信息 {@link GoodsCateListResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/list")
    BaseResponse<GoodsCateListResponse> list();

    /**
     * 根据店铺获取叶子分类列表
     *
     * @param request 包含店铺id获取叶子分类列表请求结构 {@link GoodsCateLeafByStoreIdRequest}
     * @return 叶子分类列表 {@link GoodsCateLeafByStoreIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/list-leaf-by-store-id")
    BaseResponse<GoodsCateLeafByStoreIdResponse> listLeafByStoreId(
            @RequestBody @Valid GoodsCateLeafByStoreIdRequest request);

    /**
     * 获取叶子分类列表
     *
     * @return 叶子分类列表 {@link GoodsCateLeafResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/list-leaf")
    BaseResponse<GoodsCateLeafResponse> listLeaf();

    /**
     * 获取一二三级分类名称列表
     * @return 叶子分类列表 {@link GoodsCateLeafResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/shence-burial-site")
    BaseResponse<GoodsCateShenceBurialSiteResponse> listGoodsCateShenceBurialSite(@RequestBody @Valid GoodsCateShenceBurialSiteRequest goodsCateShenceBurialSiteRequest);

    /**
     * 查询优惠券关联的使用范围
     *
     * @param request {@link GoodsCateListCouponDetailRequest}
     * @return 商品分类列表信息 {@link GoodsCateListCouponDetailResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/coupon-detail")
    BaseResponse<GoodsCateListCouponDetailResponse> couponDetail(@RequestBody @Valid GoodsCateListCouponDetailRequest request);


    /**
     * 根据分类等级查询分类信息
     * @param request
     * @return {@link GoodsCateSImpleListResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/page-by-condition")
    BaseResponse<GoodsCateSImpleListResponse> pageByCondition(@RequestBody @Valid GoodsCateListByConditionRequest request);

    /**
     * 根据分类等级查询分类信息--列表
     * @param request
     * @return {@link GoodsCateSImpleListResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/get-goods-cate-simpleList")
    BaseResponse<GoodsCateSImpleListResponse> getGoodsCateSImpleList(@RequestBody @Valid GoodsCateListByConditionRequest request);

}
