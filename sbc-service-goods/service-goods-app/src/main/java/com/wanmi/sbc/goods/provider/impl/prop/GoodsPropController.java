package com.wanmi.sbc.goods.provider.impl.prop;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.prop.GoodsPropProvider;
import com.wanmi.sbc.goods.api.request.prop.*;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropAddDefaultRefResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropAddResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropDeleteByPropIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropModifyResponse;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.model.root.GoodsPropDetail;
import com.wanmi.sbc.goods.prop.request.GoodsPropRequest;
import com.wanmi.sbc.goods.prop.service.GoodsPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/10/31 14:38
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsPropController implements GoodsPropProvider{

    @Autowired
    private GoodsPropService goodsPropService;

    /**
     * 批量修改商品属性排序
     * @param goodsPropModifySortRequest {@link GoodsPropModifySortRequest }
     * @return
     */
    
    @Override
    public BaseResponse modifySort(@RequestBody @Valid GoodsPropModifySortRequest goodsPropModifySortRequest){
        List<GoodsProp> goodsProps = GoodsPropConvert.toGoodsPropList(goodsPropModifySortRequest.getGoodsProps());
        goodsPropService.editSort(goodsProps);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 新增商品类目属性
     * @param goodsPropAddRequest {@link GoodsPropAddRequest }
     * @return 类别下所有spuId集合 {@link GoodsPropAddResponse }
     */
    
    @Override
    public BaseResponse<GoodsPropAddResponse> add(@RequestBody @Valid GoodsPropAddRequest goodsPropAddRequest){
        GoodsPropRequest goodsPropRequest = GoodsPropConvert.toGoodsPropRequest(goodsPropAddRequest);
        List<String> stringList = goodsPropService.addGoodsProp(goodsPropRequest);
        return BaseResponse.success(new GoodsPropAddResponse(stringList));
    }

    /**
     * 编辑商品类目属性
     * @param goodsPropModifyRequest {@link GoodsPropModifyRequest }
     * @return
     */
    
    @Override
    public BaseResponse<GoodsPropModifyResponse> modify(@RequestBody @Valid GoodsPropModifyRequest goodsPropModifyRequest){
        GoodsProp goodsProp = GoodsPropConvert.toGoodsProp(goodsPropModifyRequest);
        Boolean result = goodsPropService.editGoodsProp(goodsProp);
        return BaseResponse.success(new GoodsPropModifyResponse(result));
    }

    /**
     * 删除商品类目属性
     * @param goodsPropDeleteByPropIdRequest {@link GoodsPropDeleteByPropIdRequest }
     * @return
     */
    
    @Override
    public BaseResponse<GoodsPropDeleteByPropIdResponse> deleteByPropId(@RequestBody @Valid GoodsPropDeleteByPropIdRequest goodsPropDeleteByPropIdRequest){
        Boolean result = goodsPropService.deleteProp(goodsPropDeleteByPropIdRequest.getPropId());
        return BaseResponse.success(new GoodsPropDeleteByPropIdResponse(result));
    }

    /**
     * 解除商品类目属性与SPU的关联
     * @param goodsPropDeleteRefByPropIdRequest {@link GoodsPropDeleteRefByPropIdRequest }
     * @return
     */
    
    @Override
    public BaseResponse deleteRefByPropId(@RequestBody @Valid GoodsPropDeleteRefByPropIdRequest goodsPropDeleteRefByPropIdRequest){
        goodsPropService.deleteRef(goodsPropDeleteRefByPropIdRequest.getPropId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 解除商品类目属性值与SPU的关联
     * @param goodsPropDeleteRefByPropDetailRequest {@link GoodsPropDeleteRefByPropDetailRequest }
     * @return
     */
    
    @Override
    public BaseResponse deleteRefByPropDetail(@RequestBody @Valid GoodsPropDeleteRefByPropDetailRequest goodsPropDeleteRefByPropDetailRequest){
        GoodsPropDetail goodsPropDetail = new GoodsPropDetail();
        KsBeanUtil.copyPropertiesThird(goodsPropDeleteRefByPropDetailRequest.getGoodsPropDetail(),goodsPropDetail);
        goodsPropService.deleteRef(goodsPropDetail);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 解除关联操作
     * @param goodsPropDeleteRefByPropDetailRelRequest {@link GoodsPropDeleteRefByPropDetailRelRequest }
     * @return
     */
    
    @Override
    public BaseResponse deleteRefByPropDetailRel(@RequestBody @Valid GoodsPropDeleteRefByPropDetailRelRequest goodsPropDeleteRefByPropDetailRelRequest){
        List<GoodsPropDetailRel> goodsPropDetailRels =  KsBeanUtil.convertList(goodsPropDeleteRefByPropDetailRelRequest.getGoodsPropDetailRels(),GoodsPropDetailRel.class);
        goodsPropService.deleteGoodsPropDetailRels(goodsPropDetailRels);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 编辑索引
     * @param goodsPropModifyIndexRequest {@link GoodsPropModifyIndexRequest }
     * @return
     */
    
    @Override
    public BaseResponse modifyIndex(@RequestBody @Valid GoodsPropModifyIndexRequest goodsPropModifyIndexRequest){
        GoodsProp goodsProp = GoodsPropConvert.toGoodsProp(goodsPropModifyIndexRequest);
        goodsPropService.editIndex(goodsProp);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 保存默认spu与默认属性的关联
     * @param goodsPropAddDefaultRefRequest {@link GoodsPropAddDefaultRefRequest }
     * @return
    */
    
    @Override
    public BaseResponse<GoodsPropAddDefaultRefResponse> addDefaultRef(@RequestBody @Valid GoodsPropAddDefaultRefRequest goodsPropAddDefaultRefRequest){
        Boolean result = goodsPropService.saveDefaultRef(goodsPropAddDefaultRefRequest.getGoodsIds(),goodsPropAddDefaultRefRequest.getPropId());
        return BaseResponse.success(new GoodsPropAddDefaultRefResponse(result));
    }
}
