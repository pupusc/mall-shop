package com.fangdeng.server.service;

import com.fangdeng.server.client.BookuuClient;
import com.fangdeng.server.client.request.bookuu.BookuuOrderCancelRequest;
import com.fangdeng.server.client.request.bookuu.BookuuOrderStatusQueryRequest;
import com.fangdeng.server.client.response.bookuu.BookuuOrderCancelResponse;
import com.fangdeng.server.client.response.bookuu.BookuuOrderStatusQueryResponse;
import com.fangdeng.server.common.BaseResponse;
import com.fangdeng.server.dto.CancelOrderDTO;
import com.fangdeng.server.enums.BookuuDeliveryStatus;
import com.fangdeng.server.enums.DeliveryStatus;
import com.fangdeng.server.vo.CancelOrderVO;
import com.fangdeng.server.vo.DeliveryStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private BookuuClient bookuuClient;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private static String ORDER_PUSH_CANCEL = "supplier:order:cancel:";

    public BaseResponse<DeliveryStatusVO> getDeliveryStatus(String pid) {
        BookuuOrderStatusQueryRequest request = new BookuuOrderStatusQueryRequest();
        request.setOutID(Arrays.asList(pid));
        BookuuOrderStatusQueryResponse response = bookuuClient.queryOrderStatus(request);
        if (response == null) {
            log.warn("query order delivery status error,pid:{},response:{}", pid,response);
            return BaseResponse.FAILED("response is null");
        }
        DeliveryStatusVO deliveryStatusVO = new DeliveryStatusVO();
        if(CollectionUtils.isEmpty(response.getStatusDTOS())){
            return BaseResponse.success(deliveryStatusVO);
        }
        DeliveryStatusVO.DeliveryInfoVO deliveryInfoVO = new DeliveryStatusVO.DeliveryInfoVO();
        deliveryInfoVO.setPlatformCode(response.getStatusDTOS().get(0).getOrderId());
        deliveryInfoVO.setPlatformCode(pid);
        deliveryInfoVO.setExpressName(response.getStatusDTOS().get(0).getPost());
        deliveryInfoVO.setExpressNo(response.getStatusDTOS().get(0).getPostNumber());
        
        //deliveryInfoVO.setDeliverTime(response.getStatusDTOS().get(0).getPostDate());
        deliveryInfoVO.setDeliveryStatus(DeliveryStatus.getDeliveryStatus(BookuuDeliveryStatus.getDeliveryStatusByKey(response.getStatusDTOS().get(0).getOrderStatus())));
        deliveryStatusVO.setDeliveryInfoVOList(Arrays.asList(deliveryInfoVO));
        return BaseResponse.success(deliveryStatusVO);
    }


    public BaseResponse<CancelOrderVO> cancelOrder(CancelOrderDTO cancelOrderDTO) {
        BookuuOrderCancelRequest request = new BookuuOrderCancelRequest();
        request.setOrderId(cancelOrderDTO.getOrderId());
        request.setType(cancelOrderDTO.getType());
        request.setBookId(cancelOrderDTO.getErpGoodsInfoNo());
        CancelOrderVO cancelOrderVO =new CancelOrderVO();
        cancelOrderVO.setOrderID(cancelOrderDTO.getPid());
        if(StringUtils.isEmpty(request.getOrderId())){
            //没有博库订单号查询一下
            BookuuOrderStatusQueryRequest orderStatusQueryRequest = new BookuuOrderStatusQueryRequest();
            orderStatusQueryRequest.setOutID(Arrays.asList(cancelOrderDTO.getPid()));
            BookuuOrderStatusQueryResponse orderStatusQueryResponse = bookuuClient.queryOrderStatus(orderStatusQueryRequest);
            if(orderStatusQueryResponse == null || CollectionUtils.isEmpty(orderStatusQueryResponse.getStatusDTOS())){
                //没有推送到博库认为成功取消，并放到redis防止重复消费
                cancelOrderVO.setStatus(1);
                cancelOrderVO.setErrorMsg("订单取消时并未推送到博库");
                setCancelRedis(cancelOrderDTO.getPid());
                return BaseResponse.success(cancelOrderVO);
            }
            request.setOrderId(orderStatusQueryResponse.getStatusDTOS().get(0).getOrderId());
        }

        BookuuOrderCancelResponse response = bookuuClient.cancelOrder(request);
        if (response == null) {
            log.warn("cancel order error,pid:{},response:{}", cancelOrderDTO.getPid(),response);
            return BaseResponse.FAILED("response is null");
        }
        cancelOrderVO.setStatus(response.getStatus());
        cancelOrderVO.setErrorMsg(response.getErrorMsg());
        return BaseResponse.success(cancelOrderVO);

    }

    private void setCancelRedis(String orderId){
        try {
            String flag =  (String) redisTemplate.opsForValue().get(ORDER_PUSH_CANCEL+orderId);
            if (StringUtils.isEmpty(flag)) {
                redisTemplate.opsForValue().set(ORDER_PUSH_CANCEL+orderId, "true", 3, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("cancel order get redis error,orderId:{}",orderId,e);
        }
    }
}
