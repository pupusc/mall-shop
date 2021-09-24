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
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.FilterRuleEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.chooserule.ChooseRuleProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.BookListGoodsPublishProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.BookListGoodsPublishProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
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
import java.util.Objects;
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

    @Autowired
    private ChooseRuleProvider chooseRuleProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerProvider customerProvider;

    /**
     * 获取是否是知识顾问
     * @return
     */
    private boolean getIsCounselor() {
        String operatorId = commonUtil.getOperatorId();
        if (StringUtils.isEmpty(operatorId)) {
            return false;
        }
        CustomerVO customerVO = commonUtil.getCanNullCustomer();
        if (StringUtils.isEmpty(customerVO.getFanDengUserNo())) {
            return false;
        } else {
            CounselorDto counselorDto = customerProvider.isCounselor(Integer.valueOf(customerVO.getFanDengUserNo())).getContext();
            if (counselorDto == null) {
                return false;
            }
        }
        return true;
    }

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
        return BaseResponse.success(this.packageBookListModelAndGoodsList(null, bookListModelIdSet, this.getIsCounselor(), 0, 3).getContent());
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
        return BaseResponse.success(this.packageBookListModelAndGoodsList(spuId, bookListModelIdSet, this.getIsCounselor(), 0, 10).getContent());

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
        return BaseResponse.success(this.packageBookListModelAndGoodsList(null, bookListModelIdSet, this.getIsCounselor(), 0, 3).getContent());
    }

    /**
     * 封装 书单模版和商品列表信息
     * @menu 商城详情页
     * @param spuId
     * @param bookListModelIdCollection
     * @return
     */
//    private MicroServicePage<BookListModelAndGoodsListResponse> packageBookListModelAndGoodsList(String spuId, Collection<Integer> bookListModelIdCollection, boolean isCpsSpecial, Integer pageNum, Integer pageSize){
//        MicroServicePage<BookListModelAndGoodsListResponse> result = new MicroServicePage<>();
//        result.setNumber(pageNum);
//        result.setSize(pageSize);
//        result.setTotal(0);
//        result.setContent(new ArrayList<>());
//        Map<String, BookListMixProviderResponse> spuId2BookListMixMap = bookListModelAndGoodsService.supId2BookListMixMap(bookListModelIdCollection);
//        if (spuId2BookListMixMap.isEmpty()) {
//            return result;
//        }
//        return bookListModelAndGoodsService.listGoodsBySpuIdAndBookListModel(spuId2BookListMixMap, StringUtils.isEmpty(spuId) ? null : Collections.singleton(spuId), isCpsSpecial, pageNum, pageSize);
//    }


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
        requestProvider.setBusinessTypeList(Arrays.asList(PublishStateEnum.PUBLISH.getCode(), PublishStateEnum.EDIT_UN_PUBLISH.getCode()));
        if (bookListModelPageRequest.getBusinessType() != null) {
            BusinessTypeEnum byCode = BusinessTypeEnum.getByCode(bookListModelPageRequest.getBusinessType());
            if (byCode == BusinessTypeEnum.BOOK_RECOMMEND || byCode == BusinessTypeEnum.BOOK_LIST) {
                requestProvider.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), BusinessTypeEnum.BOOK_LIST.getCode()));
            } else {
                requestProvider.setBusinessTypeList(Collections.singletonList(bookListModelPageRequest.getBusinessType()));
            }

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

