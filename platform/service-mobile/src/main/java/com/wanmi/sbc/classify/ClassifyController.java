package com.wanmi.sbc.classify;

import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelSimpleResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.classify.request.ClassifyGoodsAndBookListModelPageRequest;
import com.wanmi.sbc.classify.request.HomeClassifyGoodsAndBookListModelRequest;
import com.wanmi.sbc.classify.response.ClassifyGoodsAndBookListModelResponse;
import com.wanmi.sbc.classify.response.ClassifyNoChildResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BookFlagEnum;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.classify.ClassifyCollectionProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelGoodsIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.index.RefreshConfig;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/18 2:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/classify")
@RestController
@Slf4j
@RefreshScope
public class ClassifyController {

    @Autowired
    private ClassifyProvider classifyProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private RedisListService redisService;

    @Autowired
    private RedisService redis;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private CommonUtil commonUtil;

//    @Value("${exclude-classifyIdStr:000}")
//    private String excludeClassifyIdStr;

    private static final Integer page_size = 80;

    /**
     * 分类上新80条数据KEY
     */
    private static final String CLASS_NEW_KEY = "CLASS:NEW:KEY";
    /**
     * 分类热销80条数据KEY
     */
    private static final String CLASS_HOT_KEY = "CLASS:HOT:KEY";

    @Autowired
    private  RefreshConfig refreshConfig;

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;


