package com.wanmi.sbc.goods.api.provider.ares;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.ares.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/5 10:08
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsAresProvider")
public interface GoodsAresProvider {

    /**
     * 初始化商品
     * @return {@link BaseResponse}
    */
    @PostMapping("/goods/${application.goods.version}/ares/init-goods-es")
    BaseResponse initGoodsES();

    /**
     * 初始化商品品牌
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/ares/init-goods-brand-es")
    BaseResponse initGoodsBrandES();

    /**
     * 初始化商品分类
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/ares/init-goods-cate-es")
    BaseResponse initGoodsCateES();

    /**
     * 初始化店铺分类
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/ares/init-store-cate-es")
    BaseResponse initStoreCateES();

    /**
     * 埋点处理的分发方法
     *
     * @param dispatcherFunctionRequest 包含方法类型与参数Bean {@link DispatcherFunctionRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/ares/dispatch-function")
    BaseResponse dispatchFunction(@RequestBody @Valid DispatcherFunctionRequest dispatcherFunctionRequest);

    /**
     * 添加店铺分类
     * @param goodsAresAddStoreCateRequest {@link GoodsAresAddStoreCateRequest }
     * @return
    */
    @PostMapping("/goods/${application.goods.version}/ares/add-store-cate")
    BaseResponse addStoreCate(@RequestBody @Valid GoodsAresAddStoreCateRequest goodsAresAddStoreCateRequest);

    /**
     * 修改店铺分类
     * @param goodsAresModifyStoreCateRequest {@link GoodsAresModifyStoreCateRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/modify-store-cate")
    BaseResponse modifyStoreCate(@RequestBody @Valid GoodsAresModifyStoreCateRequest goodsAresModifyStoreCateRequest);

    /**
     * 删除店铺分类
     * @param goodsAresDeleteStoreCateRequest {@link GoodsAresDeleteStoreCateRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/delete-store-cate")
    BaseResponse deleteStoreCate(@RequestBody @Valid GoodsAresDeleteStoreCateRequest goodsAresDeleteStoreCateRequest);

    /**
     * 添加商品类别
     * @param goodsAresAddGoodsCateRequest {@link GoodsAresAddGoodsCateRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/add-goods-cate")
    BaseResponse addGoodsCate(@RequestBody @Valid GoodsAresAddGoodsCateRequest goodsAresAddGoodsCateRequest);

    /**
     * 修改商品类别
     * @param goodsAresModifyGoodsCateRequest {@link GoodsAresModifyGoodsCateRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/modify-Goods-cate")
    BaseResponse modifyGoodsCate(@RequestBody @Valid GoodsAresModifyGoodsCateRequest goodsAresModifyGoodsCateRequest);

    /**
     * 删除商品类别
     * @param goodsAresDeleteGoodsCateRequest {@link GoodsAresDeleteGoodsCateRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/delete-Goods-cate")
    BaseResponse deleteGoodsCate(@RequestBody @Valid GoodsAresDeleteGoodsCateRequest goodsAresDeleteGoodsCateRequest);

    /**
     * 添加商品品牌
     * @param goodsAresAddGoodsBrandRequest {@link GoodsAresAddGoodsBrandRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/add-goods-brand")
    BaseResponse addGoodsBrand(@RequestBody @Valid GoodsAresAddGoodsBrandRequest goodsAresAddGoodsBrandRequest);

    /**
     * 修改商品品牌
     * @param goodsAresModifyGoodsBrandRequest {@link GoodsAresModifyGoodsBrandRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/modify-Goods-brand")
    BaseResponse modifyGoodsBrand(@RequestBody @Valid GoodsAresModifyGoodsBrandRequest goodsAresModifyGoodsBrandRequest);

    /**
     * 删除商品品牌
     * @param goodsAresDeleteGoodsBrandRequest {@link GoodsAresDeleteGoodsBrandRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/delete-Goods-brand")
    BaseResponse deleteGoodsBrand(@RequestBody @Valid GoodsAresDeleteGoodsBrandRequest goodsAresDeleteGoodsBrandRequest);

    /**
     * 添加商品sku
     * @param goodsAresAddGoodsSkuRequest {@link GoodsAresAddGoodsSkuRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/add-goods-sku")
    BaseResponse addGoodsSku(@RequestBody @Valid GoodsAresAddGoodsSkuRequest goodsAresAddGoodsSkuRequest);

    /**
     * 修改多个商品sku
     * @param goodsAresModifyGoodsSkuRequest 需要被添加的sku,需要被删除的skuId,需要被更新的sku {@link GoodsAresModifyGoodsSkuRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/modify-Goods-sku")
    BaseResponse modifyGoodsSku(@RequestBody @Valid GoodsAresModifyGoodsSkuRequest goodsAresModifyGoodsSkuRequest);

    /**
     * 修改单个商品sku
     * @param goodsAresModifyOneGoodsSkuRequest  {@link GoodsAresModifyOneGoodsSkuRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/modify-one-Goods-sku")
    BaseResponse modifyOneGoodsSku(@RequestBody @Valid GoodsAresModifyOneGoodsSkuRequest goodsAresModifyOneGoodsSkuRequest);

    /**
     * 批量删除商品spu
     * @param goodsAresDeleteGoodsSpuRequest 删除的spu商品idList {@link GoodsAresDeleteGoodsSpuRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/delete-Goods-spu")
    BaseResponse deleteGoodsSpu(@RequestBody @Valid GoodsAresDeleteGoodsSpuRequest goodsAresDeleteGoodsSpuRequest);

    /**
     * 批量删除商品sku
     * @param goodsAresDeleteGoodsSkuRequest 删除的sku商品idList {@link GoodsAresDeleteGoodsSkuRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/delete-Goods-sku")
    BaseResponse deleteGoodsSku(@RequestBody @Valid GoodsAresDeleteGoodsSkuRequest goodsAresDeleteGoodsSkuRequest);

    /**
     * 批量修改商品sku上下架状态
     * @param goodsAresModifyGoodsSpuRequest 上下架状态,sku商品idList {@link GoodsAresModifyGoodsSpuRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/ares/modify-Goods-spu")
    BaseResponse modifyGoodsSpu(@RequestBody @Valid GoodsAresModifyGoodsSpuRequest goodsAresModifyGoodsSpuRequest);
}
