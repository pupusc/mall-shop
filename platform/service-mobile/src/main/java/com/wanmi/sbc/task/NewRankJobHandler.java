package com.wanmi.sbc.task;


import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdsByRankListIdsRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import com.wanmi.sbc.goods.bean.dto.TagsDto;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.RankRedisListResponse;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankRequestListResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.DitaUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@JobHandler(value = "newRankJobHandler")
@Component
@EnableBinding
@Slf4j
public class NewRankJobHandler extends IJobHandler {

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
    private EsSpuNewProvider esSpuNewProvider;



    @Override
    public ReturnT<String> execute(String param) throws Exception {
        //获取首页榜单
        RankRedisListResponse rank = new RankRedisListResponse();
        rank.setRankRequestList(this.rank(param));
        redisService.setString(RedisKeyUtil.HOME_RANK, JSON.toJSONString(rank));
        return SUCCESS;
    }

    public List<RankRequest> rank(String param){
        RankRequestListResponse response = topicConfigProvider.rank();
        List<Integer> idList = response.getRankIds();
        GoodsIdsByRankListIdsRequest idsRequest=new GoodsIdsByRankListIdsRequest();
        idsRequest.setIds(idList);
        List<String> goods=new ArrayList<>();
        List<RankGoodsPublishResponse> baseResponse = bookListModelProvider.listBookListGoodsPublishByIds(idsRequest).getContext();
        if(CollectionUtils.isEmpty(baseResponse)){
            return null;
        }
        //初始化榜单商品树形结构
        idList.forEach(id->{
            List<String> goodIds=new ArrayList<>();
            List<Map> maps=new ArrayList<>();
            baseResponse.stream().filter(item->item.getBookListId().equals(id)&&goodIds.size()<3).forEach(item->{
                goodIds.add(item.getSkuId());
                Map map=new HashMap();
                map.put("spuId",item.getSpuId());
                map.put("rankText",item.getRankText());
                map.put("saleNum",item.getSaleNum());
                maps.add(map);
            });
            goods.addAll(goodIds);
            response.getRankRequestList().stream().filter(r->r.getId().equals(id)).forEach(r->r.setRankList(maps));
        });
        //初始化榜单商品
        List<GoodsCustomResponse> goodsCustomResponses = initGoods(goods,param);
        goodsCustomResponses.forEach(g->{
            EsSpuNewQueryProviderReq req=new EsSpuNewQueryProviderReq();
            List<String> spuid=new ArrayList<>();
            spuid.add(g.getGoodsId());
            req.setSpuIds(spuid);
            TagsDto tagsDto = goodsInfoQueryProvider.getTabsBySpu(g.getGoodsId()).getContext();
            List<TagsDto.Tags> tags = tagsDto.getTags();
            MarketingLabelNewDTO marketingLabel=goodsInfoQueryProvider.getMarketingLabelsBySKu(g.getGoodsInfoId()).getContext();
            List<MarketingLabelNewDTO.Labels> marketinglabels=marketingLabel.getLabels();
            List<EsSpuNewResp> esSpuNewResps = esSpuNewProvider.listNormalEsSpuNew(req).getContext().getContent();
            EsSpuNewResp esSpuNewResp = esSpuNewResps.get(0);
//            String label = g.getGoodsLabelList().get(0);
            response.getRankRequestList().forEach(r->{
                if(null!=r.getRankList()&&r.getRankList().size()>0){
                    r.getRankList().forEach(t->{
                        Map map= (Map) t;
                        if(map.get("spuId").equals(g.getGoodsId())){
                            map.put("label",esSpuNewResp.getSpuLabels());
                            map.put("marketingLabel",marketinglabels);
                            if(DitaUtil.isNotBlank(esSpuNewResp.getPic())){
                                map.put("imageUrl",esSpuNewResp.getPic());
                            }else if(DitaUtil.isNotBlank(esSpuNewResp.getUnBackgroundPic())){
                                map.put("imageUrl",esSpuNewResp.getUnBackgroundPic());
                            }else if(DitaUtil.isNotBlank(g.getImageUrl())){
                                map.put("imageUrl",g.getImageUrl());
                            }else {
                                map.put("imageUrl",g.getGoodsUnBackImg());
                            }
                            map.put("goodsInfoId",g.getGoodsInfoId());
                            map.put("subName",g.getGoodsSubName());
                            map.put("showPrice",g.getShowPrice());
                            map.put("linePrice",g.getLinePrice());
                            map.put("discount",g.getLinePrice().divide(g.getShowPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(10)));
                            map.put("stock",g.getStock());
                            map.put("author",g.getGoodsExtProperties().getAuthor());
                            map.put("publisher",g.getGoodsExtProperties().getPublisher());
                        }
                    });
                }
            });
        });
        return response.getRankRequestList();
    }

    private List<GoodsCustomResponse> initGoods(List<String> goodsInfoIds,String param) {
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
        if(param.equals("H5")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(1));
        }else if(param.equals("MINIPROGRAM")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(2));
        }else if(param.equals("MALL_NORMAL")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(3));
        }else if(param.equals("FDDS_DELIVER")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(4));
        }else if(param.equals("SUPPLIER")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(10));
        }else if(param.equals("PC")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(11));
        }else if(param.equals("APP")){
            queryRequest.setGoodsChannelTypeSet(Collections.singletonList(12));
        }

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
