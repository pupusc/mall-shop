package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInitProviderGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsProviderStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(BossSink.class)
public class GoodsConsumerService {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @StreamListener(GoodsProviderSink.INPUT)
    public void updateGoodsProviderStatus(String json){
        GoodsProviderStatusRequest request = JSONObject.parseObject(json, GoodsProviderStatusRequest.class);
        goodsProvider.updateProviderStatus(request);
        //更新关联的商家商品
        request.getStoreIds().forEach(id -> esGoodsInfoElasticProvider.initProviderEsGoodsInfo(
                EsGoodsInitProviderGoodsInfoRequest.builder().storeId(id).providerGoodsIds(null).build()));
    }
}
