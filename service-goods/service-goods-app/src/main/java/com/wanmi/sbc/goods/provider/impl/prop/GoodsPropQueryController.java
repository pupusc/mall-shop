package com.wanmi.sbc.goods.provider.impl.prop;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.prop.GoodsPropQueryProvider;
import com.wanmi.sbc.goods.api.request.prop.*;
import com.wanmi.sbc.goods.api.response.prop.*;
import com.wanmi.sbc.goods.bean.vo.GoodsPropVO;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.model.root.GoodsPropDetail;
import com.wanmi.sbc.goods.prop.request.GoodsPropRequest;
import com.wanmi.sbc.goods.prop.service.GoodsPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/10/31 14:38
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsPropQueryController implements GoodsPropQueryProvider{

    @Autowired
    private GoodsPropService goodsPropService;

    /**
     * 根据类目ID查询商品属性
     * @param goodsPropListAllByCateIdRequest {@link GoodsPropListAllByCateIdRequest }
     * @return 商品属性集合 {@link GoodsPropListAllByCateIdResponse }
    */
    
    @Override
    public BaseResponse<GoodsPropListAllByCateIdResponse> listAllByCateId(@RequestBody @Valid GoodsPropListAllByCateIdRequest goodsPropListAllByCateIdRequest){
        List<GoodsProp> goodsPropList = goodsPropService.queryAllGoodPropsByCate(goodsPropListAllByCateIdRequest.getCateId());
        if (CollectionUtils.isEmpty(goodsPropList)){
            return BaseResponse.success(new GoodsPropListAllByCateIdResponse(Collections.EMPTY_LIST));
        }
        List<GoodsPropVO> goodsPropVOList = GoodsPropConvert.toVO(goodsPropList);
        return BaseResponse.success(new GoodsPropListAllByCateIdResponse(goodsPropVOList));
    }

    /**
     * 根据类目ID查询需要索引的商品属性列表
     * (供用户根据商品属性进行筛选商品)
     * @param goodsPropListIndexByCateIdRequest {@link GoodsPropListIndexByCateIdRequest }
     * @return 商品属性集合 {@link GoodsPropListIndexByCateIdResponse }
     */
    
    @Override
    public BaseResponse<GoodsPropListIndexByCateIdResponse> listIndexByCateId(@RequestBody @Valid GoodsPropListIndexByCateIdRequest goodsPropListIndexByCateIdRequest){
        List<GoodsProp> goodsPropList = goodsPropService.queryIndexGoodPropsByCate(goodsPropListIndexByCateIdRequest.getCateId());
        if (CollectionUtils.isEmpty(goodsPropList)){
            return BaseResponse.success(new GoodsPropListIndexByCateIdResponse(Collections.EMPTY_LIST));
        }
        List<GoodsPropVO> goodsPropVOList = GoodsPropConvert.toVO(goodsPropList);
        return BaseResponse.success(new GoodsPropListIndexByCateIdResponse(goodsPropVOList));
    }

    /**
     * 每次新增初始化排序
     * @param goodsPropListInitSortRequest {@link GoodsPropListInitSortRequest }
     * @return 商品属性集合 {@link GoodsPropListInitSortResponse }
     */
    
    @Override
    public BaseResponse<GoodsPropListInitSortResponse> listInitSort(@RequestBody @Valid GoodsPropListInitSortRequest goodsPropListInitSortRequest){
        GoodsPropRequest goodsPropRequest = GoodsPropConvert.toGoodsPropRequest(goodsPropListInitSortRequest);
        List<GoodsProp>  goodsPropList = goodsPropService.initSort(goodsPropRequest);
        if (CollectionUtils.isEmpty(goodsPropList)){
            return BaseResponse.success(new GoodsPropListInitSortResponse(Collections.EMPTY_LIST));
        }
        List<GoodsPropVO> goodsPropVOList = GoodsPropConvert.toVO(goodsPropList);
        return BaseResponse.success(new GoodsPropListInitSortResponse(goodsPropVOList));
    }

    /**
     * 判断属性值是否超限
     * @param goodsPropQueryPropDetailsOverStepRequest {@link GoodsPropQueryPropDetailsOverStepRequest }
     * @return
    */
    
    @Override
    public BaseResponse<GoodsPropQueryPropDetailsOverStepResponse> queryPropDetailsOverStep(@RequestBody @Valid GoodsPropQueryPropDetailsOverStepRequest goodsPropQueryPropDetailsOverStepRequest){
        List<GoodsPropDetail> goodsPropDetails = new ArrayList<>();
        KsBeanUtil.convertList(goodsPropQueryPropDetailsOverStepRequest.getGoodsPropDetails(),GoodsPropDetail.class);
        Boolean result =goodsPropService.isDetailOverStep(goodsPropDetails);
        return BaseResponse.success(new GoodsPropQueryPropDetailsOverStepResponse(result));
    }

    /**
     * 判断是否是商品属性三级节点
     * @param goodsPropQueryIsChildNodeRequest {@link GoodsPropQueryIsChildNodeRequest }
     * @return
     */
    
    @Override
    public BaseResponse<GoodsPropQueryIsChildNodeResponse> queryIsChildNode(@RequestBody @Valid GoodsPropQueryIsChildNodeRequest goodsPropQueryIsChildNodeRequest){
        Boolean result = goodsPropService.isChildNode(goodsPropQueryIsChildNodeRequest.getCateId());
        return BaseResponse.success(new GoodsPropQueryIsChildNodeResponse(result));
    }

    /**
     * 根据类别Id查询该类别下所有spuId
     * @param goodsPropListByCateIdRequest {@link GoodsPropListByCateIdRequest }
     * @return 所有spuId集合 {@link GoodsPropListByCateIdResponse }
     */
    
    @Override
    public BaseResponse<GoodsPropListByCateIdResponse> listByCateId(@RequestBody @Valid GoodsPropListByCateIdRequest goodsPropListByCateIdRequest){
        List<String> stringList = goodsPropService.findGoodsIdsByCateId(goodsPropListByCateIdRequest.getCateId());
        return BaseResponse.success(new GoodsPropListByCateIdResponse(stringList));
    }

}
