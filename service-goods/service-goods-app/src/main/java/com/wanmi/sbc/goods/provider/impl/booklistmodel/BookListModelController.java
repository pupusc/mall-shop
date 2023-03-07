package com.wanmi.sbc.goods.provider.impl.booklistmodel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.BookListGoodsPublishProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CountBookListModelGroupProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.*;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.BookListGoodsPublishProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.CountBookListModelGroupProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.*;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishV2DTO;
import com.wanmi.sbc.goods.booklistgoodspublish.response.CountBookListModelGroupResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import com.wanmi.sbc.goods.collect.respository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 5:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@RestController
@Slf4j
public class BookListModelController implements BookListModelProvider {

    @Autowired
    private BookListModelService bookListModelService;

    @Autowired
    private BookListGoodsPublishService bookListGoodsPublishService;

    @Autowired
    private BookRepository bookRepository;

    /**
     * 新增书单模版
     * @param bookListMixProviderRequest
     * @return
     */
    @Override
    public BaseResponse add(BookListMixProviderRequest bookListMixProviderRequest) {
        bookListModelService.add(bookListMixProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改书单模版
     * @param bookListMixProviderRequest
     * @return
     */
    @Override
    public BaseResponse update(BookListMixProviderRequest bookListMixProviderRequest) {
        bookListModelService.update(bookListMixProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除书单
     * @param bookListModel
     * @return
     */
    @Override
    public BaseResponse delete(BookListModelProviderRequest bookListModel) {

        bookListModelService.delete(bookListModel.getId(), bookListModel.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发布书单
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse publish(BookListModelProviderRequest bookListModelProviderRequest) {
        bookListModelService.publish(bookListModelProviderRequest.getId(), bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取书单信息
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse<BookListModelProviderResponse> findSimpleById(BookListModelProviderRequest bookListModelProviderRequest) {
        BookListModelDTO simpleBookListModelDTO = bookListModelService.findSimpleById(bookListModelProviderRequest.getId());
        BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
        BeanUtils.copyProperties(simpleBookListModelDTO, bookListModelProviderResponse);
        return BaseResponse.success(bookListModelProviderResponse);
    }

    /**
     * 根据ids获取书单信息
     * @param rankListByBookListModelIdsRequest
     * @return
     */
    @Override
    public BaseResponse<List<BookListModelProviderResponse>> findByIds(RankListByBookListModelIdsRequest rankListByBookListModelIdsRequest) {
        List<BookListModelDTO> modelDTOS = bookListModelService.findByIds(rankListByBookListModelIdsRequest.getIds());
        List<BookListModelProviderResponse> bookListModelProviderResponses = KsBeanUtil.convertList(modelDTOS, BookListModelProviderResponse.class);
        return BaseResponse.success(bookListModelProviderResponses);
    }

    @Override
    public BaseResponse<List<RankGoodsPublishResponse>> listBookListGoodsPublishByIds(GoodsIdsByRankListIdsRequest request) {
        List<RankGoodsPublishResponse> ids = bookListGoodsPublishService.getIds(request.getIds());
        return BaseResponse.success(ids);
    }

    @Override
    public List<RankGoodsPublishTempResponse> getPublishGoodsById(Integer id) {
        System.out.println("aaaa");
        return bookListGoodsPublishService.getPublishGoodsById(id);
    }

    /**
     * 获取详情 根据id获取 书单模版详细信息【这里是获取的书单不一定发布】
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse<BookListMixProviderResponse> findById(BookListModelProviderRequest bookListModelProviderRequest) {
        BookListMixProviderResponse bookListMixProviderResponse = bookListModelService.findById(bookListModelProviderRequest.getId());
        return BaseResponse.success(bookListMixProviderResponse);
    }


    /**
     * 根据书单id列表获取 书单模版和排序规则 商品列表
     * @param bookListModelIdCollection
     * @return
     */
    @Override
    public BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByModelIds(Collection<Integer> bookListModelIdCollection){
        return BaseResponse.success(bookListModelService.listPublishGoodsByModelIds(bookListModelIdCollection, CategoryEnum.BOOK_LIST_MODEL));
    }

    /**
     * 获取书单模版列表
     * @param bookListModelPageProviderRequest
     * @return
     */
    @Override
    public BaseResponse<MicroServicePage<BookListModelProviderResponse>> listByPage(BookListModelPageProviderRequest bookListModelPageProviderRequest) {
        BookListModelPageRequest bookListModelPageRequest = new BookListModelPageRequest();
        BeanUtils.copyProperties(bookListModelPageProviderRequest, bookListModelPageRequest);
        Page<BookListModelDTO> pageBookListModel = bookListModelService.list(bookListModelPageRequest,
                bookListModelPageProviderRequest.getPageNum(), bookListModelPageProviderRequest.getPageSize());

        LocalDateTime now = LocalDateTime.now();
        List<BookListModelProviderResponse> bookListModelResponseList = pageBookListModel.getContent().stream().map(ex -> {
                                                        BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
                                                        BeanUtils.copyProperties(ex, bookListModelProviderResponse);
                                                        bookListModelProviderResponse.setTagType(null);
                                                        bookListModelProviderResponse.setTagName(null);
                                                        if (ex.getTagValidBeginTime() != null && ex.getTagValidEndTime() != null &&
                                                                ex.getTagValidBeginTime().isBefore(now) && ex.getTagValidEndTime().isAfter(now)) {
                                                            bookListModelProviderResponse.setTagType(ex.getTagType());
                                                            bookListModelProviderResponse.setTagName(ex.getTagName());
                                                        }
                                                        return bookListModelProviderResponse;
                                                    }).collect(Collectors.toList());

        MicroServicePage<BookListModelProviderResponse> microServicePage = new MicroServicePage<>();
        microServicePage.setPageable(pageBookListModel.getPageable());
        microServicePage.setTotal(pageBookListModel.getTotalElements());
        microServicePage.setContent(bookListModelResponseList);
        return BaseResponse.success(microServicePage);
    }

    /**
     * 根据书单id分类获取商品数量
     * @param countBookListModelGroupProviderRequest
     * @return
     */
    @Override
    public BaseResponse<List<CountBookListModelGroupProviderResponse>> countGroupByBookListModelIdList(CountBookListModelGroupProviderRequest countBookListModelGroupProviderRequest) {
        List<CountBookListModelGroupResponse> countBookListModelGroupResponses = bookListGoodsPublishService.countByBookListModelList(countBookListModelGroupProviderRequest);
        List<CountBookListModelGroupProviderResponse> result = new ArrayList<>();
        for (CountBookListModelGroupResponse countBookListModelGroupParam : countBookListModelGroupResponses) {
            CountBookListModelGroupProviderResponse param = new CountBookListModelGroupProviderResponse();
            BeanUtils.copyProperties(countBookListModelGroupParam, param);
            result.add(param);
        }
        return BaseResponse.success(result);
    }

    @Override
    public String importById(RankGoodsPublishResponse response) {
        int updateCount=0;
        int addCount=0;
        List<RankGoodsPublishResponse> publishGoodsBySkuNo = bookListGoodsPublishService.getPublishGoodsBySkuNo(response.getSkuNo());
        if(null != publishGoodsBySkuNo && publishGoodsBySkuNo.size()!=0){
            //有就更新
            publishGoodsBySkuNo.get(0).setSaleNum(response.getSaleNum());
            publishGoodsBySkuNo.get(0).setRankText(response.getRankText());
            updateCount=bookListGoodsPublishService.updateBookListGoodsPublish(publishGoodsBySkuNo.get(0));
            ;
        }else{
            //没有就新增
            BookListGoodsPublishV2DTO bookListGoodsPublishV2DTO = bookListGoodsPublishService.saveBookListGoodsPublish(response);
            if(null!=bookListGoodsPublishV2DTO){
                addCount++;
            }
        }

        return updateCount+","+addCount;
    }


    /**
     * 获取榜单  书单  推荐 模版列表
     * @param businessTypeId
     * @param spuId
     * @return
     */
    @Override
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBusinessTypeBookListModel(Integer businessTypeId, String spuId, Integer size) {
        return BaseResponse.success(bookListModelService.listBusinessTypeBookListModel(spuId, businessTypeId, size));
    }


    /**
     * 获取更多书单信息 根据分类去推荐出来
     * @param bookListModelId
     * @param businessTypeId
     * @param size
     * @return
     */
    public BaseResponse<List<BookListModelIdAndClassifyIdProviderResponse>> listBookListModelMore(Integer bookListModelId, Integer businessTypeId, Integer size) {
        return BaseResponse.success(bookListModelService.listBookListModelMore(bookListModelId, businessTypeId, size));
    }


    /**
     * 根据bookListId 获取商品列表信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<List<BookListGoodsPublishProviderResponse>> listBookListGoodsPublish(BookListGoodsPublishProviderRequest request) {

        List<BookListGoodsPublishDTO> list =
                bookListGoodsPublishService.list(request.getBookListIdColl(), null, request.getCategoryId(), null, request.getOperator());
        List<BookListGoodsPublishProviderResponse> collect = list.stream().map(ex -> {
            BookListGoodsPublishProviderResponse response = new BookListGoodsPublishProviderResponse();
            BeanUtils.copyProperties(ex, response);
            return response;
        }).collect(Collectors.toList());
        return BaseResponse.success(collect);
    }


    /**
     * 根据商品id列表，获取商品列表对应的书单
     */
    @Override
    public BaseResponse<List<BookListModelGoodsIdProviderResponse>> listBookListModelNoPageBySpuIdColl(BookListModelBySpuIdCollQueryRequest bookListModelBySpuIdCollQueryRequest) {
        return BaseResponse.success(bookListModelService.listBookListModelNoPageBySpuIdColl(bookListModelBySpuIdCollQueryRequest));
    }


    /**
     * 根据书单id 进行置顶或者取消置顶 0取消 1置顶操作 feature_d_v0.02
     * @param bookListModelId
     * @param hasTop
     * @return
     */
    @Override
    public BaseResponse top(Integer bookListModelId, Integer hasTop) {
        return BaseResponse.success(bookListModelService.top(bookListModelId, hasTop));
    }

    /**
     * 获取已发布的书单乱序简单信息
     * @return
     */
    @Override
    public BaseResponse<List<Integer>> findPublishBook() {
        List<BookListModelDTO> list = bookListModelService.findPublishBook();
        List<Integer> collect = list.stream().map(ex -> ex.getId()).collect(Collectors.toList());
        return BaseResponse.success(collect);
    }

    @Override
    public BaseResponse<List> getBookRecommend(String isbnId) {
        return BaseResponse.success(bookRepository.getBookRecommend(isbnId));
    }
}
