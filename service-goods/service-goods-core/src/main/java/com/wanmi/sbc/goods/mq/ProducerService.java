package com.wanmi.sbc.goods.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MQ生产者
 * @author: dyt
 * @createDate: 2019/2/25 13:57
 * @version: 1.0
 */
@Service
@Slf4j
@EnableBinding
public class ProducerService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 根据商品库id更新商品库
     * @param standardIds 商品库id
     */
    public void initStandardByStandardIds(List<String> standardIds){
        Map<String,List<String>> map = new HashMap<>();
        map.put("goodsIds", standardIds);
        resolver.resolveDestination(MQConstant.Q_ES_STANDARD_INIT).send(new GenericMessage<>(JSONObject.toJSONString(map)));
    }


    /**
     * 根据商品SkuId更新商品库
     * @param skuIds 商品SkuId
     */
    public void initGoodsBySkuIds(List<String> skuIds){
        Map<String,List<String>> map = new HashMap<>();
        map.put("skuIds", skuIds);
        resolver.resolveDestination(MQConstant.Q_ES_GOODS_INIT).send(new GenericMessage<>(JSONObject.toJSONString(map)));
    }
}
