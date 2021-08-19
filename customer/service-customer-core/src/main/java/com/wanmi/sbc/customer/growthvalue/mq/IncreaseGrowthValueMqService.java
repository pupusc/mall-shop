package com.wanmi.sbc.customer.growthvalue.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.growthvalue.service.GrowthValueIncreaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(IncreaseGrowthValueMqSink.class)
public class IncreaseGrowthValueMqService {

    @Autowired
    private GrowthValueIncreaseService growthValueIncreaseService;

    /**
     * mq接收增加成长值的信息
     *
     * @param msg
     */
    @StreamListener(MQConstant.INCREASE_GROWTH_VALUE)
    public void increaseGrowthValueMq(String msg) {
        CustomerGrowthValueAddRequest addRequest = JSONObject.parseObject(msg, CustomerGrowthValueAddRequest.class);
        growthValueIncreaseService.increaseGrowthValue(addRequest);

    }
}
