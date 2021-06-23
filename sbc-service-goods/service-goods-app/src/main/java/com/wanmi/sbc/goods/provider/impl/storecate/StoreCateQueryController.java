package com.wanmi.sbc.goods.provider.impl.storecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.storecate.*;
import com.wanmi.sbc.goods.api.response.storecate.*;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateResponseVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateVO;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.response.StoreCateResponse;
import com.wanmi.sbc.goods.storecate.service.StoreCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 9:52
 * @version: 1.0
 */
@RestController
@Validated
public class StoreCateQueryController implements StoreCateQueryProvider {

    @Autowired
    private StoreCateService storeCateService;

    /**
     * 根据店铺ID查询商品分类
     * @param storeCateListByStoreIdRequest {@link StoreCateListByStoreIdRequest }
     * @return 店铺商品分类集合 {@link StoreCateListByStoreIdResponse }
     */
    
    @Override
    public BaseResponse<StoreCateListByStoreIdResponse> listByStoreId(@RequestBody @Valid StoreCateListByStoreIdRequest storeCateListByStoreIdRequest){
        List<StoreCateResponse> storeCateResponseList =  storeCateService.query(storeCateListByStoreIdRequest.getStoreId());
        if (CollectionUtils.isEmpty(storeCateResponseList)){
            return BaseResponse.success(new StoreCateListByStoreIdResponse(Collections.EMPTY_LIST));
        }
        List<StoreCateResponseVO> storeCateResponseVOList = StoreCateConvert.toStoreCateResponseVOList(storeCateResponseList);
        return BaseResponse.success(new StoreCateListByStoreIdResponse(storeCateResponseVOList));
    }

    /**
     * 根据店铺ID查询非默认的店铺商品分类
     * @param storeCateListWithoutDefaultByStoreIdRequest {@link StoreCateListWithoutDefaultByStoreIdRequest }
     * @return 店铺商品分类集合 {@link StoreCateListWithoutDefaultByStoreIdResponse }
     */
    
    @Override
    public BaseResponse<StoreCateListWithoutDefaultByStoreIdResponse> listWithoutDefaultByStoreId(@RequestBody @Valid StoreCateListWithoutDefaultByStoreIdRequest storeCateListWithoutDefaultByStoreIdRequest){
        List<StoreCateResponse> storeCateResponseList =  storeCateService.queryNoDefault(storeCateListWithoutDefaultByStoreIdRequest.getStoreId());
        if (CollectionUtils.isEmpty(storeCateResponseList)){
            return BaseResponse.success(new StoreCateListWithoutDefaultByStoreIdResponse(Collections.EMPTY_LIST));
        }
        List<StoreCateResponseVO> storeCateResponseVOList = StoreCateConvert.toStoreCateResponseVOList(storeCateResponseList);
        return BaseResponse.success(new StoreCateListWithoutDefaultByStoreIdResponse(storeCateResponseVOList));
    }

    /**
     * 根据ID查询某个商品店铺分类
     * @param storeCateByStoreCateIdRequest {@link StoreCateByStoreCateIdRequest }
     * @return 商品分类集合 {@link StoreCateByStoreCateIdResponse }
     */
    
    @Override
    public BaseResponse<StoreCateByStoreCateIdResponse> getByStoreCateId(@RequestBody @Valid StoreCateByStoreCateIdRequest storeCateByStoreCateIdRequest){
        StoreCateResponse storeCateResponse = storeCateService.findById(storeCateByStoreCateIdRequest.getStoreCateId());
        if (Objects.isNull(storeCateResponse)){
            return BaseResponse.success(new StoreCateByStoreCateIdResponse());
        }
        StoreCateResponseVO storeCateResponseVO = StoreCateConvert.toStoreCateResponseVO(storeCateResponse);
        return BaseResponse.success(new StoreCateByStoreCateIdResponse(storeCateResponseVO));
    }

    /**
     * 验证是否有子类
     * @param storeCateQueryHasChildRequest {@link StoreCateQueryHasChildRequest }
     * @return
     */
    
    @Override
    public BaseResponse<StoreCateQueryHasChildResponse> queryHasChild(@RequestBody @Valid StoreCateQueryHasChildRequest storeCateQueryHasChildRequest){
        Integer result = storeCateService.checkHasChild(storeCateQueryHasChildRequest);
        return BaseResponse.success(new StoreCateQueryHasChildResponse(result));
    }

    /**
     * 验证分类下是否已经关联商品
     * @param storeCateQueryHasGoodsRequest {@link StoreCateQueryHasGoodsRequest }
     * @return
     */
    