//        List<GoodsCustomResponse> goodsCustomResponseList = new ArrayList<>();

        MicroServicePage<GoodsCustomResponse> result = new MicroServicePage<>();
        result.setTotal(0L);
        result.setContent(new ArrayList<>());

        //获取书单信息
        BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
        bookListModelProviderRequest.setId(bookListModelGoodsRequest.getBookListModelId());
        BaseResponse<BookListModelProviderResponse> bookListModelProviderResponseBaseResponse = bookListModelProvider.findSimpleById(bookListModelProviderRequest);
        if (bookListModelProviderResponseBaseResponse.getContext() == null) {
            throw new IllegalArgumentException("书单id不存在");
        }
        //获取控件信息
        ChooseRuleProviderRequest chooseRuleProviderRequest = new ChooseRuleProviderRequest();
        chooseRuleProviderRequest.setBookListModelId(bookListModelGoodsRequest.getBookListModelId());
        chooseRuleProviderRequest.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
        BaseResponse<ChooseRuleProviderResponse> chooseRuleNoGoodsByConditionResponse = chooseRuleProvider.findChooseRuleNoGoodsByCondition(chooseRuleProviderRequest);
        ChooseRuleProviderResponse chooseRuleProviderResponse = chooseRuleNoGoodsByConditionResponse.getContext();
        if (chooseRuleProviderResponse == null) {
            throw new IllegalArgumentException("书单控件不存在");
        }
        BookListGoodsPublishProviderRequest request = new BookListGoodsPublishProviderRequest();
        request.setBookListIdColl(Collections.singletonList(bookListModelGoodsRequest.getBookListModelId()));
        request.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
        request.setOperator("duan");
        BaseResponse<List<BookListGoodsPublishProviderResponse>> bookListGoodsPublishProviderResponses = bookListModelProvider.listBookListGoodsPublish(request);
        List<BookListGoodsPublishProviderResponse> goodsContext = bookListGoodsPublishProviderResponses.getContext();
        if (!CollectionUtils.isEmpty(goodsContext)) {

            //根据书单模版获取商品列表
            Set<String> spuIdSet = goodsContext.stream().map(BookListGoodsPublishProviderResponse::getSpuId).collect(Collectors.toSet());
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(bookListModelGoodsRequest.getPageNum());
            esGoodsCustomRequest.setPageSize(spuIdSet.size()); //TODO 一次性全部查询出来
            esGoodsCustomRequest.setGoodIdList(spuIdSet);
            boolean isCpsSpecial = this.getIsCounselor();
            if (!isCpsSpecial) {
                esGoodsCustomRequest.setCpsSpecial(0); //设置cps
            }
            BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
            MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
            List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
            if (CollectionUtils.isEmpty(content)) {
               return BaseResponse.success(result);
            }

            //esGoodsVo to map
            Map<String, EsGoodsVO> esGoodsId2EsGoodsVoMap = content.stream().collect(Collectors.toMap(EsGoodsVO::getId, Function.identity(), (k1, k2) -> k1));

            List<EsGoodsVO> resultEsGoodsVoList = new ArrayList<>();
            List<EsGoodsVO> resultEsGoodsVoUnStockList = new ArrayList<>();

            int unShowSum = 0;

            for (BookListGoodsPublishProviderResponse bookListGoodsPublishParam : goodsContext) {
                EsGoodsVO esGoodsVOLocal = esGoodsId2EsGoodsVoMap.get(bookListGoodsPublishParam.getSpuId());
                //无效的对象信息
                if (esGoodsVOLocal == null) {
                    unShowSum++;
                    continue;
                }
                //无库存不展示
                if (FilterRuleEnum.getByCode(chooseRuleProviderResponse.getFilterRule()) == FilterRuleEnum.OUT_OF_STOCK_UN_SHOW && esGoodsVOLocal.getStock() <= 0) {
                    unShowSum++;
                    continue;
                }
                //无库存沉底
                if (FilterRuleEnum.getByCode(chooseRuleProviderResponse.getFilterRule()) == FilterRuleEnum.OUT_OF_STOCK_BOTTOM && esGoodsVOLocal.getStock() <= 0) {
                    resultEsGoodsVoUnStockList.add(esGoodsVOLocal);
                    continue;
                }
                resultEsGoodsVoList.add(esGoodsVOLocal);
            }

            resultEsGoodsVoList.addAll(resultEsGoodsVoUnStockList);


            long total = goodsContext.size() - unShowSum;
            total = total <= 0 ? 0 : total;

            int from = bookListModelGoodsRequest.getPageNum() * bookListModelGoodsRequest.getPageSize();
            int to = (bookListModelGoodsRequest.getPageNum() + 1) * bookListModelGoodsRequest.getPageSize();

            result.setTotal(total);
            if (from > total) {
                return BaseResponse.success(result);
            } else if (to > total) {
                to = Integer.parseInt(total+"");
            }

            result.setContent(resultEsGoodsVoList.subList(from, to).stream()
                    .map(ex -> bookListModelAndGoodsService.packageGoodsCustomResponse(ex)).collect(Collectors.toList()));
            return BaseResponse.success(result);

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

        //排行榜列表
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), rankingPageRequest.getSpuId(), 1);
        List<BookListModelAndOrderNumProviderResponse> context = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return BaseResponse.success(null);
        }
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = context.get(0);

        MicroServicePage<BookListModelAndGoodsListResponse> microServicePageResult = this.packageBookListModelAndGoodsList(
                null, Collections.singletonList(bookListModelAndOrderNumProviderResponse.getBookListModelId()), this.getIsCounselor(),
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
        boolean isCpsSpecial = this.getIsCounselor();
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
        if (!isCpsSpecial) {
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
