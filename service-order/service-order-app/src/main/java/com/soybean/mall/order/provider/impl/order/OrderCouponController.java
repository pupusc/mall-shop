package com.soybean.mall.order.provider.impl.order;

import com.soybean.mall.order.api.provider.order.OrderCouponProvider;
import com.soybean.mall.order.prize.service.OrderCouponService;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
public class OrderCouponController implements OrderCouponProvider {

    @Autowired
    private OrderCouponService orderCouponService;
    @Override
    public BaseResponse sendCouponAfterPaid() {
        orderCouponService.sendCoupon();
        return BaseResponse.SUCCESSFUL();
    }
}
