package com.soybean.mall.job;

import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import io.seata.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步小程序订单到微信
 */

@JobHandler(value = "OrderDeliveryStatusSyncJobHandler")
@Component
@Slf4j
public class OrderDeliveryStatusSyncJobHandler  extends IJobHandler {

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====查询小程序订单并同步到微信start======");
        String[] paramterArray = params.split(",");
        int size = 0;
        String ptid = StringUtils.EMPTY;
        try {
            size = Integer.parseInt(paramterArray[0]);
            if (paramterArray.length>1){
                ptid = paramterArray[1];
            }
        } catch (RuntimeException e) {
            log.error("调用小程序订单并同步到微信,参数错误,采用默认 200,{}", e);
        }
        providerTradeQueryProvider.batchSyncDeliveryStatus(ProviderTradeErpRequest.builder()
                .pageSize(size)
                .ptid(ptid)
                .build());
        log.info("=====小程序订单并同步到微信end======");
        return SUCCESS;
    }
}