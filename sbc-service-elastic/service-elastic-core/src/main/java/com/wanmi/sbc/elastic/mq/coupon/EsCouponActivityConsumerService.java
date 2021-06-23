package com.wanmi.sbc.elastic.mq.coupon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityAddListByActivityIdRequest;
import com.wanmi.sbc.elastic.coupon.service.EsCouponActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableBinding(EsCouponActivitySink.class)
public class EsCouponActivityConsumerService {

    @Autowired
    private EsCouponActivityService esCouponActivityService;


    /**
     * 新增积分兑换券，同步ES
     * @param json
     */
    @StreamListener(MQConstant.Q_ES_SERVICE_COUPON_ADD_POINTS_COUPON)
    public void register(String json) {
        try {
            EsCouponActivityAddListByActivityIdRequest request = JSONObject.parseObject(JSON.parse(json).toString(), EsCouponActivityAddListByActivityIdRequest.class);
            esCouponActivityService.saveAllById(request);
            log.info("========新增积分兑换券，同步ES信息成功=============");
        } catch (Exception e) {
            log.error("======新增积分兑换券，同步ES数据发生异常! param={}", json, e);
        }
    }

}
