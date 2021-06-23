package com.wanmi.sbc.goods.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableBinding
public class GoodsEvaluateMqService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    public void sendStoreEvaluate(StoreEvaluateVO storeEvaluateVO) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objJson = objectMapper.writeValueAsString(storeEvaluateVO);
            resolver.resolveDestination(MQConstant.STORE_EVALUATE_ADD).send(new GenericMessage<>(objJson));
        } catch (JsonProcessingException e) {
            log.info("发送商家评价异常：{}", e.getMessage());
        }


    }
}