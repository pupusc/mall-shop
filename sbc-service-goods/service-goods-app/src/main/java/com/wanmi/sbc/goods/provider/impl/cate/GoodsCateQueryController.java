package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.*;
import com.wanmi.sbc.goods.api.response.cate.*;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO;
import com.wanmi.sbc.goods.bean.vo.CouponInfoForScopeNamesVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * com.wanmi.sbc.goods.provider.impl.cate.GoodsCateQueryController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:19
 */
@RestController
@Validated
public class GoodsCateQueryController implements GoodsCateQueryProvider {

    @Autowired
    private GoodsCateService goodsCateService;

    /**
     * 根据条件查询商品分类列表信息
     *
     * @param request {@link GoodsCateListByConditionRequest}
     * @return 商品分类列表信息 {@link GoodsCateListByConditionResponse}
     */
    @Override
    public BaseResponse<GoodsCateListByConditionResponse> listByCondition(
            @RequestBody @Valid GoodsCateListByConditionRequest request) {
        GoodsCateQueryRequest queryRequest = KsBeanUtil.convert(request, GoodsCateQueryRequest.class);
        List<GoodsCate> goodsCateList = goodsCateService.query(queryRequest);

        GoodsCateListByConditionResponse response = new GoodsCateListByConditionResponse();
        List<GoodsCateVO> goodsCateVOList = KsBeanUtil.convert(goodsCateList, GoodsCateVO.class);
        response.setGoodsCateVOList(goodsCateVOList);
        return BaseResponse.success(response);
    }

    /**
     * 根据编号查询分类信息
     *
     * @param request {@link GoodsCateByIdRequest}
     * @return 商品分类信息 {@link GoodsCateByIdResponse}
     */
    @Override
    public BaseResponse<GoodsCateByIdResponse> getById(@RequestBody @Valid GoodsCateByIdRequest request) {
        Long cateId = request.getCateId();
        GoodsCate goodsCate = goodsCateService.findById(cateId);

        GoodsCateByIdResponse response = KsBeanUtil.convert(goodsCate, GoodsCateByIdResponse.class);
        return BaseResponse.success(response);
    }

    /**
     * 根据编号批量查询分类信息
     *
     * @param request {@link GoodsCateByIdsRequest}
     * @return 商品分类列表信息 {@link GoodsCateByIdsResponse}
     */
    @Override
    public BaseResponse<GoodsCateByIdsResponse> getByIds(@RequestBody @Valid GoodsCateByIdsRequest request) {
        List<Long> cateIdList = request.getCateIds();
        List<GoodsCate> goodsCateList = goodsCateService.findByIds(cateIdList);

        GoodsCateByIdsResponse response = new GoodsCateByIdsResponse();
        List<GoodsCateVO> goodsCateVOList = KsBeanUtil.convert(goodsCateList, GoodsCateVO.class);
        response.setGoodsCateVOList(goodsCateVOList);
        return BaseResponse.success(response);
    }

    /**
     * 根据编号查询当前分类是否存在子分类
     *
     * @param request {@link GoodsCateExistsChildByIdRequest}
     * @return 是否存在 {@link GoodsCateExistsChildByIdResponse}
     */
    @Override
    public BaseResponse<GoodsCateExistsChildByIdResponse> existsChildById(
            @RequestBody @Valid GoodsCateExistsChildByIdRequest request) {
        Long cateId = request.getCateId();
        Integer result = goodsCateService.checkChild(cateId);

        GoodsCateExistsChildByIdResponse response = new GoodsCateExistsChildByIdResponse();
        response.setResult(result);
        return BaseResponse.success(response);
    }

    /**
     * 根据编号查询当前分类是否存在商品
     *
     * @param request {@link GoodsCateExistsGoodsByIdRequest}
     * @return 是否存在 {@link GoodsCateExistsGoodsByIdResponse}
     */
    @Override
    public BaseResponse<GoodsCateExistsGoodsByIdResponse> existsGoodsById(
            @RequestBody @Valid GoodsCateExistsGoodsByIdRequest request) {
        Long cateId = request.getCateId();
        Integer result = goodsCateService.checkGoods(cateId);

        GoodsCateExistsGoodsByIdResponse response = new GoodsCateExistsGoodsByIdResponse();
        response.setResult(result);
        return BaseResponse.success(response);
    }

    /**
     * 从缓存中获取商品分类信息
     *
     * @return 商品分类结果数据 {@link GoodsCateByCacheResponse}
     */
    @Override
    public BaseResponse<GoodsCateByCacheResponse> getByCache() {
        String result = goodsCateService.getGoodsCatesByCache();

        GoodsCateByCacheResponse response = new GoodsCateByCacheResponse();
        response.setResult(result);
        return BaseResponse.success(response);
    }