    @Override
    public BaseResponse<StoreCateQueryHasGoodsResponse> queryHasGoods(@RequestBody @Valid StoreCateQueryHasGoodsRequest storeCateQueryHasGoodsRequest){
        Integer result = storeCateService.checkHasGoods(storeCateQueryHasGoodsRequest);
        return BaseResponse.success(new StoreCateQueryHasGoodsResponse(result));
    }

    /**
     * 根据商品编号查询分类
     * @param storeCateListByGoodsRequest {@link StoreCateListByGoodsRequest }
     * @return {@link StoreCateListByGoodsResponse }
     */
    
    @Override
    public BaseResponse<StoreCateListByGoodsResponse> listByGoods(@RequestBody @Valid StoreCateListByGoodsRequest storeCateListByGoodsRequest){
        List<StoreCateGoodsRela> storeCateGoodsRelaList = storeCateService.getStoreCateByGoods(storeCateListByGoodsRequest.getGoodsIds());
        if (CollectionUtils.isEmpty(storeCateGoodsRelaList)){
            return BaseResponse.success(new StoreCateListByGoodsResponse(Collections.EMPTY_LIST));
        }
        List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList = KsBeanUtil.convertList(storeCateGoodsRelaList,StoreCateGoodsRelaVO.class);
        return BaseResponse.success(new StoreCateListByGoodsResponse(storeCateGoodsRelaVOList));
    }

    /**
     * 根据cateIds批量查询店铺分类列表
     * @param request 包含cateIds批量查询请求结构 {@link StoreCateListByIdsRequest }
     * @return 店铺分类列表 {@link StoreCateListByIdsResponse }
     */
    @Override
    
    public BaseResponse<StoreCateListByIdsResponse> listByIds(@RequestBody @Valid StoreCateListByIdsRequest request){
        List<StoreCate> cates = storeCateService.findByIds(request.getCateIds());
        if (CollectionUtils.isEmpty(cates)){
            return BaseResponse.success(StoreCateListByIdsResponse.builder().storeCateVOList(Collections.emptyList()).build());
        }
        List<StoreCateVO> voList = KsBeanUtil.convertList(cates, StoreCateVO.class);
        return BaseResponse.success(StoreCateListByIdsResponse.builder().storeCateVOList(voList).build());
    }

    /**
     * 根据分类ID和是否加入包含自身对象获取所有子分类
     * @param storeCateListByStoreCateIdAndIsHaveSelfRequest {@link StoreCateListByStoreCateIdAndIsHaveSelfRequest }
     * @return 子分类集合{@link StoreCateListByStoreCateIdAndIsHaveSelfResponse }
     */
    
    @Override
    public BaseResponse<StoreCateListByStoreCateIdAndIsHaveSelfResponse> listByStoreCateIdAndIsHaveSelf(@RequestBody @Valid StoreCateListByStoreCateIdAndIsHaveSelfRequest storeCateListByStoreCateIdAndIsHaveSelfRequest){
        List<StoreCate> storeCateList = storeCateService.findAllChlid(storeCateListByStoreCateIdAndIsHaveSelfRequest.getStoreCateId(),storeCateListByStoreCateIdAndIsHaveSelfRequest.isHaveSelf());
        if (CollectionUtils.isEmpty(storeCateList)){
            return BaseResponse.success(new StoreCateListByStoreCateIdAndIsHaveSelfResponse(Collections.EMPTY_LIST));
        }
        List<StoreCateVO> storeCateVOList = StoreCateConvert.toStoreCateVOList(storeCateList);
        return BaseResponse.success(new StoreCateListByStoreCateIdAndIsHaveSelfResponse(storeCateVOList));
    }

    /**
     * 根据ID获取所有子分类->所有的商品
     * @param storeCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest {@link StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest }
     * @return 所有子分类->所有的商品{@link StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse}
     */
    
    @Override
    public BaseResponse<StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse> listGoodsRelByStoreCateIdAndIsHaveSelf(@RequestBody @Valid StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest storeCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest){
        List<StoreCateGoodsRela> storeCateGoodsRelaList = storeCateService.findAllChildRela(storeCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest.getStoreCateId(),storeCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest.isHaveSelf());
        if (CollectionUtils.isEmpty(storeCateGoodsRelaList)){
            return BaseResponse.success(new StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse(Collections.EMPTY_LIST));
        }
        List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList = KsBeanUtil.convertList(storeCateGoodsRelaList,StoreCateGoodsRelaVO.class);
        return BaseResponse.success(new StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse(storeCateGoodsRelaVOList));
    }
}
