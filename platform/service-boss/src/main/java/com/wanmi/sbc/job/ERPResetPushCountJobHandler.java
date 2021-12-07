package com.wanmi.sbc.job;

import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
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
 * @author liuxia
 * @description 重置erp推送次数
 */
@JobHandler(value = "erpResetPushCountJobHandler")
@Component
@Slf4j
public class ERPResetPushCountJobHandler extends IJobHandler {

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("=====重置订单推送次数开始======");
        String[] paramterArray = param.split(",");
        int size = 0;
        String ptid = StringUtils.EMPTY;
        try {
            size = Integer.parseInt(paramterArray[0]);
            if (paramterArray.length>1){
                ptid = paramterArray[1];
            }
        } catch (RuntimeException e) {
            log.error("调用补偿ERP订单参数异常,参数错误,采用默认 200,{}", e);
        }
        providerTradeProvider.resetPushCount(ProviderTradeErpRequest.builder().ptid(ptid).pageSize(size).build());
        log.info("=====重置订单推送次数结束======");
        return SUCCESS;
    }

}