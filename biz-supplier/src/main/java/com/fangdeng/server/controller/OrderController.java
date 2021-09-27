package com.fangdeng.server.controller;

import com.fangdeng.server.common.BaseResponse;
import com.fangdeng.server.dto.CancelOrderDTO;
import com.fangdeng.server.service.OrderService;
import com.fangdeng.server.vo.CancelOrderVO;
import com.fangdeng.server.vo.DeliveryStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order")
public class OrderController {


    @Autowired
    private OrderService orderService;


    @GetMapping(value = "/delivery/status")
    public BaseResponse<DeliveryStatusVO> getDeliveryStatus(@RequestParam("pid") String pid) {
        return orderService.getDeliveryStatus(pid);
    }

    @PostMapping("/cancel")
    public BaseResponse<CancelOrderVO> cancelOrder(@RequestBody CancelOrderDTO cancelOrderDTO) {
        return orderService.cancelOrder(cancelOrderDTO);
    }
}
