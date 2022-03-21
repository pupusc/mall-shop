package com.wanmi.sbc.order.api.provider.open;

import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.order.api.request.open.OrderDeliverInfoReqBO;
import com.wanmi.sbc.order.api.request.trade.TradeWrapperBackendCommitRequest;
import com.wanmi.sbc.order.api.response.open.OrderDeliverInfoResBO;
import com.wanmi.sbc.order.bean.vo.TradeCommitResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Liang Jun
 * @desc 履约中台
 * @date 2022-02-21 18:30:00
 */
@FeignClient(value = "${application.order.name}", contextId = "OpenSubstanceProvider")
public interface OpenDeliverProvider {
    @PostMapping("/order/${application.order.version}/open/deliver/deliverInfo")
    BusinessResponse<OrderDeliverInfoResBO> deliverInfo(@RequestBody OrderDeliverInfoReqBO param);

    @PostMapping("/order/${application.order.version}/open/deliver/deliverSync")
    BusinessResponse<Object> deliverSync(@RequestBody Map<String, Object> param);

    @PostMapping("/order/${application.order.version}/open/deliver/orderCreate")
    BusinessResponse<TradeCommitResultVO> createOrder(@RequestBody TradeWrapperBackendCommitRequest param);
}