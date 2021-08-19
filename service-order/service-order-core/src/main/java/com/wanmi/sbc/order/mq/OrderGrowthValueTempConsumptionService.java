package com.wanmi.sbc.order.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.growthvalue.model.root.OrderGrowthValueTemp;
import com.wanmi.sbc.order.growthvalue.service.OrderGrowthValueTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;


/**
 * @Description: 订单状态变更生产者
 * @Autho weiwenhao
 * @Date：2021-05-13 14:47:18
 */
@Slf4j
@Service
@EnableBinding(value = {OrderGrowthValueTempConsumptionSink.class})
public class OrderGrowthValueTempConsumptionService {

    @Autowired
    private OrderGrowthValueTempService orderGrowthValueTempService;

    @StreamListener(OrderGrowthValueTempConsumptionSink.INPUT)
    public void orderGrowthValueTemp(String json){

        log.info("========新增会员权益处理订单成长值临时表MQ开始消费：{}===============",json);
        OrderGrowthValueTemp orderGrowthValueTemp = JSONObject.parseObject(json, OrderGrowthValueTemp.class);
        orderGrowthValueTempService.add(orderGrowthValueTemp);
        log.info("========新增会员权益处理订单成长值临时表MQ消费结束：{}===============",json);

    }
}
