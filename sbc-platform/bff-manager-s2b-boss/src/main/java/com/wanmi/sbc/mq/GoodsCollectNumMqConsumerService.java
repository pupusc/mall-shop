package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyCollectNumRequest;
import com.wanmi.sbc.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName GoodsCollectNumMqConsumerService
 * @Description 统计商品收藏量mq 消费者service
 * @Author lvzhenwei
 * @Date 2019/4/12 10:47
 **/
@Service
@EnableBinding(GoodsAboutNumSink.class)
public class GoodsCollectNumMqConsumerService {

    @Autowired
    private GoodsProvider goodsProvider;
    
    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private RedisService redisService;

    /**
     * @Author lvzhenwei
     * @Description 商品收藏量mq 对应消费方法
     * @Date 10:49 2019/4/12
     * @Param []
     * @return void
     **/
    @StreamListener(MQConstant.GOODS_COLLECT_NUM)
    public void goodsCollectNumMqConsumer(String msg){
        GoodsModifyCollectNumRequest request = JSONObject.parseObject(msg,GoodsModifyCollectNumRequest.class);
        goodsProvider.updateGoodsCollectNum(request);
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(request.getGoodsId()).build());

        //更新redis商品基本数据
        String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoodsId());
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + request.getGoodsId());
        }

    }
}
