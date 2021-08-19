package com.wanmi.sbc.job;


import com.wanmi.sbc.order.api.provider.trade.TradeTimeoutCancelAnalyseProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务Handler（Bean模式）
 * 超时未支付取消订单
 *
 * @author bail 2019-3-24
 */
@JobHandler(value="orderTimeoutCancelJobHandler")
@Component
@Slf4j
public class OrderTimeoutCancelJobHandler extends IJobHandler {

    @Autowired
    private TradeTimeoutCancelAnalyseProvider tradeTimeoutCancelAnalyseProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        //已启用延迟队列处理先款后货订单-超时未支付订单
//        tradeTimeoutCancelAnalyseProvider.cancelOrder();
        return SUCCESS;
    }

}
