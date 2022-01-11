package com.wanmi.sbc.vote.service;

import com.alibaba.fastjson.JSONArray;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsVoteVo;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.vote.bean.VoteBean;
import com.wanmi.sbc.vote.bean.VoteConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RefreshScope
public class GoodsVoteService {

    @Value("${vote.headimage:null}")
    private String headimage;
    @Autowired
    private VoteConfig voteConfig;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate redisTemplate;

    public String getImage(){
        return headimage;
    }

    public Map<String, List<GoodsVoteVo>> voteGoodsList() {
        Map<String, List<GoodsVoteVo>> result = new LinkedHashMap<>();
        Map<String, VoteBean> voteGoodsMap = voteConfig.getGoods();
        Map<String, String> voteCache = redisService.hgetall(RedisKeyConstant.KEY_GOODS_VOTE_NUMBER);
        voteGoodsMap.forEach((k, v) -> {
            List<GoodsVoteVo> votes = new ArrayList<>(32);
            String systemGoods = v.getSystem();
            if(systemGoods != null) {
                List<String> goodsIdList = JSONArray.parseArray(systemGoods, String.class);
                if(CollectionUtils.isNotEmpty(goodsIdList)){
                    EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
                    queryRequest.setPageNum(0);
                    queryRequest.setPageSize(goodsIdList.size());
                    queryRequest.setGoodsIds(goodsIdList);
                    queryRequest.setQueryGoods(true);
                    queryRequest.setAddedFlag(AddedFlag.YES.toValue());
                    queryRequest.setDelFlag(DeleteFlag.NO.toValue());
                    queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
                    queryRequest.setStoreState(StoreState.OPENING.toValue());
                    queryRequest.setVendibility(Constants.yes);
                    //查询商品
                    List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();

                    Map<String, List<GoodsVoteVo>> collect = esGoodsVOS.stream().map(goods -> {
                        GoodsVoteVo goodsVoteVo = new GoodsVoteVo();
                        goodsVoteVo.setDetailPage(true);
                        goodsVoteVo.setGoodsId(goods.getId());
                        goodsVoteVo.setGoodsName(goods.getGoodsName());
                        goodsVoteVo.setImage(goods.getGoodsInfos() == null ? "" : goods.getGoodsUnBackImg());
                        if (voteCache == null) {
                            goodsVoteVo.setVoteNumber(0L);
                        } else {
                            goodsVoteVo.setVoteNumber(Long.parseLong(voteCache.getOrDefault(goods.getId(), "0")));
                        }
                        return goodsVoteVo;
                    }).collect(Collectors.groupingBy(GoodsVoteVo::getGoodsId));

                    for (String s : goodsIdList) {
                        if(collect.get(s) != null) votes.add(collect.get(s).get(0));
                    }
                }
            }
            String customGoods = v.getCustom();
            if(customGoods != null) {
                List<GoodsVoteVo> goodsVoteVos = JSONArray.parseArray(customGoods, GoodsVoteVo.class);
                for (GoodsVoteVo goodsVoteVo : goodsVoteVos) {
                    goodsVoteVo.setDetailPage(false);
                    if (voteCache == null) {
                        goodsVoteVo.setVoteNumber(0L);
                    } else {
                        goodsVoteVo.setVoteNumber(Long.parseLong(voteCache.getOrDefault(goodsVoteVo.getGoodsId(), "0")));
                    }
                }
                votes.addAll(goodsVoteVos);
            }
            result.put(k, votes);
        });
        return result;
    }

    public void vote(String goodsId) {
        redisTemplate.opsForHash().increment(RedisKeyConstant.KEY_GOODS_VOTE_NUMBER, goodsId, 1L);
    }

}
