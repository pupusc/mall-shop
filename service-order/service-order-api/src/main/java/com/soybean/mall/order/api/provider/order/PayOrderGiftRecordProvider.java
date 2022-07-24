package com.soybean.mall.order.api.provider.order;


import com.soybean.mall.order.api.request.record.OrderGiftRecordMqReq;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@FeignClient(value = "${application.order.name}", contextId = "PayOrderGiftRecordProvider")
public interface PayOrderGiftRecordProvider {


    /**
     * 记录发送mq
     * @param orderGiftRecordMqReq
     * @return
     */
    @PostMapping("/order/${application.order.version}/afterRecordMessageOrder")
    BaseResponse afterRecordMessageOrder(@RequestBody @Validated OrderGiftRecordMqReq orderGiftRecordMqReq);


//    /**
//     * 支付发送mq
//     * @param orderGiftRecordMqReq
//     * @return
//     */
//    @PostMapping("/order/${application.order.version}/afterPayOrderLock")
//    BaseResponse afterPayOrderLock(@RequestBody @Validated OrderGiftRecordMqReq orderGiftRecordMqReq);

}
