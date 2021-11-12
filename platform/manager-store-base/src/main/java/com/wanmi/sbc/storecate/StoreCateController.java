package com.wanmi.sbc.storecate;

import com.alibaba.fastjson.JSONArray;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteStoreCateRequest;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.storecate.*;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateAddResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateByStoreCateIdResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByStoreIdResponse;
import com.wanmi.sbc.goods.bean.dto.StoreCateSortDTO;
import com.wanmi.sbc.goods.bean.vo.StoreCateResponseVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponMarketingScopeProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponMarketingScopeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeBatchAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeByScopeIdRequest;
import com.wanmi.sbc.marketing.bean.dto.CouponMarketingScopeDTO;
import com.wanmi.sbc.marketing.bean.vo.CouponMarketingScopeVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 店铺分类管理Controller
 * Author: bail
 * Time: 2017/11/14.10:21
 */
@Api(tags = "StoreCateController", description = "店铺分类管理服务API")
@RestController
@RequestMapping("/storeCate")
public class StoreCateController {
    @Autowired
    private StoreCateProvider storeCateProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    GoodsAresProvider goodsAresProvider;

    @Autowired
    private CouponMarketingScopeProvider couponMarketingScopeProvider;

    @Autowired
    private CouponMarketingScopeQueryProvider couponMarketingScopeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    /**
     * 查询店铺商品分类List
     */
    @ApiOperation(value = "查询店铺商品分类List")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List<StoreCateResponseVO>> list() {
        Long storeId = commonUtil.getStoreId();
        BaseResponse<StoreCateListByStoreIdResponse> baseResponse =
                storeCateQueryProvider.listByStoreId(new StoreCateListByStoreIdRequest(storeId));
        StoreCateListByStoreIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)) {
            return BaseResponse.success(Collections.emptyList());
        }
        return BaseResponse.success(response.getStoreCateResponseVOList());
    }

    /**
     * 新增店铺商品分类
     *
     * @param saveRequest 新增的分类信息
     */
    @ApiOperation(value = "新增店铺商品分类")
    @RequestMapping(method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<StoreCateResponseVO> add(@RequestBody StoreCateAddRequest saveRequest) {
        Long storeId = commonUtil.getStoreId();
        saveRequest.setStoreId(storeId);
        StoreCateAddResponse cateResponse = storeCateProvider.add(saveRequest).getContext();
        //查询父分类是否关联优惠券
        List<CouponMarketingScopeVO> couponMarketingScopes = couponMarketingScopeQueryProvider.listByScopeId(
                CouponMarketingScopeByScopeIdRequest.builder().scopeId(String.valueOf(cateResponse.getStoreCateResponseVO()
                        .getCateParentId())).build()).getContext().getScopeVOList();
        if (CollectionUtils.isNotEmpty(couponMarketingScopes)) {
            couponMarketingScopes.stream().map(couponScope -> {
                couponScope.setMarketingScopeId(null);
                couponScope.setCateGrade(couponScope.getCateGrade() + 1);
                couponScope.setScopeId(String.valueOf(cateResponse.getStoreCateResponseVO().getStoreCateId()));
                return couponScope;
            });
            couponMarketingScopeProvider.batchAdd(
                    CouponMarketingScopeBatchAddRequest.builder()
                            .scopeDTOList(KsBeanUtil.convert(couponMarketingScopes, CouponMarketingScopeDTO.class))
                            .build());
        }
        if (saveRequest.getCateParentId() == null || saveRequest.getCateParentId().equals(0L)) {
            operateLogMQUtil.convertAndSend("商品", "新增一级分类", "新增一级分类：" + saveRequest.getCateName());
        } else {
            operateLogMQUtil.convertAndSend("商品", "添加子分类", "添加子分类：" + saveRequest.getCateName());
        }

        return BaseResponse.success(cateResponse.getStoreCateResponseVO());
    }

    /**
     * 获取商品分类详情信息
     *
     * @param storeCateId 店铺商品分类标识
     */
    @ApiOperation(value = "获取商品分类详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeCateId", value = "店铺分类Id", required = true)
    @RequestMapping(value = "/{storeCateId}", method = RequestMethod.GET)
    public BaseResponse<StoreCateByStoreCateIdResponse> list(@PathVariable Long storeCateId) {
        BaseResponse<StoreCateByStoreCateIdResponse> baseResponse =
                storeCateQueryProvider.getByStoreCateId(new StoreCateByStoreCateIdRequest(storeCateId));
        return BaseResponse.success(baseResponse.getContext());
    }

    /**
     * 查询店铺商品分类List
     */
    @ApiOperation(value = "查询店铺商品分类List")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId",
            value = "店铺Id", required = true)
    @RequestMapping(value = "/two/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List<StoreCateResponseVO>> providerStorelist(@PathVariable Long storeId) {
        BaseResponse<StoreCateListByStoreIdResponse> baseResponse =
                storeCateQueryProvider.listByStoreId(new StoreCateListByStoreIdRequest(storeId));
        StoreCateListByStoreIdResponse storeCateListByStoreIdResponse = baseResponse.getContext();
        if (Objects.isNull(storeCateListByStoreIdResponse)) {
            return BaseResponse.success(Collections.emptyList());
        }
        return BaseResponse.success(storeCateListByStoreIdResponse.getStoreCateResponseVOList());
    }

    /**
     * 编辑店铺商品分类
     *
     * @param saveRequest 编辑的分类信息
     */
    @ApiOperation(value = "编辑店铺商品分类")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody StoreCateModifyRequest saveRequest) {
        Long storeId = commonUtil.getStoreId();
        saveRequest.setStoreId(storeId);
        storeCateProvider.modify(saveRequest);

        //ares埋点-商品-新增店铺分类
        operateLogMQUtil.convertAndSend("商品", "编辑分类", "编辑分类：" + saveRequest.getCateName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 检测店铺分类是否有子类
     *
     * @param queryRequest 查询条件(分类标识)
     */
    @ApiOperation(value = "检测店铺分类是否有子类")
    @RequestMapping(value = "/checkHasChild", method = RequestMethod.POST)
    public BaseResponse<Integer> checkHasChild(@RequestBody StoreCateQueryHasChildRequest queryRequest) {
        return BaseResponse.success(storeCateQueryProvider.queryHasChild(queryRequest).getContext().getResult());
    }

    /**
     * 检测店铺分类是否关联了商品
     *
     * @param queryRequest 查询条件(分类标识)
     */
    @ApiOperation(value = "检测店铺分类是否关联了商品")
    @RequestMapping(value = "/checkHasGoods", method = RequestMethod.POST)
    public BaseResponse<Integer> checkHasGoods(@RequestBody StoreCateQueryHasGoodsRequest queryRequest) {
        return BaseResponse.success(storeCateQueryProvider.queryHasGoods(queryRequest).getContext().getResult());
    }

    /**
     * 删除店铺商品分类
     */
    @ApiOperation(value = "删除店铺商品分类")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeCateId", value = "店铺分类Id", required = true)
    @RequestMapping(value = "/{storeCateId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long storeCateId) {
        StoreCateDeleteRequest saveRequest = new StoreCateDeleteRequest();
        Long storeId = commonUtil.getStoreId();
        saveRequest.setStoreId(storeId);
        saveRequest.setStoreCateId(storeCateId);
        Map<String, Object> returnMap = storeCateProvider.delete(saveRequest).getContext().getHashMap();

        List<Long> cates = JSONArray.parseArray(returnMap.get("allCate").toString(), Long.class);
        esGoodsInfoElasticProvider.delStoreCateIds(EsGoodsDeleteStoreCateRequest.builder().
                storeCateIds(cates).storeId(storeId).build());
        StoreCateByStoreCateIdRequest request = new StoreCateByStoreCateIdRequest();
        request.setStoreCateId(storeCateId);
        BaseResponse<StoreCateByStoreCateIdResponse> baseResponse =
                storeCateQueryProvider.getByStoreCateId(request);
        StoreCateByStoreCateIdResponse response = baseResponse.getContext();
        if (Objects.nonNull(response)) {
            StoreCateResponseVO storeCateResponseVO = response.getStoreCateResponseVO();
            operateLogMQUtil.convertAndSend("商品", "删除分类",
                    "删除分类：" + (Objects.nonNull(storeCateResponseVO) ? storeCateResponseVO.getCateName() : ""));
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 商家APP里店铺分类排序
     *
     * @param saveRequest
     */
    @ApiOperation(value = "商家APP里店铺分类排序")
    @RequestMapping(value = "/allInLine", method = RequestMethod.POST)
    public BaseResponse<Integer> batchSortCate(@RequestBody StoreCateBatchSortRequest saveRequest) {
        storeCateProvider.batchSort(saveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 拖拽排序店铺分类
     */
    @ApiOperation(value = "拖拽排序店铺分类")
    @RequestMapping(value = "/sort", method = RequestMethod.PUT)
    public BaseResponse goodsCateSort(@RequestBody List<StoreCateSortDTO> sortRequestList) {
        return storeCateProvider.batchModifySort(new StoreCateBatchModifySortRequest(sortRequestList));
    }

}
