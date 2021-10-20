package com.wanmi.sbc.home;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.api.request.goods.SortCustomBuilder;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.home.request.HomeNewBookRequest;
import com.wanmi.sbc.util.RandomUtil;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/18 1:26 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/mobile/home")
@RestController
public class HomePageController {


    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;


    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新上书籍
     */
    private final static String KEY_HOME_NEW_BOOK_LIST = "KEY_HOME_NEW_BOOK_LIST";


    public BaseResponse banner() {
        return BaseResponse.SUCCESSFUL();
    }

    @GetMapping("/book-list-model-recommend")
    public BaseResponse<List<BookListModelProviderResponse>> bookListModelRecommend() {
        BookListModelPageProviderRequest bookListModelPageProviderRequest = new BookListModelPageProviderRequest();
        bookListModelPageProviderRequest.setPageNum(0);
        bookListModelPageProviderRequest.setPageSize(15);
        bookListModelPageProviderRequest.setPublishStateList(Collections.singletonList(PublishStateEnum.PUBLISH.getCode()));
        bookListModelPageProviderRequest.setBusinessTypeList(Collections.singletonList(BusinessTypeEnum.BOOK_RECOMMEND.getCode()));
        BaseResponse<MicroServicePage<BookListModelProviderResponse>> microServicePageBaseResponse = bookListModelProvider.listByPage(bookListModelPageProviderRequest);
        return BaseResponse.success(microServicePageBaseResponse.getContext().getContent());
    }

    /**
     * 获取新上书籍
     * @param homeNewBookRequest
     * @return
     */
    @PostMapping("/newBookList")
    public BaseResponse<List<GoodsCustomResponse>> newBookList(@RequestBody HomeNewBookRequest homeNewBookRequest){
        List<GoodsCustomResponse> result = new ArrayList<>();
        if (homeNewBookRequest.getPageSize() <= 0) {
            return BaseResponse.success(result);
        }

        String newBookListStr = redisTemplate.opsForValue().get(KEY_HOME_NEW_BOOK_LIST) + "";
        if (!StringUtils.isEmpty(newBookListStr)) {
            return BaseResponse.success(JSON.parseArray(newBookListStr, GoodsCustomResponse.class));
        }
        //根据书单模版获取商品列表
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(0);
        esGoodsCustomRequest.setPageSize(200);
        List<SortCustomBuilder> sortBuilderList = new ArrayList<>();
        //按照销售数量排序
        sortBuilderList.add(new SortCustomBuilder("updateTime", SortOrder.DESC));
        esGoodsCustomRequest.setSortBuilderList(sortBuilderList);
        BaseResponse<MicroServicePage<EsGoodsVO>> esGoodsVOMicroServiceResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest);
        MicroServicePage<EsGoodsVO> esGoodsVOMicroServicePage = esGoodsVOMicroServiceResponse.getContext();
        List<EsGoodsVO> content = esGoodsVOMicroServicePage.getContent();
        if (CollectionUtils.isEmpty(content)) {
            return BaseResponse.success(result);
        }

        //获取随机书籍
        Collection<Integer> randomIndex = RandomUtil.getRandom(content.size(), homeNewBookRequest.getPageSize());
        List<EsGoodsVO> resultEsGoodsList = new ArrayList<>();
        for (Integer index : randomIndex) {
            resultEsGoodsList.add(content.get(index));
        }

        List<GoodsVO> goodsVOList = bookListModelAndGoodsService.changeEsGoods2GoodsVo(resultEsGoodsList);
        if (CollectionUtils.isEmpty(goodsVOList)) {
            return BaseResponse.success(result);
        }
        List<GoodsInfoVO> goodsInfoVOList = bookListModelAndGoodsService.packageGoodsInfoList(content, bookListModelAndGoodsService.getCustomerVo());
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            return BaseResponse.success(result);
        }
        Map<String, GoodsVO> supId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (EsGoodsVO esGoodsVO : resultEsGoodsList) {
            result.add(bookListModelAndGoodsService.packageGoodsCustomResponse(supId2GoodsVoMap.get(esGoodsVO.getId()), esGoodsVO, goodsInfoVOList));
        }

        if (!CollectionUtils.isEmpty(result)) {
            //存在缓存击穿的问题，如果经常击穿可以考虑 双key模式
            redisTemplate.opsForValue().set(KEY_HOME_NEW_BOOK_LIST, JSON.toJSONString(result), 30, TimeUnit.MINUTES);
        }
        //书籍存入到redis中
        return BaseResponse.success(result);
    }





}
