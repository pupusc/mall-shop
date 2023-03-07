package com.wanmi.sbc.task;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdsByRankListIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.api.response.RankPageResponse;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.topic.response.RankResponse;
import com.wanmi.sbc.topic.response.TopicResponse;
import com.wanmi.sbc.topic.response.TopicStoreyResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@JobHandler(value = "rankPageJobHandler")
@Component
@EnableBinding
@Slf4j
public class RankPageJobHandler extends IJobHandler {

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisListService redisListService;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        //获取首页榜单
        RankRedisListResponse rank = new RankRedisListResponse();
        TopicQueryRequest request=new TopicQueryRequest();
        request.setTopicKey(param);
        BaseResponse<TopicActivityVO> activityVO =  topicConfigProvider.detail(request);
        TopicResponse response = KsBeanUtil.convert(activityVO.getContext(),TopicResponse.class);
        List<TopicStoreyResponse> storeyList = response.getStoreyList();
        for(TopicStoreyResponse topicResponse:storeyList) {
            Integer storeyType = topicResponse.getStoreyType();
            if(storeyType== TopicStoreyTypeV2.RANKDETAIL.getId()) {//首页榜单
                rankPageByBookList(topicResponse);
            }
        }
//        rank.setRankRequestList(this.rank());
//        redisService.setString(RedisKeyUtil.RANK_PAGE+, JSON.toJSONString(rank));
        return SUCCESS;
    }

    public void rankPageByBookList(TopicStoreyResponse topicResponse){
        RankStoreyRequest request=new RankStoreyRequest();
        request.setTopicStoreyId(topicResponse.getId());
        RankPageResponse pageResponse = topicConfigProvider.rankPageByBookList(request);
        List<Integer> idList = pageResponse.getRankIdList();
        GoodsIdsByRankListIdsRequest idsRequest=new GoodsIdsByRankListIdsRequest();
        idsRequest.setIds(idList);
        List<RankGoodsPublishResponse> baseResponse = bookListModelProvider.listBookListGoodsPublishByIds(idsRequest).getContext();
        List<String> skus= new ArrayList<>();
        baseResponse.forEach(b->{
            if(!skus.contains(b.getSkuId())){
                skus.add(b.getSkuId());
            }
        });
        List<RankRequest> contentList = pageResponse.getPageRequest().getContentList();
        String key=RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":table";
        if(redisListService.getSize(key)>0){
            redisService.delete(key);
        }
        redisListService.putAll(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":table",contentList);
        //初始化榜单树形结构，获取商品详情
        List<GoodsCustomResponse> goodsCustomResponses = initGoods(skus);
        GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
        goodsInfoByIdRequest.setGoodsInfoIds(skus);
        goodsInfoByIdRequest.setIsHavSpecText(1);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(goodsInfoByIdRequest).getContext().getGoodsInfos();
        if(redisListService.getSize(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":goodsInfoList")>0){
            redisService.delete(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":goodsInfoList");
        }
        redisListService.putAll(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":goodsInfoList",goodsInfos);
        pageResponse.getPageRequest().getContentList().forEach(r->{
            r.getRankList().forEach(t->{
                Map tMap= (Map) t;
                List<Map> redisGoods=new ArrayList<>();
                baseResponse.stream().filter(b->b.getBookListId().equals(tMap.get("id"))).forEach(b->{
                    goodsCustomResponses.stream().filter(g->b.getSkuId().equals(g.getGoodsInfoId())).forEach(g->{
                        Map map=new HashMap();
                        map.put("id",g.getGoodsId());
                        map.put("spuNo",g.getGoodsNo());
                        map.put("skuNo",g.getGoodsInfoNo());
                        map.put("imageUrl",g.getGoodsCoverImg());
                        map.put("sorting",b.getOrderNum());
                        map.put("goodsName",g.getGoodsName());
                        map.put("linePrice",g.getLinePrice());
                        map.put("showPrice",g.getShowPrice());
                        if(null!=b.getSaleNum()) {
                            if (b.getSaleNum() >= 10000) {
                                String num = String.valueOf(b.getSaleNum() / 10000) + "万";
                                map.put("saleNum", num);
                            } else {
                                map.put("saleNum", String.valueOf(b.getSaleNum()));
                            }
                        }else {
                            map.put("saleNum", "");
                        }
                        map.put("skuId",b.getSkuId());
                        map.put("spuId",b.getSpuId());
                        map.put("label",g.getLabels());
                        map.put("subName",g.getGoodsSubName());
                        map.put("rankText",b.getRankText());
                        redisGoods.add(map);
                    });
                });
                if(redisListService.getSize(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":listGoods:"+tMap.get("id"))>0){
                    redisService.delete(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":listGoods:"+tMap.get("id"));
                }
                redisListService.putAll(RedisKeyUtil.RANK_PAGE+topicResponse.getId()+":listGoods:"+tMap.get("id"),redisGoods);
            });
        });
    }

    private List<GoodsCustomResponse> initGoods(List<String> goodsInfoIds) {
        List<GoodsCustomResponse> goodList = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodsInfoIds.size());
        queryRequest.setGoodsInfoIds(goodsInfoIds);
        queryRequest.setQueryGoods(true);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        //查询商品
        List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
        List<GoodsCustomResponse> result=  bookListModelAndGoodsService.listGoodsCustom(esGoodsVOS);
        for (EsGoodsVO goodsVo : esGoodsVOS) {
            Optional<GoodsCustomResponse> goodsCustom = result.stream().filter(p->p.getGoodsId().equals(goodsVo.getId())).findFirst();
            if(goodsCustom.isPresent()) {
                goodsVo.getGoodsInfos().forEach(p -> {
                    GoodsCustomResponse goods = new GoodsCustomResponse();
                    goods.setGoodsId(goodsCustom.get().getGoodsId());
                    goods.setGoodsNo(goodsCustom.get().getGoodsNo());
                    goods.setGoodsInfoId(p.getGoodsInfoId());
                    goods.setGoodsInfoNo(p.getGoodsInfoNo());
                    goods.setGoodsName(goodsCustom.get().getGoodsName());
                    goods.setGoodsSubName(goodsCustom.get().getGoodsSubName());
                    goods.setGoodsCoverImg(goodsCustom.get().getGoodsCoverImg());
                    goods.setGoodsUnBackImg(goodsCustom.get().getGoodsUnBackImg());
                    goods.setCpsSpecial(goodsCustom.get().getCpsSpecial());
                    goods.setShowPrice(goodsCustom.get().getShowPrice());
                    goods.setLinePrice(goodsCustom.get().getLinePrice());
                    goods.setMarketingPrice(goodsCustom.get().getMarketingPrice());
                    goods.setCouponLabelList(goodsCustom.get().getCouponLabelList());
                    goods.setGoodsLabelList(goodsCustom.get().getGoodsLabelList());
                    goods.setGoodsScore(goodsCustom.get().getGoodsScore());
                    goods.setStock(goodsCustom.get().getStock());
                    goods.setGoodsExtProperties(goodsCustom.get().getGoodsExtProperties());
                    goods.setLabels(goodsCustom.get().getLabels());
                    goods.setActivities(goodsCustom.get().getActivities());
                    MarketingLabelNewDTO context = goodsInfoQueryProvider.getMarketingLabelsBySKu(goodsCustom.get().getGoodsInfoId()).getContext();
                    if(null!=context){
                        goods.setMarketingLabels(context);
                    }
                    if(p.getStartTime()!=null && p.getEndTime()!=null && p.getStartTime().compareTo(LocalDateTime.now()) <0 && p.getEndTime().compareTo(LocalDateTime.now()) > 0) {
                        goods.setAtmosType(p.getAtmosType());
                        goods.setImageUrl(p.getImageUrl());
                        goods.setElementOne(p.getElementOne());
                        goods.setElementTwo(p.getElementTwo());
                        goods.setElementThree(p.getElementThree());
                        goods.setElementFour(p.getElementFour());
                    }else{
                        goods.setAtmosType(null);
                        goods.setImageUrl(null);
                        goods.setElementOne(null);
                        goods.setElementTwo(null);
                        goods.setElementThree(null);
                        goods.setElementFour(null);
                    }
                    goods.setActivities(goodsCustom.get().getActivities());
                    goodList.add(goods);
                });
            }

        }
        return goodList;

    }
}