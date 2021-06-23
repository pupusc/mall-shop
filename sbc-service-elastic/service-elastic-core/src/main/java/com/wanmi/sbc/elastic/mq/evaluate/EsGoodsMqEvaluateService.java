package com.wanmi.sbc.elastic.mq.evaluate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import com.wanmi.sbc.elastic.customer.model.root.StoreEvaluateSum;
import com.wanmi.sbc.elastic.customer.repository.EsStoreEvaluateSumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@EnableBinding(EsGoodsEvaluateSink.class)
public class EsGoodsMqEvaluateService {

    @Autowired
    private EsStoreEvaluateSumRepository storeEvaluateSumRepository;

    /**
     * ES商品库更新
     *
     * @param json
     */
    @StreamListener(MQConstant.STORE_EVALUATE_ADD)
    public void init(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StoreEvaluateVO storeEvaluateVO = objectMapper.readValue(json, StoreEvaluateVO.class);
            StoreEvaluateSum storeEvaluateSum = new StoreEvaluateSum();
            BeanUtils.copyProperties(storeEvaluateVO, storeEvaluateSum);
            storeEvaluateSumRepository.save(storeEvaluateSum);
            log.info("商家评价添加成功!");
        } catch (Exception e) {
            log.error("商家评价添加异常! param={}", json, e);
        }
    }
}
