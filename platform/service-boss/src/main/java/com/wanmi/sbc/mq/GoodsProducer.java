package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.goods.api.request.goods.GoodsProviderStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableBinding(GoodsProviderSink.class)
public class GoodsProducer {

    @Autowired
    private GoodsProviderSink goodsProviderSink;

    /**
     * 更新代销商品的供应商店铺状态
     * @param storeIds
     */
    public void updateProviderStatus(Integer providerStatus, List<Long> storeIds){
        goodsProviderSink.output().send(new GenericMessage<>(JSONObject.toJSONString(new GoodsProviderStatusRequest(providerStatus, storeIds))));
    }
}
