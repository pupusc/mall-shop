package com.wanmi.sbc.elastic.mq.spu;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.goods.service.EsGoodsElasticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @Author dyt
 * @Description //商品库MQ消费者
 * @Date 10:23 2020/12/16
 * @Param
 * @return
 **/
@Slf4j
@Component
@EnableBinding(EsGoodsSink.class)
public class EsGoodsMqService {

    @Autowired
    private EsGoodsElasticService esGoodsElasticService;


    /**
     * ES商品库更新
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_GOODS_INIT)
    public void init(String json) {
        try {
            EsGoodsInfoRequest request = JSONObject.parseObject(json, EsGoodsInfoRequest.class);
            esGoodsElasticService.initEsGoods(request);
            log.info("ES商品更新成功");
        } catch (Exception e) {
            log.error("ES商品更新发生异常! param={}", json, e);
        }
    }
}
