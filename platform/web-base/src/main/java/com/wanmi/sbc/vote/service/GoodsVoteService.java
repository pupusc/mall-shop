package com.wanmi.sbc.vote.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RefreshScope
public class GoodsVoteService {

    @Value("${vote.headimage:null}")
    private String headimage;
    @Value("${vote.goods.list:[]}")
    private String goodsIds;


    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate redisTemplate;

    public String getImage(){
        return headimage;
    }

    public List<GoodsVoteVo> voteGoodsList() {
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        List<String> goodsIdList = JSONArray.parseArray(goodsIds, String.class);
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

        Map<String, String> voteCache = redisService.hgetall(RedisKeyConstant.KEY_GOODS_VOTE_NUMBER);

        List<GoodsVoteVo> votes = esGoodsVOS.stream().map(goods -> {
            GoodsVoteVo goodsVoteVo = new GoodsVoteVo();
            goodsVoteVo.setGoodsId(goods.getId());
            goodsVoteVo.setGoodsName(goods.getGoodsName());
            goodsVoteVo.setImage(goods.getGoodsInfos() == null ? "" : goods.getGoodsInfos().get(0).getImageUrl());
            goodsVoteVo.setVoteNumber(Long.parseLong(voteCache.getOrDefault(goods.getId(), "0")));
            return goodsVoteVo;
        }).collect(Collectors.toList());
        return votes;
    }

    public void vote(String goodsId) {
        redisTemplate.opsForHash().increment(RedisKeyConstant.KEY_GOODS_VOTE_NUMBER, goodsId, 1L);
    }
}
