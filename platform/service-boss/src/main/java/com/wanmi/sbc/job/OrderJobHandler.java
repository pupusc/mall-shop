package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.order.api.provider.trade.TradeSettingProvider;
import com.wanmi.sbc.order.api.request.trade.ReturnOrderModifyAutoAuditRequest;
import com.wanmi.sbc.order.api.request.trade.ReturnOrderModifyAutoReceiveRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 定时任务Handler（Bean模式）
 * 订单定时任务
 */
@JobHandler(value="orderJobHandler")
@Component
@Slf4j
public class OrderJobHandler extends IJobHandler {
    @Autowired
    private TradeSettingProvider tradeSettingProvider;


    @Autowired
//    private ConfigRepository configRepository;
    private AuditQueryProvider auditQueryProvider;

    /**
     * 凌晨一点执行 考虑使用分布式定时任务
     */
    @Override
    @Transactional
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("订单定时任务执行 " + LocalDateTime.now());
        log.info("订单定时任务执行 " + LocalDateTime.now());
//        //订单自动确认收货
//        Integer autoReceive = configRepository
//                .findByConfigTypeAndDelFlag(ConfigType.ORDER_SETTING_AUTO_RECEIVE.toString(), DeleteFlag.NO).getStatus();
//        //退单自动收货
//        Config returnOrderAutoReceiveConfig = configRepository
//                .findByConfigTypeAndDelFlag(ConfigType.ORDER_SETTING_REFUND_AUTO_RECEIVE.toString(), DeleteFlag.NO);
//        //退单自动审核
//        Config autoAuditConfig = configRepository
//                .findByConfigTypeAndDelFlag(ConfigType.ORDER_SETTING_REFUND_AUTO_AUDIT.toString(), DeleteFlag.NO);
        //订单自动确认收货
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_AUTO_RECEIVE);
        Integer autoReceive = auditQueryProvider
                .getTradeConfigByType(request).getContext().getStatus();
        //退单自动收货
        request.setConfigType(ConfigType.ORDER_SETTING_REFUND_AUTO_RECEIVE);
        ConfigVO returnOrderAutoReceiveConfig = auditQueryProvider
                .getTradeConfigByType(request).getContext();
        //退单自动审核
        request.setConfigType(ConfigType.ORDER_SETTING_REFUND_AUTO_AUDIT);
        ConfigVO autoAuditConfig = auditQueryProvider
                .getTradeConfigByType(request).getContext();
        //开关只有开的时候才执行
        if (Integer.valueOf(1).equals(autoReceive)) {
            tradeSettingProvider.orderAutoReceive();
        }

        //退单自动确认收货
        if (Integer.valueOf(1).equals(returnOrderAutoReceiveConfig.getStatus())) {
            Integer day =  Integer.valueOf(JSON.parseObject(returnOrderAutoReceiveConfig.getContext()).get("day").toString());
            tradeSettingProvider.modifyReturnOrderAutoReceive(new ReturnOrderModifyAutoReceiveRequest(day));
        }

        //退单自动审核
        if (Integer.valueOf(1).equals(autoAuditConfig.getStatus())) {
            Integer day =  Integer.valueOf(JSON.parseObject(autoAuditConfig.getContext()).get("day").toString());
            tradeSettingProvider.modifyReturnOrderAutoAudit(new ReturnOrderModifyAutoAuditRequest(day));
        }
        return SUCCESS;
    }
}
