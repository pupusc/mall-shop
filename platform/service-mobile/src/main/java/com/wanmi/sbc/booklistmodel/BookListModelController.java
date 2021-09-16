package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelMobileResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.SpecialBookListMobileResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.BookListGoodsPublishProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.BookListGoodsPublishProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 1:40 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Api(tags = "BookListModelController", description = "mobile 查询书单信息")
@RestController
@RequestMapping("/mobile/booklistmodel")
@Slf4j
public class BookListModelController {

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;

    /**
     * 获取榜单
     *
     * @menu 商城详情页
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/list-ranking-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listRankingBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), spuId, 2);
        return BaseResponse.success(listBaseResponse.getContext());
    }

    /**
     * 获取更多榜单
     * @menu 商城详情页
     * @status undone
     * @param spuId
     */
    @GetMapping("/list-ranking-book-list-model/more/{spuId}")
    public BaseResponse<List<BookListModelAndGoodsListResponse>> listRankingBookListModelMore(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), spuId, 4);
        //根据书单列表 获取商品列表信息，
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumList;
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelIdSet =
                bookListModelAndOrderNumList.stream().map(BookListModelAndOrderNumProviderResponse::getBookListModelId).collect(Collectors.toSet());
        Map<String, BookListModelProviderResponse> spuIdBookListModelMap = bookListModelAndGoodsService.mapGoodsIdByBookListModelList(bookListModelIdSet);
        if (spuIdBookListModelMap.isEmpty()) {
            return BaseResponse.success(new ArrayList<>());
        }
        MicroServicePage<BookListModelAndGoodsListResponse> bookListModelAndGoodsListResult =
                bookListModelAndGoodsService.listGoodsBySpuIdAndBookListModel(spuIdBookListModelMap, Collections.singleton(spuId), 0, 3);
        return BaseResponse.success(bookListModelAndGoodsListResult.getContent());
    }


    /**
     *
     * 获取专题
     *
     * @menu 商城详情页
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/special-book-list-model/{spuId}")
    public BaseResponse<List<SpecialBookListMobileResponse>> specialBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.SPECIAL_SUBJECT.getCode(), spuId, 1);
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumResponse = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumResponse)) {
            return BaseResponse.success(new ArrayList<>());
        }
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = bookListModelAndOrderNumResponse.get(0);
        //根据专题封装书单信息
        BookListModelProviderRequest providerRequest = new BookListModelProviderRequest();
        providerRequest.setId(bookListModelAndOrderNumProviderResponse.getBookListModelId());
        BaseResponse<BookListModelProviderResponse> resultTmp = bookListModelProvider.findSimpleById(providerRequest);
        if (resultTmp.getContext() == null) {
            return BaseResponse.success(new ArrayList<>());
        }
        SpecialBookListMobileResponse result = new SpecialBookListMobileResponse();
        BeanUtils.copyProperties(resultTmp.getContext(), result);
        result.setBookListModelId(resultTmp.getContext().getId());
        return BaseResponse.success(Collections.singletonList(result));
    }

    /**
     *
     * 获取推荐
     *
     * @menu 商城详情页
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/list-recommend-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndGoodsListResponse>> listRecommendBookListModel(@PathVariable("spuId") String spuId){

        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), spuId, 2);
        //根据书单列表 获取商品列表信息，
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumList;
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelIdSet =
                bookListModelAndOrderNumList.stream().map(BookListModelAndOrderNumProviderResponse::getBookListModelId).collect(Collectors.toSet());
        return this.packageBookListModelAndGoodsList(spuId, bookListModelIdSet, 3);
    }

    /**
     *
     * 获取更多推荐
     *
     * @menu 商城详情页
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/list-recommend-book-list-model/more/{spuId}")
    public BaseResponse<List<BookListModelAndGoodsListResponse>> listRecommendBookListModelMore(@PathVariable("spuId") String spuId){

        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), spuId, 4);
        //根据书单列表 获取商品列表信息，
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumList;
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelIdSet =
                bookListModelAndOrderNumList.stream().map(BookListModelAndOrderNumProviderResponse::getBookListModelId).collect(Collectors.toSet());
        return this.packageBookListModelAndGoodsList(spuId, bookListModelIdSet, 3);

    }

    /**
     * 封装 书单模版和商品列表信息
     * @menu 商城详情页
     * @param spuId
     * @param bookListModelIdCollection
     * @param size
     * @return
     */
    private BaseResponse<List<BookListModelAndGoodsListResponse>> packageBookListModelAndGoodsList(String spuId, Collection<Integer> bookListModelIdCollection, Integer size){
        Map<String, BookListModelProviderResponse> spuIdBookListModelMap = bookListModelAndGoodsService.mapGoodsIdByBookListModelList(bookListModelIdCollection);
        if (spuIdBookListModelMap.isEmpty()) {
            return BaseResponse.success(new ArrayList<>());
        }
        MicroServicePage<BookListModelAndGoodsListResponse> bookListModelAndGoodsListResult =
                bookListModelAndGoodsService.listGoodsBySpuIdAndBookListModel(spuIdBookListModelMap, Collections.singleton(spuId), 0, size);
        return BaseResponse.success(bookListModelAndGoodsListResult.getContent());
    }

    /**
     * 根据书单模版id 获取模版信息
     * @menu 商城详情页
     * @status undone
     *
     * @param id
     * @return
     */
    @GetMapping("/find-book-list-model-by-id/{bookListModelId}")
    public BaseResponse<BookListModelMobileResponse> findById(@PathVariable("bookListModelId") Integer id){
        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        BaseResponse<BookListModelProviderResponse> bookListModelProviderResponseBaseResponse = bookListModelProvider.findSimpleById(request);
        if (bookListModelProviderResponseBaseResponse.getContext() == null) {
            return BaseResponse.SUCCESSFUL();
        }
        BookListModelMobileResponse response = new BookListModelMobileResponse();
        BeanUtils.copyProperties(bookListModelProviderResponseBaseResponse.getContext(), response);
        return BaseResponse.success(response);
    }

    /**
     * 根据书单模版id 获取书单模版商品列表信息
     *
     * @menu 商城详情页
     * @status undone
     *
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list-goods-by-book-list-model-id/{bookListModelId}/{pageNum}/{pageSize}")
    public BaseResponse<List<GoodsCustomResponse>> listGoodsByBookListModelId(@PathVariable("bookListModelId") Integer id,
                                                                        @PathVariable("pageNum") Integer pageNum,
                                                                        @PathVariable("pageSize") Integer pageSize){

        List<GoodsCustomResponse> goodsCustomResponseList = new ArrayList<>();
        BookListGoodsPublishProviderRequest request = new BookListGoodsPublishProviderRequest();
        request.setBookListIdColl(Collections.singletonList(id));
        request.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
        request.setOperator("duan");
        BaseResponse<List<BookListGoodsPublishProviderResponse>> bookListGoodsPublishProviderResponses = bookListModelProvider.listBookListGoodsPublish(request);
        List<BookListGoodsPublishProviderResponse> context = bookListGoodsPublishProviderResponses.getContext();
        if (!CollectionUtils.isEmpty(context)) {
            //根据书单模版获取商品列表
            Set<String> spuIdSet = context.stream().map(BookListGoodsPublishProviderResponse::getSpuId).collect(Collectors.toSet());
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(pageNum);
            esGoodsCustomRequest.setPageSize(pageSize);
            esGoodsCustomRequest.setGoodIdList(spuIdSet);
            BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
            MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
            List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
            if (CollectionUtils.isEmpty(content)) {
               return BaseResponse.success(goodsCustomResponseList);
            }

            for (EsGoodsVO esGoodsVO : content) {
                goodsCustomResponseList.add(bookListModelAndGoodsService.packageGoodsCustomResponse(esGoodsVO));
            }

        }
        return BaseResponse.success(goodsCustomResponseList);
    }




    public void listRankingAndSee(String spuId){
        //排行榜列表
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), spuId, 1);
        List<BookListModelAndOrderNumProviderResponse> context = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return;
        }
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = context.get(0);
        //获取书单id
        Map<String, BookListModelProviderResponse> spuIdBookListModelMap =
                bookListModelAndGoodsService.mapGoodsIdByBookListModelList(Collections.singletonList(bookListModelAndOrderNumProviderResponse.getBookListModelId()));
        if (!spuIdBookListModelMap.isEmpty()) {
            MicroServicePage<BookListModelAndGoodsListResponse> bookListModelAndGoodsListResult =
                    bookListModelAndGoodsService.listGoodsBySpuIdAndBookListModel(spuIdBookListModelMap, Collections.singleton(spuId), 1, 10);
        }

        //看了又看
        BaseResponse<List<ClassifyGoodsProviderResponse>> listClassifyGoodsAllChildOfParentResponse = classifyProvider.listGoodsIdOfChildOfParentByGoodsId(spuId);
        List<ClassifyGoodsProviderResponse> listClassifyGoodsAllChildOfParent = listClassifyGoodsAllChildOfParentResponse.getContext();
        if (CollectionUtils.isEmpty(listClassifyGoodsAllChildOfParent)) {
            return ;
        }
        Set<String> goodsIdSet = listClassifyGoodsAllChildOfParent.stream().map(ClassifyGoodsProviderResponse::getGoodsId).collect(Collectors.toSet());
        //根据商品id 获取排行榜

        //根据商品id 获取书单



    }
}
