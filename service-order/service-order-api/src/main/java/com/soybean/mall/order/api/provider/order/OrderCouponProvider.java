package com.soybean.mall.order.api.provider.order;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;



@FeignClient(value = "${application.order.name}", contextId = "OrderCouponProvider")
public interface OrderCouponProvider {

    /**
     * 同步订单状态到微信
     *
     * @param
     * @return
     */
    @PostMapping("/order/${application.order.version}/prize/batch-send-coupon")
    BaseResponse sendCouponAfterPaid();
}