    /**
     * 分类获取首页分类信息
     *
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/listClassify/home")
    public BaseResponse<List<ClassifyNoChildResponse>> listHomeClassify() {

        List<ClassifyNoChildResponse> result = new ArrayList<>();
        BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listIndexClassify();
        int maxSize = 6;
        for (ClassifyProviderResponse classifyProviderResponseParam : listBaseResponse.getContext()) {
            if (result.size() >= maxSize) {
                break;
            }
            ClassifyNoChildResponse classifyNoChildResponse = new ClassifyNoChildResponse();
            classifyNoChildResponse.setId(classifyProviderResponseParam.getId());
            classifyNoChildResponse.setClassifyName(classifyProviderResponseParam.getClassifyName());
            result.add(classifyNoChildResponse);
        }
        return BaseResponse.success(result);
    }

    /**
     * 分类获取首页分类信息
     *
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/listClassify/test")
    public BaseResponse<List<ClassifyNoChildResponse>> listHomePage() {
        List<ClassifyNoChildResponse> result = new ArrayList<>();
        BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listIndexClassify();
        int maxSize = 6;
        for (ClassifyProviderResponse classifyProviderResponseParam : listBaseResponse.getContext()) {
            if (result.size() >= maxSize) {
                break;
            }
            ClassifyNoChildResponse classifyNoChildResponse = new ClassifyNoChildResponse();
            classifyNoChildResponse.setId(classifyProviderResponseParam.getId());
            classifyNoChildResponse.setClassifyName(classifyProviderResponseParam.getClassifyName());
            result.add(classifyNoChildResponse);
        }
        return BaseResponse.success(result);
    }

    /**
     * 分类  获取分类信息
     *
     * @return
     * @menu 新版首页
     */
    @GetMapping("/listClassify/root")
    public BaseResponse<List<ClassifyNoChildResponse>> listClassify() {
        List<ClassifyNoChildResponse> result = new ArrayList<>();
        ClassifyCollectionProviderRequest classifyCollectionProviderRequest = new ClassifyCollectionProviderRequest();
        classifyCollectionProviderRequest.setParentIdColl(Collections.singleton(0));
        BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionProviderRequest);
        List<String> excludeClassifyIdList = new ArrayList<>();
        //黑名单
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(Collections.singletonList(GoodsBlackListCategoryEnum.CLASSIFT_AT_BOTTOM.getCode()));
        BaseResponse<GoodsBlackListPageProviderResponse> blackListRes = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest);
        GoodsBlackListPageProviderResponse blackList = blackListRes.getContext();
        if(blackList != null && !CollectionUtils.isEmpty(blackList.getClassifyAtBottomBlackListModel().getSecondClassifyIdList())){
            excludeClassifyIdList = blackList.getClassifyAtBottomBlackListModel().getSecondClassifyIdList();
        }
        for (ClassifyProviderResponse classifyProviderResponseParam : listBaseResponse.getContext()) {
            if (excludeClassifyIdList.contains(classifyProviderResponseParam.getId() + "")) {
                log.info("---->>>> 当前分类页面要排除的 分类id为：{}", classifyProviderResponseParam.getId());
                continue;
            }
            ClassifyNoChildResponse classifyNoChildResponse = new ClassifyNoChildResponse();
            classifyNoChildResponse.setId(classifyProviderResponseParam.getId());
            classifyNoChildResponse.setClassifyName(classifyProviderResponseParam.getClassifyName());
            result.add(classifyNoChildResponse);
        }
        return BaseResponse.success(result);
    }

    /**
     * 分类 获取分类下的 商品列表和 书单
     *
     * @param classifyGoodsAndBookListModelRequest
     * @return
     * @menu 新版首页
     */
    @PostMapping("/index/listClassifyGoodsAndBookListModel")
    public BaseResponse<List<ClassifyGoodsAndBookListModelResponse>> listClassifyGoodsAndBookListModel(@RequestBody @Validated HomeClassifyGoodsAndBookListModelRequest classifyGoodsAndBookListModelRequest) {
        List<ClassifyGoodsAndBookListModelResponse> result = new ArrayList<>();
        int pageNum = classifyGoodsAndBookListModelRequest.getPageNum();
        int pageSize = classifyGoodsAndBookListModelRequest.getPageSize(); //每次加载为 5的倍数
        int radix = 5; //基数

        if (pageSize % radix > 0) {
            throw new IllegalStateException("K-010109");
        }
        int quotient = pageSize / radix; //商
        pageSize = pageSize - quotient; //获取的数量
        String refreshCountStr = redis.getString(RedisKeyUtil.KEY_LIST_PREFIX_INDEX_REFRESH_COUNT);
        long refreshCount = Long.parseLong(StringUtils.isEmpty(refreshCountStr) ? "0" : refreshCountStr);
        //获取商品列表
        String goodsIdKey = RedisKeyUtil.KEY_LIST_PREFIX_INDEX_CLASSIFY_GOODS + "_" + commonUtil.getTerminal().getMessage() + ":" + refreshCount + ":" + classifyGoodsAndBookListModelRequest.getClassifyId();
        int goodsStart = pageNum * pageSize;
        int goodsEnd = (pageNum + 1) * pageSize;
        List<String> goodsIdList = redisService.findByRangeString(goodsIdKey, goodsStart, goodsEnd - 1);

        if (CollectionUtils.isEmpty(goodsIdList)) {
            return BaseResponse.success(result);
        }

        //es获取商品信息，
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(goodsIdList.size());
        esGoodsCustomRequest.setGoodIdList(goodsIdList);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序
        sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        esGoodsCustomRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        MicroServicePage<EsGoodsVO> esGoodsRandomList = bookListModelAndGoodsService.listEsGoodsVo(esGoodsCustomRequest);
        List<EsGoodsVO> content = esGoodsRandomList.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return BaseResponse.success(result);
        }

        List<GoodsCustomResponse> goodsCustomResponsesList = bookListModelAndGoodsService.listGoodsCustom(content);
        if (CollectionUtils.isEmpty(goodsCustomResponsesList)) {
            return BaseResponse.success(result);
        }

        Set<String> goodsIdSet = esGoodsRandomList.stream().map(EsGoodsVO::getId).collect(Collectors.toSet());
        //获取商品所对应的书单
        Map<String, BookListModelGoodsIdProviderResponse> bookListModelGoodsIdProviderResponseMap =
                bookListModelAndGoodsService.mapBookLitModelByGoodsIdColl(goodsIdSet);

        List<BookListModelProviderResponse> resultBookListModelList = new ArrayList<>();
        //获取书单列表
        int bookListModelStart = pageNum * quotient;
        int bookListModelEnd = (pageNum + 1) * quotient;
        String bookListModelKey = RedisKeyUtil.KEY_LIST_PREFIX_INDEX_BOOK_LIST_MODEL + ":" + refreshCount + ":" + classifyGoodsAndBookListModelRequest.getClassifyId();
        List<String> bookListModelIdStrList = redisService.findByRangeString(bookListModelKey, bookListModelStart, bookListModelEnd - 1);
        if (!CollectionUtils.isEmpty(bookListModelIdStrList)) {
            List<Integer> bookListModelIdList = bookListModelIdStrList.stream().map(Integer::parseInt).collect(Collectors.toList());
            //获取书单列表
            BookListModelPageProviderRequest bookListModelPageProviderRequest = new BookListModelPageProviderRequest();
            bookListModelPageProviderRequest.setBusinessTypeList(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode(), BusinessTypeEnum.FAMOUS_RECOMMEND.getCode()));
            bookListModelPageProviderRequest.setIdCollection(bookListModelIdList);
            bookListModelPageProviderRequest.setPageNum(0);
            bookListModelPageProviderRequest.setPageSize(bookListModelIdList.size());
            BaseResponse<MicroServicePage<BookListModelProviderResponse>> microServicePageBaseResponse =
                    bookListModelProvider.listByPage(bookListModelPageProviderRequest);
            resultBookListModelList = microServicePageBaseResponse.getContext().getContent();
        }

        //开始封装数据
        int bookListModelIndex = 0;
        for (int i = 0; i < goodsCustomResponsesList.size(); i++) {
            //根据销量获取商品列表
            ClassifyGoodsAndBookListModelResponse resultClassifyGoods = new ClassifyGoodsAndBookListModelResponse();

            BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomParam = new BookListModelAndGoodsCustomResponse();
            GoodsCustomResponse goodsCustomParam = goodsCustomResponsesList.get(i);
            bookListModelAndGoodsCustomParam.setGoodsCustomVo(goodsCustomParam);
            BookListModelGoodsIdProviderResponse bookListModelGoodsIdProviderResponse = bookListModelGoodsIdProviderResponseMap.get(goodsCustomParam.getGoodsId());
            if (bookListModelGoodsIdProviderResponse != null) {
                BookListModelSimpleResponse bookListModelSimpleResponse = new BookListModelSimpleResponse();
                BeanUtils.copyProperties(bookListModelGoodsIdProviderResponse, bookListModelSimpleResponse);
                bookListModelAndGoodsCustomParam.setBookListModel(bookListModelSimpleResponse);
            }
            resultClassifyGoods.setBookListModelAndGoodsCustomModel(bookListModelAndGoodsCustomParam);
            resultClassifyGoods.setType(1); //商品
            result.add(resultClassifyGoods);

            //表示书单
            if (result.size() % radix == 4 && bookListModelIndex < resultBookListModelList.size()) {
                ClassifyGoodsAndBookListModelResponse classifyBookListModel = new ClassifyGoodsAndBookListModelResponse();
                BookListModelProviderResponse bookListModelProviderResponse = resultBookListModelList.get(bookListModelIndex);
                classifyBookListModel.setBookListModel(bookListModelProviderResponse);
                classifyBookListModel.setType(2); //书单
                result.add(classifyBookListModel);
                bookListModelIndex++;
            }
        }
        return BaseResponse.success(result);
    }

    /**
     * 获取分类页的商品列表
     *
     * @param classifyGoodsAndBookListModelPageRequest
     * @return
     * @menu 新版首页
     */
    @PostMapping("/list-by-classify-id")
    public BaseResponse<MicroServicePage<BookListModelAndGoodsCustomResponse>> listByClassifyId(@Validated @RequestBody ClassifyGoodsAndBookListModelPageRequest classifyGoodsAndBookListModelPageRequest) {
        MicroServicePage<BookListModelAndGoodsCustomResponse> result = new MicroServicePage<>();
        result.setContent(new ArrayList<>());
        result.setTotal(0);
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        //获取当前一级分类下的所有子分类
        if (classifyGoodsAndBookListModelPageRequest.getClassifyId() != null) {
            ClassifyCollectionProviderRequest classifyCollectionProviderRequest = new ClassifyCollectionProviderRequest();
            classifyCollectionProviderRequest.setParentIdColl(Collections.singleton(classifyGoodsAndBookListModelPageRequest.getClassifyId()));
            BaseResponse<List<ClassifyProviderResponse>> listBaseResponse = classifyProvider.listClassifyNoChildByParentId(classifyCollectionProviderRequest);
            if (CollectionUtils.isEmpty(listBaseResponse.getContext())) {
                return BaseResponse.success(result);
            }
            Set<Integer> childClassifySet = listBaseResponse.getContext().stream().map(ClassifyProviderResponse::getId).collect(Collectors.toSet());
            esGoodsCustomRequest.setClassifyIdList(childClassifySet);
        }


        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();

        if (Arrays.asList("1", "2", "3").contains(classifyGoodsAndBookListModelPageRequest.getAnchorPushs())) {
            esGoodsCustomRequest.setAnchorPushs(classifyGoodsAndBookListModelPageRequest.getAnchorPushs());
        } else if ("4".equals(classifyGoodsAndBookListModelPageRequest.getAnchorPushs())) {
            if (redis.hasKey(CLASS_HOT_KEY)) {
                esGoodsCustomRequest.setGoodIdList(Arrays.asList(redis.getString(CLASS_HOT_KEY).split(",")));
            } else {
                //按照销售数量排序
                Long days = new Date().getTime() / (1000 * 3600 * 24);
                String script = String.format(refreshConfig.getRecommendSort(), days);
                esGoodsCustomRequest.setScriptSort(script);
                esGoodsCustomRequest.setScore(7);
                esGoodsCustomRequest.setEsSortPrice(20d);
                esGoodsCustomRequest.setBookFlag(BookFlagEnum.Book.getCode());
                esGoodsCustomRequest.setPageNum(0);
                esGoodsCustomRequest.setPageSize(page_size);
                esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
                MicroServicePage<EsGoodsVO> esGoodsVOMicroServiceResponse = bookListModelAndGoodsService.listEsGoodsVo(esGoodsCustomRequest);
                List<EsGoodsVO> esGoodsVOList = esGoodsVOMicroServiceResponse.getContent();
                List<String> goodIds = esGoodsVOList.stream().map(goodVo -> goodVo.getId()).collect(Collectors.toList());
                esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
                esGoodsCustomRequest.setGoodIdList(goodIds);
                redis.setString(CLASS_HOT_KEY, String.join(",", goodIds), 60 * 10);
            }
        } else if ("5".equals(classifyGoodsAndBookListModelPageRequest.getAnchorPushs())) {
            if (redis.hasKey(CLASS_NEW_KEY)) {
                esGoodsCustomRequest.setGoodIdList(Arrays.asList(redis.getString(CLASS_NEW_KEY).split(",")));
            } else {
                //按照时间排序
                sortBuilderList.add(new SortCustomBuilder("addedTimeNew", SortOrder.DESC));
                esGoodsCustomRequest.setAfterAddedTime(LocalDateTime.now().minus(30, ChronoUnit.DAYS));
                esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
                esGoodsCustomRequest.setBookFlag(BookFlagEnum.Book.getCode());
                esGoodsCustomRequest.setPageSize(page_size);
                esGoodsCustomRequest.setPageNum(0);
                esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
                MicroServicePage<EsGoodsVO> esGoodsVOMicroServiceResponse = bookListModelAndGoodsService.listEsGoodsVo(esGoodsCustomRequest);
                List<EsGoodsVO> esGoodsVOList = esGoodsVOMicroServiceResponse.getContent();
                List<String> goodIds = esGoodsVOList.stream().map(goodVo -> goodVo.getId()).collect(Collectors.toList());
                esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
                esGoodsCustomRequest.setGoodIdList(goodIds);
                redis.setString(CLASS_NEW_KEY, String.join(",", goodIds), 60 * 10);
            }
        }

        esGoodsCustomRequest.setPageNum(classifyGoodsAndBookListModelPageRequest.getPageNum());
        esGoodsCustomRequest.setPageSize(classifyGoodsAndBookListModelPageRequest.getPageSize());
        //拼装条件 0 表示推荐
        if (classifyGoodsAndBookListModelPageRequest.getClassifySelectType() == 0) {
            Long days = new Date().getTime() / (1000 * 3600 * 24);
            String script = String.format(refreshConfig.getRecommendSort(), days);
            esGoodsCustomRequest.setScriptSort(script);
        } else if (classifyGoodsAndBookListModelPageRequest.getClassifySelectType() == 1) {
            //按照评分排序
            sortBuilderList.add(new SortCustomBuilder("goodsExtProps.score", SortOrder.DESC));
            esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        } else if (classifyGoodsAndBookListModelPageRequest.getClassifySelectType() == 2) {
            //按照时间排序
            sortBuilderList.add(new SortCustomBuilder("createTime", SortOrder.DESC));
            esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        } else if (classifyGoodsAndBookListModelPageRequest.getClassifySelectType() == 3) {
            //按照销售数量排序
            sortBuilderList.add(new SortCustomBuilder("goodsSalesNumNew", SortOrder.DESC));
            esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        } else {
            return BaseResponse.success(result);
        }


        MicroServicePage<EsGoodsVO> esGoodsVOMicroServiceResponse = bookListModelAndGoodsService.listEsGoodsVo(esGoodsCustomRequest);
        List<EsGoodsVO> esGoodsVOList = esGoodsVOMicroServiceResponse.getContent();
        if (CollectionUtils.isEmpty(esGoodsVOList)) {
            return BaseResponse.success(result);
        }
        //获取商品书单信息
        Set<String> goodsIdSet = esGoodsVOList.stream().map(EsGoodsVO::getId).collect(Collectors.toSet());
        Map<String, BookListModelGoodsIdProviderResponse> bookListModelGoodsIdProviderResponseMap =
                bookListModelAndGoodsService.mapBookLitModelByGoodsIdColl(goodsIdSet);

        result.setTotal(esGoodsVOMicroServiceResponse.getTotal());
        result.setNumber(esGoodsVOMicroServiceResponse.getNumber());
        result.setSize(esGoodsVOMicroServiceResponse.getSize());
        result.setPageable(esGoodsVOMicroServiceResponse.getPageable());
        List<GoodsCustomResponse> goodsCustomResponsesList = bookListModelAndGoodsService.listGoodsCustom(esGoodsVOList);
        List<BookListModelAndGoodsCustomResponse> resultTmp = new ArrayList<>();
        for (GoodsCustomResponse goodsCustomParam : goodsCustomResponsesList) {
            BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomModel = new BookListModelAndGoodsCustomResponse();
            bookListModelAndGoodsCustomModel.setGoodsCustomVo(goodsCustomParam);
            BookListModelGoodsIdProviderResponse bookListModelGoodsIdModel = bookListModelGoodsIdProviderResponseMap.get(goodsCustomParam.getGoodsId());
            if (bookListModelGoodsIdModel != null) {
                BookListModelSimpleResponse bookListModelSimpleModel = new BookListModelSimpleResponse();
                BeanUtils.copyProperties(bookListModelGoodsIdModel, bookListModelSimpleModel);
                bookListModelAndGoodsCustomModel.setBookListModel(bookListModelSimpleModel);
            }
            resultTmp.add(bookListModelAndGoodsCustomModel);
        }
        result.setContent(resultTmp);
        return BaseResponse.success(result);
    }
}
