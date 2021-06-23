package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.job.service.DistributionTaskTempService;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.distribution.ReturnOrderSendMQRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeModifyReturnOrderNumByRidRequest;
import com.wanmi.sbc.order.api.request.trade.TradeReturnOrderNumUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @Description: 退单处理消费者
 * @Autho qiaokang
 * @Date：2019-03-08 16:14:12
 */
@Service
@Slf4j
@EnableBinding(BossSink.class)
public class ReturnOrderConsumerService {

    /**
     * 注入分销临时任务service
     */
    @Autowired
    private DistributionTaskTempService distributionTaskTempService;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    /**
     * 退单状态变更MQ处理：分销任务临时表退单数量加减
     *
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_RETURN_ORDER_FLOW)
    public void doReturnOrderInit(String json) {
        try {
            log.info("=============== 退单状态变更MQ处理start ===============");
            ReturnOrderSendMQRequest request = JSONObject.parseObject(json, ReturnOrderSendMQRequest.class);

            if (request.isAddFlag()) {
                // 分销任务临时表退单数量加1
                distributionTaskTempService.addReturnOrderNum(request.getOrderId());
            } else {
                // 分销任务临时表退单数量减1
                distributionTaskTempService.minusReturnOrderNum(request.getOrderId());
            }

            // 更新订单正在进行的退单数量
            tradeProvider.updateReturnOrderNum(
                    TradeReturnOrderNumUpdateRequest.builder().tid(request.getOrderId()).addFlag(request.isAddFlag()).build());
            // 更新供应商正在进行的退单数量
            if(StringUtils.isNotBlank(request.getReturnId())) {
                providerTradeProvider.updateReturnOrderNumByRid(
                        ProviderTradeModifyReturnOrderNumByRidRequest.builder().returnOrderId(request.getReturnId())
                                .addFlag(request.isAddFlag()).build());
            }
            log.info("=============== 退单状态变更MQ处理end ===============");
        } catch (Exception e) {
            log.error("退单状态变更MQ处理异常! param={}", json, e);
        }
    }

}
