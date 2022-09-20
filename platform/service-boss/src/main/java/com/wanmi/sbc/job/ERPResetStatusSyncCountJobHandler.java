//package com.wanmi.sbc.job;
//
//import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
//import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//import io.seata.common.util.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author huqingjie
// * @date 2021年04月12日
// * @description 重置erp同步次数(未发货且同步次数达到3的)
// */
//@JobHandler(value = "erpResetStatusSyncCountJobHandler")
//@Component
//@Slf4j
//public class ERPResetStatusSyncCountJobHandler extends IJobHandler {
//
//    @Autowired
//    private ProviderTradeQueryProvider providerTradeQueryProvider;
//
//    @Override
//    public ReturnT<String> execute(String param) throws Exception {
//        log.info("=====重置订单扫描次数开始======");
//
//        providerTradeQueryProvider.batchResetScanCount(ProviderTradeErpRequest.builder().ptid(param).build());
//
//        log.info("=====重置订单扫描次数结束======");
//        return SUCCESS;
//    }
//
//}
