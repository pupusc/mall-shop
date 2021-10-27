package com.wanmi.sbc.goods.api.provider.classify;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.classify.BookListModelClassifyLinkPageProviderRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyCollectionProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.BookListModelClassifyLinkProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "ClassifyProvider")
public interface ClassifyProvider {


    /**
     * 获取店铺下的所有分类列表
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/classify/all")
    BaseResponse<List<ClassifyProviderResponse>> listClassify();


    /**
     * 获取店铺下的所有分类列表
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/classify/listClassifyNoChildByParentId")
    BaseResponse<List<ClassifyProviderResponse>> listClassifyNoChildByParentId(@RequestBody ClassifyCollectionProviderRequest classifyCollectionProviderRequest);

    /**
     * 根据 商品id 获取商品所在分类的 父分类下的所有 子分类对应的商品列表
     * @param goodsId
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/classify/listGoodsIdOfChildOfParentByGoodsId/{goodsId}")
    BaseResponse<List<ClassifyGoodsProviderResponse>> listGoodsIdOfChildOfParentByGoodsId(@PathVariable("goodsId") String goodsId);

    /**
     * 根据分类id 获取商品列表
     * @param classifyIdCollection
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/classify/listGoodsIdByClassifyIdColl")
    BaseResponse<List<ClassifyGoodsProviderResponse>> listGoodsIdByClassifyIdColl(@RequestBody Collection<Integer> classifyIdCollection);

    /**
     * 根据分类 获取书单列表
     * @param bookListModelClassifyLinkPageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/classify/listBookListModelByClassifyIdColl")
    BaseResponse<List<BookListModelClassifyLinkProviderResponse>> listBookListModelByClassifyIdColl(@RequestBody BookListModelClassifyLinkPageProviderRequest bookListModelClassifyLinkPageProviderRequest);


//    @PostMapping("/goods/${application.goods.version}/classify/listPublishGoodsByIds")
//    BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByIds(@NotNull @RequestBody Collection<Integer> bookListModelIdCollection);
}
