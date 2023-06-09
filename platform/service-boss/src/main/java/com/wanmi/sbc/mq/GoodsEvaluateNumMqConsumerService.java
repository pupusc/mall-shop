package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyEvaluateNumRequest;
import com.wanmi.sbc.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName GoodsEvaluateNumMqConsumerService
 * @Description 统计商品评论数mq 消费者service
 * @Author lvzhenwei
 * @Date 2019/4/12 11:18
 **/
@Service
@EnableBinding(GoodsAboutNumSink.class)
public class GoodsEvaluateNumMqConsumerService {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private RedisService redisService;

    @StreamListener(MQConstant.GOODS_EVALUATE_NUM)
    public void goodsEvaluateNumMqConsumer(String msg){
        GoodsModifyEvaluateNumRequest request = JSONObject.parseObject(msg,GoodsModifyEvaluateNumRequest.class);
        goodsProvider.updateGoodsFavorableCommentNum(request);
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(request.getGoodsId()).build());
        //更新redis商品基本数据
        String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoodsId());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoodsId());
        }

    }

}
