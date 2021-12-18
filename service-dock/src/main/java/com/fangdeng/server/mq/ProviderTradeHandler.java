package com.fangdeng.server.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fangdeng.server.assembler.OrderAssembler;
import com.fangdeng.server.client.BookuuClient;
import com.fangdeng.server.client.request.bookuu.BookuuOrderAddRequest;
import com.fangdeng.server.client.request.bookuu.BookuuOrderStatusQueryRequest;
import com.fangdeng.server.client.request.bookuu.BookuuPackStatusQueryRequest;
import com.fangdeng.server.client.response.bookuu.BookuuOrderAddResponse;
import com.fangdeng.server.client.response.bookuu.BookuuOrderStatusQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuPackStatusQueryResponse;
import com.fangdeng.server.constant.ConsumerConstants;
import com.fangdeng.server.dto.*;
import com.fangdeng.server.enums.DeliveryStatus;
import com.fangdeng.server.vo.DeliveryItemVO;
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
import javax.swing.text.html.Option;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private RedisTemplate<String, Object> redisTemplate;

    private static String ORDER_PUSH_CONSUME = "supplier:order:push:";

    private static String ORDER_PUSH_CANCEL = "supplier:order:cancel:";

    @RabbitListener(queues = ConsumerConstants.PROVIDER_TRADE_ORDER_PUSH_QUEUE)
    @RabbitHandler
    public void orderPushConsumer(Message message, @Payload String body) {
        try {
            OrderTradeDTO orderTradeDTO = JSONObject.parseObject(body, OrderTradeDTO.class);
            log.info("order push consumer,message:{},payload:{}", message, orderTradeDTO);
            if (!checkOrderPush(orderTradeDTO.getPlatformCode()) || !checkCancel(orderTradeDTO.getPlatformCode())) {
                log.info("there is order push,request:{}", orderTradeDTO);
                return;
            }
            BookuuOrderAddRequest request = OrderAssembler.convert(orderTradeDTO);
            BookuuOrderAddResponse response = bookuuClient.addOrder(request);
            if (response.getStatus() == 1) {
                //查询一次
                BookuuOrderStatusQueryRequest orderStatusQueryRequest = new BookuuOrderStatusQueryRequest();
                orderStatusQueryRequest.setOutID(Arrays.asList(request.getSequence()));
                BookuuOrderStatusQueryResponse orderStatusQueryResponse = bookuuClient.queryOrderStatus(orderStatusQueryRequest);
                if (orderStatusQueryResponse != null && CollectionUtils.isNotEmpty(orderStatusQueryResponse.getStatusDTOS())) {
                    //为了防止消息漏掉，认为成功，在消费端处理
                    response.setStatus(0);
                    response.setOrderID(orderStatusQueryResponse.getStatusDTOS().get(0).getOrderId());
                }
            }
            ProviderTradeOrderConfirmDTO confirmDTO = ProviderTradeOrderConfirmDTO.builder().status(response.getStatus()).statusDesc(response.getStatusDesc()).orderId(response.getOrderID()).platformCode(orderTradeDTO.getPlatformCode()).build();
            log.info("order push consumer confirm,request:{}", confirmDTO);
            //回传消息
            rabbitTemplate.convertAndSend(ConsumerConstants.PROVIDER_TRADE_ORDER_PUSH_CONFIRM, ConsumerConstants.ROUTING_KEY, JSON.toJSONString(confirmDTO));
        } catch (Exception e) {
            log.error("provider order push error,request:{}", body, e);
        }
    }

    private Boolean checkOrderPush(String orderId) {
        try {
            String flag = (String) redisTemplate.opsForValue().get(ORDER_PUSH_CONSUME + orderId);
            if (StringUtils.isEmpty(flag)) {
                redisTemplate.opsForValue().set(ORDER_PUSH_CONSUME + orderId, "true", 5, TimeUnit.MINUTES);
                return true;
            }
        } catch (Exception e) {
            log.error("get redis error,orderId:{}", orderId, e);
        }
        return false;
    }

    /**
     * 物流状态查询
     *
     * @param message
     * @param body
     */
    @RabbitListener(queues = ConsumerConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC_QUEUE)
    @RabbitHandler
    public void deliveryStatusSyncConsumer(Message message, @Payload String body) {
        log.info("delivery status sync consumer,message:{},payload:{}", message, body);
        ProviderTradeOrderStatusConfirmDTO confirmDTO = this.initConfirmInfo(body);
        //成功之后再回传消息
        log.info("order delivery status confirm,request:{}", JSONObject.toJSONString(confirmDTO));
        rabbitTemplate.convertAndSend(ConsumerConstants.PROVIDER_TRADE_DELIVERY_STATUS_SYNC_CONFIRM, ConsumerConstants.ROUTING_KEY, JSON.toJSONString(confirmDTO));
    }

    private ProviderTradeOrderStatusConfirmDTO initConfirmInfo(String body){
        ProviderTradeOrderStatusConfirmDTO confirmDTO = new ProviderTradeOrderStatusConfirmDTO();
        try {
            ProviderTradeDeliveryStatusSyncDTO syncDTO = JSONObject.parseObject(body, ProviderTradeDeliveryStatusSyncDTO.class);
            log.info("delivery status sync consumer,syncDTO:{}", syncDTO);
            confirmDTO.setPlatformCode(syncDTO.getTid());

            //查询订单状态
            BookuuOrderStatusQueryRequest request = new BookuuOrderStatusQueryRequest();
            request.setOutID(Arrays.asList(syncDTO.getTid()));
            BookuuOrderStatusQueryResponse response = bookuuClient.queryOrderStatus(request);
            if (response == null || CollectionUtils.isEmpty(response.getStatusDTOS())) {
                log.warn("query order delivery status error,body:{}", body);
                return confirmDTO;
            }
            //方便测试，要去掉，测试代码
            response.getStatusDTOS().get(0).setUnpacking(1);
            List<DeliveryInfoDTO> deliveryList = new ArrayList<>();
            //如果有拆包
            if (Objects.equals(response.getStatusDTOS().get(0).getUnpacking(), 1)) {
                BookuuPackStatusQueryRequest bookuuPackStatusQueryRequest = new BookuuPackStatusQueryRequest();
                bookuuPackStatusQueryRequest.setOrderId(response.getStatusDTOS().get(0).getOrderId());
                BookuuPackStatusQueryResponse packStatusQueryResponse = bookuuClient.queryPackageStatus(bookuuPackStatusQueryRequest);
                if (packStatusQueryResponse == null || CollectionUtils.isEmpty(packStatusQueryResponse.getStatusDTOS())) {
                    return confirmDTO;
                }
                //List<OrderPackItemDTO> packItemList = packStatusQueryResponse.getStatusDTOS().get(0).getPackRecs();
                //测试代码，

                List<OrderPackItemDTO> packItemList = new ArrayList<>();
                OrderPackItemDTO packItemDTO1 = new OrderPackItemDTO();
                packItemDTO1.setBookId("31239863");
                packItemDTO1.setBookNum(1);
                packItemDTO1.setSendNum(1);
                packItemDTO1.setStatus(5);
                packItemDTO1.setBookUUID("Y10002353833");
                packItemDTO1.setPost("圆通");
                packItemDTO1.setPostNumber("11111");
                packItemList.add(packItemDTO1);

                OrderPackItemDTO packItemDTO2 = new OrderPackItemDTO();
                packItemDTO2.setBookId("31239866");
                packItemDTO2.setBookNum(1);
                packItemDTO2.setSendNum(1);
                packItemDTO2.setStatus(1);
                packItemDTO2.setBookUUID("Y10002353834");
                packItemDTO2.setPost("申通");
                packItemDTO2.setPostNumber("22222");
                packItemList.add(packItemDTO2);

                OrderPackItemDTO packItemDTO3 = new OrderPackItemDTO();
                packItemDTO3.setBookId("3425195");
                packItemDTO3.setBookNum(1);
                packItemDTO3.setSendNum(1);
                packItemDTO3.setStatus(7);
                packItemDTO3.setBookUUID("Y10002353835");
                packItemDTO3.setPost("申通");
                packItemList.add(packItemDTO2);
                if (CollectionUtils.isEmpty(packItemList)) {
                    return confirmDTO;
                }
                //博库有些商品是组合商品，所以要落此值
                packItemList.forEach(p->{
                    Optional<GoodsItemDTO> goodsItemDTOOptional = response.getStatusDTOS().get(0).getBookRecs().stream().filter(b->b.getBookId().equals(p.getBookId())).findFirst();
                    if(goodsItemDTOOptional.isPresent()) {
                        p.setSourceSpbs(goodsItemDTOOptional.get().getSourceSpbs());
                    }
                });
                Map<String, List<OrderPackItemDTO>> map = packItemList.stream().filter(p -> Arrays.asList(4, 5).contains(p.getStatus())).collect(Collectors.groupingBy(t -> t.getPostNumber()));
                //博库是返回全部数据，所以数据要自己处理
                //先处理已发货的
                if (map.isEmpty()) {
                    return confirmDTO;
                }
                map.forEach((key, value) -> {
                    DeliveryInfoDTO deliveryInfoDTO = DeliveryInfoDTO.builder()
                            .deliveryStatus(DeliveryStatus.DELIVERY_COMPLETE)
                            .expressNo(value.get(0).getPostNumber())
                            .expressName(value.get(0).getPost())
                            .platformCode(syncDTO.getTid())
                            .code(value.get(0).getBookUUID())
                            .build();
                    List<GoodsItemDTO> goods = new ArrayList<>(value.size());
                    value.forEach(g->{
                        GoodsItemDTO goodsItemDTO = new GoodsItemDTO();
                        goodsItemDTO.setBookId(g.getBookId());
                        goodsItemDTO.setBookNum(g.getBookNum());
                        goodsItemDTO.setBookSendNum(g.getSendNum());
                        goodsItemDTO.setStatus(g.getStatus());
                        goodsItemDTO.setSourceSpbs(g.getSourceSpbs());
                        goods.add(goodsItemDTO);
                    });
                    deliveryInfoDTO.setGoodsList(goods);
                    deliveryList.add(deliveryInfoDTO);
                });
                confirmDTO.setDeliveryInfoList(deliveryList);
                //有取消的怎么处理？取消+发货=全部发货
                if(packItemList.stream().anyMatch(p->Arrays.asList(0,1,2,3).contains(p.getStatus()))){
                    confirmDTO.setDeliveryStatus(DeliveryStatus.PART_DELIVERY.getKey());
                }else{
                    confirmDTO.setDeliveryStatus(DeliveryStatus.DELIVERY_COMPLETE.getKey());
                    //取消
                    List<OrderPackItemDTO> cancelItems = packItemList.stream().filter(p->Objects.equals(p.getStatus(),7)).collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(cancelItems)){
                        return confirmDTO;
                    }
                    List<GoodsItemDTO> cancelGoods = new ArrayList<>(cancelItems.size());
                    cancelItems.forEach(g->{
                        GoodsItemDTO goodsItemDTO = new GoodsItemDTO();
                        goodsItemDTO.setBookId(g.getBookId());
                        goodsItemDTO.setBookNum(g.getBookNum());
                        goodsItemDTO.setBookSendNum(g.getSendNum());
                        goodsItemDTO.setSourceSpbs(g.getSourceSpbs());
                        goodsItemDTO.setStatus(g.getStatus());
                        cancelGoods.add(goodsItemDTO);
                    });
                    confirmDTO.setCancelGoods(cancelGoods);
                }

            } else if (Objects.equals(response.getStatusDTOS().get(0).getOrderStatus(), 5) || Objects.equals(response.getStatusDTOS().get(0).getOrderStatus(), 4)) {
                DeliveryInfoDTO deliveryInfo = DeliveryInfoDTO.builder()
                        .platformCode(syncDTO.getTid())
                        .expressNo(response.getStatusDTOS().get(0).getPostNumber())
                        .expressName(response.getStatusDTOS().get(0).getPost())
                        .deliveryStatus(DeliveryStatus.DELIVERY_COMPLETE)
                        .deliverTime(LocalDateTime.now())
                        .goodsList(response.getStatusDTOS().get(0).getBookRecs())
                        .build();
                if (response.getStatusDTOS().get(0).getPostDate() != null) {
                    try {
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        deliveryInfo.setDeliverTime(LocalDateTime.parse(response.getStatusDTOS().get(0).getPostDate(), df));
                    } catch (Exception e) {
                        log.warn("syncProviderTradeDeliveryStatus trans time error,request:{}", request, e);
                    }
                }
                deliveryList.add(deliveryInfo);
                confirmDTO.setDeliveryInfoList(deliveryList);
                confirmDTO.setDeliveryStatus(DeliveryStatus.DELIVERY_COMPLETE);
                return confirmDTO;
            }
        }catch (Exception e) {
            log.error("provider trade delivery status error,request:{}", body, e);
        }
        return confirmDTO;
    }

    private Boolean checkCancel(String orderId) {
        try {
            String flag = (String) redisTemplate.opsForValue().get(ORDER_PUSH_CANCEL + orderId);
            if (StringUtils.isNotEmpty(flag)) {
                return false;
            }
        } catch (Exception e) {
            log.error("cancel order get redis error,orderId:{}", orderId, e);
        }
        return true;
    }


}