    /**
     * 根据编号查询当前分类下面所有的子分类编号
     *
     * @param request {@link GoodsCateChildCateIdsByIdResponse}
     * @return 子商品分类编号集合 {@link GoodsCateChildCateIdsByIdResponse}
     */
    @Override
    public BaseResponse<GoodsCateChildCateIdsByIdResponse> getChildCateIdById(
            @RequestBody @Valid GoodsCateChildCateIdsByIdRequest request) {

        List<Long> childCateIdList = goodsCateService.getChlidCateId(request.getCateId());

        GoodsCateChildCateIdsByIdResponse response = new GoodsCateChildCateIdsByIdResponse();
        response.setChildCateIdList(childCateIdList);
        return BaseResponse.success(response);
    }

    /**
     * 查询所有的分类信息
     *
     * @return 分类列表信息 {@link GoodsCateListResponse}
     */
    @Override
    public BaseResponse<GoodsCateListResponse> list() {
        List<GoodsCate> goodsCateList = goodsCateService.queryGoodsCate();

        GoodsCateListResponse response = new GoodsCateListResponse();
        List<GoodsCateVO> goodsCateVOList = KsBeanUtil.convert(goodsCateList, GoodsCateVO.class);
        response.setGoodsCateVOList(goodsCateVOList);
        return BaseResponse.success(response);
    }

    /**
     * 根据店铺获取叶子分类列表
     *
     * @param request 包含店铺id获取叶子分类列表请求结构 {@link GoodsCateLeafByStoreIdRequest}
     * @return 叶子分类列表 {@link GoodsCateLeafByStoreIdResponse}
     */
    @Override
    public BaseResponse<GoodsCateLeafByStoreIdResponse> listLeafByStoreId(
            @RequestBody @Valid GoodsCateLeafByStoreIdRequest request) {
        List<GoodsCate> goodsCateList = goodsCateService.queryLeafByStoreId(request.getStoreId());
        GoodsCateLeafByStoreIdResponse response = new GoodsCateLeafByStoreIdResponse();
        if (CollectionUtils.isNotEmpty(goodsCateList)) {
            response.setGoodsCateList(KsBeanUtil.convert(goodsCateList, GoodsCateVO.class));
        }
        return BaseResponse.success(response);
    }

    /**
     * 获取叶子分类列表
     *
     * @return 叶子分类列表 {@link GoodsCateLeafResponse}
     */
    @Override
    public BaseResponse<GoodsCateLeafResponse> listLeaf() {
        List<GoodsCate> goodsCateList = goodsCateService.queryLeaf();
        GoodsCateLeafResponse response = new GoodsCateLeafResponse();
        if (CollectionUtils.isNotEmpty(goodsCateList)) {
            response.setGoodsCateList(KsBeanUtil.convert(goodsCateList, GoodsCateVO.class));
        }
        return BaseResponse.success(response);
    }

    /**
     * 神策埋点获取一二级分类名称
     * @param request
     * @return
     */
    @Override
    public BaseResponse<GoodsCateShenceBurialSiteResponse> listGoodsCateShenceBurialSite(GoodsCateShenceBurialSiteRequest request) {
        return  BaseResponse.success(GoodsCateShenceBurialSiteResponse.builder().goodsCateList(goodsCateService.listGoodsCateShenceBurialSite(request.getCateIds())).build());
    }

    /**
     * 根据分类等级查询分类信息
     * @param request
     * @return {@link GoodsCateSImpleListResponse}
     */
    @Override
    public BaseResponse<GoodsCateSImpleListResponse> pageByCondition(@Valid GoodsCateListByConditionRequest request) {
        request.setDelFlag(DeleteFlag.NO.toValue());
        GoodsCateQueryRequest queryRequest = KsBeanUtil.convert(request, GoodsCateQueryRequest.class);
        List<GoodsCate> goodsCateList = goodsCateService.pageByCondition(queryRequest);
        GoodsCateSImpleListResponse response = new GoodsCateSImpleListResponse();
        List<GoodsCateSimpleVO> goodsCateVOList = KsBeanUtil.convert(goodsCateList, GoodsCateSimpleVO.class);
        response.setGoodsCateVOList(goodsCateVOList);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GoodsCateListCouponDetailResponse> couponDetail(@RequestBody @Valid GoodsCateListCouponDetailRequest request) {
        List<CouponInfoForScopeNamesVO>  voList = goodsCateService.copyToCouponInfoVoNew(request);
        return BaseResponse.success(new GoodsCateListCouponDetailResponse(voList));
    }

    @Override
    public BaseResponse<GoodsCateSImpleListResponse> getGoodsCateSImpleList(@Valid GoodsCateListByConditionRequest request) {
        request.setDelFlag(DeleteFlag.NO.toValue());
        GoodsCateQueryRequest queryRequest = KsBeanUtil.convert(request, GoodsCateQueryRequest.class);
        List<GoodsCate> goodsCateList = goodsCateService.getGoodsCateSImpleList(queryRequest);
        GoodsCateSImpleListResponse response = new GoodsCateSImpleListResponse();
        List<GoodsCateSimpleVO> goodsCateVOList = KsBeanUtil.convert(goodsCateList, GoodsCateSimpleVO.class);
        response.setGoodsCateVOList(goodsCateVOList);
        return BaseResponse.success(response);
    }
}
