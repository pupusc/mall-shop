//package com.wanmi.sbc.job;
//
//import com.wanmi.sbc.common.util.StringUtil;
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
// * @program: sbc-background
// * @description: ERP发货状态同步接口
// * @author: 0F3685-wugongjiang
// * @create: 2021-02-18 10:49
// **/
//@JobHandler(value = "erpDeliveryStatusSyncJobHandler")
//@Component
//@Slf4j
//public class ERPDeliveryStatusSyncJobHandler  extends IJobHandler {
//
//    @Autowired
//    private ProviderTradeQueryProvider providerTradeQueryProvider;
//
//    @Override
//    public ReturnT<String> execute(String params) throws Exception {
//        log.info("=====订单发货状态更新开始======");
//        String[] paramterArray = params.split(",");
//        int size = 0;
//        String ptid = StringUtils.EMPTY;
//        try {
//            size = Integer.parseInt(paramterArray[0]);
//            if (paramterArray.length>1){
//                ptid = paramterArray[1];
//            }
//        } catch (RuntimeException e) {
//            log.error("调用ERP接口更新订单发货状态,参数错误,采用默认 200,{}", e);
//        }
//        providerTradeQueryProvider.batchSyncDeliveryStatus(ProviderTradeErpRequest.builder()
//                .pageSize(size)
//                .ptid(ptid)
//                .build());
//        log.info("=====订单发货状态更新结束======");
//        return SUCCESS;
//    }
//}
