package com.wanmi.sbc.goods.provider.impl.ares;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.request.ares.*;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/5 10:08
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsAresController implements GoodsAresProvider{

    @Autowired
    private GoodsAresService goodsAresService;

    /**
     * 初始化商品
     * @return {@link BaseResponse}
    */

    @Override
   public BaseResponse initGoodsES(){
        goodsAresService.initGoodsES();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始化商品品牌
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse initGoodsBrandES(){
        goodsAresService.initGoodsBrandES();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始化商品分类
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse initGoodsCateES(){
        goodsAresService.initGoodsCateES();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始化店铺分类
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse initStoreCateES(){
        goodsAresService.initStoreCateES();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 埋点处理的分发方法
     *
     * @param dispatcherFunctionRequest 包含方法类型与参数Bean {@link DispatcherFunctionRequest}
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse dispatchFunction(@RequestBody @Valid DispatcherFunctionRequest dispatcherFunctionRequest){
        goodsAresService.dispatchFunction(dispatcherFunctionRequest.getFuncType(),dispatcherFunctionRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加店铺分类
     * @param goodsAresAddStoreCateRequest {@link GoodsAresAddStoreCateRequest }
     * @return
    */

    @Override
    public BaseResponse addStoreCate(@RequestBody @Valid GoodsAresAddStoreCateRequest goodsAresAddStoreCateRequest){
        goodsAresService.addStoreCate(goodsAresAddStoreCateRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改店铺分类
     * @param goodsAresModifyStoreCateRequest {@link GoodsAresModifyStoreCateRequest }
     * @return
     */

    @Override
    public BaseResponse modifyStoreCate(@RequestBody @Valid GoodsAresModifyStoreCateRequest goodsAresModifyStoreCateRequest){
        goodsAresService.editStoreCate(goodsAresModifyStoreCateRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除店铺分类
     * @param goodsAresDeleteStoreCateRequest {@link GoodsAresDeleteStoreCateRequest }
     * @return
     */

    @Override
    public BaseResponse deleteStoreCate(@RequestBody @Valid GoodsAresDeleteStoreCateRequest goodsAresDeleteStoreCateRequest){
        goodsAresService.delStoreCate(goodsAresDeleteStoreCateRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加商品类别
     * @param goodsAresAddGoodsCateRequest {@link GoodsAresAddGoodsCateRequest }
     * @return
     */

    @Override
    public BaseResponse addGoodsCate(@RequestBody @Valid GoodsAresAddGoodsCateRequest goodsAresAddGoodsCateRequest){
        goodsAresService.addGoodsCate(goodsAresAddGoodsCateRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品类别
     * @param goodsAresModifyGoodsCateRequest {@link GoodsAresModifyGoodsCateRequest }
     * @return
     */

    @Override
    public BaseResponse modifyGoodsCate(@RequestBody @Valid GoodsAresModifyGoodsCateRequest goodsAresModifyGoodsCateRequest){
        goodsAresService.editGoodsCate(goodsAresModifyGoodsCateRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除商品类别
     * @param goodsAresDeleteGoodsCateRequest {@link GoodsAresDeleteGoodsCateRequest }
     * @return
     */

    @Override
    public BaseResponse deleteGoodsCate(@RequestBody @Valid GoodsAresDeleteGoodsCateRequest goodsAresDeleteGoodsCateRequest){
        goodsAresService.delGoodsCate(goodsAresDeleteGoodsCateRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加商品品牌
     * @param goodsAresAddGoodsBrandRequest {@link GoodsAresAddGoodsBrandRequest }
     * @return
     */

    @Override
    public BaseResponse addGoodsBrand(@RequestBody @Valid GoodsAresAddGoodsBrandRequest goodsAresAddGoodsBrandRequest){
        goodsAresService.addGoodsBrand(goodsAresAddGoodsBrandRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品品牌
     * @param goodsAresModifyGoodsBrandRequest {@link GoodsAresModifyGoodsBrandRequest }
     * @return
     */

    @Override
    public BaseResponse modifyGoodsBrand(@RequestBody @Valid GoodsAresModifyGoodsBrandRequest goodsAresModifyGoodsBrandRequest){
        goodsAresService.editGoodsBrand(goodsAresModifyGoodsBrandRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除商品品牌
     * @param goodsAresDeleteGoodsBrandRequest {@link GoodsAresDeleteGoodsBrandRequest }
     * @return
     */

    @Override
    public BaseResponse deleteGoodsBrand(@RequestBody @Valid GoodsAresDeleteGoodsBrandRequest goodsAresDeleteGoodsBrandRequest){
        goodsAresService.delGoodsBrand(goodsAresDeleteGoodsBrandRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加商品sku
     * @param goodsAresAddGoodsSkuRequest {@link GoodsAresAddGoodsSkuRequest }
     * @return
     */

    @Override
    public BaseResponse addGoodsSku(@RequestBody @Valid GoodsAresAddGoodsSkuRequest goodsAresAddGoodsSkuRequest){
        goodsAresService.addGoodsSku(goodsAresAddGoodsSkuRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改多个商品sku
     * @param goodsAresModifyGoodsSkuRequest 需要被添加的sku,需要被删除的skuId,需要被更新的sku {@link GoodsAresModifyGoodsSkuRequest }
     * @return
     */

    @Override
    public BaseResponse modifyGoodsSku(@RequestBody @Valid GoodsAresModifyGoodsSkuRequest goodsAresModifyGoodsSkuRequest){
        goodsAresService.editGoodsSku(goodsAresModifyGoodsSkuRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改单个商品sku
     * @param goodsAresModifyOneGoodsSkuRequest  {@link GoodsAresModifyOneGoodsSkuRequest }
     * @return
     */

    @Override
   public BaseResponse modifyOneGoodsSku(@RequestBody @Valid GoodsAresModifyOneGoodsSkuRequest goodsAresModifyOneGoodsSkuRequest){
        goodsAresService.editOneGoodsSku(goodsAresModifyOneGoodsSkuRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量删除商品spu
     * @param goodsAresDeleteGoodsSpuRequest 删除的spu商品idList {@link GoodsAresDeleteGoodsSpuRequest }
     * @return
     */

    @Override
    public BaseResponse deleteGoodsSpu(@RequestBody @Valid GoodsAresDeleteGoodsSpuRequest goodsAresDeleteGoodsSpuRequest){
        goodsAresService.delGoodsSpu(goodsAresDeleteGoodsSpuRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量删除商品sku
     * @param goodsAresDeleteGoodsSkuRequest 删除的sku商品idList {@link GoodsAresDeleteGoodsSkuRequest }
     * @return
     */

    @Override
    public BaseResponse deleteGoodsSku(@RequestBody @Valid GoodsAresDeleteGoodsSkuRequest goodsAresDeleteGoodsSkuRequest){
        goodsAresService.delGoodsSku(goodsAresDeleteGoodsSkuRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量修改商品sku上下架状态
     * @param goodsAresModifyGoodsSpuRequest 上下架状态,sku商品idList {@link GoodsAresModifyGoodsSpuRequest }
     * @return
     */

    @Override
    public BaseResponse modifyGoodsSpu(@RequestBody @Valid GoodsAresModifyGoodsSpuRequest goodsAresModifyGoodsSpuRequest){
        goodsAresService.editGoodsSpuUp(goodsAresModifyGoodsSpuRequest.getObjs());
        return BaseResponse.SUCCESSFUL();
    }
}
