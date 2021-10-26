package com.wanmi.sbc.home.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.ImageTypeEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.enums.UsingStateEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import com.wanmi.sbc.home.response.HomeImageResponse;
import com.wanmi.sbc.home.response.HomeBookListRecommendResponse;
import com.wanmi.sbc.home.response.ImageResponse;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class HomePageService {

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ImageProvider imageProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    /**
     * 新上书籍
     */
    private final static String KEY_HOME_NEW_BOOK_LIST = "KEY_HOME_NEW_BOOK_LIST";

    /**
     * 畅销书
     */
    private final static String KEY_HOME_SELL_WELL_BOOK_LIST = "KEY_HOME_SELL_WELL_BOOK_LIST";

    /**
     * 特价书
     */
    private final static String KEY_HOME_SPECIAL_OFFER_BOOK_LIST = "KEY_HOME_SPECIAL_OFFER_BOOK_LIST";

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
        imagePageProviderRequest.setImageTypeList(Arrays.asList(ImageTypeEnum.ROTATION_CHART_IMG.getCode(), ImageTypeEnum.ADVERT_IMG.getCode()));
        BaseResponse<List<ImageProviderResponse>> listBaseResponse = imageProvider.listNoPage(imagePageProviderRequest);
        List<ImageProviderResponse> context = listBaseResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return homeImageResponse;
        }

        for (ImageProviderResponse imageProviderParam : context) {
            ImageResponse imageResponse = new ImageResponse();
            BeanUtils.copyProperties(imageProviderParam, imageResponse);
            if (Objects.equals(imageResponse.getImageType(), ImageTypeEnum.ROTATION_CHART_IMG.getCode())) {
                rotationChartImgList.add(imageResponse);
            } else {
                rotationChartImgList.add(imageResponse);
            }
        }
        return homeImageResponse;
    }


    public void test() {

    }

    /**
     * 获取编辑推荐和名人推荐
     * @return
     */
    public HomeBookListRecommendResponse homeRecommend() {
        HomeBookListRecommendResponse homeRecommend = new HomeBookListRecommendResponse();
        //编辑推荐
        BookListModelPageProviderRequest bookListModelPageProviderRequest = new BookListModelPageProviderRequest();
        bookListModelPageProviderRequest.setPageNum(0);
        bookListModelPageProviderRequest.setPageSize(15);
        bookListModelPageProviderRequest.setPublishStateList(Collections.singletonList(PublishStateEnum.PUBLISH.getCode()));
        bookListModelPageProviderRequest.setBusinessTypeList(Collections.singletonList(BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
        BaseResponse<MicroServicePage<BookListModelProviderResponse>> microServiceBookRecommend = bookListModelProvider.listByPage(bookListModelPageProviderRequest);
        homeRecommend.setBookListModelRecommend(microServiceBookRecommend.getContext().getContent());

        //名人推荐
        bookListModelPageProviderRequest.setPublishStateList(Collections.singletonList(PublishStateEnum.PUBLISH.getCode()));
        bookListModelPageProviderRequest.setBusinessTypeList(Collections.singletonList(BusinessTypeEnum.FAMOUS_RECOMMEND.getCode()));
        BaseResponse<MicroServicePage<BookListModelProviderResponse>> microServiceFamousRecommend = bookListModelProvider.listByPage(bookListModelPageProviderRequest);
        homeRecommend.setFamousRecommend(microServiceFamousRecommend.getContext().getContent());
        return homeRecommend;
    }


    /**
     * 新上书籍
     * @param pageSize
     * @return
     */
    public List<GoodsCustomResponse> newBookList(int pageSize) {
        List<GoodsCustomResponse> result = new ArrayList<>();

        String newBookListStr = redisTemplate.opsForValue().get(KEY_HOME_NEW_BOOK_LIST) + "";
        if (!StringUtils.isEmpty(newBookListStr)) {
            return JSON.parseArray(newBookListStr, GoodsCustomResponse.class);
        }
        //根据书单模版获取商品列表
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(200);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照更新时间排序
        sortBuilderList.add(new SortCustomBuilder("updateTime", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        result.addAll(bookListModelAndGoodsService.listRandomGoodsCustomer(esGoodsCustomRequest, pageSize));
        if (!CollectionUtils.isEmpty(result)) {
            //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
            redisTemplate.opsForValue().set(KEY_HOME_NEW_BOOK_LIST, JSON.toJSONString(result), 30L, TimeUnit.MINUTES);
        }
        return result;
    }


    /**
     * 获取畅销榜
     * @param pageSize
     * @return
     */
    public List<GoodsCustomResponse> sellWellBookList(int pageSize){
        List<GoodsCustomResponse> result = new ArrayList<>();

        String sellWellListStr = redisTemplate.opsForValue().get(KEY_HOME_SELL_WELL_BOOK_LIST) + "";
        if (!StringUtils.isEmpty(sellWellListStr)) {
            return JSON.parseArray(sellWellListStr, GoodsCustomResponse.class);
        }
        //根据书单模版获取商品列表
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(200);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序 7 天 TODO
        sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        result.addAll(bookListModelAndGoodsService.listRandomGoodsCustomer(esGoodsCustomRequest, pageSize));
        if (!CollectionUtils.isEmpty(result)) {
            //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
            redisTemplate.opsForValue().set(KEY_HOME_SELL_WELL_BOOK_LIST, JSON.toJSONString(result), 30L, TimeUnit.MINUTES);
        }
        //书籍存入到redis中
        return result;
    }


    /**
     * 特价书
     * @param pageSize
     * @return
     */
    public List<GoodsCustomResponse> specialOfferBookList(int pageSize){
        List<GoodsCustomResponse> result = new ArrayList<>();

        String sellWellListStr = redisTemplate.opsForValue().get(KEY_HOME_SPECIAL_OFFER_BOOK_LIST) + "";
        if (!StringUtils.isEmpty(sellWellListStr)) {
            return JSON.parseArray(sellWellListStr, GoodsCustomResponse.class);
        }
        //根据书单模版获取商品列表
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(200);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序 7 天 TODO
        sortBuilderList.add(new SortCustomBuilder("goodsSalesNum", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        result.addAll(bookListModelAndGoodsService.listRandomGoodsCustomer(esGoodsCustomRequest, pageSize));
        if (!CollectionUtils.isEmpty(result)) {
            //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
            redisTemplate.opsForValue().set(KEY_HOME_SPECIAL_OFFER_BOOK_LIST, JSON.toJSONString(result), 30L, TimeUnit.MINUTES);
        }
        //书籍存入到redis中
        return result;
    }


    public void notice() {

    }


}
