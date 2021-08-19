package com.wanmi.sbc.trade;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.CycleBuyPostponementRequest;
import com.wanmi.sbc.order.api.request.trade.ShippingCalendarRequest;
import com.wanmi.sbc.order.api.response.trade.ShippingCalendarResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>周期购订单相关服务API</p>
 * Created by weiwenhao on 2021-01-27-下午2:51.
 */
@Api(tags = "CycleBuyTradeController", description = "周期购订单相关服务API")
@RequestMapping("/cyclebuy/trade")
@RestController
@Validated
public class CycleBuyTradeController {


    @Autowired
    private TradeQueryProvider tradeQueryProvider;


    @Autowired
    private TradeProvider tradeProvider;

    /**
     * 周期购订单发货日历
     */
    @PostMapping("/get-shipping-calendar")
    @ApiOperation(value = "周期购订单发货日历")
    BaseResponse<ShippingCalendarResponse> getShippingCalendar(@RequestBody @Valid ShippingCalendarRequest shippingCalendarRequest) {
        return tradeQueryProvider.getShippingCalendar(shippingCalendarRequest);
    }


    /**
     * 周期购订单  顺延/取消顺延
     *
     * @param cycleBuyPostponementRequest
     * @return
     */
    @ApiOperation(value = "周期购订单  顺延/取消顺延")
    @PostMapping("/cyclebuy-postponement")
    BaseResponse cycleBuyPostponement(@RequestBody @Valid CycleBuyPostponementRequest cycleBuyPostponementRequest) {
        return tradeProvider.cycleBuyPostponement(cycleBuyPostponementRequest);
    }


    /**
     * 周期购订单  定时器推送失败---手动推送
     *
     * @param cycleBuyPostponementRequest
     * @return
     */
    @ApiOperation(value = " 周期购订单  定时器推送失败---手动推送")
    @PostMapping("/cyclebuy-supplementary-push")
    BaseResponse cycleBuySupplementaryPush(@RequestBody @Valid CycleBuyPostponementRequest cycleBuyPostponementRequest) {
        return tradeProvider.cycleBuySupplementaryPush(cycleBuyPostponementRequest);
    }

}
