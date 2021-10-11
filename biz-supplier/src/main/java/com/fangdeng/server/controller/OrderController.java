package com.fangdeng.server.controller;

import com.fangdeng.server.common.BaseResponse;
import com.fangdeng.server.dto.CancelOrderDTO;
import com.fangdeng.server.service.OrderService;
import com.fangdeng.server.vo.CancelOrderVO;
import com.fangdeng.server.vo.DeliveryStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
@Slf4j
public class OrderController {


    @Autowired
    private OrderService orderService;


    @GetMapping(value = "/delivery/status")
    public BaseResponse<DeliveryStatusVO> getDeliveryStatus(@RequestParam("pid") String pid) {
        return orderService.getDeliveryStatus(pid);
    }

    @PostMapping("/cancel")
    public BaseResponse<CancelOrderVO> cancelOrder(@RequestBody CancelOrderDTO cancelOrderDTO) {
        log.info("cancel order request:{},cancelOrderDTO");
        return orderService.cancelOrder(cancelOrderDTO);
    }
}
