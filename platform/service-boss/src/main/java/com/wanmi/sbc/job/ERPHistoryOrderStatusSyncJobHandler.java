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
// * @date 2021年04月28日
// * @description 历史订单数据同步发货状态
// */
//@JobHandler(value = "historyOrderStatusSyncJobHandler")
//@Component
//@Slf4j
//public class ERPHistoryOrderStatusSyncJobHandler extends IJobHandler {
//
//    @Autowired
//    private ProviderTradeQueryProvider providerTradeQueryProvider;
//
//    @Override
//    public ReturnT<String> execute(String params) throws Exception {
//        log.info("=====历史订单数据同步发货状态开始======");
//        String[] paramterArray = params.split(",");
//        int pageNum = 1;
//        String startTime = StringUtils.EMPTY;
//        String endTime = StringUtils.EMPTY;
//        try {
//            pageNum = Integer.parseInt(paramterArray[0]);
//            if (paramterArray.length>1){
//                startTime = paramterArray[1];
//                endTime = paramterArray[2];
//            }
//        } catch (RuntimeException e) {
//            log.error("历史订单数据同步发货状态,参数错误,采用默认 100,{}", e);
//            pageNum = 1;
//        }
//
//        providerTradeQueryProvider.batchSyncHistoryOrderStatus(ProviderTradeErpRequest.builder()
//                .pageSize(100)
//                .pageNum(pageNum)
//                .startTime(startTime)
//                .endTime(endTime)
//                .build());
//
//        log.info("=====历史订单数据同步发货状态结束======");
//        return SUCCESS;
//    }
//}
