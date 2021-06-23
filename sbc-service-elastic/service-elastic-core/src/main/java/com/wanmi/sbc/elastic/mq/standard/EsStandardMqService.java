package com.wanmi.sbc.elastic.mq.standard;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.standard.service.EsStandardService;
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
@EnableBinding(EsStandardSink.class)
public class EsStandardMqService {

    @Autowired
    private EsStandardService esStandardService;


    /**
     * ES商品库更新
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_STANDARD_INIT)
    public void init(String json) {
        try {
            EsStandardInitRequest request = JSONObject.parseObject(json, EsStandardInitRequest.class);
            esStandardService.init(request);
            log.info("ES商品库更新成功");
        } catch (Exception e) {
            log.error("ES商品库更新发生异常! param={}", json, e);
        }
    }
}
