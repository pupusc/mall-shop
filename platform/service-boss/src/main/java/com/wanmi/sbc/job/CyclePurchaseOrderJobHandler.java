//package com.wanmi.sbc.job;
//
//import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
//import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
///**
// * 周期购订单推送ERP
// */
//@JobHandler(value = "cyclePurchaseOrderJobHandler")
//@Component
//@Slf4j
//public class CyclePurchaseOrderJobHandler extends IJobHandler {
//
//
//    @Autowired
//    private ProviderTradeQueryProvider providerTradeQueryProvider;
//
//
//    @Override
//    public ReturnT<String> execute(String param) throws Exception {
//        log.info("=====周期购订单推送开始======");
//        //处理周期购推送erp业务
//        providerTradeQueryProvider.batchPushCycleOrder(ProviderTradeErpRequest.builder().pageSize(500).build());
//        log.info("=====周期购订单推送结束======");
//        return SUCCESS;
//    }
//
//}
