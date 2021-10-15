package com.wanmi.sbc.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.hotgoods.HotGoodsProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.request.goods.HotGoodsTypeRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.bean.dto.HotGoodsDto;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.index.RefreshConfig;
import com.wanmi.sbc.index.response.ActivityBranchConfigResponse;
import com.wanmi.sbc.index.response.ActivityBranchContentDetailResponse;
import com.wanmi.sbc.index.response.ActivityBranchContentResponse;
import com.wanmi.sbc.index.response.ActivityBranchResponse;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisListService<SortGoodsCustomResponse> redisService;
    @Autowired
    private EsGoodsCustomQueryProvider goodsCustomQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private BookListModelProvider bookListModelProvider;
    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;
    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private RefreshConfig refreshConfig;


    @Override
    public ReturnT<String> execute(String paramStr) throws Exception {
        //刷新排序
        hotGoodsProvider.updateSort();
        List<HotGoodsDto> hotGoods = hotGoodsProvider.selectAllBySort().getContext();
        Map<String, Integer> sortMap = hotGoods.stream().collect(Collectors.toMap(HotGoodsDto::getSpuId, HotGoodsDto::getSort, (a1, a2) -> a1));

        List<String> goodIds = hotGoods.stream().filter(hotGood -> hotGood.getType() == 1).map(hotGood -> hotGood.getSpuId()).collect(Collectors.toList());
        List<String> bookIds = hotGoods.stream().filter(hotGood -> hotGood.getType() == 2).map(hotGood -> hotGood.getSpuId()).collect(Collectors.toList());
        List<SortGoodsCustomResponse> goodList = traneserSortGoodsCustomResponseByHotGoodsDto(goodIds);
        for (SortGoodsCustomResponse goodsVo : goodList) {
            goodsVo.setSort(sortMap.get(goodsVo.getGoodsInfoId()) == null ? 0 : sortMap.get(goodsVo.getGoodsInfoId()));
        }
        goodList.sort(Comparator.comparing(SortGoodsCustomResponse::getSort).reversed());
        List<SortGoodsCustomResponse> bookList = new ArrayList<>();
        for (String bookId : bookIds) {
            BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
            bookListModelProviderRequest.setId(Integer.valueOf(bookId));
            BookListModelProviderResponse bookListModelProviderResponse = bookListModelProvider.findSimpleById(bookListModelProviderRequest).getContext();
            SortGoodsCustomResponse goodsCustomResponse = packageGoodsCustomResponse(bookListModelProviderResponse);
            goodsCustomResponse.setSort(sortMap.get(bookId));
            goodsCustomResponse.setType(2);
            bookList.add(goodsCustomResponse);
        }
        bookList.sort(Comparator.comparing(SortGoodsCustomResponse::getSort).reversed());

        Long refreshHotCount = redisTemplate.opsForValue().increment("refreshHotCount", 1);

        redisService.putAll("hotGoods" + refreshHotCount, KsBeanUtil.convertList(goodList, JSONObject.class), 45);
        redisService.putAll("hotBooks" + refreshHotCount, KsBeanUtil.convertList(bookList, JSONObject.class), 45);
        fenHuiChangRedis();
        return SUCCESS;
    }

    @Autowired
    private RedisService redis;

    /**
     * 分会场缓存数据
     */
    private void fenHuiChangRedis() {
        List<ActivityBranchConfigResponse> branchConfigResponseList = JSONArray.parseArray(refreshConfig.getShopActivityBranchConfig(), ActivityBranchConfigResponse.class);
        List<ActivityBranchContentResponse> branchVenueContents = new ArrayList<>();
        List<Integer> types = new ArrayList<>();
        branchConfigResponseList.forEach(configResponse -> {
            branchVenueContents.addAll(configResponse.getBranchVenueContents());
            types.add(configResponse.getBranchVenueId());
        });
        types.addAll(branchVenueContents.stream().map(content -> content.getType()).collect(Collectors.toList()));

        HotGoodsTypeRequest hotGoodsTypeRequest = new HotGoodsTypeRequest();
        hotGoodsTypeRequest.setTypes(types);
        List<HotGoodsDto> hotGoodsDtos = hotGoodsProvider.selectAllByTypes(hotGoodsTypeRequest).getContext();
        List<String> goodIds = hotGoodsDtos.stream().map(hotGood -> hotGood.getSpuId()).collect(Collectors.toList());
        Map<String, HotGoodsDto> sortMap = hotGoodsDtos.stream().collect(Collectors.toMap(HotGoodsDto::getSpuId, Function.identity(), (a1, a2) -> a1));

        List<SortGoodsCustomResponse> goodList = traneserSortGoodsCustomResponseByHotGoodsDto(goodIds);
        for (SortGoodsCustomResponse goodsVo : goodList) {
            goodsVo.setSort(sortMap.get(goodsVo.getGoodsInfoId()) == null ? 0 : sortMap.get(goodsVo.getGoodsInfoId()).getSort());
            goodsVo.setHotType(sortMap.get(goodsVo.getGoodsInfoId()).getType());
        }
        for (ActivityBranchConfigResponse activityBranchConfigResponse : branchConfigResponseList) {
            //分会场栏目数据
            ActivityBranchResponse activityBranchResponse = new ActivityBranchResponse();
            List<ActivityBranchContentDetailResponse> branchVenueContentList = new ArrayList<>();
            activityBranchConfigResponse.getBranchVenueContents().forEach(content -> {
                        ActivityBranchContentDetailResponse detailResponse = new ActivityBranchContentDetailResponse();
                        detailResponse.setTitle(content.getTitle());
                        detailResponse.setActivityBranchContentResponses(goodList.stream().filter(good -> good.getHotType().equals(content.getType()))
                                .sorted(Comparator.comparing(SortGoodsCustomResponse::getSort)).collect(Collectors.toList()));
                        branchVenueContentList.add(detailResponse);
                    }
            );
            activityBranchResponse.setBranchVenueContents(branchVenueContentList);
            redis.setObj("activityBranch:" + activityBranchConfigResponse.getBranchVenueId(), activityBranchResponse, 30 * 60);
            List<SortGoodsCustomResponse> hots = goodList.stream().filter(good -> good.getHotType().equals(activityBranchConfigResponse.getBranchVenueId()))
                    .sorted(Comparator.comparing(SortGoodsCustomResponse::getSort)).collect(Collectors.toList());
            redisService.putAll("activityBranch:hot:" + activityBranchConfigResponse.getBranchVenueId(), KsBeanUtil.convertList(hots, JSONObject.class), 30);


        }


    }


    private List<SortGoodsCustomResponse> traneserSortGoodsCustomResponseByHotGoodsDto(List<String> goodIds) {
        List<SortGoodsCustomResponse> goodList = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodIds.size()); //这里主要是为啦防止书单里面的数量过分的多的情况，限制最多100个
        queryRequest.setGoodsInfoIds(goodIds);
        //获取会员和等级
        queryRequest.setQueryGoods(true);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setVendibility(Constants.yes);
        List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
        List<GoodsVO> goodsVOList = bookListModelAndGoodsService.changeEsGoods2GoodsVo(esGoodsVOS);
        Map<String, GoodsVO> spuId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        List<GoodsInfoVO> goodsInfoVOList = bookListModelAndGoodsService.packageGoodsInfoList(esGoodsVOS, null);
        for (EsGoodsVO goodsVo : esGoodsVOS) {
            GoodsCustomResponse goodsCustom = bookListModelAndGoodsService
                    .packageGoodsCustomResponse(spuId2GoodsVoMap.get(goodsVo.getId()), goodsVo, goodsInfoVOList);
            SortGoodsCustomResponse goodsCustomResponse = KsBeanUtil.copyPropertiesThird(goodsCustom, SortGoodsCustomResponse.class);
            goodsCustomResponse.setType(1);
            goodList.add(goodsCustomResponse);
        }
        return goodList;
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
