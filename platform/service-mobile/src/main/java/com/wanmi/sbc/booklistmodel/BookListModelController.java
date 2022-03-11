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
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelBySpuIdCollQueryRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.topic.service.AtmosphereService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
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
@Api(tags = "troller", description = "mobile 查询书单信息")
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

    @Autowired
    private CommonUtil commonUtil;



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
        return BaseResponse.success(this.packageBookListModelAndGoodsList(null, bookListModelIdSet, bookListModelAndGoodsService.getIsCounselor(), 0, 3).getContent());
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
        return BaseResponse.success(this.packageBookListModelAndGoodsList(spuId, bookListModelIdSet, bookListModelAndGoodsService.getIsCounselor(), 0, 10).getContent());

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
        return BaseResponse.success(this.packageBookListModelAndGoodsList(null, bookListModelIdSet, bookListModelAndGoodsService.getIsCounselor(), 0, 3).getContent());
    }


    private MicroServicePage<BookListModelAndGoodsListResponse> packageBookListModelAndGoodsList(String spuId, Collection<Integer> bookListModelIdCollection, boolean isCpsSpecial, Integer pageNum, Integer pageSize){
        return bookListModelAndGoodsService.listGoodsBySpuIdAndBookListModel(bookListModelIdCollection, StringUtils.isEmpty(spuId) ? null : Collections.singleton(spuId), isCpsSpecial, pageNum, pageSize);
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
        BookListModelPageProviderRequest requestProvider = new BookListModelPageProviderRequest();
        requestProvider.setPageNum(bookListModelPageRequest.getPageNum());
        requestProvider.setPageSize(bookListModelPageRequest.getPageSize());
        requestProvider.setPublishStateList(Arrays.asList(PublishStateEnum.PUBLISH.getCode()));
        if (bookListModelPageRequest.getBusinessType() != null) {
//            BusinessTypeEnum byCode = BusinessTypeEnum.getByCode(bookListModelPageRequest.getBusinessType());
//            if (byCode == BusinessTypeEnum.BOOK_RECOMMEND || byCode == BusinessTypeEnum.BOOK_LIST) {
//                requestProvider.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), BusinessTypeEnum.BOOK_LIST.getCode()));
//            } else {
                requestProvider.setBusinessTypeList(Collections.singletonList(bookListModelPageRequest.getBusinessType()));
//            }

        }
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
        return bookListModelAndGoodsService.listGoodsByBookListModelId(bookListModelGoodsRequest.getBookListModelId(), bookListModelGoodsRequest.getPageNum(), bookListModelGoodsRequest.getPageSize(), null);
    }


    /**
     * 获取最新的排行榜的商品列表
     *
     * @menu 商城详情页
     * @status undone
     *
     */
    @PostMapping("/list-ranking")
    public BaseResponse<MicroServicePage<BookListModelAndGoodsCustomResponse>> listRanking(@Validated @RequestBody RankingPageRequest rankingPageRequest){

        if (StringUtils.isEmpty(rankingPageRequest.getSpuId())) {
            throw new IllegalArgumentException("参数错误");
        }

        MicroServicePage<BookListModelAndGoodsCustomResponse> result = new MicroServicePage<>();
        result.setTotal(0);
        result.setContent(new ArrayList<>());

        //排行榜列表
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), rankingPageRequest.getSpuId(), 1);
        List<BookListModelAndOrderNumProviderResponse> context = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return BaseResponse.success(result);
        }
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = context.get(0);

        MicroServicePage<BookListModelAndGoodsListResponse> microServicePageResult = this.packageBookListModelAndGoodsList(
                rankingPageRequest.getSpuId(), Collections.singletonList(bookListModelAndOrderNumProviderResponse.getBookListModelId()), bookListModelAndGoodsService.getIsCounselor(),
                0, 100); //当前最大是100个

        result.setTotal(microServicePageResult.getTotal());

        //手动进行分页
        //此处只是获取一个
        if (!CollectionUtils.isEmpty(microServicePageResult.getContent())) {
            BookListModelAndGoodsListResponse bookListModelAndGoodsListModel = microServicePageResult.getContent().get(0);
            if (bookListModelAndGoodsListModel.getBookListModel() == null || CollectionUtils.isEmpty(bookListModelAndGoodsListModel.getGoodsList())) {
                return BaseResponse.success(result);
            }


            int from = rankingPageRequest.getPageNum() * rankingPageRequest.getPageSize();
            int to = (rankingPageRequest.getPageNum() + 1) * rankingPageRequest.getPageSize();
            if (from > bookListModelAndGoodsListModel.getGoodsList().size()) {
                return BaseResponse.success(result);
            } else if (to > bookListModelAndGoodsListModel.getGoodsList().size()) {
                to = bookListModelAndGoodsListModel.getGoodsList().size();
            }
            List<GoodsCustomResponse> goodsCustomResponseList = bookListModelAndGoodsListModel.getGoodsList().subList(from, to);

            //获取商品所属书单
            BookListModelBySpuIdCollQueryRequest bookListModelBySpuIdCollQueryRequest = new BookListModelBySpuIdCollQueryRequest();
            bookListModelBySpuIdCollQueryRequest.setSpuIdCollection(goodsCustomResponseList.stream().map(GoodsCustomResponse::getGoodsId).collect(Collectors.toSet()));
            bookListModelBySpuIdCollQueryRequest.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
            BaseResponse<List<BookListModelGoodsIdProviderResponse>> listBookListModelNoPageBySpuIdCollResponse =
                    bookListModelProvider.listBookListModelNoPageBySpuIdColl(bookListModelBySpuIdCollQueryRequest);
            List<BookListModelGoodsIdProviderResponse> listBookListModelNoPageBySpuIdColl = listBookListModelNoPageBySpuIdCollResponse.getContext();

            //list转化成map
            Map<String, BookListModelGoodsIdProviderResponse> bookListModelGoodsIdMap =
                    listBookListModelNoPageBySpuIdColl.stream().collect(Collectors.toMap(BookListModelGoodsIdProviderResponse::getSpuId, Function.identity(), (k1, k2) -> k1));

            List<BookListModelAndGoodsCustomResponse> resultTmp = new ArrayList<>();
            for (GoodsCustomResponse goodsCustomParam : goodsCustomResponseList) {
                BookListModelAndGoodsCustomResponse param = new BookListModelAndGoodsCustomResponse();
                param.setGoodsCustomVo(goodsCustomParam);
                BookListModelGoodsIdProviderResponse bookListModelGoodsIdProviderResponse = bookListModelGoodsIdMap.get(goodsCustomParam.getGoodsId());
                if (bookListModelGoodsIdProviderResponse != null) {
                    BookListModelSimpleResponse bookListModelSimpleResponse = new BookListModelSimpleResponse();
                    BeanUtils.copyProperties(bookListModelGoodsIdProviderResponse, bookListModelSimpleResponse);
                    param.setBookListModel(bookListModelSimpleResponse);
                }
                resultTmp.add(param);
            }

            result.setContent(resultTmp);
        }

        return BaseResponse.success(result);
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
        esGoodsCustomRequest.setGoodsChannelType(commonUtil.getTerminal().getCode());
        esGoodsCustomRequest.setPageNum(seePageRequest.getPageNum());
        esGoodsCustomRequest.setPageSize(seePageRequest.getPageSize());
        esGoodsCustomRequest.setGoodIdList(goodsIdCollection);
        if (!bookListModelAndGoodsService.getIsCounselor()) {
            esGoodsCustomRequest.setCpsSpecial(0); //设置非知识顾问
        }
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序
        sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
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
        //商品id 对应的书单映射 信息
        Map<String, BookListModelGoodsIdProviderResponse> bookListModelGoodsIdMap = bookListModelAndGoodsService.mapBookLitModelByGoodsIdColl(spuIdCollection);
        //书单和商品的映射
        List<BookListModelAndGoodsCustomResponse> resultTmp = new ArrayList<>();

        Map<String, EsGoodsVO> spuId2EsGoodsVoMap = content.stream().collect(Collectors.toMap(EsGoodsVO::getId, Function.identity(), (k1, k2) -> k1));
        List<GoodsVO> goodsVOList = bookListModelAndGoodsService.changeEsGoods2GoodsVo(content);
        if (CollectionUtils.isEmpty(goodsVOList)) {
            return BaseResponse.success(result);
        }
        List<GoodsInfoVO> goodsInfoVOList = bookListModelAndGoodsService.packageGoodsInfoList(content, bookListModelAndGoodsService.getCustomerVo());
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            return BaseResponse.success(result);
        }

        for (GoodsVO goodsVO : goodsVOList) {
            BookListModelAndGoodsCustomResponse param = new BookListModelAndGoodsCustomResponse();
            param.setGoodsCustomVo(bookListModelAndGoodsService.packageGoodsCustomResponse(goodsVO, spuId2EsGoodsVoMap.get(goodsVO.getGoodsId()), goodsInfoVOList));
            BookListModelGoodsIdProviderResponse bookListModelGoodsIdProviderResponse = bookListModelGoodsIdMap.get(goodsVO.getGoodsId());
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
