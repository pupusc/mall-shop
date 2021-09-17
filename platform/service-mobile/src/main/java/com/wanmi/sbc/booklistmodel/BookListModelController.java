package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.booklistmodel.request.BookListModelGoodsPageRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.booklistmodel.request.RankingPageRequest;
import com.wanmi.sbc.booklistmodel.request.SeePageRequest;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsListResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelMobileResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelSimpleResponse;
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
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.BookListGoodsPublishProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
     * @param bookListModelId
     */
    @GetMapping("/list-ranking-book-list-model/more/{bookListModelId}")
    public BaseResponse<List<BookListModelAndGoodsListResponse>> listRankingBookListModelMore(@PathVariable("bookListModelId") Integer bookListModelId){
        BaseResponse<List<BookListModelIdAndClassifyIdProviderResponse>> listBaseResponse =
                bookListModelProvider.listBookListModelMore(bookListModelId, BusinessTypeEnum.RANKING_LIST.getCode(), 4);
        //根据书单列表 获取商品列表信息，
        List<BookListModelIdAndClassifyIdProviderResponse> bookListModelAndClassifyIdList;
        if (CollectionUtils.isEmpty(bookListModelAndClassifyIdList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelIdSet =
                bookListModelAndClassifyIdList.stream().map(BookListModelIdAndClassifyIdProviderResponse::getBookListModelId).collect(Collectors.toSet());
        return BaseResponse.success(this.packageBookListModelAndGoodsList(null, bookListModelIdSet, 0, 3).getContent());
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
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), spuId, 4);
        //根据书单列表 获取商品列表信息，
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumList;
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelIdSet =
                bookListModelAndOrderNumList.stream().map(BookListModelAndOrderNumProviderResponse::getBookListModelId).collect(Collectors.toSet());
        return BaseResponse.success(this.packageBookListModelAndGoodsList(spuId, bookListModelIdSet, 0, 3).getContent());

    }

    /**
     *
     * 获取更多推荐
     *
     * @menu 商城详情页
     * @status undone
     * @param bookListModelId
     * @return
     */
    @GetMapping("/list-recommend-book-list-model/more/{bookListModelId}")
    public BaseResponse<List<BookListModelAndGoodsListResponse>> listRecommendBookListModelMore(@PathVariable("bookListModelId") Integer bookListModelId){

        BaseResponse<List<BookListModelIdAndClassifyIdProviderResponse>> listBaseResponse =
                bookListModelProvider.listBookListModelMore(bookListModelId, BusinessTypeEnum.BOOK_RECOMMEND.getCode(), 4);
        //根据书单列表 获取商品列表信息，
        List<BookListModelIdAndClassifyIdProviderResponse> bookListModelIdAndClassifyIdList;
        if (CollectionUtils.isEmpty(bookListModelIdAndClassifyIdList = listBaseResponse.getContext())) {
            return BaseResponse.success(new ArrayList<>());
        }
        //获取书单id
        Set<Integer> bookListModelIdSet =
                bookListModelIdAndClassifyIdList.stream().map(BookListModelIdAndClassifyIdProviderResponse::getBookListModelId).collect(Collectors.toSet());
        return BaseResponse.success(this.packageBookListModelAndGoodsList(null, bookListModelIdSet, 0, 3).getContent());
    }

    /**
     * 封装 书单模版和商品列表信息
     * @menu 商城详情页
     * @param spuId
     * @param bookListModelIdCollection
     * @return
     */
    private MicroServicePage<BookListModelAndGoodsListResponse> packageBookListModelAndGoodsList(String spuId, Collection<Integer> bookListModelIdCollection, Integer pageNum, Integer pageSize){
        MicroServicePage<BookListModelAndGoodsListResponse> result = new MicroServicePage<>();
        result.setNumber(pageNum);
        result.setSize(pageSize);
        result.setTotal(0);
        result.setContent(new ArrayList<>());
        Map<String, BookListModelProviderResponse> spuIdBookListModelMap = bookListModelAndGoodsService.mapGoodsIdByBookListModelList(bookListModelIdCollection);
        if (spuIdBookListModelMap.isEmpty()) {
            return result;
        }
        return bookListModelAndGoodsService.listGoodsBySpuIdAndBookListModel(spuIdBookListModelMap, StringUtils.isEmpty(spuId) ? null : Collections.singleton(spuId), pageNum, pageSize);
    }


    /**
     * 获取书单列表
     * @menu 商城详情页
     * @status undone
     * @param bookListModelPageRequest
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<MicroServicePage<BookListModelProviderResponse>> list(@Validated @RequestBody BookListModelPageRequest bookListModelPageRequest) {
        if (bookListModelPageRequest.getBusinessType() == null || BusinessTypeEnum.getByCode(bookListModelPageRequest.getBusinessType()) == null) {
            throw new IllegalArgumentException("参数错误");
        }
        bookListModelPageRequest.setPageNum(bookListModelPageRequest.getPageNum() <= 0 ? 0 : bookListModelPageRequest.getPageNum() -1);
        BookListModelPageProviderRequest requestProvider = new BookListModelPageProviderRequest();
        requestProvider.setPageNum(bookListModelPageRequest.getPageNum());
        requestProvider.setPageSize(bookListModelPageRequest.getPageSize());
        requestProvider.setBusinessType(bookListModelPageRequest.getBusinessType());
        return bookListModelProvider.listByPage(requestProvider);
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
     * @return
     */
    @PostMapping("/list-goods-by-book-list-model-id")
    public BaseResponse<MicroServicePage<GoodsCustomResponse>> listGoodsByBookListModelId(@Validated @RequestBody BookListModelGoodsPageRequest bookListModelGoodsRequest){

        List<GoodsCustomResponse> goodsCustomResponseList = new ArrayList<>();

        MicroServicePage<GoodsCustomResponse> result = new MicroServicePage<>();
        result.setTotal(0L);
        result.setContent(goodsCustomResponseList);
        result.setNumber(bookListModelGoodsRequest.getPageNum());
        result.setSize(bookListModelGoodsRequest.getPageSize());

        BookListGoodsPublishProviderRequest request = new BookListGoodsPublishProviderRequest();
        request.setBookListIdColl(Collections.singletonList(bookListModelGoodsRequest.getBookListModelId()));
        request.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
        request.setOperator("duan");
        BaseResponse<List<BookListGoodsPublishProviderResponse>> bookListGoodsPublishProviderResponses = bookListModelProvider.listBookListGoodsPublish(request);
        List<BookListGoodsPublishProviderResponse> context = bookListGoodsPublishProviderResponses.getContext();
        if (!CollectionUtils.isEmpty(context)) {
            //根据书单模版获取商品列表
            Set<String> spuIdSet = context.stream().map(BookListGoodsPublishProviderResponse::getSpuId).collect(Collectors.toSet());
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(bookListModelGoodsRequest.getPageNum());
            esGoodsCustomRequest.setPageSize(bookListModelGoodsRequest.getPageSize());
            esGoodsCustomRequest.setGoodIdList(spuIdSet);
            BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
            MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
            List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
            if (CollectionUtils.isEmpty(content)) {
               return BaseResponse.success(result);
            }

            result.setTotal(esGoodsVOMicroServicePage.getTotal());
            result.setNumber(esGoodsVOMicroServicePage.getNumber());
            result.setSize(esGoodsVOMicroServicePage.getSize());

            for (EsGoodsVO esGoodsVO : content) {
                goodsCustomResponseList.add(bookListModelAndGoodsService.packageGoodsCustomResponse(esGoodsVO));
            }

        }
        return BaseResponse.success(result);
    }


    /**
     * 获取最新的排行榜的商品列表
     *
     * @menu 商城详情页
     * @status undone
     *
     */
    @PostMapping("/list-ranking")
    public BaseResponse<MicroServicePage<BookListModelAndGoodsListResponse>> listRanking(@Validated @RequestBody RankingPageRequest rankingPageRequest){

        if (StringUtils.isEmpty(rankingPageRequest.getSpuId())) {
            throw new IllegalArgumentException("参数错误");
        }
        rankingPageRequest.setPageNum(rankingPageRequest.getPageNum() <= 0 ? 0 : rankingPageRequest.getPageNum() -1);

        MicroServicePage<BookListModelAndGoodsListResponse> result = new MicroServicePage<>();
        result.setTotal(0);
        result.setSize(rankingPageRequest.getPageSize());
        result.setNumber(rankingPageRequest.getPageNum());
        //排行榜列表
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), rankingPageRequest.getSpuId(), 1);
        List<BookListModelAndOrderNumProviderResponse> context = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return BaseResponse.success(result);
        }
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = context.get(0);

        MicroServicePage<BookListModelAndGoodsListResponse> microServicePageResult = this.packageBookListModelAndGoodsList(
                null, Collections.singletonList(bookListModelAndOrderNumProviderResponse.getBookListModelId()),
                rankingPageRequest.getPageNum(), rankingPageRequest.getPageSize());
        return BaseResponse.success(microServicePageResult);
    }

    /**
     * 看了又看列表
     *
     * @menu 商城详情页
     * @status undone
     * @param seePageRequest
     * @return
     */
    @PostMapping("/list-see")
    public BaseResponse<MicroServicePage<BookListModelAndGoodsCustomResponse>> see(@Validated @RequestBody SeePageRequest seePageRequest) {
        if (StringUtils.isEmpty(seePageRequest.getSpuId())) {
            throw new IllegalArgumentException("参数错误");
        }
        seePageRequest.setPageNum(seePageRequest.getPageNum() <= 0 ? 0 : seePageRequest.getPageNum() -1);
        
        MicroServicePage<BookListModelAndGoodsCustomResponse> result = new MicroServicePage<>();
        result.setTotal(0);
        result.setNumber(seePageRequest.getPageNum());
        result.setSize(seePageRequest.getPageSize());

        BaseResponse<List<ClassifyGoodsProviderResponse>> listClassifyGoodsAllChildOfParentResponse = classifyProvider.listGoodsIdOfChildOfParentByGoodsId(seePageRequest.getSpuId());
        List<ClassifyGoodsProviderResponse> listClassifyGoodsAllChildOfParent = listClassifyGoodsAllChildOfParentResponse.getContext();
        if (CollectionUtils.isEmpty(listClassifyGoodsAllChildOfParent)) {
            return BaseResponse.success(result);
        }
        Collection<String> goodsIdCollection = listClassifyGoodsAllChildOfParent.stream().map(ClassifyGoodsProviderResponse::getGoodsId).collect(Collectors.toSet());
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(seePageRequest.getPageNum());
        esGoodsCustomRequest.setPageSize(seePageRequest.getPageSize());
        esGoodsCustomRequest.setGoodIdList(goodsIdCollection);
        List<SortBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序
        sortBuilderList.add(new FieldSortBuilder("goodsSalesNum").order(SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();

        //设置 分页和总数量
        result.setSize(esGoodsVOMicroServicePage.getSize());
        result.setNumber(esGoodsVOMicroServicePage.getNumber());
        result.setTotal(esGoodsVOMicroServicePage.getTotal());

        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();

        if (CollectionUtils.isEmpty(content)) {
            return BaseResponse.success(result);
        }

        //获取商品id信息
        Collection<String> spuIdCollection = content.stream().map(EsGoodsVO::getId).collect(Collectors.toSet());
        //根据商品id 获取书单信息
        BaseResponse<List<BookListModelGoodsIdProviderResponse>> listBookListModelNoPageBySpuIdCollResponse =
                bookListModelProvider.listBookListModelNoPageBySpuIdColl(spuIdCollection);
        List<BookListModelGoodsIdProviderResponse> listBookListModelNoPageBySpuIdColl = listBookListModelNoPageBySpuIdCollResponse.getContext();
        if (CollectionUtils.isEmpty(listBookListModelNoPageBySpuIdColl)) {
            return BaseResponse.success(result);
        }
        //list转化成map
        Map<String, BookListModelGoodsIdProviderResponse> bookListModelGoodsIdMap =
                listBookListModelNoPageBySpuIdColl.stream().collect(Collectors.toMap(BookListModelGoodsIdProviderResponse::getSpuId, Function.identity(), (k1, k2) -> k1));
        //书单和商品的映射
        List<BookListModelAndGoodsCustomResponse> resultTmp = new ArrayList<>();
        for (EsGoodsVO esGoodsVO : content) {
            BookListModelAndGoodsCustomResponse param = new BookListModelAndGoodsCustomResponse();
            param.setGoodsCustomVo(bookListModelAndGoodsService.packageGoodsCustomResponse(esGoodsVO));
            BookListModelGoodsIdProviderResponse bookListModelGoodsIdProviderResponse = bookListModelGoodsIdMap.get(esGoodsVO.getId());
            if (bookListModelGoodsIdProviderResponse != null) {
                BookListModelSimpleResponse bookListModelSimpleResponse = new BookListModelSimpleResponse();
                BeanUtils.copyProperties(bookListModelGoodsIdProviderResponse, bookListModelSimpleResponse);
                param.setBookListModel(bookListModelSimpleResponse);
            }
            resultTmp.add(param);
        }
        result.setContent(resultTmp);
        return BaseResponse.success(result);
    }
}
