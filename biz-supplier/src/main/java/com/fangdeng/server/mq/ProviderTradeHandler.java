package com.fangdeng.server.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangdeng.server.assembler.OrderAssembler;
import com.fangdeng.server.client.BookuuClient;
import com.fangdeng.server.client.request.bookuu.BookuuOrderAddRequest;
import com.fangdeng.server.client.request.bookuu.BookuuOrderStatusQueryRequest;
import com.fangdeng.server.client.response.bookuu.BookuuOrderAddResponse;
import com.fangdeng.server.client.response.bookuu.BookuuOrderStatusQueryResponse;
import com.fangdeng.server.constant.ConsumerConstants;
import com.fangdeng.server.dto.OrderTradeDTO;
import com.fangdeng.server.dto.ProviderTradeDeliveryStatusSyncDTO;
import com.fangdeng.server.dto.ProviderTradeOrderConfirmDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 发货单消费
 */
@Service
@Slf4j
public class ProviderTradeHandler {
    @Autowired
    private BookuuClient bookuuClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private static String ORDER_PUSH_CONSUME = "supplier.order.push";

    @RabbitListener(queues = ConsumerConstants.PROVIDER_TRADE_ORDER_PUSH_QUEUE)
    @RabbitHandler
    public void orderPushConsumer(Message message, @Payload String body) {
        OrderTradeDTO orderTradeDTO = JSONObject.parseObject(body,OrderTradeDTO.class);
        log.info("order push consumer,message:{},payload:{}",message,orderTradeDTO);
        if(!checkOrderPush(orderTradeDTO.getPlatformCode())){
            log.info("there is order push,request:{}",orderTradeDTO);
            return;
        }
        BookuuOrderAddRequest request = OrderAssembler.convert(orderTradeDTO);
        BookuuOrderAddResponse response = bookuuClient.addOrder(request);
        if(response.getStatus() == 1 && response.getStatusDesc().contains("已被导入")){
            return;
        }
        ProviderTradeOrderConfirmDTO confirmDTO = ProviderTradeOrderConfirmDTO.builder().status(response.getStatus()).statusDesc(response.getStatusDesc()).orderId(response.getOrderID()).platformCode(orderTradeDTO.getPlatformCode()).build();
        log.info("order push consumer confirm,request:{}",confirmDTO);
        //回传消息
        rabbitTemplate.convertAndSend(ConsumerConstants.PROVIDER_TRADE_ORDER_PUSH_CONFIRM,ConsumerConstants.ROUTING_KEY, JSON.toJSONString(confirmDTO));
        //手动确认

    }

    private Boolean checkOrderPush(String orderId){
        try {
            Map map =  (HashMap) redisTemplate.opsForValue().get(ORDER_PUSH_CONSUME);
            if (map == null || map.isEmpty() || !map.containsKey(orderId) || map.get(orderId) == null) {
                if(map == null){
                    map = new HashMap();
                }
                map.put(orderId,true);
                redisTemplate.opsForValue().set(ORDER_PUSH_CONSUME, map, 5, TimeUnit.MINUTES);
                return true;
            }
        } catch (Exception e) {
            log.error("get redis error,orderId:{}",orderId,e);
        }
        return false;
    }

    /**
     *  物流状态查询
     * @param message
     * @param body
     */
    @RabbitListener(queues = ConsumerConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC_QUEUE)
    @RabbitHandler
    public void deliveryStatusSyncConsumer(Message message, @Payload String body) {
        ProviderTradeDeliveryStatusSyncDTO syncDTO = JSONObject.parseObject(body,ProviderTradeDeliveryStatusSyncDTO.class);
        log.info("delivery status sync consumer,message:{},payload:{}",message,syncDTO);
        BookuuOrderStatusQueryRequest request = new BookuuOrderStatusQueryRequest();
        request.setOutID(Arrays.asList(syncDTO.getTid()));
         BookuuOrderStatusQueryResponse response = bookuuClient.queryOrderStatus(request);
        if(response ==null || CollectionUtils.isEmpty(response.getStatusDTOS())){
           log.warn("query order delivery status error,body:{}",body);
           ProviderTradeOrderConfirmDTO confirmDTO = ProviderTradeOrderConfirmDTO.builder()
                    .platformCode(syncDTO.getTid())
                    .orderStatus(-1)
                    .build();
            rabbitTemplate.convertAndSend(ConsumerConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC_CONFIRM,ConsumerConstants.ROUTING_KEY, JSON.toJSONString(confirmDTO));
            return;
        }
        ProviderTradeOrderConfirmDTO confirmDTO = ProviderTradeOrderConfirmDTO.builder()
                .orderId(response.getStatusDTOS().get(0).getOrderId())
                .platformCode(syncDTO.getTid())
                .post(response.getStatusDTOS().get(0).getPost())
                .postNumber(response.getStatusDTOS().get(0).getPostNumber())
                .orderStatus(response.getStatusDTOS().get(0).getOrderStatus())
                .goodsList(response.getStatusDTOS().get(0).getBookRecs())
                .build();
        if(StringUtils.isNotEmpty(response.getStatusDTOS().get(0).getPostDate())) {
            try {
                confirmDTO.setPostDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(response.getStatusDTOS().get(0).getPostDate()));
            }catch (Exception e){
                confirmDTO.setPostDate(new Date());
            }
        }
        //成功之后再回传消息
        log.info("order delivery status confirm,request:{}",JSONObject.toJSONString(confirmDTO));
        rabbitTemplate.convertAndSend(ConsumerConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC_CONFIRM,ConsumerConstants.ROUTING_KEY, JSON.toJSONString(confirmDTO));
        //手动确认

    }




}
