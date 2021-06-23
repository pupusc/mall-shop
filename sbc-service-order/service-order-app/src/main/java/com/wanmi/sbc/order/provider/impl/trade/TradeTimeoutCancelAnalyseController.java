package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeTimeoutCancelAnalyseProvider;
import com.wanmi.sbc.order.trade.service.TradeTimeoutCancelAnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wanggang
 * @createDate: 2018/12/17 16:21
 * @version: 1.0
 */
@Validated
@RestController
public class TradeTimeoutCancelAnalyseController implements TradeTimeoutCancelAnalyseProvider {

    @Autowired
    private TradeTimeoutCancelAnalyseService tradeTimeoutCancelAnalyseService;

    @Override
    public BaseResponse cancelOrder() {
        tradeTimeoutCancelAnalyseService.orderCancelTask();
        return BaseResponse.SUCCESSFUL();
    }
}
