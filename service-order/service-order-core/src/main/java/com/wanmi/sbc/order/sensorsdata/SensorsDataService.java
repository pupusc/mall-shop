package com.wanmi.sbc.order.sensorsdata;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByAccountRequest;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.order.bean.dto.SensorsMessageDto;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 神策埋点处理
 */
@Service
@Slf4j
public class SensorsDataService {

    private static final String PAY_SUCCESS_EVENT = "shop_pay_0_success";

    @Autowired
    private CustomerQueryProvider customerQueryProvider;
    @Autowired
    private OrderProducerService orderProducerService;

    /**
     * 支付成功埋点
     */
    public void sendPaySuccessEvent(List<Trade> trades) {
        List<SensorsMessageDto> sensorsMessageDtos = new ArrayList<>();
        log.info("支付成功埋点数据:{}", JSON.toJSONString(trades));
        for (Trade trade : trades) {
            NoDeleteCustomerGetByAccountRequest request = new NoDeleteCustomerGetByAccountRequest();
            request.setCustomerAccount(trade.getBuyer().getAccount());
            BaseResponse<NoDeleteCustomerGetByAccountResponse> noDeleteCustomerByAccount = customerQueryProvider.getNoDeleteCustomerByAccount(request);
            String fandengUserNo = noDeleteCustomerByAccount.getContext().getFanDengUserNo();
            if (StringUtils.isNotBlank(fandengUserNo)) {
                for (TradeItem tradeItem : trade.getTradeItems()) {
                    SensorsMessageDto sensorsMessageDto = new SensorsMessageDto();
                    sensorsMessageDto.setEventName(PAY_SUCCESS_EVENT);
                    sensorsMessageDto.setDistinctId(fandengUserNo);
                    sensorsMessageDto.setLoginId(false);
                    sensorsMessageDto.addProperty("click_type", "付款成功");
                    sensorsMessageDto.addProperty("var_id", tradeItem.getSkuId());
                    sensorsMessageDto.addProperty("goods_name", tradeItem.getSkuName());
                    sensorsMessageDto.addProperty("price", trade.getTradePrice().getTotalPrice().toString());
                    sensorsMessageDtos.add(sensorsMessageDto);
                }
            }
            orderProducerService.sendSensorsMessage(sensorsMessageDtos);
        }
    }

}
