package com.wanmi.sbc.home.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCountResponse;
import com.wanmi.sbc.booklistmodel.response.BookListModelAndGoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.ImageTypeEnum;
import com.wanmi.sbc.goods.api.enums.IndexModuleEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.enums.UsingStateEnum;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.provider.notice.NoticeProvider;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CountBookListModelGroupProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.api.request.notice.NoticePageProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistgoodspublish.CountBookListModelGroupProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureVo;
import com.wanmi.sbc.goods.api.response.index.IndexModuleVo;
import com.wanmi.sbc.goods.api.response.notice.NoticeProviderResponse;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.home.response.HomeBookListRecommendSubResponse;
import com.wanmi.sbc.home.response.HomeGoodsListResponse;
import com.wanmi.sbc.home.response.HomeGoodsListSubResponse;
import com.wanmi.sbc.home.response.HomeImageResponse;
import com.wanmi.sbc.home.response.HomeBookListRecommendResponse;
import com.wanmi.sbc.home.response.HomeSpecialTopicResponse;
import com.wanmi.sbc.home.response.HomeTopicResponse;
import com.wanmi.sbc.home.response.ImageResponse;
import com.wanmi.sbc.home.response.NoticeResponse;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.util.RandomUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
 * Date       : 2021/10/26 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class HomePageService {

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

//    @Autowired
//    private RedisTemplate redisTemplate;

    @Autowired
    private ImageProvider imageProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private IndexCmsProvider indexCmsProvider;

    @Autowired
    private NoticeProvider noticeProvider;

    @Autowired
    private RedisListService redisService;

    /**
     * 基础失效时间
     */
    private final static int EXPIRE_BASE_TIME = 30;

    private final static int EXPIRE_BASE_TIME_RANGE_MIN = -5;

    private final static int EXPIRE_BASE_TIME_RANGE_MAX = 5;


    /**
     * banner
     * @return
     */
    public HomeImageResponse banner() {
        HomeImageResponse homeImageResponse = new HomeImageResponse();
        List<ImageResponse> rotationChartImgList = new ArrayList<>();
        List<ImageResponse> advertImgList = new ArrayList<>();
        homeImageResponse.setRotationChartImageList(rotationChartImgList);
        homeImageResponse.setAdvertImageList(advertImgList);

        ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
        imagePageProviderRequest.setPublishState(UsingStateEnum.USING.getCode());
        imagePageProviderRequest.setStatus(StateEnum.RUNNING.getCode());
        imagePageProviderRequest.setImageTypeList(Arrays.asList(ImageTypeEnum.ROTATION_CHART_IMG.getCode(), ImageTypeEnum.ADVERT_IMG.getCode(), ImageTypeEnum.SELL_IMG.getCode()));
        BaseResponse<List<ImageProviderResponse>> listBaseResponse = imageProvider.listNoPage(imagePageProviderRequest);
        List<ImageProviderResponse> context = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return homeImageResponse;
        }

        for (ImageProviderResponse imageProviderParam : context) {
            ImageResponse imageResponse = new ImageResponse();
            BeanUtils.copyProperties(imageProviderParam, imageResponse);
            if (Objects.equals(imageProviderParam.getImageType(), ImageTypeEnum.ROTATION_CHART_IMG.getCode())) {
                rotationChartImgList.add(imageResponse);
            } else {
                advertImgList.add(imageResponse);
            }
        }
        return homeImageResponse;
    }


    /**
     * 获取特色栏目
     * @return
     */
    public List<HomeSpecialTopicResponse> homeSpecialTopic() {
        List<HomeSpecialTopicResponse> result = new ArrayList<>();
        CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest = new CmsSpecialTopicSearchRequest();
        cmsSpecialTopicSearchRequest.setPublishState(PublishState.ENABLE.toValue());
        cmsSpecialTopicSearchRequest.setState(StateEnum.RUNNING.getCode());
        BaseResponse<List<IndexFeatureVo>> listBaseResponse = indexCmsProvider.listNoPageSpecialTopic(cmsSpecialTopicSearchRequest);
        List<IndexFeatureVo> context = listBaseResponse.getContext();
        for (IndexFeatureVo indexFeatureParam : context) {
            HomeSpecialTopicResponse homeSpecialTopicResponse = new HomeSpecialTopicResponse();
            BeanUtils.copyProperties(indexFeatureParam, homeSpecialTopicResponse);
            result.add(homeSpecialTopicResponse);
        }
        return result;
    }

    /**
     * 获取主副标题
     * @return
     */
    private Map<String, HomeTopicResponse> getHomeTopic() {
        BaseResponse<List<IndexModuleVo>> indexModuleVoResponse = indexCmsProvider.searchTitle();
        List<IndexModuleVo> context = indexModuleVoResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return new HashMap<>();
        }
        Map<String, HomeTopicResponse> code2IndexModuleMap =
                context.stream().filter(ex -> Objects.equals(ex.getPublishState(), PublishState.ENABLE.toValue()))
                        .map(ex -> {
                            HomeTopicResponse homeTopicResponse = new HomeTopicResponse();
                            BeanUtils.copyProperties(ex, homeTopicResponse);
                            return homeTopicResponse;
                        }).collect(Collectors.toMap(HomeTopicResponse::getCode, Function.identity(), (k1, k2) -> k1));
        log.info("HomePageService 获取 主副标题的结果为：{}", code2IndexModuleMap);
        return code2IndexModuleMap;
    }


    /**
     * 获取编辑推荐和名人推荐
     * @return
     */
    public HomeBookListRecommendResponse homeRecommend() {
        HomeBookListRecommendResponse homeRecommend = new HomeBookListRecommendResponse();
        Map<String, HomeTopicResponse> homeTopicMap = this.getHomeTopic();
        HomeTopicResponse editRecommend = homeTopicMap.get(IndexModuleEnum.EDIT_RECOMMEND.getCode());

        BookListModelPageProviderRequest bookListModelPageProviderRequest = new BookListModelPageProviderRequest();
        bookListModelPageProviderRequest.setPageNum(0);
        bookListModelPageProviderRequest.setPageSize(15);
        if (editRecommend != null) {
            try {
                //编辑推荐
                bookListModelPageProviderRequest.setPublishStateList(Collections.singletonList(PublishStateEnum.PUBLISH.getCode()));
                bookListModelPageProviderRequest.setBusinessTypeList(Collections.singletonList(BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
                BaseResponse<MicroServicePage<BookListModelProviderResponse>> microServiceBookRecommend = bookListModelProvider.listByPage(bookListModelPageProviderRequest);
                HomeBookListRecommendSubResponse homeBookListRecommendSubResponse = new HomeBookListRecommendSubResponse();
                homeBookListRecommendSubResponse.setHomeTopicResponse(editRecommend);
                homeBookListRecommendSubResponse.setRecommendList(this.removeInvalidTag(microServiceBookRecommend.getContext().getContent(), BusinessTypeEnum.BOOK_RECOMMEND));
                homeRecommend.setBookListModelRecommend(homeBookListRecommendSubResponse);
            } catch (Exception ex) {
                log.error("HomePageService homeRecommend editRecommend error", ex);
            }
        }

        HomeTopicResponse famousRecommend = homeTopicMap.get(IndexModuleEnum.FAMOUS_RECOMMEND.getCode());
        if (famousRecommend != null) {
            try {
                //名人推荐
                bookListModelPageProviderRequest.setPublishStateList(Collections.singletonList(PublishStateEnum.PUBLISH.getCode()));
                bookListModelPageProviderRequest.setBusinessTypeList(Collections.singletonList(BusinessTypeEnum.FAMOUS_RECOMMEND.getCode()));
                BaseResponse<MicroServicePage<BookListModelProviderResponse>> microServiceFamousRecommend = bookListModelProvider.listByPage(bookListModelPageProviderRequest);

                HomeBookListRecommendSubResponse homeBookListRecommendSubResponse = new HomeBookListRecommendSubResponse();
                homeBookListRecommendSubResponse.setHomeTopicResponse(famousRecommend);
                homeBookListRecommendSubResponse.setRecommendList(this.removeInvalidTag(microServiceFamousRecommend.getContext().getContent(), BusinessTypeEnum.FAMOUS_RECOMMEND));
                homeRecommend.setFamousRecommend(homeBookListRecommendSubResponse);
            } catch (Exception ex) {
                log.error("HomePageService homeRecommend famousRecommend error", ex);
            }
        }
        return homeRecommend;
    }

    /**
     * 删除无效的标签
     * @param bookListModelList
     */
    private List<BookListModelAndGoodsCountResponse> removeInvalidTag(List<BookListModelProviderResponse> bookListModelList, BusinessTypeEnum businessTypeEnum) {
        //获取书单id列表
        Set<Integer> bookListModelIdSet = bookListModelList.stream().map(BookListModelProviderResponse::getId).collect(Collectors.toSet());
        Map<Integer, Integer> bookListModelIdAndCountMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(bookListModelIdSet)) {
            CountBookListModelGroupProviderRequest countBookListModelGroupProviderRequest = new CountBookListModelGroupProviderRequest();
            countBookListModelGroupProviderRequest.setBookListIdCollection(bookListModelIdSet);
            countBookListModelGroupProviderRequest.setBusinessTypeColl(Collections.singletonList(businessTypeEnum.getCode()));
            countBookListModelGroupProviderRequest.setCategoryId(CategoryEnum.BOOK_LIST_MODEL.getCode());
            BaseResponse<List<CountBookListModelGroupProviderResponse>> listBaseResponse =
                    bookListModelProvider.countGroupByBookListModelIdList(countBookListModelGroupProviderRequest);
            bookListModelIdAndCountMap = listBaseResponse.getContext().stream().collect(Collectors.toMap(CountBookListModelGroupProviderResponse::getBookListModelId,
                    CountBookListModelGroupProviderResponse::getGoodsCount, (k1, k2) -> k1));
        }

        List<BookListModelAndGoodsCountResponse> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (BookListModelProviderResponse bookListModelParam : bookListModelList) {
            BookListModelAndGoodsCountResponse bookListModelAndGoodsCountResponse = new BookListModelAndGoodsCountResponse();
            BeanUtils.copyProperties(bookListModelParam, bookListModelAndGoodsCountResponse);
            result.add(bookListModelAndGoodsCountResponse);
            //设置数量
            bookListModelAndGoodsCountResponse.setGoodsCount(bookListModelIdAndCountMap.get(bookListModelParam.getId()) == null ? 0 : bookListModelIdAndCountMap.get(bookListModelParam.getId()));
            if (bookListModelAndGoodsCountResponse.getTagValidBeginTime() == null || bookListModelAndGoodsCountResponse.getTagValidEndTime() == null) {
                continue;
            }
            if (bookListModelAndGoodsCountResponse.getTagValidBeginTime().isAfter(now)
                    || bookListModelAndGoodsCountResponse.getTagValidEndTime().isBefore(now)) {
                bookListModelAndGoodsCountResponse.setTagType(null);
                bookListModelAndGoodsCountResponse.setTagName("");
                bookListModelAndGoodsCountResponse.setTagValidBeginTime(null);
                bookListModelAndGoodsCountResponse.setTagValidEndTime(null);
            }

        }
        return result;
    }

    /**
     * 获取首页 商品列表对应信息
     * @return
     */
    public HomeGoodsListResponse homeGoodsList() {
        int pageSize = 20;
        HomeGoodsListResponse homeGoodsListResponse = new HomeGoodsListResponse();

        //新上
        Map<String, HomeTopicResponse> homeTopicMap = this.getHomeTopic();
        HomeTopicResponse newBooks = homeTopicMap.get(IndexModuleEnum.NEW_BOOKS.getCode());
        try {
            if (newBooks != null) {
                HomeGoodsListSubResponse newBookList = this.newBookList(pageSize);
                newBookList.setHomeTopicResponse(newBooks);
                homeGoodsListResponse.setNewBookGoods(newBookList);
            }
        } catch (Exception ex) {
            log.error("homeGoodsList newBooks exception", ex);
        }

        //畅销
        HomeTopicResponse sellWellBooks = homeTopicMap.get(IndexModuleEnum.SELL_WELL_BOOKS.getCode());
        try {
            if (sellWellBooks != null) {
                HomeGoodsListSubResponse sellWellBookResponse = this.sellWellBookList(pageSize);
                sellWellBookResponse.setHomeTopicResponse(sellWellBooks);
                homeGoodsListResponse.setSellWellGoods(sellWellBookResponse);
            }
        } catch (Exception ex) {
            log.error("homeGoodsList sellWellBooks exception", ex);
        }

       try {
           //特价
           HomeTopicResponse specialOfferBooks = homeTopicMap.get(IndexModuleEnum.SPECIAL_OFFER_BOOKS.getCode());
           if (specialOfferBooks != null) {
               HomeGoodsListSubResponse specialOfferBooksResponse = this.specialOfferBookList(pageSize);
               specialOfferBooksResponse.setHomeTopicResponse(specialOfferBooks);
               homeGoodsListResponse.setSpecialOfferBook(specialOfferBooksResponse);
           }
       } catch (Exception ex) {
           log.error("homeGoodsList specialOfferBooks exception", ex);
       }


       try {
           //非畅销
           HomeTopicResponse unSellWellBooks = homeTopicMap.get(IndexModuleEnum.UN_SELL_WELL_BOOKS.getCode());
           if (unSellWellBooks != null && unSellWellBooks.getBookListModelId() != null) {
               unSellWellBooks.setTitle(unSellWellBooks.getTitle());
               unSellWellBooks.setSubTitle(unSellWellBooks.getSubTitle());
//               //获取书单信息
//               BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
//               bookListModelProviderRequest.setId(unSellWellBooks.getBookListModelId());
//               BaseResponse<BookListModelProviderResponse> bookListModelProviderSimpleByIdResponse = bookListModelProvider.findSimpleById(bookListModelProviderRequest);
//               BookListModelProviderResponse context = bookListModelProviderSimpleByIdResponse.getContext();
//               if (context != null) {
//                   unSellWellBooks.setTitle(context.getName());
//                   unSellWellBooks.setSubTitle(context.getDesc());
//               } else {
//                   throw new SbcRuntimeException("HomePageController 书单id：" + unSellWellBooks.getBookListModelId() + "不存在");
//               }
               BaseResponse<MicroServicePage<GoodsCustomResponse>> microServicePageBaseResponse =
                       bookListModelAndGoodsService.listGoodsByBookListModelId(unSellWellBooks.getBookListModelId(), 0, 20, null);
               List<GoodsCustomResponse> content = microServicePageBaseResponse.getContext().getContent();
               List<BookListModelAndGoodsCustomResponse> bookListModelAndGoodsCustomResponseList = new ArrayList<>();
               for (GoodsCustomResponse goodsCustomParam : content) {
                   BookListModelAndGoodsCustomResponse param = new BookListModelAndGoodsCustomResponse();
                   param.setGoodsCustomVo(goodsCustomParam);
                   bookListModelAndGoodsCustomResponseList.add(param);
               }

               HomeGoodsListSubResponse unSellWellBooksResponse = new HomeGoodsListSubResponse();
               unSellWellBooksResponse.setHomeTopicResponse(unSellWellBooks);
               unSellWellBooksResponse.setBookListModelAndGoodsCustom(bookListModelAndGoodsCustomResponseList);
               homeGoodsListResponse.setUnSellWellGoods(unSellWellBooksResponse);
           }
       } catch (Exception ex) {
           log.error("homeGoodsList unSellWellBooks exception", ex);
       }


        return homeGoodsListResponse;
    }


    /**
     * 新上书籍
     * @param pageSize
     * @return
     */
    private HomeGoodsListSubResponse newBookList(int pageSize) {
        HomeGoodsListSubResponse homeGoodsListSubResponse = new HomeGoodsListSubResponse();

        List<EsGoodsVO> resultTmp = new ArrayList<>();

        List<JSONObject> newBookList = redisService.findByRange(RedisKeyUtil.KEY_HOME_NEW_BOOK_LIST, 0, pageSize);
        if (!CollectionUtils.isEmpty(newBookList)) {
            for (JSONObject goodStr : newBookList) {
                resultTmp.add(JSONObject.toJavaObject(goodStr, EsGoodsVO.class));
            }
        } else {
            //根据书单模版获取商品列表
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(0);
            esGoodsCustomRequest.setPageSize(200);
            List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
            //按照更新时间排序
            sortBuilderList.add(new SortCustomBuilder("createTime", SortOrder.DESC));
            esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
            resultTmp.addAll(bookListModelAndGoodsService.listRandomEsGoodsVo(esGoodsCustomRequest, pageSize));
            if (!CollectionUtils.isEmpty(resultTmp)) {
                //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
                int expire = EXPIRE_BASE_TIME + RandomUtil.getRandomRange(EXPIRE_BASE_TIME_RANGE_MIN, EXPIRE_BASE_TIME_RANGE_MAX);
                redisService.putAll(RedisKeyUtil.KEY_HOME_NEW_BOOK_LIST, resultTmp, expire);
            }
        }

        List<GoodsCustomResponse> result = bookListModelAndGoodsService.listGoodsCustom(resultTmp);
        List<BookListModelAndGoodsCustomResponse> bookListModelAndGoodsCustomList = new ArrayList<>();
        for (GoodsCustomResponse goodsCustomParam : result) {
            BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomResponse = new BookListModelAndGoodsCustomResponse();
            bookListModelAndGoodsCustomResponse.setGoodsCustomVo(goodsCustomParam);
            bookListModelAndGoodsCustomList.add(bookListModelAndGoodsCustomResponse);
        }
        homeGoodsListSubResponse.setBookListModelAndGoodsCustom(bookListModelAndGoodsCustomList);
        return homeGoodsListSubResponse;
    }


    /**
     * 获取畅销榜
     * @param pageSize
     * @return
     */
    private HomeGoodsListSubResponse sellWellBookList(int pageSize){
        HomeGoodsListSubResponse homeGoodsListSubResponse = new HomeGoodsListSubResponse();

        List<EsGoodsVO> resultTmp = new ArrayList<>();

        List<JSONObject> sellWellBookList = redisService.findByRange(RedisKeyUtil.KEY_HOME_SELL_WELL_BOOK_LIST, 0, pageSize);
        if (!CollectionUtils.isEmpty(sellWellBookList)) {
            for (JSONObject goodStr : sellWellBookList) {
                resultTmp.add(JSONObject.toJavaObject(goodStr, EsGoodsVO.class));
            }
        } else {
            //根据书单模版获取商品列表
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(0);
            esGoodsCustomRequest.setPageSize(200);
            List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
            sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
            esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
            resultTmp.addAll(bookListModelAndGoodsService.listRandomEsGoodsVo(esGoodsCustomRequest, pageSize));
            if (!CollectionUtils.isEmpty(resultTmp)) {
                //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
                int expire = EXPIRE_BASE_TIME + RandomUtil.getRandomRange(EXPIRE_BASE_TIME_RANGE_MIN, EXPIRE_BASE_TIME_RANGE_MAX);
                redisService.putAll(RedisKeyUtil.KEY_HOME_SELL_WELL_BOOK_LIST, resultTmp, expire);
            }
        }
        List<GoodsCustomResponse> result = bookListModelAndGoodsService.listGoodsCustom(resultTmp);
        List<BookListModelAndGoodsCustomResponse> bookListModelAndGoodsCustomList = new ArrayList<>();
        for (GoodsCustomResponse goodsCustomParam : result) {
            BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomResponse = new BookListModelAndGoodsCustomResponse();
            bookListModelAndGoodsCustomResponse.setGoodsCustomVo(goodsCustomParam);
            bookListModelAndGoodsCustomList.add(bookListModelAndGoodsCustomResponse);
        }
        homeGoodsListSubResponse.setBookListModelAndGoodsCustom(bookListModelAndGoodsCustomList);
        return homeGoodsListSubResponse;
    }


    /**
     * 特价书
     * @param pageSize
     * @return
     */
    private HomeGoodsListSubResponse specialOfferBookList(int pageSize){
        HomeGoodsListSubResponse homeGoodsListSubResponse = new HomeGoodsListSubResponse();

        List<EsGoodsVO> resultTmp = new ArrayList<>();
        List<JSONObject> specialOfferBookList = redisService.findByRange(RedisKeyUtil.KEY_HOME_SPECIAL_OFFER_BOOK_LIST, 0, pageSize);
        if (!CollectionUtils.isEmpty(specialOfferBookList)) {
            for (JSONObject goodStr : specialOfferBookList) {
                resultTmp.add(JSONObject.toJavaObject(goodStr, EsGoodsVO.class));
            }
        } else {
            //根据书单模版获取商品列表
            EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
            esGoodsCustomRequest.setPageNum(0);
            esGoodsCustomRequest.setPageSize(200);
            esGoodsCustomRequest.setScriptSpecialOffer("0.5");
            resultTmp.addAll(bookListModelAndGoodsService.listRandomEsGoodsVo(esGoodsCustomRequest, pageSize));
            if (!CollectionUtils.isEmpty(resultTmp)) {
                //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
                int expire = EXPIRE_BASE_TIME + RandomUtil.getRandomRange(EXPIRE_BASE_TIME_RANGE_MIN, EXPIRE_BASE_TIME_RANGE_MAX);
                redisService.putAll(RedisKeyUtil.KEY_HOME_SPECIAL_OFFER_BOOK_LIST, resultTmp, expire);
            }
        }
        List<GoodsCustomResponse> result = bookListModelAndGoodsService.listGoodsCustom(resultTmp);
        List<BookListModelAndGoodsCustomResponse> bookListModelAndGoodsCustomList = new ArrayList<>();
        for (GoodsCustomResponse goodsCustomParam : result) {
            BookListModelAndGoodsCustomResponse bookListModelAndGoodsCustomResponse = new BookListModelAndGoodsCustomResponse();
            bookListModelAndGoodsCustomResponse.setGoodsCustomVo(goodsCustomParam);
            bookListModelAndGoodsCustomList.add(bookListModelAndGoodsCustomResponse);
        }
        homeGoodsListSubResponse.setBookListModelAndGoodsCustom(bookListModelAndGoodsCustomList);
        return homeGoodsListSubResponse;
    }



    /**
     * 获取公告
     * @return
     */
    public NoticeResponse notice() {
        NoticePageProviderRequest request = new NoticePageProviderRequest();
        request.setNow(LocalDateTime.now());
        BaseResponse<List<NoticeProviderResponse>> listBaseResponse = noticeProvider.listNoPage(request);
        List<NoticeProviderResponse> context = listBaseResponse.getContext();
        if (!CollectionUtils.isEmpty(context)) {
            NoticeProviderResponse noticeProviderResponse = context.get(0);
            NoticeResponse noticeResponse = new NoticeResponse();
            BeanUtils.copyProperties(noticeProviderResponse, noticeResponse);
            return noticeResponse;
        }
        return null;
    }


}
