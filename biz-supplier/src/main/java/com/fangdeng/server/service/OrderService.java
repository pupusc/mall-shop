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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private BookuuClient bookuuClient;

    public BaseResponse<DeliveryStatusVO> getDeliveryStatus(String pid) {
        BookuuOrderStatusQueryRequest request = new BookuuOrderStatusQueryRequest();
        request.setOutID(Arrays.asList(pid));
        BookuuOrderStatusQueryResponse response = bookuuClient.queryOrderStatus(request);
        if (response == null || CollectionUtils.isEmpty(response.getStatusDTOS())) {
            log.warn("query order delivery status error,pid:{},response:{}", pid,response);
            return BaseResponse.FAILED("response is null");
        }
        DeliveryStatusVO deliveryStatusVO = new DeliveryStatusVO();
        DeliveryStatusVO.DeliveryInfoVO deliveryInfoVO = new DeliveryStatusVO.DeliveryInfoVO();
        deliveryInfoVO.setPlatformCode(response.getStatusDTOS().get(0).getOrderId());
        deliveryInfoVO.setPlatformCode(pid);
        deliveryInfoVO.setExpressName(response.getStatusDTOS().get(0).getPost());
        deliveryInfoVO.setExpressNo(response.getStatusDTOS().get(0).getPostNumber());
        deliveryInfoVO.setDeliveryStatus(DeliveryStatus.getDeliveryStatus(BookuuDeliveryStatus.getDeliveryStatusByKey(response.getStatusDTOS().get(0).getOrderStatus())));
        deliveryStatusVO.setDeliveryInfoVOList(Arrays.asList(deliveryInfoVO));
        return BaseResponse.success(deliveryStatusVO);
    }


    public BaseResponse<CancelOrderVO> cancelOrder(CancelOrderDTO cancelOrderDTO) {
        BookuuOrderCancelRequest request = new BookuuOrderCancelRequest();
        request.setOrderId(cancelOrderDTO.getOrderId());
        BookuuOrderCancelResponse response = bookuuClient.cancelOrder(request);
        if (response == null) {
            log.warn("cancel order error,pid:{},response:{}", cancelOrderDTO.getPid(),response);
            return BaseResponse.FAILED("response is null");
        }
        CancelOrderVO cancelOrderVO =new CancelOrderVO();
        cancelOrderVO.setOrderID(cancelOrderDTO.getPid());
        cancelOrderVO.setStatus(response.getStatus());
        cancelOrderVO.setErrorMsg(response.getErrorMsg());
        return BaseResponse.success(cancelOrderVO);

    }
}
