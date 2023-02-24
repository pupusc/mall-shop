package com.wanmi.sbc.goods.api.provider.booklistmodel;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.BookListGoodsPublishProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CountBookListModelGroupProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.*;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.BookListGoodsPublishProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.CountBookListModelGroupProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@FeignClient(value = "${application.goods.name}", contextId = "BookListModelProvider")
public interface BookListModelProvider {


    @PostMapping("/goods/${application.goods.version}/booklistmodel/add")
    BaseResponse add(@Validated(BookListModelProviderRequest.Add.class)
                     @RequestBody BookListMixProviderRequest bookListMixProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/update")
    BaseResponse update(@Validated(BookListModelProviderRequest.Update.class)
                        @RequestBody BookListMixProviderRequest bookListMixProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/delete")
    BaseResponse delete(@Validated(BookListModelProviderRequest.Delete.class)
                        @RequestBody BookListModelProviderRequest bookListModel);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/listByPage")
    BaseResponse<MicroServicePage<BookListModelProviderResponse>> listByPage(
                        @RequestBody BookListModelPageProviderRequest bookListModelPageProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/publish")
    BaseResponse publish(@Validated(BookListModelProviderRequest.Publish.class)
                         @RequestBody BookListModelProviderRequest bookListModelProviderRequest);


    @PostMapping("/goods/${application.goods.version}/booklistmodel/findSimpleById")
    BaseResponse<BookListModelProviderResponse> findSimpleById(
            @Validated(BookListModelProviderRequest.FindById.class)
            @RequestBody BookListModelProviderRequest bookListModelProviderRequest);

    @PostMapping("/goods/${application.goods.version}/booklistmodel/findByIds")
    BaseResponse<List<BookListModelProviderResponse>> findByIds(
            @RequestBody RankListByBookListModelIdsRequest rankListByBookListModelIdsRequest);

    /**
     * 根据id获取 书单模版详细信息【这里是获取的书单不一定发布】
     * @param bookListModelProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/findById")
    BaseResponse<BookListMixProviderResponse> findById(
                        @Validated(BookListModelProviderRequest.FindById.class)
                        @RequestBody BookListModelProviderRequest bookListModelProviderRequest);

    /**
     * 根据id获取 书单模版详细信息 【这里获取的书单是发布的】
     * @param bookListModelIdCollection
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/listPublishGoodsByModelIds")
    BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByModelIds(
            @NotNull @RequestBody Collection<Integer> bookListModelIdCollection);

    /**
     * 根据不同的类型获取推荐的书单信息
     * @param businessTypeId
     * @param spuId
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/booklistmodel/listBusinessTypeBookListModel/{businessTypeId}/{spuId}/{size}")
    BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBusinessTypeBookListModel(
            @PathVariable("businessTypeId") Integer businessTypeId, @PathVariable("spuId") String spuId, @PathVariable("size") Integer size);


    /**
     * 根据书单 获取书单的分类的父级别分类下的所有自己别分类对应的 书单列表信息
     * @param bookListModelId
     * @param businessTypeId
     * @param size
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/booklistmodel/listBookListModelMore/{businessTypeId}/{bookListModelId}/{size}")
    BaseResponse<List<BookListModelIdAndClassifyIdProviderResponse>> listBookListModelMore(
            @PathVariable("bookListModelId") Integer bookListModelId, @PathVariable("businessTypeId") Integer businessTypeId, @PathVariable("size") Integer size);


    /**
     * 根据bookListId 获取发布商品列表
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/listBookListGoodsPublish")
    BaseResponse<List<BookListGoodsPublishProviderResponse>> listBookListGoodsPublish(
            @Validated @RequestBody BookListGoodsPublishProviderRequest request);

    /**
     * 根据bookListIdList 获取发布商品列表
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/listBookListGoodsPublishByIds")
    BaseResponse<List<RankGoodsPublishResponse>> listBookListGoodsPublishByIds(
            @RequestBody GoodsIdsByRankListIdsRequest request);

    /**
     * 根据bookListId获取发布商品列表V2
     * @param id
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/listBookListGoodsPublishByIdV2")
    BaseResponse<List<RankGoodsPublishResponse>> getPublishGoodsById(
            @RequestParam Integer id);

    /**
     * 根据商品spuId列表 获取书单列表和商品列表
     * @param bookListModelBySpuIdCollQueryRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/listBookListModelNoPageBySpuIdColl")
    BaseResponse<List<BookListModelGoodsIdProviderResponse>> listBookListModelNoPageBySpuIdColl(
            @Validated @RequestBody BookListModelBySpuIdCollQueryRequest bookListModelBySpuIdCollQueryRequest);

    /**
     * 根据书单id 进行置顶 或者取消置顶操作操作 0表示取消置顶 1表示置顶
     * @param bookListModelId
     * @param hasTop
     * @return
     */
    @GetMapping("/goods/${application.goods.version}/booklistmodel/top/{bookListModelId}/{hasTop}")
    BaseResponse top(@PathVariable("bookListModelId") Integer bookListModelId, @PathVariable("hasTop") Integer hasTop);

    /**
     * 获取已发布的乱序书单Id简单信息
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/findPublishBook")
    BaseResponse<List<Integer>> findPublishBook();

    /**
     * 分组获取各个分类下的商品数量
     * @param countBookListModelGroupProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/booklistmodel/countGroupByBookListModelIdList")
    BaseResponse<List<CountBookListModelGroupProviderResponse>> countGroupByBookListModelIdList(@RequestBody CountBookListModelGroupProviderRequest countBookListModelGroupProviderRequest);

}
