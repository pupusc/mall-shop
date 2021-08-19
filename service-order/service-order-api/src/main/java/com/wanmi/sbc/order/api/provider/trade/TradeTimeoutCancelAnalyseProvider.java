package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: wanggang
 * @createDate: 2018/12/17 16:05
 * @version: 1.0
 */
@FeignClient(value = "${application.order.name}", contextId = "TradeTimeoutCancelAnalyseProvider")
public interface TradeTimeoutCancelAnalyseProvider {

    /**
     * 定时器-取消订单
     * @return
    */
    @PostMapping("/order/${application.order.version}/task/cancel-order")
    BaseResponse cancelOrder();
}
