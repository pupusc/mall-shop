package com.wanmi.sbc.mq.appointment;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName RushToAppointmentSaleGoodsConsumerService
 * @Description 商品预约，抢购数量同步
 * @Author zxd
 * @Date 2020/05/25 10:26
 **/
@Component
@EnableBinding(RushToAppointmentSaleGoodsSink.class)
@Slf4j
public class RushToAppointmentSaleGoodsConsumerService {

    @Autowired
    private AppointmentSaleService appointmentSaleService;


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AppointmentSaleGoodsProvider appointmentSaleGoodsProvider;


    @StreamListener(RushToAppointmentSaleGoodsSink.INPUT)
    public void rushToAppointmentSaleGoodsConsumer(String message) {
        RushToAppointmentSaleGoodsRequest request = JSONObject.parseObject(message, RushToAppointmentSaleGoodsRequest.class);
        log.info("-----消费立即预约MQ-----，参数：" + request.toString());
        //判断预约商品条件
        try {
            appointmentSaleService.judgeAppointmentGoodsCondition(request, false);
            //（加锁，保证高并发下预约数量是正确的）
            RLock rLock = redissonClient.getFairLock(RedisKeyConstant.APPOINTMENT_SALE_GOODS_INFO_COUNT + request.getAppointmentSaleId() + ":" + request.getSkuId());
            try {
                rLock.lock();
                appointmentSaleGoodsProvider.updateAppointmentCount(request);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rLock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
