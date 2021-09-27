package com.wanmi.sbc.task;

import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.hotgoods.HotGoodsProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.bean.dto.HotGoodsDto;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.redis.RedisListService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@JobHandler(value = "homeIndexGoodsJobHandler")
@Component
@EnableBinding
@Slf4j
public class HomeIndexGoodsJobHandler extends IJobHandler {

    @Autowired
    private HotGoodsProvider hotGoodsProvider;
    @Autowired
    private RedisListService redisService;
    @Autowired
    private EsGoodsCustomQueryProvider goodsCustomQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private  BookListModelProvider bookListModelProvider;

    @Override
    public ReturnT<String> execute(String paramStr) {
        //刷新排序
        hotGoodsProvider.updateSort();
        List<HotGoodsDto> hotGoods = hotGoodsProvider.selectAllBySort().getContext();
        Map<String, Integer> sortMap = hotGoods.stream().collect(Collectors.toMap(HotGoodsDto::getId, HotGoodsDto::getSort, (a1, a2) -> a1));

        List<String> goodIds = hotGoods.stream().filter(hotGood -> hotGood.getType() == 1).map(hotGood -> hotGood.getSpuId()).collect(Collectors.toList());
        List<String> bookIds = hotGoods.stream().filter(hotGood -> hotGood.getType() == 2).map(hotGood -> hotGood.getSpuId()).collect(Collectors.toList());
        //根据商品id列表 获取商品列表信息
        EsGoodsCustomQueryProviderRequest esGoodsCustomRequest = new EsGoodsCustomQueryProviderRequest();
        esGoodsCustomRequest.setPageNum(1);
        esGoodsCustomRequest.setPageSize(goodIds.size()); //这里主要是为啦防止书单里面的数量过分的多的情况，限制最多100个
        esGoodsCustomRequest.setGoodIdList(goodIds);
        List<EsGoodsVO> esGoodsVOS = goodsCustomQueryProvider.listEsGoodsNormal(esGoodsCustomRequest).getContext().getContent();
        List<GoodsVO> goodsVOList = bookListModelAndGoodsService.changeEsGoods2GoodsVo(esGoodsVOS);
        Map<String, GoodsVO> spuId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));

        List<GoodsInfoVO> goodsInfoVOList = bookListModelAndGoodsService.packageGoodsInfoList(esGoodsVOS, bookListModelAndGoodsService.getCustomerVo());

        List<SortGoodsCustomResponse> goodList = new ArrayList<>();
        for (EsGoodsVO goodsVo:esGoodsVOS) {
            SortGoodsCustomResponse goodsCustomResponse = (SortGoodsCustomResponse) bookListModelAndGoodsService
                    .packageGoodsCustomResponse(spuId2GoodsVoMap.get(goodsVo.getId()), goodsVo, goodsInfoVOList);
            goodsCustomResponse.setSort(sortMap.get(goodsVo.getId()));
            goodList.add(goodsCustomResponse);
        }
        goodList.sort(Comparator.comparing(SortGoodsCustomResponse::getSort).reversed());
        List<SortGoodsCustomResponse> bookList = new ArrayList<>();
        for (String bookId:bookIds) {
            BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
            bookListModelProviderRequest.setId(Integer.valueOf(bookId));
            BookListModelProviderResponse bookListModelProviderResponse = bookListModelProvider.findSimpleById(bookListModelProviderRequest).getContext();
            SortGoodsCustomResponse goodsCustomResponse = packageGoodsCustomResponse(bookListModelProviderResponse);
            goodsCustomResponse.setSort(sortMap.get(bookId));
            bookList.add(goodsCustomResponse);
        }
        bookList.sort(Comparator.comparing(SortGoodsCustomResponse::getSort).reversed());
        redisService.putAll("hotGoods", goodList, 30);
        redisService.putAll("hotBooks", bookList, 30);
        return SUCCESS;
    }






    private SortGoodsCustomResponse packageGoodsCustomResponse(BookListModelProviderResponse bookListModelProviderResponse) {
        SortGoodsCustomResponse goodsCustomResponse = new SortGoodsCustomResponse();
        goodsCustomResponse.setGoodsId(bookListModelProviderResponse.getId().toString());
        goodsCustomResponse.setGoodsName(bookListModelProviderResponse.getName());
        goodsCustomResponse.setGoodsCoverImg(bookListModelProviderResponse.getHeadImgUrl());
        goodsCustomResponse.setGoodsSubName(bookListModelProviderResponse.getDesc());
        return goodsCustomResponse;
    }

}
